package ru.travelagency.service.report;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.travelagency.domain.*;
import ru.travelagency.dto.core.TouristDto;
import ru.travelagency.dto.report.*;
import ru.travelagency.enums.EntryType;
import ru.travelagency.enums.FinancialCategory;
import ru.travelagency.enums.TouristType;
import ru.travelagency.exception.BusinessRuleException;
import ru.travelagency.exception.NotFoundException;
import ru.travelagency.mapper.TouristMapper;
import ru.travelagency.repository.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;


@Service
@Transactional(readOnly = true)
public class ReportService {

    private final TouristRepository touristRepository;
    private final HotelBookingRepository hotelBookingRepository;
    private final ExcursionBookingRepository excursionBookingRepository;
    private final ExcursionAgencyRepository agencyRepository;
    private final CargoRepository cargoRepository;
    private final FinancialEntryRepository financialRepository;
    private final FlightRepository flightRepository;
    private final TouristMapper touristMapper;

    public ReportService(TouristRepository touristRepository,
                         HotelBookingRepository hotelBookingRepository,
                         ExcursionBookingRepository excursionBookingRepository,
                         ExcursionAgencyRepository agencyRepository,
                         CargoRepository cargoRepository,
                         FinancialEntryRepository financialRepository,
                         FlightRepository flightRepository,
                         TouristMapper touristMapper) {
        this.touristRepository = touristRepository;
        this.hotelBookingRepository = hotelBookingRepository;
        this.excursionBookingRepository = excursionBookingRepository;
        this.agencyRepository = agencyRepository;
        this.cargoRepository = cargoRepository;
        this.financialRepository = financialRepository;
        this.flightRepository = flightRepository;
        this.touristMapper = touristMapper;
    }

    //
    // Туристы
    //

    /**
     * Q1: список туристов для таможни
     */
    public List<TouristDto> customsList(Long groupId, TouristType type) {
        List<Tourist> entities = type == null
                ? touristRepository.findAllByGroupForCustoms(groupId)
                : touristRepository.findAllByGroupAndType(groupId, classOf(type));
        return entities.stream().map(touristMapper::toDto).toList();
    }

    /**
     * Q2: список туристов, расселённых в указанной гостинице
     */
    public List<TouristDto> lodgingList(Long hotelId, TouristType type, LocalDate from, LocalDate to) {
        validatePeriod(from, to);

        Class<? extends Tourist> typeClass = type == null ? null : classOf(type);
        List<Tourist> entities = hotelBookingRepository
                .findGuestsByHotel(hotelId, typeClass, from, to);
        return entities.stream().map(touristMapper::toDto).toList();
    }

    /**
     * Q3: количество туристов, побывавших в стране за период
     */
    public long countTouristsInPeriod(LocalDate from, LocalDate to, TouristType type) {
        validatePeriod(from, to);
        return type == null
                ? touristRepository.countByArrivalPeriod(from, to)
                : touristRepository.countByArrivalPeriodAndType(from, to, classOf(type));
    }

    /**
     * Q4: сведения о конкретном человеке — все его приезды
     */
    public VisitStatsDto visitStats(String passportNumber) {
        List<Tourist> visits = touristRepository.findAllByPassportNumber(passportNumber);
        if (visits.isEmpty()) {
            throw new NotFoundException("No tourists with passport: " + passportNumber);
        }

        Tourist first = visits.getFirst();
        List<VisitStatsDto.VisitDetail> details = visits.stream()
                .map(this::toVisitDetail)
                .toList();
        return new VisitStatsDto(
                passportNumber,
                first.getFirstName(),
                first.getLastName(),
                first.getMiddleName(),
                first.getBirthDate(),
                visits.size(),
                details);
    }


    private VisitStatsDto.VisitDetail toVisitDetail(Tourist t) {
        List<VisitStatsDto.HotelStay> hotels =
                hotelBookingRepository.findAllByTouristId(t.getId()).stream()
                        .map(b -> new VisitStatsDto.HotelStay(
                                b.getHotel().getId(),
                                b.getHotel().getName(),
                                b.getRoomNumber(),
                                b.getCheckInDate(),
                                b.getCheckOutDate()))
                        .toList();

        List<VisitStatsDto.ExcursionVisit> excursions =
                excursionBookingRepository.findAllByTouristId(t.getId()).stream()
                        .map(b -> new VisitStatsDto.ExcursionVisit(
                                b.getExcursion().getId(),
                                b.getExcursion().getName(),
                                b.getExcursion().getExcursionDate(),
                                b.getExcursion().getAgency().getId(),
                                b.getExcursion().getAgency().getName()))
                        .toList();

        List<VisitStatsDto.CargoItem> cargo =
                cargoRepository.findAllByTouristId(t.getId()).stream()
                        .map(c -> new VisitStatsDto.CargoItem(
                                c.getId(),
                                c.getMarking(),
                                c.getCargoType(),
                                c.getPlacesCount(),
                                c.getWeightKg(),
                                c.getTotalCost()))
                        .toList();

        return new VisitStatsDto.VisitDetail(
                t.getId(),
                t.getGroup().getId(),
                t.getGroup().getName(),
                t.getGroup().getArrivalDate(),
                t.getGroup().getDepartureDate(),
                t.getCategory(),
                hotels,
                excursions,
                cargo);
    }

