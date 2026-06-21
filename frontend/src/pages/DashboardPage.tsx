import { Card, CardContent, CircularProgress, Alert } from '@mui/material';
import {
  Group as GroupIcon,
  Person as PersonIcon,
  Hotel as HotelIcon,
  Flight as FlightIcon,
  AccountBalance as FinanceIcon,
} from '@mui/icons-material';
import { useQuery } from '@tanstack/react-query';
import { apiGet, apiGetPage, extractMessage } from '../api/client';
import type { Page } from '../types/common';
import { useAuth } from '../auth/AuthContext';

interface StatCardProps {
  label: string;
  value: number | string;
  icon: React.ReactNode;
  color: string;
}

function StatCard({ label, value, icon, color }: StatCardProps) {
  return (
    <Card>
      <CardContent className="flex items-center gap-4 p-5">
        <div className={`w-12 h-12 rounded-lg flex items-center justify-center ${color}`}>
          {icon}
        </div>
        <div>
          <div className="text-sm text-gray-500">{label}</div>
          <div className="text-2xl font-semibold text-gray-900">{value}</div>
        </div>
      </CardContent>
    </Card>
  );
}

function useTotal(url: string, enabled: boolean) {
  return useQuery({
    queryKey: ['total', url],
    queryFn: () => apiGetPage<unknown>(url, { page: 0, size: 1 }),
    select: (page: Page<unknown>) => page.totalElements,
    enabled,
  });
}

export function DashboardPage() {
  const { hasPermission } = useAuth();

  const canSeeGroups = hasPermission('GROUP_READ');
  const canSeeTourists = hasPermission('TOURIST_READ');
  const canSeeHotels = hasPermission('HOTEL_READ');
  const canSeeFlights = hasPermission('FLIGHT_READ');
  const canSeeReports = hasPermission('REPORT_VIEW');

  const groups = useTotal('/api/groups', canSeeGroups);
  const tourists = useTotal('/api/tourists', canSeeTourists);
  const hotels = useTotal('/api/hotels', canSeeHotels);
  const flights = useTotal('/api/flights', canSeeFlights);

  const profitability = useQuery({
    queryKey: ['profitability'],
    queryFn: () =>
      apiGet<{ totalIncome: number; totalExpense: number; balance: number }>(
        '/api/reports/finance/profitability',
      ),
    enabled: canSeeReports,
  });

  const anyError =
    (canSeeGroups && groups.error) ||
    (canSeeTourists && tourists.error) ||
    (canSeeHotels && hotels.error) ||
    (canSeeFlights && flights.error) ||
    (canSeeReports && profitability.error);

  if (anyError) {
    return <Alert severity="error">{extractMessage(anyError)}</Alert>;
  }

  const value = (q: ReturnType<typeof useTotal>) =>
    q.isLoading ? <CircularProgress size={20} /> : (q.data ?? 0);

  const hasAnyWidget =
    canSeeGroups || canSeeTourists || canSeeHotels || canSeeFlights || canSeeReports;

  return (
    <div>
      <h1 className="text-2xl font-semibold text-gray-900 mb-1">Главная</h1>
      <p className="text-gray-500 mb-6">Сводка по системе</p>

      {!hasAnyWidget && (
        <Card>
          <CardContent className="p-8 text-center text-gray-500">
            На главной нечего показывать — у вашей роли нет прав на просмотр
            данных. Обратитесь к администратору.
          </CardContent>
        </Card>
      )}

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {canSeeGroups && (
          <StatCard
            label="Групп"
            value={value(groups)}
            icon={<GroupIcon className="text-sky-600" />}
            color="bg-sky-100"
          />
        )}
        {canSeeTourists && (
          <StatCard
            label="Туристов"
            value={value(tourists)}
            icon={<PersonIcon className="text-emerald-600" />}
            color="bg-emerald-100"
          />
        )}
        {canSeeHotels && (
          <StatCard
            label="Гостиниц"
            value={value(hotels)}
            icon={<HotelIcon className="text-amber-600" />}
            color="bg-amber-100"
          />
        )}
        {canSeeFlights && (
          <StatCard
            label="Рейсов"
            value={value(flights)}
            icon={<FlightIcon className="text-violet-600" />}
            color="bg-violet-100"
          />
        )}
      </div>

      {canSeeReports && (
        <Card className="mt-6">
          <CardContent className="p-6">
            <div className="flex items-center gap-3 mb-4">
              <FinanceIcon className="text-sky-600" />
              <h2 className="text-lg font-semibold text-gray-900">Финансовая сводка</h2>
            </div>
            {profitability.isLoading ? (
              <CircularProgress size={20} />
            ) : profitability.data ? (
              <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
                <div>
                  <div className="text-sm text-gray-500">Доход</div>
                  <div className="text-xl font-semibold text-emerald-600">
                    {profitability.data.totalIncome?.toLocaleString('ru-RU') ?? 0}
                  </div>
                </div>
                <div>
                  <div className="text-sm text-gray-500">Расход</div>
                  <div className="text-xl font-semibold text-rose-600">
                    {profitability.data.totalExpense?.toLocaleString('ru-RU') ?? 0}
                  </div>
                </div>
                <div>
                  <div className="text-sm text-gray-500">Баланс</div>
                  <div
                    className={`text-xl font-semibold ${
                      (profitability.data.balance ?? 0) >= 0
                        ? 'text-emerald-600'
                        : 'text-rose-600'
                    }`}
                  >
                    {profitability.data.balance?.toLocaleString('ru-RU') ?? 0}
                  </div>
                </div>
              </div>
            ) : null}
          </CardContent>
        </Card>
      )}
    </div>
  );
}
