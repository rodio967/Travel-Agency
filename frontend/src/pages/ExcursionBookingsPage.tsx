import type { GridColDef } from '@mui/x-data-grid';
import { CrudTable } from '../components/crud/CrudTable';
import type { FieldDef } from '../components/crud/FieldDef';
import type {
  ExcursionBookingDto,
  ExcursionDto,
  TouristDto,
} from '../types/dto';

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 70 },
  { field: 'touristId', headerName: 'Турист', width: 100, type: 'number' },
  { field: 'excursionId', headerName: 'Экскурсия', width: 110, type: 'number' },
  { field: 'bookedAt', headerName: 'Записан', width: 200 },
];

const fields: FieldDef<ExcursionBookingDto>[] = [
  {
    name: 'touristId',
    label: 'Турист',
    type: 'select',
    required: true,
    optionsUrl: '/api/tourists',
    optionsParams: { type: 'REST' },
    optionLabel: (t: TouristDto) =>
      `#${t.id} · ${t.lastName} ${t.firstName} (${t.touristType})`,
    helperText:
      'SHOP-туристы на экскурсии не записываются — операция будет отвергнута триггером БД.',
  },
  {
    name: 'excursionId',
    label: 'Экскурсия',
    type: 'select',
    required: true,
    optionsUrl: '/api/excursions',
    optionLabel: (e: ExcursionDto) => `#${e.id} · ${e.name}`,
  },

  {
    name: 'bookedAt',
    label: 'Дата и время записи',
    type: 'datetime',
    hideOnCreate: true,
  },
];

export function ExcursionBookingsPage() {
  return (
    <CrudTable<ExcursionBookingDto>
      baseUrl="/api/excursion-bookings"
      title="Записи на экскурсии"
      columns={columns}
      fields={fields}
      createDialogTitle="Новая запись"
      editDialogTitle="Редактирование записи"
      writePermission="EXCURSION_BOOKING_WRITE"
    />
  );
}
