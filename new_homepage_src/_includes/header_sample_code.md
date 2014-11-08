```scala
def testTurtle {
  val mockedTurtle = mock[Turtle]                              // Create mock Turtle object
 
  (mockedTurtle.setPosition _).expects(10.0, 10.0)             //
  (mockedTurtle.forward _).expects(5.0)                        // Set expectations
  (mockedTurtle.getPosition _).expects().returning(15.0, 10.0) // 
 
  drawLine(mockedTurtle, (10.0, 10.0), (15.0, 10.0))           // Exercise System Under Test
}
```
