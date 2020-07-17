package com.simformsolutions.numorphic.util

import android.graphics.Canvas
import android.graphics.Path

/**
 * Set the clip to the difference of the current clip and the specified path.
 * @param clipPath The path used in the operation.
 * @param block Block to run.
 */
internal inline fun Canvas.withClipOut(
    clipPath: Path,
    block: Canvas.() -> Unit
) {
    CanvasCompat.clipOutPath(this, clipPath)
    block()
}
