package com.mya.games.colipop.character;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.mya.games.colipop.ColiPopResources;
import com.mya.games.colipop.board.ThingResources;

import java.util.Random;

public abstract class Character {

    static final String TAG = "ColiPop";

    public static final int POSICION_LEFT = 0;
    public static final int POSICION_RIGHT = 1;
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_HAPPY = 1;
    public static final int STATUS_UNHAPPY = 2;
    public static final int STATUS_TALKING = 3;
    static final int DEFAULT_SURFACE_WIDTH = 800;
    static final int DEFAULT_SURFACE_HEIGHT = 480;
    static final int DEFAULT_POSICION_LEFT_OFFSET_X = 10;
    static final int DEFAULT_POSICION_LEFT_OFFSET_Y = 60;
    static final int DEFAULT_POSICION_RIGHT_OFFSET_X = 625;
    static final int DEFAULT_POSICION_RIGHT_OFFSET_Y = 60;
    protected static Random random = new Random();
    // Posicion del character
    protected int posicion = 0;
    // Offsets del character
    protected int offsetX = 0;
    protected int offsetY = 0;
    protected int meterOffsetX = 0;
    protected int meterOffsetY = 0;
    protected int dialogoOffsetX = 0;
    protected int dialogoOffsetY = 0;
    protected int levelOffsetX = 0;
    protected int levelOffsetY = 0;
    // Indice animation
    protected int animationIndex = 0;
    // Indice Happy Meter;
    protected int meterIndex = 0;
    protected int currentLevel = 0;
    protected boolean showLevelUpText = false;
    protected int levelUpTextIndex = 0;
    // Propiedades characters
    protected boolean gameover = false;
    protected boolean winner = false;
    protected int mainThingType;
    protected int status = 0;
    protected String talkingText = "";
    protected String[] normalTalkingText;
    protected String[] happyTalkingText;
    protected String[] unhappyTalkingText;
    protected String[] levelUpTalkingText;
    // Resources
    Resources resources = null;

    public Character(Resources resources, int posicion) {
        this.resources = resources;
        this.posicion = posicion;
    }

    public boolean isGameover() {
        return gameover;
    }

    public void setGameover(boolean gameover) {
        this.gameover = gameover;
    }

    public int getMainThingType() {
        return mainThingType;
    }

