FROM java

WORKDIR /opt/notv

COPY docker/docker-entrypoint.sh docker-entrypoint.sh
COPY target/notv-0.1.0-SNAPSHOT-standalone.jar /opt/notv
RUN chmod +x docker-entrypoint.sh
CMD ["bash", "-c", "/opt/notv/docker-entrypoint.sh"]
EXPOSE 3000
