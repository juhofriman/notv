#! /bin/bash

docker stop notv
docker rm notv

docker run -p 5000:5000 --volumes-from xmltv --name notv juhofriman/notv
