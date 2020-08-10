package com.simformsolutions.ssneumorphic.shape

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import com.simformsolutions.ssneumorphic.drawable.SSNeumorphicShapeDrawable.SSNeumorphicShapeDrawableState

/**
 * Basin SSNeumorphicShape
 *
 * Basin shape is combination of [SSNeumorphicFlatShape] and [SSNeumorphicPressedShape].
 */
internal class SSNeumorphicBasinShape(drawableState: SSNeumorphicShapeDrawableState) : SSNeumorphicShape {

    /**
     * List of shadows to be applied on this shape.
     */
    private val shadows = listOf(
        SSNeumorphicFlatShape(drawableState),
        SSNeumorphicPressedShape(drawableState)
    )

    /**
     * Apply [newDrawableState] to every shadow.
     */
    override fun setDrawableState(newDrawableState: SSNeumorphicShapeDrawableState) {
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
