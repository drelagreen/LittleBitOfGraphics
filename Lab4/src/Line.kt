import org.jsfml.graphics.Color
import org.jsfml.graphics.PrimitiveType
import org.jsfml.graphics.RenderTarget
import org.jsfml.graphics.Vertex
import org.jsfml.system.Vector2f
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

data class Vector2d(
    var x: Double,
    var y: Double
) {
    operator fun plus(b: Vector2d): Vector2d {
        return Vector2d(this.x + b.x, this.y + b.y)
    }

    operator fun minus(b: Vector2d): Vector2d {
        return Vector2d(this.x - b.x, this.y - b.y)
    }
}

const val eps = 0.01

enum class IntersectType { In, Out, Parallel }

data class IntersectPoint(
    val point: Vector2d,
    val intersectType: IntersectType
)

enum class LineStyle {
    Line, Dashed
}

fun equals(a: Double, b: Double): Boolean {
    return abs(a - b) <= eps; }

fun less(a: Double, b: Double): Boolean {
    return b - a > eps; }

fun bigger(a: Double, b: Double): Boolean {
    return a - b > eps; }

fun equals(lhs: Vector2d, rhs: Vector2d): Boolean {
    return equals(lhs.x, rhs.x) && equals(lhs.y, rhs.y);
}

fun notEquals(lhs: Vector2d, rhs: Vector2d): Boolean {
    return !equals(lhs, rhs);
}

const val DASH_LENGTH = 5

class Line(
    val start: Vector2d,
    val end: Vector2d,
) {
    val a = end.y - start.y
    val b = start.x - end.x
    val c = a * start.x + b * start.y

    fun getIntersection(rhs: Line): IntersectPoint? {
        val determinant = this.a * rhs.b - rhs.a * this.b

        if (abs(determinant) < eps) {
            return null;
        } else {
            val x = (rhs.b * this.c - this.b * rhs.c) / determinant;
            val y = (this.a * rhs.c - rhs.a * this.c) / determinant;

            // check if point belongs to segment
            if (less(x, min(start.x, end.x)) ||
                bigger(x, max(start.x, end.x))
            )
                return null;
            if (less(y, min(start.y, end.y)) ||
                bigger(y, max(start.y, end.y))
            )
                return null;

            if (less(x, min(rhs.start.x, rhs.end.x)) ||
                bigger(x, max(rhs.start.x, rhs.end.x))
            )
                return null;
            if (less(y, min(rhs.start.y, rhs.end.y)) ||
                bigger(y, max(rhs.start.y, rhs.end.y))
            )
                return null;

            val dir = if (less(determinant, 0.0)) IntersectType.In else IntersectType.Out;
            val point = Vector2d(x, y)
            if (rhs.end == point || rhs.start == point || start == point || end == point) {
                return null;
            }

            val ret = IntersectPoint(point, dir);
            return ret;
        }
    }


    fun draw(render: RenderTarget, color: Color, lineStyle: LineStyle) {
        if (lineStyle == LineStyle.Line) {
            val vertices = arrayOf(
                Vertex(Vector2f(start.x.toFloat(), start.y.toFloat()), color),
                Vertex(Vector2f(end.x.toFloat(), end.y.toFloat()), color),
            )
            render.draw(vertices, PrimitiveType.LINES)
        } else {
            val x = end.x - start.x
            val y = end.y - start.y

            val lineLength = sqrt(x * x + y * y)

            val dashCount = lineLength / (2 * DASH_LENGTH)

            val lenX = x * DASH_LENGTH / lineLength
            val lenY = x * DASH_LENGTH / lineLength

            val startDash = start

            for (i: Int in 0 until dashCount.toInt()) {
                val endDash = startDash + Vector2d(lenX, lenY)

                val vertices = arrayOf(
                    Vertex(Vector2f(startDash.x.toFloat(), startDash.y.toFloat()), color),
                    Vertex(Vector2f(end.x.toFloat(), end.y.toFloat()), color)
                )

                render.draw(vertices, PrimitiveType.LINES)
            }
        }
    }
}


