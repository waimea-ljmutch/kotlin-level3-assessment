import com.formdev.flatlaf.themes.FlatMacDarkLaf
import java.awt.Color
import java.awt.Font
import java.awt.Image
import javax.swing.*

fun ImageIcon.scaled(width: Int, height: Int): ImageIcon =
    ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH))

/**
 * Application entry point
 */
fun main() {
    FlatMacDarkLaf.setup()          // Initialise the LAF

    val game = Game()                 // Get an app state object
    val window = MainWindow(game)    // Spawn the UI, passing in the app state

    SwingUtilities.invokeLater { window.show() }
}


/**
 * Manage app state
 *
 * @property score the points earned
 */
class Game() {
    var score = 0
    val islands = mutableListOf<Island>()

    init {
        val triassic = Island("triassic Island", 250)

        val kevin = Dino("Kevin", health = 100)

        kevin.species = "carnovore"
        triassic.addDino(kevin)
        islands.add(triassic)
    }

    fun scorePoints(points: Int) {
        score += points
    }

    fun resetScore() {
        score = 0
    }

}

class Island(val name: String, val distance: Int) {
    val dinos = mutableListOf<Dino>()

    fun addDino(dino: Dino) {
        dinos.add(dino)
    }
}

class Dino(val name: String, val health: Int) {
    var species: String = "Unknown"
    var typeOfDino: String = "typeOfDino"

}


/**
 * Main UI window, handles user clicks, etc.
 *
 * @param app the app state object
 */


class NewWindow(val owner: MainWindow, val game: Game) {
    private val frame = JFrame("My game")

    private val panel = JPanel().apply { layout = null }


    init {
        setupLayout()
        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(400, 300)
        panel.background = Color(0x1e1e2e)


    }

    private fun setupWindow() {
        frame.isResizable = false
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane = panel
        frame.pack()
        frame.setLocationRelativeTo(null)
    }

    private fun setupActions() {

    }

    fun updateUI() {

    }

    fun show() {
        val ownerBounds = owner.frame.bounds          // get location of the main window

    }


}

class MainWindow(val game: Game) {
    val frame = JFrame("WINDOW TITLE")
    private val panel = JPanel().apply { layout = null }


    private val titleLabel = JLabel("Dino Explorer")

    private val infoLabel = JLabel()
    private val clickButton = JButton("Dino Info")

    private val infoWindow = InfoWindow(this, game) // Pass app state to dialog too

    private val newWindow = NewWindow(this, game)

    private val mapIcon = ImageIcon(ClassLoader.getSystemResource("images/island-for-game.png")).scaled(1200, 600)

    private val mapButton = JButton(mapIcon)


    init {
        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(1200, 1000)

        titleLabel.setBounds(0, 0, 1200, 100)

        clickButton.setBounds(500, 800, 240, 40)



        mapButton.setBounds(0, 150, 1200, 600)


        panel.add(titleLabel)

        panel.add(clickButton)
        panel.add(mapButton)

    }

    private fun setupStyles() {
        titleLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 40)
        titleLabel.horizontalAlignment = SwingConstants.CENTER

        clickButton.font = Font(Font.SANS_SERIF, Font.PLAIN, 30)
        clickButton.background = Color(19104189)

    }

    private fun setupWindow() {
        frame.isResizable = false
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane = panel

        frame.pack()
        frame.setLocationRelativeTo(null) // center screen


        mapButton.isBorderPainted = false
        mapButton.isFocusPainted = false
        mapButton.isContentAreaFilled = false
    }


    private fun setupActions() {
        clickButton.addActionListener { handleMainClick() }

    }

    private fun handleMainClick() {
        clickButton.addActionListener { handleMainClick() }
        infoWindow.show()                                               // Update the app state
        // Update this window UI to reflect this
    }


    fun updateUI() {
    }

    fun show() {
        frame.isVisible = true
    }
}


/**
 * Info UI window is a child dialog and shows how the
 * app state can be shown / updated from multiple places
 *
 * @param owner the parent frame, used to position and layer the dialog correctly
 * @param app the app state object
 */
class InfoWindow(val owner: MainWindow, val game: Game) {
    private val dialog = JDialog(owner.frame, "dino collection", false)
    private val panel = JPanel().apply { layout = null }

    private val infoLabel = JLabel()

    private val carnoIcon = ImageIcon(ClassLoader.getSystemResource("images/dino-carno.png")).scaled(150, 150)

    private val carnoLabel = JLabel("Carnotorus!", carnoIcon, SwingConstants.LEFT)


    init {
        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(600, 600)

        carnoLabel.setBounds(100, 100, 400, 300)


        panel.add(carnoLabel)


        panel.add(infoLabel)

    }

    private fun setupStyles() {
        infoLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 16)

    }

    private fun setupWindow() {
        dialog.isResizable = false                              // Can't resize
        dialog.defaultCloseOperation = JDialog.HIDE_ON_CLOSE    // Hide upon window close
        dialog.contentPane = panel                              // Main content panel
        dialog.pack()

        carnoLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 22)
        carnoLabel.horizontalTextPosition = SwingConstants.RIGHT
    }


    private fun setupActions() {

    }

    private fun handleResetClick() {
        // Update the app state
        owner.updateUI()    // Update the UI to reflect this, via the main window
    }

    fun updateUI() {

        // Use app properties to display state

    }

    fun show() {
        val ownerBounds = owner.frame.bounds          // get location of the main window
        dialog.setLocation(                           // Position next to main window
            ownerBounds.x + ownerBounds.width + 10,
            ownerBounds.y
        )

        dialog.isVisible = true
    }
}