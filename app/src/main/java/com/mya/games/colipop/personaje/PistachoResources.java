package com.mya.games.colipop.personaje;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mya.games.colipop.R;
import com.mya.games.colipop.ResourceUtils;

public class PistachoResources {

    //private static final String TAG = "PistachoResources";

    // Pistacho baseline sizes
    private static final int DEFAULT_SURFACE_WIDTH = 800;
    private static final int DEFAULT_WIDTH = 150;
    private static final int DEFAULT_HEIGHT = 125;
    private static final int DEFAULT_PIXEL_MOVE = 0;

    // Pistacho sizes
    public static int WIDTH = DEFAULT_WIDTH;
    public static int HEIGHT = DEFAULT_HEIGHT;
    public static int PIXEL_MOVE = DEFAULT_PIXEL_MOVE;

    // Pistacho graphics
    public static Bitmap[] CARA_GRAPHICS_BITMAP;
    // OJO! la logintud de esta array determina la longitud de todas las secuencias de animacion
    public static int[] CARA_ANIMATION_SEQUENCE = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    public static Bitmap[] OJOS_GRAPHICS_BITMAP;
    public static int[] OJOS_ANIMATION_SEQUENCE = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 2, 2, 2, 2, 1, 1, 1, 1, 0, 0, 0, 0
    };

    public static Bitmap[] BOCA_GRAPHICS_BITMAP;
    public static int[] BOCA_ANIMATION_SEQUENCE = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0
    };

    public static Bitmap[] BIGOTES_GRAPHICS_BITMAP;
    public static int[] BIGOTES_ANIMATION_SEQUENCE = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0
    };

    /**
     * Inicializa los graficos del Pistacho
     *
     * @param resources
     */
    public static void initializeGraphics(Resources resources) {

        CARA_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.pistacho_cara)
        };

        OJOS_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.pistacho_ojos)
        };

        BOCA_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.pistacho_boca)
        };

        BIGOTES_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.pistacho_bigotes)
        };

    }

    /**
     * Reescala los graficos del Pistacho
     *
     * @param canvasWidth
     * @param canvasHeight
     */
    public static void resizeGraphics(int surfaceWidth, int surfaceHeight) {

        //Log.d(TAG, "Surface resize: width = " + surfaceWidth + ", height = " + surfaceHeight);

        // Calculamos el refactor index ( siempre en funcion del width ?? )
        float refactorIndex = surfaceWidth;
        refactorIndex /= DEFAULT_SURFACE_WIDTH;
        // Prevencin de cosas raras
        if (refactorIndex == 0) {
            refactorIndex = 1;
        }

        // Cogemos el valor por debajo
        int width = Float.valueOf(DEFAULT_WIDTH * refactorIndex).intValue();
        int height = Float.valueOf(DEFAULT_HEIGHT * refactorIndex).intValue();
        int pixel_move = Float.valueOf(DEFAULT_PIXEL_MOVE * refactorIndex).intValue();

        WIDTH = width;
        HEIGHT = height;
        PIXEL_MOVE = pixel_move;

        int i = 0;
        for (Bitmap bitmap : CARA_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                CARA_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : OJOS_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                OJOS_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : BOCA_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                BOCA_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : BIGOTES_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                BIGOTES_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

    }

    public static void destroy() {
        ResourceUtils.recicleBitmaps(CARA_GRAPHICS_BITMAP);
        //CARA_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(OJOS_GRAPHICS_BITMAP);
        //OJOS_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(BOCA_GRAPHICS_BITMAP);
        //BOCA_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(BIGOTES_GRAPHICS_BITMAP);
        //BIGOTES_GRAPHICS_BITMAP=null;
    }

}
