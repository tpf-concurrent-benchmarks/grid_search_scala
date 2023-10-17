init:
	docker swarm init

compile:
	cd ./containers/worker && sbt compile
	cd ../..
	cd ./containers/manager && sbt compile

build:
	docker build -t grid_search_scala_worker ./containers/worker/
	docker build -t grid_search_scala_manager ./containers/manager/

setup: init compile build

deploy:
	mkdir -p graphite
	docker stack deploy -c docker-compose.yaml template

remove:
	docker stack rm template