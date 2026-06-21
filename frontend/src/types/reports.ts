import type { EntryType, FinancialCategory } from './common';

export interface VisitStatsDto {
  passportNumber: string;
  firstName: string;
  lastName: string;
  middleName: string | null;
  birthDate: string;
  visitCount: number;
  visits: VisitDetail[];
}

export interface VisitDetail {
  touristId: number;
  groupId: number;
  groupName: string;
  arrivalDate: string;
  departureDate: string;
  category: string;
  hotels: VisitHotelStay[];
  excursions: VisitExcursion[];
  cargo: VisitCargoItem[];
}

export interface VisitHotelStay {
  hotelId: number;
  hotelName: string;
  roomNumber: string | null;
  checkInDate: string | null;
  checkOutDate: string | null;
}

export interface VisitExcursion {
  excursionId: number;
  excursionName: string;
  excursionDate: string | null;
  agencyId: number;
  agencyName: string;
}

export interface VisitCargoItem {
  cargoId: number;
  marking: string | null;
  cargoType: string | null;
  placesCount: number | null;
  weightKg: number | null;
  totalCost: number | null;
}

export interface HotelOccupancyDto {
  hotelId: number;
  hotelName: string;
  roomsOccupied: number;
  touristsCount: number;
}

export interface ExcursionPopularityDto {
  excursionId: number;
  excursionName: string;
  agencyId: number;
  agencyName: string;
  bookingsCount: number;
}

export interface AgencyQualityDto {
  agencyId: number;
  agencyName: string;
  qualityRating: number | null;
  excursionsCount: number;
  totalBookings: number;
}

export interface FlightLoadDto {
  flightId: number;
  flightNumber: string;
  flightDate: string;
  cargoEntries: number;
  totalPlaces: number;
  totalWeightKg: number;
  totalVolumetricWeight: number;
}

export interface CargoTurnoverDto {
  totalPlaces: number;
  totalWeightKg: number;
  flightsCount: number;
  cargoFlightsCount: number;
  mixedFlightsCount: number;
  passengerFlightsCount: number;
}

export interface CargoTypeStatsDto {
  cargoType: string;
  entries: number;
  totalWeightKg: number;
  sharePercent: number;
}

export interface CategoryShareDto {
  restCount: number;
  shopCount: number;
  childCount: number;
  total: number;
  restPercent: number;
  shopPercent: number;
  childPercent: number;
}

export interface FlightTouristInfoDto {
  touristId: number;
  firstName: string;
  lastName: string;
  passportNumber: string | null;
  category: string;
  groupId: number;
  groupName: string;
  hotels: string[];
  cargoList: Array<{
    marking: string | null;
    cargoType: string | null;
    placesCount: number | null;
    weightKg: number | null;
    totalCost: number | null;
  }>;
}

export interface FinancialReportDto {
  totalIncome: number;
  totalExpense: number;
  balance: number;
  profitability: number | null;
  incomeByCategory: Array<{ category: FinancialCategory; amount: number }>;
  expenseByCategory: Array<{ category: FinancialCategory; amount: number }>;
}

export type { EntryType };