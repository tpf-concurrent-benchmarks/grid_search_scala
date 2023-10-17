init:
	docker swarm init

deploy:
	mkdir -p graphite
	docker stack deploy -c docker-compose.yaml template

remove:
	docker stack rm template