import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { apiDelete, apiGetPage, apiPost, apiPut } from './client';
import type { Page } from '../types/common';

interface UseCrudResourceOptions {
  baseUrl: string;
  page: number;
  size: number;
  sort?: string;
}


export function useCrudResource<T extends { id?: number }>(opts: UseCrudResourceOptions) {
  const { baseUrl, page, size, sort } = opts;
  const queryClient = useQueryClient();
  const queryKey = ['crud', baseUrl, { page, size, sort }] as const;

  const listQuery = useQuery({
    queryKey,
    queryFn: () => apiGetPage<T>(baseUrl, { page, size, sort }),
  });

  function invalidateList() {
    queryClient.invalidateQueries({ queryKey: ['crud', baseUrl] });
  }

  const createMutation = useMutation({
    mutationFn: (dto: T) => apiPost<T>(baseUrl, dto),
    onSuccess: invalidateList,
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, dto }: { id: number; dto: T }) =>
      apiPut<T>(`${baseUrl}/${id}`, dto),
    onSuccess: invalidateList,
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => apiDelete(`${baseUrl}/${id}`),
    onSuccess: invalidateList,
  });

  return {
    list: listQuery.data as Page<T> | undefined,
    listLoading: listQuery.isLoading,
    listError: listQuery.error,
    listRefetch: listQuery.refetch,
    createMutation,
    updateMutation,
    deleteMutation,
  };
}
