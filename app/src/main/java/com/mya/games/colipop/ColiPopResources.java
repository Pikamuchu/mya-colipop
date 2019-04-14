package com.mya.games.colipop;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;

public class ColiPopResources {

    static final String TAG = "ColiPop";

    public static int DEFAULT_SURFACE_WIDTH = 800;
    public static int DEFAULT_SURFACE_HEIGHT = 480;

    public static int SURFACE_WIDTH = DEFAULT_SURFACE_WIDTH;
    public static int SURFACE_HEIGHT = DEFAULT_SURFACE_HEIGHT;

    // Fondos de pantalla de juego
    public static Bitmap backgroundImage;
    public static Bitmap boardImage;

    // Fondos de titulos
    public static Bitmap titleBG;
    //public static Bitmap titleBG2;

    // Formato de textos
    public static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static void initializeGraphics(Resources resources) {
        // Fondo en baja calidad
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        // Fondos de los titulos
        titleBG = BitmapFactory.decodeResource(resources, R.drawable.title_background, options);
        //titleBG2 = BitmapFactory.decodeResource(resources, R.drawable.intermediate_background, options);

        // Fondos de boards de juego
        backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_01, options);

        // Board es muy baja calidad
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        boardImage = BitmapFactory.decodeResource(resources, R.drawable.tablero, options);
    }

    public static void resizeGraphics(int width, int height) {
        SURFACE_WIDTH = width;
        SURFACE_HEIGHT = height;

        // Reescalado de fondos
        if (titleBG != null) {
            titleBG = Bitmap.createScaledBitmap(titleBG, width, height, true);
        }
        //if ( titleBG2 != null ) {
        //	titleBG2 = Bitmap.createScaledBitmap(titleBG2, width, height, true);
        //}
        if (backgroundImage != null) {
            backgroundImage = Bitmap.createScaledBitmap(backgroundImage, width, height, true);
        }
        if (boardImage != null) {
            boardImage = Bitmap.createScaledBitmap(boardImage, width, height, true);
        }

        float refactorIndex = width;
        refactorIndex /= DEFAULT_SURFACE_WIDTH;
        // Prevencin de cosas raras
        if (refactorIndex == 0) {
            refactorIndex = 1;
        }

        // Formato Textos
        paint.setTextSize(20 * refactorIndex);
        paint.setColor(Color.BLACK);

        // Formato Talking Text
        paintText.setTextSize(24 * refactorIndex);
        paintText.setColor(Color.BLACK);
    }

    public static void changeLevelBackground(Resources resources, int level) {
        Bitmap imageToRecicle = backgroundImage;

        // Fondos de boards de juego
        level = (level + 1) % 10;
        if (level == 1) {
            backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_01);
        } else if (level == 2) {
            backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_02);
        } else if (level == 3) {
            backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_03);
        } else if (level == 4) {
            backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_04);
        } else if (level == 5) {
            backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_05);
        } else if (level == 6) {
            backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_06);
        } else if (level == 7) {
            backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_07);
        } else if (level == 8) {
            backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_08);
        } else if (level == 9) {
            backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_09);
        } else if (level == 0) {
            backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_10);
        }
        backgroundImage = Bitmap.createScaledBitmap(backgroundImage, SURFACE_WIDTH, SURFACE_HEIGHT, true);

        imageToRecicle.recycle();
    }

    public static void destroy() {
        if (titleBG != null) {
            titleBG.recycle();
            //titleBG=null;
        }
        //if ( titleBG2 != null ) {
        //    titleBG2.recycle();
        //titleBG2=null;
        //}
        if (backgroundImage != null) {
            backgroundImage.recycle();
            //backgroundImage=null;
        }
        if (boardImage != null) {
            boardImage.recycle();
            //boardImage=null;
        }
        if (backgroundImage != null) {
            backgroundImage.recycle();
            //backgroundImage=null;
        }
    }
}
