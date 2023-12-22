WORKER_REPLICAS = 4
REMOTE_WORK_DIR = gs_scala/grid_search_scala
SERVER_USER = efoppiano
SERVER_HOST = atom.famaf.unc.edu.ar

init:
	docker swarm init || true
.PHONY: init

build:
	docker rmi grid_search_scala_worker -f || true
	docker rmi grid_search_scala_manager -f || true
	docker build -t grid_search_scala_worker -f ./containers/worker/Dockerfile ./containers
	docker build -t grid_search_scala_manager -f ./containers/manager/Dockerfile ./containers
.PHONY: build

build_rabbitmq:
	if ! docker images | grep -q rostov_rabbitmq; then \
		docker build -t rostov_rabbitmq ./containers/rabbitmq/; \
	fi
.PHONY: build_rabbitmq

run_rabbitmq: build_rabbitmq
	docker stack deploy rabbitmq.yaml rabbitmq
.PHONY: run_rabbitmq

down_rabbitmq:
	if docker stack ls | grep -q rabbitmq; then \
		docker stack rm rabbitmq; \
	fi
.PHONY: down_rabbitmq

setup: init build build_rabbitmq
.PHONY: setup

deploy: remove down_rabbitmq build_rabbitmq build
	mkdir -p graphite
	mkdir -p grafana_config
	until WORKER_REPLICAS=$(WORKER_REPLICAS) \
	docker stack deploy -c docker-compose.yaml gs_scala; \
	do sleep 1; done
.PHONY: deploy

remove:
	if docker stack ls | grep -q gs_scala; then \
            docker stack rm gs_scala; \
	fi
.PHONY: remove

manager_logs:
	docker service logs -f gs_scala_manager
.PHONY: manager_logs

worker_logs:
	docker service logs -f gs_scala_worker
.PHONY: worker_logs

run_manager_local:
	cd ./containers/manager && LOCAL=true sbt -J-Xmx500M run
	cd ../..
.PHONY: run_manager_local

run_worker_local:
	cd ./containers/worker && LOCAL=true sbt -J-Xmx500M run
	cd ../..
.PHONY: run_worker_local

common_publish_local:
	cd ./containers/common && sbt publishLocal
	cd ../..
.PHONY: common_publish_local

# Server specific

## Use *_remote if you are running them from your local machine

deploy_remote:
	ssh $(SERVER_USER)@$(SERVER_HOST) 'cd $(REMOTE_WORK_DIR) && make deploy'
.PHONY: deploy_remote

remove_remote:
	ssh $(SERVER_USER)@$(SERVER_HOST) 'cd $(REMOTE_WORK_DIR) && make remove'
.PHONY: remove_remote

## Tunneling

tunnel_rabbitmq:
	ssh -L 15672:127.0.0.1:15672 $(SERVER_USER)@$(SERVER_HOST)
.PHONY: tunnel_rabbitmq

tunnel_graphite:
	ssh -L 8080:127.0.0.1:8080 $(SERVER_USER)@$(SERVER_HOST)
.PHONY: tunnel_graphite

tunnel_cadvisor:
	ssh -L 8888:127.0.0.1:8888 $(SERVER_USER)@$(SERVER_HOST)
.PHONY: tunnel_cadvisor

tunnel_grafana:
	ssh -L 8081:127.0.0.1:8081 $(SERVER_USER)@$(SERVER_HOST)
.PHONY: tunnel_grafana

# Cloud specific

deploy_cloud: remove
	mkdir -p graphite
	mkdir -p grafana_config
	until WORKER_REPLICAS=$(WORKER_REPLICAS) \
	docker stack deploy -c docker-compose.yaml gs_scala; \
	do sleep 1; done
.PHONY: deploy_cloud