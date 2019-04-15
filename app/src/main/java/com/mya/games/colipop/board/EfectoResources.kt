package com.mya.games.colipop.board

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.mya.games.colipop.R
import com.mya.games.colipop.ResourceUtils

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

    var EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP: Array<Bitmap>
    /* Save memory
    public static Bitmap[] EFECTO_CPU_TOUCH_GRAPHICS_BITMAP;
    */
    var EFECTO_BLOQUEO_GRAPHICS_BITMAP: Array<Bitmap>
    var EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP: Array<Bitmap>
    var EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP: Array<Bitmap>
    var EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP: Array<Bitmap>

    var EFECTO_PLAYER_TOUCH_OBJECT_TYPE = 100
    var EFECTO_CPU_TOUCH_OBJECT_TYPE = 101
    var EFECTO_BLOQUEO_OBJECT_TYPE = 102
    var EFECTO_CPU_MOVE_RIGHT_OBJECT_TYPE = 103
    var EFECTO_CPU_MOVE_LEFT_OBJECT_TYPE = 104
    var EFECTO_PLAYER_MOVE_RIGHT_OBJECT_TYPE = 105
    var EFECTO_PLAYER_MOVE_LEFT_OBJECT_TYPE = 106
    var EFECTO_TOUCH_MOVE_OBJECT_TYPE = 107

    var EFECTO_FIJO_ANIMATION_SEQUENCE = intArrayOf(0, 1, 2, 3)

    fun initializeGraphics(resources: Resources) {

        // Efectos en baja calidad
        val options = BitmapFactory.Options()
        options.inPurgeable = true
        options.inPreferredConfig = Bitmap.Config.ARGB_4444

        EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_01, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_02, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_03, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_04, options))
        /* Save memory
        Bitmap efecto_cpu_touch_bubble = BitmapFactory.decodeResource(resources, R.drawable.efecto_cpu_touch_bubble);
    	EFECTO_CPU_TOUCH_GRAPHICS_BITMAP = new Bitmap[] {
    			efecto_cpu_touch_bubble,
    			efecto_cpu_touch_bubble,
    			efecto_cpu_touch_bubble,
    			efecto_cpu_touch_bubble
        };
        */

        EFECTO_BLOQUEO_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.efecto_bloqueo_burbuja_01, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_bloqueo_burbuja_02, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_bloqueo_burbuja_03, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_bloqueo_burbuja_04, options))

        EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_01, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_02, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_03, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_04, options))

        EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_left_burbuja_01, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_left_burbuja_02, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_left_burbuja_03, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_left_burbuja_04, options))

        EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_right_burbuja_01, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_right_burbuja_02, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_right_burbuja_03, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_right_burbuja_04, options))

        EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.efecto_touch_move_01, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_touch_move_02, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_touch_move_03, options), BitmapFactory.decodeResource(resources, R.drawable.efecto_touch_move_04, options))

    }

    fun resizeGraphics(refactorIndex: Float) {
        var refactorIndex = refactorIndex

        // Prevencin de cosas raras
        if (refactorIndex == 0f) {
            refactorIndex = 1f
        }

        // Cogemos el valor por debajo
        val width = java.lang.Float.valueOf(DEFAULT_EFECTO_WIDTH * refactorIndex).toInt()
        val height = java.lang.Float.valueOf(DEFAULT_EFECTO_HEIGHT * refactorIndex).toInt()
        val pixel_move = java.lang.Float.valueOf(DEFAULT_EFECTO_PIXEL_MOVE * refactorIndex).toInt()

        EFECTO_WIDTH = width
        EFECTO_HEIGHT = height
        EFECTO_PIXEL_MOVE = pixel_move

        var i = 0
        for (bitmap in EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            }
            i++
        }
        /* Save memory
    	i = 0;
    	for ( Bitmap bitmap : EFECTO_CPU_TOUCH_GRAPHICS_BITMAP ) {
    		if ( bitmap != null ) {
    			EFECTO_CPU_TOUCH_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
    		}
    		i++;
    	}
    	*/

        i = 0
        for (bitmap in EFECTO_BLOQUEO_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                EFECTO_BLOQUEO_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            }
            i++
        }

        i = 0
        for (bitmap in EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            }
            i++
        }

        i = 0
        for (bitmap in EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            }
            i++
        }

        i = 0
        for (bitmap in EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            }
            i++
        }
    }

    fun destroy() {
        ResourceUtils.recicleBitmaps(EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP)
        /* Save memory
        ResourceUtils.recicleBitmaps(EFECTO_CPU_TOUCH_GRAPHICS_BITMAP);
        */
        ResourceUtils.recicleBitmaps(EFECTO_BLOQUEO_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP)
    }

}
