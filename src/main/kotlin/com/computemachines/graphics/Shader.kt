package com.computemachines.graphics

import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL20.*
import java.io.BufferedReader
import java.io.File

abstract class Shader(type: Int, filename: String) {
    val shaderId: Int by lazy { glCreateShader(type) }
    init {
        glShaderSource(shaderId, Util.getSource(filename))
        glCompileShader(shaderId)
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            println("Compile of $filename failed.")
            println(glGetShaderInfoLog(shaderId))
        }
    }
}

class VertexShader(filename: String): Shader(GL_VERTEX_SHADER, filename)
class FragmentShader(filename: String): Shader(GL_FRAGMENT_SHADER, filename)

object Util {
    fun getSource(filename: String): String {
        return Util::class.java.classLoader.getResource(filename).readText()
    }
}