    public void setMainThingType(int mainThingType) {
        this.mainThingType = mainThingType;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public void setAnimationIndex(int animationIndex) {
        this.animationIndex = animationIndex;
    }

    public int getMeterIndex() {
        return meterIndex;
    }

    public void setMeterIndex(int meterIndex) {
        this.meterIndex = meterIndex;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public boolean isShowLevelUpText() {
        return showLevelUpText;
    }

    public void setShowLevelUpText(boolean showLevelUpText) {
        this.showLevelUpText = showLevelUpText;
    }

    public int getLevelUpTextIndex() {
        return levelUpTextIndex;
    }

    public void setLevelUpTextIndex(int levelUpTextIndex) {
        this.levelUpTextIndex = levelUpTextIndex;
    }

    public String getTalkingText() {
        return talkingText;
    }

    public void setTalkingText(String talkingText) {
        this.talkingText = talkingText;
    }

    public String[] getNormalTalkingText() {
        return normalTalkingText;
    }

    public void setNormalTalkingText(String[] normalTalkingText) {
        this.normalTalkingText = normalTalkingText;
    }

    public String[] getHappyTalkingText() {
        return happyTalkingText;
    }

    public void setHappyTalkingText(String[] happyTalkingText) {
        this.happyTalkingText = happyTalkingText;
    }

    public String[] getUnhappyTalkingText() {
        return unhappyTalkingText;
    }

    public void setUnhappyTalkingText(String[] unhappyTalkingText) {
        this.unhappyTalkingText = unhappyTalkingText;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Calculate offsets en funcion de la posicion del character y los tamaos indicados
     *
     * @param surfaceWidth
     * @param surfaceHeight
     */
    protected void calculatePositionOffsets(int surfaceWidth, int surfaceHeight) {

        // Calculatemos el refactor indexes
        float refactorIndexWidth = surfaceWidth;
        refactorIndexWidth /= DEFAULT_SURFACE_WIDTH;
        // Prevencin de cosas raras
        if (refactorIndexWidth == 0) {
            refactorIndexWidth = 1;
        }

        float refactorIndexHeight = surfaceHeight;
        refactorIndexHeight /= DEFAULT_SURFACE_HEIGHT;
        // Prevencin de cosas raras
        if (refactorIndexHeight == 0) {
            refactorIndexHeight = 1;
        }

        if (this.posicion == POSICION_LEFT) {
            this.offsetX = Float.valueOf(DEFAULT_POSICION_LEFT_OFFSET_X * refactorIndexWidth).intValue();
            this.offsetY = Float.valueOf(DEFAULT_POSICION_LEFT_OFFSET_Y * refactorIndexHeight).intValue();
        } else {
            this.offsetX = Float.valueOf(DEFAULT_POSICION_RIGHT_OFFSET_X * refactorIndexWidth).intValue();
            this.offsetY = Float.valueOf(DEFAULT_POSICION_RIGHT_OFFSET_Y * refactorIndexHeight).intValue();
        }

        this.meterOffsetX = offsetX;
        this.meterOffsetY = Double.valueOf(4.25 * offsetY).intValue();

        this.dialogoOffsetX = 2 * offsetX;
        this.dialogoOffsetY = Double.valueOf(0.55 * offsetY).intValue();

        this.levelOffsetX = 10 * meterOffsetX;
        this.levelOffsetY = Double.valueOf(1.75 * meterOffsetY).intValue();

    }

    public void doTalking(int text) {
        this.talkingText = resources.getString(text);
        this.animationIndex = 0;
        this.status = STATUS_TALKING;
    }

    protected void doTalking(String text) {
        this.talkingText = text;
        this.animationIndex = 0;
        this.status = STATUS_TALKING;
    }

    protected void doTalking(String[] text) {
        if (text == null || text.length == 0) {
            return;
        }
        // Random talking
        int textNum = random.nextInt(text.length - 1);
        doTalking(text[textNum]);
    }

    protected void doTalking(String[] text, int textNum) {
        if (text == null || text.length == 0) {
            return;
        }
        // textNum is 1 based index
        if (textNum < 1 || textNum > text.length) {
            return;
        }
        doTalking(text[textNum - 1]);
    }

    public void doNormalTalking() {
        doTalking(this.normalTalkingText);
    }

    public void doHappyTalking(int textNum) {
        doTalking(this.happyTalkingText, textNum);
    }

    public void doUnhappyTalking(int textNum) {
        doTalking(this.unhappyTalkingText, textNum);
    }

    public void doLevelUpTalking(int textNum) {
        doTalking(this.levelUpTalkingText, textNum);
    }

    /**
     * @param thingType
     */
    public void updateMeter(int thingType, int numThings) {
        int index = 0;
        int baseIndex = 1;
        if (numThings >= 3) {
            baseIndex = numThings - 2;
        }
        int levelIndex = baseIndex + (3 - currentLevel);
        if (levelIndex < 1) {
            levelIndex = 1;
        }
        if (thingType == ThingResources.PIRULETA_OBJECT_TYPE) {
            index = levelIndex;
        } else if (thingType == ThingResources.CARAMELO_OBJECT_TYPE) {
            index = levelIndex;
        } else if (thingType == ThingResources.PEINE_OBJECT_TYPE) {
            index = -baseIndex;
        } else if (thingType == ThingResources.RASPA_OBJECT_TYPE) {
            index = -baseIndex;
        }
        // Updatemos contador global
        this.meterIndex += index;
        if (this.meterIndex < 0) {
            this.meterIndex = 0;
        }
        // Character talking
        if (index > 0) {
            // Nice object
            int meterIndex = this.meterIndex;
            if (meterIndex == 10) {
                doLevelUpTalking(1);
            } else if (meterIndex == 15) {
                doLevelUpTalking(2);
            } else if (meterIndex >= 20) {
                doLevelUpTalking(3);
            } else {
                doHappyTalking(index);
            }
        } else {
            // Ugly object
            if (thingType == ThingResources.RASPA_OBJECT_TYPE) {
                doUnhappyTalking(1);
            } else if (thingType == ThingResources.PEINE_OBJECT_TYPE) {
                doUnhappyTalking(2);
            }
        }
		
		/*
		if ( this.mMeterIndex < gameoverMeterIndex ) {
			this.gameover = true;
		}
		*/
        if (meterIndex >= 20) {
            currentLevel += 1;
            meterIndex = 0;
            showLevelUpText = true;
            // Cambiamos el fondo del board
            ColiPopResources.changeLevelBackground(resources, currentLevel);
        }
        // Updateamos status del meter
        updateMeterStatus(meterIndex * 9);

    }

    public abstract void initCharacter();

    /**
     * @param surfaceWidth
     * @param surfaceHeight
     */
    public abstract void resizeGraphics(int surfaceWidth, int surfaceHeight);

    /**
     * @param canvas
     */
    public abstract void doCharacterAnimation(Canvas canvas);


    /**
     * @param index
     */
    public abstract void updateMeterStatus(int index);

    /**
     *
     */
    public abstract void destroy();

}
