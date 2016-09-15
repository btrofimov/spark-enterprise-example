# spark-enterprise-example


Example project to demostrate how Spark can help to develop enterprize services. 

The project consists of two versions: respecting command completion and without it.
Both versions follow to CQRS approach however with some limitation:
 * Single storge for write and read models. Both models use the same mongo collections
 * Repositories do not fire domain events

Project uses:
 * Spring Boot/MVC for Facade application (just REST enpoints and read models)
 * Kafka for data integration - delivering commands and events.
 * Spark Streaming for command processing. All business logic lives on this side. 

The application itself is simple bulletin board with just two basic operations 
 * add bulletin 
 * view list of all bulletins.

## Build
 * Follow corresponding subproject
 * ./gradlew build

## Run
 * Follow corresponding subproject
 * cd ```./scripts/``` and run ```docker-compose up```
 * run bl-processor app (MainApp class)
 * run Spring Boot facade app
 *  run GET/POST localhost:8080/bulletinboard
 
