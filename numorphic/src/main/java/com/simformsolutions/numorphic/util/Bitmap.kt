package com.simformsolutions.numorphic.util

import android.graphics.Bitmap
import com.simformsolutions.numorphic.drawable.NumorphShapeDrawable

/**
 * Apply blur effect on the [Bitmap] and return [Bitmap] or null if failed.
 * @param drawableState
 */
internal fun Bitmap.blurred(drawableState: NumorphShapeDrawable.NumorphShapeDrawableState): Bitmap? {
    if (drawableState.inEditMode) {
        return this
    }
    return drawableState.blurProvider.blur(this)
}