import Geometry.Vector2f
import Geometry.Vector2i
import java.awt.Color
import java.awt.image.BufferedImage

const val W = 500
const val H = 500
const val CIRCLE_RADIUS = 100
const val BENCHMARK = true
const val SLEEP = 200L
val FILL_MODE = Mode.A
val PAINT_MODE = Mode.A
fun main() {
    if (BENCHMARK) {
        benchmark()
    } else {
        drawTest()
    }
}

fun benchmark() {
    val image1 = BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB)
    val image3 = BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB)
    val image4 = BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB)
    var image5 = BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB)
    var image6 = BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB)

    val colors = listOf(
        Color(0xFF, 0x00, 0x00),
        Color(0x00, 0xFF, 0x00),
        Color(0x00, 0x00, 0xFF),
        Color(0xD9, 0xDB, 0xF1),
        Color(0xAF, 0xDC, 0x9C),
        Color(0x54, 0x78, 0xA1),
        Color(0xD7, 0xAF, 0xD7)
    )

    val bounds = Vector2i(W, H)

    var sumAllPx = 0L
    var sumAllMs = 0.0

    val stats1 = Stats()
    val stats2 = Stats()

    var sumPx = 0L
    var sumMs = 0.0

    println("Замеряем рисование дуги c изменением угла (R = $CIRCLE_RADIUS):")
    (30..360).step(30).forEach {
        sumPx += stats1.pixels + stats2.pixels
        sumMs += stats1.timeMs + stats2.timeMs
        stats1.timeNs = 0
        stats1.pixels = 0
        stats2.timeNs = 0
        stats2.pixels = 0

        Drawer.arcPolar(
            image1,
            Vector2i(250 - (it / 30) * 2, 250),
            CIRCLE_RADIUS,
            colors[2].rgb,
            0f,
            it.toFloat(),
            stats2
        )

        Drawer.arcBresenham(
            image1,
            Vector2i(250 + (it / 30) * 2, 250),
            CIRCLE_RADIUS,
            colors[1].rgb,
            0f,
            it.toFloat(),
            stats1
        )

        println(
            """
            ////////////////////////////////////////////////////////////////////////////////////
            
            Итерация ${it / 30} / 12 | Угол - $it градусов: 
            ____________________________________________________________________________________
            |  Алгоритм Брезенхема - Закрашено ${stats1.pixels} за ${stats1.timeMs} ms
            __________________________________________
            |  Полярные координаты - Закрашено ${stats2.pixels} за ${stats2.timeMs} ms
            ____________________________________________________________________________________
        """.trimIndent()
        )
    }

    Window(image1, "Дуги")
    Thread.sleep(SLEEP)

    println(
        """
        Замер рисования дуги с изменением угла окончен
        Суммарно пикселей закрашено - $sumPx шт
        Суммарно времени затрачено изменено - $sumMs ms
        
        Замеряем рисование окружности c изменением радиуса:
    """.trimIndent()
    )
    sumAllMs += sumMs
    sumAllPx += sumPx
    sumPx = 0
    sumMs = 0.0
    stats1.timeNs = 0
    stats1.pixels = 0
    stats2.timeNs = 0
    stats2.pixels = 0

    (10..100).step(10).forEach {
        sumPx += stats1.pixels + stats2.pixels
        sumMs += stats1.timeMs + stats2.timeMs
        stats1.timeNs = 0
        stats1.pixels = 0
        stats2.timeNs = 0
        stats2.pixels = 0

        Drawer.arcBresenham(image3, Vector2i(250, 250), it, colors[5].rgb, 0f, 360f, stats1)
        Drawer.arcPolar(image4, Vector2i(250, 250), it, colors[4].rgb, 0f, 360f, stats2)

        println(
            """
            ////////////////////////////////////////////////////////////////////////////////////
            
            Итерация ${it / 10} / 10 | Радиус - $it пикселей: 
            ____________________________________________________________________________________
            |  Алгоритм Брезенхема - Закрашено ${stats1.pixels} за ${stats1.timeMs} ms
            __________________________________________
            |  Полярные координаты - Закрашено ${stats2.pixels} за ${stats2.timeMs} ms
            ____________________________________________________________________________________
        """.trimIndent()
        )
    }

    Window(image3, "Окружности | Брезенхем")
    Thread.sleep(SLEEP)

    Window(image4, "Окружности | Полярные координаты")
    Thread.sleep(SLEEP)

    println(
        """
        Замер рисования окружности с изменением радиуса окончен
        Суммарно пикселей закрашено - $sumPx шт
        Суммарно времени затрачено изменено - $sumMs ms
        
        Замеряем заливку окружности с изменяющимся радиусом:
    """.trimIndent()
    )

    sumAllMs += sumMs
    sumAllPx += sumPx
    sumPx = 0
    sumMs = 0.0
    stats1.timeNs = 0
    stats1.pixels = 0
    stats2.timeNs = 0
    stats2.pixels = 0

    (10..100).step(10).forEach {
        image5 = BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB)
        image6 = BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB)

        sumPx += stats1.pixels + stats2.pixels
        sumMs += stats1.timeMs + stats2.timeMs
        stats1.timeNs = 0
        stats1.pixels = 0
        stats2.timeNs = 0
        stats2.pixels = 0

        Drawer.arcBresenham(image5, Vector2i(250, 250), it, colors[0].rgb, 0f, 361f, Stats())
        Drawer.fillRec(image5, Vector2i(250, 250), bounds, colors[3].rgb, stats1)

        Drawer.arcBresenham(image6, Vector2i(250, 250), it, colors[2].rgb, 0f, 361f, Stats())
        Drawer.fillStack(image6, Vector2i(250, 250), bounds, colors[6].rgb, stats2)

        println(
            """
            ////////////////////////////////////////////////////////////////////////////////////
            
            Итерация ${it / 10} / 10 | Радиус - $it пикселей: 
            ____________________________________________________________________________________
            |  Рекурсивный алгоритм - Закрашено ${stats1.pixels} за ${stats1.timeMs} ms
            __________________________________________
            |  Алгоритм с использованием стэка - Закрашено ${stats2.pixels} за ${stats2.timeMs} ms
            ____________________________________________________________________________________
        """.trimIndent()
        )
    }

    Window(image5, "Заливка | Рекурсия")
    Thread.sleep(SLEEP)

    Window(image6, "Заливка | Стэк")
    Thread.sleep(SLEEP)

    sumAllMs += sumMs
    sumAllPx += sumPx
    println(
        """
        Замер заливки окончен
        Суммарно пикселей закрашено - $sumPx шт
        Суммарно времени затрачено изменено - $sumMs ms
        
        Всего за прогон закрашено - $sumAllPx пикселей
        За $sumAllMs ms
    """.trimIndent()
    )
}

