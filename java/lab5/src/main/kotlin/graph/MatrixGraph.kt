package graph

import draw.DrawingApi

class MatrixGraph(
    private val matrix: List<List<Boolean>>,
    drawingApi: DrawingApi,
) : Graph(matrix.size, drawingApi) {
    override fun drawGraph() {
        drawVertices()
        matrix.forEachIndexed { i: Int, row: List<Boolean> ->
            row.forEachIndexed { j: Int, e: Boolean -> if (e) drawEdge(i, j) }
        }
    }

    companion object {
        fun stringToMatrixGraph(str: String): (DrawingApi) -> MatrixGraph {
            val edges: List<List<Boolean>> = str.lines()
                .map { it.split(" ").map { e -> e.trim() == "1" } }
            return { api: DrawingApi -> MatrixGraph(edges, api) }
        }
    }
}
