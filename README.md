#NOTV

It's TV service of you (my) dreams. Possibly. One day.

It will be using http://github.com/juhofriman/xmltv-docker one day for
scraping data.

## Run with leiningen

Start ```gulp``` in another terminal, so front end get webpacked on the fly

```
lein ring server
```

## Run with docker

```
gulp
lein ring uberjar
docker build -t juhofriman/notv .
docker run -p 8000:8000 -t juhofriman/notv
```
