import axios, { AxiosError } from 'axios';
import type { ApiError, Page } from '../types/common';
import { getStoredCredentials } from '../auth/AuthContext';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 30_000,
});

api.interceptors.request.use((config) => {
  const creds = getStoredCredentials();
  if (creds) {
    config.headers.Authorization = `Basic ${creds}`;
  }
  return config;
});

api.interceptors.response.use(
  (resp) => resp,
  (error: AxiosError<ApiError>) => {
    if (error.response?.status === 401) {
      sessionStorage.removeItem('auth.basic');

      if (!window.location.pathname.startsWith('/login')) {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  },
);


export async function apiGet<T>(url: string, params?: Record<string, unknown>): Promise<T> {
  const resp = await api.get<T>(url, { params });
  return resp.data;
}

export async function apiPost<T>(url: string, body?: unknown): Promise<T> {
  const resp = await api.post<T>(url, body);
  return resp.data;
}

export async function apiPut<T>(url: string, body?: unknown): Promise<T> {
  const resp = await api.put<T>(url, body);
  return resp.data;
}

export async function apiDelete(url: string): Promise<void> {
  await api.delete(url);
}


export async function apiGetPage<T>(
  url: string,
  params: { page?: number; size?: number; sort?: string } = {},
): Promise<Page<T>> {
  return apiGet<Page<T>>(url, params);
}


export function extractMessage(err: unknown): string {
  if (err && typeof err === 'object' && 'isAxiosError' in err) {
    const ax = err as AxiosError<ApiError>;
    return ax.response?.data?.message ?? ax.message ?? 'Network error';
  }
  return err instanceof Error ? err.message : String(err);
}

export default api;
