package com.simformsolutions.numorphic.drawable

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import com.simformsolutions.numorphic.annotation.CornerFamily
import com.simformsolutions.numorphic.annotation.ShapeType
import com.simformsolutions.numorphic.blur.BlurProvider
import com.simformsolutions.numorphic.model.NumorphShapeAppearanceModel
import com.simformsolutions.numorphic.shape.BasinShape
import com.simformsolutions.numorphic.shape.FlatShape
import com.simformsolutions.numorphic.shape.PressedShape
import com.simformsolutions.numorphic.shape.Shape

/**
 * The core part of the library.
 *
 * Custom drawable to be applied as background in custom component.
 */
class NumorphShapeDrawable : Drawable {

    /**  Hold the drawable state  */
    private var drawableState: NumorphShapeDrawableState

    /**
     * Flag to update the drawable.
     */
    private var dirty = false

    /**
     * Fill paint to fill the canvas.
     */
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.TRANSPARENT
    }

    /**
     * Stroke paint to create strokes.
     */
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.TRANSPARENT
    }

    /**
     * Holds rectangle.
     * Assigned globally to reduce object creation in [draw].
     */
    private val rectF = RectF()

    /**  Outline path  */
    private val outlinePath = Path()

    /**  Shape  */
    private var shadow: Shape? = null

    /**
     * Constructor to create from
     * @param context
     */
    constructor(context: Context) : this(NumorphShapeAppearanceModel(), BlurProvider(context))

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
        NumorphShapeAppearanceModel.builder(context, attrs, defStyleAttr, defStyleRes).build(),
        BlurProvider(context)
    )

    /**
     * Internal constructor to create from
     * @param shapeAppearanceModel [NumorphShapeAppearanceModel]
     * @param blurProvider [BlurProvider]
     */
    internal constructor(
        shapeAppearanceModel: NumorphShapeAppearanceModel,
        blurProvider: BlurProvider
    ) : this(
        NumorphShapeDrawableState(
            shapeAppearanceModel,
            blurProvider
        )
    )

    /**
     * Private constructor to create from
     * @param drawableState [NumorphShapeDrawableState]
     */
    private constructor(drawableState: NumorphShapeDrawableState) : super() {
        this.drawableState = drawableState
        this.shadow = shadowOf(drawableState.shapeType, drawableState)
    }

    /**
     * Provides corresponding [Shape] of [ShapeType].
     * @param shapeType Shape type
     * @param drawableState
     * @return [Shape]
     */
    private fun shadowOf(
        @ShapeType shapeType: Int,
        drawableState: NumorphShapeDrawableState
    ): Shape {
        return when (shapeType) {
            ShapeType.FLAT -> FlatShape(drawableState)
            ShapeType.PRESSED -> PressedShape(drawableState)
            ShapeType.BASIN -> BasinShape(drawableState)
            else -> throw IllegalArgumentException("ShapeType($shapeType) is invalid.")
        }
    }

    /**  @return [drawableState]  */
    override fun getConstantState(): ConstantState? {
        return drawableState
    }

    /**
     * Create and assign new [NumorphShapeDrawableState] to [drawableState].
     * Also update the new state to the [shadow].
     * @return This Drawable
     */
    override fun mutate(): Drawable {
        val newDrawableState =
            NumorphShapeDrawableState(
                drawableState
            )
        drawableState = newDrawableState
        shadow?.setDrawableState(newDrawableState)
        return this
    }

    /**  Setter for [NumorphShapeAppearanceModel].  */
    fun setShapeAppearanceModel(shapeAppearanceModel: NumorphShapeAppearanceModel) {
        drawableState.shapeAppearanceModel = shapeAppearanceModel
        invalidateSelf()
    }

    /**  Getter for [NumorphShapeAppearanceModel].  */
    fun getShapeAppearanceModel(): NumorphShapeAppearanceModel {
        return drawableState.shapeAppearanceModel
    }

    /**
     * Setter for [NumorphShapeDrawableState.fillColor]
     * @param fillColor ColorStateList?
     */
    fun setFillColor(fillColor: ColorStateList?) {
        if (drawableState.fillColor != fillColor) {
            drawableState.fillColor = fillColor
            onStateChange(state)
        }
    }

    /**  Getter for [NumorphShapeDrawableState.fillColor]  */
    fun getFillColor(): ColorStateList? {
        return drawableState.fillColor
    }

    /**  Setter for [NumorphShapeDrawableState.strokeColor]  */
    fun setStrokeColor(strokeColor: ColorStateList?) {
        if (drawableState.strokeColor != strokeColor) {
            drawableState.strokeColor = strokeColor
            onStateChange(state)
        }
    }

    /**  Getter for [NumorphShapeDrawableState.strokeColor]  */
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

    /**  Getter for [NumorphShapeDrawableState.strokeWidth]  */
    fun getStrokeWidth(): Float {
        return drawableState.strokeWidth
    }

    /**  Setter for [NumorphShapeDrawableState.strokeWidth]  */
    fun setStrokeWidth(strokeWidth: Float) {
        drawableState.strokeWidth = strokeWidth
        invalidateSelf()
    }

    /**  Getter for Opacity  */
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    /**  Setter for [NumorphShapeDrawableState.alpha]  */
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
        rectF.set(getBoundsInternal())
        return rectF
    }

    /**
     * Setter for [NumorphShapeDrawableState.inset]
     */
    fun setInset(left: Int, top: Int, right: Int, bottom: Int) {
        drawableState.inset.set(left, top, right, bottom)
        invalidateSelf()
    }

    /**
     * Setter for [NumorphShapeDrawableState.shapeType].
     * Update the shadow as well.
     * @param shapeType
     */
    fun setShapeType(@ShapeType shapeType: Int) {
        if (drawableState.shapeType != shapeType) {
            drawableState.shapeType = shapeType
            shadow = shadowOf(shapeType, drawableState)
            invalidateSelf()
        }
    }

    /**
     * Getter for [NumorphShapeDrawableState.shapeType].
     */
    @ShapeType
    fun getShapeType(): Int {
        return drawableState.shapeType
    }

    /**
     * Setter for [NumorphShapeDrawableState.shadowElevation].
     */
    fun setShadowElevation(shadowElevation: Float) {
        if (drawableState.shadowElevation != shadowElevation) {
            drawableState.shadowElevation = shadowElevation
            invalidateSelf()
        }
    }

    /**
     * Getter for [NumorphShapeDrawableState.shadowElevation].
     */
    fun getShadowElevation(): Float {
        return drawableState.shadowElevation
    }

    /**
     * Setter for [NumorphShapeDrawableState.shadowColorLight].
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
     * Setter for [NumorphShapeDrawableState.shadowColorDark].
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
     * Getter for [NumorphShapeDrawableState.translationZ].
     */
    fun getTranslationZ(): Float {
        return drawableState.translationZ
    }

    /**
     * Setter for [NumorphShapeDrawableState.translationZ].
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
     * Getter for [NumorphShapeDrawableState.paintStyle].
     */
    fun getPaintStyle(): Paint.Style? {
        return drawableState.paintStyle
    }

    /**
     * Setter for [NumorphShapeDrawableState.paintStyle].
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

    /**
     * Whenever bounds change set dirty flag to true.
     */
    override fun onBoundsChange(bounds: Rect) {
        dirty = true
        super.onBoundsChange(bounds)
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
            shadow?.updateShadowBitmap(getBoundsInternal())
            dirty = false
        }

        /**  Draw fill shape if has a fill  */
        if (hasFill()) {
            drawFillShape(canvas)
        }

        /**  Draw shadow  */
        shadow?.draw(canvas, outlinePath)

        /**  Draw stroke if has stroke  */
        if (hasStroke()) {
            drawStrokeShape(canvas)
        }

        // Set alpha
        fillPaint.alpha = prevAlpha
        strokePaint.alpha = prevStrokeAlpha
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

        // Reset before using the path.
        path.reset()

        // Decide based on corner family.
        when (drawableState.shapeAppearanceModel.getCornerFamily()) {
            CornerFamily.OVAL -> {
                // Add Oval path.
                path.addOval(left, top, right, bottom, Path.Direction.CW)
            }
            CornerFamily.ROUNDED -> {
                // Add round rectangle path.
                val cornerSize = drawableState.shapeAppearanceModel.getCornerSize()
                path.addRoundRect(
                    left, top, right, bottom,
                    cornerSize, cornerSize,
                    Path.Direction.CW
                )
            }
        }

        // Close the path.
        path.close()
    }

    /**
     * Called to get the drawable to populate the Outline that defines its drawing area.
     *
     * Set [outline]
     */
    override fun getOutline(outline: Outline) {
        when (drawableState.shapeAppearanceModel.getCornerFamily()) {
            CornerFamily.OVAL -> {
                outline.setOval(getBoundsInternal())
            }
            CornerFamily.ROUNDED -> {
                val cornerSize = drawableState.shapeAppearanceModel.getCornerSize()
                outline.setRoundRect(getBoundsInternal(), cornerSize)
            }
        }
    }

    /**
     * Indicates whether this drawable will change its appearance based on
     * state.
     *
     * [NumorphShapeDrawableState.fillColor] is stateful.
     */
    override fun isStateful(): Boolean {
        return (super.isStateful()
                || drawableState.fillColor?.isStateful == true)
    }

    /**
     * [NumorphShapeDrawableState.fillColor] is stateful so update colors.
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
     * Setter for [NumorphShapeDrawableState.inEditMode].
     */
    fun setInEditMode(inEditMode: Boolean) {
        drawableState.inEditMode = inEditMode
    }

    /**
     * Custom ConstantState to hold custom data.
     */
    internal class NumorphShapeDrawableState : ConstantState {

        /**  The shape appearance model.  */
        var shapeAppearanceModel: NumorphShapeAppearanceModel

        /**  The blur provider  */
        val blurProvider: BlurProvider

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

        @ShapeType
        var shapeType: Int = ShapeType.DEFAULT
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
            shapeAppearanceModel: NumorphShapeAppearanceModel,
            blurProvider: BlurProvider
        ) {
            this.shapeAppearanceModel = shapeAppearanceModel
            this.blurProvider = blurProvider
        }

        /**
         * Create from old
         * @param orig [NumorphShapeDrawableState]
         */
        constructor(orig: NumorphShapeDrawableState) {
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
         * @return The [NumorphShapeDrawable].
         */
        override fun newDrawable(): Drawable {
            return NumorphShapeDrawable(
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
