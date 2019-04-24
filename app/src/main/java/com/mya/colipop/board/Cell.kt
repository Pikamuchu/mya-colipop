package com.mya.colipop.board

/**
 * Cell data object.
 */
class Cell(posX: Int, posY: Int) {
    // Board cell position
    var posX = 0
    var posY = 0
    // Flag to ignore cell logic
    var ignore = false
    // Cell objects
    var bubble: Bubble? = null
    var thing: Thing? = null
    var explosion: Explosion? = null

    init {
        this.posX = posX
        this.posY = posY
    }
}
