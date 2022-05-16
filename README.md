# Getting Started

### Install GDAL on Unbuntu

For version 3.5.0:

```shell
sudo apt-get install python3-dev openjdk-11-jdk ant g++ cmake swig
cd ~
git clone https://github.com/OSGeo/gdal.git
cd gdal
mkdir build
cd build
cmake -DBUILD_JAVA_BINDINGS=ON ..
cmake --build .
sudo chgrp adm /usr/local/bin
sudo chgrp adm /usr/local/lib
sudo chgrp adm /usr/local/share
sudo chgrp adm /usr/local/include
sudo chmod g+w /usr/local/bin
sudo chmod g+w /usr/local/lib
sudo chmod g+w /usr/local/share
sudo chmod g+w /usr/local/include
cmake --build . --target install
sudo ln -s /usr/local/share/java/libgdalalljni.so /usr/lib/libgdalalljni.so
```

For version 3.4.0:

```shell
sudo apt-get install python3-dev openjdk-11-jdk ant g++ cmake swig
cd ~
wget https://github.com/OSGeo/gdal/releases/download/v3.4.0/gdal-3.4.0.tar.gz
gunzip gdal-3.4.0.tar.gz
tar -xvf gdal-3.4.0.tar
cd gdal-3.4.0
./configure --with-java=/usr/lib/jvm/java-11-openjdk-amd64/
make
```

Deprecated because version 3.0.4 installed :

```shell
sudo update;
sudo apt-get install gdal-bin gdal-data libgdal-dev libgdal-java;
sudo ln -s /usr/lib/jni/libgdalalljni.so /usr/lib/libgdalalljni.so;
sudo ln -s /usr/lib/jni/libgdalalljni.so.26 /usr/lib/libgdalalljni.so.26;
```

### Rabbit MQ

[Management Console](http://localhost:15672/)

- login : guest
- password : guest

### Zipkin

[Zipkin Console](http://localhost:9411/)

[Documentation](https://cloud.spring.io/spring-cloud-sleuth/reference/html/#sleuth-with-zipkin-via-http)

### Controller

[Swagger](http://localhost:8080/swagger-ui.html)

```json
{
  "imageURL": "/bucket-processing/images/in/LeeHill.tif",
  "imageSizeX": 16000,
  "imageSizeY": 8000,
  "aoi": {
    "crs": "epsg:32613",
    "upperLeft": {
      "x": 472000,
      "y": 4436000
    },
    "upperRight": {
      "x": 476000,
      "y": 4436000
    },
    "lowerLeft": {
      "x": 472000,
      "y": 4434000
    },
    "lowerRight": {
      "x": 476000,
      "y": 4434000
    }
  }
}
```

### Orchestrator

[H2 Console](http://localhost:8090/h2-console)

### Tracker

[Swagger](http://localhost:8081/swagger-ui.html)
[H2 Console](http://localhost:8081/h2-console)

### Local deployment

```shell
docker run -e POSTGRES_PASSWORD=admin postgres
docker run -it -p 5672:5672 -p 15672:15672 rabbitmq:3.9-management
docker run -p 9411:9411 openzipkin/zipkin-slim
```

### Local deployment with Minikube and local docker registry

```shell
#docker run -d -p 5000:5000 --name registry registry:2
minikube start
#minikube addons enable registry
minikube addons enable ingress
minikube mount data:/data

#https://www.rabbitmq.com/kubernetes/operator/install-operator.html
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"

#https://minikube.sigs.k8s.io/docs/handbook/pushing/#1-pushing-directly-to-the-in-cluster-docker-daemon-docker-env
eval $(minikube docker-env)
#& minikube -p minikube docker-env --shell powershell | Invoke-Expression #Windows

gradlew jibDockerBuild
kubectl create namespace 'processing'
kubectl create namespace 'infra'
kubectl apply -f k8s/minikube/postgres.yaml
kubectl apply -f k8s/minikube/zipkin.yaml
kubectl apply -f k8s/minikube/rabbitmq.yaml
kubectl apply -f k8s/minikube/controller.yaml
kubectl apply -f k8s/minikube/echo-orchestrator.yaml
kubectl apply -f k8s/minikube/echo-worker.yaml
kubectl apply -f k8s/minikube/echo-merger.yaml
kubectl apply -f k8s/minikube/tracker.yaml

docker build -t ui-admin -f processing/ui-admin/Dockerfile ui-admin
kubectl apply -f k8s/dev/ui-admin.yaml

minikube tunnel
minikube dashboard
```

### GCP deployment

```shell
gcloud auth login
gcloud auth configure-docker europe-west1-docker.pkg.dev #For Artifact Registry
gcloud auth configure-docker #OR For Container Registry
gcloud container clusters get-credentials {GCP_CLUSTER} --zone europe-west1-c --project {GCP_PROJECT}

#https://www.rabbitmq.com/kubernetes/operator/install-operator.html
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"

#https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin  
gradlew jib
kubectl create namespace 'processing'
kubectl create namespace 'infra'
kubectl apply -f k8s/dev/rabbitmq.yaml
kubectl apply -f k8s/dev/controller.yaml
kubectl apply -f k8s/dev/echo-orchestrator.yaml
kubectl apply -f k8s/dev/echo-worker.yaml
kubectl apply -f k8s/dev/echo-merger.yaml
kubectl apply -f k8s/dev/tracker.yaml

docker build -t ui-admin -f processing/ui-admin/Dockerfile ui-admin
docker tag -t ui-admin eu.gcr.io/processing/ui-admin:latest
docker push eu.gcr.io/processing/ui-admin:latest


kubectl apply -f k8s/dev/ui-admin.yaml
kubectl apply -f k8s/dev/ingress.yaml
```

### DOC

[Interessant pour comprendre le but de cloud event](https://spring.io/blog/2020/12/23/cloud-events-and-spring-part-2)
[Gdal avec GCP](https://gdal.org/user/virtual_file_systems.html#vsigs-google-cloud-storage-files)

### TODO

- optimiser ecriture image avec merge : créer l'image result au début de l'orchestator et dans worker ecrire directement
  avec le warp sur l'image de result. => Ne marche pas car ecriture dans image non parallelisable
- gérer les priorité par queue dynamic et topic : une file queue.processing.echo.start.1 connecté avec un exchange topic
  processing.start.1
- Séparé le tracker queue et l'api rest
- ne pas avoir de dépendance a gdal si possible dans lib-geo pour qu'elle soit portable
- faire un systeme de fin de worker ou de merger avec fichier pour savoir si fichier valide ou non car impossible de
  savoir autrement (le fichier peut etre créer, valide et noir)
- ajouter TTL sur message et gestion d'erreur