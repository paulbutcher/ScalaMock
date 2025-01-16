---
layout: complex_article
title: User Guide - Ordering
permalink: /user-guide/ordering/
---

# Ordering


## ScalaMock 7

Basic support of call ordering is added via CallLog. It should be declared before creating a stub

```scala
given CallLog = CallLog()

val foo = stub[Foo]
val bar = stub[Bar]

// setup stubs
foo.foo.returns(_ => 1)
bar.bar.returns(_ => 2)

// call methods (this is usually called internally in some other class)
foo.foo(1)
bar.bar(1)

foo.foo.isBefore(bar.bar) // true
bar.bar.isAfter(foo.foo) // true

```

## ScalaMock

### Default behaviour

By default, expectations can be satisfied in any order. For example:

```scala
val mockedFunction = mockFunction[Int, Unit]
mockedFunction.expects(1).returns(())
mockedFunction.expects(2).returns(())
```

can be satisfied by:

```scala
mockedFunction(2)
mockedFunction(1)
```

### Ordered expectations

A specific sequence can be enforced with `inSequence`:

```scala
inSequence {
  mockedFunction.expects(1).returns(())
  mockedFunction.expects(2).returns(())
}
mockedFunction(2) // throws ExpectationException
mockedFunction(1)
```

Multiple sequences can be specified. As long as the calls within each sequence happen in the correct order, calls within different sequences can be interleaved. For example:

```scala
inSequence {
  mockedFunction.expects(1).returns(())
  mockedFunction.expects(2).returns(())
}
inSequence {
  mockedFunction.expects(3).returns(())
  mockedFunction.expects(4).returns(())
}
```

can be satisfied by:

```scala
mockedFunction(3)
mockedFunction(1)
mockedFunction(2)
mockedFunction(4)
```

### In any order expectations

To specify that there is no constraint on ordering, use `inAnyOrder` (just remember that there is an implicit `inAnyOrder` at the top level). Calls to `inSequence` and `inAnyOrder` can be arbitrarily nested. For example:

```scala
mockedObject.a.expects().returns(())
inSequence {
  mockedObject.b.expects().returns(())
  inAnyOrder {
    mockedObject.c.expects().returns(())
    inSequence {
      mockedObject.d.expects().returns(())
      mockedObject.e.expects().returns(())
    }
    mockedObject.f.expects().returns(())
  }
  mockedObject.g.expects().returns(())
}
```

All the following invocation orders of `mockedObject` methods are correct according to the above specification:

```
a, b, c, d, e, f, g
b, c, d, e, f, g, a
b, c, d, a, e, f, g
a, b, d, f, c, e, g
```
