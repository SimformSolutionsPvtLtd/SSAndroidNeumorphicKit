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

    /**
     * Builder to create [NumorphShapeAppearanceModel].
     */
    class Builder {

        @CornerFamily
        var cornerFamily: Int = CornerFamily.ROUNDED
        @Dimension
        var cornerSize: Float = 0f

        /**  Set corner family and corner size  */
        fun setAllCorners(
            @CornerFamily cornerFamily: Int,
            @Dimension cornerSize: Float
        ): Builder {
            return setAllCorners(cornerFamily)
                .setAllCornerSizes(cornerSize)
        }

        /**  Set corner family  */
        fun setAllCorners(@CornerFamily cornerFamily: Int): Builder {
            return apply {
                this.cornerFamily = cornerFamily
            }
        }

        /**  Set corner size  */
        fun setAllCornerSizes(cornerSize: Float): Builder {
            return apply {
                this.cornerSize = cornerSize
            }
        }

        /**
         * Build and return the [NumorphShapeAppearanceModel].
         */
        fun build(): NumorphShapeAppearanceModel {
            return NumorphShapeAppearanceModel(
                this
            )
        }
    }

    @CornerFamily
    private val cornerFamily: Int
    @Dimension
    private val cornerSize: Float

    /**
     * Constructor which accepts the [NumorphShapeAppearanceModel.Builder].
     * @param builder [NumorphShapeAppearanceModel.Builder]
     */
    private constructor(builder: Builder) {
        cornerFamily = builder.cornerFamily
        cornerSize = builder.cornerSize
    }

    /**
     * The default constructor. Sets default values.
     */
    constructor() {
        cornerFamily = CornerFamily.ROUNDED
        cornerSize = 0f
    }

    /**
     * Getter for [cornerFamily].
     */
    @CornerFamily
    fun getCornerFamily(): Int {
        return cornerFamily
    }

    /**
     * Getter for [cornerSize].
     */
    fun getCornerSize(): Float {
        return cornerSize
    }

    companion object {

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
            defaultCornerSize: Float = 0f
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
                defaultCornerSize
            )
        }

        /**
         * Create builder from
         * @param context Context
         * @param shapeAppearanceResId Shape appearance resource ID
         * @param defaultCornerSize Default corner size
         *
         * @return [NumorphShapeAppearanceModel.Builder]
         */
        private fun builder(
            context: Context,
            @StyleRes shapeAppearanceResId: Int,
            defaultCornerSize: Float
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
                val cornerSize =
                    getCornerSize(
                        a,
                        R.styleable.NumorphShapeAppearance_numorph_cornerSize,
                        defaultCornerSize
                    )
                return Builder()
                    .setAllCorners(cornerFamily, cornerSize)
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
}