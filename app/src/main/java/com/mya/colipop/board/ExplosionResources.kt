package com.mya.colipop.board

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.mya.colipop.R
import com.mya.colipop.ResourceUtils

object ExplosionResources {

    private val TAG = "ColiPop"

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
    lateinit var EXPLOSION_GRAPHICS_BITMAP: Array<Bitmap>

    fun initializeGraphics(resources: Resources) {

        // Explosiones en baja calidad
        val options = BitmapFactory.Options()
        options.inPurgeable = true
        options.inPreferredConfig = Bitmap.Config.ARGB_4444

        EXPLOSION_GRAPHICS_SIZE = 4
        EXPLOSION_GRAPHICS_BITMAP = arrayOf(
                BitmapFactory.decodeResource(resources, R.drawable.explosion_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.explosion_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.explosion_03, options),
                BitmapFactory.decodeResource(resources, R.drawable.explosion_04, options))

        EXPLOSION_PIXEL_MOVE = 0

        EXPLOSION_WIDTH = EXPLOSION_GRAPHICS_BITMAP[0].width
        EXPLOSION_HEIGHT = EXPLOSION_GRAPHICS_BITMAP[0].height

    }

    fun resizeGraphics(refactorIndex: Float) {
        var normRefactorIndex = refactorIndex

        // Prevencin de cosas raras
        if (normRefactorIndex == 0f) {
            normRefactorIndex = 1f
        }

        // Cogemos el valor por debajo
        val width = java.lang.Float.valueOf(DEFAULT_EXPLOSION_WIDTH * normRefactorIndex).toInt()
        val height = java.lang.Float.valueOf(DEFAULT_EXPLOSION_HEIGHT * normRefactorIndex).toInt()
        val pixel_move = java.lang.Float.valueOf(DEFAULT_EXPLOSION_PIXEL_MOVE * normRefactorIndex).toInt()

        EXPLOSION_WIDTH = width
        EXPLOSION_HEIGHT = height
        EXPLOSION_PIXEL_MOVE = pixel_move

        var i = 0
        for (bitmap in EXPLOSION_GRAPHICS_BITMAP) {
            EXPLOSION_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }

    }

    fun destroy() {
        ResourceUtils.recycleBitmaps(EXPLOSION_GRAPHICS_BITMAP)
    }
}
