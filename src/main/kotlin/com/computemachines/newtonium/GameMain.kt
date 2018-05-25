package com.computemachines.newtonium

import com.computemachines.graphics.RenderWindow
import com.computemachines.graphics.*
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback
import org.lwjgl.glfw.GLFWCursorPosCallbackI
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.glUseProgram
import org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.glClear
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*


fun main(args: Array<String>) {
    val renderWindow = RenderWindow(GameRenderer())
    renderWindow.run()
}



fun Vector4f.toVector3f() = Vector3f(x, y, z)


class GameRenderer: Renderer {
    //    val cubes: List<Cube> by lazy {
//        (-5..5).flatMap { i ->
//            (-5..5).map { j -> Cube(Vector3f(i * 3f, j * 3f, 0f)) }
//        }
//    }
    val cube: Cube by lazy { Cube(Vector3f(0f, 0f, 0f)) }
    val plane: Plane by lazy { Plane(Vector4f(), 2 to 2) }
    var pvm_matrix_uniform_loc: Int = 0
    val camera = Camera()
    val program: SimpleProgram by lazy { SimpleProgram() }

    override fun init() {

        glEnable(GL_DEPTH_TEST)
//        glEnable(GL_CULL_FACE)
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)

        camera.position(0f, 0f, 10f)

        pvm_matrix_uniform_loc = glGetUniformLocation(program.programId, "pvm_matrix")
        println("pvm_matrix_uniform_loc: $pvm_matrix_uniform_loc")

        glUseProgram(program.programId)

        println("OpenGL Version: ${glGetString(GL_VERSION)}")
        glClearColor(0f, 1f, 1f, 1f)
    }


    override fun initCallbacks(renderWindow: RenderWindow) {
        val cb = object: GLFWCursorPosCallbackI {
            override fun invoke(window: Long, xpos: Double, ypos: Double) {

                val p_inv = camera.perspectiveMatrix().invert()

                val (ndcX, ndcY) = renderWindow.screenCoords2NDC(xpos, ypos)
                val ndc = Vector4f(ndcX, ndcY, -1f, 1f)

                val viewCoordinates = p_inv.transform(ndc)
                viewCoordinates.div(viewCoordinates.w)

                viewCoordinates.mul(100f)
                viewCoordinates.w = 1f

                val worldCoordinates = camera.viewMatrix().invert().transform(viewCoordinates)
                plane.model.position.set(worldCoordinates.toVector3f())
            }
        }
        glfwSetCursorPosCallback(renderWindow.glfwWindow, cb)
    }

    var thisTime = 1.0
    var lastTime = 0.0
    fun calculateFramerate(lastTime: Double, thisTime: Double)
            = 1/(thisTime - lastTime)

    override fun draw() {
        thisTime = glfwGetTime()
//        println("framerate: ${calculateFramerate(lastTime, thisTime)}")
        lastTime = thisTime

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
//        for(cube in cubes) {
//            cube.draw(camera, program.pvm_uniform_loc)
//        }
        plane.draw(camera, program.pvm_uniform_loc)
//        camera.pv_matrix.rotateY(-0.001f)
    }

    override fun terminate() {


        glDisableVertexAttribArray(0)
        glBindVertexArray(0)

        if (glGetError() == GL_NO_ERROR) println("No Error")

    }
}