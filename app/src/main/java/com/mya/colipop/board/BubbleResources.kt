package com.mya.colipop.board

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.mya.colipop.R
import com.mya.colipop.ResourceUtils

/**
 * Bubble object resources.
 */
object BubbleResources {

    private val TAG = "ColiPop"

    // Bubble baseline size
    var DEFAULT_BUBBLE_WIDTH = 32
    var DEFAULT_BUBBLE_HEIGHT = 32
    var DEFAULT_BUBBLE_PIXEL_MOVE = 4

    // Bubble size
    var BUBBLE_WIDTH = DEFAULT_BUBBLE_WIDTH
    var BUBBLE_HEIGHT = DEFAULT_BUBBLE_HEIGHT
    var BUBBLE_WIDTH_MEDIOS = BUBBLE_WIDTH / 2
    var BUBBLE_HEIGHT_MEDIOS = BUBBLE_HEIGHT / 2
    var BUBBLE_PIXEL_MOVE = DEFAULT_BUBBLE_PIXEL_MOVE

    // Status de movimiento
    var BUBBLE_MOVE_NONE = 0
    var BUBBLE_MOVE_UP = 1
    var BUBBLE_MOVE_DOWN = 2
    var BUBBLE_MOVE_LEFT = 3
    var BUBBLE_MOVE_RIGHT = 4
    var BUBBLE_MOVE_TILT = 5

    // Bubble graphics
    var BUBBLE_GRAPHICS_SIZE = 4
    lateinit var BUBBLE_GRAPHICS_BITMAP: Array<Bitmap>
    var BUBBLE_ANIMATION_SEQUENCE = intArrayOf(1, 1, 2, 2, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    // Bubble graphics
    var BUBBLE_MOVE_GRAPHICS_SIZE = 4
    lateinit var BUBBLE_MOVE_GRAPHICS_BITMAP: Array<Bitmap>
    var BUBBLE_MOVE_ANIMATION_SEQUENCE = intArrayOf(0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3)

    fun initializeGraphics(resources: Resources) {
        // Low quality bubbles
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_4444

        BUBBLE_GRAPHICS_SIZE = 4
        BUBBLE_GRAPHICS_BITMAP = arrayOf(
                BitmapFactory.decodeResource(resources, R.drawable.burbuja_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.burbuja_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.burbuja_03, options),
                BitmapFactory.decodeResource(resources, R.drawable.burbuja_04, options))

        BUBBLE_MOVE_GRAPHICS_SIZE = 4
        BUBBLE_MOVE_GRAPHICS_BITMAP = arrayOf(
                BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_03, options),
                BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_04, options))
    }

    /**
     * Perform graphic resize to adapt to the device screen.
     */
    fun resizeGraphics(refactorIndex: Float) {
        var normRefactorIndex = refactorIndex
        if (normRefactorIndex == 0f) {
            normRefactorIndex = 1f
        }

        val width = java.lang.Float.valueOf(DEFAULT_BUBBLE_WIDTH * normRefactorIndex).toInt()
        val height = java.lang.Float.valueOf(DEFAULT_BUBBLE_HEIGHT * normRefactorIndex).toInt()
        val pixelMove = java.lang.Float.valueOf(DEFAULT_BUBBLE_PIXEL_MOVE * normRefactorIndex).toInt()

        BUBBLE_WIDTH = width
        BUBBLE_HEIGHT = height
        BUBBLE_WIDTH_MEDIOS = width / 2
        BUBBLE_HEIGHT_MEDIOS = height / 2
        BUBBLE_PIXEL_MOVE = pixelMove

        var i = 0
        for (bitmap in BUBBLE_GRAPHICS_BITMAP) {
            BUBBLE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }

        i = 0
        for (bitmap in BUBBLE_MOVE_GRAPHICS_BITMAP) {
            BUBBLE_MOVE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            i++
        }
    }

    fun destroy() {
        ResourceUtils.recycleBitmaps(BUBBLE_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(BUBBLE_MOVE_GRAPHICS_BITMAP)
    }
}
