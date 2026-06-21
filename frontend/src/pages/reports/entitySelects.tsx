import { TextField, MenuItem } from '@mui/material';
import { useRawList } from '../../api/useOptions';
import type {
  TouristGroupDto,
  HotelDto,
  FlightDto,
} from '../../types/dto';

/**
 * Переиспользуемые выпадающие списки для форм параметров отчётов.
 * Вместо ручного ввода ID пользователь выбирает существующую запись.
 * Список грузится через useRawList (кэш 30с), пока грузится — селект отключён.
 */

interface SelectProps {
  value: number | '';
  onChange: (id: number) => void;
  label?: string;
  width?: number;
}

export function GroupSelect({ value, onChange, label = 'Группа', width = 220 }: SelectProps) {
  const { data, isLoading } = useRawList<TouristGroupDto>('/api/groups');
  return (
    <TextField
      label={label}
      select
      size="small"
      value={isLoading ? '' : value}
      onChange={(e) => onChange(Number(e.target.value))}
      sx={{ width }}
      disabled={isLoading}
      helperText={isLoading ? 'Загрузка…' : undefined}
    >
      {(data ?? []).map((g) => (
        <MenuItem key={g.id} value={g.id}>
          #{g.id} · {g.name}
        </MenuItem>
      ))}
    </TextField>
  );
}

export function HotelSelect({ value, onChange, label = 'Гостиница', width = 240 }: SelectProps) {
  const { data, isLoading } = useRawList<HotelDto>('/api/hotels');
  return (
    <TextField
      label={label}
      select
      size="small"
      value={isLoading ? '' : value}
      onChange={(e) => onChange(Number(e.target.value))}
      sx={{ width }}
      disabled={isLoading}
      helperText={isLoading ? 'Загрузка…' : undefined}
    >
      {(data ?? []).map((h) => (
        <MenuItem key={h.id} value={h.id}>
          #{h.id} · {h.name}
        </MenuItem>
      ))}
    </TextField>
  );
}

export function FlightSelect({ value, onChange, label = 'Рейс', width = 260 }: SelectProps) {
  const { data, isLoading } = useRawList<FlightDto>('/api/flights');
  return (
    <TextField
      label={label}
      select
      size="small"
      value={isLoading ? '' : value}
      onChange={(e) => onChange(Number(e.target.value))}
      sx={{ width }}
      disabled={isLoading}
      helperText={isLoading ? 'Загрузка…' : undefined}
    >
      {(data ?? []).map((f) => (
        <MenuItem key={f.id} value={f.id}>
          #{f.id} · {f.flightNumber} ({f.flightDate})
        </MenuItem>
      ))}
    </TextField>
  );
}
