package com.mya.games.colipop.board;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mya.games.colipop.R;
import com.mya.games.colipop.ResourceUtils;

public class EfectoResources {

    static final String TAG = "ColiPop";

    // Bubble baseline size
    public static int DEFAULT_EFECTO_WIDTH = 32;
    public static int DEFAULT_EFECTO_HEIGHT = 32;
    public static int DEFAULT_EFECTO_PIXEL_MOVE = 0;

    // Bubble size
    public static int EFECTO_WIDTH = DEFAULT_EFECTO_WIDTH;
    public static int EFECTO_HEIGHT = DEFAULT_EFECTO_HEIGHT;
    public static int EFECTO_PIXEL_MOVE = DEFAULT_EFECTO_PIXEL_MOVE;

    public static Bitmap[] EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP;
    /* Save memory
    public static Bitmap[] EFECTO_CPU_TOUCH_GRAPHICS_BITMAP;
    */
    public static Bitmap[] EFECTO_BLOQUEO_GRAPHICS_BITMAP;
    public static Bitmap[] EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP;
    public static Bitmap[] EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP;
    public static Bitmap[] EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP;

    public static int EFECTO_PLAYER_TOUCH_OBJECT_TYPE = 100;
    public static int EFECTO_CPU_TOUCH_OBJECT_TYPE = 101;
    public static int EFECTO_BLOQUEO_OBJECT_TYPE = 102;
    public static int EFECTO_CPU_MOVE_RIGHT_OBJECT_TYPE = 103;
    public static int EFECTO_CPU_MOVE_LEFT_OBJECT_TYPE = 104;
    public static int EFECTO_PLAYER_MOVE_RIGHT_OBJECT_TYPE = 105;
    public static int EFECTO_PLAYER_MOVE_LEFT_OBJECT_TYPE = 106;
    public static int EFECTO_TOUCH_MOVE_OBJECT_TYPE = 107;

    public static int[] EFECTO_FIJO_ANIMATION_SEQUENCE = new int[]{0, 1, 2, 3};

    public static void initializeGraphics(Resources resources) {

        // Efectos en baja calidad
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;

        EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_03, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_04, options)
        };
        /* Save memory
        Bitmap efecto_cpu_touch_bubble = BitmapFactory.decodeResource(resources, R.drawable.efecto_cpu_touch_bubble);
    	EFECTO_CPU_TOUCH_GRAPHICS_BITMAP = new Bitmap[] {
    			efecto_cpu_touch_bubble,
    			efecto_cpu_touch_bubble,
    			efecto_cpu_touch_bubble,
    			efecto_cpu_touch_bubble
        };
        */

        EFECTO_BLOQUEO_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.efecto_bloqueo_burbuja_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_bloqueo_burbuja_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_bloqueo_burbuja_03, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_bloqueo_burbuja_04, options)
        };

        EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_03, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_touch_burbuja_04, options)
        };

        EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_left_burbuja_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_left_burbuja_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_left_burbuja_03, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_left_burbuja_04, options)
        };

        EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_right_burbuja_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_right_burbuja_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_right_burbuja_03, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_player_move_right_burbuja_04, options)
        };

        EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.efecto_touch_move_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_touch_move_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_touch_move_03, options),
                BitmapFactory.decodeResource(resources, R.drawable.efecto_touch_move_04, options)
        };

    }

    public static void resizeGraphics(float refactorIndex) {

        // Prevencin de cosas raras
        if (refactorIndex == 0) {
            refactorIndex = 1;
        }

        // Cogemos el valor por debajo
        int width = Float.valueOf(DEFAULT_EFECTO_WIDTH * refactorIndex).intValue();
        int height = Float.valueOf(DEFAULT_EFECTO_HEIGHT * refactorIndex).intValue();
        int pixel_move = Float.valueOf(DEFAULT_EFECTO_PIXEL_MOVE * refactorIndex).intValue();

        EFECTO_WIDTH = width;
        EFECTO_HEIGHT = height;
        EFECTO_PIXEL_MOVE = pixel_move;

        int i = 0;
        for (Bitmap bitmap : EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
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

        i = 0;
        for (Bitmap bitmap : EFECTO_BLOQUEO_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                EFECTO_BLOQUEO_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }
    }

    public static void destroy() {
        ResourceUtils.recicleBitmaps(EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP);
    	/* Save memory
        ResourceUtils.recicleBitmaps(EFECTO_CPU_TOUCH_GRAPHICS_BITMAP);
        */
        ResourceUtils.recicleBitmaps(EFECTO_BLOQUEO_GRAPHICS_BITMAP);
        ResourceUtils.recicleBitmaps(EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP);
        ResourceUtils.recicleBitmaps(EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP);
        ResourceUtils.recicleBitmaps(EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP);
    }

}
