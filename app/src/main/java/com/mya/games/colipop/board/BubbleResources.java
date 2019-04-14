package com.mya.games.colipop.board;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mya.games.colipop.R;
import com.mya.games.colipop.ResourceUtils;

public class BubbleResources {

    static final String TAG = "ColiPop";

    // Bubble baseline size
    public static int DEFAULT_BUBBLE_WIDTH = 32;
    public static int DEFAULT_BUBBLE_HEIGHT = 32;
    public static int DEFAULT_BUBBLE_PIXEL_MOVE = 4;

    // Bubble size
    public static int BUBBLE_WIDTH = DEFAULT_BUBBLE_WIDTH;
    public static int BUBBLE_HEIGHT = DEFAULT_BUBBLE_HEIGHT;
    public static int BUBBLE_WIDTH_MEDIOS = BUBBLE_WIDTH / 2;
    public static int BUBBLE_HEIGHT_MEDIOS = BUBBLE_HEIGHT / 2;
    public static int BUBBLE_PIXEL_MOVE = DEFAULT_BUBBLE_PIXEL_MOVE;

    // Statuss de condicion de bubble
    public static int BUBBLE_STATUS_NONE = -1;
    public static int BUBBLE_STATUS_TURQUESA = 0;
    public static int BUBBLE_STATUS_VERDE = 1;
    public static int BUBBLE_STATUS_AMARILLO = 2;
    public static int BUBBLE_STATUS_ROJO = 3;

    // Statuss de union between bubbles
    public static int BUBBLE_UNION_NONE = 0;
    public static int BUBBLE_UNION_UP = 1;
    public static int BUBBLE_UNION_DOWN = 2;
    public static int BUBBLE_UNION_LEFT = 3;
    public static int BUBBLE_UNION_RIGHT = 4;

    // Statuss de movimiento
    public static int BUBBLE_MOVE_NONE = 0;
    public static int BUBBLE_MOVE_UP = 1;
    public static int BUBBLE_MOVE_DOWN = 2;
    public static int BUBBLE_MOVE_LEFT = 3;
    public static int BUBBLE_MOVE_RIGHT = 4;

    // Bubble graphics
    public static int BUBBLE_GRAPHICS_SIZE = 4;
    public static Bitmap[] BUBBLE_GRAPHICS_BITMAP;
    public static int[] BUBBLE_ANIMATION_SEQUENCE = new int[]{
            1, 1, 2, 2, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    // Bubble graphics
    public static int BUBBLE_MOVE_GRAPHICS_SIZE = 4;
    public static Bitmap[] BUBBLE_MOVE_GRAPHICS_BITMAP;
    public static int[] BUBBLE_MOVE_ANIMATION_SEQUENCE = new int[]{
            0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3,
            0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2,
            1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3,
            0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3,
            1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3};
	
	/* Save memory
	// Bubble status graphics
	public static int BUBBLE_STATUS_GRAPHICS_SIZE = 5;
	public static Bitmap[] BUBBLE_STATUS_GRAPHICS_BITMAP;

	// Bubble union graphics
	public static int BUBBLE_UNION_GRAPHICS_SIZE = 5;
	public static Bitmap[] BUBBLE_UNION_GRAPHICS_BITMAP;
	*/

    public static void initializeGraphics(Resources resources) {
        // Bubbles en baja calidad
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;

        BUBBLE_GRAPHICS_SIZE = 4;
        BUBBLE_GRAPHICS_BITMAP = new Bitmap[BUBBLE_GRAPHICS_SIZE];

        BUBBLE_GRAPHICS_BITMAP[0] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_01, options);
        BUBBLE_GRAPHICS_BITMAP[1] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_02, options);
        BUBBLE_GRAPHICS_BITMAP[2] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_03, options);
        BUBBLE_GRAPHICS_BITMAP[3] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_04, options);

        BUBBLE_MOVE_GRAPHICS_SIZE = 4;
        BUBBLE_MOVE_GRAPHICS_BITMAP = new Bitmap[BUBBLE_MOVE_GRAPHICS_SIZE];

        BUBBLE_MOVE_GRAPHICS_BITMAP[0] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_01, options);
        BUBBLE_MOVE_GRAPHICS_BITMAP[1] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_02, options);
        BUBBLE_MOVE_GRAPHICS_BITMAP[2] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_03, options);
        BUBBLE_MOVE_GRAPHICS_BITMAP[3] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_04, options);
    	
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

    public static void resizeGraphics(float refactorIndex) {
        // Prevencion de cosas raras
        if (refactorIndex == 0) {
            refactorIndex = 1;
        }

        // Cogemos el valor por debajo
        int width = Float.valueOf(DEFAULT_BUBBLE_WIDTH * refactorIndex).intValue();
        int height = Float.valueOf(DEFAULT_BUBBLE_HEIGHT * refactorIndex).intValue();
        int pixel_move = Float.valueOf(DEFAULT_BUBBLE_PIXEL_MOVE * refactorIndex).intValue();

        BUBBLE_WIDTH = width;
        BUBBLE_HEIGHT = height;
        BUBBLE_WIDTH_MEDIOS = width / 2;
        BUBBLE_HEIGHT_MEDIOS = height / 2;
        BUBBLE_PIXEL_MOVE = pixel_move;

        int i = 0;
        for (Bitmap bitmap : BUBBLE_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                BUBBLE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : BUBBLE_MOVE_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                BUBBLE_MOVE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
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

    public static void destroy() {
        ResourceUtils.recicleBitmaps(BUBBLE_GRAPHICS_BITMAP);
        ResourceUtils.recicleBitmaps(BUBBLE_MOVE_GRAPHICS_BITMAP);
    	/* Save memory
    	ResourceUtils.recicleBitmaps(BUBBLE_STATUS_GRAPHICS_BITMAP);
    	ResourceUtils.recicleBitmaps(BUBBLE_UNION_GRAPHICS_BITMAP);
    	*/
    }
}
