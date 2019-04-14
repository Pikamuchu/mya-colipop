package com.mya.games.colipop;

public class IATouchEvent {

    public int action = 0;
    public int x = 0;
    public int y = 0;

    public IATouchEvent(int action, int x, int y) {
        this.action = action;
        this.x = x;
        this.y = y;
    }

}
