package RuuhkaSim

// Loop on ohjelman ajosilmukka. 30 päivitystä sekunnissa.

class Loop extends Runnable {
  val FPS = 30
  var running: Boolean = false
  override def run() {
    while (true) {

      val sleepTime = 1000 / FPS
      Thread.sleep(sleepTime)
      update(sleepTime)
    }
  }

  // Silmukan update kutsuu AgentControllerin updatemetodia, joka päivittää jokaisen agentin nopeuden ja paikan.
  // Update kutsuu myös Board:in update metodia, joka kutsuu scala-swingin uudelleenpiirtoa, tämä päivittää ohjelmaruudun vastaamaan tapahtuneita muutoksia.

  def update(delta: Long) = {

    if (running) { SimulationController.agents.update(delta) }

    SimulationController.board.repaint
  }
}