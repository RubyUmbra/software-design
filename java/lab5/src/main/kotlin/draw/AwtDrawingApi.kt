package draw

import java.awt.Graphics2D

class AwtDrawingApi(
    private val graphics: Graphics2D,
    override val drawingAreaWidth: Long,
    override val drawingAreaHeight: Long,
) : DrawingApi {
    override fun drawCircle(circle: Circle) {
        val x: Int = circle.c.x.toInt()
        val y: Int = circle.c.y.toInt()

        val r: Int = circle.r.toInt()

        graphics.fillOval(x - r, y - r, 2 * r, 2 * r)
    }

    override fun drawLine(line: Line) {
        val ax: Int = line.a.x.toInt()
        val ay: Int = line.a.y.toInt()

        val bx: Int = line.b.x.toInt()
        val by: Int = line.b.y.toInt()

        graphics.drawLine(ax, ay, bx, by)
    }
}
