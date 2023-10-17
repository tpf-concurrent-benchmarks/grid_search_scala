FROM hseeberger/scala-sbt:11.0.12_1.5.5_2.13.6

WORKDIR /src

COPY . /src

RUN sbt compile

CMD ["sbt", "run"]