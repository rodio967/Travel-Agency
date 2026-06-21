import type { GridColDef } from '@mui/x-data-grid';
import { CrudTable } from '../components/crud/CrudTable';
import type { FieldDef } from '../components/crud/FieldDef';
import type { TouristGroupDto, FlightDto } from '../types/dto';

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 80 },
  { field: 'name', headerName: 'Название', flex: 1, minWidth: 200 },
  { field: 'arrivalDate', headerName: 'Прилёт', width: 130 },
  { field: 'departureDate', headerName: 'Отлёт', width: 130 },
  { field: 'arrivalFlightId', headerName: 'Рейс прилёта', width: 130, type: 'number' },
  { field: 'departureFlightId', headerName: 'Рейс отлёта', width: 130, type: 'number' },
];


const flightLabel = (f: FlightDto) =>
  `#${f.id} · ${f.flightNumber} · ${f.flightDate}`;

const fields: FieldDef<TouristGroupDto>[] = [
  { name: 'name', label: 'Название группы', type: 'text', required: true },
  { name: 'arrivalDate', label: 'Дата прилёта', type: 'date', required: true },
  { name: 'departureDate', label: 'Дата отлёта', type: 'date', required: true },
  {
    name: 'arrivalFlightId',
    label: 'Рейс прилёта',
    type: 'select',
    required: true,
    optionsUrl: '/api/flights',
    optionLabel: flightLabel,
  },
  {
    name: 'departureFlightId',
    label: 'Рейс отлёта',
    type: 'select',
    required: true,
    optionsUrl: '/api/flights',
    optionLabel: flightLabel,
  },
];

export function GroupsPage() {
  return (
    <CrudTable<TouristGroupDto>
      baseUrl="/api/groups"
      title="Туристические группы"
      columns={columns}
      fields={fields}
      createDialogTitle="Новая группа"
      editDialogTitle="Редактирование группы"
      writePermission="GROUP_WRITE"
    />
  );
}
