class Image(
    val height: Int = 255,
    val width: Int = 0,
    val pal: Array<Long> = arrayOf(
        0xff000000, 0xc0000000, 0x80000000, 0x40000000,
        0xffff0000, 0xc0ff0000, 0x80ff0000, 0x40ff0000,
        0xff00ff00, 0xc000ff00, 0x8000ff00, 0x4000ff00,
        0xff0000ff, 0xc00000ff, 0x800000ff, 0x400000ff
    ),
    val data: Array<Pixel> = emptyArray()
) {
    private val palSize: Int = 16
    private val bitPerPix: Int = 4

    fun generateBytes(): Array<UByte> {
        val pack: Array<UByte> = Array(2 + 2 + 1 + 2 + palSize * 4 + (height * width) / 2) { UByte.MIN_VALUE }
        /*HEADER START*/
        val weightBytes = intishToBytes(width)
        pack[0] = weightBytes.first
        pack[1] = weightBytes.second

        val heightBytes = intishToBytes(height)
        pack[2] = heightBytes.first
        pack[3] = heightBytes.second

        pack[4] = intishToBytes(bitPerPix).second

        val palSizeBytes = intishToBytes(palSize)
        pack[5] = palSizeBytes.first
        pack[6] = palSizeBytes.second
        /*HEADER END*/
        /*PAL START*/
        pal.flatMap {
            longishToBytes(it).asList()
        }.forEachIndexed { index, uByte ->
            pack[7 + index] = uByte
        }
        /*PAL END*/
        /*IMG START*/
        var temp = 0
        data.forEachIndexed { index, pixel ->
            val x = pixel.palX
            val y = pixel.palY
            val byte = x.shl(2) + y

            if (index % 2 == 0) {
                temp = byte
            } else {
                val byte2 = temp.shl(4) + byte
                pack[7 + palSize * 4 + index / 2] = byte2.toUByte()
            }
        }
        /*IMG END*/

        return pack
    }

    private fun intishToBytes(value: Int): Pair<UByte, UByte> {
        return Pair(
            ((value ushr 8) and 0xff).toUByte(),
            (value and 0xff).toUByte()
        )
    }

    private fun longishToBytes(value: Long): Array<UByte> {
        val array: Array<UByte> = Array(4) { UByte.MIN_VALUE }
        array[3] = ((value ushr 24) and 0xff).toUByte()
        array[2] = ((value ushr 16) and 0xff).toUByte()
        array[1] = ((value ushr 8) and 0xff).toUByte()
        array[0] = (value and 0xff).toUByte()
        return array
    }
}