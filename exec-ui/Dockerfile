FROM node:8.11

COPY . /var/app

WORKDIR /var/app

RUN npm install

EXPOSE 4200

ENTRYPOINT [ "npm", "run-script" ]

CMD [ "local" ]