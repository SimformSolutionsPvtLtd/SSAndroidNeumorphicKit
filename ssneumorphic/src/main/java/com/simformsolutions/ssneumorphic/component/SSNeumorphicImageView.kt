package com.simformsolutions.ssneumorphic.component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.simformsolutions.ssneumorphic.R
import com.simformsolutions.ssneumorphic.annotation.SSNeumorphicShapeType
import com.simformsolutions.ssneumorphic.blueprint.SSNeumorphicView
import com.simformsolutions.ssneumorphic.drawable.SSNeumorphicShapeDrawable
import com.simformsolutions.ssneumorphic.model.SSNeumorphicShapeAppearanceModel
import com.simformsolutions.ssneumorphic.util.SSNeumorphicResources

class SSNeumorphicImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.ss_neumorphicImageViewStyle,
    defStyleRes: Int = R.style.Widget_SSNeumorphic_ImageView
) : AppCompatImageView(context, attrs, defStyleAttr), SSNeumorphicView {

    private var isInitialized: Boolean = false
    private val shapeDrawable: SSNeumorphicShapeDrawable

    private var lightShadowColor: Int
    private var darkShadowColor: Int

    private var insetStart = 0
    private var insetEnd = 0
    private var insetTop = 0
    private var insetBottom = 0

    init {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.SSNeumorphicImageView, defStyleAttr, defStyleRes
        )
        val fillColor = a.getColorStateList(R.styleable.SSNeumorphicImageView_ss_neumorphic_backgroundColor)
        val strokeColor = a.getColorStateList(R.styleable.SSNeumorphicImageView_ss_neumorphic_strokeColor)
        val strokeWidth = a.getDimension(R.styleable.SSNeumorphicImageView_ss_neumorphic_strokeWidth, 0f)
        val shapeType = a.getInt(R.styleable.SSNeumorphicImageView_ss_neumorphic_shapeType, SSNeumorphicShapeType.DEFAULT)
        val inset = a.getDimensionPixelSize(
            R.styleable.SSNeumorphicImageView_ss_neumorphic_inset, 0
        )
        val insetStart = a.getDimensionPixelSize(
            R.styleable.SSNeumorphicImageView_ss_neumorphic_insetStart, -1
        )
        val insetEnd = a.getDimensionPixelSize(
            R.styleable.SSNeumorphicImageView_ss_neumorphic_insetEnd, -1
        )
        val insetTop = a.getDimensionPixelSize(
            R.styleable.SSNeumorphicImageView_ss_neumorphic_insetTop, -1
        )
        val insetBottom = a.getDimensionPixelSize(
            R.styleable.SSNeumorphicImageView_ss_neumorphic_insetBottom, -1
        )
        val shadowElevation = a.getDimension(
            R.styleable.SSNeumorphicImageView_ss_neumorphic_shadowElevation, 0f
        )
        lightShadowColor = SSNeumorphicResources.getColor(
            context, a,
            R.styleable.SSNeumorphicImageView_ss_neumorphic_shadowColorLight,
            R.color.ss_neumorphic_default_color_shadow_light
        )
        darkShadowColor = SSNeumorphicResources.getColor(
            context, a,
            R.styleable.SSNeumorphicImageView_ss_neumorphic_shadowColorDark,
            R.color.ss_neumorphic_default_color_shadow_dark
        )
        val noShadow = a.getBoolean(
            R.styleable.SSNeumorphicImageView_ss_neumorphic_noShadow, false
        )
        a.recycle()

        shapeDrawable = SSNeumorphicShapeDrawable(context, attrs, defStyleAttr, defStyleRes).apply {
            setInEditMode(isInEditMode)
            setShapeType(shapeType)
            setShadowElevation(shadowElevation)
            setShadowColorLight(lightShadowColor)
            setShadowColorDark(darkShadowColor)
            setFillColor(fillColor)
            setStroke(strokeWidth, strokeColor)
            setTranslationZ(translationZ)
        }
        internalSetInset(
            if (insetStart >= 0) insetStart else inset,
            if (insetTop >= 0) insetTop else inset,
            if (insetEnd >= 0) insetEnd else inset,
            if (insetBottom >= 0) insetBottom else inset
        )
        setBackgroundInternal(shapeDrawable)
        if(noShadow) hideShadow() else showShadow()
        isInitialized = true
    }

    /**
     * Show shadow.
     */
    override fun showShadow() {
        setShadowColorLight(lightShadowColor)
        setShadowColorDark(darkShadowColor)
    }

    /**
     * Hide shadow.
     */
    override fun hideShadow() {
        setShadowColorLight(ContextCompat.getColor(context, R.color.ss_neumorphic_transparent))
        setShadowColorDark(ContextCompat.getColor(context, R.color.ss_neumorphic_transparent))
    }

    /**
     * Intercept this method. Native implementation will draw over shadow.
     * @param drawable Drawable to draw
     */
    override fun setImageDrawable(drawable: Drawable?) {
        /**  Call as bitmap  */
        shapeDrawable.setImageBitmap(drawable?.toBitmap())
    }

    /**
     * Intercept this method. Native implementation will draw over shadow.
     * @param bm Bitmap to draw
     */
    override fun setImageBitmap(bm: Bitmap?) {
        shapeDrawable.setImageBitmap(bm)
    }

    override fun setBackground(drawable: Drawable?) {
        setBackgroundDrawable(drawable)
    }

    override fun setBackgroundDrawable(drawable: Drawable?) {
        /**  Run only when layout measuring is completed. */
        post {
            shapeDrawable.setBackgroundDrawable(drawable, width, height)
        }
    }

    private fun setBackgroundInternal(drawable: Drawable?) {
        super.setBackgroundDrawable(drawable)
    }

    override fun setShapeAppearanceModel(shapeAppearanceModel: SSNeumorphicShapeAppearanceModel) {
        shapeDrawable.setShapeAppearanceModel(shapeAppearanceModel)
    }

    override fun getShapeAppearanceModel(): SSNeumorphicShapeAppearanceModel {
        return shapeDrawable.getShapeAppearanceModel()
    }

    override fun setBackgroundColor(color: Int) {
        shapeDrawable.setFillColor(ColorStateList.valueOf(color))
    }

    override fun setBackgroundColor(backgroundColor: ColorStateList?) {
        shapeDrawable.setFillColor(backgroundColor)
    }

    override fun getBackgroundColor(): ColorStateList? {
        return shapeDrawable.getFillColor()
    }

    override fun setStrokeColor(strokeColor: ColorStateList?) {
        shapeDrawable.setStrokeColor(strokeColor)
    }

    override fun getStrokeColor(): ColorStateList? {
        return shapeDrawable.getStrokeColor()
    }

    override fun setStrokeWidth(strokeWidth: Float) {
        shapeDrawable.setStrokeWidth(strokeWidth)
    }

    override fun getStrokeWidth(): Float {
        return shapeDrawable.getStrokeWidth()
    }

    override fun setShapeType(@SSNeumorphicShapeType shapeType: Int) {
        shapeDrawable.setShapeType(shapeType)
    }

    @SSNeumorphicShapeType
    override fun getShapeType(): Int {
        return shapeDrawable.getShapeType()
    }

    override fun setInset(left: Int, top: Int, right: Int, bottom: Int) {
        internalSetInset(left, top, right, bottom)
    }

    override fun setShadowElevation(elevation: Float) {
        shapeDrawable.setShadowElevation(elevation)
    }

    override fun getShadowElevation(): Float {
        return shapeDrawable.getShadowElevation()
    }

    override fun setShadowColorLight(@ColorInt shadowColor: Int) {
        shapeDrawable.setShadowColorLight(shadowColor)
    }

    override fun getShadowColorLight(): Int {
        return shapeDrawable.getShadowColorLight()
    }

    override fun setShadowColorDark(@ColorInt shadowColor: Int) {
        shapeDrawable.setShadowColorDark(shadowColor)
    }

    override fun getShadowColorDark(): Int {
        return shapeDrawable.getShadowColorDark()
    }

    override fun setTranslationZ(translationZ: Float) {
        super.setTranslationZ(translationZ)
        if (isInitialized) {
            shapeDrawable.setTranslationZ(translationZ)
        }
    }

    private fun internalSetInset(left: Int, top: Int, right: Int, bottom: Int) {
        var changed = false
        if (insetStart != left) {
            changed = true
            insetStart = left
        }
        if (insetTop != top) {
            changed = true
            insetTop = top
        }
        if (insetEnd != right) {
            changed = true
            insetEnd = right
        }
        if (insetBottom != bottom) {
            changed = true
            insetBottom = bottom
        }

        if (changed) {
            shapeDrawable.setInset(left, top, right, bottom)
            requestLayout()
            invalidateOutline()
        }
    }
}
