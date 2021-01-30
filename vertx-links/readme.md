Layrry Vert.x Example
---

This app demonstrates the usage of Layrry with dynamic modules in Vert.x. 

# Build

## Prerequisites

- JDK 15
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

Plugins will be placed under `staging/plugins-prepared` at the root.

# Run

Run it directly from the layrry-launcher JAR (assuming Layrry repo was cloned adjacent to this repo)

```sh
$ java --enable-preview -jar ../../layrry/layrry-launcher/target/layrry-launcher-1.0.0.Alpha1-all.jar --layers-config staging/layers.yml --properties staging/versions.properties
```

**JBang**

```sh
$ jbang layrry@moditect --layers-config staging/layers.yml --properties staging/versions.properties
```

Example output
```
23:10:50.284 [main] INFO  c.e.l.l.c.i.LayrryLinksVerticle - Adding plug-in: PluginDescriptor [name=plugins-layrry-links-membership-1.0.0, moduleLayer=org.moditect.layrry.examples.links.membership]
23:10:50.417 [vert.x-eventloop-thread-0] INFO  o.m.l.e.l.c.i.LayrryLinksVerticle - Adding router for path: /routes
23:10:50.422 [vert.x-eventloop-thread-0] INFO  o.m.l.e.l.c.i.LayrryLinksVerticle - Adding router for path: /members
23:10:50.470 [vert.x-eventloop-thread-0] INFO  o.m.l.e.l.c.i.LayrryLinksVerticle - Server ready! Browse to http://localhost:8080/routes
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
