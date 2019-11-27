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
docker-compose up -d
```
It is important that when stopping the project you should use:
```
docker-compose down
```
To fully shut it down, else there will be issues when starting it up again.

## Configurations
There are some configurations that should be checked before deploying the program to any
non-dev environments. The file `env.properties` in the `nha-init` folder needs to be updated
with username and passwords.

To populate the database or recover a database dump, simply add it to the `nha-db` folder as a 
.sql file. It will automatically be run by MySQL at startup, in alphabetical order.
If you have a database dump it might be a good idea to remove the `_populateTables.sql` file.  
When the database starts up it will mirror the `/var/lib/mysql/` from the docker container to
a `mysql-storage` folder locally. **Be aware removing this folder will reset the database.**  

Changing which ports Wildfly should expose to the host can be done by changing the `docker-compose.yml` file.
They are specified under the `ports:` section and are of the `HOST : CONTAINER` format. So to map port 9999 from
the host to the containers port 8443 change the line: `"8443":"8443"` to `"9999":"8443"`.

## Dump or restore database
To dump or restore the database from Docker run:
```
# Backup
docker exec nha-db /usr/bin/mysqldump -u root --password=ROOT_PW DATABASE > backup.sql

# Restore
cat backup.sql | docker exec -i nha-db /usr/bin/mysql -u root --password=ROOT_PW DATABASE
```

Where DATABASE is the database you want to restore or dump and ROOT_PW is the root password set
in `env.properties`.