---
layout: article
title: Roadmap
permalink: /roadmap/
---

The Roadmap aims to set out when certain features will appear. 

## December 2014 - ScalaMock 3.3
* Custom matchers ([issue #42](https://github.com/paulbutcher/ScalaMock/issues/42))
* Mocking case classes and classes without default constructor ([issue #56](https://github.com/paulbutcher/ScalaMock/issues/56))
* Fixing issues with mocking overloaded issues ([issue #39](https://github.com/paulbutcher/ScalaMock/issues/39), [issue #73](https://github.com/paulbutcher/ScalaMock/issues/73))

If there is something you would like to have in ScalaMock 3.3 - just [request feature](https://github.com/paulbutcher/ScalaMock/issues/) or vote on existing one.

## First Quarter 2016 - ScalaMock 4

As soon as [scala.meta](http://scalameta.org/) is available, we plan to start working on ScalaMock 4. If scala.meta delivers on its promise, ScalaMock 4 should be able to mock any trait, no matter how complex its type.

In addition, we expect that it will also support:

* improved syntax: `mockObject.expects.method(arguments)` instead of: `(mockObject.method _) expects (arguments)`
* mocking object creation (constructors)
* mocking singleton and companion objects (static methods)
* mocking final classes and classes with final methods or private constructors
