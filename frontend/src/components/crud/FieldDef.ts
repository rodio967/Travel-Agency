import type { SelectOption } from '../../api/useOptions';

export type FieldType =
  | 'text'
  | 'textarea'
  | 'number'
  | 'date'
  | 'datetime'
  | 'select'
  | 'multiselect'
  | 'enum'
  | 'boolean';

export interface FieldDef<T = unknown> {
  name: keyof T & string;
  label: string;
  type: FieldType;
  required?: boolean;
  helperText?: string;
  min?: number;
  max?: number;
  step?: number;
  enumValues?: SelectOption[];

  dynamicEnumValues?: (formValue: Record<string, unknown>) => SelectOption[];
  optionsUrl?: string;
  filterOption?: (item: any, formValue: Record<string, unknown>) => boolean;
  resetWhenChanged?: string[];
  syncOnSelect?: (item: any) => Record<string, unknown>;
  optionLabel?: (item: any) => string;
  hideOnEdit?: boolean;
  hideOnCreate?: boolean;

  optionsParams?: Record<string, string | number>;
}
