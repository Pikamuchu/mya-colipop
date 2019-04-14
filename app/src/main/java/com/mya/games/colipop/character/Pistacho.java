package com.mya.games.colipop.character;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.mya.games.colipop.ColiPopResources;
import com.mya.games.colipop.R;

public class Pistacho extends Character {

    static final String TAG = "ColiPop";

    public Pistacho(Resources resources, int posicion) {
        super(resources, posicion);

        ColitaResources.initializeGraphics(resources);

        // Initailizing text resources
        this.normalTalkingText = resources.getStringArray(R.array.colita_talking_normal);
        this.happyTalkingText = resources.getStringArray(R.array.colita_talking_happy);
        this.unhappyTalkingText = resources.getStringArray(R.array.colita_talking_unhappy);
        this.levelUpTalkingText = resources.getStringArray(R.array.colita_talking_levelup);

    }

    @Override
    public void initCharacter() {
        this.animationIndex = 0;

        this.meterIndex = 0;
        this.currentLevel = 0;
        this.showLevelUpText = false;
        this.levelUpTextIndex = 0;

        this.gameover = false;
        this.winner = false;
        this.status = 0;

        this.talkingText = resources.getString(R.string.colita_talking_hello);
        this.status = STATUS_TALKING;
    }

    public void resizeGraphics(int surfaceWidth, int surfaceHeight) {
        calculatePositionOffsets(surfaceWidth, surfaceHeight);
        ColitaResources.resizeGraphics(surfaceWidth, surfaceHeight);
    }

