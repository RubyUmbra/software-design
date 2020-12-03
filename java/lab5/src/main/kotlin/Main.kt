import draw.DrawingApi
import graph.Graph
import graph.ListGraph
import graph.MatrixGraph
import main.AwtApplication
import main.JavaFxApplication

object Configuration {
    lateinit var graphFromApi: (DrawingApi) -> Graph
    var WIDTH: Long = 600
    var HEIGHT: Long = 600
}

fun main(args: Array<String>) {
    require(args.size == 3)

    Configuration.graphFromApi = when (args[0]) {
        "list" -> ListGraph::stringToListGraph
        "matrix" -> MatrixGraph::stringToMatrixGraph
        else -> throw IllegalArgumentException()
    }(java.io.File(args[2]).readText())

    when (args[1]) {
        "awt" -> AwtApplication().run()
        "javaFX" -> JavaFxApplication().run()
        else -> throw IllegalArgumentException()
    }
}
