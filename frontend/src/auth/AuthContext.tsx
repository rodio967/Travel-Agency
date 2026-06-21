import { createContext, useContext, useEffect, useState, type ReactNode } from 'react';
import type { CurrentUser } from '../types/common';
import { apiGet } from '../api/client';

interface AuthContextValue {
  user: CurrentUser | null;
  loading: boolean;
  login: (username: string, password: string) => Promise<void>;
  logout: () => void;
  hasRole: (role: string) => boolean;
  hasPermission: (code: string) => boolean;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

const STORAGE_KEY = 'auth.basic';


export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<CurrentUser | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const stored = sessionStorage.getItem(STORAGE_KEY);
    if (!stored) {
      setLoading(false);
      return;
    }
    apiGet<CurrentUser>('/api/me')
      .then(setUser)
      .catch(() => {
        sessionStorage.removeItem(STORAGE_KEY);
      })
      .finally(() => setLoading(false));
  }, []);

  async function login(username: string, password: string) {
    const encoded = btoa(`${username}:${password}`);
    sessionStorage.setItem(STORAGE_KEY, encoded);
    try {
      const me = await apiGet<CurrentUser>('/api/me');
      setUser(me);
    } catch (err) {
      sessionStorage.removeItem(STORAGE_KEY);
      throw err;
    }
  }

  function logout() {
    sessionStorage.removeItem(STORAGE_KEY);
    setUser(null);
  }

  function hasRole(role: string): boolean {
    if (!user) return false;
    const normalized = role.startsWith('ROLE_') ? role : `ROLE_${role}`;
    return user.roles.includes(normalized);
  }


  function hasPermission(code: string): boolean {
    if (!user) return false;
    return user.permissions.includes(code);
  }

  return (
    <AuthContext.Provider value={{ user, loading, login, logout, hasRole, hasPermission }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}


export function getStoredCredentials(): string | null {
  return sessionStorage.getItem(STORAGE_KEY);
}
