package com.simformsolutions.numorphic.annotation

import androidx.annotation.IntDef
import androidx.annotation.RestrictTo

/**
 * Represents shape type of the Shape.
 *      - Flat
 *      - Pressed
 *      - Basin
 *
 * Default is Flat.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@IntDef(
    ShapeType.FLAT,
    ShapeType.PRESSED,
    ShapeType.BASIN
)
@Retention(AnnotationRetention.SOURCE)
annotation class ShapeType {
    companion object {
        const val FLAT = 0
        const val PRESSED = 1
        const val BASIN = 2

        const val DEFAULT =
            FLAT
    }
}
