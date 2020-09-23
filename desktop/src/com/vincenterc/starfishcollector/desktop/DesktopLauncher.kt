package com.vincenterc.starfishcollector.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.vincenterc.starfishcollector.StarfishGame

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            title = "Starfish Collector"
            width = 800
            height = 600
        }
        LwjglApplication(StarfishGame(), config)
    }
}