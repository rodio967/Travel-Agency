import { useState } from 'react';
import { Avatar, IconButton, Menu, MenuItem, Divider } from '@mui/material';
import { useAuth } from '../../auth/AuthContext';

export function Header() {
  const { user, logout } = useAuth();
  const [anchor, setAnchor] = useState<HTMLElement | null>(null);

  if (!user) return null;
  const role = user.roles[0]?.replace('ROLE_', '') ?? '';

  return (
    <header className="h-16 bg-white border-b border-gray-200 flex items-center justify-end px-6 sticky top-0 z-10">
      <div className="flex items-center gap-3">
        <div className="text-right hidden sm:block">
          <div className="text-sm font-medium text-gray-900">{user.username}</div>
          <div className="text-xs text-gray-500">{role}</div>
        </div>
        <IconButton onClick={(e) => setAnchor(e.currentTarget)} size="small">
          <Avatar sx={{ width: 36, height: 36, bgcolor: '#0ea5e9', fontSize: 14 }}>
            {user.username[0]?.toUpperCase()}
          </Avatar>
        </IconButton>
        <Menu
          anchorEl={anchor}
          open={Boolean(anchor)}
          onClose={() => setAnchor(null)}
          anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
          transformOrigin={{ vertical: 'top', horizontal: 'right' }}
        >
          <MenuItem disabled>{user.username}</MenuItem>
          <Divider />
          <MenuItem
            onClick={() => {
              setAnchor(null);
              logout();
            }}
          >
            Выйти
          </MenuItem>
        </Menu>
      </div>
    </header>
  );
}
