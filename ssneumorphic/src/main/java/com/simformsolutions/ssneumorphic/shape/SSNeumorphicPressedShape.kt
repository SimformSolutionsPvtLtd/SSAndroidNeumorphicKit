package com.simformsolutions.ssneumorphic.shape

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.withClip
import androidx.core.graphics.withSave
import androidx.core.graphics.withTranslation
import com.simformsolutions.ssneumorphic.annotation.SSNeumorphicCornerFamily
import com.simformsolutions.ssneumorphic.drawable.SSNeumorphicShapeDrawable.SSNeumorphicShapeDrawableState
import com.simformsolutions.ssneumorphic.util.blurred
import kotlin.math.min

/**
 * Pressed SSNeumorphicShape
 *
 * Shadows are applied inside of the view.
 */
internal class SSNeumorphicPressedShape(
    private var drawableState: SSNeumorphicShapeDrawableState
) : SSNeumorphicShape {

    /**  Single shadow [Bitmap]. Containing light shadow and dark shadow.  */
    private var shadowBitmap: Bitmap? = null

    /**  Light shadow Gradient drawable  */
    private val lightShadowDrawable = GradientDrawable()

    /**  Dark shadow Gradient drawable  */
    private val darkShadowDrawable = GradientDrawable()

    override fun setDrawableState(newDrawableState: SSNeumorphicShapeDrawableState) {
        this.drawableState = newDrawableState
    }

    override fun draw(canvas: Canvas, outlinePath: Path) {
        canvas.withSave {
            withClip(outlinePath) {
                shadowBitmap?.let {
                    val left: Float
                    val top: Float
                    val inset = drawableState.inset
                    left = inset.left.toFloat()
                    top = inset.top.toFloat()
                    drawBitmap(it, left, top, null)
                }
            }
        }
    }

    override fun updateShadowBitmap(bounds: Rect) {
        val shadowElevation = drawableState.shadowElevation.toInt()
        val w = bounds.width()
        val h = bounds.height()
        val width: Int = w + shadowElevation
        val height: Int = h + shadowElevation
        val shapeAppearanceModel = drawableState.shapeAppearanceModel

        fun Float.cornerRadius(): Float = min(
            min(w / 2f, h / 2f), this
        )

        lightShadowDrawable.apply {
            setSize(width, height)
            setStroke(shadowElevation, drawableState.shadowColorLight)

            when (drawableState.shapeAppearanceModel.cornerFamily) {
                SSNeumorphicCornerFamily.OVAL -> {
                    shape = GradientDrawable.OVAL
                }
                SSNeumorphicCornerFamily.ROUNDED -> {
                    shape = GradientDrawable.RECTANGLE
                    //cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, cornerSize, cornerSize, 0f, 0f)
                    cornerRadii = floatArrayOf(
                        shapeAppearanceModel.cornerRadiusTopLeft.cornerRadius(),
                        shapeAppearanceModel.cornerRadiusTopLeft.cornerRadius(),
                        shapeAppearanceModel.cornerRadiusTopRight.cornerRadius(),
                        shapeAppearanceModel.cornerRadiusTopRight.cornerRadius(),
                        shapeAppearanceModel.cornerRadiusBottomRight.cornerRadius(),
                        shapeAppearanceModel.cornerRadiusBottomRight.cornerRadius(),
                        shapeAppearanceModel.cornerRadiusBottomLeft.cornerRadius(),
                        shapeAppearanceModel.cornerRadiusBottomLeft.cornerRadius()
                    )
                }
            }
        }
        darkShadowDrawable.apply {
            setSize(width, height)
            setStroke(shadowElevation, drawableState.shadowColorDark)

            when (drawableState.shapeAppearanceModel.cornerFamily) {
                SSNeumorphicCornerFamily.OVAL -> {
                    shape = GradientDrawable.OVAL
                }
                SSNeumorphicCornerFamily.ROUNDED -> {
                    shape = GradientDrawable.RECTANGLE
                    //cornerRadii = floatArrayOf(cornerSize, cornerSize, 0f, 0f, 0f, 0f, 0f, 0f)
                    cornerRadii = floatArrayOf(
                        shapeAppearanceModel.cornerRadiusTopLeft.cornerRadius(),
                        shapeAppearanceModel.cornerRadiusTopLeft.cornerRadius(),
                        shapeAppearanceModel.cornerRadiusTopRight.cornerRadius(),
                        shapeAppearanceModel.cornerRadiusTopRight.cornerRadius(),
                        shapeAppearanceModel.cornerRadiusBottomRight.cornerRadius(),
                        shapeAppearanceModel.cornerRadiusBottomRight.cornerRadius(),
                        shapeAppearanceModel.cornerRadiusBottomLeft.cornerRadius(),
                        shapeAppearanceModel.cornerRadiusBottomLeft.cornerRadius()
                    )
                }
            }
        }

        lightShadowDrawable.setSize(width, height)
        lightShadowDrawable.setBounds(0, 0, width, height)
        darkShadowDrawable.setSize(width, height)
        darkShadowDrawable.setBounds(0, 0, width, height)
        shadowBitmap = generateShadowBitmap(w, h)
    }

    /**
     * Generate the [Bitmap] from the shape.
     * @return Returns a Bitmap combining light shadow and dark shadow.
     */
    private fun generateShadowBitmap(w: Int, h: Int): Bitmap? {
        val shadowElevation = drawableState.shadowElevation
        return Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            .applyCanvas {
                withSave {
                    withTranslation(-shadowElevation, -shadowElevation) {
                        lightShadowDrawable.draw(this)
                    }
                }
                darkShadowDrawable.draw(this)
            }
            .blurred(drawableState)
    }
}