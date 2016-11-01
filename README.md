# Avro-Lenses [![Build Status](https://travis-ci.org/ogirardot/avro-lenses.svg?branch=master)](https://travis-ci.org/ogirardot/avro-lenses)
A started draft of a runtime Lenses implementation for mutating or creating transformed GenericRecords

## How to use
The artifact is deployed on sonatype's central repository, so all you need is to add it to your resolvers and use it like that : 

```
resolvers += Resolver.sonatypeRepo("snapshot")

libraryDependencies += "fr.psug.avro" %% "avro-lenses" % "0.1.0-SNAPSHOT"
```

Or to use the latest stable release

```
resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies += "fr.psug.avro" %% "avro-lenses" % "0.1.0"
```

For now there is no official release, just a snapshot.
