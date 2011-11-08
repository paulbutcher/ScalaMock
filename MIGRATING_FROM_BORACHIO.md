# Migrating from Borachio

ScalaMock continues to support all of Borachio's features, but there are a few changes required to your code:

## Using Proxy Mocks

To use proxy mocks, in addition to `MockFactory`, you will also need to mix `ProxyMockFactory` into your test suite.

## Predicate Matching

Predicate matching is now type safe. The syntax has changed slightly. Instead of:

    m expectsWhere { (x: Int, y: Double) => x < y }
    
use:

    m.expects(where { _ < _ })
    
## Unspecified Arguments

It is no longer possible to leave arguments unspecified. Instead of:

    m returns "foo"

use:

    m expects () returns "foo"
