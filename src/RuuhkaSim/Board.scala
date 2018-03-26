package RuuhkaSim
import scala.swing._
import java.awt.Color
import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

class Board extends Panel {
  val greywall = new Color(75, 85, 85)
  val redcarpet = new Color(200, 50, 50)
  val flooring = new Color(170, 170, 230)
  val coffee = new Color(192, 255, 51)
  val redshirt = new Color(255, 0, 0)
  val room = Array.ofDim[Tile](18, 18)
  var door = new Tile(Vector2D(25, 25), 2)

  // Alustaa huoneen, täyttää huoneen Tile:llä jotka ovat reunoilla seiniä ja muualta lattiaa.

  def alustus = {
    val origo: Vector2D = Vector2D(0, 0)

    for (j <- 0 until room.length) {
      for (i <- 0 until room.head.length) {
        if (j == 0 || j == 17) {
          room(j)(i) = new Tile(Vector2D(25 + i * 50, 25 + j * 50), 0)
        } else if ((i == 0 && (j != 0 && j != 17)) || (i == 17 && (j != 0 && j != 17))) {
          room(j)(i) = new Tile(Vector2D(25 + i * 50, 25 + j * 50), 0)
        } else {
          room(j)(i) = new Tile(Vector2D(25 + i * 50, 25 + j * 50), 1)
        }
      }

    }

  }
  // Huoneen 1 seinän ja oven sijainnit

  def room1 = {
    door = new Tile(Vector2D(275, 875), 2)
    room(17)(5) = door

    for (j <- 3 until 17) {
      room(j)(7) = new Tile(Vector2D(375, 25 + j * 50), 0)
    }
    for (j <- 3 until 17) {
      room(j)(8) = new Tile(Vector2D(425, 25 + j * 50), 0)
    }

    for (i <- 2 until 15) {
      room(4)(i) = new Tile(Vector2D(25 + i * 50, 225), 0)
    }
    for (i <- 11 until 17) {
      room(7)(i) = new Tile(Vector2D(25 + i * 50, 375), 0)
    }
    room(6)(5) = new Tile(Vector2D(275, 325), 0)
    room(6)(2) = new Tile(Vector2D(125, 325), 0)
    room(8)(2) = new Tile(Vector2D(125, 425), 0)
    room(8)(5) = new Tile(Vector2D(275, 425), 0)
    room(10)(2) = new Tile(Vector2D(125, 525), 0)
    room(10)(5) = new Tile(Vector2D(275, 525), 0)
    room(12)(2) = new Tile(Vector2D(125, 625), 0)
    room(12)(5) = new Tile(Vector2D(275, 625), 0)
    room(14)(2) = new Tile(Vector2D(125, 725), 0)
    room(14)(5) = new Tile(Vector2D(275, 725), 0)

  }
  def room2 = {
    door = new Tile(Vector2D(475, 475), 2)
    room(9)(9) = door

  }
  // piirtämiseen käytettävät parametrit sekä override-määritys uudelleenpiirrolle. VF ja PM ovat Boolean arvot, jotka kertovat
  // piirretäänkö Vektorikenttä (VF) tai polkukartta (PM) näkyviin. Näitä ohjataan oikean reunan painikkeilla.

  val squareSize = 50
  val dimension = new Dimension(room.length * squareSize, room(0).length * squareSize)
  var VF: Boolean = false
  var PM: Boolean = false

  override def paintComponent(g: Graphics2D) {

    for (j <- 0 until room.length) {
      for (i <- 0 until room.head.length) {
        if (room(j)(i).typ == 0) {
          g.setColor(greywall)
          g.fillRect(room(j)(i).center.x.toInt - 25, room(j)(i).center.y.toInt - 25, squareSize, squareSize)
        } else if (room(j)(i).typ == 2) {
          g.setColor(redcarpet)
          g.fillRect(room(j)(i).center.x.toInt - 25, room(j)(i).center.y.toInt - 25, squareSize, squareSize)
        } else {
          g.setColor(flooring)
          g.fillRect(room(j)(i).center.x.toInt - 25, room(j)(i).center.y.toInt - 25, squareSize, squareSize)
        }
      }
    }
    for (agent <- SimulationController.agents.agents) {

      g.setColor(redshirt)
      g.fillOval(agent.location.x.toInt - 5, agent.location.y.toInt - 5, 10, 10)
    }
    if (PM) {
      for (tile <- SimulationController.pathMap) {
        g.setColor(coffee)
        g.drawString(tile._2.toString, tile._1.center.x.toInt, tile._1.center.y.toInt)
      }
    }
    if (VF) {
      for (tile <- SimulationController.vectorfield) {
        g.setColor(redshirt)
        g.drawLine(tile._1.center.x.toInt, tile._1.center.y.toInt, (tile._1.center.x + tile._2.x * 15).toInt, (tile._1.center.y + tile._2.y * 15).toInt)
      }
    }
  }

  def update(delta: Long) = {

  }

  override def size = dimension
  override def preferredSize = dimension
  override def minimumSize = dimension
  override def maximumSize = dimension

}