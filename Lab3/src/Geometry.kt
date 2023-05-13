import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.sqrt

object Geometry {
    fun calculateAngle(first: Vector2f, second: Vector2f): Float {
        val firstCopy = Vector2f(first.x, first.y)
        val secondCopy = Vector2f(second.x, second.y)

        return when {
            secondCopy.x < firstCopy.x && secondCopy.y > firstCopy.y -> {
                secondCopy.x += 2 * abs(secondCopy.x - firstCopy.x)
                val angle = atan((secondCopy.y - firstCopy.y) / (secondCopy.x - firstCopy.x)) * 180 / PI
                (2 * (90 - angle) + angle).toFloat()
            }

            secondCopy.x < firstCopy.x && secondCopy.y < firstCopy.y -> {
                secondCopy.x += 2 * abs(secondCopy.x - firstCopy.x)
                secondCopy.y += 2 * abs(secondCopy.y - firstCopy.y)
                (180 + atan((secondCopy.y - firstCopy.y) / (secondCopy.x - firstCopy.x)) * 180 / PI).toFloat()
            }

            secondCopy.x > firstCopy.x && secondCopy.y < firstCopy.y -> {
                secondCopy.y += 2 * abs(secondCopy.y - firstCopy.y)
                (360 - atan((secondCopy.y - firstCopy.y) / (secondCopy.x - firstCopy.x)) * 180 / PI).toFloat()
            }

            secondCopy.x == firstCopy.x -> {
                if (secondCopy.y > firstCopy.y) {
                    90f
                } else {
                    270f
                }
            }

            secondCopy.y == firstCopy.y -> {
                if (secondCopy.x > firstCopy.x) {
                    0f
                } else {
                    180f
                }
            }

            else -> (atan((secondCopy.y - firstCopy.y) / (secondCopy.x - firstCopy.x)) * 180 / PI).toFloat()
        }
    }

    fun circlesIntersection(first: Vector2f, firstRadius: Int, second: Vector2f, secondRadius: Int): Pair<Vector2f, Vector2f> {
        val f = Vector2f(first.x, first.y)
        val s = Vector2f(second.x, second.y)

        val oX = 0 - f.x
        val oY = 0 - f.y
        f.x += oX
        f.y += oY
        s.x += oX
        s.y += oY

        val a = -2 * s.x
        val b = -2 * s.y
        val c = s.y * s.y + s.x * s.x + firstRadius * firstRadius - secondRadius * secondRadius
        val x0 = -a * c / (a * a + b * b)
        val y0 = -b * c / (a * a + b * b)
        val d = firstRadius * firstRadius - c * c / (a * a + b * b)
        val m = sqrt(d / (a * a + b * b))
        val ax = x0 + b * m - oX
        val bx = x0 - b * m - oX
        val ay = y0 - a * m - oY
        val by = y0 + a * m - oY
        return Pair(
            Vector2f(ax, ay), Vector2f(bx, by)
        )
    }

    data class Vector2f(
        var x: Float,
        var y: Float
    )

    data class Vector2i(
        var x: Int,
        var y: Int
    )
}