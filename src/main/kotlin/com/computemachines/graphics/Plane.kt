package com.computemachines.graphics

import org.joml.AxisAngle4f
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays
import org.lwjgl.system.MemoryStack.stackPush

/*
 * Total number of vertices == count.x*count.y
 * count must be >= (2,2)
 *
 * */
class Plane(homogeneous: Vector4f,
            count: Pair<Int, Int> = Pair(2, 2)) : Mesh {
    private val step = Pair(1 / count.first.toFloat(), 1 / count.second.toFloat())
    val vertices = Array(count.first, { i ->
        Array(count.second, { j ->
            Pair(step.first * i, step.second * j)
        })
    })
    val indices = mutableListOf<Pair<Int, Int>>()

    init {
        for (i in 0..(count.first - 2)) {
            for (j in 0..(count.second - 1)) {
                indices.add(Pair(i, j))
                indices.add(Pair(i + 1, j))
            }
        }
    }

    private val flatVertices = vertices.flatten().flatMap { listOf(it.first, it.second) }.toFloatArray()
    private val flatIndices = IntArray(indices.size, { it: Int ->
        count.second * indices[it].first + indices[it].second
    })

    init {
        println(flatVertices)
    }

    var model = CoordSystem(Vector3f(), AxisAngle4f(0f, 0f, 1f, 0f))



    val vaoId: Int by lazy { glGenVertexArrays() }
    val vertex_vboId: Int by lazy { glGenBuffers() }
    val index_vboId: Int by lazy { glGenBuffers() }


    fun setup_Array_VAO() {
        stackPush().use {
            val vertex_buffer = it.callocFloat(flatVertices.size)
            vertex_buffer.put(flatVertices)
            vertex_buffer.flip()
            glBindBuffer(GL_ARRAY_BUFFER, vertex_vboId)
            glBufferData(GL_ARRAY_BUFFER, vertex_buffer, GL_STATIC_DRAW)
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)

            val index_buffer = it.callocInt(flatIndices.size)
            index_buffer.put(flatIndices)
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
        glDrawElements(GL_TRIANGLE_STRIP, flatIndices.size, GL_UNSIGNED_INT, 0)
    }
}