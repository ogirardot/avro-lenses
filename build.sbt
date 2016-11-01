import de.heikoseeberger.sbtheader.license.MIT

name := "avro-lenses"

organization := "fr.psug.avro"

scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.10.6", "2.11.8")

coverageEnabled := true

val avroVersion = "1.8.0"

val monocleVersion = "1.3.1"

libraryDependencies ++= Seq(
  "com.github.julien-truffaut"  %%  "monocle-core"    % monocleVersion,
  "com.github.julien-truffaut"  %%  "monocle-generic" % monocleVersion,
  "com.github.julien-truffaut"  %%  "monocle-macro"   % monocleVersion,        
  "com.github.julien-truffaut"  %%  "monocle-state"   % monocleVersion,     
  "com.github.julien-truffaut"  %%  "monocle-refined" % monocleVersion,
  "com.github.julien-truffaut"  %%  "monocle-unsafe"  % monocleVersion,
  "org.apache.avro" % "avro" % avroVersion,
  "com.github.julien-truffaut"  %%  "monocle-law"     % monocleVersion % "test",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

resolvers in ThisBuild ++= Seq(
  Resolver.sonatypeRepo("snapshot"),
  Resolver.sonatypeRepo("releases")
)

headers := Map(
  "scala" -> MIT("2016", "Olivier Girardot")
)

// Your profile name of the sonatype account. The default is the same with the organization value
sonatypeProfileName := "fr.psug"

// To sync with Maven central, you need to supply the following information:
pomExtra in Global := {
  <url>https://github.com/ogirardot/avro-lenses</url>
    <licenses>
      <license>
        <name>MIT</name>
        <url>https://opensource.org/licenses/MIT</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/ogirardot/avro-lenses</connection>
      <developerConnection>scm:git:git@github.com:ogirardot/avro-lenses</developerConnection>
      <url>github.com/ogirardot/avro-lenses</url>
    </scm>
    <developers>
      <developer>
        <id>ogirardot</id>
        <name>Olivier GIRARDOT</name>
        <url>https://www.linkedin.com/in/oliviergirardot</url>
      </developer>
    </developers>
}
