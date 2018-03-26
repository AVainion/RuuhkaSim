package RuuhkaSim
import scala.collection.mutable.Buffer

// AgentContoller huolehtii jokaisen agentin sijainnin ja nopeuden muutoksesta threadin ottaessa uudellen aika-askelen.

class AgentController {

  var agents: Buffer[Agent] = Buffer()

  var evasion: Boolean = true

  def add(agent: Agent) = agents += agent

  // Huoneen 1 agenttien määrä ja sijainnit, kutsutaan alustettaessa ohjelmaa Preset[1] painikkeella.

  def room1 = {
    agents = Buffer()
    for (j <- 1 to 25) {
      for (i <- 1 to 25) {

        add(new Agent(Vector2D(455 + 15 * i, 455 + 15 * j), Vector2D(0, 0)))
      }
    }
  }
  // Huoneen 2 - || -  . Huone 2 on jätetty tarkoituksella yksinkertaiseksi, tarkoituksena osoittaa ohjelman laajennettavuus
  // arbitraarisen monelle huoneelle, ja että vektorikentän ja polkukartan luonti eivät ole hardcoded-arvoja.

  def room2 = {
    agents = Buffer()
    for (j <- 1 to 6) {
      for (i <- 1 to 52) {

        add(new Agent(Vector2D(50 + 15 * i, 50 + 15 * j), Vector2D(0, 0)))
      }
    }
  }

  // Metodi lähellä sijaitsevien toisten agenttien havaitsemiselle. Palauttaa Map-tietorakenteen, jossa avaimina jokainen agentti 
  // ja arvoina tämän agentin lähistöllä olevat muut agentit ilmoitettuna vektorina Buffer:in sisällä.

  def agentneighbors(agents: Buffer[Agent]): Map[Agent, Buffer[Vector2D]] = {
    var agentNeighbors: Map[Agent, Buffer[Vector2D]] = Map()
    for (agent <- agents) {
      val others = agents.-(agent)
      var nbrs: Buffer[Vector2D] = Buffer()
      for (neighbor <- others) {
        if ((agent.location - neighbor.location).length <= 25.0) nbrs += (agent.location - neighbor.location)
      }
      agentNeighbors += agent -> nbrs

    }
    agentNeighbors
  }

  var neighborAg = agentneighbors(agents)

  // Agenttien update, päivittää ensin lähistöllä sijaitsevat agentit, jonka jälkeen liikuttaa jokaista agenttia kutsumalla näiden move-metodia.
  // lopuksi suodattaa pois agentit jotka ovat saavuttaneet oven.

  def update(delta: Long) = {

    neighborAg = agentneighbors(agents)

    for (agent <- agents) {
      agent.move(neighborAg, evasion)
    }
    agents = agents.filter(x => !x.atDoor)

  }

}