package com.simformsolutions.ssneumorphic.blur

import android.graphics.Color

/**
 * Holds data later be used in blur.
 * @param width
 * @param height
 * @param radius
 * @param sampling
 * @param color
 */
internal data class SSNeumorphicBlurFactor(
    val width: Int,
    val height: Int,
    val radius: Int = DEFAULT_RADIUS,
    val sampling: Int = DEFAULT_SAMPLING,
    val color: Int = Color.TRANSPARENT
) {
    companion object {
        const val DEFAULT_RADIUS = 25
        const val DEFAULT_SAMPLING = 1
    }
}
