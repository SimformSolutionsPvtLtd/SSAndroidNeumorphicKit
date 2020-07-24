package com.simformsolutions.numorphic.component

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.simformsolutions.numorphic.R
import com.simformsolutions.numorphic.annotation.ShapeType
import com.simformsolutions.numorphic.blueprint.NumorphView
import com.simformsolutions.numorphic.drawable.NumorphShapeDrawable
import com.simformsolutions.numorphic.model.NumorphShapeAppearanceModel
import com.simformsolutions.numorphic.util.NumorphResources

class NumorphEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.numorphEditTextStyle,
    defStyleRes: Int = R.style.Widget_Numorph_EditText
) : AppCompatEditText(context, attrs, defStyleAttr), NumorphView {

    private var isInitialized: Boolean = false
    private val shapeDrawable: NumorphShapeDrawable

    private var lightShadowColor: Int
    private var darkShadowColor: Int

    private var insetStart = 0
    private var insetEnd = 0
    private var insetTop = 0
    private var insetBottom = 0

    init {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.NumorphEditText, defStyleAttr, defStyleRes
        )
        val fillColor = a.getColorStateList(R.styleable.NumorphEditText_numorph_backgroundColor)
        val strokeColor = a.getColorStateList(R.styleable.NumorphEditText_numorph_strokeColor)
        val strokeWidth = a.getDimension(R.styleable.NumorphEditText_numorph_strokeWidth, 0f)
        val shapeType = a.getInt(R.styleable.NumorphEditText_numorph_shapeType, ShapeType.DEFAULT)
        val inset = a.getDimensionPixelSize(
            R.styleable.NumorphEditText_numorph_inset, 0
        )
        val insetStart = a.getDimensionPixelSize(
            R.styleable.NumorphEditText_numorph_insetStart, -1
        )
        val insetEnd = a.getDimensionPixelSize(
            R.styleable.NumorphEditText_numorph_insetEnd, -1
        )
        val insetTop = a.getDimensionPixelSize(
            R.styleable.NumorphEditText_numorph_insetTop, -1
        )
        val insetBottom = a.getDimensionPixelSize(
            R.styleable.NumorphEditText_numorph_insetBottom, -1
        )
        val shadowElevation = a.getDimension(
            R.styleable.NumorphEditText_numorph_shadowElevation, 0f
        )
        lightShadowColor = NumorphResources.getColor(
            context, a,
            R.styleable.NumorphEditText_numorph_shadowColorLight,
            R.color.default_color_shadow_light
        )
        darkShadowColor = NumorphResources.getColor(
            context, a,
            R.styleable.NumorphEditText_numorph_shadowColorDark,
            R.color.default_color_shadow_dark
        )
        val noShadow = a.getBoolean(
            R.styleable.NumorphEditText_noShadow, false
        )
        a.recycle()

        shapeDrawable = NumorphShapeDrawable(context, attrs, defStyleAttr, defStyleRes).apply {
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

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        requestLayout()
        invalidate()
    }

    override fun showShadow() {
        setShadowColorLight(lightShadowColor)
        setShadowColorDark(darkShadowColor)
    }

    override fun hideShadow() {
        setShadowColorLight(ContextCompat.getColor(context, R.color.transparent))
        setShadowColorDark(ContextCompat.getColor(context, R.color.transparent))
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

    override fun setShapeAppearanceModel(shapeAppearanceModel: NumorphShapeAppearanceModel) {
        shapeDrawable.setShapeAppearanceModel(shapeAppearanceModel)
    }

    override fun getShapeAppearanceModel(): NumorphShapeAppearanceModel {
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

    override fun setShapeType(@ShapeType shapeType: Int) {
        shapeDrawable.setShapeType(shapeType)
    }

    @ShapeType
    override fun getShapeType(): Int {
        return shapeDrawable.getShapeType()
    }

    override fun setInset(left: Int, top: Int, right: Int, bottom: Int) {
        internalSetInset(left, top, right, bottom)
    }

    override fun setShadowElevation(shadowElevation: Float) {
        shapeDrawable.setShadowElevation(shadowElevation)
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

    companion object {
        private const val LOG_TAG = "NumorphEditText"
    }
}