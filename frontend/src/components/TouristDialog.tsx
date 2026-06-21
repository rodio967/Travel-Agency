import { useEffect, useMemo, useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Alert,
  CircularProgress,
  ToggleButton,
  ToggleButtonGroup,
} from '@mui/material';
import { FormField } from './crud/FormField';
import type { FieldDef } from './crud/FieldDef';
import { extractMessage } from '../api/client';
import { useRawList } from '../api/useOptions';
import type {
  HotelDto,
  TouristDto,
  TouristGroupDto,
} from '../types/dto';
import type { TouristType } from '../types/common';

interface Props {
  open: boolean;
  initialValue: TouristDto | null;
  saving: boolean;
  error: unknown;
  onSubmit: (dto: TouristDto) => void;
  onClose: () => void;
}

const GENDERS = [
  { value: 'MALE', label: 'Мужской' },
  { value: 'FEMALE', label: 'Женский' },
];

const TYPE_LABELS: Record<TouristType, string> = {
  REST: 'Отдыхающий',
  SHOP: 'За грузом',
  CHILD: 'Ребёнок',
};


function commonFields(): FieldDef<Record<string, unknown>>[] {
  return [
    {
      name: 'groupId',
      label: 'Группа',
      type: 'select',
      required: true,
      optionsUrl: '/api/groups',
      optionLabel: (g: TouristGroupDto) => `#${g.id} · ${g.name}`,
    },
    { name: 'lastName', label: 'Фамилия', type: 'text', required: true },
    { name: 'firstName', label: 'Имя', type: 'text', required: true },
    { name: 'middleName', label: 'Отчество', type: 'text' },
    {
      name: 'gender',
      label: 'Пол',
      type: 'enum',
      required: true,
      enumValues: GENDERS as unknown as { value: number; label: string }[],
    },
    { name: 'birthDate', label: 'Дата рождения', type: 'date', required: true },
  ];
}

