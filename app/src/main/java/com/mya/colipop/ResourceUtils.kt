package com.mya.colipop

import android.graphics.Bitmap

/**
 * Colipop resources utilities.
 */
object ResourceUtils {

    fun recycleBitmaps(bitmaps: Array<Bitmap>?) {
        if (bitmaps == null) {
            return
        }
        for (bitmap in bitmaps) {
            bitmap.recycle()
        }
    }
}
