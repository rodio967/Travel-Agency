package ru.travelagency.dto.report;

import java.util.List;
import java.util.Map;


public record RawSqlResponse(
        List<String> columns,
        List<Map<String, Object>> rows,
        int rowCount,
        boolean truncated
) {}