fun drawTest() {
    val image = BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB)
    val statsFill = Stats()
    val statsPaint = Stats()

    val colors = listOf(
        Color(0xFF, 0x00, 0x00),
        Color(0x00, 0xFF, 0x00),
        Color(0x00, 0x00, 0xFF),
        Color(0xD9, 0xDB, 0xF1),
        Color(0xAF, 0xDC, 0x9C),
        Color(0x54, 0x78, 0xA1),
        Color(0xD7, 0xAF, 0xD7)
    )

    val fillFun = if (FILL_MODE == Mode.A) Drawer::fillRec else Drawer::fillStack
    val paintFun = if (PAINT_MODE == Mode.A) Drawer::arcBresenham else Drawer::arcPolar
    val bounds = Vector2i(W, H)

    val circle1 = Vector2f(170f, 170f)
    val circle2 = Vector2f(circle1.x + 1.5f * CIRCLE_RADIUS, circle1.y)
    val circle3 = Vector2f(circle1.x + 0.75f * CIRCLE_RADIUS, circle1.y + 1.25f * CIRCLE_RADIUS)

    var intersection = Geometry.circlesIntersection(circle1, CIRCLE_RADIUS, circle2, CIRCLE_RADIUS)

    var angle1 = Geometry.calculateAngle(circle1, intersection.first)
    var angle2 = Geometry.calculateAngle(circle1, intersection.second)
    var angle3 = Geometry.calculateAngle(circle2, intersection.first)
    var angle4 = Geometry.calculateAngle(circle2, intersection.second)

    paintFun(
        image,
        Vector2i(circle1.x.toInt(), circle1.y.toInt()),
        CIRCLE_RADIUS,
        colors[0].rgb,
        angle2,
        angle1,
        statsPaint
    )
    Window(image, "1")
    Thread.sleep(SLEEP)
    paintFun(
        image,
        Vector2i(circle2.x.toInt(), circle2.y.toInt()),
        CIRCLE_RADIUS,
        colors[1].rgb,
        angle3,
        angle4,
        statsPaint
    )
    Window(image, "2")
    Thread.sleep(SLEEP)
    val fill1 = Vector2i(intersection.second.x.toInt(), intersection.second.y.toInt() + 4)
    val fill2 = Vector2i(intersection.first.x.toInt(), intersection.first.y.toInt() - 4)

    intersection = Geometry.circlesIntersection(circle2, CIRCLE_RADIUS, circle3, CIRCLE_RADIUS)

    angle1 = Geometry.calculateAngle(circle2, intersection.first)
    angle2 = Geometry.calculateAngle(circle2, intersection.second)
    angle3 = Geometry.calculateAngle(circle3, intersection.first)
    angle4 = Geometry.calculateAngle(circle3, intersection.second)

    paintFun(
        image,
        Vector2i(circle2.x.toInt(), circle2.y.toInt()),
        CIRCLE_RADIUS,
        colors[1].rgb,
        angle2,
        angle1,
        statsPaint
    )
    Window(image, "3")
    Thread.sleep(SLEEP)
    paintFun(
        image,
        Vector2i(circle3.x.toInt(), circle3.y.toInt()),
        CIRCLE_RADIUS,
        colors[2].rgb,
        angle3,
        angle4,
        statsPaint
    )
    Window(image, "4")
    Thread.sleep(SLEEP)
    val fill3 = Vector2i(intersection.first.x.toInt() - 4, intersection.first.y.toInt() + 6)

    intersection = Geometry.circlesIntersection(circle3, CIRCLE_RADIUS, circle1, CIRCLE_RADIUS)

    angle1 = Geometry.calculateAngle(circle3, intersection.first)
    angle2 = Geometry.calculateAngle(circle3, intersection.second)
    angle3 = Geometry.calculateAngle(circle1, intersection.first)
    angle4 = Geometry.calculateAngle(circle1, intersection.second)

    paintFun(
        image,
        Vector2i(circle3.x.toInt(), circle3.y.toInt()),
        CIRCLE_RADIUS,
        colors[2].rgb,
        angle2,
        angle1,
        statsPaint
    )
    Window(image, "5")
    Thread.sleep(SLEEP)
    paintFun(
        image,
        Vector2i(circle1.x.toInt(), circle1.y.toInt()),
        CIRCLE_RADIUS,
        colors[0].rgb,
        angle3,
        angle4,
        statsPaint
    )
    Window(image, "6")
    Thread.sleep(SLEEP)

    val fill4 = Vector2i(intersection.first.x.toInt() + 4, intersection.first.y.toInt() + 6)

    fillFun(image, fill1, bounds, colors[3].rgb, statsFill)
    Window(image, "7")
    Thread.sleep(SLEEP)
    fillFun(image, fill2, bounds, colors[4].rgb, statsFill)
    Window(image, "8")
    Thread.sleep(SLEEP)
    fillFun(image, fill3, bounds, colors[5].rgb, statsFill)
    Window(image, "9")
    Thread.sleep(SLEEP)
    fillFun(image, fill4, bounds, colors[6].rgb, statsFill)
    Window(image, "10")
    Thread.sleep(SLEEP)

    println(
        """
        Окно $W x $H
        Радиус окружности - $CIRCLE_RADIUS
        
        На рисование фигур ушло ${statsPaint.timeMs} ms
        На закрашивание фигур ушло ${statsFill.timeMs} ms
        В режиме рисования было закрашено ${statsPaint.pixels} пикселей
        В режиме заливки было закрашено ${statsFill.pixels} пикселей
        
        Всего изменено пикселей - ${statsFill.pixels + statsPaint.pixels} px
        Всего времени затрачено - ${statsFill.timeMs + statsPaint.timeMs} ms
        
        Метод растеризации дуги - ${if (PAINT_MODE == Mode.A) "Алгоритм Брезенхема" else "Полярные координаты"}
        Метод заливки - ${if (FILL_MODE == Mode.A) "Рекурсивный алгоритм" else "Рекурсивный алгоритм с использованием стека"}
    """.trimIndent()
    )

    Window(image, "Лаб3")
}

enum class Mode {
    A, B
}


