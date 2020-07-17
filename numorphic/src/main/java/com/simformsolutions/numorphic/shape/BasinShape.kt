package com.simformsolutions.numorphic.shape

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import com.simformsolutions.numorphic.drawable.NumorphShapeDrawable.NumorphShapeDrawableState

/**
 * Basin Shape
 *
 * Basin shape is combination of [FlatShape] and [PressedShape].
 */
internal class BasinShape(drawableState: NumorphShapeDrawableState) : Shape {

    /**
     * List of shadows to be applied on this shape.
     */
    private val shadows = listOf(
        FlatShape(drawableState),
        PressedShape(drawableState)
    )

    /**
     * Apply [newDrawableState] to every shadow.
     */
    override fun setDrawableState(newDrawableState: NumorphShapeDrawableState) {
        shadows.forEach { it.setDrawableState(newDrawableState) }
    }

    /**
     * Call draw on every shadow.
     */
    override fun draw(canvas: Canvas, outlinePath: Path) {
        shadows.forEach { it.draw(canvas, outlinePath) }
    }

    /**
     * Call updateShadowBitmap for every shadow.
     */
    override fun updateShadowBitmap(bounds: Rect) {
        shadows.forEach { it.updateShadowBitmap(bounds) }
    }
}
