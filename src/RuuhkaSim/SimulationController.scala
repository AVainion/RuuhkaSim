
// RuuhkaSimulaatio

// Alpo Vainionpää 4/2017

package RuuhkaSim
import scala.swing._
import scala.swing.BorderPanel.Position._
import scala.collection.mutable.Buffer

//SimulationController alustaa ohjelman luomalla tarvittavat oliot sekä scala-swingin tarvitsemat komponentit. Samalla luodaan valitun huoneen polkukartta
// sekä vektorikenttä käyttäen breadth-first algoritmia. Tästä enemmän PathingSteering-luokassa.

object SimulationController extends SimpleSwingApplication {
  val agents = new AgentController
  val board = new Board()
  board.alustus

  val frame = new MainFrame()
  val loop = new Loop()
  val thread = new Thread(loop)
  val steering = new PathingSteering
  var pathMap = steering.heatmap(board.door)

  var vectorfield = steering.vectorField(pathMap)

  // Ohjelmaikkunan oikealle puolelle lisättävät painikkeet. roomX-painikkeet nollaavat huoneen, kutsuvat Board ja AgentsController-luokan
  // metodia valitulle huoneen numerolle, ja luovat valitusta huoneesta polkukartan ja vektorikentän. Keskeyttävät myös agenttien päivityksen threadissa.
  // Tämän jälkeen thread:in toiminta käynnistetään uudelleen.

  protected val room1Button = new Button {
    action = Action("          Preset [1]          ") {
      thread.suspend()
      board.alustus
      board.room1
      agents.room1
      pathMap = steering.heatmap(board.door)
      vectorfield = steering.vectorField(pathMap)
      loop.running = false
      thread.resume()
    }
    preferredSize = new Dimension(150, 75)
    minimumSize = new Dimension(150, 75)
  }
  protected val room2Button = new Button {
    action = Action("         Preset [2]           ") {
      thread.suspend()
      board.alustus
      board.room2
      agents.room2
      pathMap = steering.heatmap(board.door)
      vectorfield = steering.vectorField(pathMap)
      loop.running = false
      thread.resume()
    }
    preferredSize = new Dimension(150, 75)
    minimumSize = new Dimension(150, 75)
  }
  protected val startButton = new Button {
    action = Action("        Start / Stop         ") { loop.running = !loop.running }
    preferredSize = new Dimension(150, 75)
    minimumSize = new Dimension(150, 75)
  }
  protected val pathmapButton = new Button {
    action = Action("     Show Pathmap     ") { board.PM = (!board.PM) }
    preferredSize = new Dimension(150, 75)
    minimumSize = new Dimension(150, 75)
  }
  protected val vectorfieldButton = new Button {
    action = Action("   Show Vectorfield   ") { board.VF = (!board.VF) }
    preferredSize = new Dimension(150, 75)
    minimumSize = new Dimension(150, 75)
  }
  protected val resetButton = new Button {
    action = Action("       Reset room         ") {
      thread.suspend()
      loop.running = false
      board.alustus
      agents.agents = Buffer()
      pathMap = pathMap.empty
      vectorfield = vectorfield.empty

      thread.resume()
    }
    preferredSize = new Dimension(150, 75)
    minimumSize = new Dimension(150, 75)
  }
  protected val evasionButton = new Button {
    action = Action("     Evasion On/Off      ") { agents.evasion = !agents.evasion }
    preferredSize = new Dimension(150, 75)
    minimumSize = new Dimension(150, 75)
  }

  val buttonPanel = new BoxPanel(Orientation.Vertical) {
    preferredSize = new Dimension(150, 240)
    minimumSize = new Dimension(150, 240)
    maximumSize = new Dimension(150, 240)
  }
  buttonPanel.contents += (startButton, pathmapButton, vectorfieldButton, resetButton, room1Button, room2Button, evasionButton)

  thread.start()

  frame.title = "Ruuhkasimulaatio"

  frame.contents = new BorderPanel {
    layout += board -> Center
    layout += buttonPanel -> East
    preferredSize = new Dimension(1050, 900)
    minimumSize = new Dimension(1050, 900)
    maximumSize = new Dimension(1050, 900) //board.size
  }

  frame.peer.setLocationRelativeTo(null)
  frame.resizable = false
  frame.visible = true
  frame.pack

  def top = frame

  println(frame.size)
  println(board.size)
}