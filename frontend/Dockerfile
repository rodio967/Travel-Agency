FROM node:22-alpine
WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY . .

RUN mv src/pages/reports/entitySelects.tsx src/pages/reports/EntitySelects.tsx

CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
