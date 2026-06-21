import { useMemo } from 'react';

import {
  TextField,
  MenuItem,
  FormControl,
  InputLabel,
  Select,
  FormControlLabel,
  Switch,
  CircularProgress,
  Chip,
  Box,
  OutlinedInput,
} from '@mui/material';
import type { FieldDef } from './FieldDef';
import { useOptions, useRawList, type SelectOption } from '../../api/useOptions';

interface Props {
  field: FieldDef;
  value: unknown;
  onChange: (value: unknown, derived?: Record<string, unknown>) => void;
  formValue?: Record<string, unknown>;
}


export function FormField({ field, value, onChange, formValue = {} }: Props) {
  const optionsUrl = useMemo(() => {
      if (field.type !== 'select' && field.type !== 'multiselect') return null;
      if (!field.optionsUrl) return null;
      if (!field.optionsParams) return field.optionsUrl;
      const qs = new URLSearchParams();
      for (const [k, v] of Object.entries(field.optionsParams)) {
        qs.set(k, String(v));
      }
      return `${field.optionsUrl}?${qs.toString()}`;
    }, [field.type, field.optionsUrl, field.optionsParams]);

    const optionsQuery = useOptions({
      url: optionsUrl,
      toLabel: field.optionLabel ?? ((item: { id?: number }) => `#${item.id}`),
    });

  const needsRawList =
    (field.type === 'select' || field.type === 'multiselect') &&
    (!!field.filterOption || !!field.syncOnSelect);
  const rawListQuery = useRawList<Record<string, unknown> & { id?: number }>(
    needsRawList ? (field.optionsUrl ?? null) : null,
  );

  const toLabel = field.optionLabel ?? ((item: { id?: number }) => `#${item.id}`);
  const selectOptions: SelectOption[] = needsRawList
    ? (rawListQuery.data ?? [])
        .filter((item) => item.id != null)
        .filter((item) =>
          field.filterOption ? field.filterOption(item, formValue) : true,
        )
        .map((item) => ({ value: item.id as number, label: toLabel(item) }))
    : (optionsQuery.data ?? []);
  const selectLoading = needsRawList
    ? rawListQuery.isLoading
    : optionsQuery.isLoading;

  const commonProps = {
    label: field.label + (field.required ? ' *' : ''),
    helperText: field.helperText,
    fullWidth: true,
    size: 'small' as const,
  };

  switch (field.type) {
    case 'text':
      return (
        <TextField
          {...commonProps}
          value={(value as string) ?? ''}
          onChange={(e) => onChange(e.target.value)}
        />
      );

    case 'textarea':
      return (
        <TextField
          {...commonProps}
          multiline
          minRows={3}
          value={(value as string) ?? ''}
          onChange={(e) => onChange(e.target.value)}
        />
      );

    case 'number':
      return (
        <TextField
          {...commonProps}
          type="number"
          value={value === null || value === undefined ? '' : String(value)}
          onChange={(e) => {
            const raw = e.target.value;
            onChange(raw === '' ? null : Number(raw));
          }}
          inputProps={{ min: field.min, max: field.max, step: field.step ?? 'any' }}
        />
      );

    case 'date':
      return (
        <TextField
          {...commonProps}
          type="date"
          value={(value as string) ?? ''}
          onChange={(e) => onChange(e.target.value || null)}
          InputLabelProps={{ shrink: true }}
        />
      );

    case 'datetime':
      return (
        <TextField
          {...commonProps}
          type="datetime-local"
          value={(value as string) ?? ''}
          onChange={(e) => onChange(e.target.value || null)}
          InputLabelProps={{ shrink: true }}
        />
      );

    case 'enum': {
      const opts = field.dynamicEnumValues
        ? field.dynamicEnumValues(formValue)
        : (field.enumValues ?? []);
      return (
        <FormControl fullWidth size="small">
          <InputLabel>{field.label + (field.required ? ' *' : '')}</InputLabel>
          <Select
            label={field.label + (field.required ? ' *' : '')}
            value={(value as string) ?? ''}
            onChange={(e) => onChange(e.target.value || null)}
          >
            <MenuItem value="">
              <em>—</em>
            </MenuItem>
            {opts.map((o: SelectOption) => (
              <MenuItem key={o.value} value={o.value}>
                {o.label}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      );
    }

    case 'select': {
      const opts = selectOptions;
      const isLoading = selectLoading;
      return (
        <FormControl fullWidth size="small">
          <InputLabel>{field.label + (field.required ? ' *' : '')}</InputLabel>
          <Select
            label={field.label + (field.required ? ' *' : '')}
            value={value === null || value === undefined ? '' : String(value)}
            onChange={(e) => {
              const raw = e.target.value;
              const id = raw === '' ? null : Number(raw);

              let derived: Record<string, unknown> | undefined;
              if (field.syncOnSelect) {
                const item =
                  id == null
                    ? null
                    : (rawListQuery.data ?? []).find(
                        (it) => it.id === id,
                      );
                derived = field.syncOnSelect(item ?? null);
              }
              onChange(id, derived);
            }}
            disabled={isLoading}
            endAdornment={isLoading ? <CircularProgress size={16} className="mr-6" /> : null}
          >
            <MenuItem value="">
              <em>—</em>
            </MenuItem>
            {opts.map((o) => (
              <MenuItem key={o.value} value={o.value}>
                {o.label}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      );
    }

    case 'multiselect': {
      const opts = selectOptions;
      const isLoading = selectLoading;
      const selected = Array.isArray(value) ? (value as number[]) : [];
      return (
        <FormControl fullWidth size="small">
          <InputLabel>{field.label + (field.required ? ' *' : '')}</InputLabel>
          <Select
            multiple
            label={field.label + (field.required ? ' *' : '')}
            value={selected}
            onChange={(e) => {
              const v = e.target.value;
              const arr = Array.isArray(v) ? v : [v];
              onChange(arr.map((x) => Number(x)));
            }}
            input={<OutlinedInput label={field.label + (field.required ? ' *' : '')} />}
            renderValue={(vals) => (
              <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                {(vals as number[]).map((id) => {
                  const opt = opts.find((o) => o.value === id);
                  return <Chip key={id} label={opt?.label ?? `#${id}`} size="small" />;
                })}
              </Box>
            )}
            disabled={isLoading}
          >
            {opts.map((o) => (
              <MenuItem key={o.value} value={o.value}>
                {o.label}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      );
    }

    case 'boolean':
      return (
        <FormControlLabel
          control={
            <Switch
              checked={Boolean(value)}
              onChange={(e) => onChange(e.target.checked)}
            />
          }
          label={field.label}
        />
      );

    default:
      return null;
  }
}
