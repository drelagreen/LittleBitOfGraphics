import org.jsfml.graphics.Color
import org.jsfml.graphics.RectangleShape
import org.jsfml.graphics.RenderTarget
import org.jsfml.system.Vector2f
import org.jsfml.system.Vector2i

data class LayerMergeResult(
    val background: RectangleShape = RectangleShape(),
    val shapes: List<Shape> = emptyList(),
    val maskedShapes: List<Shape> = emptyList()
)

interface Layer {
    fun merge(previous: LayerMergeResult, showMasked: Boolean): LayerMergeResult
}

class BackgroundLayer(val backgroundColor: Color) : Layer {
    override fun merge(previous: LayerMergeResult, showMasked: Boolean): LayerMergeResult {
        previous.background.fillColor = backgroundColor
        return previous
    }
}

class ShapesLayer(val shapes: List<Shape>) : Layer {
    override fun merge(previous: LayerMergeResult, showMasked: Boolean): LayerMergeResult {
        val newOutShapes = ArrayList<Shape>()
        val masked = ArrayList<Shape>()

        shapes.forEach { upperShape ->
            previous.shapes.forEach { previousShape ->
                val algorithm = WeilerAthertonAlgorithm(previousShape, upperShape)
                val p = algorithm.clipping(showMasked)
                newOutShapes.addAll(p.first)
                masked.addAll(p.second)
            }
            newOutShapes.add(upperShape)
        }

        return previous.copy(
            shapes = newOutShapes,
            maskedShapes = if (!showMasked) previous.maskedShapes else previous.maskedShapes + masked
        )
    }
}

class WindowMaskLayer(val width: Int, val height: Int) : Layer {
    lateinit var position: Vector2i
    override fun merge(previous: LayerMergeResult, showMasked: Boolean): LayerMergeResult {
        val maskedOut = ArrayList<Shape>()
        val inner = ArrayList<Shape>()

        val mask = Shape(
            listOf(
                Vector2d(position.x - width / 2.0, position.y - height / 2.0),
                Vector2d(position.x + width / 2.0, position.y - height / 2.0),
                Vector2d(position.x + width / 2.0, position.y + height / 2.0),
                Vector2d(position.x - width / 2.0, position.y + height / 2.0),
            )
        )

        previous.shapes.forEach { previousShape ->
            val algorithm = WeilerAthertonAlgorithm(previousShape, mask)
            val p = algorithm.clipping(true)

            maskedOut.addAll(p.first)
            inner.addAll(p.second)
        }

        inner.add(mask)

        val result = previous.copy(
            shapes = inner,
            maskedShapes = if (!showMasked) previous.maskedShapes else previous.maskedShapes + maskedOut,
        )

        result.background.setPosition(position.x - width / 2.0f, position.y - height / 2.0f)
        result.background.size = Vector2f(width.toFloat(), height.toFloat())

        return result
    }
}

class Container {
    val layers = ArrayList<Layer>()

    fun addLayer(layer: Layer) {
        layers.add(layer)
    }

    fun draw(render: RenderTarget, showMasked: Boolean) {
        var result = LayerMergeResult()

        layers.forEach { layer ->
            result = layer.merge(result, showMasked)
        }

        render.draw(result.background)

        if (showMasked) {
            result.maskedShapes.forEach { shape ->
                shape.draw(render, Color(0x50, 0x50, 0x50), LineStyle.Dashed)
            }
        }

        result.shapes.forEach { shape ->
            shape.draw(render, Color.RED)
        }
    }
}

