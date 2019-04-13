package com.mya.games.colipop.personaje;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mya.games.colipop.R;
import com.mya.games.colipop.ResourceUtils;

public class ColitaResources {

    //private static final String TAG = "ColitaResources";

    // Colita baseline sizes
    private static final int DEFAULT_SURFACE_WIDTH = 800;
    private static final int DEFAULT_SURFACE_HEIGHT = 480;
    private static final int DEFAULT_WIDTH = 150;
    private static final int DEFAULT_HEIGHT = 125;
    private static final int DEFAULT_PIXEL_MOVE = 0;
    // Dialogo baseline Sizes
    private static final int DEFAULT_DIALOGO_CORTO_WIDTH = 150;
    private static final int DEFAULT_DIALOGO_MEDIO_WIDTH = 300;
    private static final int DEFAULT_DIALOGO_MEDIO2_WIDTH = 450;
    private static final int DEFAULT_DIALOGO_LARGO_WIDTH = 600;
    private static final int DEFAULT_DIALOGO_HEIGHT = 75;
    // Colita sizes
    public static int WIDTH = DEFAULT_WIDTH;
    public static int HEIGHT = DEFAULT_HEIGHT;
    public static int PIXEL_MOVE = DEFAULT_PIXEL_MOVE;
    // Dialogo Sizes
    public static int DIALOGO_CORTO_WIDTH = DEFAULT_DIALOGO_CORTO_WIDTH;
    public static int DIALOGO_MEDIO_WIDTH = DEFAULT_DIALOGO_MEDIO_WIDTH;
    public static int DIALOGO_MEDIO2_WIDTH = DEFAULT_DIALOGO_MEDIO2_WIDTH;
    public static int DIALOGO_LARGO_WIDTH = DEFAULT_DIALOGO_LARGO_WIDTH;
    public static int DIALOGO_HEIGHT = DEFAULT_DIALOGO_HEIGHT;

    // Texto Lengths
    public static int TEXTO_CORTO = 10;
    public static int TEXTO_MEDIO = 20;
    public static int TEXTO_MEDIO2 = 30;
    public static int TEXTO_LARGO = 40;

