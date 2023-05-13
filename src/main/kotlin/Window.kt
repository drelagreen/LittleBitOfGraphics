import java.awt.*
import java.util.*
import javax.swing.JFrame

// В конструктор парента имя окна
class Window : JFrame("Lab1") {
    // Устанавливаем количество кадров в секунду
    val FRAME_RATE = 60

    // Переменные для замера времени, чтобы отрисовывать N кадров в секунду
    private var time = 0L
    private var lastRenderTime = 0L
    private val timeBetweenFrames = 1000L / FRAME_RATE

    //Инициализация окна и суперцикл
    init {
        // Дефолтный размер окна (можно менять)
        size = Dimension(720, 720)
        // Выход по нажатии на крест
        defaultCloseOperation = EXIT_ON_CLOSE
        // Видимое
        isVisible = true
        //У окна бэкграунд будет белый
        background = Color.white

        // Создаем канвас с описаными там фигурами
        val canvas = Canvas1().apply {
            background = Color.white
        }
        //Добавляем канвас в окно
        add(canvas)


        while (true) {
            time = System.currentTimeMillis()
            if (time - lastRenderTime >= timeBetweenFrames) {
                System.err.println("$time $lastRenderTime ${time - lastRenderTime}")
                lastRenderTime = time
                canvas.repaint()
                //                canvas.paint(canvas.graphics)
                /* Если сделать так то будут дерганья, но этот метод больше соответсвует заданию, потому что тут мы можем сами настроить как именно
                * затирать канвас.
                * Но, в связи с тем что repaint работает так как надо, мы будем использовать именно его
                */
            }
        }
    }
}

fun getRainbowColorHex(colorHex: String, step: Int): Pair<String, Int> {
    var colorStep = step
    var colorDecimal = toDecimal(colorHex)
    when (colorStep) {
        0 -> {
            colorDecimal += toDecimal("100")
            if (colorDecimal >= toDecimal("FFFF00")) {
                colorStep = 1
            }
        }

        1 -> {
            colorDecimal -= toDecimal("10000")
            if (colorDecimal <= toDecimal("FF00")) {
                colorStep = 2
            }
        }

        2 -> {
            colorDecimal += 1
            if (colorDecimal >= toDecimal("FFFF")) {
                colorStep = 3
            }
        }

        3 -> {
            colorDecimal -= toDecimal("100")
            if (colorDecimal <= toDecimal("FF")) {
                colorStep = 4
            }
        }

        4 -> {
            colorDecimal += toDecimal("10000")
            if (colorDecimal >= toDecimal("FF00FF")) {
                colorStep = 5
            }
        }

        5 -> {
            colorDecimal -= 1
            if (colorDecimal <= toDecimal("FF0000")) {
                colorStep = 0
            }
        }
    }
    return Pair(toHex(colorDecimal), colorStep)
}

fun toDecimal(hex: String): Int {
    var hexx = hex
    val digits = "0123456789ABCDEF"
    hexx = hexx.uppercase(Locale.getDefault())
    var a = 0
    for (element in hexx) {
        val d = digits.indexOf(element)
        a = 16 * a + d
    }
    return a
}

fun toHex(decimal: Int): String {
    var decimall = decimal
    var rem: Int
    var hex = ""
    val hexChars = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
    while (decimall > 0) {
        rem = decimall % 16
        hex = hexChars[rem].toString() + hex
        decimall /= 16
    }
    return hex
}