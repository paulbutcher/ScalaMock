Version 2.0:

- Add compiler plugin to provide:
  - Typesafe mocks
  - Singleton/companion object mocking
  - Constructor mocking
- Rename to ScalaMock

Version 1.4:

- Add `stubs` method as syntactic sugar for `expects ... anyNumberOfTimes`
- Add `inAnyOrder` and allow ordered and unordered expectations to be arbitrarily nested.
- Add `where` for predicate matching and `onCall` for computed return values.

Version 1.3:

- Switch to using thread context class loader when creating proxies by default
- Add ability to override proxy class loader strategy
- Add support for Scala 2.9.1
- Switch to using sbt 0.10
- Split test framework support out into separate libraries

Version 1.2:

- Add support for Scala 2.9.0-1
- Give a slightly nicer error if withExpectations isn't used

Version 1.1:

- Add the CallLogging trait

Version 1.0:

- Scala 2.9.0 support
- Test frameworks are now optional dependencies

Version 0.8:

- Integration with specs2 - [Daniel Westheide](https://github.com/dwestheide)

Version 0.7:

- Stop expectation errors being lost when the SUT swallows exceptions

Version 0.6:

- Add VerboseErrors trait
- Add withExpectations for JUnit3
- Fix anyNumberOfTimes, which didn't work because the range (0 to scala.Int.MaxValue) is actually empty

Version 0.5:

- Improved error messages for inSequence expectations

Version 0.4:

- Add Junit3 support

Version 0.3:

- Rethink argument matching to do away with the need for the ^ method
- Improve error messages

Version 0.2:

- Add support for wildcard and epsilon argument matchers

Version 0.1:

- Initial release
