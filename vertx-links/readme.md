Layrry Vert.x Example
---

This app demonstrates the usage of Layrry with dynamic modules in Vert.x. 

# Build

## Prerequisites

- JDK 11
- Maven 3.6.3 or Gradle 6+ (project includes wrappers for each)
- Layrry 1.0-SNAPSHOT
- [jbang](https://github.com/jbangdev/jbang) (optional)

**NOTE**: Layrry has yet to be published to Maven Central. You have to build your own snapshot for the time being.
JDK 15 is needed to build Layrry.

```sh
$ git clone https://github.com/moditect/layrry.git
$ cd layrry
$ mvn install
```

Once Layrry is installed, change back to where you cloned this repository.

```sh
$ ./mvnw install
```

Plugins will be placed under `staging/plugins-prepared` at the root.

# Run

Run it directly from the layrry-launcher JAR (assuming Layrry repo was cloned adjacent to this repo)

```sh
$ java --enable-preview -jar ../../layrry/layrry-launcher/target/layrry-launcher-1.0-SNAPSHOT-all.jar --layers-config staging/layers.yml --properties staging/versions.properties
```

.jbang

```sh
$ jbang layrry@moditect --layers-config staging/layers.yml --properties staging/versions.properties
```

Example output
```
23:10:50.284 [main] INFO  c.e.l.l.c.i.LayrryLinksVerticle - Adding plug-in: PluginDescriptor [name=plugins-layrry-links-membership-1.0.0, moduleLayer=com.example.layrry.links.membership]
23:10:50.417 [vert.x-eventloop-thread-0] INFO  c.e.l.l.c.i.LayrryLinksVerticle - Adding router for path: /routes
23:10:50.422 [vert.x-eventloop-thread-0] INFO  c.e.l.l.c.i.LayrryLinksVerticle - Adding router for path: /members
23:10:50.470 [vert.x-eventloop-thread-0] INFO  c.e.l.l.c.i.LayrryLinksVerticle - Server ready! Browse to http://localhost:8080/routes
```

# Dynamically manage modules

To dynamically add the `layrry-links-tournament` module, run the following:
```
cp -r staging/layrry-links-tournament-* staging/route-plugins2
```

To dynamically remove the `layrry-links-tournament` module, run the following:
```
rm -rf statging/route-plugins2/layrry-links-tournament-*
```
