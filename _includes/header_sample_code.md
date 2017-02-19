```scala
test("drawline turtle interaction") {
  // Create mock Turtle object
  val mockedTurtle = mock[Turtle]
 
  // Set expectations
  (mockedTurtle.setPosition _).expects(10.0, 10.0)
  (mockedTurtle.forward _).expects(5.0)
  (mockedTurtle.getPosition _).expects().returning(15.0, 10.0) 
 
  // Exercise System Under Test
  drawLine(mockedTurtle, (10.0, 10.0), (15.0, 10.0))
}
```
