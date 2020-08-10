package com.simformsolutions.ssneumorphic.blueprint

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import com.simformsolutions.ssneumorphic.annotation.SSNeumorphicCornerFamily
import com.simformsolutions.ssneumorphic.annotation.SSNeumorphicShapeType
import com.simformsolutions.ssneumorphic.model.SSNeumorphicShapeAppearanceModel

/**
 * Delegate class for every SSNeumorphicView.
 * Interface to declare methods and variables every View has to implement.
 */
internal interface SSNeumorphicView {
    /**
     * Show shadow.
     */
    fun showShadow()

    /**
     * Hide shadow.
     */
    fun hideShadow()

    /**
     * Sets the background drawable.
     * @param drawable the drawable of the background
     */
    fun setBackground(drawable: Drawable?) {
        setBackgroundDrawable(drawable)
    }

    /**
     * Sets the background drawable.
     * @param drawable the drawable of the background
     */
    fun setBackgroundDrawable(drawable: Drawable?)

    /**
     * Getter for [SSNeumorphicShapeAppearanceModel]
     * @return [SSNeumorphicShapeAppearanceModel]
     */
    fun getShapeAppearanceModel(): SSNeumorphicShapeAppearanceModel

    /**
     * Setter for [SSNeumorphicShapeAppearanceModel]
     * @param shapeAppearanceModel the new shape appearance model
     */
    fun setShapeAppearanceModel(shapeAppearanceModel: SSNeumorphicShapeAppearanceModel)

    /**
     * Sets the background color.
     * @param color the color of the background
     */
    fun setBackgroundColor(color: Int)

    /**
     * Sets the background color.
     * @param backgroundColor the [ColorStateList] of the background
     */
    fun setBackgroundColor(backgroundColor: ColorStateList?)

    /**
     * Get the background color.
     * @return [ColorStateList]
     */
    fun getBackgroundColor(): ColorStateList?

    /**
     * Sets the stroke color.
     * @param strokeColor the stroke color
     */
    fun setStrokeColor(strokeColor: ColorStateList?)

    /**
     * Get the stroke color.
     * @return [ColorStateList]
     */
    fun getStrokeColor(): ColorStateList?

    /**
     * Sets the stroke width.
     * @param strokeWidth the width of the stroke
     */
    fun setStrokeWidth(strokeWidth: Float)

    /**
     * Get the stroke width
     * @return [Float] the stroke width
     */
    fun getStrokeWidth(): Float

    /**
     * Sets the shape type.
     * @param shapeType
     */
    fun setShapeType(@SSNeumorphicShapeType shapeType: Int)

    /**
     * Get the shape type.
     * @return [SSNeumorphicShapeType]
     */
    fun getShapeType(): Int

    /**
     * Sets the insets.
     * @param left inset left
     * @param top inset top
     * @param right inset right
     * @param bottom inset bottom
     */
    fun setInset(left: Int, top: Int, right: Int, bottom: Int)

    /**
     * Sets shadow elevation.
     * @param elevation
     */
    fun setShadowElevation(elevation: Float)

    /**
     * Get the shadow elevation.
     * @return shadow elevation
     */
    fun getShadowElevation(): Float

    /**
     * Sets the shadow color light.
     * @param shadowColor the light shadow color
     */
    fun setShadowColorLight(@ColorInt shadowColor: Int)

    /**
     * Get the shadow color light.
     * @return shadow color light
     */
    @ColorInt
    fun getShadowColorLight(): Int

    /**
     * Sets the shadow color dark.
     * @param shadowColor the dark shadow color
     */
    fun setShadowColorDark(@ColorInt shadowColor: Int)

    /**
     * Get the shadow color dark.
     * @return shadow color dark
     */
    @ColorInt
    fun getShadowColorDark(): Int

    /**
     * Sets the translation z.
     * @param translationZ
     */
    fun setTranslationZ(translationZ: Float)

    /**
     * Set all corner radius or specific corner radius.
     * @param cornerRadius Set all corner radius
     * @param cornerRadiusTopLeft Set top left corner radius
     * @param cornerRadiusTopRight Set top right corner radius
     * @param cornerRadiusBottomRight Set top right corner radius
     * @param cornerRadiusBottomLeft Set bottom left corner radius
     */
    fun setCorner(
        @Dimension cornerRadius: Float,
        @Dimension cornerRadiusTopLeft: Float = cornerRadius,
        @Dimension cornerRadiusTopRight: Float = cornerRadius,
        @Dimension cornerRadiusBottomRight: Float = cornerRadius,
        @Dimension cornerRadiusBottomLeft: Float = cornerRadius
    ) {
        // Create a new shape appearance model.
        val newShapeAppearanceModel = SSNeumorphicShapeAppearanceModel.builder(getShapeAppearanceModel())
            .setCorner(
                cornerRadius = cornerRadius,
                cornerRadiusTopLeft = cornerRadiusTopLeft,
                cornerRadiusTopRight = cornerRadiusTopRight,
                cornerRadiusBottomRight = cornerRadiusBottomRight,
                cornerRadiusBottomLeft = cornerRadiusBottomLeft
            )
            .build()

        // Set shape appearance model.
        setShapeAppearanceModel(newShapeAppearanceModel)
    }

    /**
     * Set corner family
     * @param cornerFamily Set corner family
     */
    fun setCornerFamily(@SSNeumorphicCornerFamily cornerFamily: Int) {
        // Create a new shape appearance model.
        val newShapeAppearanceModel = SSNeumorphicShapeAppearanceModel.builder(getShapeAppearanceModel())
            .setCornerFamily(cornerFamily)
            .build()

        // Set shape appearance model.
        setShapeAppearanceModel(newShapeAppearanceModel)
    }
}