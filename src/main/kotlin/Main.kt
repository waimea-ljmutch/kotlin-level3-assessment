import com.formdev.flatlaf.themes.FlatMacDarkLaf
import java.awt.Color
import java.awt.Font
import javax.swing.*

/**
 * Application entry point
 */
fun main() {
    FlatMacDarkLaf.setup()          // Initialise the LAF

    val game = Game()                 // Get an app state object
    val window = MainWindow(game)    // Spawn the UI, passing in the app state

    SwingUtilities.invokeLater { window.show() }
    ClassLoader.getSystemResource()
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
class MainWindow(val game: Game) {
    val frame = JFrame("WINDOW TITLE")
    private val panel = JPanel().apply { layout = null }

    private val titleLabel = JLabel("Dino Explorer")

    private val infoLabel = JLabel()
    private val clickButton = JButton()

    private val infoWindow = InfoWindow(this, game)      // Pass app state to dialog too

    init {
        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(1200, 600)

        titleLabel.setBounds(30, 30, 1200, 600)

        clickButton.setBounds(30, 150, 240, 40)


        panel.add(titleLabel)

        panel.add(clickButton)

    }

    private fun setupStyles() {
        titleLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 32)

        clickButton.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
        clickButton.background = Color(0xcc0055)

    }

    private fun setupWindow() {
        frame.isResizable = false                           // Can't resize
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE  // Exit upon window close
        frame.contentPane = panel                           // Define the main content
        frame.pack()
        frame.setLocationRelativeTo(null)                   // Centre on the screen
    }

    private fun setupActions() {
        clickButton.addActionListener { handleMainClick() }

    }

    private fun handleMainClick() {
        // Update the app state
        // Update this window UI to reflect this
    }

    private fun handleInfoClick() {
        infoWindow.show()
    }

    fun updateUI() {
        val Icon = ImageIcon(ClassLoader.getSystemResource("compass.png"))
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
    private val dialog = JDialog(owner.frame, "DIALOG TITLE", false)
    private val panel = JPanel().apply { layout = null }

    private val infoLabel = JLabel()
    private val resetButton = JButton("Reset")

    init {
        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(600, 600)


        resetButton.setBounds(30, 120, 180, 30)

        panel.add(infoLabel)
        panel.add(resetButton)
    }

    private fun setupStyles() {
        infoLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
        resetButton.font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
    }

    private fun setupWindow() {
        dialog.isResizable = false                              // Can't resize
        dialog.defaultCloseOperation = JDialog.HIDE_ON_CLOSE    // Hide upon window close
        dialog.contentPane = panel                              // Main content panel
        dialog.pack()
    }

    private fun setupActions() {

    }

    private fun handleResetClick() {
        game.resetScore()    // Update the app state
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