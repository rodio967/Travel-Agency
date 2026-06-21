import type { GridColDef } from '@mui/x-data-grid';
import { CrudTable } from '../components/crud/CrudTable';
import type { FieldDef } from '../components/crud/FieldDef';
import type { HotelDto } from '../types/dto';

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 80 },
  { field: 'name', headerName: 'Название', flex: 1, minWidth: 180 },
  { field: 'address', headerName: 'Адрес', flex: 2, minWidth: 240 },
  { field: 'stars', headerName: 'Звёзды', width: 100, type: 'number' },
  {
    field: 'pricePerNight',
    headerName: 'Цена/ночь',
    width: 120,
    type: 'number',
    valueFormatter: (value: number | null) =>
      value != null ? value.toLocaleString('ru-RU') : '—',
  },
];

const fields: FieldDef<HotelDto>[] = [
  { name: 'name', label: 'Название', type: 'text', required: true },
  { name: 'address', label: 'Адрес', type: 'text', required: true },
  { name: 'stars', label: 'Звёзды', type: 'number', min: 1, max: 5 },
  { name: 'pricePerNight', label: 'Цена за ночь', type: 'number', min: 0 },
];

export function HotelsPage() {
  return (
    <CrudTable<HotelDto>
      baseUrl="/api/hotels"
      title="Гостиницы"
      columns={columns}
      fields={fields}
      createDialogTitle="Новая гостиница"
      editDialogTitle="Редактирование гостиницы"
      writePermission="HOTEL_WRITE"
    />
  );
}
