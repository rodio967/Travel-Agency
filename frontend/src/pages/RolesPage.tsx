import { useState, useMemo, useEffect } from 'react';
import {
  Card,
  CardContent,
  Button,
  IconButton,
  Tooltip,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  DialogContentText,
  TextField,
  Checkbox,
  FormControlLabel,
  CircularProgress,
  Chip,
} from '@mui/material';
import {
  DataGrid,
  type GridColDef,
  type GridPaginationModel,
} from '@mui/x-data-grid';
import { Add, Edit, Delete, Lock } from '@mui/icons-material';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import {
  apiGet,
  apiGetPage,
  apiPost,
  apiPut,
  apiDelete,
  extractMessage,
} from '../api/client';
import type { Page } from '../types/common';
import type { PermissionDto, RoleDto } from '../types/acl';
import { useAuth } from '../auth/AuthContext';

const CATEGORY_LABELS: Record<string, string> = {
  TOURIST: 'Туристы и группы',
  LOGISTICS: 'Логистика',
  SERVICES: 'Услуги и финансы',
  ANALYTICS: 'Аналитика',
  ADMIN: 'Администрирование',
};

export function RolesPage() {
  const { hasPermission } = useAuth();
  const canMutate = hasPermission('ROLES_MANAGE');

  const [pagination, setPagination] = useState<GridPaginationModel>({
    page: 0,
    pageSize: 20,
  });
  const [editing, setEditing] = useState<RoleDto | 'new' | null>(null);
  const [deleting, setDeleting] = useState<RoleDto | null>(null);

  const queryClient = useQueryClient();
  const queryKey = ['crud', '/api/roles', pagination] as const;

  const listQuery = useQuery({
    queryKey,
    queryFn: () =>
      apiGetPage<RoleDto>('/api/roles', {
        page: pagination.page,
        size: pagination.pageSize,
      }),
  });

  function invalidate() {
    queryClient.invalidateQueries({ queryKey: ['crud', '/api/roles'] });
  }

  const deleteMutation = useMutation({
    mutationFn: (id: number) => apiDelete(`/api/roles/${id}`),
    onSuccess: () => {
      invalidate();
      setDeleting(null);
    },
  });

  const list = listQuery.data as Page<RoleDto> | undefined;

  const columns: GridColDef[] = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'name', headerName: 'Название', flex: 1, minWidth: 130 },
    { field: 'description', headerName: 'Описание', flex: 2, minWidth: 200 },
    {
      field: 'systemic',
      headerName: 'Системная',
      width: 110,
      renderCell: (p) =>
        p.value ? (
          <Tooltip title="Системная роль">
            <Lock fontSize="small" className="text-amber-600" />
          </Tooltip>
        ) : null,
    },
    {
      field: 'permissionCodes',
      headerName: 'Права',
      flex: 1,
      sortable: false,
      valueGetter: (value: string[]) =>
        `${(value ?? []).length} permissions`,
    },
    {
      field: '__actions',
      headerName: '',
      sortable: false,
      filterable: false,
      width: 100,
      align: 'right',
      headerAlign: 'right',
      renderCell: (p) => (
        <div className="flex gap-1">
          {canMutate && (
            <>
              <Tooltip title="Редактировать">
                <IconButton size="small" onClick={() => setEditing(p.row)}>
                  <Edit fontSize="small" />
                </IconButton>
              </Tooltip>
              {!p.row.systemic && (
                <Tooltip title="Удалить">
                  <IconButton
                    size="small"
                    onClick={() => setDeleting(p.row)}
                    color="error"
                  >
                    <Delete fontSize="small" />
                  </IconButton>
                </Tooltip>
              )}
            </>
          )}
        </div>
      ),
    },
  ];

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-semibold text-gray-900">Роли и разрешения</h1>
        </div>
        {canMutate && (
          <Button
            variant="contained"
            startIcon={<Add />}
            onClick={() => setEditing('new')}
          >
            Создать роль
          </Button>
        )}
      </div>

      {listQuery.error && (
        <Alert severity="error" className="mb-4">
          {extractMessage(listQuery.error)}
        </Alert>
      )}
      {deleteMutation.error && (
        <Alert severity="error" className="mb-4">
          {extractMessage(deleteMutation.error)}
        </Alert>
      )}

      <Card>
        <CardContent className="p-0">
          <DataGrid
            rows={list?.content ?? []}
            columns={columns}
            getRowId={(r) => r.id}
            loading={listQuery.isLoading}
            paginationMode="server"
            paginationModel={pagination}
            onPaginationModelChange={setPagination}
            rowCount={list?.totalElements ?? 0}
            pageSizeOptions={[10, 20, 50]}
            disableRowSelectionOnClick
            autoHeight
            sx={{ border: 0 }}
          />
        </CardContent>
      </Card>

      <RoleEditor
        open={editing !== null}
        initialValue={editing === 'new' ? null : editing}
        onClose={() => setEditing(null)}
        onSaved={() => {
          invalidate();
          setEditing(null);
        }}
      />

      <Dialog open={deleting !== null} onClose={() => setDeleting(null)}>
        <DialogTitle>Удалить роль?</DialogTitle>
        <DialogContent>
          <DialogContentText>
            {deleting && `Роль "${deleting.name}" будет удалена.`} Если на неё
            ссылаются пользователи, БД отвергнет операцию.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleting(null)}>Отмена</Button>
          <Button
            onClick={() => deleting?.id && deleteMutation.mutate(deleting.id)}
            color="error"
            variant="contained"
            disabled={deleteMutation.isPending}
          >
            Удалить
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

