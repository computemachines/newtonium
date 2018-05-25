package com.computemachines.graphics

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.GL_FLOAT
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL30.glGenVertexArrays
import org.lwjgl.system.MemoryStack.*

class Quad {
    val vertices = floatArrayOf(
        // Left bottom triangle
        -0.5f, 0.45f, 0f,
        -0.5f, -0.5f, 0f,
        0.5f, -0.5f, 0f,
        // Right top triangle
        0.5f, -0.5f, 0f,
        0.5f, 0.45f, 0f,
        -0.5f, 0.5f, 0f
    )


    val vaoId: Int by lazy { glGenVertexArrays() }
    val vboId: Int by lazy { glGenBuffers() }


    fun setupVAO() {
        glBindVertexArray(vaoId);
        setup_Array_VAO()
        glBindVertexArray(0)
    }

    fun setup_Array_VAO() {
        // fill vbo with vertices
        stackPush().use {
            val buffer = it.callocFloat(vertices.size)
            buffer.put(vertices)
            buffer.flip()
            glBindBuffer(GL_ARRAY_BUFFER, vboId)
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
        }
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    init {
        setupVAO()
    }
}