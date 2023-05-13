import org.jsfml.graphics.Color
import org.jsfml.graphics.RenderWindow
import org.jsfml.window.Mouse
import org.jsfml.window.VideoMode
import org.jsfml.window.event.Event

const val showMasked = false
const val maskWidth = 500
const val maskHeight = 500

fun main() {
    val container = Container()
    val config = Config.Test

    container.addLayer(
        BackgroundLayer(
            Color(
                config.backgroundColor.red,
                config.backgroundColor.green,
                config.backgroundColor.blue
            )
        )
    )

    config.shapesLayers.forEach { layer ->
        val shapes = ArrayList<Shape>()
        layer.shapes.forEach { shape ->
            val points = ArrayList<Vector2d>()
            shape.points.forEach { point ->
                points.add(Vector2d(point.x.toDouble(), point.y.toDouble()))
            }
            shapes.add(Shape(points))
        }
        val shapesLayer = ShapesLayer(shapes)
        container.addLayer(shapesLayer)
    }

    val mask = WindowMaskLayer(maskWidth, maskHeight)
    container.addLayer(mask)

    val window = RenderWindow(VideoMode(1000, 1000), "Lab 4")

    while (window.isOpen) {
        val event = window.pollEvent()
        if (event != null && event.type == Event.Type.CLOSED) {
            window.close()
        }

        window.clear()

        val mousePosition = Mouse.getPosition(window)
        mask.position = mousePosition

        container.draw(window, showMasked)

        window.display()
    }

}


data class Config(
    val backgroundColor: BackgroundColor,
    val shapesLayers: List<ShapesLayer>
) {
    data class BackgroundColor(
        val red: Int,
        val green: Int,
        val blue: Int,
    )

    data class ShapesLayer(
        val shapes: List<Shape>
    ) {
        data class Shape(
            val points: List<Point>
        ) {
            data class Point(
                val x: Int,
                val y: Int,
            )
        }
    }

    companion object {
        val Test = Config(
            backgroundColor = BackgroundColor(
                red = 20,
                green = 20,
                blue = 20,
            ),
            shapesLayers = listOf(
                ShapesLayer(
                    shapes = listOf(
                        ShapesLayer.Shape(
                            points = listOf(
                                ShapesLayer.Shape.Point(
                                    x = 200,
                                    y = 200
                                ),
                                ShapesLayer.Shape.Point(
                                    x = 300,
                                    y = 200
                                ),
                                ShapesLayer.Shape.Point(
                                    x = 200,
                                    y = 299
                                ),
                                ShapesLayer.Shape.Point(
                                    x = 200,
                                    y = 299
                                ),
                            )
                        )
                    )
                ),
                ShapesLayer(
                    shapes = listOf(
                        ShapesLayer.Shape(
                            points = listOf(
                                ShapesLayer.Shape.Point(
                                    x = 100,
                                    y = 300
                                ),
                                ShapesLayer.Shape.Point(
                                    x = 200,
                                    y = 100
                                ),
                                ShapesLayer.Shape.Point(
                                    x = 300,
                                    y = 300
                                ),
                            )
                        )
                    )
                ),
                ShapesLayer(
                    shapes = listOf(
                        ShapesLayer.Shape(
                            points = listOf(
                                ShapesLayer.Shape.Point(
                                    x = 656,
                                    y = 155
                                ),
                                ShapesLayer.Shape.Point(
                                    x = 529,
                                    y = 379
                                ),
                                ShapesLayer.Shape.Point(
                                    x = 400,
                                    y = 154
                                ),
                            )
                        )
                    )
                ),
                ShapesLayer(
                    shapes = listOf(
                        ShapesLayer.Shape(
                            points = listOf(
                                ShapesLayer.Shape.Point(
                                    x = 400,
                                    y = 304
                                ),
                                ShapesLayer.Shape.Point(
                                    x = 528,
                                    y = 80
                                ),
                                ShapesLayer.Shape.Point(
                                    x = 658,
                                    y = 304
                                ),
                            )
                        )
                    )
                ),
            )
        )
    }
}