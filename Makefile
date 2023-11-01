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
.PHONY: jar

build: common_publish_local jar
	docker rmi grid_search_scala_worker -f
	docker rmi grid_search_scala_manager -f
	docker build -t grid_search_scala_worker -f ./containers/worker/Dockerfile .
	docker build -t grid_search_scala_manager -f ./containers/manager/Dockerfile .
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