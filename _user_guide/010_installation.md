---
layout: complex_article
title: User Guide - Installation
permalink: /user-guide/installation/
---

# Installation

## Packages 

### Releases

You can download ScalaMock jars and sources from Maven Central.

The latest released version can be found here: [Releases](https://github.com/paulbutcher/ScalaMock/releases).
Or, to see all released and published versions, search at [Maven Central](https://search.maven.org/search?q=g:org.scalamock%20AND%20a:scalamock_2.11&core=gav), or [Sonatype OSS Nexus](https://oss.sonatype.org/#nexus-search;quick~scalamock_2.11).

Release versions are also in JCenter, so your favourite resolver should be covered.

### CI Builds

A nice option where you can pick the latest snapshot, or a specific tag or commit hash is jitpack.
Fetching the library this way may take a minute if this is the first time it was requested, as builds are kicked off just in time. Subsequent downloads are cached. To get the latest and freshest, use this:

```scala
resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies += "com.github.barkhorn" % "ScalaMock" % "master-SNAPSHOT"
```

Read more here: https://jitpack.io/#barkhorn/ScalaMock/master-SNAPSHOT

Our CI (Travis) also publishes builds to the Sonatype Snapshot Repo.

```scala
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
libraryDependencies += "org.scalamock" % "ScalaMock" % "x.y.z-SNAPSHOT"
```

You can search for snapshot builds on [Sonatype OSS Nexus](https://oss.sonatype.org/#nexus-search;quick~scalamock_2.11).

## Sources

You can find the ScalaMock repository and source code on [Github](https://github.com/paulbutcher/ScalaMock). The documentation is available in the [gh-pages branch](https://github.com/paulbutcher/ScalaMock/tree/gh-pages).

## sbt projects

To use ScalaMock in [sbt](http://www.scala-sbt.org/) with [ScalaTest](http://www.scalatest.org/) add the following to your project file:

```scala
// for versions 4.0+ use this dependency:
libraryDependencies += "org.scalamock" %% "scalamock" % "(version)" % Test
// as ScalaTest is now an "optional" dependency, you should include that yourself:
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test
```

To use ScalaMock with [Specs2](http://etorreborre.github.com/specs2/):

```scala
// for versions 4.0+ use this dependency:
libraryDependencies += "org.scalamock" %% "scalamock" % "(version)" % Test
// as Specs2 is now an "optional" dependency, you should include that yourself:
libraryDependencies += "org.specs2" %% "specs2-core" % "3.9.1" % Test
```

## Maven projects

To include ScalaMock in your Maven project add the following to your `pom.xml`:

```xml
<dependency>
    <groupId>org.scalamock</groupId>
    <artifactId>scalamock_2.11</artifactId>
    <version>(version)</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.scalatest</groupId>
    <artifactId>scalatest_2.11</artifactId>
    <version>3.0.4</version>
    <scope>test</scope>
</dependency>
```

## Gradle

```groovy
testCompile 'org.scalamock:scalamock_2.11:(version)'
testCompile 'org.scalatest:scalatest_2.11:3.0.4'
```

## Upgrading from ScalaMock Version 3.x

Before version 4.0, ScalaMock had different artefacts for ScalaTest and Specs2 (scalamock-scalatest-support and scalamock-specs2-support), which actually pulled in versions of ScalaTest and Spec2 respectively.
Since ScalaMock version 4.0.0, this was changed to combine both these adaptors into the main jar file and make the transitive dependencies optional. When upgrading, make sure to include a new dependency to ScalaTest/Specs2 if your project doesn't have one already. See the examples above for guidance.
