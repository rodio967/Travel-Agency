import type { GridColDef } from '@mui/x-data-grid';
import { CrudTable } from '../components/crud/CrudTable';
import type { FieldDef } from '../components/crud/FieldDef';
import type { FinancialEntryDto, TouristGroupDto, TouristDto } from '../types/dto';

const ENTRY_TYPES = [
  { value: 'INCOME' as const, label: 'Доход' },
  { value: 'EXPENSE' as const, label: 'Расход' },
];

const CATEGORIES = [
  { value: 'HOTEL', label: 'Гостиница' },
  { value: 'EXCURSION', label: 'Экскурсия' },
  { value: 'VISA', label: 'Виза' },
  { value: 'FLIGHT_FEES', label: 'Взлёт-посадка' },
  { value: 'AIRPORT_LOADING', label: 'Загрузка/разгрузка' },
  { value: 'AIRPORT_DISPATCH', label: 'Диспетчерские' },
  { value: 'CARGO_STORAGE', label: 'Хранение груза' },
  { value: 'OFFICE', label: 'Офис' },
  { value: 'UNFORESEEN', label: 'Непредвиденные' },
  { value: 'TOUR_PAYMENT', label: 'Оплата тура' },
  { value: 'CARGO_FEE', label: 'Оплата груза' },
  { value: 'OTHER', label: 'Прочее' },
];

const EXCLUDED_CATEGORIES: Record<string, string[]> = {
  REST: ['CARGO_FEE', 'CARGO_STORAGE'],
  SHOP: ['EXCURSION'],
  CHILD: ['EXCURSION', 'CARGO_FEE', 'CARGO_STORAGE', 'VISA'],
};

function categoriesForTourist(touristType: unknown) {
  const excluded =
    typeof touristType === 'string'
      ? (EXCLUDED_CATEGORIES[touristType] ?? [])
      : [];
  return CATEGORIES.filter((c) => !excluded.includes(c.value));
}

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 80 },
  { field: 'groupId', headerName: 'Группа', width: 100, type: 'number' },
  { field: 'touristId', headerName: 'Турист', width: 100, type: 'number' },
  {
    field: 'entryType',
    headerName: 'Тип',
    width: 100,
    valueGetter: (value) =>
      ENTRY_TYPES.find((t) => t.value === value)?.label ?? value,
  },
  {
    field: 'category',
    headerName: 'Категория',
    width: 180,
    valueGetter: (value) =>
      CATEGORIES.find((c) => c.value === value)?.label ?? value,
  },
  { field: 'amount', headerName: 'Сумма', width: 130, type: 'number' },
  { field: 'entryDate', headerName: 'Дата', width: 130 },
  { field: 'description', headerName: 'Описание', flex: 1, minWidth: 200 },
];

const fields: FieldDef<FinancialEntryDto>[] = [
  {
    name: 'groupId',
    label: 'Группа',
    type: 'select',
    required: true,
    optionsUrl: '/api/groups',
    optionLabel: (g: TouristGroupDto) => `#${g.id} · ${g.name}`,
  },
  {
    name: 'touristId',
    label: 'Турист (необязательно)',
    type: 'select',
    optionsUrl: '/api/tourists',
    optionLabel: (t: TouristDto) =>
      `#${t.id} · ${t.lastName} ${t.firstName} (${t.touristType})`,

    filterOption: (t: TouristDto, form) =>
      form.groupId != null && t.groupId === form.groupId,

    syncOnSelect: (t: TouristDto | null) => ({
      touristType: t?.touristType ?? null,
    }),

    resetWhenChanged: ['groupId'],
    helperText: 'Сначала выберите группу',
  },
  {
    name: 'entryType',
    label: 'Тип',
    type: 'enum',
    required: true,
    enumValues: ENTRY_TYPES as unknown as { value: number; label: string }[],
  },
  {
    name: 'category',
    label: 'Категория',
    type: 'enum',
    required: true,
    dynamicEnumValues: (form) =>
      categoriesForTourist(form.touristType) as unknown as {
        value: number;
        label: string;
      }[],

    resetWhenChanged: ['touristId'],
  },
  { name: 'amount', label: 'Сумма', type: 'number', required: true, min: 0 },
  { name: 'entryDate', label: 'Дата', type: 'date', required: true },
  { name: 'description', label: 'Описание', type: 'textarea' },
];

export function FinancialEntriesPage() {
  return (
    <CrudTable<FinancialEntryDto>
      baseUrl="/api/financial-entries"
      title="Финансовые операции"
      columns={columns}
      fields={fields}
      createDialogTitle="Новая операция"
      editDialogTitle="Редактирование операции"
      writePermission="FINANCE_WRITE"
      prepareDto={(dto) => {
        const { ...rest } = dto as FinancialEntryDto & { touristType?: unknown };
        delete (rest as { touristType?: unknown }).touristType;
        return rest as FinancialEntryDto;
      }}
    />
  );
}
