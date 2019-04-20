package com.mya.colipop.board

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.mya.colipop.R
import com.mya.colipop.ResourceUtils

/**
 * Efecto object resources.
 */
object EfectoResources {

    private val TAG = "ColiPop"

    // Bubble baseline size
    var DEFAULT_EFECTO_WIDTH = 32
    var DEFAULT_EFECTO_HEIGHT = 32
    var DEFAULT_EFECTO_PIXEL_MOVE = 0

    // Bubble size
    var EFECTO_WIDTH = DEFAULT_EFECTO_WIDTH
    var EFECTO_HEIGHT = DEFAULT_EFECTO_HEIGHT
    var EFECTO_PIXEL_MOVE = DEFAULT_EFECTO_PIXEL_MOVE

    lateinit var EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var EFECTO_BLOQUEO_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP: Array<Bitmap>

    var EFECTO_PLAYER_TOUCH_OBJECT_TYPE = 100
    var EFECTO_BLOQUEO_OBJECT_TYPE = 102
    var EFECTO_PLAYER_MOVE_RIGHT_OBJECT_TYPE = 105
    var EFECTO_PLAYER_MOVE_LEFT_OBJECT_TYPE = 106
    var EFECTO_TOUCH_MOVE_OBJECT_TYPE = 107

    var EFECTO_FIJO_ANIMATION_SEQUENCE = intArrayOf(0, 1, 2, 3)

    fun initializeGraphics(resources: Resources) {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_4444

        EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_01, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_02, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_03, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_04, options))
        EFECTO_BLOQUEO_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.efecto_bloqueo_burbuja_01, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_bloqueo_burbuja_02, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_bloqueo_burbuja_03, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_bloqueo_burbuja_04, options))
        EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_01, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_02, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_03, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_04, options))
        EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_left_burbuja_01, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_left_burbuja_02, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_left_burbuja_03, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_left_burbuja_04, options))
        EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_right_burbuja_01, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_right_burbuja_02, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_right_burbuja_03, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_right_burbuja_04, options))
        EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.efecto_touch_move_01, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_touch_move_02, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_touch_move_03, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_touch_move_04, options))
    }

    /**
     * Perform graphic resize to adapt to the device screen.
     */
    fun resizeGraphics(refactorIndex: Float) {
        var normRefactorIndex = refactorIndex
        if (normRefactorIndex == 0f) {
            normRefactorIndex = 1f
        }

        val width = java.lang.Float.valueOf(DEFAULT_EFECTO_WIDTH * normRefactorIndex).toInt()
        val height = java.lang.Float.valueOf(DEFAULT_EFECTO_HEIGHT * normRefactorIndex).toInt()
        val pixel_move = java.lang.Float.valueOf(DEFAULT_EFECTO_PIXEL_MOVE * normRefactorIndex).toInt()

        EFECTO_WIDTH = width
        EFECTO_HEIGHT = height
        EFECTO_PIXEL_MOVE = pixel_move

        var i = 0
        for (bitmap in EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP) {
            EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }

        i = 0
        for (bitmap in EFECTO_BLOQUEO_GRAPHICS_BITMAP) {
            EFECTO_BLOQUEO_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }

        i = 0
        for (bitmap in EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP) {
            EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }

        i = 0
        for (bitmap in EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP) {
            EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }

        i = 0
        for (bitmap in EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP) {
            EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }
    }

    /**
     * Perform graphic recycle.
     */
    fun destroy() {
        ResourceUtils.recycleBitmaps(EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(EFECTO_BLOQUEO_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP)
    }
}
