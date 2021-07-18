# Course Registration Quarkus

This project was intended as a learning experience in Quarkus + Hibernate ORM with deployment in Kubernetes

## Running the application in dev mode

```shell script
./mvnw compile quarkus:dev
```

Be sure to run the `postgres` image with port 5432 exposed.

Also change `quarkus.hibernate-orm.database.generation=drop-and-create` in [`/src/main/resources/application.properties`](/src/main/resources/application.properties) to generate the appropriate schema.

## Packaging, Containerization and Running

The application can be packaged using:
```shell script
./mvnw clean package
```

> Note: Building requires `rsync` as a dependency to copy frontend build files to quarkus directory

Build docker image of the server using:
```shell script
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/course-registration-jvm .
```

Build database image using:
```shell script
docker build -f src/main/docker/Dockerfile.database -t quarkus/course-registration-database .
```

Run the containers using:
```shell script
docker-compose up
```