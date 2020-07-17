package com.simformsolutions.numorphic.annotation

import androidx.annotation.IntDef
import androidx.annotation.RestrictTo

/**
 * Represents corner family of the Shape.
 *      - Rounded
 *      - Oval
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@IntDef(
    CornerFamily.ROUNDED,
    CornerFamily.OVAL
)
@Retention(AnnotationRetention.SOURCE)
annotation class CornerFamily {
    companion object {
        const val ROUNDED = 0
        const val OVAL = 1
    }
}
