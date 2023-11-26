WORKER_REPLICAS = 4

init:
	mkdir -p .make
	docker swarm init || true
.PHONY: init

.make/jar_native: $(shell find containers -type f -not -path "*/target/*")
	cd ./containers/manager && sbt assembly
	cd ../..
	cd ./containers/worker && sbt assembly
	cd ../..
	cp ./containers/manager/target/scala-3.3.1/manager.jar ./containers/manager/manager.jar
	cp ./containers/worker/target/scala-3.3.1/worker.jar ./containers/worker/worker.jar
	touch .make/jar_native

.make/jar_dockerized: $(shell find containers -type f -not -path "*/target/*")
	docker compose -f docker/compilation.yaml up --build
	docker compose -f docker/compilation.yaml down
	cp ./compilation/manager/scala-3.3.1/manager.jar ./containers/manager/manager.jar
	cp ./compilation/worker/scala-3.3.1/worker.jar ./containers/worker/worker.jar
	touch .make/jar_dockerized

jar: common_publish_local
	if command -v sbt &> /dev/null; then \
		make .make/jar_native; \
	else \
		make .make/jar_dockerized; \
	fi

.make/build: $(shell find containers -type f -not -path "*/target/*")
	docker rmi grid_search_scala_worker -f
	docker rmi grid_search_scala_manager -f
	docker build -t grid_search_scala_worker -f ./containers/worker/Dockerfile ./containers/worker
	docker build -t grid_search_scala_manager -f ./containers/manager/Dockerfile ./containers/manager
	touch .make/build

build: jar .make/build

build_rabbitmq:
	if ! docker images | grep -q rostov_rabbitmq; then \
		docker build -t rostov_rabbitmq ./containers/rabbitmq/; \
	fi
.PHONY: build_rabbitmq

run_rabbitmq: build_rabbitmq
	docker stack deploy -c docker/rabbitmq.yaml rabbitmq
.PHONY: run_rabbitmq

down_rabbitmq:
	if docker stack ls | grep -q rabbitmq; then \
		docker stack rm rabbitmq; \
	fi
.PHONY: down_rabbitmq

setup: init build build_rabbitmq
.PHONY: setup

deploy: remove down_rabbitmq common_publish_local build_rabbitmq
	mkdir -p graphite
	mkdir -p grafana_config
	until WORKER_REPLICAS=$(WORKER_REPLICAS) docker stack deploy \
 	-c docker/rabbitmq.yaml \
	-c docker/common.yaml \
	-c docker/local.yaml gs_scala; do sleep 1; done
.PHONY: deploy

deploy_jars: remove down_rabbitmq build build_rabbitmq
	mkdir -p graphite
	mkdir -p grafana_config
	until WORKER_REPLICAS=$(WORKER_REPLICAS) docker stack deploy \
 	-c docker/rabbitmq.yaml \
	-c docker/common.yaml \
	-c docker/server.yaml gs_scala; do sleep 1; done
.PHONY: deploy_jars

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

.make/common_publish_local: $(shell find containers/common -type f -not -path "*/target/*")
	cd ./containers/common && sbt publishLocal
	cd ../..
	touch .make/common_publish_local

common_publish_local: .make/common_publish_local

# Server specific

.make/upload_jars: $(shell find containers -type f -name "*.jar")
	scp containers/manager/manager.jar efoppiano@atom.famaf.unc.edu.ar:gs_scala/grid_search_scala/containers/manager
	scp containers/worker/worker.jar efoppiano@atom.famaf.unc.edu.ar:gs_scala/grid_search_scala/containers/worker
	touch .make/upload_jars

upload_jars: build .make/upload_jars

REMOTE_WORK_DIR = gs_scala/grid_search_scala

## Use *_remote if you are running them from your local machine

_deploy_remote:
	mkdir -p graphite
	mkdir -p grafana_config
	until WORKER_REPLICAS=$(WORKER_REPLICAS) docker stack deploy \
 	-c docker/rabbitmq.yaml \
	-c docker/common.yaml \
	-c docker/server.yaml gs_scala; do sleep 1; done
.PHONY: _deploy_remote

deploy_remote: upload_jars
	ssh efoppiano@atom.famaf.unc.edu.ar 'cd $(REMOTE_WORK_DIR) && make _deploy_remote'
.PHONY: deploy_remote

remove_remote:
	ssh efoppiano@atom.famaf.unc.edu.ar 'cd $(REMOTE_WORK_DIR) && make remove'
.PHONY: remove_remote


## Tunneling

tunnel_rabbitmq:
	ssh -L 15672:127.0.0.1:15672 efoppiano@atom.famaf.unc.edu.ar
.PHONY: tunnel_rabbitmq

tunnel_graphite:
	ssh -L 8080:127.0.0.1:8080 efoppiano@atom.famaf.unc.edu.ar
.PHONY: tunnel_graphite

tunnel_cadvisor:
	ssh -L 8888:127.0.0.1:8888 efoppiano@atom.famaf.unc.edu.ar
.PHONY: tunnel_cadvisor

tunnel_grafana:
	ssh -L 8081:127.0.0.1:8081 efoppiano@atom.famaf.unc.edu.ar
.PHONY: tunnel_grafana
