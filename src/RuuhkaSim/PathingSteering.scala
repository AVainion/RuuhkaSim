package RuuhkaSim
import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

// Luokka joka sisältää suunnistus- ja ohjauslogiikkaan tarvittavia metodeja.

class PathingSteering {
  val room = SimulationController.board.room

  // heatmap luo breadth-first algoritmillä Map[Tile,Int]-rakenteen, jossa jokaiselle huoneen laatalle annetaan arvoksi polun pituus ovelta laatalle
  // kulkien pääilmansuuntia pitkin. Lähtee liikkeelle ovesta, jonka arvo on 0.

  def heatmap(start: Tile): Map[Tile, Int] = {
    var heatMap: Map[Tile, Int] = Map()
    var queue: Buffer[Tile] = Buffer(start)
    var temp: Buffer[Tile] = Buffer()
    var distance = 0
    var current = start

    def iterate(queue: Buffer[Tile], distance: Int) = {
      for (tile <- queue) {
        if (!heatMap.contains(tile) && (tile.typ != 0)) {
          heatMap += tile -> distance
        }
      }
    }
    while (!queue.isEmpty) {
      temp = Buffer()

      for (tile <- queue) {
        for (neighbor <- neighbors(tile)) {
          if (neighbor.isDefined) {
            if (!heatMap.contains(neighbor.get) && (neighbor.get.typ != 0) && !temp.contains(neighbor.get)) temp += neighbor.get
          }
        }
      }
      iterate(queue, distance)
      distance += 1
      queue = temp

    }
    heatMap
  }

  // Käyttäen yllä määriteltyä heatmap-metodia, luo ja palauttaa Map[Tile,Vector2D]-rakenteen, jossa jokaiselle huoneen laatalle annetaan arvoksi vektori,
  // joka osoittaa lyhyemmän polun suuntaan.
  // Vektori x-komp. = vasemmanpuoleisen laatan polun pituus - oikean puolen laatan polunpituus
  // Vektori y-komp. = yläpuolisen -||-   -   alapuolisen -||-

  def vectorField(pathMap: Map[Tile, Int]): Map[Tile, Vector2D] = {
    var field: Map[Tile, Vector2D] = Map()
    for (Tile <- pathMap.keys) {
      val adjacent = neighbors(Tile)
      val left = if (pathMap.contains(adjacent(2).getOrElse(Tile))) pathMap(adjacent(2).getOrElse(Tile)) else pathMap(Tile)
      val right = if (pathMap.contains(adjacent(3).getOrElse(Tile))) pathMap(adjacent(3).getOrElse(Tile)) else pathMap(Tile)
      val up = if (pathMap.contains(adjacent(0).getOrElse(Tile))) pathMap(adjacent(0).getOrElse(Tile)) else pathMap(Tile)
      val down = if (pathMap.contains(adjacent(1).getOrElse(Tile))) pathMap(adjacent(1).getOrElse(Tile)) else pathMap(Tile)
      var vector = new Vector2D(left - right, up - down)

      field += Tile -> vector.normalize
    }
    field

  }

  

  // Katsoo laatan pääilmansuunnissa sijaitsevat naapurit ja palauttaa ne Buffer[Option[Tile]]-kääreessä.

  def neighbors(tile: Tile): Buffer[Option[Tile]] = {
    val j = (tile.center.y.toInt - 25) / 50
    val i = (tile.center.x.toInt - 25) / 50
    var neighbors: Buffer[Option[Tile]] = Buffer()

    if (room.isDefinedAt(j - 1)) {
      neighbors += Some(room(j - 1)(i))

    } else {
      neighbors += None

    }

    if (room.isDefinedAt(j + 1)) {
      neighbors += Some(room(j + 1)(i))

    } else {
      neighbors += None

    }

    if (room(j).isDefinedAt(i - 1)) {

      neighbors += Some(room(j)(i - 1))
    } else {
      neighbors += None

    }

    if (room(j).isDefinedAt(i + 1)) {
      neighbors += Some(room(j)(i + 1))

    } else {
      neighbors += None

    }

    neighbors

  }

  // Katsoo laatan diagonaali-ilmansuunnissa sijaitsevat naapurit ja palauttaa ne Buffer[Option[Tile]]-kääreessä.

  def neighborsCorner(tile: Tile): Buffer[Option[Tile]] = {
    val j = (tile.center.y.toInt - 25) / 50
    val i = (tile.center.x.toInt - 25) / 50
    var neighbors: Buffer[Option[Tile]] = Buffer()

    if (room.isDefinedAt(j - 1)) {
      if (room(j - 1).isDefinedAt(i - 1)) {
        neighbors += Some(room(j - 1)(i - 1))
      } else {
        neighbors += None
      }

      if (room(j - 1).isDefinedAt(i + 1)) {
        neighbors += Some(room(j - 1)(i + 1))
      } else {
        neighbors += None
      }
    }

    if (room.isDefinedAt(j + 1)) {

      if (room(j + 1).isDefinedAt(i + 1)) {
        neighbors += Some(room(j + 1)(i + 1))
      } else {
        neighbors += None
      }

      if (room(j + 1).isDefinedAt(i - 1)) {
        neighbors += Some(room(j + 1)(i - 1))
      } else {
        neighbors += None
      }
    }

    neighbors

  }
}