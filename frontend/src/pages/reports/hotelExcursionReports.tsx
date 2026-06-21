import { TextField, Card, CardContent } from '@mui/material';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import { ReportRunner } from '../../components/reports/ReportRunner';
import { apiGet } from '../../api/client';
import type {
  AgencyQualityDto,
  ExcursionPopularityDto,
  HotelOccupancyDto,
} from '../../types/reports';

// Q5 — занятость гостиниц за период
export function HotelOccupancyReport() {
  return (
    <ReportRunner<{ from: string; to: string }, HotelOccupancyDto[]>
      title="Занятость гостиниц (Q5)"
      description="Гостиницы и количество расселённых туристов за период."
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
        apiGet<HotelOccupancyDto[]>('/api/reports/hotels/occupancy', {
          from: p.from,
          to: p.to,
        })
      }
      renderResult={(rows) => {
        const cols: GridColDef[] = [
          { field: 'hotelId', headerName: 'ID', width: 80 },
          { field: 'hotelName', headerName: 'Гостиница', flex: 1 },
          { field: 'roomsOccupied', headerName: 'Номеров', width: 110, type: 'number' },
          { field: 'touristsCount', headerName: 'Туристов', width: 110, type: 'number' },
        ];
        return (
          <Card>
            <CardContent className="p-0">
              <DataGrid
                rows={rows.map((r) => ({ id: r.hotelId, ...r }))}
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

// Q6 — туристов на экскурсиях за период
export function ExcursionsTouristsCountReport() {
  return (
    <ReportRunner<{ from: string; to: string }, { count: number }>
      title="Туристы на экскурсиях (Q6)"
      description="Уникальных туристов, заказавших экскурсии за период."
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
        apiGet<{ count: number }>(
          '/api/reports/excursions/tourists-count',
          { from: p.from, to: p.to },
        )
      }
      renderResult={(r) => (
        <Card>
          <CardContent className="p-6">
            <div className="text-sm text-gray-500">Уникальных туристов</div>
            <div className="text-4xl font-semibold text-gray-900 mt-1">
              {r.count.toLocaleString('ru-RU')}
            </div>
          </CardContent>
        </Card>
      )}
    />
  );
}

// Q7a — топ популярных экскурсий
export function TopExcursionsReport() {
  return (
    <ReportRunner<{ limit: number }, ExcursionPopularityDto[]>
      title="Самые популярные экскурсии (Q7a)"
      description="Рейтинг по количеству записавшихся туристов."
      defaultParams={{ limit: 10 }}
      renderParamsForm={(p, set) => (
        <TextField
          label="Сколько показать"
          type="number"
          size="small"
          value={p.limit}
          onChange={(e) => set({ ...p, limit: Number(e.target.value) || 10 })}
          inputProps={{ min: 1, max: 100 }}
          sx={{ width: 160 }}
        />
      )}
      fetchReport={(p) =>
        apiGet<ExcursionPopularityDto[]>('/api/reports/excursions/top', {
          limit: p.limit,
        })
      }
      renderResult={(rows) => {
        const cols: GridColDef[] = [
          { field: 'excursionId', headerName: 'ID', width: 80 },
          { field: 'excursionName', headerName: 'Экскурсия', flex: 1 },
          { field: 'agencyName', headerName: 'Агентство', flex: 1 },
          {
            field: 'bookingsCount',
            headerName: 'Записей',
            width: 120,
            type: 'number',
          },
        ];
        return (
          <Card>
            <CardContent className="p-0">
              <DataGrid
                rows={rows.map((r) => ({ id: r.excursionId, ...r }))}
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

// Q7b — топ агентств
export function TopAgenciesReport() {
  return (
    <ReportRunner<{ limit: number }, AgencyQualityDto[]>
      title="Лучшие агентства (Q7b)"
      description="Сортировка по рейтингу качества."
      defaultParams={{ limit: 10 }}
      renderParamsForm={(p, set) => (
        <TextField
          label="Сколько показать"
          type="number"
          size="small"
          value={p.limit}
          onChange={(e) => set({ ...p, limit: Number(e.target.value) || 10 })}
          inputProps={{ min: 1, max: 100 }}
          sx={{ width: 160 }}
        />
      )}
      fetchReport={(p) =>
        apiGet<AgencyQualityDto[]>('/api/reports/agencies/top', {
          limit: p.limit,
        })
      }
      renderResult={(rows) => {
        const cols: GridColDef[] = [
          { field: 'agencyId', headerName: 'ID', width: 80 },
          { field: 'agencyName', headerName: 'Агентство', flex: 1 },
          {
            field: 'qualityRating',
            headerName: 'Рейтинг',
            width: 110,
            type: 'number',
          },
          {
            field: 'excursionsCount',
            headerName: 'Экскурсий',
            width: 120,
            type: 'number',
          },
          {
            field: 'totalBookings',
            headerName: 'Записей',
            width: 110,
            type: 'number',
          },
        ];
        return (
          <Card>
            <CardContent className="p-0">
              <DataGrid
                rows={rows.map((r) => ({ id: r.agencyId, ...r }))}
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
