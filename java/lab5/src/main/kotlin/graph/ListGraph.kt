package graph

import draw.DrawingApi

class ListGraph(
    vertexCount: Int,
    private val edges: List<Edge>,
    drawingApi: DrawingApi,
) : Graph(vertexCount, drawingApi) {
    data class Edge(val i: Int, val j: Int)

    override fun drawGraph() {
        drawVertices()
        edges.forEach { (i: Int, j: Int) -> drawEdge(i, j) }
    }

    companion object {
        fun stringToListGraph(str: String): (DrawingApi) -> ListGraph {
            var n = 0
            val edges: List<Edge> = str.lines().map {
                val (i, j) = it.split(" ").map(String::toInt)
                n = maxOf(n, i)
                n = maxOf(n, j)
                Edge(i, j)
            }
            return { api: DrawingApi -> ListGraph(n, edges, api) }
        }
    }
}
