package com.mya.games.colipop

import android.graphics.Bitmap

object ResourceUtils {

    fun recicleBitmaps(bitmaps: Array<Bitmap>?) {
        if (bitmaps == null) {
            return
        }
        for (bitmap in bitmaps) {
            bitmap?.recycle()
        }
    }

}
