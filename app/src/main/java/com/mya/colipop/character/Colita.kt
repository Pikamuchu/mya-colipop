package com.mya.colipop.character

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix

import com.mya.colipop.ColiPopResources
import com.mya.colipop.R

class Colita(resources: Resources, position: Int) : Character(resources, position) {

    private val TAG = "ColiPop"

    init {
        ColitaResources.initializeGraphics(resources)

        // Initailizing text resources
        this.normalTalkingText = resources.getStringArray(R.array.colita_talking_normal)
        this.happyTalkingText = resources.getStringArray(R.array.colita_talking_happy)
        this.unhappyTalkingText = resources.getStringArray(R.array.colita_talking_unhappy)
        this.levelUpTalkingText = resources.getStringArray(R.array.colita_talking_levelup)

        initCharacter()
    }

    override fun initCharacter() {
        super.initCharacter()

        animationIndex = 0
        meterIndex = 0
        currentLevel = 0
        isShowLevelUpText = false
        levelUpTextIndex = 0
        isGameover = false
        isWinner = false
        status = 0

        updateMeterStatus(0)

        this.talkingText = resources!!.getString(R.string.colita_talking_hello)
        this.status = Character.STATUS_TALKING
    }

    override fun resizeGraphics(surfaceWidth: Int, surfaceHeight: Int) {
        calculatePositionOffsets(surfaceWidth, surfaceHeight)

        ColitaResources.resizeGraphics(surfaceWidth, surfaceHeight)
    }

