package com.mya.games.colipop.board

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.mya.games.colipop.R
import com.mya.games.colipop.ResourceUtils

object BubbleResources {

    internal val TAG = "ColiPop"

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

    // Statuss de condicion de bubble
    var BUBBLE_STATUS_NONE = -1
    var BUBBLE_STATUS_TURQUESA = 0
    var BUBBLE_STATUS_VERDE = 1
    var BUBBLE_STATUS_AMARILLO = 2
    var BUBBLE_STATUS_ROJO = 3

    // Statuss de union between bubbles
    var BUBBLE_UNION_NONE = 0
    var BUBBLE_UNION_UP = 1
    var BUBBLE_UNION_DOWN = 2
    var BUBBLE_UNION_LEFT = 3
    var BUBBLE_UNION_RIGHT = 4

    // Statuss de movimiento
    var BUBBLE_MOVE_NONE = 0
    var BUBBLE_MOVE_UP = 1
    var BUBBLE_MOVE_DOWN = 2
    var BUBBLE_MOVE_LEFT = 3
    var BUBBLE_MOVE_RIGHT = 4

    // Bubble graphics
    var BUBBLE_GRAPHICS_SIZE = 4
    var BUBBLE_GRAPHICS_BITMAP: Array<Bitmap>
    var BUBBLE_ANIMATION_SEQUENCE = intArrayOf(1, 1, 2, 2, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    // Bubble graphics
    var BUBBLE_MOVE_GRAPHICS_SIZE = 4
    var BUBBLE_MOVE_GRAPHICS_BITMAP: Array<Bitmap>
    var BUBBLE_MOVE_ANIMATION_SEQUENCE = intArrayOf(0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3)

    /* Save memory
	// Bubble status graphics
	public static int BUBBLE_STATUS_GRAPHICS_SIZE = 5;
	public static Bitmap[] BUBBLE_STATUS_GRAPHICS_BITMAP;

	// Bubble union graphics
	public static int BUBBLE_UNION_GRAPHICS_SIZE = 5;
	public static Bitmap[] BUBBLE_UNION_GRAPHICS_BITMAP;
	*/

    fun initializeGraphics(resources: Resources) {
        // Bubbles en baja calidad
        val options = BitmapFactory.Options()
        options.inPurgeable = true
        options.inPreferredConfig = Bitmap.Config.ARGB_4444

        BUBBLE_GRAPHICS_SIZE = 4
        BUBBLE_GRAPHICS_BITMAP = arrayOfNulls(BUBBLE_GRAPHICS_SIZE)

        BUBBLE_GRAPHICS_BITMAP[0] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_01, options)
        BUBBLE_GRAPHICS_BITMAP[1] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_02, options)
        BUBBLE_GRAPHICS_BITMAP[2] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_03, options)
        BUBBLE_GRAPHICS_BITMAP[3] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_04, options)

        BUBBLE_MOVE_GRAPHICS_SIZE = 4
        BUBBLE_MOVE_GRAPHICS_BITMAP = arrayOfNulls(BUBBLE_MOVE_GRAPHICS_SIZE)

        BUBBLE_MOVE_GRAPHICS_BITMAP[0] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_01, options)
        BUBBLE_MOVE_GRAPHICS_BITMAP[1] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_02, options)
        BUBBLE_MOVE_GRAPHICS_BITMAP[2] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_03, options)
        BUBBLE_MOVE_GRAPHICS_BITMAP[3] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_04, options)

        /* Save memory
    	BUBBLE_STATUS_GRAPHICS_SIZE = 4;
    	BUBBLE_STATUS_GRAPHICS_BITMAP = new Bitmap[BUBBLE_STATUS_GRAPHICS_SIZE];

    	BUBBLE_STATUS_GRAPHICS_BITMAP[0] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_fondo_turquesa);
    	BUBBLE_STATUS_GRAPHICS_BITMAP[1] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_fondo_verde);
    	BUBBLE_STATUS_GRAPHICS_BITMAP[2] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_fondo_amarillo);
    	BUBBLE_STATUS_GRAPHICS_BITMAP[3] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_fondo_rojo);

    	BUBBLE_UNION_GRAPHICS_SIZE = 5;
    	BUBBLE_UNION_GRAPHICS_BITMAP = new Bitmap[BUBBLE_UNION_GRAPHICS_SIZE];

    	BUBBLE_UNION_GRAPHICS_BITMAP[0] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_01);
    	BUBBLE_UNION_GRAPHICS_BITMAP[1] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_union_up);
    	BUBBLE_UNION_GRAPHICS_BITMAP[2] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_union_down);
    	BUBBLE_UNION_GRAPHICS_BITMAP[3] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_union_right);
    	BUBBLE_UNION_GRAPHICS_BITMAP[4] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_union_left);
    	*/

    }

    fun resizeGraphics(refactorIndex: Float) {
        var refactorIndex = refactorIndex
        // Prevencion de cosas raras
        if (refactorIndex == 0f) {
            refactorIndex = 1f
        }

        // Cogemos el valor por debajo
        val width = java.lang.Float.valueOf(DEFAULT_BUBBLE_WIDTH * refactorIndex)!!.toInt()
        val height = java.lang.Float.valueOf(DEFAULT_BUBBLE_HEIGHT * refactorIndex)!!.toInt()
        val pixel_move = java.lang.Float.valueOf(DEFAULT_BUBBLE_PIXEL_MOVE * refactorIndex)!!.toInt()

        BUBBLE_WIDTH = width
        BUBBLE_HEIGHT = height
        BUBBLE_WIDTH_MEDIOS = width / 2
        BUBBLE_HEIGHT_MEDIOS = height / 2
        BUBBLE_PIXEL_MOVE = pixel_move

        var i = 0
        for (bitmap in BUBBLE_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                BUBBLE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            }
            i++
        }

        i = 0
        for (bitmap in BUBBLE_MOVE_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                BUBBLE_MOVE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            }
            i++
        }
        /* Save memory
    	i = 0;
    	for ( Bitmap bitmap : BUBBLE_STATUS_GRAPHICS_BITMAP ) {
    		if ( bitmap != null ) {
    			BUBBLE_STATUS_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
    		}
    		i++;
    	}

    	i = 0;
    	for ( Bitmap bitmap : BUBBLE_UNION_GRAPHICS_BITMAP ) {
    		if ( bitmap != null ) {
    			BUBBLE_UNION_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
    		}
    		i++;
    	}
    	*/
    }

    fun destroy() {
        ResourceUtils.recicleBitmaps(BUBBLE_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(BUBBLE_MOVE_GRAPHICS_BITMAP)
        /* Save memory
    	ResourceUtils.recicleBitmaps(BUBBLE_STATUS_GRAPHICS_BITMAP);
    	ResourceUtils.recicleBitmaps(BUBBLE_UNION_GRAPHICS_BITMAP);
    	*/
    }
}
