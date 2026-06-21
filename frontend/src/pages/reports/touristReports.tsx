import {
  TextField,
  MenuItem,
  Card,
  CardContent,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Chip,
  Divider,
} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import { ReportRunner } from '../../components/reports/ReportRunner';
import { apiGet } from '../../api/client';
import { GroupSelect, HotelSelect } from './EntitySelects';
import type { TouristDto } from '../../types/dto';
import type { TouristType } from '../../types/common';
import type {
  CategoryShareDto,
  VisitStatsDto,
  VisitDetail,
} from '../../types/reports';

const TYPE_OPTIONS: Array<{ value: TouristType | ''; label: string }> = [
  { value: '', label: 'Все' },
  { value: 'REST', label: 'Отдыхающие' },
  { value: 'SHOP', label: 'Шоп-туры' },
  { value: 'CHILD', label: 'Дети' },
];

const TOURIST_COLUMNS: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 70 },
  { field: 'touristType', headerName: 'Тип', width: 90 },
  { field: 'lastName', headerName: 'Фамилия', flex: 1, minWidth: 130 },
  { field: 'firstName', headerName: 'Имя', flex: 1, minWidth: 110 },
  { field: 'passportNumber', headerName: 'Паспорт', width: 130 },
  { field: 'groupId', headerName: 'Группа', width: 90, type: 'number' },
];

// Q1 — список группы для таможни
export function CustomsListReport() {
  return (
    <ReportRunner<{ groupId: number; type: TouristType | '' }, TouristDto[]>
      title="Список для таможни (Q1)"
      description="Туристы указанной группы. Опционально — только одной категории."
      defaultParams={{ groupId: 1, type: '' }}
      renderParamsForm={(p, set) => (
        <div className="flex gap-3 flex-wrap">
          <GroupSelect
            value={p.groupId}
            onChange={(groupId) => set({ ...p, groupId })}
          />
          <TextField
            label="Категория"
            select
            size="small"
            value={p.type}
            onChange={(e) => set({ ...p, type: e.target.value as TouristType | '' })}
            sx={{ width: 200 }}
          >
            {TYPE_OPTIONS.map((o) => (
              <MenuItem key={o.value} value={o.value}>
                {o.label}
              </MenuItem>
            ))}
          </TextField>
        </div>
      )}
      fetchReport={(p) =>
        apiGet<TouristDto[]>('/api/reports/customs/' + p.groupId, {
          ...(p.type ? { type: p.type } : {}),
        })
      }
      renderResult={(rows) => (
        <Card>
          <CardContent className="p-0">
            <DataGrid
              rows={rows.map((r) => ({ id: r.id, ...r }))}
              columns={TOURIST_COLUMNS}
              autoHeight
              disableRowSelectionOnClick
              sx={{ border: 0 }}
            />
          </CardContent>
        </Card>
      )}
    />
  );
}

// Q2 — туристы, расселённые в гостинице
export function HotelGuestsReport() {
  return (
    <ReportRunner<
      { hotelId: number; type: TouristType | ''; from: string; to: string },
      TouristDto[]
    >
      title="Расселение в гостинице (Q2)"
      description="Туристы, расселённые в указанной гостинице. Опционально — категория и период."
      defaultParams={{ hotelId: 1, type: '', from: '', to: '' }}
      renderParamsForm={(p, set) => (
        <div className="flex gap-3 flex-wrap">
          <HotelSelect
            value={p.hotelId}
            onChange={(hotelId) => set({ ...p, hotelId })}
          />
          <TextField
            label="Категория"
            select
            size="small"
            value={p.type}
            onChange={(e) => set({ ...p, type: e.target.value as TouristType | '' })}
            sx={{ width: 180 }}
          >
            {TYPE_OPTIONS.map((o) => (
              <MenuItem key={o.value} value={o.value}>
                {o.label}
              </MenuItem>
            ))}
          </TextField>
          <TextField
            label="С"
            type="date"
            size="small"
            value={p.from}
            onChange={(e) => set({ ...p, from: e.target.value })}
            InputLabelProps={{ shrink: true }}
          />
          <TextField
            label="По"
            type="date"
            size="small"
            value={p.to}
            onChange={(e) => set({ ...p, to: e.target.value })}
            InputLabelProps={{ shrink: true }}
          />
        </div>
      )}
      fetchReport={(p) =>
        apiGet<TouristDto[]>(`/api/reports/hotels/${p.hotelId}/guests`, {
          ...(p.type ? { type: p.type } : {}),
          ...(p.from ? { from: p.from } : {}),
          ...(p.to ? { to: p.to } : {}),
        })
      }
      renderResult={(rows) => (
        <Card>
          <CardContent className="p-0">
            <DataGrid
              rows={rows.map((r) => ({ id: r.id, ...r }))}
              columns={TOURIST_COLUMNS}
              autoHeight
              disableRowSelectionOnClick
              sx={{ border: 0 }}
            />
          </CardContent>
        </Card>
      )}
    />
  );
}

