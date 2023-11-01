FROM alpine:latest

ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk
ENV PATH="$PATH:$JAVA_HOME/bin"

RUN apk --update add openjdk11-jre

RUN rm -rf /var/cache/apk/*

WORKDIR /app

COPY manager.jar .

CMD ["java", "-jar", "manager.jar"]