    public void doCharacterAnimation(Canvas canvas) {
        // cambiamos el indice de animacin
        this.animationIndex++;
        // Hacemos loop
        if (this.animationIndex >= ColitaResources.CARA_ANIMATION_SEQUENCE.length) {
            this.animationIndex = 0;
            this.status = STATUS_NORMAL;
        }

        // Localizing variables
        int animationIndex = this.animationIndex;
        int offsetX = this.offsetX;
        int offsetY = this.offsetY;

        // Animate Cara
        int graphicIndex = ColitaResources.CARA_ANIMATION_SEQUENCE[animationIndex];
        if (graphicIndex >= ColitaResources.CARA_GRAPHICS_BITMAP.length) {
            graphicIndex = 0;
        }
        canvas.drawBitmap(ColitaResources.CARA_GRAPHICS_BITMAP[graphicIndex], offsetX, offsetY, null);

        // Animate Ojos
        graphicIndex = ColitaResources.OJOS_ANIMATION_SEQUENCE[animationIndex];
        if (graphicIndex >= ColitaResources.OJOS_GRAPHICS_BITMAP.length) {
            graphicIndex = 0;
        }
        canvas.drawBitmap(ColitaResources.OJOS_GRAPHICS_BITMAP[graphicIndex], offsetX, offsetY, null);

        // Animate Boca
        int status = this.status;
        if (status == STATUS_NORMAL) {
            graphicIndex = ColitaResources.BOCA_NORMAL_ANIMATION_SEQUENCE[animationIndex];
            if (graphicIndex >= ColitaResources.BOCA_GRAPHICS_BITMAP.length) {
                graphicIndex = 0;
            }
            canvas.drawBitmap(ColitaResources.BOCA_GRAPHICS_BITMAP[graphicIndex], offsetX, offsetY, null);
        } else if (status == STATUS_HAPPY) {
            graphicIndex = ColitaResources.BOCA_HAPPY_ANIMATION_SEQUENCE[animationIndex];
            if (graphicIndex >= ColitaResources.BOCA_GRAPHICS_BITMAP.length) {
                graphicIndex = 0;
            }
            canvas.drawBitmap(ColitaResources.BOCA_GRAPHICS_BITMAP[graphicIndex], offsetX, offsetY, null);
        } else if (status == STATUS_UNHAPPY) {
            graphicIndex = ColitaResources.BOCA_UNHAPPY_ANIMATION_SEQUENCE[animationIndex];
            if (graphicIndex >= ColitaResources.BOCA_GRAPHICS_BITMAP.length) {
                graphicIndex = 0;
            }
            canvas.drawBitmap(ColitaResources.BOCA_GRAPHICS_BITMAP[graphicIndex], offsetX, offsetY, null);
        } else if (status == STATUS_TALKING) {
            graphicIndex = ColitaResources.BOCA_TALKING_ANIMATION_SEQUENCE[animationIndex];
            if (graphicIndex >= ColitaResources.BOCA_GRAPHICS_BITMAP.length) {
                graphicIndex = 0;
            }
            canvas.drawBitmap(ColitaResources.BOCA_GRAPHICS_BITMAP[graphicIndex], offsetX, offsetY, null);
        }

        // Animation texto
        if (status == STATUS_TALKING) {
            graphicIndex = ColitaResources.DIALOGO_ANIMATION_SEQUENCE[animationIndex];
            if (graphicIndex >= ColitaResources.DIALOGO_CORTO_GRAPHICS_BITMAP.length) {
                graphicIndex = 0;
            }
            String talkingText = this.talkingText;
            if (talkingText == null || talkingText.length() < ColitaResources.TEXTO_CORTO) {
                canvas.drawBitmap(ColitaResources.DIALOGO_CORTO_GRAPHICS_BITMAP[graphicIndex], 0, 0, null);
            } else if (talkingText.length() < ColitaResources.TEXTO_MEDIO) {
                canvas.drawBitmap(ColitaResources.DIALOGO_MEDIO_GRAPHICS_BITMAP[graphicIndex], 0, 0, null);
            } else if (talkingText.length() < ColitaResources.TEXTO_MEDIO2) {
                canvas.drawBitmap(ColitaResources.DIALOGO_MEDIO_GRAPHICS_BITMAP[graphicIndex], 0, 0, null);
            } else {
                canvas.drawBitmap(ColitaResources.DIALOGO_LARGO_GRAPHICS_BITMAP[graphicIndex], 0, 0, null);
            }

            canvas.drawText(this.talkingText, this.dialogoOffsetX, this.dialogoOffsetY, ColiPopResources.paintText);
        }

        // Animate Meter
        offsetY = this.meterOffsetY;
        offsetX = this.meterOffsetX;

        // De momento el fondo es una imagen fija
        canvas.drawBitmap(ColitaResources.METER_FONDO_GRAPHICS_BITMAP[0], offsetX, offsetY, null);

        // Animacin de cambio de nivel
        if (this.showLevelUpText) {
            // cambiamos el indice de animacin epecifico del texto level up
            this.levelUpTextIndex++;
            if (this.levelUpTextIndex >= ColitaResources.LEVEL_UP_ANIMATION_SEQUENCE.length) {
                this.levelUpTextIndex = 0;
                this.showLevelUpText = false;
            }
            // Animate Texto Level up
            graphicIndex = ColitaResources.LEVEL_UP_ANIMATION_SEQUENCE[this.levelUpTextIndex];
            if (graphicIndex >= ColitaResources.LEVEL_UP_GRAPHICS_BITMAP.length) {
                graphicIndex = 0;
            }
            canvas.drawBitmap(ColitaResources.LEVEL_UP_GRAPHICS_BITMAP[graphicIndex], offsetX, offsetY, null);
        }

        // Flecha
        graphicIndex = ColitaResources.METER_FLECHA_ANIMATION_SEQUENCE[animationIndex];
        if (graphicIndex >= ColitaResources.METER_FLECHA_GRAPHICS_BITMAP.length) {
            graphicIndex = 0;
        }
        canvas.drawBitmap(ColitaResources.METER_FLECHA_GRAPHICS_BITMAP[graphicIndex], offsetX, offsetY, null);

        // Level value
        offsetY = this.levelOffsetY;
        offsetX = this.levelOffsetX;

        canvas.drawText(" = " + (this.currentLevel + 1), offsetX, offsetY, ColiPopResources.paint);
    }

    public void updateMeterStatus(int index) {
        // Rotate Matrix
        Matrix mtx = new Matrix();
        if (index < 81) {
            mtx.postRotate(0);
        } else if (index < 171) {
            mtx.postRotate(90);
        } else {
            mtx.postRotate(180);
        }
        // Calculatendo bitmap index
        int bitmapIndex = 0;
        if (index < 90) {
            bitmapIndex = index;
        } else {
            bitmapIndex = index - 90;
        }
        bitmapIndex = bitmapIndex / 9;
        if (bitmapIndex >= ColitaResources.METER_FLECHA1_GRAPHICS_BITMAP.length) {
            bitmapIndex = 0;
        }
        Bitmap[] flecha_bitmaps = new Bitmap[]{
                ColitaResources.METER_FLECHA1_GRAPHICS_BITMAP[bitmapIndex],
                ColitaResources.METER_FLECHA2_GRAPHICS_BITMAP[bitmapIndex]
        };
        // Rotating Bitmap
        int i = 0;
        for (Bitmap bitmap : flecha_bitmaps) {
            if (bitmap != null) {
                // El meter es cuadrado !!
                ColitaResources.METER_FLECHA_GRAPHICS_BITMAP[i] = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);
            }
            i++;
        }
    }

    @Override
    public void destroy() {
        PistachoResources.destroy();
    }
}
