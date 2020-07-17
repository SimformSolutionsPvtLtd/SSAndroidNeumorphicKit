package com.simformsolutions.numorphic.model

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.Dimension
import androidx.annotation.StyleRes
import com.simformsolutions.numorphic.R
import com.simformsolutions.numorphic.annotation.CornerFamily

/**
 * Holds data related to appearance of the shape.
 * Support:
 *      * Corner family
 *          - Rounded
 *          - Oval
 *      * Corner size
 */
class NumorphShapeAppearanceModel {
    @CornerFamily
    val cornerFamily: Int

    @Dimension
    val cornerRadius: Float

    @Dimension
    val cornerRadiusTopLeft: Float

    @Dimension
    val cornerRadiusTopRight: Float

    @Dimension
    val cornerRadiusBottomRight: Float

    @Dimension
    val cornerRadiusBottomLeft: Float

    /**
     * Constructor which accepts the [NumorphShapeAppearanceModel.Builder].
     * @param builder [NumorphShapeAppearanceModel.Builder]
     */
    private constructor(builder: Builder) {
        cornerFamily = builder.cornerFamily
        cornerRadius = builder.cornerRadius
        cornerRadiusTopLeft = builder.cornerRadiusTopLeft
        cornerRadiusTopRight = builder.cornerRadiusTopRight
        cornerRadiusBottomRight = builder.cornerRadiusBottomRight
        cornerRadiusBottomLeft = builder.cornerRadiusBottomLeft
    }

    /**
     * The default constructor. Sets default values.
     */
    constructor() {
        cornerFamily = DEFAULT_CORNER_FAMILY
        cornerRadius = DEFAULT_CORNER_RADIUS
        cornerRadiusTopLeft = cornerRadius
        cornerRadiusTopRight = cornerRadius
        cornerRadiusBottomRight = cornerRadius
        cornerRadiusBottomLeft = cornerRadius
    }

    companion object {
        const val DEFAULT_CORNER_FAMILY = CornerFamily.ROUNDED
        const val DEFAULT_CORNER_RADIUS = 0f

        /**
         * @return [NumorphShapeAppearanceModel.Builder].
         */
        fun builder(): Builder {
            return Builder()
        }

        /**
         * Create builder from
         * @param context Context
         * @param attrs AttributeSet?
         * @param defStyleAttr Default style attribute
         * @param defStyleRes Default style resource
         *
         * @return [NumorphShapeAppearanceModel.Builder]
         */
        fun builder(
            context: Context,
            attrs: AttributeSet?,
            @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int,
            defaultCornerRadius: Float = DEFAULT_CORNER_RADIUS
        ): Builder {
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.NumorphShape, defStyleAttr, defStyleRes
            )
            val shapeAppearanceResId = a.getResourceId(
                R.styleable.NumorphShape_numorph_shapeAppearance, 0
            )
            a.recycle()
            return builder(
                context,
                shapeAppearanceResId,
                defaultCornerRadius
            )
        }

        /**
         * Create builder from
         * @param context Context
         * @param shapeAppearanceResId Shape appearance resource ID
         * @param defaultCornerRadius Default corner radius
         *
         * @return [NumorphShapeAppearanceModel.Builder]
         */
        private fun builder(
            context: Context,
            @StyleRes shapeAppearanceResId: Int,
            defaultCornerRadius: Float
        ): Builder {
            val a = context.obtainStyledAttributes(
                shapeAppearanceResId,
                R.styleable.NumorphShapeAppearance
            )
            try {
                val cornerFamily = a.getInt(
                    R.styleable.NumorphShapeAppearance_numorph_cornerFamily,
                    CornerFamily.ROUNDED
                )
                val cornerRadius =
                    getCornerSize(
                        a,
                        R.styleable.NumorphShapeAppearance_numorph_cornerRadius,
                        defaultCornerRadius
                    )
                val cornerRadiusTopLeft =
                    getCornerSize(
                        a,
                        R.styleable.NumorphShapeAppearance_numorph_cornerRadiusTopLeft,
                        cornerRadius
                    )
                val cornerRadiusTopRight =
                    getCornerSize(
                        a,
                        R.styleable.NumorphShapeAppearance_numorph_cornerRadiusTopRight,
                        cornerRadius
                    )
                val cornerRadiusBottomRight =
                    getCornerSize(
                        a,
                        R.styleable.NumorphShapeAppearance_numorph_cornerRadiusBottomRight,
                        cornerRadius
                    )
                val cornerRadiusBottomLeft =
                    getCornerSize(
                        a,
                        R.styleable.NumorphShapeAppearance_numorph_cornerRadiusBottomLeft,
                        cornerRadius
                    )

                return Builder()
                    .set(
                        cornerFamily = cornerFamily,
                        cornerRadius = cornerRadius,
                        cornerRadiusTopLeft = cornerRadiusTopLeft,
                        cornerRadiusTopRight = cornerRadiusTopRight,
                        cornerRadiusBottomRight = cornerRadiusBottomRight,
                        cornerRadiusBottomLeft = cornerRadiusBottomLeft
                    )
            } finally {
                a.recycle()
            }
        }

