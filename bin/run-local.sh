#! /bin/bash

docker stop notv
docker rm notv

NREPL_PORT=7888
docker run -p 3000:3000 -p 7888:7888 --volumes-from xmltv --name notv juhofriman/notv
