package com.simformsolutions.ssneumorphic.shape

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.withSave
import androidx.core.graphics.withTranslation
import com.simformsolutions.ssneumorphic.annotation.SSNeumorphicCornerFamily
import com.simformsolutions.ssneumorphic.drawable.SSNeumorphicShapeDrawable.SSNeumorphicShapeDrawableState
import com.simformsolutions.ssneumorphic.model.SSNeumorphicShapeAppearanceModel
import com.simformsolutions.ssneumorphic.util.blurred
import com.simformsolutions.ssneumorphic.util.withClipOut
import kotlin.math.roundToInt

/**
 * Flat SSNeumorphicShape.
 *
 * Shadows are applied outside of the view.
 */
internal class SSNeumorphicFlatShape(
    private var drawableState: SSNeumorphicShapeDrawableState
) : SSNeumorphicShape {

    /**  Light shadow Bitmap to apply light shadow  */
    private var lightShadowBitmap: Bitmap? = null

    /**  Dark shadow Bitmap to apply dark shadow  */
    private var darkShadowBitmap: Bitmap? = null

    /**  Light shadow Gradient drawable  */
    private val lightShadowDrawable = GradientDrawable()

    /**  Dark shadow Gradient drawable  */
    private val darkShadowDrawable = GradientDrawable()

    override fun setDrawableState(newDrawableState: SSNeumorphicShapeDrawableState) {
        this.drawableState = newDrawableState
    }

    override fun draw(canvas: Canvas, outlinePath: Path) {
        canvas.withSave {
            withClipOut(outlinePath) {
                val elevation = drawableState.shadowElevation
                val z = drawableState.shadowElevation + drawableState.translationZ
                val left: Float
                val top: Float
                val inset = drawableState.inset
                left = inset.left.toFloat()
                top = inset.top.toFloat()
                lightShadowBitmap?.let {
                    val offset = -elevation - z
                    drawBitmap(it, offset + left, offset + top, null)
                }
                darkShadowBitmap?.let {
                    val offset = -elevation + z
                    drawBitmap(it, offset + left, offset + top, null)
                }
            }
        }
    }

    override fun updateShadowBitmap(bounds: Rect) {
        /**
         * Set corner shape on the [GradientDrawable].
         * @param shapeAppearanceModel
         */
        fun GradientDrawable.setCornerShape(shapeAppearanceModel: SSNeumorphicShapeAppearanceModel) {
            when (shapeAppearanceModel.cornerFamily) {
                SSNeumorphicCornerFamily.OVAL -> {
                    shape = GradientDrawable.OVAL
                }
                SSNeumorphicCornerFamily.ROUNDED -> {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadii =
                        floatArrayOf(
                            shapeAppearanceModel.cornerRadiusTopLeft,
                            shapeAppearanceModel.cornerRadiusTopLeft,
                            shapeAppearanceModel.cornerRadiusTopRight,
                            shapeAppearanceModel.cornerRadiusTopRight,
                            shapeAppearanceModel.cornerRadiusBottomRight,
                            shapeAppearanceModel.cornerRadiusBottomRight,
                            shapeAppearanceModel.cornerRadiusBottomLeft,
                            shapeAppearanceModel.cornerRadiusBottomLeft
                        )
                }
            }
        }

        lightShadowDrawable.apply {
            setColor(drawableState.shadowColorLight)
            setCornerShape(drawableState.shapeAppearanceModel)
        }
        darkShadowDrawable.apply {
            setColor(drawableState.shadowColorDark)
            setCornerShape(drawableState.shapeAppearanceModel)
        }

        val w = bounds.width()
        val h = bounds.height()
        lightShadowDrawable.setSize(w, h)
        lightShadowDrawable.setBounds(0, 0, w, h)
        darkShadowDrawable.setSize(w, h)
        darkShadowDrawable.setBounds(0, 0, w, h)
        lightShadowBitmap = lightShadowDrawable.toBlurredBitmap(w, h)
        darkShadowBitmap = darkShadowDrawable.toBlurredBitmap(w, h)
    }

    /**
     * Blur the [Drawable] and return [Bitmap].
     * @param w Width
     * @param h Height
     */
    private fun Drawable.toBlurredBitmap(w: Int, h: Int): Bitmap? {
        val shadowElevation = drawableState.shadowElevation
        val width = (w + shadowElevation * 2).roundToInt()
        val height = (h + shadowElevation * 2).roundToInt()
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            .applyCanvas {
                withSave {
                    withTranslation(shadowElevation, shadowElevation) {
                        draw(this)
                    }
                }
            }
            .blurred(drawableState)
    }
}