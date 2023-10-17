init:
	docker swarm init

compile:
	cd ./containers/worker && sbt compile
	cd ../..
	cd ./containers/manager && sbt compile

build:
	docker rmi grid_search_scala_worker -f
	docker rmi grid_search_scala_manager -f
	docker build -t grid_search_scala_worker ./containers/worker/
	docker build -t grid_search_scala_manager ./containers/manager/

build_rabbitmq:
	docker build -t rostov_rabbitmq ./containers/rabbitmq/

setup: init compile build build_rabbitmq

deploy:
	mkdir -p graphite
	docker stack deploy -c docker-compose.yaml gs_scala

remove:
	docker stack rm gs_scala

manager_logs:
	docker service logs -f gs_scala_manager