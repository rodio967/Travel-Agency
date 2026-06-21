package ru.travelagency.security;

import java.util.List;


public final class PermissionCodes {

    //Туристы и группы
    public static final String TOURIST_READ = "TOURIST_READ";
    public static final String TOURIST_WRITE = "TOURIST_WRITE";
    public static final String GROUP_READ = "GROUP_READ";
    public static final String GROUP_WRITE = "GROUP_WRITE";
    public static final String VISA_READ = "VISA_READ";
    public static final String VISA_WRITE = "VISA_WRITE";

    //Гостиницы и расселение
    public static final String HOTEL_READ = "HOTEL_READ";
    public static final String HOTEL_WRITE = "HOTEL_WRITE";
    public static final String HOTEL_BOOKING_READ = "HOTEL_BOOKING_READ";
    public static final String HOTEL_BOOKING_WRITE = "HOTEL_BOOKING_WRITE";

    //Рейсы и груз
    public static final String FLIGHT_READ = "FLIGHT_READ";
    public static final String FLIGHT_WRITE = "FLIGHT_WRITE";
    public static final String CARGO_READ = "CARGO_READ";
    public static final String CARGO_WRITE = "CARGO_WRITE";

    //Экскурсии
    public static final String AGENCY_READ = "AGENCY_READ";
    public static final String AGENCY_WRITE = "AGENCY_WRITE";
    public static final String EXCURSION_READ = "EXCURSION_READ";
    public static final String EXCURSION_WRITE = "EXCURSION_WRITE";
    public static final String EXCURSION_BOOKING_READ = "EXCURSION_BOOKING_READ";
    public static final String EXCURSION_BOOKING_WRITE = "EXCURSION_BOOKING_WRITE";

    //Финансы
    public static final String FINANCE_READ = "FINANCE_READ";
    public static final String FINANCE_WRITE = "FINANCE_WRITE";

    //Аналитика и админ
    public static final String REPORT_VIEW = "REPORT_VIEW";
    public static final String RAW_SQL_EXEC = "RAW_SQL_EXEC";
    public static final String USER_MANAGE = "USER_MANAGE";
    public static final String ROLES_MANAGE = "ROLES_MANAGE";


    public static final List<PermissionDef> ALL = List.of(
            new PermissionDef(TOURIST_READ, "TOURIST", "Просмотр туристов"),
            new PermissionDef(TOURIST_WRITE, "TOURIST", "Создание/изменение/удаление туристов"),
            new PermissionDef(GROUP_READ, "TOURIST", "Просмотр групп"),
            new PermissionDef(GROUP_WRITE, "TOURIST", "Управление группами"),
            new PermissionDef(VISA_READ, "TOURIST", "Просмотр виз"),
            new PermissionDef(VISA_WRITE, "TOURIST", "Управление визами"),

            new PermissionDef(HOTEL_READ, "LOGISTICS", "Просмотр гостиниц"),
            new PermissionDef(HOTEL_WRITE, "LOGISTICS", "Управление гостиницами"),
            new PermissionDef(HOTEL_BOOKING_READ, "LOGISTICS", "Просмотр расселения"),
            new PermissionDef(HOTEL_BOOKING_WRITE, "LOGISTICS", "Управление расселением"),
            new PermissionDef(FLIGHT_READ, "LOGISTICS", "Просмотр рейсов"),
            new PermissionDef(FLIGHT_WRITE, "LOGISTICS", "Управление рейсами"),
            new PermissionDef(CARGO_READ, "LOGISTICS", "Просмотр груза"),
            new PermissionDef(CARGO_WRITE, "LOGISTICS", "Управление грузом"),

            new PermissionDef(AGENCY_READ, "SERVICES", "Просмотр агентств"),
            new PermissionDef(AGENCY_WRITE, "SERVICES", "Управление агентствами"),
            new PermissionDef(EXCURSION_READ, "SERVICES", "Просмотр экскурсий"),
            new PermissionDef(EXCURSION_WRITE, "SERVICES", "Управление экскурсиями"),
            new PermissionDef(EXCURSION_BOOKING_READ, "SERVICES", "Просмотр записей на экскурсии"),
            new PermissionDef(EXCURSION_BOOKING_WRITE, "SERVICES", "Управление записями на экскурсии"),
            new PermissionDef(FINANCE_READ, "SERVICES", "Просмотр финансовых записей"),
            new PermissionDef(FINANCE_WRITE, "SERVICES", "Управление финансами"),

            new PermissionDef(REPORT_VIEW, "ANALYTICS", "Просмотр отчётов"),
            new PermissionDef(RAW_SQL_EXEC, "ANALYTICS", "Выполнение произвольного SQL"),
            new PermissionDef(USER_MANAGE, "ADMIN", "Управление пользователями"),
            new PermissionDef(ROLES_MANAGE, "ADMIN", "Управление ролями")
    );

    public record PermissionDef(String code, String category, String description) {}

    private PermissionCodes() {}
}
