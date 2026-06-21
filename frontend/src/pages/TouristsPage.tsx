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
  DialogActions,
  DialogContentText,
  CircularProgress,
} from '@mui/material';
import {
  DataGrid,
  type GridColDef,
  type GridPaginationModel,
} from '@mui/x-data-grid';
import { Add, Edit, Delete } from '@mui/icons-material';
import { useCrudResource } from '../api/useCrudResource';
import { extractMessage } from '../api/client';
import { useAuth } from '../auth/AuthContext';
import { TouristDialog } from '../components/TouristDialog';
import type { TouristDto } from '../types/dto';

const columns: GridColDef[] = [
  { field: 'id', headerName: 'ID', width: 70 },
  {
    field: 'touristType',
    headerName: 'Тип',
    width: 110,
    renderCell: (p) => {
      const v = p.value as string;
      const styles: Record<string, { bg: string; color: string }> = {
        REST:  { bg: '#dcfce7', color: '#166534' },
        SHOP:  { bg: '#fef3c7', color: '#92400e' },
        CHILD: { bg: '#e0f2fe', color: '#075985' },
      };
      const labels: Record<string, string> = {
        REST: 'Отдых',
        SHOP: 'Шоп',
        CHILD: 'Ребёнок',
      };
      const s = styles[v] ?? { bg: '#f3f4f6', color: '#374151' };
      return (
        <span
          style={{
            backgroundColor: s.bg,
            color: s.color,
            padding: '2px 10px',
            borderRadius: 12,
            fontSize: 12,
            fontWeight: 500,
            display: 'inline-block',
          }}
        >
          {labels[v] ?? v ?? '—'}
        </span>
      );
    },
  },
  { field: 'lastName', headerName: 'Фамилия', flex: 1, minWidth: 130 },
  { field: 'firstName', headerName: 'Имя', flex: 1, minWidth: 110 },
  { field: 'middleName', headerName: 'Отчество', flex: 1, minWidth: 110 },
  { field: 'gender', headerName: 'Пол', width: 80 },
  { field: 'birthDate', headerName: 'Дата рожд.', width: 120 },
  { field: 'passportNumber', headerName: 'Паспорт', width: 130 },
  { field: 'groupId', headerName: 'Группа', width: 90, type: 'number' },
];

export function TouristsPage() {
  const { hasPermission } = useAuth();
  const canMutate = hasPermission('TOURIST_WRITE');

  const [pagination, setPagination] = useState<GridPaginationModel>({
    page: 0,
    pageSize: 20,
  });

  const {
    list,
    listLoading,
    listError,
    createMutation,
    updateMutation,
    deleteMutation,
  } = useCrudResource<TouristDto>({
    baseUrl: '/api/tourists',
    page: pagination.page,
    size: pagination.pageSize,
  });

  const [editing, setEditing] = useState<TouristDto | 'new' | null>(null);
  const [deleting, setDeleting] = useState<TouristDto | null>(null);

  function handleSubmit(dto: TouristDto) {
    if (editing === 'new') {
      createMutation.mutate(dto, { onSuccess: () => setEditing(null) });
    } else if (editing) {
      const id = editing.id;
      if (id == null) return;
      updateMutation.mutate({ id, dto }, { onSuccess: () => setEditing(null) });
    }
  }

  function handleDelete() {
    if (!deleting?.id) return;
    deleteMutation.mutate(deleting.id, { onSuccess: () => setDeleting(null) });
  }

  const actionsColumn: GridColDef = {
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
            <Tooltip title="Удалить">
              <IconButton
                size="small"
                onClick={() => setDeleting(p.row)}
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

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-gray-900">Туристы</h1>
        {canMutate && (
          <Button
            variant="contained"
            startIcon={<Add />}
            onClick={() => setEditing('new')}
          >
            Добавить
          </Button>
        )}
      </div>

      {listError && (
        <Alert severity="error" className="mb-4">
          {extractMessage(listError)}
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
            columns={[...columns, actionsColumn]}
            getRowId={(r) => r.id}
            loading={listLoading}
            paginationMode="server"
            paginationModel={pagination}
            onPaginationModelChange={setPagination}
            rowCount={list?.totalElements ?? 0}
            pageSizeOptions={[10, 20, 50, 100]}
            disableRowSelectionOnClick
            autoHeight
            sx={{ border: 0 }}
          />
        </CardContent>
      </Card>

      <TouristDialog
        open={editing !== null}
        initialValue={editing === 'new' ? null : editing}
        saving={createMutation.isPending || updateMutation.isPending}
        error={createMutation.error ?? updateMutation.error}
        onSubmit={handleSubmit}
        onClose={() => setEditing(null)}
      />

      <Dialog open={deleting !== null} onClose={() => setDeleting(null)}>
        <DialogTitle>Удалить туриста?</DialogTitle>
        <DialogContent>
          <DialogContentText>
            {deleting && `${deleting.lastName} ${deleting.firstName}`}. Удаление
            необратимо. На туриста могут ссылаться визы, бронирования,
            экскурсии — БД отвергнет операцию, если связи остались.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => setDeleting(null)}
            disabled={deleteMutation.isPending}
          >
            Отмена
          </Button>
          <Button
            onClick={handleDelete}
            color="error"
            variant="contained"
            disabled={deleteMutation.isPending}
          >
            {deleteMutation.isPending ? (
              <CircularProgress size={20} color="inherit" />
            ) : (
              'Удалить'
            )}
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
