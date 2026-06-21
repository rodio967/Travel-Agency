import type { GridColDef } from '@mui/x-data-grid';
import { CrudTable } from '../components/crud/CrudTable';
import type { FieldDef } from '../components/crud/FieldDef';
import type { FlightDto } from '../types/dto';

const FLIGHT_TYPES = [
  { value: 'PASSENGER' as const, label: 'Пассажирский' },
  { value: 'CARGO' as const, label: 'Грузовой' },
  { value: 'MIXED' as const, label: 'Грузо-пассажирский' },
];

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 80 },
  { field: 'flightNumber', headerName: 'Номер', width: 130 },
  { field: 'flightDate', headerName: 'Дата', width: 130 },
  {
    field: 'flightType',
    headerName: 'Тип',
    width: 180,
    valueGetter: (value) =>
      FLIGHT_TYPES.find((t) => t.value === value)?.label ?? value,
  },
  { field: 'maxSeats', headerName: 'Мест', width: 100, type: 'number' },
  {
    field: 'maxCargoKg',
    headerName: 'Груз макс. (кг)',
    width: 140,
    type: 'number',
  },
  {
    field: 'airportFees',
    headerName: 'Аэропорт. сборы',
    width: 150,
    type: 'number',
  },
];

const fields: FieldDef<FlightDto>[] = [
  { name: 'flightNumber', label: 'Номер рейса', type: 'text', required: true },
  { name: 'flightDate', label: 'Дата', type: 'date', required: true },
  {
    name: 'flightType',
    label: 'Тип',
    type: 'enum',
    required: true,
    enumValues: FLIGHT_TYPES as unknown as { value: number; label: string }[],
  },
  { name: 'maxSeats', label: 'Макс. мест', type: 'number', min: 1 },
  { name: 'maxCargoKg', label: 'Макс. груз, кг', type: 'number', min: 0 },
  { name: 'airportFees', label: 'Аэропортовые сборы', type: 'number', min: 0 },
];

export function FlightsPage() {
  return (
    <CrudTable<FlightDto>
      baseUrl="/api/flights"
      title="Рейсы"
      columns={columns}
      fields={fields}
      createDialogTitle="Новый рейс"
      editDialogTitle="Редактирование рейса"
      writePermission="FLIGHT_WRITE"
    />
  );
}
