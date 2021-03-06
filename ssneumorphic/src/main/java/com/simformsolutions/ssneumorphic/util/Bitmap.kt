package com.simformsolutions.ssneumorphic.util

import android.graphics.Bitmap
import com.simformsolutions.ssneumorphic.drawable.SSNeumorphicShapeDrawable

/**
 * Apply blur effect on the [Bitmap] and return [Bitmap] or null if failed.
 * @param drawableState
 */
internal fun Bitmap.blurred(drawableState: SSNeumorphicShapeDrawable.SSNeumorphicShapeDrawableState): Bitmap? {
    if (drawableState.inEditMode) {
        return this
    }
    return drawableState.blurProvider.blur(this)
}