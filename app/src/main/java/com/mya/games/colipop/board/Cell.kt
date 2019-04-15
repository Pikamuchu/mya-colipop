package com.mya.games.colipop.board

class Cell(posX: Int, posY: Int) {
    // Board cell position
    var posX = 0
    var posY = 0

    // Flag to ignore cell logic
    var ignore = false

    var bubble: Bubble? = null
    var thing: Thing? = null
    var explosion: Explosion? = null

    init {
        this.posX = posX
        this.posY = posY
    }
}
