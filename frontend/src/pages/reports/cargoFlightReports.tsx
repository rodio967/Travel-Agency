import { TextField, Card, CardContent } from '@mui/material';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import { ReportRunner } from '../../components/reports/ReportRunner';
import { apiGet } from '../../api/client';
import { FlightSelect } from './EntitySelects';
import type {
  CargoTurnoverDto,
  CargoTypeStatsDto,
  FlightLoadDto,
  FlightTouristInfoDto,
} from '../../types/reports';

// Q8 — загрузка рейса
export function FlightLoadReport() {
  return (
    <ReportRunner<{ flightId: number }, FlightLoadDto>
      title="Загрузка рейса (Q8)"
      description="Места, вес и объёмный вес груза на конкретном рейсе."
      defaultParams={{ flightId: 1 }}
      renderParamsForm={(p, set) => (
        <FlightSelect
          value={p.flightId}
          onChange={(flightId) => set({ flightId })}
        />
      )}
      fetchReport={(p) =>
        apiGet<FlightLoadDto>(`/api/reports/flights/${p.flightId}/load`)
      }
      renderResult={(r) => (
        <Card>
          <CardContent className="p-6">
            <div className="text-lg font-semibold text-gray-900 mb-1">
              Рейс {r.flightNumber} — {r.flightDate}
            </div>
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 mt-4">
              <Stat label="Ведомостей" value={r.cargoEntries} />
              <Stat label="Мест" value={r.totalPlaces} />
              <Stat label="Вес, кг" value={r.totalWeightKg} />
              <Stat label="Объёмный, кг" value={r.totalVolumetricWeight} />
            </div>
          </CardContent>
        </Card>
      )}
    />
  );
}

// Q9 — грузооборот
export function CargoTurnoverReport() {
  return (
    <ReportRunner<{ from: string; to: string }, CargoTurnoverDto>
      title="Грузооборот (Q9)"
      description="Объём, вес груза и состав рейсов за период."
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
        apiGet<CargoTurnoverDto>('/api/reports/cargo/turnover', {
          from: p.from,
          to: p.to,
        })
      }
      renderResult={(r) => (
        <Card>
          <CardContent className="p-6">
            <div className="grid grid-cols-2 sm:grid-cols-3 gap-4">
              <Stat label="Мест груза" value={r.totalPlaces} />
              <Stat label="Вес, кг" value={r.totalWeightKg} />
              <Stat label="Рейсов всего" value={r.flightsCount} />
              <Stat label="Грузовых" value={r.cargoFlightsCount} color="#f59e0b" />
              <Stat label="Грузо-пассажирских" value={r.mixedFlightsCount} color="#0ea5e9" />
              <Stat label="Пассажирских" value={r.passengerFlightsCount} color="#10b981" />
            </div>
          </CardContent>
        </Card>
      )}
    />
  );
}

// Q12 — типы груза
export function CargoTypesReport() {
  return (
    <ReportRunner<Record<string, never>, CargoTypeStatsDto[]>
      title="Типы груза (Q12)"
      description="Удельная доля каждого типа в общем грузопотоке."
      defaultParams={{}}
      renderParamsForm={() => <div />}
      autoRun
      fetchReport={() => apiGet<CargoTypeStatsDto[]>('/api/reports/cargo/by-type')}
      renderResult={(rows) => {
        const cols: GridColDef[] = [
          { field: 'cargoType', headerName: 'Тип груза', flex: 1 },
          { field: 'entries', headerName: 'Ведомостей', width: 130, type: 'number' },
          { field: 'totalWeightKg', headerName: 'Вес, кг', width: 130, type: 'number' },
          { field: 'sharePercent', headerName: 'Доля, %', width: 110, type: 'number' },
        ];
        return (
          <Card>
            <CardContent className="p-0">
              <DataGrid
                rows={rows.map((r, i) => ({ id: i, ...r }))}
                columns={cols}
                autoHeight
                disableRowSelectionOnClick
                sx={{ border: 0 }}
              />
            </CardContent>
          </Card>
        );
      }}
    />
  );
}

// Q15 — туристы рейса
export function FlightTouristsReport() {
  return (
    <ReportRunner<{ flightId: number }, FlightTouristInfoDto[]>
      title="Туристы рейса (Q15)"
      description="Сводка по рейсу: туристы, гостиницы, груз, маркировка."
      defaultParams={{ flightId: 1 }}
      renderParamsForm={(p, set) => (
        <FlightSelect
          value={p.flightId}
          onChange={(flightId) => set({ flightId })}
        />
      )}
      fetchReport={(p) =>
        apiGet<FlightTouristInfoDto[]>(
          `/api/reports/flights/${p.flightId}/tourists`,
        )
      }
      renderResult={(rows) => {
        const cols: GridColDef[] = [
          { field: 'touristId', headerName: 'ID', width: 70 },
          { field: 'lastName', headerName: 'Фамилия', flex: 1, minWidth: 130 },
          { field: 'firstName', headerName: 'Имя', flex: 1, minWidth: 110 },
          { field: 'category', headerName: 'Категория', width: 110 },
          { field: 'passportNumber', headerName: 'Паспорт', width: 130 },
          { field: 'groupName', headerName: 'Группа', width: 150 },
          {
            field: 'hotels',
            headerName: 'Гостиницы',
            flex: 1,
            valueGetter: (value: string[]) => (value ?? []).join(', '),
          },
          {
            field: 'cargoList',
            headerName: 'Груз',
            flex: 1,
            valueGetter: (value: FlightTouristInfoDto['cargoList']) =>
              (value ?? [])
                .map((c) =>
                  [c.marking, c.cargoType, c.weightKg ? `${c.weightKg}кг` : null]
                    .filter(Boolean)
                    .join(' / '),
                )
                .join('; '),
          },
        ];
        return (
          <Card>
            <CardContent className="p-0">
              <DataGrid
                rows={rows.map((r) => ({ id: r.touristId, ...r }))}
                columns={cols}
                autoHeight
                disableRowSelectionOnClick
                sx={{ border: 0 }}
              />
            </CardContent>
          </Card>
        );
      }}
    />
  );
}

function Stat({
  label,
  value,
  color,
}: {
  label: string;
  value: number | string;
  color?: string;
}) {
  return (
    <div className="text-center p-3 rounded border border-gray-200">
      <div className="text-xs text-gray-500">{label}</div>
      <div
        className="text-2xl font-semibold mt-1"
        style={color ? { color } : undefined}
      >
        {typeof value === 'number' ? value.toLocaleString('ru-RU') : value}
      </div>
    </div>
  );
}
