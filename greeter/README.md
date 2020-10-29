Layrry Greeter Example
---

This app demonstrates the usage of Layrry with conflicting module versions.

![Layrry Example](images/greeter.png)

# Build

## Prerequisites

- JDK 11
- Maven 3.6.3
- Layrry 1.0-SNAPSHOT
- [jbang](https://github.com/jbangdev/jbang) (optional)

**NOTE**: Layrry has yet to be published to Maven Central. You have to build your own snapshot for the time being.
JDK 11 is needed to build Layrry.

```sh
$ git clone https://github.com/moditect/layrry.git
$ cd layrry
$ mvn install
```

Once Layrry is installed, change back to where you cloned this repository.

```sh
$ ./mvnw install
```

# Run

Run it directly from the layrry-launcher JAR (assuming Layrry repo was cloned adjacent to this repo)

```sh
$ java -jar ../../layrry/layrry-launcher/target/layrry-launcher-1.0-SNAPSHOT-all.jar --layers-config staging/layers.yml
```

.Maven

```sh
$ ./mvnw -am -pl :runner exec:exec@run-yaml
$ ./mvnw -am -pl :runner exec:exec@run-toml
```

.jbang

```sh
$ jbang layrry@moditect --layers-config staging/layers.yml
```