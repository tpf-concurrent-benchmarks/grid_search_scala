# Grid Search - Scala

## Objective

This is a Scala implementation of a system for performing a grid search under [common specifications](https://github.com/tpf-concurrent-benchmarks/docs/tree/main/grid_search) defined for multiple languages.

The objective of this project is to benchmark the language on a real-world distributed system.

## Deployment

### Requirements

- [Docker >3](https://www.docker.com/) (needs docker swarm)
- For local development:
  - [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
  - [Scala 3.3.1](https://www.scala-lang.org/download/)
  - [sbt 1.9.6](https://www.scala-sbt.org/)

### Configuration

- **Number of replicas:** `N_WORKERS` constant is defined in the `Makefile` file.
- **Data config:** in `containers/manager/src/main/resources/data.json` you can define (this config is built into the container):
  - `data`: Intervals for each parameter, in format: [start, end, step, precision]
  - `agg`: Aggregation function to be used: MIN | MAX | AVG
  - `maxItemsPerBatch`: Maximum number of items per batch (batches are sub-intervals)
- **Manager config:** in `containers/manager/src/main/resources/manager.conf` you can define (this config is built into the container):
  - middleware config (rabbitmq addres, credentials, etc)
  - metrics config (graphite address)
  - data config path

### Commands

#### Startup

- `make setup` will make required initializations, equivalent to:
  - `make init` starts docker swarm
  - `down_rabbitmq` `build_rabbitmq`
  - `make build`: builds manager and worker images

#### Run

- `make deploy`: deploys the manager and worker services locally, alongside with Graphite, Grafana and cAdvisor.
- `make remove`: removes all services (stops the swarm)

> There are make scripts to run rabbit, graphana, manager and worker independently.
> Such as: `run_rabbitmq`, `run_graphite`, `run_manager_local`, `run_worker_local`

#### Logs

- `make manager_logs`: shows the logs of the manager service
- `make worker_logs`: shows the logs of the worker service

#### Local development

- `make common_publish_local` publishes the common packets to the local maven repository.
- `run_manager_local` and `run_worker_local` can be used to run the manager and worker services locally.

#### Remote deployment

- `make deploy_remote` builds and deploys the system on the server.

> There are make scripts to tunnel the services such as: `tunnel_rabbitmq`, `tunnel_graphite`, `tunnel_cadvisor`, `tunnel_grafana`.

> This will require ssh access to the server

### Monitoring

- Grafana: [http://127.0.0.1:8081](http://127.0.0.1:8081)
- Graphite: [http://127.0.0.1:8080](http://127.0.0.1:8080)
- Logs
