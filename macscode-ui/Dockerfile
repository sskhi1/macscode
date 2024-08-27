FROM node:20-alpine

WORKDIR /app

COPY . .

RUN npm install --production

EXPOSE 3000

ENV DANGEROUSLY_DISABLE_HOST_CHECK=true

CMD ["npm", "start"]