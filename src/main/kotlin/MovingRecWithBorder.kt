import java.awt.Color
import java.awt.Graphics2D

/**
 * Аналогично овалу
 */
class MovingRecWithBorder(
    initX: Int = 0,
    initY: Int = 0,
    initH: Int = 100,
    initW: Int = 100,
    val coordStep: (Int, Int, Int, Int) -> Pair<Pair<Int, Int>, Pair<Int, Int>>
) {
    private var cX = initX
    private var cY = initY
    private var cW = initW
    private var cH = initH

    private var color = "FF0000"
    private var colorStep = 0;

    fun paint(g: Graphics2D) {
        val colorPair = getRainbowColorHex(color, colorStep)
        color = colorPair.first
        colorStep = colorPair.second

        g.color = Color.decode("#$color")
        g.fillRect(cX, cY, cW, cH)
        g.color = Color.BLACK
        g.drawRect(cX, cY, cW, cH)

        val step = coordStep(cX, cY, cW, cH)
        cX = step.first.first
        cY = step.first.second
        cW = step.second.first
        cH = step.second.second
    }
}