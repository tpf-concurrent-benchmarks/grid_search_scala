init:
	docker swarm init
.PHONY: init

compile:
	cd ./containers/worker && sbt compile
	cd ../..
	cd ./containers/manager && sbt compile
.PHONY: compile

jar:
	docker compose -f docker-compose-compilation.yaml up --build
	docker compose -f docker-compose-compilation.yaml down
	cp ./compilation/manager/scala-3.3.1/manager.jar ./containers/manager/manager.jar
	cp ./compilation/worker/scala-3.3.1/worker.jar ./containers/worker/worker.jar
.PHONY: jar

build: jar
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

setup: init compile build build_rabbitmq
.PHONY: setup

deploy: remove build down_rabbitmq down_graphite
	mkdir -p graphite
	MY_UID="$(shell id -u)" MY_GID="$(shell id -g)" docker stack deploy -c docker-compose.yaml gs_scala
.PHONY: deploy

deploy_server: remove_server
	mkdir -p graphite
	MY_UID="$(shell id -u)" MY_GID="$(shell id -g)" docker stack deploy -c docker-compose-server.yaml gs_scala
.PHONY: deploy_server

remove:
	if docker stack ls | grep -q gs_scala; then \
            docker stack rm gs_scala; \
	fi
.PHONY: remove

remove_server:
	if docker stack ls | grep -q gs_scala; then \
			docker stack rm gs_scala; \
	fi
.PHONY: remove_server

manager_logs:
	docker service logs -f gs_scala_manager
.PHONY: manager_logs

worker_logs:
	docker service logs -f gs_scala_worker
.PHONY: worker_logs

run_manager_local:
	cd ./containers/manager && LOCAL=true sbt run
	cd ../..
.PHONY: run_manager_local

run_manager_tests:
	cd ./containers/manager && sbt test
	cd ../..
.PHONY: run_manager_tests

run_worker_local:
	cd ./containers/worker && LOCAL=true sbt run
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