import { Routes, Route } from 'react-router-dom';
import { ProtectedRoute } from './auth/ProtectedRoute';
import { Layout } from './components/layout/Layout';
import { LoginPage } from './pages/LoginPage';
import { DashboardPage } from './pages/DashboardPage';
import { PlaceholderPage } from './pages/PlaceholderPage';
import { HotelsPage } from './pages/HotelsPage';
import { FlightsPage } from './pages/FlightsPage';
import { GroupsPage } from './pages/GroupsPage';
import { ExcursionAgenciesPage } from './pages/ExcursionAgenciesPage';
import { ExcursionsPage } from './pages/ExcursionsPage';
import { FinancialEntriesPage } from './pages/FinancialEntriesPage';
import { TouristsPage } from './pages/TouristsPage';
import { VisasPage } from './pages/VisasPage';
import { CargoPage } from './pages/CargoPage';
import { HotelBookingsPage } from './pages/HotelBookingsPage';
import { ExcursionBookingsPage } from './pages/ExcursionBookingsPage';
import { ReportsPage } from './pages/ReportsPage';
import { SqlConsolePage } from './pages/SqlConsolePage';
import { UsersPage } from './pages/UsersPage';
import { RolesPage } from './pages/RolesPage';
import {
  CustomsListReport,
  HotelGuestsReport,
  TouristsCountReport,
  VisitStatsReport,
  CategoryShareReport,
} from './pages/reports/touristReports';
import {
  HotelOccupancyReport,
  ExcursionsTouristsCountReport,
  TopExcursionsReport,
  TopAgenciesReport,
} from './pages/reports/hotelExcursionReports';
import {
  FlightLoadReport,
  CargoTurnoverReport,
  CargoTypesReport,
  FlightTouristsReport,
} from './pages/reports/cargoFlightReports';
import {
  FinanceGroupReport,
  FinancePeriodReport,
  FinanceProfitabilityReport,
} from './pages/reports/financeReports';

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />

      <Route
        element={
          <ProtectedRoute>
            <Layout />
          </ProtectedRoute>
        }
      >
        <Route index element={<DashboardPage />} />

        {}
        <Route path="/hotels"             element={<HotelsPage />} />
        <Route path="/flights"            element={<FlightsPage />} />
        <Route path="/groups"             element={<GroupsPage />} />
        <Route path="/excursions"         element={<ExcursionsPage />} />
        <Route path="/excursion-agencies" element={<ExcursionAgenciesPage />} />
        <Route path="/finance"            element={<FinancialEntriesPage />} />

        {}
        <Route path="/tourists"           element={<TouristsPage />} />
        <Route path="/visas"              element={<VisasPage />} />
        <Route path="/cargo"              element={<CargoPage />} />
        <Route path="/hotel-bookings"     element={<HotelBookingsPage />} />
        <Route path="/excursion-bookings" element={<ExcursionBookingsPage />} />

        {}
        <Route path="/reports"                            element={<ReportsPage />} />
        <Route path="/reports/customs"                    element={<CustomsListReport />} />
        <Route path="/reports/hotels-guests"              element={<HotelGuestsReport />} />
        <Route path="/reports/tourists-count"             element={<TouristsCountReport />} />
        <Route path="/reports/tourist-visits"             element={<VisitStatsReport />} />
        <Route path="/reports/category-share"             element={<CategoryShareReport />} />
        <Route path="/reports/hotels-occupancy"           element={<HotelOccupancyReport />} />
        <Route path="/reports/excursions-tourists"        element={<ExcursionsTouristsCountReport />} />
        <Route path="/reports/top-excursions"             element={<TopExcursionsReport />} />
        <Route path="/reports/top-agencies"               element={<TopAgenciesReport />} />
        <Route path="/reports/flight-load"                element={<FlightLoadReport />} />
        <Route path="/reports/cargo-turnover"             element={<CargoTurnoverReport />} />
        <Route path="/reports/cargo-types"                element={<CargoTypesReport />} />
        <Route path="/reports/flight-tourists"            element={<FlightTouristsReport />} />
        <Route path="/reports/finance-group"              element={<FinanceGroupReport />} />
        <Route path="/reports/finance-period"             element={<FinancePeriodReport />} />
        <Route path="/reports/finance-profitability"      element={<FinanceProfitabilityReport />} />

        {}
        <Route
          path="/sql-console"
          element={
            <ProtectedRoute requiredPermission="RAW_SQL_EXEC">
              <SqlConsolePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/users"
          element={
            <ProtectedRoute requiredPermission="USER_MANAGE">
              <UsersPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/roles"
          element={
            <ProtectedRoute requiredPermission="ROLES_MANAGE">
              <RolesPage />
            </ProtectedRoute>
          }
        />

        <Route path="*" element={<PlaceholderPage title="404" description="Страница не найдена." />} />
      </Route>
    </Routes>
  );
}
