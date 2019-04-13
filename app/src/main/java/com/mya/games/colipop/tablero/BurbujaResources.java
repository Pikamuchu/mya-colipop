/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// FIXME: review and cleanup

package com.mya.games.colipop.tablero;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mya.games.colipop.R;
import com.mya.games.colipop.ResourceUtils;

public class BurbujaResources {

    // Burbuja baseline size
    public static int DEFAULT_BURBUJA_WIDTH = 32;
    public static int DEFAULT_BURBUJA_HEIGHT = 32;
    public static int DEFAULT_BURBUJA_PIXEL_MOVE = 4;

    // Burbuja size
    public static int BURBUJA_WIDTH = DEFAULT_BURBUJA_WIDTH;
    public static int BURBUJA_HEIGHT = DEFAULT_BURBUJA_HEIGHT;
    public static int BURBUJA_WIDTH_MEDIOS = BURBUJA_WIDTH / 2;
    public static int BURBUJA_HEIGHT_MEDIOS = BURBUJA_HEIGHT / 2;
    public static int BURBUJA_PIXEL_MOVE = DEFAULT_BURBUJA_PIXEL_MOVE;

    // Estados de condicion de burbuja
    public static int BURBUJA_ESTADO_NONE = -1;
    public static int BURBUJA_ESTADO_TURQUESA = 0;
    public static int BURBUJA_ESTADO_VERDE = 1;
    public static int BURBUJA_ESTADO_AMARILLO = 2;
    public static int BURBUJA_ESTADO_ROJO = 3;

    // Estados de union entre burbujas
    public static int BURBUJA_UNION_NONE = 0;
    public static int BURBUJA_UNION_UP = 1;
    public static int BURBUJA_UNION_DOWN = 2;
    public static int BURBUJA_UNION_LEFT = 3;
    public static int BURBUJA_UNION_RIGHT = 4;

    // Estados de movimiento
    public static int BURBUJA_MOVE_NONE = 0;
    public static int BURBUJA_MOVE_UP = 1;
    public static int BURBUJA_MOVE_DOWN = 2;
    public static int BURBUJA_MOVE_LEFT = 3;
    public static int BURBUJA_MOVE_RIGHT = 4;

