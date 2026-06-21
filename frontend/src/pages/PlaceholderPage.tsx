import { Card, CardContent } from '@mui/material';
import { Construction } from '@mui/icons-material';

interface Props {
  title: string;
  description?: string;
}

export function PlaceholderPage({ title, description }: Props) {
  return (
    <div>
      <h1 className="text-2xl font-semibold text-gray-900 mb-6">{title}</h1>
      <Card>
        <CardContent className="p-12 text-center">
          <Construction sx={{ fontSize: 56, color: '#9ca3af' }} className="mb-3" />
          <p className="text-gray-700 font-medium mb-1">В разработке</p>
          <p className="text-sm text-gray-500">
            {description ?? 'Эта страница появится в следующих шагах.'}
          </p>
        </CardContent>
      </Card>
    </div>
  );
}
