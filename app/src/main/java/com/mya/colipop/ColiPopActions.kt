package com.mya.colipop

import com.mya.colipop.board.Board
import com.mya.colipop.board.BubbleResources
import com.mya.colipop.board.Cell
import com.mya.colipop.board.EfectoResources

object ColiPopActions {

    private val TAG = "ColiPop"

    fun tiltOtherCells(board: Board?, cell: Cell?) {
        if (board != null && cell != null) {
            var cellOther = board.getCellInPos(cell.posX + 1, cell.posY)
            tiltCell(cellOther, cell.posX, cell.posY, board)
            cellOther = board.getCellInPos(cell.posX - 1, cell.posY)
            tiltCell(cellOther, cell.posX, cell.posY, board)
            cellOther = board.getCellInPos(cell.posX, cell.posY + 1)
            tiltCell(cellOther, cell.posX, cell.posY, board)
            cellOther = board.getCellInPos(cell.posX, cell.posY - 1)
            tiltCell(cellOther, cell.posX, cell.posY, board)
        }
    }

    fun updateTouchBoard(cellOrig: Cell?, cellDest: Cell?, board: Board, ignoreRemoveEffect: Boolean, ignoreMove: Boolean) {
        var cellOrigin = cellOrig
        var cellDestiny = cellDest

        if (cellOrigin != null && cellDestiny != null) {
            // Caso la bubble esta en movimiento y ya no esta donde se ha producido el evento
            if (cellOrigin.bubble == null) {
                //Log.d(TAG, "Cell sin bubble: Buscamos posible bubble en Movimiento.");
                var isOtherCell = false
                // Miramos cell de arriba
                var otherCell = board.getCell(cellOrigin.posX, cellOrigin.posY - 1)
                if (otherCell != null && otherCell.bubble != null && otherCell.bubble?.move == BubbleResources.BUBBLE_MOVE_UP) {
                    //Log.d(TAG, "Cell con bubble en movimiento encontrada arriba.");
                    isOtherCell = true
                    cellOrigin = otherCell
                    cellDestiny = board.getCell(cellDestiny.posX, cellDestiny.posY - 1)
                }
                // Miramos cell de la derecha
                otherCell = board.getCell(cellOrigin.posX + 1, cellOrigin.posY)
                if (!isOtherCell && otherCell != null && otherCell.bubble != null && otherCell.bubble?.move == BubbleResources.BUBBLE_MOVE_RIGHT) {
                    //Log.d(TAG, "Cell con bubble en movimiento encontrada a la derecha.");
                    isOtherCell = true
                    cellOrigin = otherCell
                    cellDestiny = board.getCell(cellDestiny!!.posX + 1, cellDestiny.posY)
                }
                // Miramos cell de la izquierda
                otherCell = board.getCell(cellOrigin.posX - 1, cellOrigin.posY)
                if (!isOtherCell && otherCell != null && otherCell.bubble != null && otherCell.bubble?.move == BubbleResources.BUBBLE_MOVE_LEFT) {
                    //Log.d(TAG, "Cell con bubble en movimiento encontrada a la izquierda.");
                    isOtherCell = true
                    cellOrigin = otherCell
                    cellDestiny = board.getCell(cellDestiny!!.posX - 1, cellDestiny.posY)
                }
                if (!isOtherCell || cellDestiny == null) {
                    //Log.d(TAG, "No se ha encontrado movimiento valido. No hacemos nada.");
                    return
                }
            } else if (cellOrigin.thing != null && cellOrigin.bubble?.move != BubbleResources.BUBBLE_MOVE_NONE) {
                return
            }

            // Caso remove bubble
            if (cellOrigin.posX == cellDestiny.posX && cellOrigin.posY == cellDestiny.posY) {
                // Solo se pueden remover bubbles sin things
                if (cellDestiny.thing == null) {
                    //Log.d(TAG, "Removendo bubble en Cell: posX=" + cellDestiny.posX + ", posY=" + cellDestiny.posY );

                    if (!ignoreRemoveEffect) {
                        board.addEfectoBubble(cellDestiny.bubble, EfectoResources.EFECTO_PLAYER_TOUCH_OBJECT_TYPE)
                    }

                    board.removeBubble(cellDestiny)

                } else {
                    //Log.d(TAG, "No se puede remover bubble en Cell: posX=" + cellDestiny.posX + ", posY=" + cellDestiny.posY );

                    // Si la bubble no se puede remover mostramos efecto bloqueo
                    if (!ignoreRemoveEffect) {
                        board.addEfectoBubble(cellDestiny.bubble, EfectoResources.EFECTO_BLOQUEO_OBJECT_TYPE)
                    }
                }

                // TODO: Trigger explosion sound

            // Caso movimiento de bubbles between cells
            } else if (!ignoreMove && cellOrigin.thing != null) {
                val deltaX = cellOrigin.posX - cellDestiny.posX
                val deltaY = cellOrigin.posY - cellDestiny.posY

                // Solo se permite movimientos de 1 cell como maximo
                var cell_i = cellOrigin.posX
                val cell_j = cellOrigin.posY
                if (deltaX > 0) {
                    cell_i -= 1
                }
                if (deltaX < 0) {
                    cell_i += 1
                }
                cellDestiny = board.getCell(cell_i, cell_j)

                // Control de cell valida
                if (cellDestiny == null || cellDestiny.thing != null) {
                    //Log.d(TAG, "Movimiento ignorado: cell destiny nula o con bubble");
                    board.touchBubble(cellOrigin, deltaX, deltaY)

                } else {
                    //Log.d(TAG, "Moviendo bubble de Cell: posX=" + cellOrigin.posX + ", posY=" + cellOrigin.posY + " a Cell: posX=" + cellDestiny.posX + ", posY=" + cellDestiny.posY );

                    if (cellOrigin.posX < cellDestiny.posX) {
                        board.addEfectoBubble(cellOrigin.bubble, EfectoResources.EFECTO_PLAYER_MOVE_RIGHT_OBJECT_TYPE)
                    } else {
                        board.addEfectoBubble(cellOrigin.bubble, EfectoResources.EFECTO_PLAYER_MOVE_LEFT_OBJECT_TYPE)
                    }

                    board.moveBubbleBetweenCells(cellOrigin, cellDestiny)
                }
            }
        } else {
            //TODO: Trigger touch sound ?
        }
    }

    private fun tiltCell(cell: Cell?, fromX: Int, fromY: Int, board: Board) {
        if (cell != null) {
            val deltaX = fromX - cell.posX
            val deltaY = fromY - cell.posY
            board.touchBubble(cell, deltaX, deltaY)
            cell.bubble?.move = BubbleResources.BUBBLE_MOVE_TILT
        }
    }
}
