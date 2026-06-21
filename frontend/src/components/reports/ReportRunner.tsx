import { useState, useEffect, type ReactNode } from 'react';
import { Card, CardContent, Button, Alert, CircularProgress } from '@mui/material';
import { PlayArrow } from '@mui/icons-material';
import { useMutation } from '@tanstack/react-query';
import { extractMessage } from '../../api/client';

interface ReportRunnerProps<TParams, TResult> {
  title: string;
  description?: string;
  defaultParams: TParams;
  renderParamsForm: (
    params: TParams,
    setParams: (p: TParams) => void,
  ) => ReactNode;

  fetchReport: (params: TParams) => Promise<TResult>;
  renderResult: (result: TResult, params: TParams) => ReactNode;
  autoRun?: boolean;
}

export function ReportRunner<TParams, TResult>({
  title,
  description,
  defaultParams,
  renderParamsForm,
  fetchReport,
  renderResult,
  autoRun,
}: ReportRunnerProps<TParams, TResult>) {
  const [params, setParams] = useState<TParams>(defaultParams);

  const mutation = useMutation({
    mutationFn: () => fetchReport(params),
  });

  useEffect(() => {
    if (autoRun) {
      mutation.mutate();
    }
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-semibold text-gray-900">{title}</h1>
      {description && (
        <p className="text-sm text-gray-500 mt-1 mb-6">{description}</p>
      )}

      {!autoRun && (
        <Card className="mb-4">
          <CardContent className="p-4">
            <div className="flex flex-col gap-3">
              {renderParamsForm(params, setParams)}
              <div>
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
                  disabled={mutation.isPending}
                >
                  Сформировать
                </Button>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {mutation.error && (
        <Alert severity="error" className="mb-4">
          {extractMessage(mutation.error)}
        </Alert>
      )}

      {mutation.data && renderResult(mutation.data, params)}
    </div>
  );
}
