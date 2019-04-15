package com.mya.games.colipop.character

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.mya.games.colipop.R
import com.mya.games.colipop.ResourceUtils

object ColitaResources {

    private val TAG = "ColiPop"

    // Colita baseline sizes
    private val DEFAULT_SURFACE_WIDTH = 800
    private val DEFAULT_SURFACE_HEIGHT = 480
    private val DEFAULT_WIDTH = 150
    private val DEFAULT_HEIGHT = 125
    private val DEFAULT_PIXEL_MOVE = 0
    // Dialogo baseline Sizes
    private val DEFAULT_DIALOGO_CORTO_WIDTH = 150
    private val DEFAULT_DIALOGO_MEDIO_WIDTH = 300
    private val DEFAULT_DIALOGO_MEDIO2_WIDTH = 450
    private val DEFAULT_DIALOGO_LARGO_WIDTH = 600
    private val DEFAULT_DIALOGO_HEIGHT = 75
    // Colita sizes
    var WIDTH = DEFAULT_WIDTH
    var HEIGHT = DEFAULT_HEIGHT
    var PIXEL_MOVE = DEFAULT_PIXEL_MOVE
    // Dialogo Sizes
    var DIALOGO_CORTO_WIDTH = DEFAULT_DIALOGO_CORTO_WIDTH
    var DIALOGO_MEDIO_WIDTH = DEFAULT_DIALOGO_MEDIO_WIDTH
    var DIALOGO_MEDIO2_WIDTH = DEFAULT_DIALOGO_MEDIO2_WIDTH
    var DIALOGO_LARGO_WIDTH = DEFAULT_DIALOGO_LARGO_WIDTH
    var DIALOGO_HEIGHT = DEFAULT_DIALOGO_HEIGHT

    // Texto Lengths
    var TEXTO_CORTO = 10
    var TEXTO_MEDIO = 20
    var TEXTO_MEDIO2 = 30
    var TEXTO_LARGO = 40

