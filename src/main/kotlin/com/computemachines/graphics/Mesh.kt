package com.computemachines.graphics

interface Mesh {
    fun bind()
    fun draw(camera: Camera, pvm_uniform: Int)
}