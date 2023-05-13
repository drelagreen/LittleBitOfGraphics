import Geometry.Vector2i
import java.awt.image.BufferedImage
import java.util.*
import kotlin.math.*

object Drawer {
    fun fillRec(
        image: BufferedImage,
        start: Vector2i,
        bounds: Vector2i,
        fillRGB: Int,
        stats: Stats,
    ) {
        stats.startTimer()

        fillRec(
            image = image,
            currentPixelVector = start,
            boundsVector = bounds,
            rgbFill = fillRGB,
            rgbOld = image.getRGB(start.x, start.y),
            stats = stats
        )

        stats.stopTimer()
    }

    private fun fillRec(
        image: BufferedImage,
        currentPixelVector: Vector2i,
        boundsVector: Vector2i,
        rgbFill: Int,
        rgbOld: Int,
        stats: Stats
    ) {
        if (currentPixelVector.x >= 0 && currentPixelVector.x < boundsVector.x && currentPixelVector.y >= 0 && currentPixelVector.y < boundsVector.y) {
            image.getRGB(currentPixelVector.x, currentPixelVector.y)
            val rgb = image.getRGB(currentPixelVector.x, currentPixelVector.y)
            if (rgb == rgbFill || rgbOld != rgb) return

            stats.pInc()
            image.setRGB(currentPixelVector.x, currentPixelVector.y, rgbFill)
            listOf(
                -1 to -1,
                -1 to 0,
                -1 to 1,
                0 to -1,
                0 to 1,
                1 to -1,
                1 to 0,
                1 to 1
            ).forEach {
                fillRec(
                    image,
                    Vector2i(currentPixelVector.x + it.first, currentPixelVector.y + it.second),
                    boundsVector,
                    rgbFill,
                    rgbOld,
                    stats
                )
            }
        }
    }

    fun fillStack(
        image: BufferedImage,
        start: Vector2i,
        bounds: Vector2i,
        fillRGB: Int,
        stats: Stats,
    ) {
        stats.startTimer()

        val pixels: Stack<Vector2i> = Stack()

        pixels.push(start)
        val oldRGB = image.getRGB(start.x, start.y)

        while (!pixels.empty()) {
            val pixel = pixels.pop()
            if (pixel.x >= 0 && pixel.x < bounds.x && pixel.y >= 0 && pixel.y < bounds.y) {
                val rgb = image.getRGB(pixel.x, pixel.y)

                if (rgb != fillRGB && rgb == oldRGB) {
                    stats.pInc()
                    image.setRGB(pixel.x, pixel.y, fillRGB)
                    listOf(
                        -1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1
                    ).forEach {
                        pixels.push(Vector2i(pixel.x + it.first, pixel.y + it.second))
                    }
                }
            }
        }

        stats.stopTimer()
    }

    private fun setPixels(
        image: BufferedImage,
        center: Vector2i,
        x: Int,
        y: Int,
        color: Int,
        alpha: Float,
        beta: Float,
        angle: Float,
        stats: Stats
    ) {
        if (alpha <= beta) {
            if (angle > alpha && angle < beta) image.setRGB(center.x + x, center.y + y, color).apply { stats.pInc() }
            if (45 * 2 - angle > alpha && 45 * 2 - angle < beta) image.setRGB(center.x + y, center.y + x, color)
                .apply { stats.pInc() }
            if (angle + 45 * 2 > alpha && angle + 45 * 2 < beta) image.setRGB(center.x - y, center.y + x, color)
                .apply { stats.pInc() }
            if (45 * 4 - angle > alpha && 45 * 4 - angle < beta) image.setRGB(center.x - x, center.y + y, color)
                .apply { stats.pInc() }
            if (angle + 45 * 4 > alpha && angle + 45 * 4 < beta) image.setRGB(center.x - x, center.y - y, color)
                .apply { stats.pInc() }
            if (45 * 6 - angle > alpha && 45 * 6 - angle < beta) image.setRGB(center.x - y, center.y - x, color)
                .apply { stats.pInc() }
            if (angle + 45 * 6 > alpha && angle + 45 * 6 < beta) image.setRGB(center.x + y, center.y - x, color)
                .apply { stats.pInc() }
            if (45 * 8 - angle > alpha && 45 * 8 - angle < beta) image.setRGB(center.x + x, center.y - y, color)
                .apply { stats.pInc() }
        } else {
            if ((angle in alpha..360.0f) || (angle in 0.0f..beta)) image.setRGB(center.x + x, center.y + y, color)
                .apply { stats.pInc() }
            if ((45 * 2 - angle in alpha..360.0f) || (45 * 2 - angle in 0.0f..beta)) image.setRGB(
                center.x + y, center.y + x, color
            ).apply { stats.pInc() }
            if ((angle + 45 * 2 in alpha..360.0f) || (angle + 45 * 2 in 0.0f..beta)) image.setRGB(
                center.x - y, center.y + x, color
            ).apply { stats.pInc() }
            if ((45 * 4 - angle in alpha..360.0f) || (45 * 4 - angle in 0.0f..beta)) image.setRGB(
                center.x - x, center.y + y, color
            ).apply { stats.pInc() }
            if ((angle + 45 * 4 in alpha..360.0f) || (angle + 45 * 4 in 0.0f..beta)) image.setRGB(
                center.x - x, center.y - y, color
            ).apply { stats.pInc() }
            if ((45 * 6 - angle in alpha..360.0f) || (45 * 6 - angle in 0.0f..beta)) image.setRGB(
                center.x - y, center.y - x, color
            ).apply { stats.pInc() }
            if ((angle + 45 * 6 in alpha..360.0f) || (angle + 45 * 6 in 0.0f..beta)) image.setRGB(
                center.x + y, center.y - x, color
            ).apply { stats.pInc() }
            if ((45 * 8 - angle in alpha..360.0f) || (45 * 8 - angle in 0.0f..beta)) image.setRGB(
                center.x + x, center.y - y, color
            ).apply { stats.pInc() }
        }
    }

    fun arcBresenham(
        image: BufferedImage, center: Vector2i, radius: Int, color: Int, alpha: Float, beta: Float, stats: Stats
    ) {
        stats.startTimer()

        var x = radius
        var y = 0
        var delta = 3 - 2 * radius

        while (x >= y) {
            setPixels(
                image,
                center,
                x,
                y,
                color,
                alpha,
                beta,
                (acos(x / sqrt(x.toFloat().pow(2) + y.toFloat().pow(2))) * 180.0 / PI).toFloat(),
                stats
            )

            setPixels(
                image,
                center,
                x - 1,
                y,
                color,
                alpha,
                beta,
                (acos(x / sqrt(x.toFloat().pow(2) + y.toFloat().pow(2))) * 180.0 / PI).toFloat(),
                stats
            )
            delta += if (delta < 0) 4 * y++ + 6;
            else 4 * (y++ - x--) + 10;
        }

        stats.stopTimer()
    }

    fun arcPolar(
        image: BufferedImage, center: Vector2i, radius: Int, color: Int, alpha: Float, beta: Float, stats: Stats
    ) {
        stats.startTimer()

        var x = radius
        var y = 0
        var angle = 0f
        val step = 1f / radius
        while (x > y) {
            angle += step
            x = (radius * cos(angle)).toInt()
            y = (radius * sin(angle)).toInt()
            setPixels(image, center, x, y, color, alpha, beta, angle * 180 / PI.toFloat(), stats)
            setPixels(image, center, x - 1, y, color, alpha, beta, angle * 180 / PI.toFloat(), stats)
        }

        stats.stopTimer()
    }
}
