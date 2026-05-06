import com.formdev.flatlaf.themes.FlatMacDarkLaf
import java.awt.Color
import java.awt.Font
import java.awt.Image
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

fun ImageIcon.scaled(width: Int, height: Int): ImageIcon =
    ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH))

/**
 * game entry point
 */
fun main() {
    FlatMacDarkLaf.setup()          // Initialise the LAF

    val game = Game()                 // Get an app state object
    val window = MainWindow(game)    // Spawn the UI, passing in the app state

    SwingUtilities.invokeLater { window.show() }
}


/**
 * Manage game state
 *
 * @property dinoCount the points earned
 */
class Game() {
    var dinoCount = 0
    val islands = mutableListOf<Island>()
    var currentIsland: Island? = null

    init {
        val triassic = Island("triassic Island", 210, 120, 25)
        val jurassic = Island("jurassic Island", 303, 317, 25)
        val barassic = Island("barassic Island", 694, 205, 25)
        val kevin = Dino("Kevin", "carnivore", "dino-carno.png")
        val dave = Dino("Dave", "carnivore", "dino-trex.png")
        val drako = Dino("drako", "herbivore", "dino-stego.png")

        triassic.addDino(kevin)
        barassic.addDino(drako)
        jurassic.addDino(dave)
        islands.add(triassic)
        islands.add(jurassic)
        islands.add(barassic)
    }

    fun gotoIsland(island: Island) {
        currentIsland = island
    }

    fun scorePoint() {
        dinoCount++
    }

    fun isOver(): Boolean {
        return dinoCount >= 3
    }
}

class Island(
    val name: String,
    val mapX: Int,
    val mapY: Int,
    var mapR: Int
) {
    var dino: Dino? = null

    fun addDino(newDino: Dino) {
        dino = newDino
    }

    fun removeDino() {
        dino = null
    }
}

// Data class for dinos
class Dino(val name: String, val species: String, val image: String)


/**
 * Main UI window, handles user clicks, etc.
 *
 * @param game the game state object
 */

class MainWindow(val game: Game) {
    val frame = JFrame("WINDOW TITLE")
    private val panel = JPanel().apply { layout = null }


    private val titleLabel = JLabel("Dino Explorer")
    private val islandLabel = JLabel("Click the map to travel to an island...")

    private val dinoButton = JButton("Search Island...")
    private val infoWindow = InfoWindow(this, game) // Pass app state to dialog too

    private val countLabel = JLabel("Count of Dino")
    // putting the dino count score next to the dino label to show the dinos you have collected


    private val mapIcon = ImageIcon(ClassLoader.getSystemResource("images/island-for-game.png")).scaled(1200, 600)
    private val targetIcon = ImageIcon(ClassLoader.getSystemResource("images/red_target.png")).scaled(100, 100)

    private val mapLabel = JLabel(mapIcon)
    private val targetLabel = JLabel(targetIcon)


    init {
        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(1200, 800)

        titleLabel.setBounds(0, 0, 1200, 100)
        dinoButton.setBounds(800, 730, 240, 40)
        mapLabel.setBounds(0, 100, 1200, 600)
        targetLabel.setBounds(0, 0, 100, 100)
        islandLabel.setBounds(100, 730, 600, 40)
        countLabel.setBounds(500, 730, 600, 40)

        panel.add(titleLabel)
        panel.add(dinoButton)
        panel.add(mapLabel)
        panel.add(islandLabel)
        panel.add(targetLabel)
        panel.add(countLabel)

        panel.setComponentZOrder(targetLabel, 0)

        targetLabel.isVisible = false
        dinoButton.isEnabled = false
    }

    private fun setupStyles() {
        titleLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 40)
        titleLabel.horizontalAlignment = SwingConstants.CENTER

        dinoButton.font = Font(Font.SANS_SERIF, Font.PLAIN, 30)
        dinoButton.background = Color(19104189)

        islandLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)

        countLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)

    }

    private fun setupWindow() {
        frame.isResizable = false
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane = panel

        frame.pack()
        frame.setLocationRelativeTo(null) // center screen
    }


    private fun setupActions() {
        dinoButton.addActionListener { handleMainClick() }
        mapLabel.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                handleMapClick(e.x, e.y)
            }
        })
    }

    private fun handleMapClick(x: Int, y: Int) {
        println("$x, $y")

        for (island in game.islands) {
            if (
                x >= island.mapX - island.mapR &&
                y >= island.mapY - island.mapR &&
                x <= island.mapX + island.mapR &&
                y <= island.mapY + island.mapR
            ) {

                game.gotoIsland(island)

                targetLabel.setLocation(island.mapX - 50, island.mapY + 50)
                targetLabel.isVisible = true

                islandLabel.text = "Going to ${island.name}"
                dinoButton.isEnabled = true
            }
        }
    }

    private fun handleMainClick() {
        dinoButton.addActionListener { handleMainClick() }
        infoWindow.show()                                               // Update the app state
        // Update this window UI to reflect this
    }


    fun updateUI() {
        countLabel.text = "Collected: ${game.dinoCount} / 3"
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

    private val infoLabel = JLabel("DINO NAME")
    private val dinoLabel = JLabel()
    private val collectButton = JButton("Collect Dino")


    init {
        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(300, 440)

        dinoLabel.setBounds(30, 30, 240, 240)
        infoLabel.setBounds(30, 300, 240, 40)
        collectButton.setBounds(30, 370, 240, 40)

        panel.add(dinoLabel)
        panel.add(infoLabel)
        panel.add(collectButton)
    }

    private fun setupStyles() {
        infoLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
    }

    private fun setupWindow() {
        dialog.isResizable = false                              // Can't resize
        dialog.defaultCloseOperation = JDialog.HIDE_ON_CLOSE    // Hide upon window close
        dialog.contentPane = panel                              // Main content panel
        dialog.pack()

        dinoLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 22)
        dinoLabel.horizontalTextPosition = SwingConstants.RIGHT
    }


    private fun setupActions() {
        collectButton.addActionListener { handleDinoClick() }
    }

    fun handleDinoClick() {
        if (game.currentIsland?.dino == null) return

        game.scorePoint()
        game.currentIsland?.removeDino()
        updateUI()
        owner.updateUI()

        if (game.isOver()) {
            JOptionPane.showMessageDialog(
                owner.frame,
                "You found them all!!!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            )
        }
    }

    fun updateUI() {

        if (game.currentIsland == null) {
            infoLabel.text = "Select an island to go to..."
            collectButton.isEnabled = false
            return
        }

        if (game.currentIsland!!.dino == null) {
            infoLabel.text = "No dinos here!"
            dinoLabel.icon = null
            collectButton.isEnabled = false
            return
        }

        val dino = game.currentIsland!!.dino!!
        val dinoFile = "images/" + dino.image
        val dinoName = dino.name

        val dinoIcon = ImageIcon(ClassLoader.getSystemResource(dinoFile)).scaled(240, 240)
        infoLabel.text = "You have found $dinoName!!!"
        dinoLabel.icon = dinoIcon
        collectButton.isEnabled = true
    }

    fun show() {
        val ownerBounds = owner.frame.bounds          // get location of the main window
        dialog.setLocation(                           // Position next to main window
            ownerBounds.x + ownerBounds.width + 10,
            ownerBounds.y
        )

        dialog.isVisible = true

        updateUI()
    }
}