package RuuhkaSim
import scala.math.sqrt
import scala.math._

case class Vector2D(val x: Double, val y: Double) {

  //Vector2D luokalla lasketaan tavallisen kaksiulotteisen tason vektorimatematiikkaa. Sisältää tavalliset yhteen- ja vähennyslaskut,
  // kertomisen skalaarilla, vektorin normin eli pituuden, että metodin vektorin yksikkövektorin laskemiseen (normalize).

  def +(other: Vector2D) = {
    new Vector2D(this.x + other.x, this.y + other.y)
  }

  def -(other: Vector2D) = {
    new Vector2D(this.x - other.x, this.y - other.y)
  }

  def *(other: Double): Vector2D = {
    new Vector2D(this.x * other, this.y * other)
  }

  def *(other: Vector2D): Double = {
    this.x * other.x + this.y * other.y
  }

  def length: Double = {
    sqrt((this.x * this.x) + (this.y * this.y))
  }

  def normalize: Vector2D = {
    if (this.length == 0) new Vector2D(0, 0)
    else new Vector2D(this.x / this.length, this.y / this.length)
  }

  override def toString = {
    this.x + "i " + this.y + "j"
  }
}