    // Burbuja graphics
    public static int BURBUJA_GRAPHICS_SIZE = 4;
    public static Bitmap[] BURBUJA_GRAPHICS_BITMAP;
    public static int[] BURBUJA_ANIMATION_SEQUENCE = new int[]{
            1, 1, 2, 2, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    // Burbuja graphics
    public static int BURBUJA_MOVE_GRAPHICS_SIZE = 4;
    public static Bitmap[] BURBUJA_MOVE_GRAPHICS_BITMAP;
    public static int[] BURBUJA_MOVE_ANIMATION_SEQUENCE = new int[]{
            0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3,
            0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2,
            1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3,
            0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3,
            1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3};
	
	/* Save memory
	// Burbuja status graphics
	public static int BURBUJA_STATUS_GRAPHICS_SIZE = 5;
	public static Bitmap[] BURBUJA_STATUS_GRAPHICS_BITMAP;

	// Burbuja union graphics
	public static int BURBUJA_UNION_GRAPHICS_SIZE = 5;
	public static Bitmap[] BURBUJA_UNION_GRAPHICS_BITMAP;
	*/

    public static void initializeGraphics(Resources resources) {

        // Burbujas en baja calidad
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;

        BURBUJA_GRAPHICS_SIZE = 4;
        BURBUJA_GRAPHICS_BITMAP = new Bitmap[BURBUJA_GRAPHICS_SIZE];

        BURBUJA_GRAPHICS_BITMAP[0] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_01, options);
        BURBUJA_GRAPHICS_BITMAP[1] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_02, options);
        BURBUJA_GRAPHICS_BITMAP[2] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_03, options);
        BURBUJA_GRAPHICS_BITMAP[3] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_04, options);

        BURBUJA_MOVE_GRAPHICS_SIZE = 4;
        BURBUJA_MOVE_GRAPHICS_BITMAP = new Bitmap[BURBUJA_MOVE_GRAPHICS_SIZE];

        BURBUJA_MOVE_GRAPHICS_BITMAP[0] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_01, options);
        BURBUJA_MOVE_GRAPHICS_BITMAP[1] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_02, options);
        BURBUJA_MOVE_GRAPHICS_BITMAP[2] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_03, options);
        BURBUJA_MOVE_GRAPHICS_BITMAP[3] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_move_04, options);
    	
    	/* Save memory
    	BURBUJA_STATUS_GRAPHICS_SIZE = 4;
    	BURBUJA_STATUS_GRAPHICS_BITMAP = new Bitmap[BURBUJA_STATUS_GRAPHICS_SIZE];
        
    	BURBUJA_STATUS_GRAPHICS_BITMAP[0] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_fondo_turquesa);
    	BURBUJA_STATUS_GRAPHICS_BITMAP[1] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_fondo_verde);
    	BURBUJA_STATUS_GRAPHICS_BITMAP[2] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_fondo_amarillo);
    	BURBUJA_STATUS_GRAPHICS_BITMAP[3] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_fondo_rojo);

    	BURBUJA_UNION_GRAPHICS_SIZE = 5;
    	BURBUJA_UNION_GRAPHICS_BITMAP = new Bitmap[BURBUJA_UNION_GRAPHICS_SIZE];
        
    	BURBUJA_UNION_GRAPHICS_BITMAP[0] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_01);
    	BURBUJA_UNION_GRAPHICS_BITMAP[1] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_union_up);
    	BURBUJA_UNION_GRAPHICS_BITMAP[2] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_union_down);
    	BURBUJA_UNION_GRAPHICS_BITMAP[3] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_union_right);
    	BURBUJA_UNION_GRAPHICS_BITMAP[4] = BitmapFactory.decodeResource(resources, R.drawable.burbuja_union_left);
    	*/

    }

    public static void resizeGraphics(float refactorIndex) {

        // Prevencin de cosas raras
        if (refactorIndex == 0) {
            refactorIndex = 1;
        }

        // Cogemos el valor por debajo
        int width = Float.valueOf(DEFAULT_BURBUJA_WIDTH * refactorIndex).intValue();
        int height = Float.valueOf(DEFAULT_BURBUJA_HEIGHT * refactorIndex).intValue();
        int pixel_move = Float.valueOf(DEFAULT_BURBUJA_PIXEL_MOVE * refactorIndex).intValue();

        BURBUJA_WIDTH = width;
        BURBUJA_HEIGHT = height;
        BURBUJA_WIDTH_MEDIOS = width / 2;
        BURBUJA_HEIGHT_MEDIOS = height / 2;
        BURBUJA_PIXEL_MOVE = pixel_move;

        int i = 0;
        for (Bitmap bitmap : BURBUJA_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                BURBUJA_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }

        i = 0;
        for (Bitmap bitmap : BURBUJA_MOVE_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                BURBUJA_MOVE_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
            }
            i++;
        }
    	/* Save memory
    	i = 0;
    	for ( Bitmap bitmap : BURBUJA_STATUS_GRAPHICS_BITMAP ) {
    		if ( bitmap != null ) {
    			BURBUJA_STATUS_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
    		}
    		i++;
    	}

    	i = 0;
    	for ( Bitmap bitmap : BURBUJA_UNION_GRAPHICS_BITMAP ) {
    		if ( bitmap != null ) {
    			BURBUJA_UNION_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true);
    		}
    		i++;
    	}
    	*/

    }

    public static void destroy() {
        ResourceUtils.recicleBitmaps(BURBUJA_GRAPHICS_BITMAP);
        //BURBUJA_GRAPHICS_BITMAP=null;
        ResourceUtils.recicleBitmaps(BURBUJA_MOVE_GRAPHICS_BITMAP);
        //BURBUJA_MOVE_GRAPHICS_BITMAP=null;
    	/* Save memory
    	ResourceUtils.recicleBitmaps(BURBUJA_STATUS_GRAPHICS_BITMAP);
    	//BURBUJA_STATUS_GRAPHICS_BITMAP=null;
    	ResourceUtils.recicleBitmaps(BURBUJA_UNION_GRAPHICS_BITMAP);
    	//BURBUJA_UNION_GRAPHICS_BITMAP=null;
    	*/
    }

}
