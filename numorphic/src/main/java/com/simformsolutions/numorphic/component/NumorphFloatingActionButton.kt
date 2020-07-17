package com.simformsolutions.numorphic.component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageButton
import com.simformsolutions.numorphic.R
import com.simformsolutions.numorphic.annotation.ShapeType
import com.simformsolutions.numorphic.drawable.NumorphShapeDrawable
import com.simformsolutions.numorphic.model.NumorphShapeAppearanceModel
import com.simformsolutions.numorphic.util.NumorphResources

class NumorphFloatingActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.numorphFloatingActionButtonStyle,
    defStyleRes: Int = R.style.Widget_Numorph_FloatingActionButton
) : AppCompatImageButton(context, attrs, defStyleAttr) {

    private var isInitialized: Boolean = false
    private val shapeDrawable: NumorphShapeDrawable

    private var insetStart = 0
    private var insetEnd = 0
    private var insetTop = 0
    private var insetBottom = 0

    init {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.NumorphFloatingActionButton, defStyleAttr, defStyleRes
        )
        val fillColor = a.getColorStateList(R.styleable.NumorphFloatingActionButton_numorph_backgroundColor)
        val strokeColor = a.getColorStateList(R.styleable.NumorphFloatingActionButton_numorph_strokeColor)
        val strokeWidth = a.getDimension(R.styleable.NumorphFloatingActionButton_numorph_strokeWidth, 0f)
        val shapeType =
            a.getInt(R.styleable.NumorphFloatingActionButton_numorph_shapeType, ShapeType.DEFAULT)
        val inset = a.getDimensionPixelSize(
            R.styleable.NumorphFloatingActionButton_numorph_inset, 0
        )
        val insetStart = a.getDimensionPixelSize(
            R.styleable.NumorphFloatingActionButton_numorph_insetStart, -1
        )
        val insetEnd = a.getDimensionPixelSize(
            R.styleable.NumorphFloatingActionButton_numorph_insetEnd, -1
        )
        val insetTop = a.getDimensionPixelSize(
            R.styleable.NumorphFloatingActionButton_numorph_insetTop, -1
        )
        val insetBottom = a.getDimensionPixelSize(
            R.styleable.NumorphFloatingActionButton_numorph_insetBottom, -1
        )
        val shadowElevation = a.getDimension(
            R.styleable.NumorphFloatingActionButton_numorph_shadowElevation, 0f
        )
        val shadowColorLight = NumorphResources.getColor(
            context, a,
            R.styleable.NumorphFloatingActionButton_numorph_shadowColorLight,
            R.color.default_color_shadow_light
        )
        val shadowColorDark = NumorphResources.getColor(
            context, a,
            R.styleable.NumorphFloatingActionButton_numorph_shadowColorDark,
            R.color.default_color_shadow_dark
        )
        a.recycle()

        shapeDrawable = NumorphShapeDrawable(context, attrs, defStyleAttr, defStyleRes).apply {
            setInEditMode(isInEditMode)
            setShapeType(shapeType)
            setShadowElevation(shadowElevation)
            setShadowColorLight(shadowColorLight)
            setShadowColorDark(shadowColorDark)
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
        isInitialized = true
    }

    override fun setBackground(drawable: Drawable?) {
        setBackgroundDrawable(drawable)
    }

    override fun setBackgroundDrawable(drawable: Drawable?) {
        Log.i(LOG_TAG, "Setting a custom background is not supported.")
    }

    private fun setBackgroundInternal(drawable: Drawable?) {
        super.setBackgroundDrawable(drawable)
    }

    fun setShapeAppearanceModel(shapeAppearanceModel: NumorphShapeAppearanceModel) {
        shapeDrawable.setShapeAppearanceModel(shapeAppearanceModel)
    }

    fun getShapeAppearanceModel(): NumorphShapeAppearanceModel {
        return shapeDrawable.getShapeAppearanceModel()
    }

    override fun setBackgroundColor(color: Int) {
        shapeDrawable.setFillColor(ColorStateList.valueOf(color))
    }

    fun setBackgroundColor(backgroundColor: ColorStateList?) {
        shapeDrawable.setFillColor(backgroundColor)
    }

    fun getBackgroundColor(): ColorStateList? {
        return shapeDrawable.getFillColor()
    }

    fun setStrokeColor(strokeColor: ColorStateList?) {
        shapeDrawable.setStrokeColor(strokeColor)
    }

    fun getStrokeColor(): ColorStateList? {
        return shapeDrawable.getStrokeColor()
    }

    fun setStrokeWidth(strokeWidth: Float) {
        shapeDrawable.setStrokeWidth(strokeWidth)
    }

    fun getStrokeWidth(): Float {
        return shapeDrawable.getStrokeWidth()
    }

    fun setShapeType(@ShapeType shapeType: Int) {
        shapeDrawable.setShapeType(shapeType)
    }

    @ShapeType
    fun getShapeType(): Int {
        return shapeDrawable.getShapeType()
    }

    fun setInset(left: Int, top: Int, right: Int, bottom: Int) {
        internalSetInset(left, top, right, bottom)
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

    fun setShadowElevation(shadowElevation: Float) {
        shapeDrawable.setShadowElevation(shadowElevation)
    }

    fun getShadowElevation(): Float {
        return shapeDrawable.getShadowElevation()
    }

    fun setShadowColorLight(@ColorInt shadowColor: Int) {
        shapeDrawable.setShadowColorLight(shadowColor)
    }

    fun setShadowColorDark(@ColorInt shadowColor: Int) {
        shapeDrawable.setShadowColorDark(shadowColor)
    }

    override fun setTranslationZ(translationZ: Float) {
        super.setTranslationZ(translationZ)
        if (isInitialized) {
            shapeDrawable.setTranslationZ(translationZ)
        }
    }

    companion object {
        private const val LOG_TAG = "NumorphFAB"
    }
}
