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

run_rabbitmq:
	docker run -d --hostname my-rabbit --name rostov_rabbitmq -p 15672:15672 -p 5672:5672 rostov_rabbitmq

down_rabbitmq:
	docker stop rostov_rabbitmq
	docker rm rostov_rabbitmq

setup: init compile build build_rabbitmq

deploy:
	mkdir -p graphite
	docker stack deploy -c docker-compose.yaml gs_scala

remove:
	docker stack rm gs_scala

manager_logs:
	docker service logs -f gs_scala_manager

run_manager_local:
	cd ./containers/manager && LOCAL=true sbt run
	cd ../..

run_manager_tests:
	cd ./containers/manager && sbt test
	cd ../..

run_worker_local:
	cd ./containers/worker && LOCAL=true sbt run
	cd ../..

run_worker_tests:
	cd ./containers/worker && sbt test
	cd ../..