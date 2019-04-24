package com.mya.colipop

import android.graphics.Bitmap

/**
 * Colipop resources utilities.
 */
object ResourceUtils {

    /**
     * Performs bitmap resource recycle.
     */
    fun recycleBitmaps(bitmaps: Array<Bitmap>?) {
        if (bitmaps == null) {
            return
        }
        for (bitmap in bitmaps) {
            bitmap.recycle()
        }
    }
}
