data class Stats(
    var pixels: Int = 0,
    var timeNs: Long = 0L,
) {
    val timeMs: Double get() = timeNs.toDouble() / 1000000

    private var startNs = 0L
    fun stopTimer() {
        timeNs += System.nanoTime() - startNs
    }

    fun startTimer() {
        startNs = System.nanoTime()
    }

    fun pInc() {
        pixels += 1
    }

    fun pInc(value: Int) {
        pixels += value
    }
}