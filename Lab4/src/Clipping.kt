import kotlin.math.sqrt

fun Vector2d.length(): Double {
    return sqrt(this.x * this.x + this.y * this.y)
}

class WeilerAthertonAlgorithm(
    val subject: Shape,
    val mask: Shape
) {
    var subjectPoints: ArrayList<Vector2d> = ArrayList()
    var maskPoints: ArrayList<Vector2d> = ArrayList()
    val inPoints = HashSet<Vector2d>()
    val outPoints = HashSet<Vector2d>()

    fun clipping(returnMasked: Boolean): Pair<List<Shape>, List<Shape>> {
        if (mask.contains(subject)) {
            return if (returnMasked) Pair(emptyList(), listOf(subject)) else Pair(emptyList(), emptyList())
        } else {
            subjectPoints = ArrayList(subject.points)
            maskPoints = ArrayList(mask.points)

            calculateAndClassifyIntersections()

            return if (hasIntersections()) {
                val externalShapes = getExternalShapes()

                if (returnMasked) {
                    val innerShapes = getInnerShapes()
                    Pair(ArrayList(externalShapes), ArrayList(innerShapes))
                } else {
                    Pair(ArrayList(externalShapes), emptyList())
                }
            } else {
                Pair(listOf(subject), emptyList())
            }
        }
    }

    fun hasIntersections(): Boolean {
       return  !(inPoints.isEmpty() && outPoints.isEmpty())
    }

    fun calculateAndClassifyIntersections() {
        subject.edges.forEach { line ->
            mask.edges.forEach { edge ->
                val intersection = line.getIntersection(edge)

                if (intersection != null) {
                    if (intersection.intersectType == IntersectType.In) {
                        inPoints.add(intersection.point)
                    } else {
                        outPoints.add(intersection.point)
                    }

                    insertOnLine(intersection.point, line, subjectPoints)
                    insertOnLine(intersection.point, edge, maskPoints)
                }
            }
        }
    }

    fun insertOnLine(point: Vector2d, line: Line, list: ArrayList<Vector2d>) {
        if (list.contains(point)) return

        val begin = list.indexOf(line.start)
        val end = list.indexOf(line.end)

        val distanceFromStart = (point - line.start).length()
        var i = list.indexOf(line.start) + 1

        while (i != end && i != list.size) {
            if ((list[i] - list[begin]).length() >= distanceFromStart) {
                break
            } else {
                i++
            }
        }

        list.add(i, point)
    }

    fun getExternalShapes(): List<Shape> {
        val used = HashSet<Vector2d>()
        val mergedShapes = ArrayList<Shape>()

        outPoints.forEach { point ->
            if (!used.contains(point)) {
                val startPoint = point

                val merged = ArrayList<Vector2d>()

                var total = subjectPoints.indexOf(startPoint)

                do {
                    do {
                        used.add(subjectPoints[total])
                        merged.add(subjectPoints[total])

                        total++
                        if (total == subjectPoints.size) {
                            total = 0
                        }
                    } while (!inPoints.contains(subjectPoints[total]) && !outPoints.contains(subjectPoints[total]))

                    used.add(subjectPoints[total])

                    if (subjectPoints[total] == startPoint) break

                    var reversedTotal = maskPoints.lastIndexOf(subjectPoints[total])

                    do {
                        used.add(maskPoints[reversedTotal])
                        merged.add(maskPoints[reversedTotal])

                        reversedTotal--

                        if (reversedTotal == -1) {
                            reversedTotal = maskPoints.size - 1
                        }
                    } while (
                        !inPoints.contains(maskPoints[reversedTotal]) && !outPoints.contains(maskPoints[reversedTotal])
                    )

                    total = subjectPoints.indexOf(maskPoints[reversedTotal])
                } while (subjectPoints[total] != startPoint)

                mergedShapes.add(Shape(ArrayList(merged)))
            }
        }

        return mergedShapes
    }

    fun getInnerShapes(): List<Shape> {
        val used = HashSet<Vector2d>()
        val mergedShapes = ArrayList<Shape>()

        inPoints.forEach { point ->
            if (!used.contains(point)) {
                val startPoint = point

                val masked = ArrayList<Vector2d>()

                var total = subjectPoints.indexOf(startPoint)

                do {
                    used.add(subjectPoints[total])
                    do {
                        used.add(subjectPoints[total])
                        masked.add(subjectPoints[total])

                        total++
                        if (total == subjectPoints.size) {
                            total = 0
                        }
                    } while (!inPoints.contains(subjectPoints[total]) && !outPoints.contains(subjectPoints[total]))

                    used.add(subjectPoints[total])

                    if (subjectPoints[total] == startPoint) break

                    total = maskPoints.indexOf(subjectPoints[total])

                    do {
                        used.add(maskPoints[total])
                        masked.add(maskPoints[total])

                        total++

                        if (total == maskPoints.size) {
                            total = 0
                        }
                    } while (
                        !inPoints.contains(maskPoints[total]) && !outPoints.contains(maskPoints[total])
                    )

                    total = subjectPoints.indexOf(maskPoints[total])
                } while (subjectPoints[total] != startPoint)

                mergedShapes.add(Shape(ArrayList(masked)))
            }
        }

        return mergedShapes
    }
}