import type { GridColDef } from '@mui/x-data-grid';
import { CrudTable } from '../components/crud/CrudTable';
import type { FieldDef } from '../components/crud/FieldDef';
import type { TouristDto, VisaDto } from '../types/dto';

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 70 },
  { field: 'visaNumber', headerName: 'Номер визы', width: 150 },
  { field: 'touristId', headerName: 'Турист', width: 100, type: 'number' },
  { field: 'issueDate', headerName: 'Выдана', width: 130 },
  { field: 'expiryDate', headerName: 'Действует до', width: 130 },
  { field: 'cost', headerName: 'Стоимость', width: 120, type: 'number' },
];

const fields: FieldDef<VisaDto>[] = [
  {
    name: 'touristId',
    label: 'Турист',
    type: 'select',
    required: true,
    optionsUrl: '/api/tourists',
    optionLabel: (t: TouristDto) =>
      `#${t.id} · ${t.lastName} ${t.firstName} (${t.touristType})`,
    filterOption: (t: TouristDto) => t.touristType !== 'CHILD',
    helperText: 'Visa для взрослых',
  },
  { name: 'visaNumber', label: 'Номер визы', type: 'text', required: true },
  { name: 'issueDate', label: 'Дата выдачи', type: 'date', required: true },
  {
    name: 'expiryDate',
    label: 'Действует до',
    type: 'date',
    required: true,
  },
  { name: 'cost', label: 'Стоимость', type: 'number', min: 0 },
];

export function VisasPage() {
  return (
    <CrudTable<VisaDto>
      baseUrl="/api/visas"
      title="Визы"
      columns={columns}
      fields={fields}
      createDialogTitle="Новая виза"
      editDialogTitle="Редактирование визы"
      writePermission="VISA_WRITE"
    />
  );
}
