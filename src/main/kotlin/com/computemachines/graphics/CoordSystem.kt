package com.computemachines.graphics

import org.joml.AxisAngle4f
import org.joml.Matrix4f
import org.joml.Vector3f

/**
 * A Coordinate System.
 *
 * Represents the position and rotation of an object.
 */
class CoordSystem(val position: Vector3f, val rotation: AxisAngle4f,
                  val scale: Vector3f = Vector3f(1f, 1f, 1f)) {

    private var _matrix_stale = true
    val matrix: Matrix4f = Matrix4f()
        get() {
            if(_matrix_stale)
                field.identity().translate(position).scale(scale).rotate(rotation)
            return field
        }
}