// Q3 — количество туристов за период
export function TouristsCountReport() {
  return (
    <ReportRunner<
      { from: string; to: string; type: TouristType | '' },
      { count: number }
    >
      title="Количество туристов за период (Q3)"
      description="Сколько туристов прибыло за указанный период."
      defaultParams={{ from: '2026-01-01', to: '2026-12-31', type: '' }}
      renderParamsForm={(p, set) => (
        <div className="flex gap-3 flex-wrap">
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
          <TextField
            label="Категория"
            select
            size="small"
            value={p.type}
            onChange={(e) => set({ ...p, type: e.target.value as TouristType | '' })}
            sx={{ width: 180 }}
          >
            {TYPE_OPTIONS.map((o) => (
              <MenuItem key={o.value} value={o.value}>
                {o.label}
              </MenuItem>
            ))}
          </TextField>
        </div>
      )}
      fetchReport={(p) =>
        apiGet<{ count: number }>('/api/reports/tourists/count', {
          from: p.from,
          to: p.to,
          ...(p.type ? { type: p.type } : {}),
        })
      }
      renderResult={(r) => (
        <Card>
          <CardContent className="p-6">
            <div className="text-sm text-gray-500">Всего туристов</div>
            <div className="text-4xl font-semibold text-gray-900 mt-1">
              {r.count.toLocaleString('ru-RU')}
            </div>
          </CardContent>
        </Card>
      )}
    />
  );
}

// Q4 — приезды конкретного человека
export function VisitStatsReport() {
  return (
    <ReportRunner<{ passport: string }, VisitStatsDto>
      title="Приезды человека (Q4)"
      description="История поездок по номеру паспорта."
      defaultParams={{ passport: '' }}
      renderParamsForm={(p, set) => (
        <TextField
          label="Номер паспорта"
          size="small"
          value={p.passport}
          onChange={(e) => set({ ...p, passport: e.target.value })}
          sx={{ width: 240 }}
          required
        />
      )}
      fetchReport={(p) =>
        apiGet<VisitStatsDto>(
          '/api/reports/tourists/visits/' + encodeURIComponent(p.passport),
        )
      }
      renderResult={(r) => (
        <Card>
          <CardContent className="p-5">
            <div className="text-lg font-semibold text-gray-900">
              {r.lastName} {r.firstName} {r.middleName ?? ''}
            </div>
            <div className="text-sm text-gray-500 mt-1 mb-4">
              Паспорт: {r.passportNumber} · Дата рождения: {r.birthDate} ·{' '}
              <strong>Поездок: {r.visitCount}</strong>
            </div>

            <div className="flex flex-col gap-3">
              {r.visits.map((v) => (
                <VisitCard key={v.touristId} visit={v} />
              ))}
            </div>
          </CardContent>
        </Card>
      )}
    />
  );
}

