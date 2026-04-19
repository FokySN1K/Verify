FROM node:22-alpine
WORKDIR /app

COPY package*.json ./
RUN if [ -f package-lock.json ]; then npm ci; else npm install; fi

COPY . .

EXPOSE 13000
CMD ["sh", "-lc", "npm run dev -- --host 0.0.0.0 --port ${FRONTEND_PORT:-13000}"]
