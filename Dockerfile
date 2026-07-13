FROM sbtscala/scala-sbt:eclipse-temurin-21.0.6_7_1.10.11_3.3.5
WORKDIR /app
COPY . .
RUN sbt compile
CMD ["sbt", "runMain com.Scala3Essentials.Playground"]