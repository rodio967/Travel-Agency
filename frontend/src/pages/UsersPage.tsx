import { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Alert,
  CircularProgress,
} from '@mui/material';
import type { GridColDef } from '@mui/x-data-grid';
import { useMutation } from '@tanstack/react-query';
import { CrudTable } from '../components/crud/CrudTable';
import type { FieldDef } from '../components/crud/FieldDef';
import { apiPost, extractMessage } from '../api/client';
import type { AppUserDto } from '../types/dto';
import type { RoleDto } from '../types/acl';

export function UsersPage() {
  const [pwdUserId, setPwdUserId] = useState<number | null>(null);

  const columns: GridColDef[] = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'username', headerName: 'Имя', flex: 1, minWidth: 150 },
    {
      field: 'roleNames',
      headerName: 'Роли',
      flex: 1,
      minWidth: 200,
      sortable: false,
      valueGetter: (value: string[]) => (value ?? []).join(', '),
    },
    { field: 'enabled', headerName: 'Активен', width: 100, type: 'boolean' },
    {
      field: '__pwd',
      headerName: '',
      width: 160,
      sortable: false,
      filterable: false,
      renderCell: (p) => (
        <Button size="small" onClick={() => setPwdUserId(p.row.id)} variant="text">
          Сменить пароль
        </Button>
      ),
    },
  ];

  const fields: FieldDef<AppUserDto>[] = [
    { name: 'username', label: 'Имя пользователя', type: 'text', required: true },
    {
      name: 'password',
      label: 'Пароль',
      type: 'text',
      required: true,
      hideOnEdit: true,
    },
    {
      name: 'roleIds',
      label: 'Роли',
      type: 'multiselect',
      required: true,
      optionsUrl: '/api/roles',
      optionLabel: (r: RoleDto) => r.name,
      helperText: 'Можно выбрать несколько',
    },
    { name: 'enabled', label: 'Активен', type: 'boolean' },
  ];

  return (
    <>
      <CrudTable<AppUserDto>
        baseUrl="/api/users"
        title="Пользователи системы"
        columns={columns}
        fields={fields}
        createDialogTitle="Новый пользователь"
        editDialogTitle="Редактирование пользователя"
        writePermission="USER_MANAGE"
      />
      <ChangePasswordDialog
        userId={pwdUserId}
        onClose={() => setPwdUserId(null)}
      />
    </>
  );
}

function ChangePasswordDialog({
  userId,
  onClose,
}: {
  userId: number | null;
  onClose: () => void;
}) {
  const [password, setPassword] = useState('');

  const mutation = useMutation({
    mutationFn: (newPassword: string) =>
      apiPost(`/api/users/${userId}/password`, { newPassword }),
    onSuccess: () => {
      setPassword('');
      onClose();
    },
  });

  return (
    <Dialog
      open={userId !== null}
      onClose={() => {
        setPassword('');
        onClose();
      }}
      maxWidth="xs"
      fullWidth
    >
      <DialogTitle>Сменить пароль пользователю #{userId}</DialogTitle>
      <DialogContent>
        <div className="flex flex-col gap-3 mt-2">
          <TextField
            label="Новый пароль"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            size="small"
            fullWidth
            autoFocus
          />
          {mutation.error && (
            <Alert severity="error">{extractMessage(mutation.error)}</Alert>
          )}
        </div>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={mutation.isPending}>
          Отмена
        </Button>
        <Button
          onClick={() => mutation.mutate(password)}
          variant="contained"
          disabled={mutation.isPending || !password}
        >
          {mutation.isPending ? (
            <CircularProgress size={20} color="inherit" />
          ) : (
            'Сохранить'
          )}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
