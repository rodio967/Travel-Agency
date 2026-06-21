import type {
  EntryType,
  FinancialCategory,
  FlightType,
  Gender,
} from './common';


export interface HotelDto {
  id?: number;
  name: string;
  address: string;
  stars?: number;
  pricePerNight?: number;
}


export interface FlightDto {
  id?: number;
  flightNumber: string;
  flightDate: string;
  flightType: FlightType;
  maxSeats?: number;
  maxCargoKg?: number;
  airportFees?: number;
}


export interface TouristGroupDto {
  id?: number;
  name: string;
  arrivalDate: string;
  departureDate: string;
  arrivalFlightId?: number;
  departureFlightId?: number;
}


export interface HotelBookingDto {
  id?: number;
  touristId: number;
  hotelId: number;
  roomNumber?: string;
  checkInDate: string;
  checkOutDate: string;
  cost?: number;
}


export interface VisaDto {
  id?: number;
  touristId: number;
  visaNumber: string;
  issueDate: string;
  expiryDate: string;
  cost?: number;
}


export interface ExcursionAgencyDto {
  id?: number;
  name: string;
  contact?: string;
  qualityRating?: number;
}


export interface ExcursionDto {
  id?: number;
  agencyId: number;
  name: string;
  description?: string;
  price?: number;
  excursionDate?: string;
}


export interface ExcursionBookingDto {
  id?: number;
  touristId: number;
  excursionId: number;
  bookedAt?: string;
}


export interface CargoDto {
  id?: number;
  touristId: number;
  flightId: number;
  marking?: string;
  cargoType?: string;
  placesCount?: number;
  weightKg?: number;
  volumetricWeight?: number;
  packagingCost?: number;
  insuranceCost?: number;
  totalCost?: number;
}


export interface FinancialEntryDto {
  id?: number;
  groupId: number;
  touristId?: number | null;
  entryType: EntryType;
  category: FinancialCategory;
  amount: number;
  entryDate: string;
  description?: string;
}


export interface AppUserDto {
  id?: number;
  username: string;
  password?: string;
  roleIds?: number[];
  roleNames?: string[];
  enabled: boolean;
}


interface TouristBase {
  id?: number;
  firstName: string;
  lastName: string;
  middleName?: string;
  gender: Gender;
  birthDate: string;
  passportNumber?: string;
  groupId: number;
  preferredHotelIds?: number[];
}

export interface RestTouristDto extends TouristBase {
  touristType: 'REST';
  preferredExcursionType?: string;
}

export interface ShopTouristDto extends TouristBase {
  touristType: 'SHOP';
  cargoLimitKg?: number;
  preferredCargoType?: string;
}

export interface ChildDto extends TouristBase {
  touristType: 'CHILD';
  parentId: number;
}

export type TouristDto = RestTouristDto | ShopTouristDto | ChildDto;
