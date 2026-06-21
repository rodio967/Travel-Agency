import { NavLink } from 'react-router-dom';
import {
  Dashboard as DashboardIcon,
  Hotel as HotelIcon,
  Flight as FlightIcon,
  Group as GroupIcon,
  Person as PersonIcon,
  Tour as TourIcon,
  Business as BusinessIcon,
  Inventory as InventoryIcon,
  AccountBalance as FinanceIcon,
  Assessment as ReportIcon,
  Code as SqlIcon,
  AdminPanelSettings as AdminIcon,
  Security as SecurityIcon,
  CardTravel as VisaIcon,
  MeetingRoom as RoomIcon,
  EventNote as BookingIcon,
} from '@mui/icons-material';
import { useAuth } from '../../auth/AuthContext';

interface NavItem {
  to: string;
  label: string;
  icon: React.ReactNode;
  requiredPermission?: string | null;
  section: 'main' | 'tourists' | 'logistics' | 'reports' | 'admin';
}

const items: NavItem[] = [
  { to: '/',                    label: 'Dashboard',     icon: <DashboardIcon />, section: 'main' },

  { to: '/groups',              label: 'Группы',        icon: <GroupIcon />,     section: 'tourists', requiredPermission: 'GROUP_READ' },
  { to: '/tourists',            label: 'Туристы',       icon: <PersonIcon />,    section: 'tourists', requiredPermission: 'TOURIST_READ' },
  { to: '/visas',               label: 'Визы',          icon: <VisaIcon />,      section: 'tourists', requiredPermission: 'VISA_READ' },

  { to: '/hotels',              label: 'Гостиницы',     icon: <HotelIcon />,     section: 'logistics', requiredPermission: 'HOTEL_READ' },
  { to: '/hotel-bookings',      label: 'Расселение',    icon: <RoomIcon />,      section: 'logistics', requiredPermission: 'HOTEL_BOOKING_READ' },
  { to: '/flights',             label: 'Рейсы',         icon: <FlightIcon />,    section: 'logistics', requiredPermission: 'FLIGHT_READ' },
  { to: '/cargo',               label: 'Груз',          icon: <InventoryIcon />, section: 'logistics', requiredPermission: 'CARGO_READ' },

  { to: '/excursion-agencies',  label: 'Агентства',     icon: <BusinessIcon />,  section: 'reports',   requiredPermission: 'AGENCY_READ' },
  { to: '/excursions',          label: 'Экскурсии',     icon: <TourIcon />,      section: 'reports',   requiredPermission: 'EXCURSION_READ' },
  { to: '/excursion-bookings',  label: 'Записи',        icon: <BookingIcon />,   section: 'reports',   requiredPermission: 'EXCURSION_BOOKING_READ' },
  { to: '/finance',             label: 'Финансы',       icon: <FinanceIcon />,   section: 'reports',   requiredPermission: 'FINANCE_READ' },
  { to: '/reports',             label: 'Отчёты',        icon: <ReportIcon />,    section: 'reports',   requiredPermission: 'REPORT_VIEW' },

  { to: '/sql-console',         label: 'SQL-консоль',   icon: <SqlIcon />,       section: 'admin',     requiredPermission: 'RAW_SQL_EXEC' },
  { to: '/users',               label: 'Пользователи',  icon: <AdminIcon />,     section: 'admin',     requiredPermission: 'USER_MANAGE' },
  { to: '/roles',               label: 'Роли',          icon: <SecurityIcon />,  section: 'admin',     requiredPermission: 'ROLES_MANAGE' },
];

const SECTION_LABELS: Record<NavItem['section'], string | null> = {
  main: null,
  tourists: 'Туристы',
  logistics: 'Логистика',
  reports: 'Услуги и отчёты',
  admin: 'Администрирование',
};

export function Sidebar() {
  const { hasPermission } = useAuth();
  const visible = items.filter(
    (it) => !it.requiredPermission || hasPermission(it.requiredPermission),
  );

  const sections = (['main', 'tourists', 'logistics', 'reports', 'admin'] as const)
    .map((sec) => ({
      key: sec,
      label: SECTION_LABELS[sec],
      items: visible.filter((it) => it.section === sec),
    }))
    .filter((s) => s.items.length > 0);

  return (
    <aside className="w-64 bg-slate-900 text-slate-200 flex flex-col h-screen sticky top-0">
      <div className="px-6 py-5 border-b border-slate-800">
        <div className="text-lg font-semibold text-white">Travel Agency</div>
        <div className="text-xs text-slate-400 mt-1">Admin Console</div>
      </div>
      <nav className="flex-1 overflow-y-auto py-2">
        {sections.map((section, i) => (
          <div key={section.key} className={i > 0 ? 'mt-4' : ''}>
            {section.label && (
              <div className="px-6 py-1 text-[10px] uppercase tracking-wider text-slate-500 font-semibold">
                {section.label}
              </div>
            )}
            {section.items.map((it) => (
              <NavLink
                key={it.to}
                to={it.to}
                end={it.to === '/'}
                className={({ isActive }) =>
                  `flex items-center gap-3 px-6 py-2 text-sm transition ${
                    isActive
                      ? 'bg-slate-800 text-white border-l-2 border-sky-400'
                      : 'text-slate-300 hover:bg-slate-800/60 hover:text-white border-l-2 border-transparent'
                  }`
                }
              >
                <span className="text-current text-[20px]">{it.icon}</span>
                <span>{it.label}</span>
              </NavLink>
            ))}
          </div>
        ))}
      </nav>
      <div className="p-4 text-xs text-slate-500 border-t border-slate-800">
        Travel Agency
      </div>
    </aside>
  );
}
