init:
	docker swarm init
.PHONY: init

compile:
	cd ./containers/worker && sbt compile
	cd ../..
	cd ./containers/manager && sbt compile
.PHONY: compile

jar: common_publish_local
	docker compose -f docker-compose-compilation.yaml up --build
	docker compose -f docker-compose-compilation.yaml down
	cp ./compilation/manager/scala-3.3.1/manager.jar ./containers/manager/manager.jar
	cp ./compilation/worker/scala-3.3.1/worker.jar ./containers/worker/worker.jar
.PHONY: jar

jar_local:
	cd ./containers/manager && sbt assembly
	cd ../..
	cd ./containers/worker && sbt assembly
	cd ../..
	cp ./containers/manager/target/scala-3.3.1/manager.jar ./containers/manager/manager.jar
	cp ./containers/worker/target/scala-3.3.1/worker.jar ./containers/worker/worker.jar
.PHONY: jar_local

build:
	docker rmi grid_search_scala_worker -f
	docker rmi grid_search_scala_manager -f
	docker build -t grid_search_scala_worker -f ./containers/worker/Dockerfile ./containers/worker
	docker build -t grid_search_scala_manager -f ./containers/manager/Dockerfile ./containers/manager
.PHONY: build

build_rabbitmq:
	docker build -t rostov_rabbitmq ./containers/rabbitmq/
.PHONY: build_rabbitmq

run_rabbitmq:
	docker stack deploy -c docker-compose-rabbitmq.yaml rabbitmq
.PHONY: run_rabbitmq

down_rabbitmq:
	if docker stack ls | grep -q rabbitmq; then \
		docker stack rm rabbitmq; \
	fi
.PHONY: down_rabbitmq

run_graphite: down_graphite
	docker stack deploy -c docker-compose-graphite.yaml graphite
.PHONY: run_graphite

down_graphite:
	if docker stack ls | grep -q graphite; then \
		docker stack rm graphite; \
	fi
.PHONY: down_graphite

setup: init common_publish_local compile build build_rabbitmq
.PHONY: setup

deploy: remove build down_rabbitmq down_graphite
	mkdir -p graphite
	mkdir -p grafana_config
	docker stack deploy -c docker-compose.yaml gs_scala
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

run_manager_tests:
	cd ./containers/manager && sbt test
	cd ../..
.PHONY: run_manager_tests

run_worker_local:
	cd ./containers/worker && LOCAL=true sbt -J-Xmx500M run
	cd ../..
.PHONY: run_worker_local

run_worker_tests:
	cd ./containers/worker && sbt test
	cd ../..
.PHONY: run_worker_tests

common_publish_local:
	cd ./containers/common && sbt publishLocal
	cd ../..
.PHONY: common_publish_local

# Server specific

upload_jars: jar
	scp containers/manager/manager.jar efoppiano@atom.famaf.unc.edu.ar:gs_scala/grid_search_scala/containers/manager
	scp containers/worker/worker.jar efoppiano@atom.famaf.unc.edu.ar:gs_scala/grid_search_scala/containers/worker
.PHONY: upload_jars

REMOTE_WORK_DIR = gs_scala/grid_search_scala

## Use *_remote if you are running them from your local machine
## Do not use those that start with _
_build_remote:
	docker rmi grid_search_scala_worker -f
	docker rmi grid_search_scala_manager -f
	docker build -t grid_search_scala_worker -f ./containers/worker/Dockerfile ./containers/worker
	docker build -t grid_search_scala_manager -f ./containers/manager/Dockerfile ./containers/manager
.PHONY: _build_remote

build_remote: upload_jars
	ssh efoppiano@atom.famaf.unc.edu.ar 'cd $(REMOTE_WORK_DIR) && make _build_remote'
.PHONY: build_remote

_deploy_remote:
	mkdir -p graphite
	docker stack deploy -c docker-compose-server.yaml gs_scala
.PHONY: _deploy_remote

deploy_remote: remove_remote build_remote
	ssh efoppiano@atom.famaf.unc.edu.ar 'cd $(REMOTE_WORK_DIR) && make _deploy_remote'
.PHONY: deploy_remote

_remove_remote:
	if docker stack ls | grep -q gs_scala; then \
			docker stack rm gs_scala; \
	fi
.PHONY: _remove_remote

remove_remote:
	ssh efoppiano@atom.famaf.unc.edu.ar 'cd $(REMOTE_WORK_DIR) && make _remove_remote'
.PHONY: remove_remote

## Use *_server if you are running them from the server (remember to upload the jars first)
build_server:
	docker rmi grid_search_scala_worker -f
	docker rmi grid_search_scala_manager -f
	docker build -t grid_search_scala_worker -f ./containers/worker/Dockerfile ./containers/worker
	docker build -t grid_search_scala_manager -f ./containers/manager/Dockerfile ./containers/manager
.PHONY: build_server

deploy_server: remove_server build_server
	mkdir -p graphite
	docker stack deploy -c docker-compose-server.yaml gs_scala
.PHONY: deploy_server

remove_server:
	if docker stack ls | grep -q gs_scala; then \
			docker stack rm gs_scala; \
	fi
.PHONY: remove_server

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
