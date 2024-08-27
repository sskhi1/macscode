FROM node:20-alpine AS build

WORKDIR /app

COPY . .

RUN npm install --production

RUN npm run build

FROM nginx:alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/build /app/build

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]