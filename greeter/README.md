Layrry Greeter Example
---

This app demonstrates the usage of Layrry with conflicting module versions.

![Layrry Example](images/greeter.png)

# Build

## Prerequisites

- JDK 11
- Maven 3.6.3
- Layrry 1.0.0.Alpha1
- [jbang](https://github.com/jbangdev/jbang) (optional)

Download Layrry from [https://github.com/moditect/layrry/releases/tag/1.0.0.Alpha1](https://github.com/moditect/layrry/releases/tag/1.0.0.Alpha1), or install it via
[SDKMAN!](https://sdkmain.io), or build it from source.
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
$ java -jar ../../layrry/layrry-launcher/target/layrry-launcher-1.0.0.Alpha1-all.jar --layers-config staging/layers.yml
```

**Maven**

```sh
$ ./mvnw -am -pl :runner exec:exec@run-yaml
$ ./mvnw -am -pl :runner exec:exec@run-toml
```

**JBang**

```sh
$ jbang layrry@moditect --layers-config staging/layers.yml
```

**Binary**

If you installed the binary distribution from the release page or from SDKMAN!

```sh
$ layrry --layers-config staging/layers.yml
```