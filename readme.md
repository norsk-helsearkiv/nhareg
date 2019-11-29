## INFO

This repository contains the registration solution for pasient journals for Norsk Helsearkiv.

## Compiling
This project is a Maven project using the standard commands to compile the project:
```
# To package
mvn clean package -Dhttps.protocols=TLSv1.2

# To run tests
mvn clean verify -Dhttps.protocols=TLSv1.2
```

## Running
The project can be run by using Docker:
```
docker run -e <env var> -e <env var> -p <port> -p <port> nhareg
```
or Docker compose:
```
docker-compose up
```
Then to shut it down run:
```
docker kill <container id>
```
or with Docker compose:
```
docker-compose down
```

## Configurations
The image comes with several default configurations that should be changed. To do so you need to pass these
environment variables when you launch the image:
- MYSQL_ROOT_PASSWORD: password to the *root* account on the database
- MYSQL_USER: user with access to the nhareg database.
- MYSQL_PASSWORD: password of the MYSQL_USER.
- WILDFLY_PASSWORD: password you want to set for the Wildfly admin console.

Given these variables the image automatically configure the applications on startup. There are several ways
to pass these variables:
1. docker-compose.yml - this file contains the necessary setup, simply uncomment and fill in the info.
2. pass the variables through the commandline:
```
docker run --env MYSQL_USER=nhareg --env MYSQL_PASSWORD=pass --env MYSQL_ROOT_PASSWORD=pass --env WILDFLY_PASSWORD=pass nhareg
```
or pass the variables through a file, containing variables of format <variable>=value:
```
docker run --env-file ./env.list nhareg
```

## Manual deployment
It is possible to manually deploy applications to the running Wildfly image, example:
```
docker cp ./tjeneste/target/api.war nha-app:/opt/jboss/wildfly/standalone/deployments
```
It is however recommended to simply use the latest image.