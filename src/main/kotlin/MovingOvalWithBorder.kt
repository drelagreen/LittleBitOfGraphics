import java.awt.Color
import java.awt.Graphics2D

/**
 * Задаем начальные положения и размеры
 * В виде лямбды передаем поведение на каждый тик
 * на вход лямбде поведения идет 4 инта
 * на выход - пара из двух пар, по два инта в каждой
 * Pair(Pair(Int, Int), Pair(Int,Int))
 *
 * первые 2 инта новое положение
 * вторые два инта - новые размеры
 */
class MovingOvalWithBorder(
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
        // Красиво меняем цвет
        val colorPair = getRainbowColorHex(color, colorStep)
        color = colorPair.first
        colorStep = colorPair.second

        //Заливаем овал
        g.color = Color.decode("#$color")
        g.fillOval(cX, cY, cW, cH)
        //Делаем ему границу черного цвета
        g.color = Color.BLACK
        g.drawOval(cX, cY, cW, cH)

        //Получаем и применяем новые параметры из лямбды
        val step = coordStep(cX, cY, cW, cH)
        cX = step.first.first
        cY = step.first.second
        cW = step.second.first
        cH = step.second.second
    }
}