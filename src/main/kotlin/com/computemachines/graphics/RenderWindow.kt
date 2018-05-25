package com.computemachines.graphics

import org.lwjgl.glfw.*
import org.lwjgl.opengl.*

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack.*
import org.lwjgl.system.MemoryUtil.NULL as LWJGL_NULL


typealias Window = Long


// RenderWindow handles glfw to create window.
// All opengl state management is done in renderer.
// RenderWindow has only one renderer.
class RenderWindow(val renderer: Renderer) {
    companion object {
        fun getGLFWWindowSize(window: Long) = stackPush().use {
            val pWidth = it.callocInt(1)
            var pHeight = it.callocInt(1)
            glfwGetWindowSize(window, pWidth, pHeight)
            Pair(pWidth.get(0), pHeight.get(0))
        }
    }

    fun screenCoords2NDC(x: Double, y: Double): Pair<Float, Float> {
        val (width, height) = windowSize
        return (2*x.toFloat()/width - 1) to (1 - 2*y.toFloat()/height)
    }

    private val initialWindowSize = Pair(300, 300)
    val glfwWindow: Long by lazy {
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
        var window: Long? = glfwCreateWindow(initialWindowSize.first,
                initialWindowSize.second, "Hello World!", LWJGL_NULL, LWJGL_NULL)
        if ( window == LWJGL_NULL ) window = null
        window ?: throw RuntimeException("Failed to create GLFW window")
    }

    val windowSize get() = getGLFWWindowSize(glfwWindow)

    fun centerWindow() {
        val (width, height) = windowSize
        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        glfwSetWindowPos(glfwWindow, (vidmode!!.width() - width)/2,
                (vidmode.height() - height)/2)
    }

    init {
        initGLFW()
        GL.createCapabilities()
    }

    fun initGLFW() {
        GLFWErrorCallback.createPrint(System.err).set()
        if ( !glfwInit() )
            throw IllegalStateException("Unable to initialize GLFW")
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        centerWindow()

        glfwMakeContextCurrent(glfwWindow)
        glfwSwapInterval(1)

        glfwShowWindow(glfwWindow)

        println("Finished initGLFW")
    }


    fun run() {
        renderer.init()
        renderer.initCallbacks(this)

        while ( !glfwWindowShouldClose(glfwWindow) ) {

            renderer.draw()

            glfwSwapBuffers(glfwWindow)
            glfwPollEvents()
        }

        renderer.terminate()
        glfwTerminate()
    }
}