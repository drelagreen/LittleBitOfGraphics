class ImageFactory {
    @OptIn(ExperimentalUnsignedTypes::class)
    fun asImage(bytes: ByteArray): Image {
        val uBytes = bytes.asUByteArray().toTypedArray()

        val weight = twoBytesToInt(
            arrayOf(
                uBytes[0],
                uBytes[1]
            )
        )

        val height = twoBytesToInt(
            arrayOf(
                uBytes[2],
                uBytes[3]
            )
        )

        val palSize = twoBytesToInt(
            arrayOf(
                uBytes[5],
                uBytes[6]
            )
        )

        //pal
        for (i: Int in 0 until palSize step 4) {
            fourBytesToLong(
                arrayOf(
                    uBytes[7 + i],
                    uBytes[7 + i + 1],
                    uBytes[7 + i + 2],
                    uBytes[7 + i + 3]
                )
            )
        }

        //img
        var pixelX = 0
        var pixelY = 0
        val pixels = ArrayList<Pixel>(weight * height)
        for (i: Int in 0 until (weight * height) / 2) {
            val byte = uBytes[7 + palSize * 4 + i].toInt()
            val a = byte.shr(4).and(0xf)
            val b = byte.and(0xf)
            /**
             *
             * aa aa bb bb
             * x  y  x  y
             * */
            val ax = a.shr(2).and(0b11)
            val ay = a.and(0b11)

            val bx = b.shr(2).and(0b11)
            val by = b.and(0b11)

            val p1 = Pixel(pixelX, pixelY, ax, ay)

            pixelX++
            if (pixelX > weight - 1) {
                pixelX = 0
                pixelY++
            }

            val p2 = Pixel(pixelX, pixelY, bx, by)

            pixelX++
            if (pixelX > weight - 1) {
                pixelX = 0
                pixelY++
            }

            pixels.add(p1)
            pixels.add(p2)
        }

        return Image(height, weight, data = pixels.toTypedArray())
    }

    private fun twoBytesToInt(value: Array<UByte>): Int {
        return (value[0].toInt().shl(8) + value[1].toInt())
    }

    private fun fourBytesToLong(value: Array<UByte>): Long {
        return (value[3].toLong().shl(24) +
                value[2].toLong().shl(16) +
                value[1].toLong().shl(8) +
                value[0].toLong())
    }
}