export function TouristDialog({
  open,
  initialValue,
  saving,
  error,
  onSubmit,
  onClose,
}: Props) {
  const isEdit = initialValue !== null;
  const [type, setType] = useState<TouristType>('REST');
  const [value, setValue] = useState<Record<string, unknown>>({});

  useEffect(() => {
    if (!open) return;
    if (initialValue) {
      setType(initialValue.touristType);
      setValue({ ...initialValue });
    } else {
      setType('REST');
      setValue({});
    }
  }, [open, initialValue]);

  const groupId = value.groupId as number | undefined;

  /**
   * Подгружаем СЫРОЙ список туристов — нам нужны groupId и touristType
   * каждой записи, чтобы корректно отфильтровать кандидатов в родители.
   * <p>
   * Подгружаем только когда тип CHILD и группа выбрана — иначе зря грузим.
   * <p>
   * staleTime: 0 — всегда свежие данные. Это важно для случая, когда
   * пользователь только что создал нового взрослого и сразу делает ребёнка:
   * без свежего запроса нового туриста не было бы в списке.
   */
  const allTourists = useRawList<TouristDto>(
    type === 'CHILD' && groupId ? '/api/tourists' : null,
    0,
  );


  const parentCandidates = useMemo(() => {
    if (!allTourists.data || !groupId) return [];
    const myId = (value as { id?: number }).id;
    const filtered = allTourists.data.filter((t) => {
      const type = String(t.touristType ?? '').toUpperCase();
      const isAdult = type === 'REST' || type === 'SHOP';
      const sameGroup = t.groupId === groupId;
      const notSelf = t.id !== myId;
      return isAdult && sameGroup && notSelf;
    });

    console.debug('[parentCandidates] groupId=', groupId,
      'all=', allTourists.data.length,
      'filtered=', filtered.length,
      'rawTypes=', allTourists.data.map((t) => `${t.id}:${t.touristType}`));
    return filtered;
  }, [allTourists.data, groupId, value]);

  function handleChange(name: string, v: unknown) {
    setValue((prev) => ({ ...prev, [name]: v }));
  }

  function handleSubmit() {
    const dto = { ...value, touristType: type } as TouristDto;
    onSubmit(dto);
  }


  const fields: FieldDef<Record<string, unknown>>[] = [
    ...commonFields(),
    {
      name: 'preferredHotelIds',
      label: 'Желаемые гостиницы',
      type: 'multiselect',
      optionsUrl: '/api/hotels',
      optionLabel: (h: HotelDto) => `#${h.id} · ${h.name}`,
    },
    {
      name: 'passportNumber',
      label: 'Номер паспорта',
      type: 'text',
      required: type !== 'CHILD',
      helperText:
        type === 'CHILD'
          ? 'Не обязательно (у детей паспорта может не быть)'
          : undefined,
    },
  ];

  if (type === 'REST') {
    fields.push({
      name: 'preferredExcursionType',
      label: 'Предпочитаемый тип экскурсий',
      type: 'text',
      helperText: 'например, история, природа, гастрономия',
    });
  } else if (type === 'SHOP') {
    fields.push({
      name: 'cargoLimitKg',
      label: 'Лимит груза, кг',
      type: 'number',
      min: 0,
    });
    fields.push({
      name: 'preferredCargoType',
      label: 'Предпочитаемый тип груза',
      type: 'text',
    });
  }


  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>
        {isEdit ? 'Редактирование туриста' : 'Новый турист'}
      </DialogTitle>
      <DialogContent>
        <div className="flex flex-col gap-4 mt-2">
          {!isEdit && (
            <div>
              <div className="text-sm text-gray-600 mb-2">Категория туриста</div>
              <ToggleButtonGroup
                value={type}
                exclusive
                onChange={(_, v) => v && setType(v)}
                size="small"
                fullWidth
              >
                {(['REST', 'SHOP', 'CHILD'] as TouristType[]).map((t) => (
                  <ToggleButton key={t} value={t}>
                    {TYPE_LABELS[t]}
                  </ToggleButton>
                ))}
              </ToggleButtonGroup>
            </div>
          )}

          {fields.map((f) => (
            <FormField
              key={f.name}
              field={f as FieldDef}
              value={value[f.name]}
              onChange={(v) => handleChange(f.name, v)}
            />
          ))}

          {/* Родитель отдельный select для CHILD */}
          {type === 'CHILD' && (
            <div>
              <div className="text-sm text-gray-600 mb-1">Родитель *</div>
              <select
                value={String((value.parentId as number | undefined) ?? '')}
                onChange={(e) => {
                  const v = e.target.value;
                  handleChange('parentId', v === '' ? null : Number(v));
                }}
                className="w-full px-3 py-2 border border-gray-300 rounded text-sm bg-white"
                disabled={!groupId || allTourists.isLoading}
              >
                <option value="">— выбрать —</option>
                {parentCandidates.map((t) => (
                  <option key={t.id} value={t.id}>
                    {t.lastName} {t.firstName}
                    {t.middleName ? ` ${t.middleName}` : ''} ({t.touristType})
                  </option>
                ))}
              </select>
              <div className="text-xs text-gray-500 mt-1">
                {!groupId
                  ? 'Сначала выберите группу выше'
                  : allTourists.isLoading
                  ? 'Загружаем кандидатов…'
                  : parentCandidates.length === 0
                  ? 'В этой группе нет взрослых туристов. Создайте сначала родителя.'
                  : `Доступно кандидатов: ${parentCandidates.length}`}
              </div>
            </div>
          )}

          {error && <Alert severity="error">{extractMessage(error)}</Alert>}
        </div>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={saving}>
          Отмена
        </Button>
        <Button onClick={handleSubmit} variant="contained" disabled={saving}>
          {saving ? <CircularProgress size={20} color="inherit" /> : 'Сохранить'}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
