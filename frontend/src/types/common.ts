
export type Gender = 'MALE' | 'FEMALE';

export type TouristType = 'REST' | 'SHOP' | 'CHILD';

export type FlightType = 'PASSENGER' | 'CARGO' | 'MIXED';

export type EntryType = 'INCOME' | 'EXPENSE';

export type FinancialCategory =
  | 'HOTEL'
  | 'EXCURSION'
  | 'VISA'
  | 'FLIGHT_FEES'
  | 'AIRPORT_LOADING'
  | 'AIRPORT_DISPATCH'
  | 'CARGO_STORAGE'
  | 'OFFICE'
  | 'UNFORESEEN'
  | 'TOUR_PAYMENT'
  | 'CARGO_FEE'
  | 'OTHER';

export type UserRole = 'ADMIN' | 'MANAGER' | 'OPERATOR';


export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}


export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
}


export interface CurrentUser {
  username: string;
  roles: string[];
  permissions: string[];
}
