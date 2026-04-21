# 🔧 BUILD
FROM node:20-alpine as build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

RUN npm run build


# 🚀 SERVE
FROM nginx:alpine

# 👇 copiar config personalizada
COPY nginx.conf /etc/nginx/nginx.conf

# 👇 copiar build
COPY --from=build /app/dist /usr/share/nginx/html

expose 80