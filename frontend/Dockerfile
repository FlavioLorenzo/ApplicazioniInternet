# base image
FROM node:latest

# set working directory
WORKDIR /var/www

# add `/app/node_modules/.bin` to $PATH
ENV PATH /var/www/node_modules/.bin:$PATH

# install and cache app dependencies
COPY package*.json /var/www/
RUN npm install
RUN npm install -g @angular/cli

# add app
COPY . /var/www

# start app
CMD ng serve --host 0.0.0.0


