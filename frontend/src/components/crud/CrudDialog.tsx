import { useEffect, useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Alert,
  CircularProgress,
} from '@mui/material';
import { FormField } from './FormField';
import type { FieldDef } from './FieldDef';
import { extractMessage } from '../../api/client';

interface Props<T> {
  open: boolean;
  initialValue: T | null;
  fields: FieldDef<T>[];
  title: string;
  saving: boolean;
  error: unknown;
  onSubmit: (value: T) => void;
  onClose: () => void;
}


export function CrudDialog<T>({
  open,
  initialValue,
  fields,
  title,
  saving,
  error,
  onSubmit,
  onClose,
}: Props<T>) {
  const [value, setValue] = useState<Partial<T>>({});

  useEffect(() => {
    if (open) {
      setValue((initialValue ?? {}) as Partial<T>);
    }
  }, [open, initialValue]);

  const isEdit = initialValue !== null;
  const visibleFields = fields.filter((f) =>
    isEdit ? !f.hideOnEdit : !f.hideOnCreate,
  );

  function handleFieldChange(
    name: string,
    newValue: unknown,
    derived?: Record<string, unknown>,
  ) {
    setValue((prev) => {
      const next = { ...prev, [name]: newValue } as Record<string, unknown>;
      if (derived) Object.assign(next, derived);
      for (const f of fields) {
        if (f.resetWhenChanged?.includes(name)) {
          next[f.name] = null;
        }
      }
      return next as Partial<T>;
    });
  }

  function handleSubmit() {
    onSubmit(value as T);
  }

  const missingRequired = visibleFields
    .filter((f) => f.required)
    .some((f) => {
      const v = (value as Record<string, unknown>)[f.name];
      return v === undefined || v === null || v === '';
    });

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>{title}</DialogTitle>
      <DialogContent>
        <div className="flex flex-col gap-4 mt-2">
          {visibleFields.map((field) => (
            <FormField
              key={field.name}
              field={field as FieldDef}
              value={(value as Record<string, unknown>)[field.name]}
              onChange={(v, derived) => handleFieldChange(field.name, v, derived)}
              formValue={value as Record<string, unknown>}
            />
          ))}
          {error ? <Alert severity="error">{extractMessage(error)}</Alert> : null}
        </div>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={saving}>
          Отмена
        </Button>
        <Button
          onClick={handleSubmit}
          disabled={saving || missingRequired}
          variant="contained"
        >
          {saving ? <CircularProgress size={20} color="inherit" /> : 'Сохранить'}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
