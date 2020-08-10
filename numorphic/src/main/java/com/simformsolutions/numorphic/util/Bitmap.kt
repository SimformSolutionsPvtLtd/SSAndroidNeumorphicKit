package com.simformsolutions.numorphic.util

import android.graphics.Bitmap
import com.simformsolutions.numorphic.drawable.SSNeumorphicShapeDrawable

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