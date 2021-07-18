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

## Kubernetes

The kubernetes configuration files in [`kubernetes`](kubernetes/) are generated using [`kompose`](https://kompose.io/).

Before applying them, execute:
```shell script
eval $(minikube docker-env)
```
to set the docker environment from minikube.

Then, for all deployments, set `spec.template.spec.containers[0].imagePullPolicy=Never` to use local images.

Build every docker image as described in above section to make them available to the new docker environment.

Then, `cd` to `kubernetes/` folder, and execute
```shell script
kubectl apply -f .
```

After that, port forward to see the application in action as:
```shell script
kubectl port-forward server-<...> 8080:8080
```

And navigate to <http://localhost:8080/>
