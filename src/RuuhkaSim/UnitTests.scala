package RuuhkaSim

import org.junit.Test
import org.junit.Assert._

/*
 * Unit tests for 2D Vectors
 *
 */

class UnitTests {

  @Test def testTask1() {
    val a: Vector2D = Vector2D(5.0, 7.5)
    val b: Vector2D = Vector2D(15.0, 10.0)
    val c = a + b
    val d = Vector2D(20.0, 17.5)
    assertEquals("Set values of 2D vectors to " ++ a.toString ++ " and " ++ b.toString
      ++ "but the sum a+b returned" ++ c.toString ++ " back",
      d,
      c)

  }
}