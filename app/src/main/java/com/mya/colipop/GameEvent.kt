package com.mya.colipop

import android.view.KeyEvent

/**
 * Base class for any external event passed to the ColiPopThread. This can
 * include user input, system events, network input, etc.
 */
open class GameEvent {

    var time: Long = 0
    var type: Byte = 0

    init {
        this.time = System.currentTimeMillis()
    }

    companion object {
        val TIMER_EVENT: Byte = 0
        val KEY_EVENT: Byte = 1
        val TOUCH_EVENT: Byte = 2
    }
}

/**
 * A GameEvent subclass for time based
 */
internal class TimerGameEvent : GameEvent() {
    init {
        this.type = GameEvent.TIMER_EVENT
    }
}

/**
 * A GameEvent subclass for key based user input. Values are those used by
 * the standard onKey
 */
internal class KeyGameEvent (var keyCode: Int, var up: Boolean, var msg: KeyEvent) : GameEvent() {
    init {
        this.type = GameEvent.KEY_EVENT
    }
}

/**
 * A GameEvent subclass for touch based user input.
 */
internal class TouchGameEvent : GameEvent {
    var motionEvent: Int = 0
    var x: Int = 0
    var y: Int = 0

    /**
     * Simple constructor to make populating this event easier.
     */
    constructor(motionEvent: Int, x: Int, y: Int) {
        this.type = GameEvent.TOUCH_EVENT
        this.motionEvent = motionEvent
        this.x = x
        this.y = y
    }
}