#!/bin/bash
cd backend
mvn -Dmaven.test.skip=true package
cd ..
docker-compose up --build
