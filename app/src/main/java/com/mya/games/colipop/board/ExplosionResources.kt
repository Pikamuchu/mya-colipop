package com.mya.games.colipop.board

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.mya.games.colipop.R
import com.mya.games.colipop.ResourceUtils

object ExplosionResources {

    internal val TAG = "ColiPop"

    // Explosion baseline size
    var DEFAULT_EXPLOSION_WIDTH = 64
    var DEFAULT_EXPLOSION_HEIGHT = 64
    var DEFAULT_EXPLOSION_PIXEL_MOVE = 0

    // Explosion size
    var EXPLOSION_WIDTH = DEFAULT_EXPLOSION_WIDTH
    var EXPLOSION_HEIGHT = DEFAULT_EXPLOSION_HEIGHT
    var EXPLOSION_PIXEL_MOVE = DEFAULT_EXPLOSION_PIXEL_MOVE

    // Explosion graphics & animation properties
    var EXPLOSION_GRAPHICS_SIZE = 4
    var EXPLOSION_GRAPHICS_BITMAP: Array<Bitmap>

    fun initializeGraphics(resources: Resources) {

        // Explosiones en baja calidad
        val options = BitmapFactory.Options()
        options.inPurgeable = true
        options.inPreferredConfig = Bitmap.Config.ARGB_4444

        EXPLOSION_GRAPHICS_SIZE = 4
        EXPLOSION_GRAPHICS_BITMAP = arrayOfNulls(EXPLOSION_GRAPHICS_SIZE)

        EXPLOSION_GRAPHICS_BITMAP[0] = BitmapFactory.decodeResource(resources, R.drawable.explosion_01, options)
        EXPLOSION_GRAPHICS_BITMAP[1] = BitmapFactory.decodeResource(resources, R.drawable.explosion_02, options)
        EXPLOSION_GRAPHICS_BITMAP[2] = BitmapFactory.decodeResource(resources, R.drawable.explosion_03, options)
        EXPLOSION_GRAPHICS_BITMAP[3] = BitmapFactory.decodeResource(resources, R.drawable.explosion_04, options)

        EXPLOSION_PIXEL_MOVE = 0

        EXPLOSION_WIDTH = EXPLOSION_GRAPHICS_BITMAP[0].width
        EXPLOSION_HEIGHT = EXPLOSION_GRAPHICS_BITMAP[0].height

    }

    fun resizeGraphics(refactorIndex: Float) {
        var refactorIndex = refactorIndex

        // Prevencin de cosas raras
        if (refactorIndex == 0f) {
            refactorIndex = 1f
        }

        // Cogemos el valor por debajo
        val width = java.lang.Float.valueOf(DEFAULT_EXPLOSION_WIDTH * refactorIndex)!!.toInt()
        val height = java.lang.Float.valueOf(DEFAULT_EXPLOSION_HEIGHT * refactorIndex)!!.toInt()
        val pixel_move = java.lang.Float.valueOf(DEFAULT_EXPLOSION_PIXEL_MOVE * refactorIndex)!!.toInt()

        EXPLOSION_WIDTH = width
        EXPLOSION_HEIGHT = height
        EXPLOSION_PIXEL_MOVE = pixel_move

        var i = 0
        for (bitmap in EXPLOSION_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                EXPLOSION_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            }
            i++
        }

    }

    fun destroy() {
        ResourceUtils.recicleBitmaps(EXPLOSION_GRAPHICS_BITMAP)
        //EXPLOSION_GRAPHICS_BITMAP=null;
    }

}