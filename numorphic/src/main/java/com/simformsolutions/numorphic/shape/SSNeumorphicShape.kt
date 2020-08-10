package com.simformsolutions.numorphic.shape

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import com.simformsolutions.numorphic.drawable.SSNeumorphicShapeDrawable.SSNeumorphicShapeDrawableState

/**
 * Declaration of the [SSNeumorphicShape].
 */
internal interface SSNeumorphicShape {
    /** Set [newDrawableState] */
    fun setDrawableState(newDrawableState: SSNeumorphicShapeDrawableState)

    /**
     * This method will be generally called when onDraw is called.
     * Use to draw shape.
     *
     * @param canvas Canvas on which to draw
     * @param outlinePath Outline path of the view.
     */
    fun draw(canvas: Canvas, outlinePath: Path)

    /**
     * This method update or create the required shadow bitmaps by the shape.
     *
     * @param bounds Bounds of the view.
     */
    fun updateShadowBitmap(bounds: Rect)
}
