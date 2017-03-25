---
layout: complex_article
title: User Guide - Installation
permalink: /user-guide/installation/
---

# Installation

## Packages 

### Releases

You can download ScalaMock jars and sources from Maven Central or JCenter.

Latest released version is [![GitHub release](https://img.shields.io/github/release/paulbutcher/scalamock.svg)](https://github.com/paulbutcher/ScalaMock/releases).

To see all released and published versions, search at [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.scalamock%22%20scalatest-support), or [Sonatype OSS Nexus](https://oss.sonatype.org/#nexus-search;quick~scalamock-scalatest-support).

Release versions are also in JFrog JCenter and [Bintray](https://bintray.com/scalamock),
so your favourite resolver should be covered.

### CI Builds

If you want try a pre-release version, you can add Sonatype Snapshots to your resolvers. Travis publishes all commits to the Snapshot Repo.
So if you need a fix urgently, you can try one of those CI builds. First, add the Snapshot Repo to your resolvers:

```scala
// e.g. for SBT:
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
```

You can search for CI builds on [Sonatype OSS Nexus](https://oss.sonatype.org/#nexus-search;quick~scalamock-scalatest-support).

## Sources

You can find the ScalaMock repository and source code on [Github](https://github.com/paulbutcher/ScalaMock).

## sbt projects

To use ScalaMock in [sbt](http://www.scala-sbt.org/) with [ScalaTest](http://www.scalatest.org/) add the following to your project file:

```scala
libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "(version)" % Test
```

To use ScalaMock with [Specs2](http://etorreborre.github.com/specs2/):

```scala
libraryDependencies += "org.scalamock" %% "scalamock-specs2-support" % "(version)" % Test
```

## Maven projects

To include ScalaMock in your Maven project add the following to your `pom.xml`:

```xml
<dependency>
    <groupId>org.scalamock</groupId>
    <artifactId>scalamock-scalatest-support_2.11</artifactId>
    <version>(version)</version>
    <scope>test</scope>
</dependency>
```

## Gradle

```groovy
testCompile 'org.scalamock:scalamock-scalatest-support_2.12:(version)'
```
