import { useState } from 'react';
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
  DialogContentText,
  DialogActions,
} from '@mui/material';
import {
  DataGrid,
  type GridColDef,
  type GridPaginationModel,
  type GridSortModel,
} from '@mui/x-data-grid';
import { Add, Edit, Delete } from '@mui/icons-material';
import { useCrudResource } from '../../api/useCrudResource';
import { extractMessage } from '../../api/client';
import { CrudDialog } from './CrudDialog';
import type { FieldDef } from './FieldDef';
import { useAuth } from '../../auth/AuthContext';

interface Props<T extends { id?: number }> {
  baseUrl: string;
  title: string;
  columns: GridColDef[];
  fields: FieldDef<T>[];
  createDialogTitle: string;
  editDialogTitle: string;
  writePermission: string;
  prepareDto?: (dto: T) => T;
}

export function CrudTable<T extends { id?: number }>({
  baseUrl,
  title,
  columns,
  fields,
  createDialogTitle,
  editDialogTitle,
  writePermission,
  prepareDto,
}: Props<T>) {
  const { hasPermission } = useAuth();
  const canMutate = hasPermission(writePermission);

  const [pagination, setPagination] = useState<GridPaginationModel>({
    page: 0,
    pageSize: 20,
  });
  const [sortModel, setSortModel] = useState<GridSortModel>([]);

  const sort = sortModel[0]
    ? `${sortModel[0].field},${sortModel[0].sort ?? 'asc'}`
    : undefined;

  const {
    list,
    listLoading,
    listError,
    createMutation,
    updateMutation,
    deleteMutation,
  } = useCrudResource<T>({
    baseUrl,
    page: pagination.page,
    size: pagination.pageSize,
    sort,
  });

  const [editing, setEditing] = useState<T | null | 'new'>(null);
  const [deleting, setDeleting] = useState<T | null>(null);

  function handleSubmit(dto: T) {
    const prepared = prepareDto ? prepareDto(dto) : dto;
    if (editing === 'new') {
      createMutation.mutate(prepared, {
        onSuccess: () => setEditing(null),
      });
    } else if (editing) {
      const id = (editing as T).id;
      if (id == null) return;
      updateMutation.mutate(
        { id, dto: prepared },
        { onSuccess: () => setEditing(null) },
      );
    }
  }

  function handleDelete() {
    if (!deleting?.id) return;
    deleteMutation.mutate(deleting.id, {
      onSuccess: () => setDeleting(null),
    });
  }

  const actionsColumn: GridColDef = {
    field: '__actions',
    headerName: '',
    sortable: false,
    filterable: false,
    width: 100,
    align: 'right',
    headerAlign: 'right',
    renderCell: (params) => (
      <div className="flex gap-1">
        {canMutate && (
          <>
            <Tooltip title="Редактировать">
              <IconButton size="small" onClick={() => setEditing(params.row)}>
                <Edit fontSize="small" />
              </IconButton>
            </Tooltip>
            <Tooltip title="Удалить">
              <IconButton
                size="small"
                onClick={() => setDeleting(params.row)}
                color="error"
              >
                <Delete fontSize="small" />
              </IconButton>
            </Tooltip>
          </>
        )}
      </div>
    ),
  };

  const allColumns = [...columns, actionsColumn];

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-gray-900">{title}</h1>
        {canMutate && (
          <Button variant="contained" startIcon={<Add />} onClick={() => setEditing('new')}>
            Добавить
          </Button>
        )}
      </div>

      {listError ? (
        <Alert severity="error" className="mb-4">
          {extractMessage(listError)}
        </Alert>
      ) : null}

      {deleteMutation.error ? (
        <Alert severity="error" className="mb-4">
          {extractMessage(deleteMutation.error)}
        </Alert>
      ) : null}

      <Card>
        <CardContent className="p-0">
          <DataGrid
            rows={list?.content ?? []}
            columns={allColumns}
            getRowId={(row) => row.id}
            loading={listLoading}
            paginationMode="server"
            sortingMode="server"
            paginationModel={pagination}
            onPaginationModelChange={setPagination}
            sortModel={sortModel}
            onSortModelChange={setSortModel}
            rowCount={list?.totalElements ?? 0}
            pageSizeOptions={[10, 20, 50, 100]}
            disableRowSelectionOnClick
            autoHeight
            sx={{ border: 0 }}
          />
        </CardContent>
      </Card>

      <CrudDialog<T>
        open={editing !== null}
        initialValue={editing === 'new' ? null : editing}
        fields={fields}
        title={editing === 'new' ? createDialogTitle : editDialogTitle}
        saving={createMutation.isPending || updateMutation.isPending}
        error={createMutation.error ?? updateMutation.error}
        onSubmit={handleSubmit}
        onClose={() => setEditing(null)}
      />

      <Dialog open={deleting !== null} onClose={() => setDeleting(null)}>
        <DialogTitle>Удалить запись?</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Удаление необратимо. Если на запись ссылаются другие сущности,
            БД может отвергнуть операцию.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleting(null)} disabled={deleteMutation.isPending}>
            Отмена
          </Button>
          <Button
            onClick={handleDelete}
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
