FROM ubuntu:latest

RUN apt-get update -y
RUN apt-get install -y curl
RUN curl -sL https://deb.nodesource.com/setup_10.x | bash -
RUN apt-get install -y nodejs
WORKDIR /var/www/

RUN npm install
RUN npm install -g @angular/cli
RUN npm install --save-dev @angular/cli

EXPOSE 4200

CMD ["ng", "serve", "--host", "0.0.0.0"]

