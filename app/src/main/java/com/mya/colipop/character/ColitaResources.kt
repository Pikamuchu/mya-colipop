package com.mya.colipop.character

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.mya.colipop.R
import com.mya.colipop.ResourceUtils

/**
 * Colita character object resources.
 */
object ColitaResources {

    private val TAG = "ColiPop"

    // Colita baseline sizes
    private const val DEFAULT_SURFACE_WIDTH = 800
    private const val DEFAULT_SURFACE_HEIGHT = 480
    private const val DEFAULT_WIDTH = 150
    private const val DEFAULT_HEIGHT = 125
    private const val DEFAULT_PIXEL_MOVE = 0
    // Dialogo baseline Sizes
    private const val DEFAULT_DIALOGO_CORTO_WIDTH = 150
    private const val DEFAULT_DIALOGO_MEDIO_WIDTH = 300
    private const val DEFAULT_DIALOGO_MEDIO2_WIDTH = 450
    private const val DEFAULT_DIALOGO_LARGO_WIDTH = 600
    private const val DEFAULT_DIALOGO_HEIGHT = 75
    // Colita sizes
    private var WIDTH = DEFAULT_WIDTH
    private var HEIGHT = DEFAULT_HEIGHT
    private var PIXEL_MOVE = DEFAULT_PIXEL_MOVE
    // Dialogo Sizes
    private var DIALOGO_CORTO_WIDTH = DEFAULT_DIALOGO_CORTO_WIDTH
    private var DIALOGO_MEDIO_WIDTH = DEFAULT_DIALOGO_MEDIO_WIDTH
    private var DIALOGO_MEDIO2_WIDTH = DEFAULT_DIALOGO_MEDIO2_WIDTH
    private var DIALOGO_LARGO_WIDTH = DEFAULT_DIALOGO_LARGO_WIDTH
    private var DIALOGO_HEIGHT = DEFAULT_DIALOGO_HEIGHT

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

    /**
     * Perform graphic initialization.
     */
    fun initializeGraphics(resources: Resources) {
        val options = BitmapFactory.Options()

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

    /**
     * Perform graphic resize to adapt to the device screen.
     */
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

        for ((index, bitmap) in CARA_GRAPHICS_BITMAP.withIndex()) {
            CARA_GRAPHICS_BITMAP[index] = Bitmap.createScaledBitmap(bitmap, width, height, true)
        }

        for ((index, bitmap) in OJOS_GRAPHICS_BITMAP.withIndex()) {
            OJOS_GRAPHICS_BITMAP[index] = Bitmap.createScaledBitmap(bitmap, width, height, true)
        }

        for ((index, bitmap) in BOCA_GRAPHICS_BITMAP.withIndex()) {
            BOCA_GRAPHICS_BITMAP[index] = Bitmap.createScaledBitmap(bitmap, width, height, true)
        }

        // El meter es cuadrado

        for ((index, bitmap) in METER_FONDO_GRAPHICS_BITMAP.withIndex()) {
            METER_FONDO_GRAPHICS_BITMAP[index] = Bitmap.createScaledBitmap(bitmap, width, width, true)
        }

        for ((index, bitmap) in METER_FLECHA_GRAPHICS_BITMAP.withIndex()) {
            METER_FLECHA_GRAPHICS_BITMAP[index] = Bitmap.createScaledBitmap(bitmap, width, width, true)
        }

        for ((index, bitmap) in METER_FLECHA1_GRAPHICS_BITMAP.withIndex()) {
            METER_FLECHA1_GRAPHICS_BITMAP[index] = Bitmap.createScaledBitmap(bitmap, width, width, true)
        }

        for ((index, bitmap) in METER_FLECHA2_GRAPHICS_BITMAP.withIndex()) {
            METER_FLECHA2_GRAPHICS_BITMAP[index] = Bitmap.createScaledBitmap(bitmap, width, width, true)
        }

        for ((index, bitmap) in LEVEL_UP_GRAPHICS_BITMAP.withIndex()) {
            LEVEL_UP_GRAPHICS_BITMAP[index] = Bitmap.createScaledBitmap(bitmap, width, width, true)
        }

        // Cogemos el valor por debajo
        val dialogoCortoWidth = java.lang.Float.valueOf(DEFAULT_DIALOGO_CORTO_WIDTH * refactorIndex).toInt()
        val dialogoMedioWidth = java.lang.Float.valueOf(DEFAULT_DIALOGO_MEDIO_WIDTH * refactorIndex).toInt()
        val dialogoMedio2Width = java.lang.Float.valueOf(DEFAULT_DIALOGO_MEDIO2_WIDTH * refactorIndex).toInt()
        val dialogoLargoWidth = java.lang.Float.valueOf(DEFAULT_DIALOGO_LARGO_WIDTH * refactorIndex).toInt()
        val dialogoHeight = java.lang.Float.valueOf(DEFAULT_DIALOGO_HEIGHT * refactorIndexHeight).toInt()

        DIALOGO_CORTO_WIDTH = dialogoCortoWidth
        DIALOGO_MEDIO_WIDTH = dialogoMedioWidth
        DIALOGO_MEDIO2_WIDTH = dialogoMedio2Width
        DIALOGO_LARGO_WIDTH = dialogoLargoWidth
        DIALOGO_HEIGHT = dialogoHeight

        for ((index, bitmap) in DIALOGO_CORTO_GRAPHICS_BITMAP.withIndex()) {
            DIALOGO_CORTO_GRAPHICS_BITMAP[index] = Bitmap.createScaledBitmap(bitmap, dialogoCortoWidth, dialogoHeight, true)
        }

        for ((index, bitmap) in DIALOGO_MEDIO_GRAPHICS_BITMAP.withIndex()) {
            DIALOGO_MEDIO_GRAPHICS_BITMAP[index] = Bitmap.createScaledBitmap(bitmap, dialogoMedioWidth, dialogoHeight, true)
        }

        for ((index, bitmap) in DIALOGO_MEDIO2_GRAPHICS_BITMAP.withIndex()) {
            DIALOGO_MEDIO2_GRAPHICS_BITMAP[index] = Bitmap.createScaledBitmap(bitmap, dialogoMedio2Width, dialogoHeight, true)
        }

        for ((index, bitmap) in DIALOGO_LARGO_GRAPHICS_BITMAP.withIndex()) {
            DIALOGO_LARGO_GRAPHICS_BITMAP[index] = Bitmap.createScaledBitmap(bitmap, dialogoLargoWidth, dialogoHeight, true)
        }
    }

    /**
     * Perform graphic recycle.
     */
    fun destroy() {
        ResourceUtils.recycleBitmaps(CARA_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(OJOS_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(BOCA_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(METER_FONDO_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(DIALOGO_CORTO_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(DIALOGO_MEDIO_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(DIALOGO_MEDIO2_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(DIALOGO_LARGO_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(METER_FLECHA1_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(METER_FLECHA2_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(METER_FLECHA_GRAPHICS_BITMAP)
        ResourceUtils.recycleBitmaps(LEVEL_UP_GRAPHICS_BITMAP)
    }
}
