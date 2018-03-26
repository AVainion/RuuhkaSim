package RuuhkaSim
import scala.collection.mutable.Buffer

// Luokka Agenteille. Agenteilla on parametreina paikkavektori ja nopeusvektori, sekä private val:eina maksiminopeus ja kiihtyvyys.

class Agent(var location: Vector2D, var velocity: Vector2D) {

  private val maxspeed = 3
  private val maxacceleration = 1

  // Metodi joka palauttaa Tile-olion, eli huoneen laatan jolla agentti tällä hetkellä sijaitsee käärittynä Optioniin.

  def spot: Option[Tile] = {
    for (tilearray <- SimulationController.board.room) {
      for (tile <- tilearray)
        if (tile.contains(location)) return Some(tile)
    }
    None
  }

  // Metodi joka määrittää ihannenopeuden, eli nopeusvektorin joka osoittaa suuntaan joka johtaa lähemmäs ovea.
  // Vektori noudetaan vektorikenttä-Mapista (vectorfield) avaimena agentin nykyinen sijainti-Tile.

  def desiredSpeed: Vector2D = {
    val spott = spot
    if (spot.isDefined) {
      if (spott.get.typ == 1) SimulationController.vectorfield(spott.get)
      else Vector2D(0, 0)
    } else Vector2D(0, 0)
  }

  // Metodi joka "jarruttaa" (antaa kulkusuuntaan nähden negatiivista kiihtyvyyttä) mikäli agentti on liian lähellä seinää.
  // Kutsuu sekä pääilmansuunnissa olevia naapurilaattoja (neighbors) että diagonaaleja (neighborsCorner), tarkastaa ovatko Tile:t tyyppiä (0), eli seiniä,
  // ja tämän jälkeen antaa tilanteeseen sopivan kiihtyvyysvektorin. Jarrutusvoima kasvaa mitä lähemmäs seinää agentti ajautuu.

  def wallbraking: Vector2D = {
    var acc: Vector2D = Vector2D(0, 0)
    if (spot.get.typ == 1) {

      val neighbors: Buffer[Option[Tile]] = SimulationController.steering.neighbors(spot.get)
      val neighborsCorner: Buffer[Option[Tile]] = SimulationController.steering.neighborsCorner(spot.get)

      if ((neighbors(0).get.typ == 0) && (location.y - neighbors(0).get.center.y) <= 40) {
        acc = acc + Vector2D(0, 25 / (location.y - neighbors(0).get.center.y))
      }
      if ((neighbors(1).get.typ == 0) && (neighbors(1).get.center.y - location.y) <= 40) {
        acc = acc + Vector2D(0, 25 / (location.y - neighbors(1).get.center.y))
      }
      if ((neighbors(2).get.typ == 0) && (location.x - neighbors(2).get.center.x) <= 40) {
        acc = acc + Vector2D(25 / (location.x - neighbors(2).get.center.x), 0)
      }
      if ((neighbors(3).get.typ == 0) && (neighbors(3).get.center.x - location.x) <= 40) {
        acc = acc + Vector2D(25 / (location.x - neighbors(3).get.center.x), 0)
      }

      if ((neighborsCorner(0).get.typ == 0) && (location - neighborsCorner(0).get.center).length <= 45) {
        acc = acc + Vector2D(30 / (location.x - neighborsCorner(0).get.center.x), 25 / (location.y - neighborsCorner(0).get.center.y))
      }
      if ((neighborsCorner(1).get.typ == 0) && (location - neighborsCorner(1).get.center).length <= 45) {
        acc = acc + Vector2D(30 / (location.x - neighborsCorner(1).get.center.x), 25 / (location.y - neighborsCorner(1).get.center.y))
      }
      if ((neighborsCorner(2).get.typ == 0) && (location - neighborsCorner(2).get.center).length <= 45) {
        acc = acc + Vector2D(30 / (location.x - neighborsCorner(2).get.center.x), 25 / (location.y - neighborsCorner(2).get.center.y))
      }
      if ((neighborsCorner(3).get.typ == 0) && (location - neighborsCorner(3).get.center).length <= 45) {
        acc = acc + Vector2D(30 / (location.x - neighborsCorner(3).get.center.x), 25 / (location.y - neighborsCorner(3).get.center.y))
      }

    }

    acc
  }

  // Metodi joka kutsuu angettien naapuri-Map:stä (NeighborAg) omaa nimeään avaimena. 
  // Saatu Buffer sisältää vektoreina etäisyydet lähellä oleviin muihin agentteihin.
  // Kokoaa näistä vektoreista yhdistelmäkiihtyvyysvektorin, joka saa sitä suurempia arvoja mitä lähempänä naapuri on agenttia.

  def evade(neighborAg: Map[Agent, Buffer[Vector2D]]): Vector2D = {
    var acc: Vector2D = Vector2D(0, 0)
    for (vector <- neighborAg(this)) {
      acc = acc + (vector.normalize * (4 / vector.length))
    }
    acc
  }

  // Itse liikutusmetodi. Kutsuu kaikkia yllä määriteltyjä metodeja määrittäkseen kokonaiskiihtyvyysvektorin muutettuna yksikkövektoriksi,
  // joka kerrotaan määritellyllä maksimikiihtyvyydellä. 
  // Uusi nopeus = vanha nopeus + kokonaiskiihtyvyys. Tämän jälkeen tarkistaa ylittääkö uusi nopeus maksiminopeuden, ja mikäli
  // kyllä, asettaa sen maksimiin. Tämän jälkeen sijainti muutetaan: uusi sijainti = vanha sijainti + uusi nopeus.
  // Lopuksi tarkastaa onko agentti seinän sisällä (eli agentin sijainti-Tile on tyyppiä 0), mikäli on, lisää yksikkövektorin poispäin seinä-Tilen 
  // keskipisteestä kunnes sijainti-Tile on jälleen lattiaa (tyyppiä 1)

  def move(neighborAg: Map[Agent, Buffer[Vector2D]], evasion: Boolean) = {

    if (evasion) { velocity = velocity + (desiredSpeed + wallbraking + evade(neighborAg)).normalize * maxacceleration }

    else { velocity = velocity + (desiredSpeed + wallbraking).normalize * maxacceleration }

    if (velocity.length.toInt >= maxspeed) velocity = velocity.normalize * maxspeed
    location = location + velocity
    while (spot.get.typ == 0) {
      location = location + (location - spot.get.center).normalize
    }
  }

  // metodi tarkistaa onko laatta (Tile), millä agentti sijaitsee tyyppiä 2, eli ovi. Palauttaa Boolean-arvon.

  def atDoor: Boolean = {
    if (spot.isDefined) {
      if (spot.get.typ == 2) true
      else false
    } else false
  }

}