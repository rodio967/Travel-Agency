import { useQuery } from '@tanstack/react-query';
import { apiGetPage } from './client';

export interface SelectOption {
  value: number;
  label: string;
}

interface UseOptionsArgs<T> {
  url: string | null;
  toLabel: (item: T) => string;
}


export function useOptions<T extends { id?: number }>({
  url,
  toLabel,
}: UseOptionsArgs<T>) {
  return useQuery({
    queryKey: ['options', url],
    queryFn: async (): Promise<SelectOption[]> => {
      if (!url) return [];
      const page = await apiGetPage<T>(url, { page: 0, size: 1000, sort: 'id,asc' });
      return page.content
        .filter((item) => item.id != null)
        .map((item) => ({ value: item.id as number, label: toLabel(item) }));
    },
    enabled: url !== null,
    staleTime: 60_000,
  });
}


export function useRawList<T extends { id?: number }>(
  url: string | null,
  staleTime: number = 30_000,
) {
  return useQuery({
    queryKey: ['raw-list', url],
    queryFn: async (): Promise<T[]> => {
      if (!url) return [];
      const page = await apiGetPage<T>(url, { page: 0, size: 1000, sort: 'id,asc' });
      return page.content;
    },
    enabled: url !== null,
    staleTime,
  });
}
