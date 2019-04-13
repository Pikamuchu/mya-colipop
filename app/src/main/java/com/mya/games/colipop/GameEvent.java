package com.mya.games.colipop;

import android.view.KeyEvent;

/**
 * Base class for any external event passed to the ColiPopThread. This can
 * include user input, system events, network input, etc.
 */
public class GameEvent {

    public static final byte TIMER_EVENT = 0;
    public static final byte KEY_EVENT = 1;
    public static final byte TOUCH_EVENT = 2;

    public long time;
    public byte type;

    public GameEvent() {
        time = System.currentTimeMillis();
    }

}

/**
 * A GameEvent subclass for time based
 */
class TimerGameEvent extends GameEvent {
    /**
     * Simple constructor to make populating this event easier.
     */
    public TimerGameEvent() {
        this.type = TIMER_EVENT;
    }
}

/**
 * A GameEvent subclass for key based user input. Values are those used by
 * the standard onKey
 */
class KeyGameEvent extends GameEvent {
    public int keyCode;
    public KeyEvent msg;
    public boolean up;
    /**
     * Simple constructor to make populating this event easier.
     */
    public KeyGameEvent(int keyCode, boolean up, KeyEvent msg) {
        this.type = KEY_EVENT;
        this.keyCode = keyCode;
        this.msg = msg;
        this.up = up;
    }
}

/**
 * A GameEvent subclass for touch based user input.
 */
class TouchGameEvent extends GameEvent {

    public static final byte PLAYER_CPU = 0;
    public static final byte PLAYER_1 = 1;
    public static final byte PLAYER_2 = 2;
    public int player;
    public int motionEvent;
    public int x;
    public int y;
    /**
     * Simple constructor to make populating this event easier.
     */
    public TouchGameEvent(int motionEvent, int x, int y) {
        this.type = TOUCH_EVENT;
        this.motionEvent = motionEvent;
        this.x = x;
        this.y = y;
        this.player = PLAYER_1;
    }
    public TouchGameEvent(int player, int motionEvent, int x, int y) {
        this.type = TOUCH_EVENT;
        this.motionEvent = motionEvent;
        this.x = x;
        this.y = y;
        this.player = player;
    }
}