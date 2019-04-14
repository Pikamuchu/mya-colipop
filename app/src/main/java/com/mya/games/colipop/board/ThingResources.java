package com.mya.games.colipop.board;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mya.games.colipop.R;
import com.mya.games.colipop.ResourceUtils;

public class ThingResources {

    static final String TAG = "ColiPop";

    // Thing baseline size
    public static int DEFAULT_THING_WIDTH = 32;
    public static int DEFAULT_THING_HEIGHT = 32;
    public static int DEFAULT_THING_PIXEL_MOVE = 4;
    public static int DEFAULT_THING_GRANDE_WIDTH = 64;
    public static int DEFAULT_THING_GRANDE_HEIGHT = 64;
    public static int DEFAULT_THING_GRANDE_PIXEL_MOVE = 8;

    // Thing size
    public static int THING_WIDTH = DEFAULT_THING_WIDTH;
    public static int THING_HEIGHT = DEFAULT_THING_HEIGHT;
    public static int THING_WIDTH_MEDIO = THING_WIDTH / 2;
    public static int THING_HEIGHT_MEDIO = THING_HEIGHT / 2;
    public static int THING_PIXEL_MOVE = DEFAULT_THING_PIXEL_MOVE;
    public static int THING_GRANDE_WIDTH = DEFAULT_THING_GRANDE_WIDTH;
    public static int THING_GRANDE_HEIGHT = DEFAULT_THING_GRANDE_HEIGHT;
    public static int THING_GRANDE_PIXEL_MOVE = DEFAULT_THING_GRANDE_PIXEL_MOVE;

    // Statuss de thing
    public static int THING_STATUS_BUBBLE = 0;
    public static int THING_STATUS_EXPLODE = 1;
    public static int THING_STATUS_MOVIDO = 2;
    public static int THING_STATUS_REMOVED = 3;
    public static int THING_STATUS_EFECTO_FIJO = 4;

    public static int[] THING_ANIMATION_SEQUENCE = new int[]{0};

    public static int[] THING_GRANDE_ANIMATION_SEQUENCE = new int[]{0};

    public static int THING_REMOVED_GRAPHICS_SIZE = 4;
    public static int[] THING_REMOVED_ANIMATION_SEQUENCE = new int[]{
            0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3,
            0, 0, 1, 1, 2, 2, 3, 3, 0, 0, 1, 1, 2, 2, 3, 3,
            0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3,
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1,
            2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3};

    public static int THING_GRAPHICS_SIZE = 1;

    public static Bitmap[] CARAMELO_GRAPHICS_BITMAP;
    public static Bitmap[] CARAMELO_GRANDE_GRAPHICS_BITMAP;
    public static Bitmap[] PIRULETA_GRAPHICS_BITMAP;
    public static Bitmap[] PIRULETA_GRANDE_GRAPHICS_BITMAP;
    public static Bitmap[] RASPA_GRAPHICS_BITMAP;
    public static Bitmap[] RASPA_GRANDE_GRAPHICS_BITMAP;
    public static Bitmap[] PEINE_GRAPHICS_BITMAP;
    public static Bitmap[] PEINE_GRANDE_GRAPHICS_BITMAP;

    public static int CARAMELO_PORCENTAJE = 12;
    public static int PIRULETA_PORCENTAJE = 12;
    public static int RASPA_PORCENTAJE = 6;
    public static int PEINE_PORCENTAJE = 6;

    public static int CARAMELO_OBJECT_TYPE = 0;
    public static int PIRULETA_OBJECT_TYPE = 1;
    public static int RASPA_OBJECT_TYPE = 10;
    public static int PEINE_OBJECT_TYPE = 11;


    public static void initializeGraphics(Resources resources) {

        // Si se anyaden mas bitmaps hay que subir esto
        THING_GRAPHICS_SIZE = 1;

        CARAMELO_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.caramelo_01)
        };

        PIRULETA_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.piruleta_01)
        };

        RASPA_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.raspa_01)
        };

        PEINE_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.peine_01)
        };

        CARAMELO_GRANDE_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.caramelo_grande_01)
        };

        PIRULETA_GRANDE_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.piruleta_grande_01)
        };

        RASPA_GRANDE_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.raspa_grande_01)
        };

        PEINE_GRANDE_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.peine_grande_01)
        };

    }

    public static void resizeGraphics(float refactorIndex) {

        // Prevencin de cosas raras
        if (refactorIndex == 0) {
            refactorIndex = 1;
        }

        // Cogemos el valor por debajo
        int width = Float.valueOf(DEFAULT_THING_WIDTH * refactorIndex).intValue();
        int height = Float.valueOf(DEFAULT_THING_HEIGHT * refactorIndex).intValue();
        int pixel_move = Float.valueOf(DEFAULT_THING_PIXEL_MOVE * refactorIndex).intValue();

        THING_WIDTH = width;
        THING_HEIGHT = height;
        THING_WIDTH_MEDIO = width / 2;
        THING_HEIGHT_MEDIO = height / 2;
        THING_PIXEL_MOVE = pixel_move;

        int i = 0;
        for (Bitmap bitmap : CARAMELO_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                CARAMELO_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : PIRULETA_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                PIRULETA_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : RASPA_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                RASPA_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : PEINE_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                PEINE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        // Cogemos el valor por debajo
        width = Float.valueOf(DEFAULT_THING_GRANDE_WIDTH * refactorIndex).intValue();
        height = Float.valueOf(DEFAULT_THING_GRANDE_HEIGHT * refactorIndex).intValue();
        pixel_move = Float.valueOf(DEFAULT_THING_GRANDE_PIXEL_MOVE * refactorIndex).intValue();

        THING_GRANDE_WIDTH = width;
        THING_GRANDE_HEIGHT = height;
        THING_GRANDE_PIXEL_MOVE = pixel_move;

        i = 0;
        for (Bitmap bitmap : CARAMELO_GRANDE_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                CARAMELO_GRANDE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : PIRULETA_GRANDE_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                PIRULETA_GRANDE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : RASPA_GRANDE_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                RASPA_GRANDE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : PEINE_GRANDE_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                PEINE_GRANDE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

    }

    public static void destroy() {
        ResourceUtils.recicleBitmaps(CARAMELO_GRAPHICS_BITMAP);
        //CARAMELO_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(CARAMELO_GRANDE_GRAPHICS_BITMAP);
        //CARAMELO_GRANDE_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(PIRULETA_GRAPHICS_BITMAP);
        //PIRULETA_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(PIRULETA_GRANDE_GRAPHICS_BITMAP);
        //PIRULETA_GRANDE_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(RASPA_GRAPHICS_BITMAP);
        //RASPA_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(RASPA_GRANDE_GRAPHICS_BITMAP);
        //RASPA_GRANDE_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(PEINE_GRAPHICS_BITMAP);
        //PEINE_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(PEINE_GRANDE_GRAPHICS_BITMAP);
        //PEINE_GRANDE_GRAPHICS_BITMAP=null;
    }

}
