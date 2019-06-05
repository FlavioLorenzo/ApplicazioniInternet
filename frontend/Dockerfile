FROM ubuntu:latest

COPY exercise4 /var/www/
RUN apt-get update -y
RUN apt-get install -y curl
RUN curl -sL https://deb.nodesource.com/setup_10.x | bash -
RUN apt-get install -y nodejs
WORKDIR /var/www/
RUN npm uninstall -g angular-cli
RUN npm uninstall --save-dev angular-cli
RUN rm -rf node_modules dist
RUN npm install -g @angular/cli
RUN npm install --save-dev @angular/cli
RUN npm install
EXPOSE 4200

CMD ["ng", "serve", "--host", "0.0.0.0"]

