import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JFrame

class Window(private val image: Image) : JFrame("Lab2") {
    init {
        size = Dimension(image.width, image.height)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        background = Color.white
        isResizable = false
    }

    override fun paint(g: Graphics) {
        image.data.forEach {
            val colorRaw = image.pal[it.palY * 4 + it.palX]
            val a = colorRaw.ushr(24).and(0xff).toInt()
            val red = colorRaw.ushr(16).and(0xff).toInt()
            val green = colorRaw.ushr(8).and(0xff).toInt()
            val blue = colorRaw.and(0xff).toInt()

            g.color = Color(red, green, blue, a)
            g.drawRect(it.x, it.y, 1, 1)
        }
    }
}