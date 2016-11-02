# Avro-Lenses [![Build Status](https://travis-ci.org/ogirardot/avro-lenses.svg?branch=master)](https://travis-ci.org/ogirardot/avro-lenses)[![Coverage Status](https://coveralls.io/repos/github/ogirardot/avro-lenses/badge.svg?branch=master)](https://coveralls.io/github/ogirardot/avro-lenses?branch=master)
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

## In Practice
```
// here for example we define a transformer that will transform toUpperCase the field 'name' of struct 'person'
import fr.psug.avro.AvroLens._

val transformer = defineWithSideEffect[String]("a.b", _.toUpperCase)
// then you can apply it 'transformer' on any generic record and it will be mutated in place

// If you want additional safety you can use it providing the Schema : 
val transformer = defineWithSideEffectAndSchema[String]("a.b", _.toUpperCase, schema)
```

For more use cases, you can check out the unit tests that contains exhaustive tests of all the features.
The `path` is quite intuitive and conforms **almost** to the JSONPath standard : 

* `a.b.c` will refer to the field/array `c` of struct `b` of struct `a`
* `a[]` will refer to the array inside of field `a`
 