    // Colita graphics
    lateinit var CARA_GRAPHICS_BITMAP: Array<Bitmap>
    // OJO! la logintud de esta array determina la longitud de todas las secuencias de animation
    var CARA_ANIMATION_SEQUENCE = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    lateinit var OJOS_GRAPHICS_BITMAP: Array<Bitmap>
    var OJOS_ANIMATION_SEQUENCE = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 0, 0, 0, 0, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 0, 0, 0, 0, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    var OJOS_TALKING_ANIMATION_SEQUENCE = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1)

    lateinit var BOCA_GRAPHICS_BITMAP: Array<Bitmap>
    var BOCA_NORMAL_ANIMATION_SEQUENCE = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    var BOCA_HAPPY_ANIMATION_SEQUENCE = intArrayOf(0, 0, 0, 0, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 3, 3, 3, 3, 0, 0, 0, 0, 5, 5, 5, 5, 4, 4, 4, 4, 2, 2, 2, 2, 3, 3, 3, 3, 0, 0, 0, 0, 2, 2, 2, 2, 4, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 2, 0, 0, 0, 0, 2, 2, 2, 2, 4, 4, 4, 4, 5, 5, 5, 5, 3, 3, 3, 3)
    var BOCA_UNHAPPY_ANIMATION_SEQUENCE = intArrayOf(0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 0, 0, 0, 0, 5, 5, 5, 5, 4, 4, 4, 4, 2, 2, 2, 2, 3, 3, 3, 3, 0, 0, 0, 0, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 2, 4, 4, 4, 4, 5, 5, 5, 5, 3, 3, 3, 3)
    var BOCA_TALKING_ANIMATION_SEQUENCE = intArrayOf(0, 0, 0, 0, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 3, 3, 3, 3, 0, 0, 0, 0, 5, 5, 5, 5, 4, 4, 4, 4, 2, 2, 2, 2, 3, 3, 3, 3, 0, 0, 0, 0, 2, 2, 2, 2, 4, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 2, 0, 0, 0, 0, 2, 2, 2, 2, 4, 4, 4, 4, 5, 5, 5, 5, 3, 3, 3, 3)

    lateinit var DIALOGO_CORTO_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var DIALOGO_MEDIO_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var DIALOGO_MEDIO2_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var DIALOGO_LARGO_GRAPHICS_BITMAP: Array<Bitmap>
    var DIALOGO_ANIMATION_SEQUENCE = intArrayOf(0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 0, 0, 0, 0)

    lateinit var METER_FONDO_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var METER_FLECHA1_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var METER_FLECHA2_GRAPHICS_BITMAP: Array<Bitmap>
    lateinit var METER_FLECHA_GRAPHICS_BITMAP: Array<Bitmap>
    var METER_FLECHA_ANIMATION_SEQUENCE = intArrayOf(0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1)

    lateinit var LEVEL_UP_GRAPHICS_BITMAP: Array<Bitmap>
    var LEVEL_UP_ANIMATION_SEQUENCE = intArrayOf(0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1)

    fun initializeGraphics(resources: Resources) {
        val options = BitmapFactory.Options()
        options.inPurgeable = true

        CARA_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.coli_cara, options))

        OJOS_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.coli_ojos_01, options), BitmapFactory.decodeResource(resources, R.drawable.coli_ojos_02, options), BitmapFactory.decodeResource(resources, R.drawable.coli_ojos_03, options))

        BOCA_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.coli_boca_01, options), BitmapFactory.decodeResource(resources, R.drawable.coli_boca_02, options), BitmapFactory.decodeResource(resources, R.drawable.coli_boca_11, options), BitmapFactory.decodeResource(resources, R.drawable.coli_boca_12, options), BitmapFactory.decodeResource(resources, R.drawable.coli_boca_13, options), BitmapFactory.decodeResource(resources, R.drawable.coli_boca_14, options))

        METER_FONDO_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.meter_fondo, options))

        // Dialogos y fecha de meter en baja calidad
        options.inPreferredConfig = Bitmap.Config.ARGB_4444

        DIALOGO_CORTO_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.dialogo_corto_01, options), BitmapFactory.decodeResource(resources, R.drawable.dialogo_corto_02, options), BitmapFactory.decodeResource(resources, R.drawable.dialogo_corto_03, options))

        DIALOGO_MEDIO_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.dialogo_medio_01, options), BitmapFactory.decodeResource(resources, R.drawable.dialogo_medio_02, options), BitmapFactory.decodeResource(resources, R.drawable.dialogo_medio_03, options))

        DIALOGO_MEDIO2_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.dialogo_medio2_01, options), BitmapFactory.decodeResource(resources, R.drawable.dialogo_medio2_02, options), BitmapFactory.decodeResource(resources, R.drawable.dialogo_medio2_03, options))

        DIALOGO_LARGO_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.dialogo_largo_01, options), BitmapFactory.decodeResource(resources, R.drawable.dialogo_largo_02, options), BitmapFactory.decodeResource(resources, R.drawable.dialogo_largo_03, options))

        METER_FLECHA1_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_01, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_02, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_03, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_04, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_05, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_06, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_07, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_08, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha1_09, options))

        METER_FLECHA2_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_01, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_02, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_03, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_04, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_05, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_06, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_07, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_08, options), BitmapFactory.decodeResource(resources, R.drawable.meter_flecha2_09, options))

        METER_FLECHA_GRAPHICS_BITMAP = arrayOf(METER_FLECHA1_GRAPHICS_BITMAP[0], METER_FLECHA2_GRAPHICS_BITMAP[0])

        LEVEL_UP_GRAPHICS_BITMAP = arrayOf(BitmapFactory.decodeResource(resources, R.drawable.level_up_01, options), BitmapFactory.decodeResource(resources, R.drawable.level_up_02, options))
    }

    fun resizeGraphics(surfaceWidth: Int, surfaceHeight: Int) {
        //Log.d(TAG, "Surface resize: width = " + surfaceWidth + ", height = " + surfaceHeight);

        // Calculatemos el refactor index ( siempre en funcion del width ?? )
        var refactorIndex = surfaceWidth.toFloat()
        refactorIndex /= DEFAULT_SURFACE_WIDTH.toFloat()
        // Prevencin de cosas raras
        if (refactorIndex == 0f) {
            refactorIndex = 1f
        }
        var refactorIndexHeight = surfaceHeight.toFloat()
        refactorIndexHeight /= DEFAULT_SURFACE_HEIGHT.toFloat()
        // Prevencin de cosas raras
        if (refactorIndexHeight == 0f) {
            refactorIndexHeight = 1f
        }

        // Cogemos el valor por debajo
        val width = java.lang.Float.valueOf(DEFAULT_WIDTH * refactorIndex).toInt()
        val height = java.lang.Float.valueOf(DEFAULT_HEIGHT * refactorIndex).toInt()
        val pixel_move = java.lang.Float.valueOf(DEFAULT_PIXEL_MOVE * refactorIndex).toInt()

        WIDTH = width
        HEIGHT = height
        PIXEL_MOVE = pixel_move

        var i = 0
        for (bitmap in CARA_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                CARA_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            }
            i++
        }

        i = 0
        for (bitmap in OJOS_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                OJOS_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            }
            i++
        }

        i = 0
        for (bitmap in BOCA_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                BOCA_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, height, true)
            }
            i++
        }

        // El meter es cuadrado !!

        i = 0
        for (bitmap in METER_FONDO_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                METER_FONDO_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, width, true)
            }
            i++
        }

        i = 0
        for (bitmap in METER_FLECHA_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                METER_FLECHA_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, width, true)
            }
            i++
        }

        i = 0
        for (bitmap in METER_FLECHA1_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                METER_FLECHA1_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, width, true)
            }
            i++
        }

        i = 0
        for (bitmap in METER_FLECHA2_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                METER_FLECHA2_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, width, true)
            }
            i++
        }

        i = 0
        for (bitmap in LEVEL_UP_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                LEVEL_UP_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, width, width, true)
            }
            i++
        }

        // Cogemos el valor por debajo
        val dialogo_corto_width = java.lang.Float.valueOf(DEFAULT_DIALOGO_CORTO_WIDTH * refactorIndex).toInt()
        val dialogo_medio_width = java.lang.Float.valueOf(DEFAULT_DIALOGO_MEDIO_WIDTH * refactorIndex).toInt()
        val dialogo_medio2_width = java.lang.Float.valueOf(DEFAULT_DIALOGO_MEDIO2_WIDTH * refactorIndex).toInt()
        val dialogo_largo_width = java.lang.Float.valueOf(DEFAULT_DIALOGO_LARGO_WIDTH * refactorIndex).toInt()
        val dialogo_height = java.lang.Float.valueOf(DEFAULT_DIALOGO_HEIGHT * refactorIndexHeight).toInt()

        DIALOGO_CORTO_WIDTH = dialogo_corto_width
        DIALOGO_MEDIO_WIDTH = dialogo_medio_width
        DIALOGO_MEDIO2_WIDTH = dialogo_medio2_width
        DIALOGO_LARGO_WIDTH = dialogo_largo_width
        DIALOGO_HEIGHT = dialogo_height

        i = 0
        for (bitmap in DIALOGO_CORTO_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                DIALOGO_CORTO_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, dialogo_corto_width, dialogo_height, true)
            }
            i++
        }

        i = 0
        for (bitmap in DIALOGO_MEDIO_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                DIALOGO_MEDIO_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, dialogo_medio_width, dialogo_height, true)
            }
            i++
        }

        i = 0
        for (bitmap in DIALOGO_MEDIO2_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                DIALOGO_MEDIO2_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, dialogo_medio2_width, dialogo_height, true)
            }
            i++
        }

        i = 0
        for (bitmap in DIALOGO_LARGO_GRAPHICS_BITMAP) {
            if (bitmap != null) {
                DIALOGO_LARGO_GRAPHICS_BITMAP[i] = Bitmap.createScaledBitmap(bitmap, dialogo_largo_width, dialogo_height, true)
            }
            i++
        }
    }

    fun destroy() {
        ResourceUtils.recicleBitmaps(CARA_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(OJOS_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(BOCA_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(METER_FONDO_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(DIALOGO_CORTO_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(DIALOGO_MEDIO_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(DIALOGO_MEDIO2_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(DIALOGO_LARGO_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(METER_FLECHA1_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(METER_FLECHA2_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(METER_FLECHA_GRAPHICS_BITMAP)
        ResourceUtils.recicleBitmaps(LEVEL_UP_GRAPHICS_BITMAP)
    }
}
