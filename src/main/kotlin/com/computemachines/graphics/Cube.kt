package com.computemachines.graphics

import org.joml.AxisAngle4f
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays
import org.lwjgl.system.MemoryStack.stackPush


class Cube(position: Vector3f) : Mesh {
    val vertices = floatArrayOf(
            0f, 0f, 0f,    //0
            1f, -1f, -1f,  //1
            -1f, -1f, -1f, //2
            1f, 1f, -1f,   //3
            -1f, 1f, -1f,  //4
            1f, -1f, 1f,   //5
            -1f, -1f, 1f,  //6
            -1f, 1f, 1f,   //7
            1f, 1f, 1f     //8
    )

    var model = CoordSystem(position, AxisAngle4f(0f, 0f, 1f, 0f))

    val indices = intArrayOf(4, 3, 7, 8, 5, 3, 1, 4, 2, 7, 6, 5, 2, 1)
//    val indices = intArrayOf(1, 2, 5, 6, 7, 2, 4, 1, 3, 5, 8, 7, 3, 4)

    val vaoId: Int by lazy { glGenVertexArrays() }
    val vertex_vboId: Int by lazy { glGenBuffers() }
    val index_vboId: Int by lazy { glGenBuffers() }


    fun setup_Array_VAO() {
        stackPush().use {
            val vertex_buffer = it.callocFloat(vertices.size)
            vertex_buffer.put(vertices)
            vertex_buffer.flip()
            glBindBuffer(GL_ARRAY_BUFFER, vertex_vboId)
            glBufferData(GL_ARRAY_BUFFER, vertex_buffer, GL_STATIC_DRAW)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

            val index_buffer = it.callocInt(indices.size)
            index_buffer.put(indices)
            index_buffer.flip()
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, index_vboId)
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, index_buffer, GL_STATIC_DRAW)
        }
    }

    init {
        glBindVertexArray(vaoId);
        setup_Array_VAO()
        glBindVertexArray(0)
    }

    override fun bind() {

        glBindVertexArray(vaoId)
        glEnableVertexAttribArray(0)
    }

    override fun draw(camera: Camera, pvm_uniform: Int) {

        stackPush().use {
            val fb = it.callocFloat(16)
            val pvm_matrix = Matrix4f()
            camera.pv_matrix.mul(model.matrix, pvm_matrix)
            pvm_matrix.get(fb)
            glUniformMatrix4fv(pvm_uniform, false, fb)
        }

//        model.rotation.angle += 0.01f

        bind()
        glDrawElements(GL_TRIANGLE_STRIP, 14, GL_UNSIGNED_INT, 0)
    }
}