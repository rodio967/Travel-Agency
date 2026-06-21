import type { GridColDef } from '@mui/x-data-grid';
import { CrudTable } from '../components/crud/CrudTable';
import type { FieldDef } from '../components/crud/FieldDef';
import type { CargoDto, FlightDto, TouristDto } from '../types/dto';

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 70 },
  { field: 'touristId', headerName: 'Турист', width: 90, type: 'number' },
  { field: 'flightId', headerName: 'Рейс', width: 90, type: 'number' },
  { field: 'marking', headerName: 'Маркировка', width: 130 },
  { field: 'cargoType', headerName: 'Тип', width: 130 },
  { field: 'placesCount', headerName: 'Мест', width: 80, type: 'number' },
  { field: 'weightKg', headerName: 'Вес, кг', width: 100, type: 'number' },
  { field: 'totalCost', headerName: 'Сумма', width: 110, type: 'number' },
];

const fields: FieldDef<CargoDto>[] = [
  {
    name: 'touristId',
    label: 'Турист (только SHOP)',
    type: 'select',
    required: true,
    optionsUrl: '/api/tourists',
    optionsParams: { type: 'SHOP' },
    optionLabel: (t: TouristDto) =>
      `#${t.id} · ${t.lastName} ${t.firstName} (${t.touristType})`,
    helperText:
      'Груз оформляется только на shop-туристов — для других типов БД отвергнет операцию.',
  },
  {
    name: 'flightId',
    label: 'Рейс отправки',
    type: 'select',
    required: true,
    optionsUrl: '/api/flights',
    optionLabel: (f: FlightDto) =>
      `#${f.id} · ${f.flightNumber} · ${f.flightDate}`,
  },
  { name: 'marking', label: 'Маркировка', type: 'text' },
  {
    name: 'cargoType',
    label: 'Тип груза',
    type: 'text',
    helperText: 'например: одежда, электроника, бытовая техника',
  },
  {
    name: 'placesCount',
    label: 'Количество мест',
    type: 'number',
    min: 1,
  },
  { name: 'weightKg', label: 'Вес, кг', type: 'number', min: 0 },
  {
    name: 'volumetricWeight',
    label: 'Объёмный вес, кг',
    type: 'number',
    min: 0,
  },
  {
    name: 'packagingCost',
    label: 'Стоимость упаковки',
    type: 'number',
    min: 0,
  },
  {
    name: 'insuranceCost',
    label: 'Стоимость страховки',
    type: 'number',
    min: 0,
  },
  {
    name: 'totalCost',
    label: 'Итоговая сумма',
    type: 'number',
    min: 0,
  },
];

export function CargoPage() {
  return (
    <CrudTable<CargoDto>
      baseUrl="/api/cargo"
      title="Груз (весовые ведомости)"
      columns={columns}
      fields={fields}
      createDialogTitle="Новая ведомость"
      editDialogTitle="Редактирование ведомости"
      writePermission="CARGO_WRITE"
    />
  );
}
