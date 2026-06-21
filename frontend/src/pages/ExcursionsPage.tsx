import type { GridColDef } from '@mui/x-data-grid';
import { CrudTable } from '../components/crud/CrudTable';
import type { FieldDef } from '../components/crud/FieldDef';
import type { ExcursionDto, ExcursionAgencyDto } from '../types/dto';

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 80 },
  { field: 'name', headerName: 'Название', flex: 1, minWidth: 200 },
  { field: 'agencyId', headerName: 'Агентство', width: 120, type: 'number' },
  { field: 'price', headerName: 'Цена', width: 120, type: 'number' },
  { field: 'excursionDate', headerName: 'Дата', width: 130 },
];

const fields: FieldDef<ExcursionDto>[] = [
  {
    name: 'agencyId',
    label: 'Агентство',
    type: 'select',
    required: true,
    optionsUrl: '/api/excursion-agencies',
    optionLabel: (a: ExcursionAgencyDto) => `#${a.id} · ${a.name}`,
  },
  { name: 'name', label: 'Название экскурсии', type: 'text', required: true },
  { name: 'description', label: 'Описание', type: 'textarea' },
  { name: 'price', label: 'Цена', type: 'number', min: 0, required: true },
  { name: 'excursionDate', label: 'Дата проведения', type: 'date', required: true },
];

export function ExcursionsPage() {
  return (
    <CrudTable<ExcursionDto>
      baseUrl="/api/excursions"
      title="Экскурсии"
      columns={columns}
      fields={fields}
      createDialogTitle="Новая экскурсия"
      editDialogTitle="Редактирование экскурсии"
      writePermission="EXCURSION_WRITE"
    />
  );
}
