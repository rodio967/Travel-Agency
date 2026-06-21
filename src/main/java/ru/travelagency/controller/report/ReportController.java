package ru.travelagency.controller.report;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.dto.TouristDto;
import ru.travelagency.dto.report.*;
import ru.travelagency.enums.TouristType;
import ru.travelagency.service.report.ReportService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static ru.travelagency.security.PermissionCodes.REPORT_VIEW;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasAuthority('" + REPORT_VIEW + "')")
public class ReportController {

    private final ReportService reports;

    public ReportController(ReportService reports) {
        this.reports = reports;
    }

    // Туристы

    @GetMapping("/customs/{groupId}")
    public List<TouristDto> customsList(@PathVariable Long groupId,
                                        @RequestParam(required = false) TouristType type) {
        return reports.customsList(groupId, type);
    }

    @GetMapping("/hotels/{hotelId}/guests")
    public List<TouristDto> lodgingList(
            @PathVariable Long hotelId,
            @RequestParam(required = false) TouristType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reports.lodgingList(hotelId, type, from, to);
    }

    @GetMapping("/tourists/count")
    public Map<String, Long> countTouristsInPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) TouristType type) {
        return Map.of("count", reports.countTouristsInPeriod(from, to, type));
    }

    @GetMapping("/tourists/visits/{passportNumber}")
    public VisitStatsDto visitStats(@PathVariable String passportNumber) {
        return reports.visitStats(passportNumber);
    }

    @GetMapping("/tourists/category-share")
    public CategoryShareDto categoryShare(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reports.categoryShare(from, to);
    }

    //Гостиницы и экскурсии

    @GetMapping("/hotels/occupancy")
    public List<HotelOccupancyDto> hotelOccupancy(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reports.hotelOccupancy(from, to);
    }

    @GetMapping("/excursions/tourists-count")
    public Map<String, Long> touristsOnExcursions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return Map.of("count", reports.touristsOnExcursions(from, to));
    }

    @GetMapping("/excursions/top")
    public List<ExcursionPopularityDto> topExcursions(@RequestParam(defaultValue = "10") int limit) {
        return reports.mostPopularExcursions(limit);
    }

    @GetMapping("/agencies/top")
    public List<AgencyQualityDto> topAgencies(@RequestParam(defaultValue = "10") int limit) {
        return reports.topQualityAgencies(limit);
    }

    //Грузы и рейсы

    @GetMapping("/flights/{flightId}/load")
    public FlightLoadDto flightLoad(@PathVariable Long flightId) {
        return reports.flightLoad(flightId);
    }

    @GetMapping("/cargo/turnover")
    public CargoTurnoverDto cargoTurnover(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reports.cargoTurnover(from, to);
    }

    @GetMapping("/cargo/by-type")
    public List<CargoTypeStatsDto> cargoTypeStats() {
        return reports.cargoTypeStats();
    }

    @GetMapping("/flights/{flightId}/tourists")
    public List<FlightTouristInfoDto> flightTouristInfo(@PathVariable Long flightId) {
        return reports.flightTouristInfo(flightId);
    }

    //Финансы

    @GetMapping("/finance/group/{groupId}")
    public FinancialReportDto groupFinancialReport(
            @PathVariable Long groupId,
            @RequestParam(required = false) TouristType touristType) {
        return reports.groupFinancialReport(groupId, touristType);
    }

    @GetMapping("/finance/period")
    public FinancialReportDto periodFinancialReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reports.periodFinancialReport(from, to);
    }

    @GetMapping("/finance/profitability")
    public FinancialReportDto globalProfitability() {
        return reports.globalProfitability();
    }
}