    /**
     * Q14: процентное отношение REST/SHOP/CHILD за период
     */
    public CategoryShareDto categoryShare(LocalDate from, LocalDate to) {
        validatePeriod(from, to);
        List<Object[]> rows = (from == null || to == null)
                ? touristRepository.categoryShareTotal()
                : touristRepository.categoryShareInPeriod(from, to);

        long rest = 0, shop = 0, child = 0;
        for (Object[] row : rows) {
            // row[0] — Class<? extends Tourist>, row[1] — Long count

            Class<?> type = (Class<?>) row[0];
            long count = ((Number) row[1]).longValue();

            if (type == RestTourist.class) rest = count;
            else if (type == ShopTourist.class) shop = count;
            else if (type == Child.class) child = count;
        }

        long total = rest + shop + child;

        return new CategoryShareDto(
                rest, shop, child, total,
                percent(rest, total),
                percent(shop, total),
                percent(child, total));
    }

    //
    // Гостиницы
    //

    /**
     * Q5: список гостиниц с занятостью за период
     */
    public List<HotelOccupancyDto> hotelOccupancy(LocalDate from, LocalDate to) {
        validatePeriod(from, to);
        return hotelBookingRepository.occupancyInPeriod(from, to);
    }

    //
    // Экскурсии
    //

    /**
     * Q6: общее количество туристов, заказавших экскурсии за период
     */
    public long touristsOnExcursions(LocalDate from, LocalDate to) {
        validatePeriod(from, to);
        return excursionBookingRepository.countDistinctTouristsInPeriod(from, to);
    }

    /**
     * Q7a: топ самых популярных экскурсий
     */
    public List<ExcursionPopularityDto> mostPopularExcursions(int limit) {
        return excursionBookingRepository.mostPopularExcursions().stream()
                .limit(limit)
                .toList();
    }

    /**
     * Q7b: лучшие по качеству агентства
     */
    public List<AgencyQualityDto> topQualityAgencies(int limit) {
        return agencyRepository.topQualityAgencies().stream()
                .limit(limit)
                .toList();
    }

    //
    // Грузы и рейсы
    //

    /**
     * Q8: загрузка указанного рейса
     */
    public FlightLoadDto flightLoad(Long flightId) {
        FlightLoadDto dto = cargoRepository.getFlightLoad(flightId);
        if (dto == null) throw NotFoundException.of(Flight.class, flightId);
        return dto;
    }

    /**
     * Q9: статистика грузооборота за период
     */
    public CargoTurnoverDto cargoTurnover(LocalDate from, LocalDate to) {
        validatePeriod(from, to);
        PeriodTotals totals = cargoRepository.sumPlacesAndWeightInPeriod(from, to);

        long totalPlaces = totals.totalPlaces() != null ? totals.totalPlaces() : 0L;
        BigDecimal totalWeight = totals.totalWeight() != null
                ? totals.totalWeight()
                : BigDecimal.ZERO;

        long cargo = 0;
        long mixed = 0;
        long passenger = 0;
        for (Object[] row : cargoRepository.flightTypeBreakdownInPeriod(from, to)) {
            // row[0] — FlightType; row[1] — count
            String type = row[0].toString();
            long count = ((Number) row[1]).longValue();

            switch (type) {
                case "CARGO" -> cargo = count;
                case "MIXED" -> mixed = count;
                case "PASSENGER" -> passenger = count;
            }
        }
        long totalFlights = cargo + mixed + passenger;
        return new CargoTurnoverDto(totalPlaces, totalWeight,
                totalFlights, cargo, mixed, passenger);
    }

    /**
     * Q12: статистика по типам груза с долями в %
     */
    public List<CargoTypeStatsDto> cargoTypeStats() {
        BigDecimal total = cargoRepository.totalCargoWeight();
        List<Object[]> rows = cargoRepository.statsByCargoType();
        return rows.stream()
                .map(row -> {
                    String type = (String) row[0];
                    long entries = ((Number) row[1]).longValue();

                    BigDecimal weight = (BigDecimal) row[2];
                    BigDecimal share = total.signum() == 0
                            ? BigDecimal.ZERO
                            : weight.multiply(BigDecimal.valueOf(100))
                            .divide(total, 2, RoundingMode.HALF_UP);
                    return new CargoTypeStatsDto(type, entries, weight, share);
                })
                .toList();
    }

    /**
     * Q15: сведения о туристах указанного рейса
     */
    public List<FlightTouristInfoDto> flightTouristInfo(Long flightId) {
        if (!flightRepository.existsById(flightId)) {
            throw NotFoundException.of(Flight.class, flightId);
        }

        List<Tourist> tourists = flightRepository.findTouristsByFlight(flightId);

        return tourists.stream()
                .map(t -> toFlightTouristInfo(t, flightId))
                .toList();
    }


