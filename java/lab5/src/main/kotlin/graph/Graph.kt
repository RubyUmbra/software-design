package graph

import draw.Circle
import draw.DrawingApi
import draw.Line
import draw.Point

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

abstract class Graph(
    private val verticesNum: Int,
    private val drawingApi: DrawingApi,
) {
    private val center: Point =
        Point(drawingApi.drawingAreaWidth / 2, drawingApi.drawingAreaHeight / 2)
    private val radius: Long = drawingApi.drawingAreaWidth / 3
    private val pointSize: Long = drawingApi.drawingAreaHeight / 100

    abstract fun drawGraph()

    private fun vertexToPoint(i: Int): Point {
        val a: Double = 2 * PI * i / verticesNum
        return Point(center.x + radius * cos(a), center.y + radius * sin(a))
    }

    protected fun drawVertex(i: Int): Unit =
        drawingApi.drawCircle(Circle(vertexToPoint(i), pointSize))

    protected fun drawEdge(a: Int, b: Int): Unit =
        drawingApi.drawLine(Line(vertexToPoint(a), vertexToPoint(b)))

    protected fun drawVertices(): Unit = repeat(verticesNum, ::drawVertex)
}
