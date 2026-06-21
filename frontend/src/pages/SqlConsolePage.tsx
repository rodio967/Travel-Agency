import { useState } from 'react';
import {
  Card,
  CardContent,
  Button,
  TextField,
  Alert,
  CircularProgress,
  Chip,
} from '@mui/material';
import { PlayArrow, History } from '@mui/icons-material';
import { useMutation } from '@tanstack/react-query';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import { apiPost, extractMessage } from '../api/client';

interface RawSqlResponse {
  columns: string[];
  rows: Array<Record<string, unknown>>;
  rowCount: number;
  truncated: boolean;
}

const SAMPLE_QUERIES = [
  {
    label: 'Туристы по типу',
    sql: 'select tourist_type, count(*) as cnt from tourist group by tourist_type',
  },
  {
    label: 'Гостиницы',
    sql: 'select id, name, address, stars from hotel order by stars desc nulls last',
  },
  {
    label: 'Группы и кол-во туристов',
    sql: `select g.id, g.name, count(t.id) as tourists
          from tourist_group g
          left join tourist t on t.group_id = g.id
          group by g.id, g.name
          order by g.arrival_date desc`,
  },
];

export function SqlConsolePage() {
  const [sql, setSql] = useState('select id, name from hotel');
  const [limit, setLimit] = useState(100);

  const mutation = useMutation({
    mutationFn: () =>
      apiPost<RawSqlResponse>('/api/raw-sql/query', { sql, limit }),
  });

  const result = mutation.data;
  const gridColumns: GridColDef[] = (result?.columns ?? []).map((name) => ({
    field: name,
    headerName: name,
    flex: 1,
    minWidth: 120,
    sortable: false,
    valueFormatter: (value: unknown) => {
      if (value === null || value === undefined) return '—';
      if (typeof value === 'object') return JSON.stringify(value);
      return String(value);
    },
  }));

  const gridRows =
    result?.rows.map((row, i) => ({ id: i, ...row })) ?? [];

  function loadSample(s: string) {
    setSql(s);
  }

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-semibold text-gray-900">SQL-консоль</h1>
          <p className="text-sm text-gray-500 mt-1">
            Только SELECT. Доступ к app_user и системным таблицам запрещён.
          </p>
        </div>
      </div>

      <Card className="mb-4">
        <CardContent className="p-4">
          <div className="mb-3">
            <div className="text-sm text-gray-600 mb-2 flex items-center gap-2">
              <History fontSize="small" /> Примеры:
            </div>
            <div className="flex flex-wrap gap-2">
              {SAMPLE_QUERIES.map((q) => (
                <Chip
                  key={q.label}
                  label={q.label}
                  size="small"
                  onClick={() => loadSample(q.sql)}
                  variant="outlined"
                  clickable
                />
              ))}
            </div>
          </div>

          <TextField
            label="SQL-запрос"
            multiline
            minRows={6}
            maxRows={15}
            value={sql}
            onChange={(e) => setSql(e.target.value)}
            fullWidth
            InputProps={{
              style: {
                fontFamily: '"JetBrains Mono", Menlo, Consolas, monospace',
                fontSize: 13,
              },
            }}
          />

          <div className="flex items-center gap-3 mt-3">
            <TextField
              label="Лимит строк"
              type="number"
              size="small"
              value={limit}
              onChange={(e) => setLimit(Number(e.target.value) || 100)}
              inputProps={{ min: 1, max: 10000 }}
              sx={{ width: 140 }}
            />
            <Button
              variant="contained"
              startIcon={
                mutation.isPending ? (
                  <CircularProgress size={16} color="inherit" />
                ) : (
                  <PlayArrow />
                )
              }
              onClick={() => mutation.mutate()}
              disabled={mutation.isPending || !sql.trim()}
            >
              Выполнить
            </Button>
            {result && (
              <span className="text-sm text-gray-600 ml-auto">
                Получено: {result.rowCount} строк
                {result.truncated && (
                  <span className="text-amber-600 ml-2">
                    (обрезано до {limit})
                  </span>
                )}
              </span>
            )}
          </div>

          {mutation.error && (
            <Alert severity="error" className="mt-3">
              {extractMessage(mutation.error)}
            </Alert>
          )}
        </CardContent>
      </Card>

      {result && (
        <Card>
          <CardContent className="p-0">
            <DataGrid
              rows={gridRows}
              columns={gridColumns}
              pageSizeOptions={[25, 50, 100]}
              initialState={{ pagination: { paginationModel: { pageSize: 25 } } }}
              disableRowSelectionOnClick
              autoHeight
              sx={{ border: 0 }}
            />
          </CardContent>
        </Card>
      )}
    </div>
  );
}
