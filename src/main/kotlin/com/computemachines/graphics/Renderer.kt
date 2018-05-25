package com.computemachines.graphics

interface Renderer {
    fun init()
    fun initCallbacks(window: RenderWindow)
    fun draw()
    fun terminate()
}