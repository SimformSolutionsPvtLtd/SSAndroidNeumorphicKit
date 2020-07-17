package com.simformsolutions.numorphic.util

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Region
import android.os.Build

internal object CanvasCompat {
    /**
     * Set the clip to the difference of the current clip and the specified path.
     * @param canvas
     * @param path The path used in the operation.
     */
    fun clipOutPath(canvas: Canvas, path: Path): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutPath(path)
        } else {
            canvas.clipPath(path, Region.Op.DIFFERENCE)
        }
    }
}
