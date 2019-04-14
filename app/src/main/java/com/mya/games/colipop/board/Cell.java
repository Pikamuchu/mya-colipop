package com.mya.games.colipop.board;

public class Cell {
    // Board cell position
    public int posX = 0;
    public int posY = 0;

    // Flag to ignore cell logic
    public boolean ignore = false;

    public Bubble bubble = null;
    public Thing thing = null;
    public Explosion explosion = null;

    public Cell(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }
}
