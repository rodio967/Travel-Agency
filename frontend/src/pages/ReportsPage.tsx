import { Link as RouterLink } from 'react-router-dom';
import { Card, CardContent } from '@mui/material';
import {
  Group as GroupIcon,
  Hotel as HotelIcon,
  Tour as TourIcon,
  Flight as FlightIcon,
  Inventory as InventoryIcon,
  AccountBalance as FinanceIcon,
  PieChart as PieIcon,
  Star as StarIcon,
} from '@mui/icons-material';

interface ReportLink {
  to: string;
  title: string;
  description: string;
  icon: React.ReactNode;
}

const REPORTS: ReportLink[] = [
  {
    to: 'customs',
    title: 'Список для таможни',
    description: 'Туристы группы — полный список или по категории.',
    icon: <GroupIcon />,
  },
  {
    to: 'hotels-guests',
    title: 'Расселение в гостинице',
    description: 'Туристы, расселённые в указанной гостинице. С фильтрами.',
    icon: <HotelIcon />,
  },
  {
    to: 'tourists-count',
    title: 'Количество туристов за период',
    description: 'Сколько человек побывало в стране, всего или по категории.',
    icon: <GroupIcon />,
  },
  {
    to: 'tourist-visits',
    title: 'Приезды конкретного человека',
    description: 'История поездок по номеру паспорта.',
    icon: <GroupIcon />,
  },
  {
    to: 'category-share',
    title: 'Соотношение REST/SHOP/CHILD',
    description: 'Доли категорий туристов, всего или за период.',
    icon: <PieIcon />,
  },
  {
    to: 'hotels-occupancy',
    title: 'Занятость гостиниц',
    description: 'Загруженность гостиниц за период: номера и туристы.',
    icon: <HotelIcon />,
  },
  {
    to: 'excursions-tourists',
    title: 'Туристы на экскурсиях',
    description: 'Сколько разных туристов записано на экскурсии за период.',
    icon: <TourIcon />,
  },
  {
    to: 'top-excursions',
    title: 'Самые популярные экскурсии',
    description: 'Рейтинг по количеству записавшихся.',
    icon: <StarIcon />,
  },
  {
    to: 'top-agencies',
    title: 'Лучшие агентства',
    description: 'Топ агентств по рейтингу качества.',
    icon: <StarIcon />,
  },
  {
    to: 'flight-load',
    title: 'Загрузка рейса',
    description: 'Места, вес и объёмный вес груза на конкретном рейсе.',
    icon: <FlightIcon />,
  },
  {
    to: 'cargo-turnover',
    title: 'Грузооборот',
    description: 'Объём, вес груза и состав рейсов за период.',
    icon: <InventoryIcon />,
  },
  {
    to: 'cargo-types',
    title: 'Типы груза',
    description: 'Доля каждого типа груза в общем грузопотоке.',
    icon: <PieIcon />,
  },
  {
    to: 'flight-tourists',
    title: 'Туристы рейса',
    description: 'Сводка по рейсу: туристы, гостиницы, груз, маркировка.',
    icon: <FlightIcon />,
  },
  {
    to: 'finance-group',
    title: 'Финансы по группе',
    description: 'Доходы, расходы, баланс и рентабельность одной группы.',
    icon: <FinanceIcon />,
  },
  {
    to: 'finance-period',
    title: 'Финансы за период',
    description: 'Доходы и расходы за указанный период.',
    icon: <FinanceIcon />,
  },
  {
    to: 'finance-profitability',
    title: 'Глобальная рентабельность',
    description: 'Общая рентабельность представительства.',
    icon: <FinanceIcon />,
  },
];

export function ReportsPage() {
  return (
    <div>
      <h1 className="text-2xl font-semibold text-gray-900 mb-1">Отчёты</h1>
      <p className="text-sm text-gray-500 mb-6">
        Выберите нужный запрос
      </p>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {REPORTS.map((r) => (
          <RouterLink
            key={r.to}
            to={r.to}
            className="block hover:translate-y-[-1px] transition-transform"
          >
            <Card className="h-full hover:border-sky-400 cursor-pointer">
              <CardContent className="p-5">
                <div className="flex items-start gap-3">
                  <div className="w-10 h-10 rounded bg-sky-50 flex items-center justify-center flex-shrink-0 text-sky-600">
                    {r.icon}
                  </div>
                  <div>
                    <div className="font-medium text-gray-900 mb-1">{r.title}</div>
                    <div className="text-sm text-gray-500">{r.description}</div>
                  </div>
                </div>
              </CardContent>
            </Card>
          </RouterLink>
        ))}
      </div>
    </div>
  );
}
