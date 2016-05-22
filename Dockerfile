FROM java

RUN mkdir /opt/notv
WORKDIR /opt/notv

ADD docker/docker-entrypoint.sh docker-entrypoint.sh
ADD target/uberjar/notv-0.1.0-SNAPSHOT-standalone.jar /opt/notv
RUN chmod +x docker-entrypoint.sh
CMD ["bash", "-c", "/opt/notv/docker-entrypoint.sh"]
EXPOSE 5000
