package com.mya.colipop.character

import android.content.res.Resources
import android.graphics.Canvas
import com.mya.colipop.ColiPopResources
import com.mya.colipop.board.ThingResources
import java.util.*

abstract class Character(resources: Resources, position: Int) {

    private val TAG = "ColiPop"

    protected var resources: Resources? = null

    // Position del character
    var position = 0
    // Offsets del character
    var offsetX = 0
    var offsetY = 0
    protected var meterOffsetX = 0
    protected var meterOffsetY = 0
    protected var dialogoOffsetX = 0
    protected var dialogoOffsetY = 0
    protected var levelOffsetX = 0
    protected var levelOffsetY = 0
    // Indice animation
    var animationIndex = 0
    // Indice Happy Meter;
    var meterIndex = 0
    var currentLevel = 0
    var isShowLevelUpText = false
    var levelUpTextIndex = 0
    // Propiedades characters
    var isGameover = false
    var isWinner = false
    var status = 0
    var talkingText = ""
    lateinit var normalTalkingText: Array<String>
    lateinit var happyTalkingText: Array<String>
    lateinit var unhappyTalkingText: Array<String>
    lateinit var levelUpTalkingText: Array<String>

    init {
        this.resources = resources
        this.position = position
    }

    /**
     * Perform offsets calculation for the device surface sizes
     */
    protected fun calculatePositionOffsets(surfaceWidth: Int, surfaceHeight: Int) {

        // Calculatemos el refactor indexes
        var refactorIndexWidth = surfaceWidth.toFloat()
        refactorIndexWidth /= DEFAULT_SURFACE_WIDTH.toFloat()
        // Prevencin de cosas raras
        if (refactorIndexWidth == 0f) {
            refactorIndexWidth = 1f
        }

        var refactorIndexHeight = surfaceHeight.toFloat()
        refactorIndexHeight /= DEFAULT_SURFACE_HEIGHT.toFloat()
        if (refactorIndexHeight == 0f) {
            refactorIndexHeight = 1f
        }

        if (this.position == POSITION_LEFT) {
            this.offsetX = java.lang.Float.valueOf(DEFAULT_POSITION_LEFT_OFFSET_X * refactorIndexWidth).toInt()
            this.offsetY = java.lang.Float.valueOf(DEFAULT_POSITION_LEFT_OFFSET_Y * refactorIndexHeight).toInt()
        } else {
            this.offsetX = java.lang.Float.valueOf(DEFAULT_POSITION_RIGHT_OFFSET_X * refactorIndexWidth).toInt()
            this.offsetY = java.lang.Float.valueOf(DEFAULT_POSITION_RIGHT_OFFSET_Y * refactorIndexHeight).toInt()
        }

        this.meterOffsetX = offsetX
        this.meterOffsetY = java.lang.Double.valueOf(4.25 * offsetY).toInt()

        this.dialogoOffsetX = 2 * offsetX
        this.dialogoOffsetY = java.lang.Double.valueOf(0.55 * offsetY).toInt()

        this.levelOffsetX = 10 * meterOffsetX
        this.levelOffsetY = java.lang.Double.valueOf(1.75 * meterOffsetY).toInt()

    }

    fun doTalking(text: Int) {
        this.talkingText = resources!!.getString(text)
        this.animationIndex = 0
        this.status = STATUS_TALKING
    }

    private fun doTalking(text: String) {
        this.talkingText = text
        this.animationIndex = 0
        this.status = STATUS_TALKING
    }

    private fun doTalking(text: Array<String>?) {
        if (text == null || text.isEmpty()) {
            return
        }
        // Random talking
        val textNum = random.nextInt(text.size - 1)
        doTalking(text[textNum])
    }

    private fun doTalking(text: Array<String>?, textNum: Int) {
        if (text == null || text.isEmpty()) {
            return
        }
        // textNum is 1 based index
        if (textNum < 1 || textNum > text.size) {
            return
        }
        doTalking(text[textNum - 1])
    }

    fun doNormalTalking() {
        doTalking(this.normalTalkingText)
    }

    private fun doHappyTalking(textNum: Int) {
        doTalking(this.happyTalkingText, textNum)
    }

    private fun doUnhappyTalking(textNum: Int) {
        doTalking(this.unhappyTalkingText, textNum)
    }

    private fun doLevelUpTalking(textNum: Int) {
        doTalking(this.levelUpTalkingText, textNum)
    }

    fun updateMeter(thingType: Int, numThings: Int) {
        var index = 0
        var baseIndex = 1
        if (numThings >= 3) {
            baseIndex = numThings - 2
        }
        var levelIndex = baseIndex + (3 - currentLevel)
        if (levelIndex < 1) {
            levelIndex = 1
        }

        // calculating meter index
        when (thingType) {
            ThingResources.PIRULETA_OBJECT_TYPE -> index = levelIndex
            ThingResources.CARAMELO_OBJECT_TYPE -> index = levelIndex
            ThingResources.PEINE_OBJECT_TYPE -> index = -baseIndex
            ThingResources.RASPA_OBJECT_TYPE -> index = -baseIndex
        }

        // Updating global counter
        this.meterIndex += index
        if (this.meterIndex < 0) {
            this.meterIndex = 0
        }

        // Character talking
        if (index > 0) {
            // Nice object
            when {
                this.meterIndex == 10 -> doLevelUpTalking(1)
                this.meterIndex == 15 -> doLevelUpTalking(2)
                this.meterIndex >= 20 -> doLevelUpTalking(3)
                else -> doHappyTalking(index)
            }
        } else {
            // Ugly object
            when (thingType) {
                ThingResources.RASPA_OBJECT_TYPE -> doUnhappyTalking(1)
                ThingResources.PEINE_OBJECT_TYPE -> doUnhappyTalking(2)
            }
        }

        if (meterIndex >= 20) {
            currentLevel += 1
            meterIndex = 0
            isShowLevelUpText = true
            // Cambiamos el fondo del board
            ColiPopResources.changeLevelBackground(resources!!, currentLevel)
        }

        // Updateamos status del meter
        updateMeterStatus(meterIndex * 9)
    }

    open fun initCharacter() {
        this.meterIndex = 0
        this.currentLevel = 0
        this.isShowLevelUpText = false
        this.levelUpTextIndex = 0
        this.isGameover = false
        this.isWinner = false
        this.status = 0
    }

    abstract fun resizeGraphics(surfaceWidth: Int, surfaceHeight: Int)

    abstract fun doCharacterAnimation(canvas: Canvas)

    abstract fun updateMeterStatus(index: Int)

    abstract fun destroy()

    companion object {
        const val POSITION_LEFT = 0
        const val POSITION_RIGHT = 1
        const val STATUS_NORMAL = 0
        const val STATUS_HAPPY = 1
        const val STATUS_UNHAPPY = 2
        const val STATUS_TALKING = 3
        private const val DEFAULT_SURFACE_WIDTH = 800
        private const val DEFAULT_SURFACE_HEIGHT = 480
        private const val DEFAULT_POSITION_LEFT_OFFSET_X = 10
        private const val DEFAULT_POSITION_LEFT_OFFSET_Y = 60
        private const val DEFAULT_POSITION_RIGHT_OFFSET_X = 625
        private const val DEFAULT_POSITION_RIGHT_OFFSET_Y = 60
        protected var random = Random()
    }
}