    // Colita graphics
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
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            2, 2, 2, 2, 0, 0, 0, 0, 2, 2, 2, 2, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 2, 2, 2, 2, 0, 0, 0, 0, 2, 2, 2, 2,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    public static int[] OJOS_TALKING_ANIMATION_SEQUENCE = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1
    };

    public static Bitmap[] BOCA_GRAPHICS_BITMAP;
    public static int[] BOCA_NORMAL_ANIMATION_SEQUENCE = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    public static int[] BOCA_HAPPY_ANIMATION_SEQUENCE = new int[]{
            0, 0, 0, 0, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4,
            3, 3, 3, 3, 0, 0, 0, 0, 5, 5, 5, 5, 4, 4, 4, 4,
            2, 2, 2, 2, 3, 3, 3, 3, 0, 0, 0, 0, 2, 2, 2, 2,
            4, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 2, 0, 0, 0, 0,
            2, 2, 2, 2, 4, 4, 4, 4, 5, 5, 5, 5, 3, 3, 3, 3
    };
    public static int[] BOCA_UNHAPPY_ANIMATION_SEQUENCE = new int[]{
            0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            3, 3, 3, 3, 0, 0, 0, 0, 5, 5, 5, 5, 4, 4, 4, 4,
            2, 2, 2, 2, 3, 3, 3, 3, 0, 0, 0, 0, 2, 2, 2, 2,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            2, 2, 2, 2, 4, 4, 4, 4, 5, 5, 5, 5, 3, 3, 3, 3
    };
    public static int[] BOCA_TALKING_ANIMATION_SEQUENCE = new int[]{
            0, 0, 0, 0, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4,
            3, 3, 3, 3, 0, 0, 0, 0, 5, 5, 5, 5, 4, 4, 4, 4,
            2, 2, 2, 2, 3, 3, 3, 3, 0, 0, 0, 0, 2, 2, 2, 2,
            4, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 2, 0, 0, 0, 0,
            2, 2, 2, 2, 4, 4, 4, 4, 5, 5, 5, 5, 3, 3, 3, 3
    };

    public static Bitmap[] DIALOGO_CORTO_GRAPHICS_BITMAP;
    public static Bitmap[] DIALOGO_MEDIO_GRAPHICS_BITMAP;
    public static Bitmap[] DIALOGO_MEDIO2_GRAPHICS_BITMAP;
    public static Bitmap[] DIALOGO_LARGO_GRAPHICS_BITMAP;
    public static int[] DIALOGO_ANIMATION_SEQUENCE = new int[]{
            0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 0, 0, 0, 0,
    };

    public static Bitmap[] METER_FONDO_GRAPHICS_BITMAP;
    public static Bitmap[] METER_FLECHA1_GRAPHICS_BITMAP;
    public static Bitmap[] METER_FLECHA2_GRAPHICS_BITMAP;
    public static Bitmap[] METER_FLECHA_GRAPHICS_BITMAP;
    public static int[] METER_FLECHA_ANIMATION_SEQUENCE = new int[]{
            0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1,
            0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1,
            0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1,
            0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1,
            0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1,
    };

    public static Bitmap[] LEVEL_UP_GRAPHICS_BITMAP;
    public static int[] LEVEL_UP_ANIMATION_SEQUENCE = new int[]{
            0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1,
            0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1
    };

    /**
     * Inicializa los graficos de la Colita
     *
     * @param resources
     */
    public static void initializeGraphics(Resources resources) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;

        CARA_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.coli_cara, options)
        };

        OJOS_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.coli_ojos_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.coli_ojos_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.coli_ojos_03, options)
        };

        BOCA_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.coli_boca_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.coli_boca_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.coli_boca_11, options),
                BitmapFactory.decodeResource(resources, R.drawable.coli_boca_12, options),
                BitmapFactory.decodeResource(resources, R.drawable.coli_boca_13, options),
                BitmapFactory.decodeResource(resources, R.drawable.coli_boca_14, options)
        };

        METER_FONDO_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.meter_fondo, options)
        };

        // Dialogos y fecha de meter en baja calidad
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;

        DIALOGO_CORTO_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.dialogo_corto_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.dialogo_corto_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.dialogo_corto_03, options)
        };

        DIALOGO_MEDIO_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.dialogo_medio_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.dialogo_medio_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.dialogo_medio_03, options)
        };

        DIALOGO_MEDIO2_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.dialogo_medio2_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.dialogo_medio2_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.dialogo_medio2_03, options)
        };

        DIALOGO_LARGO_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.dialogo_largo_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.dialogo_largo_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.dialogo_largo_03, options)
        };

        METER_FLECHA1_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_03, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_04, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_05, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_06, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_07, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_08, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_09, options)
        };

        METER_FLECHA2_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_02, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_03, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_04, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_05, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_06, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_07, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_08, options),
                BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_09, options)
        };

        METER_FLECHA_GRAPHICS_BITMAP = new Bitmap[]{
                METER_FLECHA1_GRAPHICS_BITMAP[0],
                METER_FLECHA2_GRAPHICS_BITMAP[0]
        };

        LEVEL_UP_GRAPHICS_BITMAP = new Bitmap[]{
                BitmapFactory.decodeResource(resources, R.drawable.level_up_01, options),
                BitmapFactory.decodeResource(resources, R.drawable.level_up_02, options)
        };

    }

    /**
     * Reescala los graficos de la Colita
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
        float refactorIndexHeight = surfaceHeight;
        refactorIndexHeight /= DEFAULT_SURFACE_HEIGHT;
        // Prevencin de cosas raras
        if (refactorIndexHeight == 0) {
            refactorIndexHeight = 1;
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

        // El meter es cuadrado !!

        i = 0;
        for (Bitmap bitmap : METER_FONDO_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                METER_FONDO_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, width, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : METER_FLECHA_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                METER_FLECHA_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, width, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : METER_FLECHA1_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                METER_FLECHA1_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, width, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : METER_FLECHA2_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                METER_FLECHA2_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, width, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : LEVEL_UP_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                LEVEL_UP_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, width, true);
            }
            i++;
        }

        // Cogemos el valor por debajo
        int dialogo_corto_width = Float.valueOf(DEFAULT_DIALOGO_CORTO_WIDTH * refactorIndex).intValue();
        int dialogo_medio_width = Float.valueOf(DEFAULT_DIALOGO_MEDIO_WIDTH * refactorIndex).intValue();
        int dialogo_medio2_width = Float.valueOf(DEFAULT_DIALOGO_MEDIO2_WIDTH * refactorIndex).intValue();
        int dialogo_largo_width = Float.valueOf(DEFAULT_DIALOGO_LARGO_WIDTH * refactorIndex).intValue();
        int dialogo_height = Float.valueOf(DEFAULT_DIALOGO_HEIGHT * refactorIndexHeight).intValue();

        DIALOGO_CORTO_WIDTH = dialogo_corto_width;
        DIALOGO_MEDIO_WIDTH = dialogo_medio_width;
        DIALOGO_MEDIO2_WIDTH = dialogo_medio2_width;
        DIALOGO_LARGO_WIDTH = dialogo_largo_width;
        DIALOGO_HEIGHT = dialogo_height;

        i = 0;
        for (Bitmap bitmap : DIALOGO_CORTO_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                DIALOGO_CORTO_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, dialogo_corto_width, dialogo_height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : DIALOGO_MEDIO_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                DIALOGO_MEDIO_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, dialogo_medio_width, dialogo_height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : DIALOGO_MEDIO2_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                DIALOGO_MEDIO2_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, dialogo_medio2_width, dialogo_height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : DIALOGO_LARGO_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                DIALOGO_LARGO_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, dialogo_largo_width, dialogo_height, true);
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
        ResourceUtils.recicleBitmaps(METER_FONDO_GRAPHICS_BITMAP);
        //METER_FONDO_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(DIALOGO_CORTO_GRAPHICS_BITMAP);
        //DIALOGO_CORTO_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(DIALOGO_MEDIO_GRAPHICS_BITMAP);
        //DIALOGO_MEDIO_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(DIALOGO_MEDIO2_GRAPHICS_BITMAP);
        //DIALOGO_MEDIO2_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(DIALOGO_LARGO_GRAPHICS_BITMAP);
        //DIALOGO_LARGO_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(METER_FLECHA1_GRAPHICS_BITMAP);
        //METER_FLECHA1_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(METER_FLECHA2_GRAPHICS_BITMAP);
        //METER_FLECHA2_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(METER_FLECHA_GRAPHICS_BITMAP);
        //METER_FLECHA_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(LEVEL_UP_GRAPHICS_BITMAP);
        //LEVEL_UP_GRAPHICS_BITMAP=null;
    }

}
