#!/bin/bash

echo $(java -version)
exec java -server -jar /opt/notv/notv-0.1.0-SNAPSHOT-standalone.jar
