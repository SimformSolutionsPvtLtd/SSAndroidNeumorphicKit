package com.simformsolutions.numorphic.annotation

import androidx.annotation.IntDef
import androidx.annotation.RestrictTo

/**
 * Represents corner family of the SSNeumorphicShape.
 *      - Rounded
 *      - Oval
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@IntDef(
    SSNeumorphicCornerFamily.ROUNDED,
    SSNeumorphicCornerFamily.OVAL
)
@Retention(AnnotationRetention.SOURCE)
annotation class SSNeumorphicCornerFamily {
    companion object {
        const val ROUNDED = 0
        const val OVAL = 1
    }
}
