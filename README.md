#NOTV

It's TV service of your (read it as 'my') dreams. Possibly. One day.

It uses http://github.com/juhofriman/xmltv-docker for
scraping data.

## Developing

### REPL

Just kick REPL in and use ```(start-notv)``` and ```(restart-notv)``` to edit code on the fly.

### lein run

```bash
lein run -m notv-backend.core
```

Starts notv and uses example-tvdata.xml under resources

### lein uberjar

```bash
lein uberjar
TVDATA_FILE=resources/example-tvdata.xml java -jar target/uberjar/notv-0.1.0-SNAPSHOT-standalone.jar
```

Runs notv from uberjar with given tvdata file.

### Docker

```bash
bin/build-notv.sh
bin/run-local.sh
```

Build docker image and runs it. This expects xmltv docker image running within same machine.
