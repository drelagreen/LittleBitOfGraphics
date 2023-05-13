import java.awt.Canvas
import java.awt.Graphics
import java.awt.Graphics2D
import java.util.Random

class Canvas1 : Canvas() {
    //Делаем фигуру и указываем лямбду поведения на каждый тик
    val rec1 = MovingRecWithBorder(0, 0) { x, y, w, h ->
        val coords = Pair(
            x + 1, y + 1
        )
        val sizes = Pair(
            w + 1, h + 1
        )
        Pair(coords, sizes)
    }
    val rec2 = MovingRecWithBorder(0, 0) { x, y, w, h ->
        val coords = Pair(
            x + 1, y
        )
        val sizes = Pair(
            w + 1, h
        )
        Pair(coords, sizes)
    }
    val rec3 = MovingRecWithBorder(0, 0) { x, y, w, h ->
        val coords = Pair(
            x, y + 1
        )
        val sizes = Pair(
            w,
            if (y > 400) {
                h - 1
            } else {
                h + 1
            }
        )
        Pair(coords, sizes)
    }
    val oval = MovingOvalWithBorder(100, 100, 100, 100){ x, y, w, h ->
        val coords = Pair(
            x + Random().nextInt(3) - 1, y + Random().nextInt(3) - 1
        )
        val sizes = Pair(
            w + Random().nextInt(3) - 1,
            h + Random().nextInt(3) - 1
        )
        Pair(coords, sizes)
    }
    override fun paint(g: Graphics?) {
        g as Graphics2D
        rec1.paint(g)
        rec2.paint(g)
        rec3.paint(g)
        oval.paint(g)
    }
}