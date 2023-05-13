import FileManager.saveImage
import kotlin.random.Random

const val H = 300
const val W = 300

fun main() {
    val image = generateImage()
    image.saveImage("/Users/dzhalnin/LittleBitOfGraphics/LittleBitOfGraphics/test2.mop")
    val image2 = FileManager.loadImage("/Users/dzhalnin/LittleBitOfGraphics/LittleBitOfGraphics/test2.mop")
    Window(image2)
}

fun generateImage(): Image {
    val arrayList = ArrayList<Pixel>()

    var randomX = Random.nextInt(0, 4)
    var randomY = Random.nextInt(0, 4)
    var randomR = 0

    for (y: Int in 0 until H) {
        for (x: Int in 0 until W) {
            Pixel(x = x, y = y, palX = randomX, palY = randomY).let { arrayList.add(it) }

            randomR = Random.nextInt(0, 300)
            if (randomR == 1){
                randomX = Random.nextInt(0, 4)
                randomY = Random.nextInt(0, 4)
            }
        }
    }

    return Image(
        height = H,
        width = W,
        data = arrayList.toTypedArray()
    )
}