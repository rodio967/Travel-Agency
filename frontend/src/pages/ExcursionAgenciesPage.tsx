import type { GridColDef } from '@mui/x-data-grid';
import { CrudTable } from '../components/crud/CrudTable';
import type { FieldDef } from '../components/crud/FieldDef';
import type { ExcursionAgencyDto } from '../types/dto';

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 80 },
  { field: 'name', headerName: 'Название', flex: 1, minWidth: 200 },
  { field: 'contact', headerName: 'Контакт', flex: 1, minWidth: 180 },
  { field: 'qualityRating', headerName: 'Рейтинг', width: 120, type: 'number' },
];

const fields: FieldDef<ExcursionAgencyDto>[] = [
  { name: 'name', label: 'Название', type: 'text', required: true },
  { name: 'contact', label: 'Контакт (телефон, email)', type: 'text' },
  {
    name: 'qualityRating',
    label: 'Рейтинг качества (0–10)',
    type: 'number',
    min: 0,
    max: 10,
    step: 0.1,
  },
];

export function ExcursionAgenciesPage() {
  return (
    <CrudTable<ExcursionAgencyDto>
      baseUrl="/api/excursion-agencies"
      title="Экскурсионные агентства"
      columns={columns}
      fields={fields}
      createDialogTitle="Новое агентство"
      editDialogTitle="Редактирование агентства"
      writePermission="AGENCY_WRITE"
    />
  );
}
