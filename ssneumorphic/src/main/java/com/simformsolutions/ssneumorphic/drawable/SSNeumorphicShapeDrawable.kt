package com.simformsolutions.ssneumorphic.drawable

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.simformsolutions.ssneumorphic.annotation.SSNeumorphicCornerFamily
import com.simformsolutions.ssneumorphic.annotation.SSNeumorphicShapeType
import com.simformsolutions.ssneumorphic.blur.SSNeumorphicBlurProvider
import com.simformsolutions.ssneumorphic.model.SSNeumorphicShapeAppearanceModel
import com.simformsolutions.ssneumorphic.shape.SSNeumorphicBasinShape
import com.simformsolutions.ssneumorphic.shape.SSNeumorphicFlatShape
import com.simformsolutions.ssneumorphic.shape.SSNeumorphicPressedShape
import com.simformsolutions.ssneumorphic.shape.SSNeumorphicShape


/**
 * The core part of the library.
 *
 * Custom drawable to be applied as background in custom component.
 */
class SSNeumorphicShapeDrawable : Drawable {

    /**  Hold the drawable state  */
    private var drawableState: SSNeumorphicShapeDrawableState

    /**  Flag to update the drawable.  */
    private var dirty = false

    /**
     * Fill paint to fill the canvas for background color.
     */
    private val fillPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.TRANSPARENT
    }

    /**
     * Stroke paint to create strokes for stroke around view.
     */
    private val strokePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.TRANSPARENT
    }

    /**
     * Internal bounds.
     * Assigned globally to reduce object creation in [draw].
     */
    private val boundsRectF = RectF()

    /**
     * Outline path.
     */
    private val outlinePath = Path()

    /**
     * Shadow of the [SSNeumorphicShapeType] for the drawable.
     */
    private var shapeShadow: SSNeumorphicShape? = null

    /**  Bitmap for Image  */
    private var imageBitmap: Bitmap? = null

    /**
     * Constructor to create from
     * @param context
     */
    constructor(context: Context) : this(SSNeumorphicShapeAppearanceModel(), SSNeumorphicBlurProvider(context))

    /**
     * Constructor to create from
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    constructor(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int,
        @StyleRes defStyleRes: Int
    ) : this(
        SSNeumorphicShapeAppearanceModel.builder(context, attrs, defStyleAttr, defStyleRes).build(),
        SSNeumorphicBlurProvider(context)
    )

    /**
     * Internal constructor to create from
     * @param shapeAppearanceModel [SSNeumorphicShapeAppearanceModel]
     * @param blurProvider [SSNeumorphicBlurProvider]
     */
    internal constructor(
        shapeAppearanceModel: SSNeumorphicShapeAppearanceModel,
        blurProvider: SSNeumorphicBlurProvider
    ) : this(
        SSNeumorphicShapeDrawableState(
            shapeAppearanceModel,
            blurProvider
        )
    )

    /**
     * Private constructor to create from
     * @param drawableState [SSNeumorphicShapeDrawableState]
     */
    private constructor(drawableState: SSNeumorphicShapeDrawableState) : super() {
        this.drawableState = drawableState
        this.shapeShadow = shadowOf(drawableState.shapeType, drawableState)
    }

    override fun draw(canvas: Canvas) {
        val prevAlpha = fillPaint.alpha
        fillPaint.alpha =
            modulateAlpha(
                prevAlpha,
                drawableState.alpha
            )

        strokePaint.strokeWidth = drawableState.strokeWidth
        val prevStrokeAlpha = strokePaint.alpha
        strokePaint.alpha =
            modulateAlpha(
                prevStrokeAlpha,
                drawableState.alpha
            )

        /**  If dirty update the drawable  */
        if (dirty) {
            calculateOutlinePath(getBoundsAsRectF(), outlinePath)
            shapeShadow?.updateShadowBitmap(getBoundsInternal())
            dirty = false
        }

        /**  Draw fill shape if has a fill  */
        if (hasFill()) {
            drawFillShape(canvas)
        }

        /**  Draw shadow  */
        shapeShadow?.draw(canvas, outlinePath)

        /**  Draw stroke if has stroke  */
        if (hasStroke()) {
            drawStrokeShape(canvas)
        }

        /**  Draw image if has image bitmap  */
        if (hasImageBitmap()) {
            drawImageBitmap(canvas)
        }

        // Set alpha
        fillPaint.alpha = prevAlpha
        strokePaint.alpha = prevStrokeAlpha
    }

    /**  @return [drawableState]  */
    override fun getConstantState(): ConstantState? {
        return drawableState
    }

    /**
     * Create and assign new [SSNeumorphicShapeDrawableState] to [drawableState].
     * Also update the new state to the [shapeShadow].
     * @return This Drawable
     */
    override fun mutate(): Drawable {
        val newDrawableState =
            SSNeumorphicShapeDrawableState(
                drawableState
            )
        drawableState = newDrawableState
        shapeShadow?.setDrawableState(newDrawableState)
        return this
    }

    /**
     * Called to get the drawable to populate the Outline that defines its drawing area.
     *
     * Set [outline]
     */
    override fun getOutline(outline: Outline) {
        super.getOutline(outline)
        when (drawableState.shapeAppearanceModel.cornerFamily) {
            SSNeumorphicCornerFamily.OVAL -> {
                outline.setOval(getBoundsInternal())
            }
            SSNeumorphicCornerFamily.ROUNDED -> {
                val cornerSize = drawableState.shapeAppearanceModel.cornerRadius
                outline.setRoundRect(getBoundsInternal(), cornerSize)
            }
        }
    }

    /**
     * Indicates whether this drawable will change its appearance based on
     * state.
     *
     * [SSNeumorphicShapeDrawableState.fillColor] is stateful.
     */
    override fun isStateful(): Boolean {
        return (super.isStateful()
                || drawableState.fillColor?.isStateful == true)
    }

    /**
     * Provides corresponding [SSNeumorphicShape] of [SSNeumorphicShapeType].
     * @param shapeType SSNeumorphicShape type
     * @param drawableState
     * @return [SSNeumorphicShape]
     */
    private fun shadowOf(
        @SSNeumorphicShapeType shapeType: Int,
        drawableState: SSNeumorphicShapeDrawableState
    ): SSNeumorphicShape = when (shapeType) {
        SSNeumorphicShapeType.FLAT -> SSNeumorphicFlatShape(drawableState)
        SSNeumorphicShapeType.PRESSED -> SSNeumorphicPressedShape(drawableState)
        SSNeumorphicShapeType.BASIN -> SSNeumorphicBasinShape(drawableState)
        else -> throw IllegalArgumentException("SSNeumorphicShapeType($shapeType) is invalid.")
    }

    /**  Setter for [SSNeumorphicShapeAppearanceModel].  */
    fun setShapeAppearanceModel(shapeAppearanceModel: SSNeumorphicShapeAppearanceModel) {
        drawableState.shapeAppearanceModel = shapeAppearanceModel
        invalidateSelf()
    }

    /**  Getter for [SSNeumorphicShapeAppearanceModel].  */
    fun getShapeAppearanceModel(): SSNeumorphicShapeAppearanceModel {
        return drawableState.shapeAppearanceModel
    }

    /**
     * Setter for [SSNeumorphicShapeDrawableState.fillColor]
     * @param fillColor ColorStateList?
     */
    fun setFillColor(fillColor: ColorStateList?) {
        if (drawableState.fillColor != fillColor) {
            drawableState.fillColor = fillColor
            onStateChange(state)
        }
    }

    /**  Getter for [SSNeumorphicShapeDrawableState.fillColor]  */
    fun getFillColor(): ColorStateList? {
        return drawableState.fillColor
    }

    /**  Setter for [SSNeumorphicShapeDrawableState.strokeColor]  */
    fun setStrokeColor(strokeColor: ColorStateList?) {
        if (drawableState.strokeColor != strokeColor) {
            drawableState.strokeColor = strokeColor
            onStateChange(state)
        }
    }

    /**  Getter for [SSNeumorphicShapeDrawableState.strokeColor]  */
    fun getStrokeColor(): ColorStateList? {
        return drawableState.strokeColor
    }

    /**
     * Set
     * @param strokeWidth as [Float]
     * @param strokeColor as [ColorInt]
     */
    fun setStroke(strokeWidth: Float, @ColorInt strokeColor: Int) {
        setStrokeWidth(strokeWidth)
        setStrokeColor(ColorStateList.valueOf(strokeColor))
    }

    /**
     * Set
     * @param strokeWidth as [Float]
     * @param strokeColor as nullable [ColorStateList]
     */
    fun setStroke(strokeWidth: Float, strokeColor: ColorStateList?) {
        setStrokeWidth(strokeWidth)
        setStrokeColor(strokeColor)
    }

    /**  Getter for [SSNeumorphicShapeDrawableState.strokeWidth]  */
    fun getStrokeWidth(): Float {
        return drawableState.strokeWidth
    }

    /**  Setter for [SSNeumorphicShapeDrawableState.strokeWidth]  */
    fun setStrokeWidth(strokeWidth: Float) {
        drawableState.strokeWidth = strokeWidth
        invalidateSelf()
    }

    /**
     * Set Image from
     * @param bm Bitmap
     */
    fun setImageBitmap(bm: Bitmap?) {
        imageBitmap = bm
        invalidateSelf()
    }

    /**
     * Get Image bitmap.
     */
    fun getImageBitmap(bm: Bitmap?): Bitmap? {
        return imageBitmap
    }

    /**
     * Set background from drawable.
     * @param drawable Drawable to be drawn
     * @param width Width of the view
     * @param height Height of the view
     */
    fun setBackgroundDrawable(drawable: Drawable?, @Px width: Int, @Px height: Int) {
        // Width and Height should not be zero.
        if (width == 0 && height == 0)
            return

        setImageBitmap(drawable?.toBitmap(width, height))
    }

    /**  Getter for Opacity  */
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    /**  Setter for [SSNeumorphicShapeDrawableState.alpha]  */
    override fun setAlpha(alpha: Int) {
        if (drawableState.alpha != alpha) {
            drawableState.alpha = alpha
            invalidateSelfIgnoreShape()
        }
    }

    /**
     * Setter for color filter
     *
     * NOTE : Not supported yet
     */
    override fun setColorFilter(colorFilter: ColorFilter?) {
        // not supported yet
    }

    /**
     * Calculate bounds using insets.
     * @return Internal bounds as [Rect]
     */
    private fun getBoundsInternal(): Rect {
        return drawableState.inset.let { inset ->
            val bounds = super.getBounds()
            Rect(
                bounds.left + inset.left,
                bounds.top + inset.top,
                bounds.right - inset.right,
                bounds.bottom - inset.bottom
            )
        }
    }

    /**
     * @return Get bounds as [RectF]
     */
    private fun getBoundsAsRectF(): RectF {
        boundsRectF.set(getBoundsInternal())
        return boundsRectF
    }

    /**
     * Setter for [SSNeumorphicShapeDrawableState.inset]
     */
    fun setInset(left: Int, top: Int, right: Int, bottom: Int) {
        drawableState.inset.set(left, top, right, bottom)
        invalidateSelf()
    }

    /**
     * Setter for [SSNeumorphicShapeDrawableState.shapeType].
     * Update the shadow as well.
     * @param shapeType
     */
    fun setShapeType(@SSNeumorphicShapeType shapeType: Int) {
        if (drawableState.shapeType != shapeType) {
            drawableState.shapeType = shapeType
            shapeShadow = shadowOf(shapeType, drawableState)
            invalidateSelf()
        }
    }

    /**
     * Getter for [SSNeumorphicShapeDrawableState.shapeType].
     */
    @SSNeumorphicShapeType
    fun getShapeType(): Int {
        return drawableState.shapeType
    }

    /**
     * Setter for [SSNeumorphicShapeDrawableState.shadowElevation].
     */
    fun setShadowElevation(shadowElevation: Float) {
        if (drawableState.shadowElevation != shadowElevation) {
            drawableState.shadowElevation = shadowElevation
            invalidateSelf()
        }
    }

    /**
     * Getter for [SSNeumorphicShapeDrawableState.shadowElevation].
     */
    fun getShadowElevation(): Float {
        return drawableState.shadowElevation
    }

    /**
     * Setter for [SSNeumorphicShapeDrawableState.shadowColorLight].
     *
     * Set the light shadow color.
     *
     * @param shadowColor
     */
    fun setShadowColorLight(@ColorInt shadowColor: Int) {
        if (drawableState.shadowColorLight != shadowColor) {
            drawableState.shadowColorLight = shadowColor
            invalidateSelf()
        }
    }

    /**
     * Getter for [SSNeumorphicShapeDrawableState.shadowColorLight]
     *
     * Get the light shadow color.
     *
     * @return shadow color light
     */
    @ColorInt
    fun getShadowColorLight(): Int {
        return drawableState.shadowColorLight
    }

    /**
     * Setter for [SSNeumorphicShapeDrawableState.shadowColorDark].
     *
     * Set the dark shadow color.
     *
     * @param shadowColor
     */
    fun setShadowColorDark(@ColorInt shadowColor: Int) {
        if (drawableState.shadowColorDark != shadowColor) {
            drawableState.shadowColorDark = shadowColor
            invalidateSelf()
        }
    }

    /**
     * Getter for [SSNeumorphicShapeDrawableState.shadowColorDark]
     *
     * Get the dark shadow color.
     *
     * @return shadow color dark
     */
    @ColorInt
    fun getShadowColorDark(): Int {
        return drawableState.shadowColorLight
    }

    /**
     * Getter for [SSNeumorphicShapeDrawableState.translationZ].
     */
    fun getTranslationZ(): Float {
        return drawableState.translationZ
    }

    /**
     * Setter for [SSNeumorphicShapeDrawableState.translationZ].
     */
    fun setTranslationZ(translationZ: Float) {
        if (drawableState.translationZ != translationZ) {
            drawableState.translationZ = translationZ
            invalidateSelfIgnoreShape()
        }
    }

    /**
     * Getter for Z value. It is addition of shadow elevation and translation z.
     */
    fun getZ(): Float {
        return getShadowElevation() + getTranslationZ()
    }

    /**
     * Setter for Z value.
     */
    fun setZ(z: Float) {
        /**
         * Set [setTranslationZ] by removing [getShadowElevation] from [z].
         */
        setTranslationZ(z - getShadowElevation())
    }

    /**
     * This call will invalidate the view.
     * Set dirty flag to true.
     */
    override fun invalidateSelf() {
        dirty = true
        super.invalidateSelf()
    }

    /**
     * Do not ignore shape in invalidation.
     * Calling [invalidateSelf] here too.
     */
    private fun invalidateSelfIgnoreShape() {
        super.invalidateSelf()
    }

    /**
     * Getter for [SSNeumorphicShapeDrawableState.paintStyle].
     */
    fun getPaintStyle(): Paint.Style? {
        return drawableState.paintStyle
    }

    /**
     * Setter for [SSNeumorphicShapeDrawableState.paintStyle].
     */
    fun setPaintStyle(paintStyle: Paint.Style) {
        drawableState.paintStyle = paintStyle
        invalidateSelfIgnoreShape()
    }

    /**
     * Whether the drawable is filled or not.
     * @return Boolean
     */
    private fun hasFill(): Boolean {
        return (drawableState.paintStyle === Paint.Style.FILL_AND_STROKE
                || drawableState.paintStyle === Paint.Style.FILL)
    }

    /**
     * Whether the drawable has stroke or not.
     */
    private fun hasStroke(): Boolean {
        return ((drawableState.paintStyle == Paint.Style.FILL_AND_STROKE
                || drawableState.paintStyle == Paint.Style.STROKE)
                && strokePaint.strokeWidth > 0)
    }

    private fun hasImageBitmap(): Boolean {
        return imageBitmap != null
    }

    /**
     * Whenever bounds change set dirty flag to true.
     */
    override fun onBoundsChange(bounds: Rect) {
        dirty = true
        super.onBoundsChange(bounds)
    }

    /**
     * Draw fill on specified canvas.
     * @param canvas
     */
    private fun drawFillShape(canvas: Canvas) {
        canvas.drawPath(outlinePath, fillPaint)
    }

    /**
     * Draw stroke shape on specified canvas.
     * @param canvas
     */
    private fun drawStrokeShape(canvas: Canvas) {
        canvas.drawPath(outlinePath, strokePaint)
    }

    private fun drawImageBitmap(canvas: Canvas) {
        imageBitmap?.let {
            canvas.clipPath(outlinePath)
            canvas.drawBitmap(it, null, getBoundsInternal(), null)
        }
    }

    /**
     * Getter for [outlinePath].
     */
    fun getOutlinePath(): Path {
        return outlinePath
    }

    /**
     * Calculate outline path with
     * @param bounds
     * and set
     * @param path
     */
    private fun calculateOutlinePath(bounds: RectF, path: Path) {
        // Inset Left
        val left = drawableState.inset.left.toFloat()
        // Inset Right
        val top = drawableState.inset.top.toFloat()
        // Left + Width
        val right = left + bounds.width()
        // Top + Height
        val bottom = top + bounds.height()

        val shapeAppearanceModel = drawableState.shapeAppearanceModel

        // Reset before using the path.
        path.reset()

        /**
         * Calculate radius to match elevation radius.
         * Add shadow elevation if radius is greater than 0.
         */
        fun Float.calculateRadius(): Float = if (this == 0f) {
            this
        } else {
            this + drawableState.shadowElevation
        }

        // Decide based on corner family.
        when (drawableState.shapeAppearanceModel.cornerFamily) {
            SSNeumorphicCornerFamily.OVAL -> {
                // Add Oval path.
                path.addOval(left, top, right, bottom, Path.Direction.CW)
            }
            SSNeumorphicCornerFamily.ROUNDED -> {
                // Add round rectangle path.
                path.addRoundRect(
                    left, top, right, bottom,
                    floatArrayOf(
                        shapeAppearanceModel.cornerRadiusTopLeft.calculateRadius(),
                        shapeAppearanceModel.cornerRadiusTopLeft.calculateRadius(),
                        shapeAppearanceModel.cornerRadiusTopRight.calculateRadius(),
                        shapeAppearanceModel.cornerRadiusTopRight.calculateRadius(),
                        shapeAppearanceModel.cornerRadiusBottomRight.calculateRadius(),
                        shapeAppearanceModel.cornerRadiusBottomRight.calculateRadius(),
                        shapeAppearanceModel.cornerRadiusBottomLeft.calculateRadius(),
                        shapeAppearanceModel.cornerRadiusBottomLeft.calculateRadius()
                    ),
                    Path.Direction.CW
                )
            }
        }

        // Close the path.
        path.close()
    }

    /**
     * [SSNeumorphicShapeDrawableState.fillColor] is stateful so update colors.
     * @return true if state is changed.
     */
    override fun onStateChange(state: IntArray): Boolean {
        val invalidateSelf = updateColorsForState(state)
        if (invalidateSelf) {
            invalidateSelf()
        }
        return invalidateSelf
    }

    /**
     * Updates color for state.
     * @return true if state is changed.
     */
    private fun updateColorsForState(state: IntArray): Boolean {
        /**  Flag set to true when state is changed.  */
        var invalidateSelf = false

        drawableState.fillColor?.let { fillColor ->
            val previousFillColor: Int = fillPaint.color
            val newFillColor: Int = fillColor.getColorForState(state, previousFillColor)
            if (previousFillColor != newFillColor) {
                // Set new fill paint
                fillPaint.color = newFillColor
                // Mark state change.
                invalidateSelf = true
            }
        }
        drawableState.strokeColor?.let { strokeColor ->
            val previousStrokeColor = strokePaint.color
            val newStrokeColor = strokeColor.getColorForState(state, previousStrokeColor)
            if (previousStrokeColor != newStrokeColor) {
                // Set new fill color
                strokePaint.color = newStrokeColor
                // Mark state change.
                invalidateSelf = true
            }
        }
        return invalidateSelf
    }

    /**
     * Setter for [SSNeumorphicShapeDrawableState.inEditMode].
     */
    fun setInEditMode(inEditMode: Boolean) {
        drawableState.inEditMode = inEditMode
    }

    /**
     * Custom ConstantState to hold custom data.
     */
    internal class SSNeumorphicShapeDrawableState : ConstantState {

        /**  The shape appearance model.  */
        var shapeAppearanceModel: SSNeumorphicShapeAppearanceModel

        /**  The blur provider  */
        val blurProvider: SSNeumorphicBlurProvider

        /**
         * Edit mode flag.
         */
        var inEditMode: Boolean = false

        /**  Internal insets AKA extra padding  */
        var inset: Rect = Rect()

        var fillColor: ColorStateList? = null
        var strokeColor: ColorStateList? = null
        var strokeWidth = 0f

        var alpha = 255

        @SSNeumorphicShapeType
        var shapeType: Int = SSNeumorphicShapeType.DEFAULT
        var shadowElevation: Float = 0f
        var shadowColorLight: Int = Color.WHITE
        var shadowColorDark: Int = Color.BLACK
        var translationZ = 0f

        var paintStyle: Paint.Style = Paint.Style.FILL_AND_STROKE

        /**
         * Create from
         * @param shapeAppearanceModel
         * @param blurProvider
         */
        constructor(
            shapeAppearanceModel: SSNeumorphicShapeAppearanceModel,
            blurProvider: SSNeumorphicBlurProvider
        ) {
            this.shapeAppearanceModel = shapeAppearanceModel
            this.blurProvider = blurProvider
        }

        /**
         * Create from old
         * @param orig [SSNeumorphicShapeDrawableState]
         */
        constructor(orig: SSNeumorphicShapeDrawableState) {
            shapeAppearanceModel = orig.shapeAppearanceModel
            blurProvider = orig.blurProvider
            inEditMode = orig.inEditMode
            inset = Rect(orig.inset)
            fillColor = orig.fillColor
            strokeColor = orig.strokeColor
            strokeWidth = orig.strokeWidth
            alpha = orig.alpha
            shapeType = orig.shapeType
            shadowElevation = orig.shadowElevation
            shadowColorLight = orig.shadowColorLight
            shadowColorDark = orig.shadowColorDark
            translationZ = orig.translationZ
            paintStyle = orig.paintStyle
        }

        /**
         * Create new Drawable.
         * @return The [SSNeumorphicShapeDrawable].
         */
        override fun newDrawable(): Drawable {
            return SSNeumorphicShapeDrawable(
                this
            ).apply {
                // Force the calculation of the path for the new drawable.
                dirty = true
            }
        }

        /**
         * Return bit mask (0 | 1) to completely reload the drawable.
         * 1 - To reload
         * 0 - Not to reload
         */
        override fun getChangingConfigurations(): Int {
            return 0
        }
    }

    companion object {
        private fun modulateAlpha(paintAlpha: Int, alpha: Int): Int {
            val scale = alpha + (alpha ushr 7) // convert to 0..256
            return paintAlpha * scale ushr 8
        }
    }
}
