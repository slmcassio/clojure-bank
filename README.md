# Clojure Bank
A Clojure library designed to parse, validate and process accounts and transactions for testing purposes.

## Build 
`docker build -t clojure-bank .`

## Run
```
$ cat operations
{ "account": { "activeCard": true, "availableLimit": 100 } }
{ "transaction": { "merchant": "Burger King", "amount": 20, "time": "2019-02-13T10:00:00.000Z" } }
{ "transaction": { "merchant": "Habbib's", "amount": 90, "time": "2019-02-13T11:00:00.000Z" } }

$ docker run -i --rm clojure-bank < operations
{ "account": { "activeCard": true, "availableLimit": 100 }, "violations": [] }
{ "account": { "activeCard": true, "availableLimit": 80 }, "violations": [] }
{ "account": { "activeCard": true, "availableLimit": 80 }, "violations": [ "insufficient-limit" ] }
```

## Development
The project was built with Leiningen ([Leiningen](https://leiningen.org)).
The project organisation was based on the hexagonal architecture:
	* Ports: The layer that communicates with the outside world (e.g. cli).
	* Adapters: The layer that converts external data representations into internal ones, and vice-versa (e.g. adapters, db).
	* Services: All the business / application / domain logic lives here (e.g. services, validations).

### Run
`lein run < operations`

### Tests
* Unit: `lein test`
* Integration: `lein test :integration`
* All: `lein test :all`
