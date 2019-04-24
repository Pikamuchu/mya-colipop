package com.mya.colipop

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint

/**
 * Manages colipop resources.
 */
object ColiPopResources {

    private val TAG = "ColiPop"

    var DEFAULT_SURFACE_WIDTH = 800
    var DEFAULT_SURFACE_HEIGHT = 480

    var SURFACE_WIDTH = DEFAULT_SURFACE_WIDTH
    var SURFACE_HEIGHT = DEFAULT_SURFACE_HEIGHT

    // Fondos de pantalla de juego
    var backgroundImage: Bitmap? = null
    var boardImage: Bitmap? = null

    // Fondos de titulos
    var titleBG: Bitmap? = null

    // Formato de textos
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var paintText = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * Perform graphic initialization.
     */
    fun initializeGraphics(resources: Resources) {
        // Fondo en baja calidad
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565

        // Fondos de los titulos
        this.titleBG = BitmapFactory.decodeResource(resources, R.drawable.title_background, options)

        // Fondos de boards de juego
        this.backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_01, options)

        // Board es muy baja calidad
        options.inPreferredConfig = Bitmap.Config.ARGB_4444
        this.boardImage = BitmapFactory.decodeResource(resources, R.drawable.tablero, options)
    }

    /**
     * Perform graphic resize to adapt to the device screen.
     */
    fun resizeGraphics(width: Int, height: Int) {
        SURFACE_WIDTH = width
        SURFACE_HEIGHT = height

        // Reescalado de fondos
        if (this.titleBG != null) {
            this.titleBG = Bitmap.createScaledBitmap(titleBG, width, height, true)
        }
        if (this.backgroundImage != null) {
            this.backgroundImage = Bitmap.createScaledBitmap(backgroundImage, width, height, true)
        }
        if (this.boardImage != null) {
            this.boardImage = Bitmap.createScaledBitmap(boardImage, width, height, true)
        }

        var refactorIndex = width.toFloat()
        refactorIndex /= DEFAULT_SURFACE_WIDTH.toFloat()
        // Prevencin de cosas raras
        if (refactorIndex == 0f) {
            refactorIndex = 1f
        }

        // Formato Textos
        this.paint.textSize = 20 * refactorIndex
        this.paint.color = Color.BLACK

        // Formato Talking Text
        this.paintText.textSize = 24 * refactorIndex
        this.paintText.color = Color.BLACK
    }

    /**
     * Perform background change logic.
     */
    fun changeLevelBackground(resources: Resources?, level: Int) {
        if (resources == null) {
            return
        }

        val imageToRecicle = this.backgroundImage

        // Fondos de boards de juego
        var normLevel = (level + 1) % 10
        var levelBackgroundImage: Bitmap? = null
        when (normLevel) {
            1 -> levelBackgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_01)
            2 -> levelBackgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_02)
            3 -> levelBackgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_03)
            4 -> levelBackgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_04)
            5 -> levelBackgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_05)
            6 -> levelBackgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_06)
            7 -> levelBackgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_07)
            8 -> levelBackgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_08)
            9 -> levelBackgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_09)
            0 -> levelBackgroundImage = BitmapFactory.decodeResource(resources, R.drawable.background_10)
        }
        if (levelBackgroundImage != null) {
            this.backgroundImage = Bitmap.createScaledBitmap(levelBackgroundImage, SURFACE_WIDTH, SURFACE_HEIGHT, true)
        }

        imageToRecicle?.recycle()
    }

    /**
     * Perform graphic recycle.
     */
    fun destroy() {
        this.titleBG?.recycle()
        this.backgroundImage?.recycle()
        this.boardImage?.recycle()
    }
}
