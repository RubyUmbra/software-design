package draw

import javafx.scene.Group
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext

class JavaFXDrawingApi(
    stage: Stage,
    override val drawingAreaWidth: Long,
    override val drawingAreaHeight: Long,
) : DrawingApi {
    private val graphics: GraphicsContext

    init {
        val canvas = Canvas(drawingAreaWidth.toDouble(), drawingAreaHeight.toDouble())
        graphics = canvas.graphicsContext2D
        stage.scene = Scene(Group().apply { children.add(canvas) })
        stage.show()
    }

    override fun drawCircle(circle: Circle) {
        val x: Double = circle.c.x.toDouble()
        val y: Double = circle.c.y.toDouble()

        val r: Double = circle.r.toDouble()

        graphics.fillOval(x - r, y - r, 2 * r, 2 * r)
    }

    override fun drawLine(line: Line) {
        val ax: Double = line.a.x.toDouble()
        val ay: Double = line.a.y.toDouble()

        val bx: Double = line.b.x.toDouble()
        val by: Double = line.b.y.toDouble()

        graphics.strokeLine(ax, ay, bx, by)
    }
}
