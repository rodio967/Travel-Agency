package ru.travelagency.service.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.travelagency.dto.report.RawSqlRequest;
import ru.travelagency.dto.report.RawSqlResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class RawSqlService {

    private static final int DEFAULT_LIMIT = 1000;

    private static final int QUERY_TIMEOUT_SECONDS = 30;

    private final JdbcTemplate jdbcTemplate;
    private final SqlGuard guard;

    public RawSqlService(JdbcTemplate jdbcTemplate, SqlGuard guard) {
        this.jdbcTemplate = jdbcTemplate;
        this.guard = guard;
    }


    @Transactional(readOnly = true)
    public RawSqlResponse execute(RawSqlRequest request) {
        guard.validate(request.sql());

        int limit = request.limit() != null ? request.limit() : DEFAULT_LIMIT;
        String username = currentUsername();

        log.info("Raw SQL by user='{}', limit={}: {}", username, limit, request.sql());

        return jdbcTemplate.execute((Connection conn) -> {
            try (PreparedStatement ps = conn.prepareStatement(request.sql())) {
                ps.setMaxRows(limit + 1);
                ps.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
                ps.setFetchSize(Math.min(limit, 1000));

                try (ResultSet rs = ps.executeQuery()) {
                    return mapResultSet(rs, limit);
                }
            }
        });
    }


    private RawSqlResponse mapResultSet(ResultSet rs, int limit) throws java.sql.SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        List<String> columns = new ArrayList<>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            columns.add(meta.getColumnLabel(i));
        }

        List<Map<String, Object>> rows = new ArrayList<>();
        boolean truncated = false;
        while (rs.next()) {
            if (rows.size() >= limit) {
                truncated = true;
                break;
            }
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(columns.get(i - 1), rs.getObject(i));
            }
            rows.add(row);
        }
        return new RawSqlResponse(columns, rows, rows.size(), truncated);
    }

    private String currentUsername() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            return auth != null ? auth.getName() : "anonymous";
        } catch (Exception e) {
            return "unknown";
        }
    }
}
