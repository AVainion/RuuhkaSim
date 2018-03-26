package RuuhkaSim

// Luokka huoneen laatoille, jokaisella laatalla on keskipistepaikkavektori, sekä tyyppi.

class Tile(val center: Vector2D, val typ: Int) {

  // typ=0 seinä
  // typ=1 lattia
  // typ=2 ovi

  val leftupper: Vector2D = Vector2D(-25, -25)
  val rightlower: Vector2D = Vector2D(25, 25)
  val area: (Vector2D, Vector2D) = (center + leftupper, center + rightlower)

  // tarkastaa sijaitseeko paikkavektori Tile:n pinta-alan sisäpuolella vektoriavaruudessa.
  // Pinta-alat ovat suljettuja välejä kummastakin päästä, tällä yritetään välttää liukulukuaritmetiikan ongelmatapauksia.
  
  
  def contains(spot: Vector2D): Boolean ={
    (area._1.x <= spot.x && area._2.x >= spot.x) && (area._1.y <= spot.y && area._2.y >= spot.y)}
    
   

}