    private FlightTouristInfoDto toFlightTouristInfo(Tourist t, Long flightId) {
        List<String> hotels = hotelBookingRepository.findAllByTouristId(t.getId()).stream()
                .map(this::formatHotelStay)
                .toList();

        List<FlightTouristInfoDto.CargoSummary> cargoList =
                cargoRepository.findAllByTouristId(t.getId()).stream()
                        .filter(c -> c.getFlight().getId().equals(flightId))
                        .map(this::toCargoSummary)
                        .toList();

        return new FlightTouristInfoDto(
                t.getId(),
                t.getFirstName(),
                t.getLastName(),
                t.getPassportNumber(),
                t.getCategory(),
                t.getGroup().getId(),
                t.getGroup().getName(),
                hotels,
                cargoList);
    }


    private String formatHotelStay(HotelBooking b) {
        return b.getHotel().getName() + (b.getRoomNumber() != null ? " #" + b.getRoomNumber() : "");
    }

    private FlightTouristInfoDto.CargoSummary toCargoSummary(Cargo c) {
        return new FlightTouristInfoDto.CargoSummary(
                c.getMarking(),
                c.getCargoType(),
                c.getPlacesCount(),
                c.getWeightKg(),
                c.getTotalCost());
    }

    //
    // Финансы
    //

    /**
     * Q10: полный финансовый отчёт по группе
     */
    public FinancialReportDto groupFinancialReport(Long groupId, TouristType touristType) {
        if (touristType == null) {
            BigDecimal income = financialRepository.sumByGroupAndType(groupId, EntryType.INCOME);
            BigDecimal expense = financialRepository.sumByGroupAndType(groupId, EntryType.EXPENSE);
            return buildReport(income, expense,
                    financialRepository.sumByGroupGroupedByCategory(groupId, EntryType.INCOME),
                    financialRepository.sumByGroupGroupedByCategory(groupId, EntryType.EXPENSE));
        }


        Class<? extends Tourist> touristClass = classOf(touristType);

        BigDecimal income = financialRepository.sumByGroupTypeAndTouristType(groupId, EntryType.INCOME, touristClass);
        BigDecimal expense = financialRepository.sumByGroupTypeAndTouristType(groupId, EntryType.EXPENSE, touristClass);

        return buildReport(income, expense,
                financialRepository.sumByGroupCategoryAndTouristType(groupId, EntryType.INCOME, touristClass),
                financialRepository.sumByGroupCategoryAndTouristType(groupId, EntryType.EXPENSE, touristClass));
    }

    /**
     * Q11: доходы/расходы за период
     */
    public FinancialReportDto periodFinancialReport(LocalDate from, LocalDate to) {
        validatePeriod(from, to);

        BigDecimal income = financialRepository.sumByPeriodAndType(from, to, EntryType.INCOME);
        BigDecimal expense = financialRepository.sumByPeriodAndType(from, to, EntryType.EXPENSE);
        return buildReport(income, expense,
                financialRepository.sumByPeriodGroupedByCategory(from, to, EntryType.INCOME),
                financialRepository.sumByPeriodGroupedByCategory(from, to, EntryType.EXPENSE));
    }

    /**
     * Q13: глобальная рентабельность представительства
     */
    public FinancialReportDto globalProfitability() {
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;

        for (Object[] row : financialRepository.globalIncomeExpense()) {
            EntryType type = (EntryType) row[0];
            BigDecimal sum = (BigDecimal) row[1];

            if (type == EntryType.INCOME) income = sum;
            else if (type == EntryType.EXPENSE) expense = sum;
        }

        return buildReport(income, expense, List.of(), List.of());
    }



    /**
     * Сборка финансового отчёта из посчитанных сумм.
     */
    private FinancialReportDto buildReport(BigDecimal income, BigDecimal expense,
                                           List<Object[]> incomeRows,
                                           List<Object[]> expenseRows) {
        BigDecimal balance = income.subtract(expense);
        BigDecimal profitability = expense.signum() == 0
                ? null
                : income.divide(expense, 4, RoundingMode.HALF_UP);

        return new FinancialReportDto(
                income, expense, balance, profitability,
                toCategoryAmounts(incomeRows),
                toCategoryAmounts(expenseRows));
    }

    private List<FinancialReportDto.CategoryAmount> toCategoryAmounts(List<Object[]> rows) {
        return rows.stream()
                .map(row -> new FinancialReportDto.CategoryAmount(
                        (FinancialCategory) row[0],
                        (BigDecimal) row[1]))
                .toList();
    }


    private Class<? extends Tourist> classOf(TouristType type) {
        return switch (type) {
            case REST -> RestTourist.class;
            case SHOP -> ShopTourist.class;
            case CHILD -> Child.class;
        };
    }

    private BigDecimal percent(long value, long total) {
        if (total == 0) return BigDecimal.ZERO;

        return BigDecimal.valueOf(value)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
    }


    private void validatePeriod(LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new BusinessRuleException("Invalid period: 'from' (" + from + ") must not be after 'to' (" + to + ")");
        }
    }
}