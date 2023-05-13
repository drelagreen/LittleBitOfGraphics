import java.io.File

object FileManager {
    fun loadImage(path: String): Image {
        val imageFactory = ImageFactory()
        return File(path).readBytes().let { imageFactory.asImage(it) }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun Image.saveImage(path: String) {
        val byteArray = this.generateBytes().toUByteArray().asByteArray()
        File(path).writeBytes(byteArray)
    }
}
