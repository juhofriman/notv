#!/bin/bash

echo $(java -version)
export TVDATA_FILE="/data/tvdata.xml"
exec java -server -jar /opt/notv/notv-0.1.0-SNAPSHOT-standalone.jar
