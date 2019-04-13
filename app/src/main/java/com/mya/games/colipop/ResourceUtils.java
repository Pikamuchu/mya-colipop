package com.mya.games.colipop;

import android.graphics.Bitmap;

public class ResourceUtils {

    public static void recicleBitmaps(Bitmap[] bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bitmap : bitmaps) {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

}
