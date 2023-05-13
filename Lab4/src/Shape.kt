import org.jsfml.graphics.Color
import org.jsfml.graphics.RenderTarget
import java.util.ArrayList
import java.util.LinkedList

class Shape(
    val points: List<Vector2d>
) {
    var edgeEnd = 0
    var edgeStart = points.size - 1
    val edges = ArrayList<Line>()

    init {
        while (edgeEnd != points.size) {
            edges.add(Line(points[edgeStart], points[edgeEnd]))
            edgeStart = edgeEnd++
        }
    }

    fun contains(point: Vector2d): Boolean {
        edges.forEach { edge ->
            val det =
                ((edge.end.x - edge.start.x) * (-point.y + edge.start.y)
                        - (-edge.end.y + edge.start.y) * (point.x - edge.start.x))
            if (bigger(det,0.0)) {
                return false
            }
        }

        return true
    }

    fun contains(other: Shape): Boolean {
        other.points.forEach { point ->
            if (!contains(point)) {
                return false
            }
        }

        return true
    }

    fun draw(render: RenderTarget, lineColor: Color, lineStyle: LineStyle = LineStyle.Line) {
        edges.forEach { edge ->
            edge.draw(render, lineColor, lineStyle)
        }
    }
}