// Карточка одного приезда: даты/группа + раскрываемые блоки
// «Гостиницы», «Экскурсии», «Груз».
function VisitCard({ visit }: { visit: VisitDetail }) {
  const fmtDate = (d: string | null) => d ?? '—';
  const fmtNum = (n: number | null) =>
    n == null ? '—' : n.toLocaleString('ru-RU');

  return (
    <div className="rounded border border-gray-200 p-4">
      <div className="flex items-center justify-between flex-wrap gap-2">
        <div>
          <div className="font-medium text-gray-900">{visit.groupName}</div>
          <div className="text-sm text-gray-500">
            {visit.arrivalDate} → {visit.departureDate}
          </div>
        </div>
        <Chip label={visit.category} size="small" />
      </div>

      <Divider sx={{ my: 1.5 }} />

      <Accordion disableGutters elevation={0} sx={{ '&:before': { display: 'none' } }}>
        <AccordionSummary expandIcon={<ExpandMoreIcon />} sx={{ px: 0, minHeight: 40 }}>
          <span className="text-sm font-medium">
            Гостиницы ({visit.hotels.length})
          </span>
        </AccordionSummary>
        <AccordionDetails sx={{ px: 0, pt: 0 }}>
          {visit.hotels.length === 0 ? (
            <div className="text-sm text-gray-400">Нет данных о расселении</div>
          ) : (
            <ul className="text-sm text-gray-700 list-disc pl-5 space-y-1">
              {visit.hotels.map((h, i) => (
                <li key={i}>
                  <strong>{h.hotelName}</strong>
                  {h.roomNumber ? ` · комн. ${h.roomNumber}` : ''} ·{' '}
                  {fmtDate(h.checkInDate)} → {fmtDate(h.checkOutDate)}
                </li>
              ))}
            </ul>
          )}
        </AccordionDetails>
      </Accordion>

      <Accordion disableGutters elevation={0} sx={{ '&:before': { display: 'none' } }}>
        <AccordionSummary expandIcon={<ExpandMoreIcon />} sx={{ px: 0, minHeight: 40 }}>
          <span className="text-sm font-medium">
            Экскурсии ({visit.excursions.length})
          </span>
        </AccordionSummary>
        <AccordionDetails sx={{ px: 0, pt: 0 }}>
          {visit.excursions.length === 0 ? (
            <div className="text-sm text-gray-400">Экскурсий не заказывал</div>
          ) : (
            <ul className="text-sm text-gray-700 list-disc pl-5 space-y-1">
              {visit.excursions.map((e, i) => (
                <li key={i}>
                  <strong>{e.excursionName}</strong>
                  {e.excursionDate ? ` · ${e.excursionDate}` : ''} ·{' '}
                  агентство: {e.agencyName}
                </li>
              ))}
            </ul>
          )}
        </AccordionDetails>
      </Accordion>

      <Accordion disableGutters elevation={0} sx={{ '&:before': { display: 'none' } }}>
        <AccordionSummary expandIcon={<ExpandMoreIcon />} sx={{ px: 0, minHeight: 40 }}>
          <span className="text-sm font-medium">
            Груз ({visit.cargo.length})
          </span>
        </AccordionSummary>
        <AccordionDetails sx={{ px: 0, pt: 0 }}>
          {visit.cargo.length === 0 ? (
            <div className="text-sm text-gray-400">Груз не сдавал</div>
          ) : (
            <ul className="text-sm text-gray-700 list-disc pl-5 space-y-1">
              {visit.cargo.map((c, i) => (
                <li key={i}>
                  {c.marking ?? 'без маркировки'}
                  {c.cargoType ? ` · ${c.cargoType}` : ''} · мест:{' '}
                  {fmtNum(c.placesCount)} · вес: {fmtNum(c.weightKg)} кг · сумма:{' '}
                  {fmtNum(c.totalCost)}
                </li>
              ))}
            </ul>
          )}
        </AccordionDetails>
      </Accordion>
    </div>
  );
}

// Q14 — соотношение категорий
export function CategoryShareReport() {
  return (
    <ReportRunner<{ from: string; to: string }, CategoryShareDto>
      title="Соотношение категорий туристов (Q14)"
      description="Доли REST/SHOP/CHILD за период (или за всё время, если даты пусты)."
      defaultParams={{ from: '', to: '' }}
      renderParamsForm={(p, set) => (
        <div className="flex gap-3 flex-wrap">
          <TextField
            label="С"
            type="date"
            size="small"
            value={p.from}
            onChange={(e) => set({ ...p, from: e.target.value })}
            InputLabelProps={{ shrink: true }}
          />
          <TextField
            label="По"
            type="date"
            size="small"
            value={p.to}
            onChange={(e) => set({ ...p, to: e.target.value })}
            InputLabelProps={{ shrink: true }}
          />
        </div>
      )}
      fetchReport={(p) =>
        apiGet<CategoryShareDto>('/api/reports/tourists/category-share', {
          ...(p.from ? { from: p.from } : {}),
          ...(p.to ? { to: p.to } : {}),
        })
      }
      renderResult={(r) => (
        <Card>
          <CardContent className="p-6">
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <ShareCard label="Отдыхающие" count={r.restCount} pct={r.restPercent} color="#10b981" />
              <ShareCard label="За грузом" count={r.shopCount} pct={r.shopPercent} color="#f59e0b" />
              <ShareCard label="Дети" count={r.childCount} pct={r.childPercent} color="#0ea5e9" />
            </div>
            <div className="mt-4 text-sm text-gray-500 text-center">
              Всего: <strong>{r.total}</strong>
            </div>
          </CardContent>
        </Card>
      )}
    />
  );
}

function ShareCard({
  label,
  count,
  pct,
  color,
}: {
  label: string;
  count: number;
  pct: number;
  color: string;
}) {
  return (
    <div className="text-center p-4 rounded border border-gray-200">
      <div className="text-sm text-gray-500">{label}</div>
      <div className="text-3xl font-semibold mt-1" style={{ color }}>
        {pct}%
      </div>
      <div className="text-xs text-gray-500 mt-1">{count} чел.</div>
    </div>
  );
}