    override fun doCharacterAnimation(canvas: Canvas) {
        // cambiamos el indice de animacin
        this.animationIndex++
        // Hacemos loop
        if (this.animationIndex >= ColitaResources.CARA_ANIMATION_SEQUENCE.size) {
            this.animationIndex = 0
            this.status = Character.STATUS_NORMAL
        }

        // Localizing variables
        val animationIndex = this.animationIndex
        var offsetX = this.offsetX
        var offsetY = this.offsetY

        // Animate Cara
        var graphicIndex = ColitaResources.CARA_ANIMATION_SEQUENCE[animationIndex]
        if (graphicIndex >= ColitaResources.CARA_GRAPHICS_BITMAP.size) {
            graphicIndex = 0
        }
        canvas.drawBitmap(ColitaResources.CARA_GRAPHICS_BITMAP[graphicIndex], offsetX.toFloat(), offsetY.toFloat(), null)

        // Animate Ojos
        graphicIndex = ColitaResources.OJOS_ANIMATION_SEQUENCE[animationIndex]
        if (graphicIndex >= ColitaResources.OJOS_GRAPHICS_BITMAP.size) {
            graphicIndex = 0
        }
        canvas.drawBitmap(ColitaResources.OJOS_GRAPHICS_BITMAP[graphicIndex], offsetX.toFloat(), offsetY.toFloat(), null)

        // Animate Boca
        val status = this.status
        if (status == Character.STATUS_NORMAL) {
            graphicIndex = ColitaResources.BOCA_NORMAL_ANIMATION_SEQUENCE[animationIndex]
            if (graphicIndex >= ColitaResources.BOCA_GRAPHICS_BITMAP.size) {
                graphicIndex = 0
            }
            canvas.drawBitmap(ColitaResources.BOCA_GRAPHICS_BITMAP[graphicIndex], offsetX.toFloat(), offsetY.toFloat(), null)
        } else if (status == Character.STATUS_HAPPY) {
            graphicIndex = ColitaResources.BOCA_HAPPY_ANIMATION_SEQUENCE[animationIndex]
            if (graphicIndex >= ColitaResources.BOCA_GRAPHICS_BITMAP.size) {
                graphicIndex = 0
            }
            canvas.drawBitmap(ColitaResources.BOCA_GRAPHICS_BITMAP[graphicIndex], offsetX.toFloat(), offsetY.toFloat(), null)
        } else if (status == Character.STATUS_UNHAPPY) {
            graphicIndex = ColitaResources.BOCA_UNHAPPY_ANIMATION_SEQUENCE[animationIndex]
            if (graphicIndex >= ColitaResources.BOCA_GRAPHICS_BITMAP.size) {
                graphicIndex = 0
            }
            canvas.drawBitmap(ColitaResources.BOCA_GRAPHICS_BITMAP[graphicIndex], offsetX.toFloat(), offsetY.toFloat(), null)
        } else if (status == Character.STATUS_TALKING) {
            graphicIndex = ColitaResources.BOCA_TALKING_ANIMATION_SEQUENCE[animationIndex]
            if (graphicIndex >= ColitaResources.BOCA_GRAPHICS_BITMAP.size) {
                graphicIndex = 0
            }
            canvas.drawBitmap(ColitaResources.BOCA_GRAPHICS_BITMAP[graphicIndex], offsetX.toFloat(), offsetY.toFloat(), null)
        }

        // Animation texto
        if (status == Character.STATUS_TALKING) {
            graphicIndex = ColitaResources.DIALOGO_ANIMATION_SEQUENCE[animationIndex]
            if (graphicIndex >= ColitaResources.DIALOGO_CORTO_GRAPHICS_BITMAP.size) {
                graphicIndex = 0
            }
            val talkingText = this.talkingText
            if (talkingText.length < ColitaResources.TEXTO_CORTO) {
                canvas.drawBitmap(ColitaResources.DIALOGO_CORTO_GRAPHICS_BITMAP[graphicIndex], 0f, 0f, null)
            } else if (talkingText.length < ColitaResources.TEXTO_MEDIO) {
                canvas.drawBitmap(ColitaResources.DIALOGO_MEDIO_GRAPHICS_BITMAP[graphicIndex], 0f, 0f, null)
            } else if (talkingText.length < ColitaResources.TEXTO_MEDIO2) {
                canvas.drawBitmap(ColitaResources.DIALOGO_MEDIO2_GRAPHICS_BITMAP[graphicIndex], 0f, 0f, null)
            } else {
                canvas.drawBitmap(ColitaResources.DIALOGO_LARGO_GRAPHICS_BITMAP[graphicIndex], 0f, 0f, null)
            }

            canvas.drawText(this.talkingText, this.dialogoOffsetX.toFloat(), this.dialogoOffsetY.toFloat(), ColiPopResources.paintText)
        }

        // Animate Meter
        offsetY = this.meterOffsetY
        offsetX = this.meterOffsetX

        // De momento el fondo es una imagen fija
        canvas.drawBitmap(ColitaResources.METER_FONDO_GRAPHICS_BITMAP[0], offsetX.toFloat(), offsetY.toFloat(), null)

        // Animacin de cambio de nivel
        if (this.isShowLevelUpText) {
            // cambiamos el indice de animacin epecifico del texto level up
            this.levelUpTextIndex++
            if (this.levelUpTextIndex >= ColitaResources.LEVEL_UP_ANIMATION_SEQUENCE.size) {
                this.levelUpTextIndex = 0
                this.isShowLevelUpText = false
            }
            // Animate Texto Level up
            graphicIndex = ColitaResources.LEVEL_UP_ANIMATION_SEQUENCE[this.levelUpTextIndex]
            if (graphicIndex >= ColitaResources.LEVEL_UP_GRAPHICS_BITMAP.size) {
                graphicIndex = 0
            }
            canvas.drawBitmap(ColitaResources.LEVEL_UP_GRAPHICS_BITMAP[graphicIndex], offsetX.toFloat(), offsetY.toFloat(), null)
        }

        // Flecha
        graphicIndex = ColitaResources.METER_FLECHA_ANIMATION_SEQUENCE[animationIndex]
        if (graphicIndex >= ColitaResources.METER_FLECHA_GRAPHICS_BITMAP.size) {
            graphicIndex = 0
        }
        canvas.drawBitmap(ColitaResources.METER_FLECHA_GRAPHICS_BITMAP[graphicIndex], offsetX.toFloat(), offsetY.toFloat(), null)

        // Level value
        offsetY = this.levelOffsetY
        offsetX = this.levelOffsetX

        canvas.drawText(" = " + (this.currentLevel + 1), offsetX.toFloat(), offsetY.toFloat(), ColiPopResources.paint)
    }

    override fun updateMeterStatus(index: Int) {
        // Rotate Matrix
        val mtx = Matrix()
        if (index < 81) {
            mtx.postRotate(0f)
        } else if (index < 171) {
            mtx.postRotate(90f)
        } else {
            mtx.postRotate(180f)
        }
        // Calculatendo bitmap index
        var bitmapIndex: Int
        if (index < 90) {
            bitmapIndex = index
        } else {
            bitmapIndex = index - 90
        }
        bitmapIndex /= 9
        if (bitmapIndex >= ColitaResources.METER_FLECHA1_GRAPHICS_BITMAP.size) {
            bitmapIndex = 0
        }
        val flechaBitmaps = arrayOf(ColitaResources.METER_FLECHA1_GRAPHICS_BITMAP[bitmapIndex], ColitaResources.METER_FLECHA2_GRAPHICS_BITMAP[bitmapIndex])
        // Rotating Bitmap
        for ((i, bitmap) in flechaBitmaps.withIndex()) {
            ColitaResources.METER_FLECHA_GRAPHICS_BITMAP[i] = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, mtx, true)
        }
    }

    override fun destroy() {
        ColitaResources.destroy()
    }
}
