package ru.travelagency.service.sql;

import org.springframework.stereotype.Component;
import ru.travelagency.exception.BusinessRuleException;

import java.util.List;
import java.util.regex.Pattern;


@Component
public class SqlGuard {

    private static final List<String> FORBIDDEN_KEYWORDS = List.of(
            "INSERT", "UPDATE", "DELETE", "TRUNCATE", "DROP", "ALTER", "CREATE",
            "GRANT", "REVOKE", "MERGE", "REPLACE", "EXEC", "EXECUTE", "CALL",
            "COPY", "VACUUM", "REINDEX", "CLUSTER", "SET", "RESET",
            "LISTEN", "NOTIFY", "LOCK"
    );


    private static final List<String> FORBIDDEN_OBJECTS = List.of(
            "app_user", "pg_user", "pg_shadow", "pg_authid", "pg_roles", "pg_catalog"
    );

    private static final Pattern STARTS_WITH_SELECT = Pattern.compile("^\\s*(select)\\b", Pattern.CASE_INSENSITIVE);

    private static final Pattern COMMENT_LINE = Pattern.compile("--");
    private static final Pattern COMMENT_BLOCK = Pattern.compile("/\\*");


    public void validate(String sql) {
        if (sql == null || sql.isBlank()) {
            throw new BusinessRuleException("SQL must not be empty");
        }
        String trimmed = getTrimmed(sql);

        if (COMMENT_LINE.matcher(trimmed).find()
                || COMMENT_BLOCK.matcher(trimmed).find()) {
            throw new BusinessRuleException("SQL comments are not allowed");
        }

        if (!STARTS_WITH_SELECT.matcher(trimmed).find()) {
            throw new BusinessRuleException("Only SELECT statements are allowed");
        }

        for (String keyword : FORBIDDEN_KEYWORDS) {
            Pattern p = Pattern.compile("\\b" + keyword + "\\b", Pattern.CASE_INSENSITIVE);
            if (p.matcher(trimmed).find()) {
                throw new BusinessRuleException("Forbidden keyword: " + keyword);
            }
        }

        for (String obj : FORBIDDEN_OBJECTS) {
            Pattern p = Pattern.compile("\\b" + obj + "\\b", Pattern.CASE_INSENSITIVE);
            if (p.matcher(trimmed).find()) {
                throw new BusinessRuleException("Access to '" + obj + "' is forbidden");
            }
        }
    }

    private String getTrimmed(String sql) {
        String trimmed = sql.trim();

        if (trimmed.contains(";")) {
            String stripped = trimmed.endsWith(";")
                    ? trimmed.substring(0, trimmed.length() - 1)
                    : trimmed;
            if (stripped.contains(";")) {
                throw new BusinessRuleException("Multiple statements are not allowed (';' in the middle of SQL)");
            }
            trimmed = stripped;
        }
        return trimmed;
    }
}
