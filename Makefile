init:
	docker swarm init

deploy:
	docker stack deploy -c docker-compose.yaml template

remove:
	docker stack rm template