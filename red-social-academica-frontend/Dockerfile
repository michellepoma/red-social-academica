# Etapa de construcción
FROM node:20-alpine as builder
WORKDIR /app
COPY . .
RUN npm install && npm run build --prod

# Etapa de despliegue
FROM nginx:alpine
COPY --from=builder /app/dist/frontend /usr/share/nginx/html
EXPOSE 80