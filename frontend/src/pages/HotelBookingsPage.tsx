import type { GridColDef } from '@mui/x-data-grid';
import { CrudTable } from '../components/crud/CrudTable';
import type { FieldDef } from '../components/crud/FieldDef';
import type { HotelBookingDto, HotelDto, TouristDto } from '../types/dto';

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 70 },
  { field: 'touristId', headerName: 'Турист', width: 90, type: 'number' },
  { field: 'hotelId', headerName: 'Гостиница', width: 100, type: 'number' },
  { field: 'roomNumber', headerName: 'Номер', width: 100 },
  { field: 'checkInDate', headerName: 'Заезд', width: 120 },
  { field: 'checkOutDate', headerName: 'Выезд', width: 120 },
  { field: 'cost', headerName: 'Стоимость', width: 130, type: 'number' },
];

const fields: FieldDef<HotelBookingDto>[] = [
  {
    name: 'touristId',
    label: 'Турист',
    type: 'select',
    required: true,
    optionsUrl: '/api/tourists',
    optionLabel: (t: TouristDto) =>
      `#${t.id} · ${t.lastName} ${t.firstName}`,
  },
  {
    name: 'hotelId',
    label: 'Гостиница',
    type: 'select',
    required: true,
    optionsUrl: '/api/hotels',
    optionLabel: (h: HotelDto) => `#${h.id} · ${h.name}`,
  },
  { name: 'roomNumber', label: 'Номер комнаты', type: 'text', required: true },
  { name: 'checkInDate', label: 'Дата заезда', type: 'date', required: true },
  {
    name: 'checkOutDate',
    label: 'Дата выезда',
    type: 'date',
    required: true,
  },
  { name: 'cost', label: 'Стоимость', type: 'number', min: 0 },
];

export function HotelBookingsPage() {
  return (
    <CrudTable<HotelBookingDto>
      baseUrl="/api/hotel-bookings"
      title="Расселение"
      columns={columns}
      fields={fields}
      createDialogTitle="Новое расселение"
      editDialogTitle="Редактирование расселения"
      writePermission="HOTEL_BOOKING_WRITE"
    />
  );
}
