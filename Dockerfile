FROM ubuntu

WORKDIR /opt/xmltv

RUN sudo apt-get update
RUN apt-get install -y xmltv
RUN apt-get install -y python
COPY docker/tv_grab_fi.conf tv_grab_fi.conf

COPY docker/docker-entrypoint.sh docker-entrypoint.sh
RUN chmod +x docker-entrypoint.sh
CMD ["bash", "-c", "/opt/xmltv/docker-entrypoint.sh"]
EXPOSE 8000