        /**
         * Get corner size from
         * @param a TypedArray
         * @param index Index in [a]
         * @param defaultValue Default corner size
         */
        private fun getCornerSize(
            a: TypedArray, index: Int, defaultValue: Float
        ): Float {
            val value = a.peekValue(index) ?: return defaultValue
            return if (value.type == TypedValue.TYPE_DIMENSION) {
                TypedValue.complexToDimensionPixelSize(
                    value.data, a.resources.displayMetrics
                ).toFloat()
            } else {
                defaultValue
            }
        }
    }

    /**
     * Builder to create [NumorphShapeAppearanceModel].
     */
    class Builder {

        @CornerFamily
        var cornerFamily: Int = DEFAULT_CORNER_FAMILY

        @Dimension
        var cornerRadius: Float = DEFAULT_CORNER_RADIUS

        @Dimension
        var cornerRadiusTopLeft: Float = cornerRadius

        @Dimension
        var cornerRadiusTopRight: Float = cornerRadius

        @Dimension
        var cornerRadiusBottomRight: Float = cornerRadius

        @Dimension
        var cornerRadiusBottomLeft: Float = cornerRadius

        /**
         * Set
         *      * corner family
         *      * corner radius
         *      * corner radius top-left
         *      * corner radius top-right
         *      * corner radius bottom-right
         *      * corner radius bottom-left
         *
         * @return This Builder
         */
        fun set(
            @CornerFamily cornerFamily: Int,
            @Dimension cornerRadius: Float = DEFAULT_CORNER_RADIUS,
            @Dimension cornerRadiusTopLeft: Float = cornerRadius,
            @Dimension cornerRadiusTopRight: Float = cornerRadius,
            @Dimension cornerRadiusBottomRight: Float = cornerRadius,
            @Dimension cornerRadiusBottomLeft: Float = cornerRadius
        ): Builder {
            return setCornerFamily(cornerFamily)
                .setCorner(
                    cornerRadius,
                    cornerRadiusTopLeft,
                    cornerRadiusTopRight,
                    cornerRadiusBottomRight,
                    cornerRadiusBottomLeft
                )
        }

        /**
         * Set corner family.
         */
        fun setCornerFamily(@CornerFamily cornerFamily: Int): Builder {
            this.cornerFamily = cornerFamily
            return this
        }

        /**
         * Set all corner radius or specific corner radius.
         * @return This Builder.
         */
        fun setCorner(
            @Dimension cornerRadius: Float = DEFAULT_CORNER_RADIUS,
            @Dimension cornerRadiusTopLeft: Float = cornerRadius,
            @Dimension cornerRadiusTopRight: Float = cornerRadius,
            @Dimension cornerRadiusBottomRight: Float = cornerRadius,
            @Dimension cornerRadiusBottomLeft: Float = cornerRadius
        ): Builder {
            this.cornerRadius = cornerRadius
            this.cornerRadiusTopLeft = cornerRadiusTopLeft
            this.cornerRadiusTopRight = cornerRadiusTopRight
            this.cornerRadiusBottomRight = cornerRadiusBottomRight
            this.cornerRadiusBottomLeft = cornerRadiusBottomLeft
            return this
        }

        /**
         * Build and return the [NumorphShapeAppearanceModel].
         */
        fun build(): NumorphShapeAppearanceModel = NumorphShapeAppearanceModel(this)
    }
}