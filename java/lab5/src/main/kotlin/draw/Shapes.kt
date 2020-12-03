package draw

data class Point(val x: Long, val y: Long) {
    constructor(x: Double, y: Double) : this(x.toLong(), y.toLong())
}

data class Line(val a: Point, val b: Point)

data class Circle(val c: Point, val r: Long)
