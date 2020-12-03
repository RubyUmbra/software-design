package main

import draw.JavaFXDrawingApi

import javafx.application.Application
import javafx.stage.Stage

class JavaFxApplication : Application() {
    override fun start(stage: Stage) =
        Configuration.graphFromApi(
            JavaFXDrawingApi(stage, Configuration.WIDTH, Configuration.HEIGHT)
        ).drawGraph()

    fun run(): Unit = launch()
}
