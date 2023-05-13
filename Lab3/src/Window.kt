import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JFrame

class Window(
    private val image: BufferedImage,
    tittle: String
) : JFrame(tittle) {

    init {
        size = Dimension(image.width, image.height)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        background = Color.white
        isResizable = false
    }

    override fun paint(g: Graphics) {
        g.drawImage(image, 0, 0) { _, _, _, _, _, _ -> false }
    }
}