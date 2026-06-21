import { TextField, Card, CardContent } from '@mui/material';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import { ReportRunner } from '../../components/reports/ReportRunner';
import { apiGet } from '../../api/client';
import { GroupSelect } from './EntitySelects';
import type { FinancialReportDto } from '../../types/reports';

const CATEGORY_LABELS: Record<string, string> = {
  HOTEL: 'Гостиница',
  EXCURSION: 'Экскурсия',
  VISA: 'Виза',
  FLIGHT_FEES: 'Взлёт-посадка',
  AIRPORT_LOADING: 'Загрузка/разгрузка',
  AIRPORT_DISPATCH: 'Диспетчерские',
  CARGO_STORAGE: 'Хранение груза',
  OFFICE: 'Офис',
  UNFORESEEN: 'Непредвиденные',
  TOUR_PAYMENT: 'Оплата тура',
  CARGO_FEE: 'Оплата груза',
  OTHER: 'Прочее',
};

function FinancialReportView({ r }: { r: FinancialReportDto }) {
  const cols: GridColDef[] = [
    {
      field: 'category',
      headerName: 'Категория',
      flex: 1,
      valueGetter: (value: string) => CATEGORY_LABELS[value] ?? value,
    },
    {
      field: 'amount',
      headerName: 'Сумма',
      width: 160,
      type: 'number',
    },
  ];

  return (
    <div className="flex flex-col gap-4">
      {/* Сводка */}
      <Card>
        <CardContent className="p-6">
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
            <div className="text-center p-3 rounded border border-gray-200">
              <div className="text-xs text-gray-500">Доход</div>
              <div className="text-2xl font-semibold mt-1 text-emerald-600">
                {r.totalIncome?.toLocaleString('ru-RU') ?? 0}
              </div>
            </div>
            <div className="text-center p-3 rounded border border-gray-200">
              <div className="text-xs text-gray-500">Расход</div>
              <div className="text-2xl font-semibold mt-1 text-rose-600">
                {r.totalExpense?.toLocaleString('ru-RU') ?? 0}
              </div>
            </div>
            <div className="text-center p-3 rounded border border-gray-200">
              <div className="text-xs text-gray-500">Баланс</div>
              <div
                className={`text-2xl font-semibold mt-1 ${
                  (r.balance ?? 0) >= 0 ? 'text-emerald-600' : 'text-rose-600'
                }`}
              >
                {r.balance?.toLocaleString('ru-RU') ?? 0}
              </div>
            </div>
            <div className="text-center p-3 rounded border border-gray-200">
              <div className="text-xs text-gray-500">Рентабельность</div>
              <div className="text-2xl font-semibold mt-1 text-sky-600">
                {r.profitability != null
                  ? `×${r.profitability.toFixed(2)}`
                  : '—'}
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Доходы по категориям */}
      {r.incomeByCategory.length > 0 && (
        <Card>
          <CardContent className="p-4">
            <div className="font-medium text-gray-900 mb-2">Доходы по категориям</div>
            <DataGrid
              rows={r.incomeByCategory.map((c, i) => ({ id: i, ...c }))}
              columns={cols}
              autoHeight
              hideFooter
              disableRowSelectionOnClick
              sx={{ border: 0 }}
            />
          </CardContent>
        </Card>
      )}

      {/* Расходы по категориям */}
      {r.expenseByCategory.length > 0 && (
        <Card>
          <CardContent className="p-4">
            <div className="font-medium text-gray-900 mb-2">Расходы по категориям</div>
            <DataGrid
              rows={r.expenseByCategory.map((c, i) => ({ id: i, ...c }))}
              columns={cols}
              autoHeight
              hideFooter
              disableRowSelectionOnClick
              sx={{ border: 0 }}
            />
          </CardContent>
        </Card>
      )}
    </div>
  );
}

// Q10 — финансы по группе
export function FinanceGroupReport() {
  return (
    <ReportRunner<{ groupId: number; touristType: string }, FinancialReportDto>
      title="Финансовый отчёт по группе (Q10)"
      description="Доходы, расходы, баланс и рентабельность одной группы."
      defaultParams={{ groupId: 1, touristType: '' }}
      renderParamsForm={(p, set) => (
        <div className="flex gap-3 items-center flex-wrap">
          <GroupSelect
            value={p.groupId}
            onChange={(groupId) => set({ ...p, groupId })}
          />
          <TextField
            label="Категория туристов"
            select
            size="small"
            value={p.touristType}
            onChange={(e) => set({ ...p, touristType: e.target.value })}
            sx={{ width: 220 }}
            SelectProps={{ native: true }}
            InputLabelProps={{ shrink: true }}
          >
            <option value="">В целом (все)</option>
            <option value="REST">Отдыхающие (REST)</option>
            <option value="SHOP">Shop-туристы (SHOP)</option>
            <option value="CHILD">Дети (CHILD)</option>
          </TextField>
        </div>
      )}
      fetchReport={(p) =>
        apiGet<FinancialReportDto>(
          `/api/reports/finance/group/${p.groupId}`,
          p.touristType ? { touristType: p.touristType } : undefined,
        )
      }
      renderResult={(r) => <FinancialReportView r={r} />}
    />
  );
}

// Q11 — финансы за период
export function FinancePeriodReport() {
  return (
    <ReportRunner<{ from: string; to: string }, FinancialReportDto>
      title="Финансы за период (Q11)"
      description="Доходы и расходы за указанный период с разбивкой по категориям."
      defaultParams={{ from: '2026-01-01', to: '2026-12-31' }}
      renderParamsForm={(p, set) => (
        <div className="flex gap-3">
          <TextField
            label="С"
            type="date"
            size="small"
            value={p.from}
            onChange={(e) => set({ ...p, from: e.target.value })}
            InputLabelProps={{ shrink: true }}
            required
          />
          <TextField
            label="По"
            type="date"
            size="small"
            value={p.to}
            onChange={(e) => set({ ...p, to: e.target.value })}
            InputLabelProps={{ shrink: true }}
            required
          />
        </div>
      )}
      fetchReport={(p) =>
        apiGet<FinancialReportDto>('/api/reports/finance/period', {
          from: p.from,
          to: p.to,
        })
      }
      renderResult={(r) => <FinancialReportView r={r} />}
    />
  );
}

// Q13 — глобальная рентабельность
export function FinanceProfitabilityReport() {
  return (
    <ReportRunner<Record<string, never>, FinancialReportDto>
      title="Глобальная рентабельность (Q13)"
      description="Доходы, расходы и рентабельность представительства за всё время."
      defaultParams={{}}
      renderParamsForm={() => <div />}
      autoRun
      fetchReport={() =>
        apiGet<FinancialReportDto>('/api/reports/finance/profitability')
      }
      renderResult={(r) => <FinancialReportView r={r} />}
    />
  );
}
