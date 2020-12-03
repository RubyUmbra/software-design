package draw

interface DrawingApi {
    val drawingAreaWidth: Long
    val drawingAreaHeight: Long

    fun drawCircle(circle: Circle)
    fun drawLine(line: Line)
}
