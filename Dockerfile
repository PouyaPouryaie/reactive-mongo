FROM azul/zulu-openjdk-alpine:11
ADD target/mongo-reactive-1.0.0.jar /mongo-reactive-1.0.0.jar
ENTRYPOINT ["java", "-jar", "mongo-reactive-1.0.0.jar"]