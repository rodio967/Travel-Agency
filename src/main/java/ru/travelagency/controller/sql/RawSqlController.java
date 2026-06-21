package ru.travelagency.controller.sql;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.travelagency.dto.report.RawSqlRequest;
import ru.travelagency.dto.report.RawSqlResponse;
import ru.travelagency.service.sql.RawSqlService;

import static ru.travelagency.security.PermissionCodes.RAW_SQL_EXEC;

@RestController
@RequestMapping("/api/raw-sql")
public class RawSqlController {

    private final RawSqlService service;

    public RawSqlController(RawSqlService service) {
        this.service = service;
    }

    @PostMapping("/query")
    @PreAuthorize("hasAuthority('" + RAW_SQL_EXEC + "')")
    public RawSqlResponse query(@RequestBody @Valid RawSqlRequest request) {
        return service.execute(request);
    }
}
