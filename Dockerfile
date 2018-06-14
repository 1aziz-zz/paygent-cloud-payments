FROM openjdk:8-jdk-alpine
COPY build /home/
WORKDIR /home
CMD [ "java", "-jar", "libs/payment-service-0.1.jar" ]
EXPOSE 9003