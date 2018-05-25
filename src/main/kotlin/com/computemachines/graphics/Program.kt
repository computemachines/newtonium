package com.computemachines.graphics

import org.lwjgl.opengl.GL20.*

open class Program(vertFilename: String, fragFilename: String){
    val programId: Int by lazy { glCreateProgram() }
    val vertexShader = VertexShader(vertFilename)
    val fragmentShader = FragmentShader(fragFilename)
    init {
        glAttachShader(programId, vertexShader.shaderId)
        glAttachShader(programId, fragmentShader.shaderId)
        glLinkProgram(programId)
        glValidateProgram(programId)
    }
}

class SimpleProgram: Program("simple.vert", "pass.frag") {
    val pvm_uniform_loc: Int by lazy { glGetUniformLocation(programId, "pvm_matrix") }
}