function RoleEditor({
  open,
  initialValue,
  onClose,
  onSaved,
}: {
  open: boolean;
  initialValue: RoleDto | null;
  onClose: () => void;
  onSaved: () => void;
}) {
  const isEdit = initialValue !== null;
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [permissionIds, setPermissionIds] = useState<Set<number>>(new Set());

  const permsQuery = useQuery({
    queryKey: ['permissions'],
    queryFn: () => apiGet<PermissionDto[]>('/api/roles/permissions'),
    enabled: open,
    staleTime: 5 * 60_000,
  });

  const grouped = useMemo(() => {
    const all = permsQuery.data ?? [];
    const map = new Map<string, PermissionDto[]>();
    for (const p of all) {
      if (!map.has(p.category)) map.set(p.category, []);
      map.get(p.category)!.push(p);
    }

    for (const list of map.values()) {
      list.sort((a, b) => a.code.localeCompare(b.code));
    }
    return map;
  }, [permsQuery.data]);


  useEffect(() => {
    if (!open) return;
    if (initialValue) {
      setName(initialValue.name);
      setDescription(initialValue.description ?? '');
      setPermissionIds(new Set(initialValue.permissionIds ?? []));
    } else {
      setName('');
      setDescription('');
      setPermissionIds(new Set());
    }
  }, [open, initialValue]);

  const saveMutation = useMutation({
    mutationFn: (dto: RoleDto) =>
      isEdit && initialValue?.id != null
        ? apiPut<RoleDto>(`/api/roles/${initialValue.id}`, dto)
        : apiPost<RoleDto>('/api/roles', dto),
    onSuccess: onSaved,
  });

  function togglePermission(id: number) {
    setPermissionIds((prev) => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      return next;
    });
  }

  function toggleCategory(perms: PermissionDto[]) {
    const allSelected = perms.every((p) => permissionIds.has(p.id));
    setPermissionIds((prev) => {
      const next = new Set(prev);
      if (allSelected) {
        perms.forEach((p) => next.delete(p.id));
      } else {
        perms.forEach((p) => next.add(p.id));
      }
      return next;
    });
  }

  function handleSave() {
    saveMutation.mutate({
      name,
      description,
      permissionIds: Array.from(permissionIds),
    });
  }

  const isSystemic = initialValue?.systemic;

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <DialogTitle>{isEdit ? `Редактирование роли «${initialValue?.name}»` : 'Новая роль'}</DialogTitle>
      <DialogContent>
        <div className="flex flex-col gap-4 mt-2">
          <TextField
            label="Название"
            size="small"
            value={name}
            onChange={(e) => setName(e.target.value)}
            disabled={isSystemic}
            helperText={
              isSystemic
                ? 'Системная роль — имя нельзя менять'
                : 'Например: AUDITOR'
            }
            required
          />
          <TextField
            label="Описание"
            size="small"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            multiline
            minRows={2}
          />

          <div className="text-sm text-gray-600 mt-2">
            Разрешения ({permissionIds.size} выбрано)
          </div>
          {permsQuery.isLoading && <CircularProgress size={20} />}

          {Array.from(grouped.entries()).map(([category, perms]) => {
            const allSelected = perms.every((p) => permissionIds.has(p.id));
            const someSelected = perms.some((p) => permissionIds.has(p.id));
            return (
              <Card key={category} variant="outlined">
                <CardContent className="p-3">
                  <div className="flex items-center justify-between mb-2">
                    <div className="font-medium text-gray-800">
                      {CATEGORY_LABELS[category] ?? category}
                    </div>
                    <Chip
                      label={allSelected ? 'Снять все' : 'Выбрать все'}
                      size="small"
                      onClick={() => toggleCategory(perms)}
                      variant={someSelected ? 'filled' : 'outlined'}
                      color={allSelected ? 'primary' : 'default'}
                    />
                  </div>
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-1">
                    {perms.map((p) => (
                      <FormControlLabel
                        key={p.id}
                        control={
                          <Checkbox
                            size="small"
                            checked={permissionIds.has(p.id)}
                            onChange={() => togglePermission(p.id)}
                          />
                        }
                        label={
                          <div>
                            <div className="text-sm">{p.description}</div>
                            <div className="text-xs text-gray-500 font-mono">
                              {p.code}
                            </div>
                          </div>
                        }
                      />
                    ))}
                  </div>
                </CardContent>
              </Card>
            );
          })}

          {saveMutation.error && (
            <Alert severity="error">{extractMessage(saveMutation.error)}</Alert>
          )}
        </div>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={saveMutation.isPending}>
          Отмена
        </Button>
        <Button
          onClick={handleSave}
          variant="contained"
          disabled={saveMutation.isPending || !name.trim()}
        >
          {saveMutation.isPending ? (
            <CircularProgress size={20} color="inherit" />
          ) : (
            'Сохранить'
          )}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
