package com.mya.colipop.board

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.mya.colipop.R
import com.mya.colipop.ResourceUtils

/**
 * Thing object resources.
 */
object ThingResources {

    private val TAG = "ColiPop"

    // Thing baseline size
    var DEFAULT_THING_WIDTH = 32
    var DEFAULT_THING_HEIGHT = 32
    var DEFAULT_THING_PIXEL_MOVE = 4
    var DEFAULT_THING_GRANDE_WIDTH = 64
    var DEFAULT_THING_GRANDE_HEIGHT = 64
    var DEFAULT_THING_GRANDE_PIXEL_MOVE = 8

    // Thing size
    var THING_WIDTH = DEFAULT_THING_WIDTH
    var THING_HEIGHT = DEFAULT_THING_HEIGHT
    var THING_WIDTH_MEDIO = THING_WIDTH / 2
    var THING_HEIGHT_MEDIO = THING_HEIGHT / 2
    var THING_PIXEL_MOVE = DEFAULT_THING_PIXEL_MOVE
    var THING_GRANDE_WIDTH = DEFAULT_THING_GRANDE_WIDTH
    var THING_GRANDE_HEIGHT = DEFAULT_THING_GRANDE_HEIGHT
    var THING_GRANDE_PIXEL_MOVE = DEFAULT_THING_GRANDE_PIXEL_MOVE

    // Thing status
    var THING_STATUS_BUBBLE = 0
    var THING_STATUS_EXPLODE = 1
    var THING_STATUS_MOVIDO = 2
    var THING_STATUS_REMOVED = 3
    var THING_STATUS_EFECTO_FIJO = 4

    var THING_ANIMATION_SEQUENCE = intArrayOf(0)

    var THING_REMOVED_GRAPHICS_SIZE = 4
    var THING_REMOVED_ANIMATION_SEQUENCE = intArrayOf(0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 0, 0, 1, 1, 2, 2, 3, 3, 0, 0, 1, 1, 2, 2, 3, 3, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3)

    var THING_GRAPHICS_SIZE = 1

    lateinit var CARAMELO_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var CARAMELO_GRANDE_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var PIRULETA_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var PIRULETA_GRANDE_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var RASPA_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var RASPA_GRANDE_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var PEINE_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var PEINE_GRANDE_GRAPHICS_BITMAP: Array<Bitmap>

    var CARAMELO_PORCENTAJE = 12
    var PIRULETA_PORCENTAJE = 12
    var RASPA_PORCENTAJE = 6
    var PEINE_PORCENTAJE = 6

    var CARAMELO_OBJECT_TYPE = 0
    var PIRULETA_OBJECT_TYPE = 1
    var RASPA_OBJECT_TYPE = 10
    var PEINE_OBJECT_TYPE = 11


    fun initializeGraphics(resources: Resources) {
        // Increase Si se anyaden mas bitmaps hay que subir esto
        THING_GRAPHICS_SIZE = 1
        CARAMELO_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.caramelo_01))
        PIRULETA_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.piruleta_01))
        RASPA_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.raspa_01))
        PEINE_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.peine_01))
        CARAMELO_GRANDE_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.caramelo_grande_01))
        PIRULETA_GRANDE_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.piruleta_grande_01))
        RASPA_GRANDE_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.raspa_grande_01))
        PEINE_GRANDE_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.peine_grande_01))
    }

    /**
     * Perform graphic resize to adapt to the device screen.
     */
    fun resizeGraphics(refactorIndex: Float) {
        var normRefactorIndex = refactorIndex

        // Prevencin de cosas raras
        if (normRefactorIndex == 0f) {
            normRefactorIndex = 1f
        }

        // Cogemos el valor por debajo
        var width = java.lang.Float.valueOf(DEFAULT_THING_WIDTH * normRefactorIndex).toInt()
        var height = java.lang.Float.valueOf(DEFAULT_THING_HEIGHT * normRefactorIndex).toInt()
        var pixel_move = java.lang.Float.valueOf(DEFAULT_THING_PIXEL_MOVE * normRefactorIndex).toInt()

        THING_WIDTH = width
        THING_HEIGHT = height
        THING_WIDTH_MEDIO = width / 2
        THING_HEIGHT_MEDIO = height / 2
        THING_PIXEL_MOVE = pixel_move

        var i = 0
        for (bitmap in CARAMELO_GRAPHICS_BITMAP) {
            CARAMELO_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }

        i = 0
        for (bitmap in PIRULETA_GRAPHICS_BITMAP) {
            PIRULETA_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }

        i = 0
        for (bitmap in RASPA_GRAPHICS_BITMAP) {
            RASPA_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }

        i = 0
        for (bitmap in PEINE_GRAPHICS_BITMAP) {
            PEINE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }

        // Cogemos el valor por debajo
        width = java.lang.Float.valueOf(DEFAULT_THING_GRANDE_WIDTH * normRefactorIndex).toInt()
        height = java.lang.Float.valueOf(DEFAULT_THING_GRANDE_HEIGHT * normRefactorIndex).toInt()
        pixel_move = java.lang.Float.valueOf(DEFAULT_THING_GRANDE_PIXEL_MOVE * normRefactorIndex).toInt()

        THING_GRANDE_WIDTH = width
        THING_GRANDE_HEIGHT = height
        THING_GRANDE_PIXEL_MOVE = pixel_move

        i = 0
        for (bitmap in CARAMELO_GRANDE_GRAPHICS_BITMAP) {
            CARAMELO_GRANDE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }

        i = 0
        for (bitmap in PIRULETA_GRANDE_GRAPHICS_BITMAP) {
            PIRULETA_GRANDE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }

        i = 0
        for (bitmap in RASPA_GRANDE_GRAPHICS_BITMAP) {
            RASPA_GRANDE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }

        i = 0
        for (bitmap in PEINE_GRANDE_GRAPHICS_BITMAP) {
            PEINE_GRANDE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }
    }

    /**
     * Perform graphic recycle.
     */
    fun destroy() {
        ResourceUtils.recycleBitmaps(CARAMELO_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(CARAMELO_GRANDE_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(PIRULETA_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(PIRULETA_GRANDE_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(RASPA_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(RASPA_GRANDE_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(PEINE_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(PEINE_GRANDE_GRAPHICS_BITMAP)
    }
}
