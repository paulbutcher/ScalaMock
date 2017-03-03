---
layout: complex_article
title: User Guide - Installation
permalink: /user-guide/installation/
---

# Installation

## Packages 

You can download ScalaMock jars and sources from Maven Central or JCenter.

Latest Release on Github is [![GitHub release](https://img.shields.io/github/release/paulbutcher/scalamock.svg)](https://github.com/paulbutcher/ScalaMock/releases).

We also have a group on [Bintray](https://bintray.com/scalamock) where you can subscribe to update notifications.

To see all published versions look [here](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.scalamock%22%20scalatest-support).

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
