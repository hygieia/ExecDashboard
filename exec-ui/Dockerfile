FROM node:8.11 as build
WORKDIR /var/app
COPY .angular-cli.json ./.angular-cli.json
COPY tsconfig.json ./tsconfig.json
COPY package.json ./package.json
COPY package-lock.json ./package-lock.json
COPY src ./src
RUN npm install && \
    npm run-script build-prod

FROM httpd:2.4
ENV API_URL http://localhost:8080
COPY --from=build /var/app/dist /usr/local/apache2/htdocs/
COPY docker-util/docker-entrypoint.sh /usr/local/bin/
ENTRYPOINT ["docker-entrypoint.sh"]
CMD ["httpd-foreground"]
