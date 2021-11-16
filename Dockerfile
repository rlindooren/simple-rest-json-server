FROM openjdk:18-jdk
EXPOSE ${PORT}
RUN mkdir /app
COPY ./build/install/"simple-rest-json-server"/ /app/
WORKDIR /app/bin
ENTRYPOINT ["./simple-rest-json-server"]
