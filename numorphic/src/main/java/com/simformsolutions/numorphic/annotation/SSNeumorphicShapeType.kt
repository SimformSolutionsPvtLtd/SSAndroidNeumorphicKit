package com.simformsolutions.numorphic.annotation

import androidx.annotation.IntDef
import androidx.annotation.RestrictTo

/**
 * Represents shape type of the SSNeumorphicShape.
 *      - Flat
 *      - Pressed
 *      - Basin
 *
 * Default is Flat.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@IntDef(
    SSNeumorphicShapeType.FLAT,
    SSNeumorphicShapeType.PRESSED,
    SSNeumorphicShapeType.BASIN
)
@Retention(AnnotationRetention.SOURCE)
annotation class SSNeumorphicShapeType {
    companion object {
        const val FLAT = 0
        const val PRESSED = 1
        const val BASIN = 2

        const val DEFAULT = FLAT
    }
}
