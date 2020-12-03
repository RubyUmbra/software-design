package main

import draw.AwtDrawingApi

import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

import kotlin.system.exitProcess

class AwtApplication : Frame() {
    override fun paint(g: Graphics): Unit =
        Configuration.graphFromApi(
            AwtDrawingApi(g as Graphics2D, Configuration.WIDTH, Configuration.HEIGHT)
        ).drawGraph()

    fun run() {
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(we: WindowEvent) {
                exitProcess(0)
            }
        })
        setSize(Configuration.WIDTH.toInt(), Configuration.HEIGHT.toInt())
        isVisible = true
    }
}
