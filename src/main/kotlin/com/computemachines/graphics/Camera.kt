package com.computemachines.graphics

import org.joml.Matrix4f
import org.joml.Vector3f

class Camera {
    var _position: Vector3f = Vector3f(0f, 0f, 0f)
    fun position(x: Float, y: Float, z: Float) {
        _position = Vector3f(x, y, z)
        stale = true
    }
    var _lookAt = Vector3f(0f, 0f, 0f)
    fun lookat(x: Float, y: Float, z: Float) {
        _lookAt = Vector3f(x, y, z)
        stale = true
    }
    var _up = Vector3f(0f, 1f, 0f)
    fun up(x: Float, y: Float, z: Float) {
        _up = Vector3f(x, y, z)
        stale = true
    }
    var _near = 0.1f
    fun near(v: Float) {
        _near = v
        stale = true
    }
    var _far = 100f
    fun far(v: Float) {
        _far = v
        stale = true
    }
    var _fieldOfView = Math.toRadians(45.0).toFloat()
    fun fieldOfView(v: Float) {
        _fieldOfView = v
        stale = true
    }
    var _aspectRatio = 1f
    fun aspectRatio(v: Float) {
        _aspectRatio = v
        stale = true
    }


    var stale: Boolean = true

    private fun recompute_pv_matrix() {
        stale = false
        _pv_matrix = Matrix4f().perspective(_fieldOfView, _aspectRatio, _near, _far)
                .lookAt(_position, _lookAt, _up)
    }

    fun perspectiveMatrix(): Matrix4f {
        return Matrix4f().perspective(_fieldOfView, _aspectRatio, _near, _far)
    }

    fun viewMatrix(): Matrix4f {
        return Matrix4f().lookAt(_position, _lookAt, _up)
    }

    private var _pv_matrix: Matrix4f = Matrix4f()
    var pv_matrix: Matrix4f = Matrix4f()
        get() {
            if (stale) recompute_pv_matrix()
            return _pv_matrix
        }
}