package com.mya.games.colipop.tablero;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mya.games.colipop.R;
import com.mya.games.colipop.ResourceUtils;

public class ExplosionResources {

    // Explosion baseline size
    public static int DEFAULT_EXPLOSION_WIDTH = 64;
    public static int DEFAULT_EXPLOSION_HEIGHT = 64;
    public static int DEFAULT_EXPLOSION_PIXEL_MOVE = 0;

    // Explosion size
    public static int EXPLOSION_WIDTH = DEFAULT_EXPLOSION_WIDTH;
    public static int EXPLOSION_HEIGHT = DEFAULT_EXPLOSION_HEIGHT;
    public static int EXPLOSION_PIXEL_MOVE = DEFAULT_EXPLOSION_PIXEL_MOVE;

    // Explosion graphics & animation properties
    public static int EXPLOSION_GRAPHICS_SIZE = 4;
    public static Bitmap[] EXPLOSION_GRAPHICS_BITMAP;

    public static void initializeGraphics(Resources resources) {

        // Explosiones en baja calidad
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;

        EXPLOSION_GRAPHICS_SIZE = 4;
        EXPLOSION_GRAPHICS_BITMAP = new Bitmap[EXPLOSION_GRAPHICS_SIZE];

        EXPLOSION_GRAPHICS_BITMAP[0] = BitmapFactory.decodeResource(resources, R.drawable.explosion_01, options);
        EXPLOSION_GRAPHICS_BITMAP[1] = BitmapFactory.decodeResource(resources, R.drawable.explosion_02, options);
        EXPLOSION_GRAPHICS_BITMAP[2] = BitmapFactory.decodeResource(resources, R.drawable.explosion_03, options);
        EXPLOSION_GRAPHICS_BITMAP[3] = BitmapFactory.decodeResource(resources, R.drawable.explosion_04, options);

        EXPLOSION_PIXEL_MOVE = 0;

        EXPLOSION_WIDTH = EXPLOSION_GRAPHICS_BITMAP[0].getWidth();
        EXPLOSION_HEIGHT = EXPLOSION_GRAPHICS_BITMAP[0].getHeight();

    }

    public static void resizeGraphics(float refactorIndex) {

        // Prevencin de cosas raras
        if (refactorIndex == 0) {
            refactorIndex = 1;
        }

        // Cogemos el valor por debajo
        int width = Float.valueOf(DEFAULT_EXPLOSION_WIDTH * refactorIndex).intValue();
        int height = Float.valueOf(DEFAULT_EXPLOSION_HEIGHT * refactorIndex).intValue();
        int pixel_move = Float.valueOf(DEFAULT_EXPLOSION_PIXEL_MOVE * refactorIndex).intValue();

        EXPLOSION_WIDTH = width;
        EXPLOSION_HEIGHT = height;
        EXPLOSION_PIXEL_MOVE = pixel_move;

        int i = 0;
        for (Bitmap bitmap : EXPLOSION_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                EXPLOSION_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

    }

    public static void destroy() {
        ResourceUtils.recicleBitmaps(EXPLOSION_GRAPHICS_BITMAP);
        //EXPLOSION_GRAPHICS_BITMAP=null;
    }

}
