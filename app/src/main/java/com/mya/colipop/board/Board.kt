package com.mya.colipop.board

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import com.mya.colipop.ColiPopResources
import com.mya.colipop.character.Character
import java.util.*

/**
 * Board object.
 */
class Board(resources: Resources, private var character1: Character) {

    private val TAG = "ColiPop"

    private var resources: Resources? = null

    private var bubbleCommonAnimationIndex = 0

    private lateinit var cells: Array<Array<Cell?>>

    private var initFullBoard = true
    private var boardInitialized = false
    private var boardStable = false
    private var boardStableTime: Long = 0
    private var lastInitialBubbleTime: Long = 0
    private var boardFullBubbles = false
    private var boardFullThings = false

    private var thingsAnimated: Array<Thing?> = arrayOfNulls(Board.MAX_THINGS_ANIMATED)

    private var thingsExploded = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    private var timeBetweenBubbles = DEFAULT_TIME_BETWEEN_BUBBLES

    private var lastLevel = 0

    private val initialCellXPos: Int
        get() {
            val cells = this.cells
            val init = random.nextInt(Board.MAX_WIDTH - 1)

            for (i in init until Board.MAX_WIDTH) {
                val leftCell = cells[i][Board.MAX_HEIGHT - 1]
                if (leftCell == null || leftCell.bubble == null) {
                    return i
                }
            }
            for (i in 0 until init - 1) {
                val leftCell = cells[i][Board.MAX_HEIGHT - 1]
                if (leftCell == null || leftCell.bubble == null) {
                    return i
                }
            }
            return -1
        }

    val isBoardInitialized: Boolean
        get() = this.boardInitialized

    val isBoardFullThings: Boolean
        get() = this.boardFullThings

    init {
        this.resources = resources

        BubbleResources.initializeGraphics(resources)

        //Log.d(TAG, "Graficos Bubbles inicializados");

        ExplosionResources.initializeGraphics(resources)

        //Log.d(TAG, "Graficos Explosiones inicializados");

        ThingResources.initializeGraphics(resources)

        //Log.d(TAG, "Graficos Things inicializados");

        EfectoResources.initializeGraphics(resources)

        //Log.d(TAG, "Graficos Efectos inicializados");

        //initBoard();
    }

    /**
     * Perform board initialization.
     */
    fun initBoard() {
        this.cells = Array(Board.MAX_WIDTH) { arrayOfNulls<Cell?>(Board.MAX_HEIGHT) }

        this.character1.initCharacter()

        // Localizing external variables
        val cells = this.cells
        val initFullBoard = this.initFullBoard

        for (i in 0 until Board.MAX_WIDTH) {
            for (j in 0 until Board.MAX_HEIGHT) {
                val cell = Cell(i, j)

                // Condicion de inicializar board full de bubbles
                if (initFullBoard) {
                    val bubble = Bubble()

                    bubble.drawX = Board.OFFSET_X + i * BubbleResources.BUBBLE_WIDTH
                    bubble.drawY = Board.OFFSET_Y + j * BubbleResources.BUBBLE_HEIGHT

                    cell.bubble = bubble

                    addRandomObject(cell)

                    // Comprobamos que el thing no pete nada
                    if (i >= 2 && cell.thing != null
                            && cells[i - 1][j]!!.thing != null && cells[i - 1][j]!!.thing!!.type == cell.thing!!.type
                            && cells[i - 2][j]!!.thing != null && cells[i - 2][j]!!.thing!!.type == cell.thing!!.type) {

                        cell.thing = null
                    }
                    if (j >= 2 && cell.thing != null
                            && cells[i][j - 1]!!.thing != null && cells[i][j - 1]!!.thing!!.type == cell.thing!!.type
                            && cells[i][j - 2]!!.thing != null && cells[i][j - 2]!!.thing!!.type == cell.thing!!.type) {

                        cell.thing = null
                    }
                }

                cells[i][j] = cell
            }
        }

        boardStable = true
        boardStableTime = System.currentTimeMillis()

        // Inicializamos marcadores
        thingsExploded = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

        thingsAnimated = arrayOfNulls(Board.MAX_THINGS_ANIMATED)

        boardInitialized = true

        // Resto variables
        timeBetweenBubbles = DEFAULT_TIME_BETWEEN_BUBBLES
        lastLevel = 0

        //Log.d(TAG, "Board inicializado");
    }

    /**
     * Get the cell at indicated board position.
     */
    fun getCell(i: Int, j: Int): Cell? {
        return if (i < 0 || i >= Board.MAX_WIDTH || j < 0 || j >= Board.MAX_HEIGHT) {
            null
        } else cells[i][j]
    }

    /**
     * Perform board animation.
     */
    fun doBoardAnimation(canvas: Canvas?) {
        if (canvas == null || !boardInitialized) {
            return
        }

        this.bubbleCommonAnimationIndex++
        // Doing loop
        if (this.bubbleCommonAnimationIndex >= BubbleResources.BUBBLE_ANIMATION_SEQUENCE.size) {
            this.bubbleCommonAnimationIndex = 0
        }

        val cells = this.cells

        val bubblesToExplodeList: MutableList<Cell> = mutableListOf()

        var boardFullBubbles = true
        var boardFullThings = true
        var isSomethingMoving = false
        for (i in 0 until Board.MAX_WIDTH) {
            for (j in 0 until Board.MAX_HEIGHT) {
                val cell = cells[i][j] ?: continue

                // Marca para no processar la cell ( solo 1 vez )
                if (cell.ignore) {
                    cell.ignore = false
                    continue
                }

                val thing = cell.thing
                if (thing == null) {
                    boardFullThings = false
                }

                val bubble = cell.bubble
                if (bubble == null) {
                    boardFullBubbles = false
                } else {
                    updateBubbleAnimation(bubble, thing)

                    /**
                     * Update bubble position
                     */

                    // Si la bubble esta centrada verticalmente
                    // Nota: no se como esto esta as, pero si lo touchs se descuaringa todo
                    val bubbleCellPosX = Board.OFFSET_X + i * BubbleResources.BUBBLE_HEIGHT
                    if (bubble.drawX == bubbleCellPosX && canBubbleMoveUp(cells, bubble, i, j)) {

                        moveAndUpdateBubblePosition(cells, bubble, thing, i, j, BubbleResources.BUBBLE_MOVE_UP, false)
                        // indicamos que hay movimiento de bubbles
                        isSomethingMoving = true
                        // Marcamos bubble como que se move
                        bubble.move = BubbleResources.BUBBLE_MOVE_UP

                    } else {
                        // Centramos la bubble en su cell si no lo esta
                        val bubbleCellPosY = Board.OFFSET_Y + j * BubbleResources.BUBBLE_HEIGHT
                        if (bubble.drawY != bubbleCellPosY || bubble.drawX != bubbleCellPosX) {
                            centraBubbleINPOSITION(bubble, bubbleCellPosX, bubbleCellPosY)
                            // indicamos que hay movimiento de bubbles
                            isSomethingMoving = true
                        } else {
                            // Marcamos bubble como que no se move
                            bubble.move = BubbleResources.BUBBLE_MOVE_NONE
                        }
                    }

                    if (thing != null && (thing.status == ThingResources.THING_STATUS_BUBBLE || thing.status == ThingResources.THING_STATUS_MOVIDO)) {
                        var bitmap: Bitmap? = null

                        val type = thing.type
                        if (type == ThingResources.CARAMELO_OBJECT_TYPE) {
                            bitmap = ThingResources.CARAMELO_GRAPHICS_BITMAP[thing.graphicIndex]
                        } else if (type == ThingResources.PIRULETA_OBJECT_TYPE) {
                            bitmap = ThingResources.PIRULETA_GRAPHICS_BITMAP[thing.graphicIndex]
                        } else if (type == ThingResources.RASPA_OBJECT_TYPE) {
                            bitmap = ThingResources.RASPA_GRAPHICS_BITMAP[thing.graphicIndex]
                        } else if (type == ThingResources.PEINE_OBJECT_TYPE) {
                            bitmap = ThingResources.PEINE_GRAPHICS_BITMAP[thing.graphicIndex]
                        }

                        if (thing.status == ThingResources.THING_STATUS_MOVIDO) {
                            // Movemos bubble hasta su destiny
                            if (moveThingBetweenBubbles(thing)) {
                                // Si el thing est ubicado reseteamos statuss union de las bubbles que intervienen
                                thing.status = ThingResources.THING_STATUS_BUBBLE
                            }
                            // indicamos que hay movimiento de bubbles
                            isSomethingMoving = true

                        } else {
                            // Sincronizamos positiones de thing y bubble
                            if (thing.drawX != bubble.drawX) {
                                thing.drawX = bubble.drawX
                            }
                            if (thing.drawY != bubble.drawY) {
                                thing.drawY = bubble.drawY
                            }
                        }

                        // Animate thing
                        canvas.drawBitmap(bitmap!!, thing.drawX.toFloat(), thing.drawY.toFloat(), null)
                    }

                    var bitmap: Bitmap?
                    // Caso bubble en movimiento
                    if (bubble.move != BubbleResources.BUBBLE_MOVE_NONE) {
                        bitmap = BubbleResources.BUBBLE_MOVE_GRAPHICS_BITMAP[bubble.graphicIndex]

                        // Caso bubble quieta
                    } else {
                        bitmap = BubbleResources.BUBBLE_GRAPHICS_BITMAP[bubble.graphicIndex]
                    }

                    // Animate bubble
                    canvas.drawBitmap(bitmap, bubble.drawX.toFloat(), bubble.drawY.toFloat(), null)
                }

                val explosion = cell.explosion
                if (explosion != null) {
                    // cambiamos el indice de animacin
                    explosion.graphicIndex++

                    // Caso bubble ha acabado de exploder
                    if (explosion.graphicIndex >= ExplosionResources.EXPLOSION_GRAPHICS_BITMAP.size) {
                        cell.explosion = null

                        if (thing != null && thing.status == ThingResources.THING_STATUS_EXPLODE) {
                            thingsExploded[thing.type]++
                            cell.thing = null

                        }

                        // Animating exploding bubble
                    } else {

                        // Animate explosion
                        canvas.drawBitmap(ExplosionResources.EXPLOSION_GRAPHICS_BITMAP[explosion.graphicIndex], explosion.drawX.toFloat(), explosion.drawY.toFloat(), null)

                        // Animate thing grande
                        if (thing != null && thing.status == ThingResources.THING_STATUS_EXPLODE) {

                            var bitmap: Bitmap? = null

                            val type = thing.type
                            if (type == ThingResources.CARAMELO_OBJECT_TYPE) {
                                bitmap = ThingResources.CARAMELO_GRANDE_GRAPHICS_BITMAP[0]
                            } else if (type == ThingResources.PIRULETA_OBJECT_TYPE) {
                                bitmap = ThingResources.PIRULETA_GRANDE_GRAPHICS_BITMAP[0]
                            } else if (type == ThingResources.RASPA_OBJECT_TYPE) {
                                bitmap = ThingResources.RASPA_GRANDE_GRAPHICS_BITMAP[0]
                            } else if (type == ThingResources.PEINE_OBJECT_TYPE) {
                                bitmap = ThingResources.PEINE_GRANDE_GRAPHICS_BITMAP[0]
                            }

                            canvas.drawBitmap(bitmap!!, explosion.drawX.toFloat(), explosion.drawY.toFloat(), null)
                        }

                        // indicamos que hay movimiento en el board
                        isSomethingMoving = true
                    }
                }

                /**
                 * Logica matcheado horizontal de things
                 */

                // Miramos si la bubble tiene thing
                if (i < Board.MAX_WIDTH - 2 && bubble != null && bubble.move == BubbleResources.BUBBLE_MOVE_NONE && thing != null && thing.status == ThingResources.THING_STATUS_BUBBLE) {
                    // Miramos si la bubble de la derecha tiene el mismo thing
                    val thingType = thing.type
                    val cellRight1 = cells[i + 1][j]
                    if (cellRight1 != null && cellRight1.bubble != null && cellRight1.bubble!!.move == BubbleResources.BUBBLE_MOVE_NONE
                            && cellRight1.thing != null && cellRight1.thing!!.type == thingType && cellRight1.thing!!.status == ThingResources.THING_STATUS_BUBBLE) {
                        // Miramos si la bubble de mas a la derecha tiene el mismo thing
                        val cellRight2 = cells[i + 2][j]
                        if (cellRight2 != null && cellRight2.bubble != null && cellRight2.bubble!!.move == BubbleResources.BUBBLE_MOVE_NONE
                                && cellRight2.thing != null && cellRight2.thing!!.type == thingType && cellRight2.thing!!.status == ThingResources.THING_STATUS_BUBBLE) {
                            // Explodemos bubbles
                            bubblesToExplodeList += cell
                            bubblesToExplodeList += cellRight1
                            bubblesToExplodeList += cellRight2
                            // variable para contabilizar things explodes
                            var numThings = 3
                            // ignoramos las cells de la derecha para evitar efecto raro
                            cellRight1.ignore = true
                            cellRight2.ignore = true
                            // Si hay mas bubbles con el mismo thing las explodemos tambien
                            for (k in i + 3 until Board.MAX_WIDTH) {
                                val cellRight = cells[k][j]
                                if (cellRight != null && cellRight.bubble != null && cellRight.bubble!!.move == BubbleResources.BUBBLE_MOVE_NONE
                                        && cellRight.thing != null && cellRight.thing!!.type == thingType && cellRight.thing!!.status == ThingResources.THING_STATUS_BUBBLE) {
                                    bubblesToExplodeList += cellRight
                                    numThings++
                                    // ignoramos las cells de la derecha para evitar efecto raro
                                    cellRight.ignore = true
                                } else {
                                    // En el momento en que no encontremos una bubble a exploder salimos del for
                                    break
                                }
                            }
                            // Miramos tambien a la izquierda por si acaso
                            for (k in i - 1 downTo 0) {
                                val cellLeft = cells[k][j]
                                if (cellLeft != null && cellLeft.bubble != null && cellLeft.bubble!!.move == BubbleResources.BUBBLE_MOVE_NONE
                                        && cellLeft.thing != null && cellLeft.thing!!.type == thingType && cellLeft.thing!!.status == ThingResources.THING_STATUS_BUBBLE) {
                                    bubblesToExplodeList += cellLeft
                                    numThings++
                                } else {
                                    // En el momento en que no encontremos una bubble a exploder salimos del for
                                    break
                                }
                            }
                            // Marcamos como que hay movimiento
                            isSomethingMoving = true
                            // Updatemos meters de jugadores
                            this.character1.updateMeter(thingType, numThings)
                        }
                    }
                }

                /**
                 * Logica matcheado vertical de things
                 */

                // Miramos si la bubble tiene thing
                if (j < Board.MAX_HEIGHT - 2 && bubble != null && bubble.move == BubbleResources.BUBBLE_MOVE_NONE && thing != null && thing.status == ThingResources.THING_STATUS_BUBBLE) {
                    // Miramos si la bubble de la arriba tiene el mismo thing
                    val thingType = thing.type
                    val cellDown1 = cells[i][j + 1]
                    if (cellDown1 != null && cellDown1.bubble != null && cellDown1.bubble!!.move == BubbleResources.BUBBLE_MOVE_NONE
                            && cellDown1.thing != null && cellDown1.thing!!.type == thingType && cellDown1.thing!!.status == ThingResources.THING_STATUS_BUBBLE) {
                        // Miramos si la bubble de mas a la arriba tiene el mismo thing
                        val cellDown2 = cells[i][j + 2]
                        if (cellDown2 != null && cellDown2.bubble != null && cellDown2.bubble!!.move == BubbleResources.BUBBLE_MOVE_NONE
                                && cellDown2.thing != null && cellDown2.thing!!.type == thingType && cellDown2.thing!!.status == ThingResources.THING_STATUS_BUBBLE) {
                            // Explodemos bubbles
                            bubblesToExplodeList += cell
                            bubblesToExplodeList += cellDown1
                            bubblesToExplodeList += cellDown2
                            // variable para contabilizar things explodes
                            var numThings = 3
                            // ignoramos las cells de abajo para evitar efectos raros
                            cellDown1.ignore = true
                            cellDown2.ignore = true
                            // Si hay mas bubbles con el mismo thing las explodemos tambien
                            for (k in j + 3 until Board.MAX_HEIGHT) {
                                val cellDown = cells[i][k]
                                if (cellDown != null && cellDown.bubble != null && cellDown.bubble!!.move == BubbleResources.BUBBLE_MOVE_NONE
                                        && cellDown.thing != null && cellDown.thing!!.type == thingType && cellDown.thing!!.status == ThingResources.THING_STATUS_BUBBLE) {
                                    bubblesToExplodeList += cellDown
                                    numThings++
                                    // ignoramos las cells de abajo para evitar efectos raros
                                    cellDown.ignore = true
                                } else {
                                    // En el momento en que no encontremos una bubble a exploder salimos del for
                                    break
                                }
                            }
                            // Miramos tambien abajo por si acaso
                            for (k in j - 1 downTo 0) {
                                val cellDown = cells[i][k]
                                if (cellDown != null && cellDown.bubble != null && cellDown.bubble!!.move == BubbleResources.BUBBLE_MOVE_NONE
                                        && cellDown.thing != null && cellDown.thing!!.type == thingType && cellDown.thing!!.status == ThingResources.THING_STATUS_BUBBLE) {
                                    bubblesToExplodeList += cellDown
                                    numThings++
                                } else {
                                    // En el momento en que no encontremos una bubble a exploder salimos del for
                                    break
                                }
                            }
                            // Marcamos como que hay movimiento
                            isSomethingMoving = true
                            // Updatemos meters de jugadores
                            this.character1.updateMeter(thingType, numThings)
                        }
                    }
                }
            }
        }

        for (bubbleToExplode in bubblesToExplodeList) {
            explodeBubble(bubbleToExplode)
        }

        // Control de movimientos en el board
        if (isSomethingMoving) {
            this.boardStable = false
        } else {
            // Solo updatemos a true el flag una vez para no falsear boardStableTime
            if (!this.boardStable) {
                this.boardStable = true
                this.boardStableTime = System.currentTimeMillis()
            }
        }

        // Board full de bubbles
        this.boardFullThings = boardFullThings
        this.boardFullBubbles = boardFullBubbles

        /**
         * Animacin de otros things
         */

        animateThingsIndependents(canvas)

        /**
         * Dibujamos counters
         */

        //LIKE
        val offset_x_like = OFFSET_X_LIKE
        var offset_y_like = OFFSET_Y_LIKE
        val offset_x_like_counter = OFFSET_X_LIKE_COUNTER
        var offset_y_like_counter = OFFSET_Y_LIKE_COUNTER

        canvas.drawBitmap(ThingResources.CARAMELO_GRAPHICS_BITMAP[0], offset_x_like.toFloat(), offset_y_like.toFloat(), null)
        canvas.drawText(" = " + thingsExploded[ThingResources.CARAMELO_OBJECT_TYPE], offset_x_like_counter.toFloat(), offset_y_like_counter.toFloat(), ColiPopResources.paint)

        offset_y_like += COUNTER_Y_SPACER
        offset_y_like_counter += COUNTER_Y_SPACER

        canvas.drawBitmap(ThingResources.PIRULETA_GRAPHICS_BITMAP[0], offset_x_like.toFloat(), offset_y_like.toFloat(), null)
        canvas.drawText(" = " + thingsExploded[ThingResources.PIRULETA_OBJECT_TYPE], offset_x_like_counter.toFloat(), offset_y_like_counter.toFloat(), ColiPopResources.paint)

        // DISLIKE
        val offset_x_dislike = OFFSET_X_DISLIKE
        var offset_y_dislike = OFFSET_Y_DISLIKE
        val offset_x_dislike_counter = OFFSET_X_DISLIKE_COUNTER
        var offset_y_dislike_counter = OFFSET_Y_DISLIKE_COUNTER

        canvas.drawBitmap(ThingResources.RASPA_GRAPHICS_BITMAP[0], offset_x_dislike.toFloat(), offset_y_dislike.toFloat(), null)
        canvas.drawText(" = " + thingsExploded[ThingResources.RASPA_OBJECT_TYPE], offset_x_dislike_counter.toFloat(), offset_y_dislike_counter.toFloat(), ColiPopResources.paint)

        offset_y_dislike += COUNTER_Y_SPACER
        offset_y_dislike_counter += COUNTER_Y_SPACER

        canvas.drawBitmap(ThingResources.PEINE_GRAPHICS_BITMAP[0], offset_x_dislike.toFloat(), offset_y_dislike.toFloat(), null)
        canvas.drawText(" = " + thingsExploded[ThingResources.PEINE_OBJECT_TYPE], offset_x_dislike_counter.toFloat(), offset_y_dislike_counter.toFloat(), ColiPopResources.paint)
    }

    private fun updateBubbleAnimation(bubble: Bubble, thing: Thing?) {
        if (bubble.move == BubbleResources.BUBBLE_MOVE_NONE) {
            // Todas las bubbles que estn quietas utilizan el mismo indice
            bubble.graphicIndex = BubbleResources.BUBBLE_ANIMATION_SEQUENCE[this.bubbleCommonAnimationIndex]
            if (bubble.graphicIndex >= BubbleResources.BUBBLE_GRAPHICS_BITMAP.size) {
                bubble.graphicIndex = 0
            }
        } else {
            // Las bubbles en movimento van de forma independent
            bubble.animationIndex++
            // Hacemos loop
            if (bubble.animationIndex >= BubbleResources.BUBBLE_MOVE_ANIMATION_SEQUENCE.size) {
                bubble.animationIndex = 0
            }
            bubble.graphicIndex = BubbleResources.BUBBLE_MOVE_ANIMATION_SEQUENCE[this.bubbleCommonAnimationIndex]
            if (bubble.graphicIndex >= BubbleResources.BUBBLE_MOVE_GRAPHICS_BITMAP.size) {
                bubble.graphicIndex = 0
            }
        }

        if (thing != null) {
            // utilizamos un indice de animacin comun
            thing.animationIndex++
            // Hacemos loop
            if (thing.animationIndex >= ThingResources.THING_ANIMATION_SEQUENCE.size) {
                thing.animationIndex = 0
            }
            thing.graphicIndex = ThingResources.THING_ANIMATION_SEQUENCE[thing.animationIndex]
            if (thing.graphicIndex >= ThingResources.THING_GRAPHICS_SIZE) {
                thing.graphicIndex = 0
            }
        }
    }

    private fun canBubbleMoveUp(cells: Array<Array<Cell?>>, bubble: Bubble?, i: Int, j: Int): Boolean {
        if (bubble == null) {
            // TODO: revisar esto
            return false
        }
        if (j == 0) {
            // Esta arriba del todo, no se puede mover mas
            return false
        }
        val upCell = cells[i][j - 1]
        return (upCell == null || upCell.bubble == null && upCell.explosion == null)
    }

    private fun canBubbleMoveDown(cells: Array<Array<Cell>>, bubble: Bubble?, i: Int, j: Int): Boolean {
        if (bubble == null) {
            // TODO: revisar esto
            return false
        }
        if (j == Board.MAX_HEIGHT - 1) {
            // Esta arriba del todo, no se puede mover mas
            return false
        }
        val downCell = cells[i][j + 1]
        return (downCell.bubble == null && downCell.explosion == null)
    }

    private fun canBubbleMoveRight(cells: Array<Array<Cell>>, bubble: Bubble?, i: Int, j: Int): Boolean {
        if (bubble == null) {
            // TODO: revisar esto
            return false
        }
        // Miramos que hacia la derecha haya sitio para moverse
        if (i == Board.MAX_WIDTH - 1) {
            // Esta a la derecha del todo, no se puede mover mas
            return false
        }
        val rightCell = cells[i + 1][j]
        if (rightCell.bubble == null && rightCell.explosion == null) {
            // Miramos que hacia arriba a la derecha haya un hueco donde meterse
            if (j == 0) {
                // No puede haber hueco ya que esta arriba del todo
                return false
            }
            val rightUpCell = cells[i + 1][j - 1]
            return (rightUpCell.bubble == null && rightUpCell.explosion == null)
        } else {
            // No hay sitio a la derecha
            return false
        }
    }

    private fun canBubbleMoveLeft(cells: Array<Array<Cell>>, bubble: Bubble?, i: Int, j: Int): Boolean {
        if (bubble == null) {
            // TODO: revisar esto
            return false
        }
        // Miramos que hacia la izquierda haya sitio para moverse
        if (i == 0) {
            // Esta a la izquierda del todo, no se puede mover mas
            return false
        }
        val leftCell = cells[i - 1][j]
        if (leftCell.bubble == null && leftCell.explosion == null) {
            // Miramos que hacia arriba a la izquierda haya un hueco donde meterse
            if (j == 0) {
                // No puede haber hueco ya que esta arriba del todo
                return false
            }
            val leftUpCell = cells[i - 1][j - 1]
            return (leftUpCell.bubble == null && leftUpCell.explosion == null)
        } else {
            return false
        }
    }

    private fun centraBubbleINPOSITION(bubble: Bubble?, x: Int, y: Int) {
        if (bubble == null) {
            return
        }

        var finX = false
        var drawX = bubble.drawX
        if (x == bubble.drawX) {
            finX = true

        } else if (x > bubble.drawX) {
            drawX += BubbleResources.BUBBLE_PIXEL_MOVE
            if (x - drawX < BubbleResources.BUBBLE_PIXEL_MOVE) {
                finX = true
            } else {
                bubble.drawX = drawX
            }

        } else if (x < bubble.drawX) {
            drawX -= BubbleResources.BUBBLE_PIXEL_MOVE
            if (bubble.drawX - x < BubbleResources.BUBBLE_PIXEL_MOVE) {
                finX = true
            } else {
                bubble.drawX = drawX
            }
        }
        var finY = false
        var drawY = bubble.drawY
        if (y == bubble.drawY) {
            finY = true

        } else if (y > bubble.drawY) {
            drawY += BubbleResources.BUBBLE_PIXEL_MOVE
            if (y - bubble.drawY < BubbleResources.BUBBLE_PIXEL_MOVE) {
                finY = true
            } else {
                bubble.drawY = drawY
            }

        } else if (y < bubble.drawY) {
            drawY -= BubbleResources.BUBBLE_PIXEL_MOVE
            if (bubble.drawY - y < BubbleResources.BUBBLE_PIXEL_MOVE) {
                finY = true
            } else {
                bubble.drawY = drawY
            }
        }

        if (finX && finY) {
            // Forzamos centrado por si acaso
            bubble.drawX = x
            bubble.drawY = y
        }
    }

    /**
     * Adds a new bubble to the board.
     */
    fun addInitialBubble() {
        if (!boardInitialized) {
            return
        }

        val level = this.character1.currentLevel
        if (level != lastLevel) {
            timeBetweenBubbles -= java.lang.Double.valueOf(0.2 * timeBetweenBubbles)!!.toLong()
            lastLevel = level
        }

        val difTime = System.currentTimeMillis() - lastInitialBubbleTime
        if (difTime < timeBetweenBubbles) {
            //No ha pasado el tiempo suficiente para aadir otra bubble
            return
        }

        val xPos = initialCellXPos
        if (xPos < 0) {
            // No se ha encontrado position inicial
            return
        }

        val bubble = Bubble()

        bubble.drawX = OFFSET_X + xPos * BubbleResources.BUBBLE_WIDTH
        bubble.drawY = OFFSET_Y + Board.MAX_HEIGHT * BubbleResources.BUBBLE_HEIGHT

        val cell = cells[xPos][Board.MAX_HEIGHT - 1] ?: return

        cell.bubble = bubble
        addRandomObject(cell)

        lastInitialBubbleTime = System.currentTimeMillis()
    }

    private fun addRandomObject(cell: Cell?) {
        if (cell == null || cell.bubble == null) {
            return
        }

        val thing = Thing()

        thing.status = ThingResources.THING_STATUS_BUBBLE

        val level = this.character1.currentLevel

        val porcentaje = random.nextInt(100)
        if (porcentaje < ThingResources.CARAMELO_PORCENTAJE + level) {
            thing.type = ThingResources.CARAMELO_OBJECT_TYPE

        } else if (porcentaje < ThingResources.CARAMELO_PORCENTAJE + ThingResources.PIRULETA_PORCENTAJE + 2 * level) {
            if (level >= 1) {
                thing.type = ThingResources.PIRULETA_OBJECT_TYPE
            } else {
                thing.type = ThingResources.CARAMELO_OBJECT_TYPE
            }

        } else if (porcentaje < ThingResources.CARAMELO_PORCENTAJE + ThingResources.PIRULETA_PORCENTAJE + ThingResources.RASPA_PORCENTAJE + 3 * level) {
            if (level >= 2) {
                thing.type = ThingResources.RASPA_OBJECT_TYPE
            } else {
                if (level >= 1) {
                    thing.type = ThingResources.PIRULETA_OBJECT_TYPE
                } else {
                    thing.type = ThingResources.CARAMELO_OBJECT_TYPE
                }
            }

        } else if (porcentaje < ThingResources.CARAMELO_PORCENTAJE + ThingResources.PIRULETA_PORCENTAJE + ThingResources.RASPA_PORCENTAJE + ThingResources.PEINE_PORCENTAJE + 4 * level) {
            if (level >= 4) {
                thing.type = ThingResources.PEINE_OBJECT_TYPE
            } else {
                if (level >= 2) {
                    thing.type = ThingResources.PIRULETA_OBJECT_TYPE
                } else {
                    thing.type = ThingResources.CARAMELO_OBJECT_TYPE
                }
            }

        } else {
            // Sin thing
            return
        }

        val bubble = cell.bubble

        thing.drawX = bubble!!.drawX
        thing.drawY = bubble.drawY

        // Randomizamos indices animacin
        thing.animationIndex = random.nextInt(ThingResources.THING_ANIMATION_SEQUENCE.size)

        cell.thing = thing
    }

    private fun moveAndUpdateBubblePosition(cells: Array<Array<Cell?>>, bubble: Bubble, thing: Thing?, i: Int, j: Int, direction: Int, ignoreNewCell: Boolean) {
        // Movemos bubble
        if (direction == BubbleResources.BUBBLE_MOVE_UP) {
            bubble.drawY -= BubbleResources.BUBBLE_PIXEL_MOVE
        } else if (direction == BubbleResources.BUBBLE_MOVE_LEFT) {
            bubble.drawX -= BubbleResources.BUBBLE_PIXEL_MOVE
        } else if (direction == BubbleResources.BUBBLE_MOVE_RIGHT) {
            bubble.drawX += BubbleResources.BUBBLE_PIXEL_MOVE
        } else if (direction == BubbleResources.BUBBLE_MOVE_DOWN) {
            bubble.drawY += BubbleResources.BUBBLE_PIXEL_MOVE
        }

        // Recalculatemos posicin en board
        val newCellPos = calculatePosBubbleInBoard(bubble)

        val newCell = cells[newCellPos.i][newCellPos.j]
        if (newCell?.bubble != null) {
            // La cell ya est ocupada
            return
        }

        // Quitamos la bubble de su cell anterior
        val cell = cells[i][j]
        cell?.bubble = null

        // Ponemos la bubble en la nueva position
        newCell?.bubble = bubble

        // Updateamos tambien el thing ( si hay )
        if (thing != null) {
            if (direction == BubbleResources.BUBBLE_MOVE_UP) {
                thing.drawY -= BubbleResources.BUBBLE_PIXEL_MOVE
            } else if (direction == BubbleResources.BUBBLE_MOVE_LEFT) {
                thing.drawX -= BubbleResources.BUBBLE_PIXEL_MOVE
            } else if (direction == BubbleResources.BUBBLE_MOVE_RIGHT) {
                thing.drawX += BubbleResources.BUBBLE_PIXEL_MOVE
            } else if (direction == BubbleResources.BUBBLE_MOVE_DOWN) {
                thing.drawY += BubbleResources.BUBBLE_PIXEL_MOVE
            }
            cell?.thing = null
            newCell?.thing = thing
        }

        if (ignoreNewCell) {
            newCell?.ignore = true
        }
    }

    private fun calculatePosBubbleInBoard(bubble: Bubble): CellPosition {
        var xPos = (bubble.drawX - OFFSET_X) / BubbleResources.BUBBLE_WIDTH
        var yPos = (bubble.drawY - OFFSET_Y) / BubbleResources.BUBBLE_HEIGHT

        if (xPos < 0) {
            xPos = 0
        }
        if (xPos >= Board.MAX_WIDTH) {
            xPos = Board.MAX_WIDTH - 1
        }

        if (yPos < 0) {
            yPos = 0
        }
        if (yPos >= Board.MAX_HEIGHT) {
            yPos = Board.MAX_HEIGHT - 1
        }

        return CellPosition(xPos, yPos)
    }

    /**
     * Get the cell at screen coordinates.
     */
    fun getCellInCoordinates(x: Int, y: Int): Cell? {
        if (!boardInitialized) {
            return null
        }

        val normX = (x - Board.OFFSET_X) / BubbleResources.BUBBLE_WIDTH
        val normY = (y - Board.OFFSET_Y) / BubbleResources.BUBBLE_HEIGHT

        //Log.d(TAG, "getCellInCoordinates: x=" + x + ", y=" + y + ", normX=" + normX + ", normY=" + normY );

        return getCellInPos(normX, normY)
    }

    /**
     * Get cell at board normalized position.
     */
    fun getCellInPos(posX: Int, posY: Int): Cell? {
        return if (posX < Board.MAX_WIDTH && posX >= 0 && posY < Board.MAX_HEIGHT && posY >= 0) {
            cells[posX][posY]
        } else null
    }

    private fun explodeBubble(cell: Cell?) {
        if (cell == null || cell.bubble == null) {
            return
        }

        val explosion = Explosion()

        explosion.graphicIndex = 0

        val bubble = cell.bubble
        explosion.drawX = bubble!!.drawX - (ExplosionResources.EXPLOSION_WIDTH - BubbleResources.BUBBLE_WIDTH) / 2
        explosion.drawY = bubble.drawY - (ExplosionResources.EXPLOSION_HEIGHT - BubbleResources.BUBBLE_HEIGHT) / 2

        cell.explosion = explosion

        val thing = cell.thing
        if (thing != null) {
            thing.animationIndex = 0
            thing.graphicIndex = 0

            thing.drawX = explosion.drawX
            thing.drawY = explosion.drawY

            thing.targetX = 5 + ThingResources.THING_WIDTH / 2
            thing.targetY = 120 + ThingResources.THING_HEIGHT / 2

            thing.status = ThingResources.THING_STATUS_EXPLODE
        }

        cell.bubble = null
    }

    fun removeBubble(cell: Cell?) {
        if (cell == null || cell.bubble == null) {
            return
        }

        explodeBubble(cell)

        val thing = cell.thing
        if (thing != null) {
            thing.targetX = thing.drawX
            thing.targetY = BOARD_HEIGHT + ThingResources.THING_HEIGHT

            thing.status = ThingResources.THING_STATUS_REMOVED
        }

        // Aadimos thing a los animated independentmente y lo quitamos de la cell
        var thingAdded = false
        for (i in 0 until Board.MAX_THINGS_ANIMATED) {
            if (this.thingsAnimated[i] == null) {
                this.thingsAnimated[i] = thing
                thingAdded = true
                break
            }
        }
        // Si no encontramos posicin libre, lo metemos en una posicin aleatoria
        if (!thingAdded) {
            this.thingsAnimated[random.nextInt(Board.MAX_THINGS_ANIMATED - 1)] = thing
        }
        cell.thing = null

        // Tambien controla la velocidad del turno de la CPU
        boardStable = false
    }

    fun touchBubble(cell: Cell?, directionX: Int, directionY: Int) {
        if (cell == null) {
            return
        }

        val bubble = cell.bubble
        if (bubble == null || bubble.move != BubbleResources.BUBBLE_MOVE_NONE) {
            return
        }

        // Limitamos maximo de movimiento
        var movimientoX = 0
        if (directionX > 0) {
            movimientoX = BubbleResources.BUBBLE_WIDTH_MEDIOS
        } else if (directionX < 0) {
            movimientoX = -BubbleResources.BUBBLE_WIDTH_MEDIOS
        }
        var movimientoY = 0
        if (directionY > 0) {
            movimientoY = BubbleResources.BUBBLE_WIDTH_MEDIOS
        } else if (directionY < 0) {
            movimientoY = -BubbleResources.BUBBLE_WIDTH_MEDIOS
        }

        bubble.drawX -= movimientoX
        bubble.drawY -= movimientoY

        val thing = cell.thing ?: return

        thing.drawX -= movimientoX
        thing.drawY -= movimientoY
    }

    fun moveBubbleBetweenCells(origin: Cell?, destiny: Cell?) {
        if (origin == null || destiny == null) {
            return
        }

        val deltaX = destiny.posX - origin.posX
        val deltaY = destiny.posY - origin.posY
        if (deltaX > 1 || deltaX < -1 || deltaY > 1 || deltaY < -1) {
            return
        }

        origin.bubble ?: return

        val thingDestiny = destiny.thing
        if (thingDestiny != null) {
            return
        }

        // Movemos bubble a la nueva cell
        destiny.bubble = origin.bubble
        destiny.thing = origin.thing
        // Quitamos bubble de la cell origin
        origin.bubble = null
        origin.thing = null

        // Updatemos status de movimiento de la bubble
        if (deltaX == 0 && deltaY > 0) {
            //Log.d(TAG,"Moving bubble down: deltaX = " + deltaX + ", deltaY = " + deltaY);
            destiny.bubble!!.move = BubbleResources.BUBBLE_MOVE_DOWN
        } else if (deltaX == 0 && deltaY < 0) {
            //Log.d(TAG,"Moving bubble up: deltaX = " + deltaX + ", deltaY = " + deltaY);
            destiny.bubble!!.move = BubbleResources.BUBBLE_MOVE_UP
        } else if (deltaY == 0 && deltaX > 0) {
            //Log.d(TAG,"Moving bubble right: deltaX = " + deltaX + ", deltaY = " + deltaY);
            destiny.bubble!!.move = BubbleResources.BUBBLE_MOVE_RIGHT
        } else if (deltaY == 0 && deltaX < 0) {
            //Log.d(TAG,"Moving bubble left: deltaX = " + deltaX + ", deltaY = " + deltaY);
            destiny.bubble!!.move = BubbleResources.BUBBLE_MOVE_LEFT
        }

        // Tambien controla la velocidad del turno de la CPU
        boardStable = false
    }

    fun addEfectoBubble(bubble: Bubble?, efectoFijoType: Int) {
        if (bubble == null) {
            return
        }

        val thing = Thing()

        thing.bubble = bubble
        thing.drawX = bubble.drawX
        thing.drawY = bubble.drawY
        thing.type = efectoFijoType
        thing.status = ThingResources.THING_STATUS_EFECTO_FIJO

        // Localizing external variables
        val things_animated = this.thingsAnimated

        // Aadimos thing a los animated independentmente
        var thingAdded = false
        for (i in 0 until Board.MAX_THINGS_ANIMATED) {
            if (things_animated[i] == null) {
                things_animated[i] = thing
                thingAdded = true
                break
            }
        }
        // Si no encontramos posicin libre, lo metemos en una posicin aleatoria
        if (!thingAdded) {
            things_animated[random.nextInt(Board.MAX_THINGS_ANIMATED - 1)] = thing
        }
    }

    fun addEfectoTouch(drawX: Int, drawY: Int, efectoFijoType: Int) {
        val thing = Thing()

        thing.drawX = drawX - ThingResources.THING_WIDTH_MEDIO
        thing.drawY = drawY - ThingResources.THING_HEIGHT_MEDIO
        thing.type = efectoFijoType
        thing.status = ThingResources.THING_STATUS_EFECTO_FIJO

        // Localizing external variables
        val things_animated = this.thingsAnimated

        // Aadimos thing a los animated independentmente
        var thingAdded = false
        for (i in 0 until Board.MAX_THINGS_ANIMATED) {
            if (things_animated[i] == null) {
                things_animated[i] = thing
                thingAdded = true
                break
            }
        }
        // Si no encontramos posicin libre, lo metemos en una posicin aleatoria
        if (!thingAdded) {
            things_animated[random.nextInt(Board.MAX_THINGS_ANIMATED - 1)] = thing
        }
    }

    fun animateThingsIndependents(canvas: Canvas?) {
        // Animating los things independents
        val things_animated = this.thingsAnimated
        if (things_animated.size == 0) {
            return
        }

        //Localizing extenal variables
        val THING_STATUS_REMOVED = ThingResources.THING_STATUS_REMOVED
        val THING_STATUS_EFECTO_FIJO = ThingResources.THING_STATUS_EFECTO_FIJO

        for (i in 0 until Board.MAX_THINGS_ANIMATED) {
            val thing = things_animated[i] ?: continue
// De momento animating solo los things removeds
            if (thing.status == THING_STATUS_REMOVED) {
                animateThingRemoved(canvas, i, thing)

            } else if (thing.status == THING_STATUS_EFECTO_FIJO) {
                animateThingEfectoFijo(canvas, i, thing)
            }
        }
    }

    fun animateThingRemoved(canvas: Canvas?, i: Int, thing: Thing) {
        thing.animationIndex++
        // Hacemos loop
        if (thing.animationIndex >= ThingResources.THING_REMOVED_ANIMATION_SEQUENCE.size) {
            thing.animationIndex = 0
        }
        thing.graphicIndex = ThingResources.THING_REMOVED_ANIMATION_SEQUENCE[thing.animationIndex]
        if (thing.graphicIndex >= ThingResources.THING_GRAPHICS_SIZE) {
            thing.graphicIndex = 0
        }

        var finX = false
        var drawX = thing.drawX
        if (thing.targetX == thing.drawX) {
            finX = true

        } else if (thing.targetX > thing.drawX) {
            drawX += ThingResources.THING_PIXEL_MOVE
            if (thing.targetX - drawX < ThingResources.THING_REMOVED_GRAPHICS_SIZE) {
                finX = true
            } else {
                thing.drawX = drawX
            }

        } else if (thing.targetX < thing.drawX) {
            drawX -= ThingResources.THING_PIXEL_MOVE
            if (thing.drawX - thing.targetX < ThingResources.THING_REMOVED_GRAPHICS_SIZE) {
                finX = true
            } else {
                thing.drawX = drawX
            }
        }
        var finY = false
        var drawY = thing.drawY
        if (thing.targetY == thing.drawY) {
            finY = true

        } else if (thing.targetY > thing.drawY) {
            drawY += ThingResources.THING_PIXEL_MOVE
            if (thing.targetY - thing.drawY < ThingResources.THING_REMOVED_GRAPHICS_SIZE) {
                finY = true
            } else {
                thing.drawY = drawY
            }

        } else if (thing.targetY < thing.drawY) {
            drawY -= ThingResources.THING_PIXEL_MOVE
            if (thing.drawY - thing.targetY < ThingResources.THING_REMOVED_GRAPHICS_SIZE) {
                finY = true
            } else {
                thing.drawY = drawY
            }
        }

        if (finX && finY) {
            thingsAnimated[i] = null

        } else {
            var bitmap: Bitmap? = null

            val type = thing.type
            if (type == ThingResources.CARAMELO_OBJECT_TYPE) {
                bitmap = ThingResources.CARAMELO_GRANDE_GRAPHICS_BITMAP[thing.graphicIndex]
            } else if (type == ThingResources.PIRULETA_OBJECT_TYPE) {
                bitmap = ThingResources.PIRULETA_GRANDE_GRAPHICS_BITMAP[thing.graphicIndex]
            } else if (type == ThingResources.RASPA_OBJECT_TYPE) {
                bitmap = ThingResources.RASPA_GRANDE_GRAPHICS_BITMAP[thing.graphicIndex]
            } else if (type == ThingResources.PEINE_OBJECT_TYPE) {
                bitmap = ThingResources.PEINE_GRANDE_GRAPHICS_BITMAP[thing.graphicIndex]
            }

            canvas!!.drawBitmap(bitmap!!, drawX.toFloat(), drawY.toFloat(), null)
        }
    }

    private fun animateThingEfectoFijo(canvas: Canvas?, i: Int, thing: Thing) {
        thing.animationIndex++
        // Sin loop, lo quitamos
        if (thing.animationIndex >= EfectoResources.EFECTO_FIJO_ANIMATION_SEQUENCE.size) {
            thingsAnimated[i] = null
            return
        }
        thing.graphicIndex = EfectoResources.EFECTO_FIJO_ANIMATION_SEQUENCE[thing.animationIndex]
        if (thing.graphicIndex >= EfectoResources.EFECTO_FIJO_ANIMATION_SEQUENCE.size) {
            thing.graphicIndex = 0
        }

        var bitmap: Bitmap? = null

        val type = thing.type
        if (type == EfectoResources.EFECTO_BLOQUEO_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_BLOQUEO_GRAPHICS_BITMAP[thing.graphicIndex]

        } else if (type == EfectoResources.EFECTO_PLAYER_TOUCH_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP[thing.graphicIndex]

        } else if (type == EfectoResources.EFECTO_PLAYER_MOVE_LEFT_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP[thing.graphicIndex]

        } else if (type == EfectoResources.EFECTO_PLAYER_MOVE_RIGHT_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP[thing.graphicIndex]

        } else if (type == EfectoResources.EFECTO_TOUCH_MOVE_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP[thing.graphicIndex]
        }

        // Si el thing tiene asociado una bubble seguimos su position
        if (thing.bubble != null) {
            thing.drawX = thing.bubble!!.drawX
            thing.drawY = thing.bubble!!.drawY
        }

        canvas!!.drawBitmap(bitmap!!, thing.drawX.toFloat(), thing.drawY.toFloat(), null)
    }

    private fun moveThingBetweenBubbles(thing: Thing?): Boolean {
        if (thing == null) {
            return false
        }

        var finX = false
        var drawX = thing.drawX
        if (thing.targetX == thing.drawX) {
            finX = true

        } else if (thing.targetX > thing.drawX) {
            drawX += ThingResources.THING_PIXEL_MOVE
            if (thing.targetX - drawX < ThingResources.THING_GRAPHICS_SIZE) {
                finX = true
            } else {
                thing.drawX = drawX
            }

        } else if (thing.targetX < thing.drawX) {
            drawX -= ThingResources.THING_PIXEL_MOVE
            if (thing.drawX - thing.targetX < ThingResources.THING_GRAPHICS_SIZE) {
                finX = true
            } else {
                thing.drawX = drawX
            }
        }
        var finY = false
        var drawY = thing.drawY
        if (thing.targetY == thing.drawY) {
            finY = true

        } else if (thing.targetY > thing.drawY) {
            drawY += ThingResources.THING_PIXEL_MOVE
            if (thing.targetY - thing.drawY < ThingResources.THING_GRAPHICS_SIZE) {
                finY = true
            } else {
                thing.drawY = drawY
            }

        } else if (thing.targetY < thing.drawY) {
            drawY -= ThingResources.THING_PIXEL_MOVE
            if (thing.drawY - thing.targetY < ThingResources.THING_GRAPHICS_SIZE) {
                finY = true
            } else {
                thing.drawY = drawY
            }
        }

        return finX && finY
    }

    fun destroy() {
        BubbleResources.destroy()
        EfectoResources.destroy()
        ExplosionResources.destroy()
        ThingResources.destroy()
    }

    companion object {
        private val DEFAULT_SURFACE_WIDTH = 800
        private val DEFAULT_SURFACE_HEIGHT = 480

        // Tamaos de boards
        private var DEFAULT_BOARD_WIDTH = 320
        private var DEFAULT_BOARD_HEIGHT = 320

        private var BOARD_WIDTH = DEFAULT_BOARD_WIDTH
        private var BOARD_HEIGHT = DEFAULT_BOARD_HEIGHT

        // Offsets del board
        private var DEFAULT_OFFSET_X = 111
        private var DEFAULT_OFFSET_Y = 20

        private var OFFSET_X = DEFAULT_OFFSET_X
        private var OFFSET_Y = DEFAULT_OFFSET_Y

        // OFFSET DE COUNTERS
        private var DEFAULT_OFFSET_X_LIKE = 650
        private var DEFAULT_OFFSET_Y_LIKE = 75
        private var DEFAULT_OFFSET_X_DISLIKE = 650
        private var DEFAULT_OFFSET_Y_DISLIKE = 250

        private var OFFSET_X_LIKE = DEFAULT_OFFSET_X_LIKE
        private var OFFSET_Y_LIKE = DEFAULT_OFFSET_Y_LIKE
        private var OFFSET_X_DISLIKE = DEFAULT_OFFSET_X_DISLIKE
        private var OFFSET_Y_DISLIKE = DEFAULT_OFFSET_Y_DISLIKE

        private var COUNTER_Y_SPACER = java.lang.Double.valueOf(1.2 * ThingResources.THING_HEIGHT)!!.toInt()

        private var OFFSET_X_LIKE_COUNTER = OFFSET_X_LIKE + ThingResources.THING_WIDTH
        private var OFFSET_Y_LIKE_COUNTER = OFFSET_Y_LIKE + java.lang.Double.valueOf((ThingResources.THING_HEIGHT / 2).toDouble())!!.toInt()
        private var OFFSET_X_DISLIKE_COUNTER = OFFSET_X_DISLIKE + ThingResources.THING_WIDTH
        private var OFFSET_Y_DISLIKE_COUNTER = OFFSET_Y_DISLIKE + java.lang.Double.valueOf((ThingResources.THING_HEIGHT / 2).toDouble())!!.toInt()

        // Board num cells
        private var MAX_WIDTH = 8
        private var MAX_HEIGHT = 8

        private var random = Random()

        // Numero maximo de things animated independents
        private var MAX_THINGS_ANIMATED = 32

        private var DEFAULT_TIME_BETWEEN_BUBBLES: Long = 1000

        fun resizeBoard(surfaceWidth: Int, surfaceHeight: Int) {
            //Log.d(TAG, "Surface resize: width = " + surfaceWidth + ", height = " + surfaceHeight);

            // Nos quedamos con la proporcion mas pequea ( El board siempre ser cuadrado )
            var refactorIndex: Float
            var indexWidth = surfaceWidth.toFloat()
            indexWidth /= DEFAULT_BOARD_WIDTH.toFloat()
            var indexHeight = surfaceHeight.toFloat()
            indexHeight /= DEFAULT_BOARD_HEIGHT.toFloat()
            if (indexWidth > indexHeight) {
                BOARD_WIDTH = surfaceHeight
                BOARD_HEIGHT = surfaceHeight
                refactorIndex = indexHeight
            } else {
                BOARD_WIDTH = surfaceWidth
                BOARD_HEIGHT = surfaceWidth
                refactorIndex = indexWidth
            }

            //Log.d(TAG, "Surface resize: BOARD_WIDTH = " + BOARD_WIDTH + ", BOARD_HEIGHT = " + BOARD_HEIGHT + ", refactor index = " + refactorIndex);

            BubbleResources.resizeGraphics(refactorIndex)

            //Log.d(TAG, "Graficos Bubbles resizados");

            ExplosionResources.resizeGraphics(refactorIndex)

            //Log.d(TAG, "Graficos Explosiones resizados");

            ThingResources.resizeGraphics(refactorIndex)

            //Log.d(TAG, "Graficos Things resizados");

            EfectoResources.resizeGraphics(refactorIndex)

            //Log.d(TAG, "Graficos Efectos resizados");

            // Stablecemos offsets para centrar el board en la pantalla
            var offsetWidth = surfaceWidth.toFloat()
            offsetWidth -= (BubbleResources.BUBBLE_WIDTH * Board.MAX_WIDTH).toFloat()
            offsetWidth /= 2f
            OFFSET_X = java.lang.Float.valueOf(offsetWidth)!!.toInt()
            var offsetHeight = surfaceHeight.toFloat()
            offsetHeight -= (BubbleResources.BUBBLE_HEIGHT * Board.MAX_HEIGHT).toFloat()
            offsetHeight /= 2f
            OFFSET_Y = java.lang.Float.valueOf(offsetHeight)!!.toInt()

            // Recolocamos counters
            calculateCountersOffsets(surfaceWidth, surfaceHeight)

            //Log.d(TAG, "Surface resize: OFFSET_X = " + OFFSET_X + ", OFFSET_Y = " + OFFSET_Y );
        }

        protected fun calculateCountersOffsets(surfaceWidth: Int, surfaceHeight: Int) {
            // Calculatemos el refactor indexes
            var refactorIndexWidth = surfaceWidth.toFloat()
            refactorIndexWidth /= DEFAULT_SURFACE_WIDTH.toFloat()
            // Prevencin de cosas raras
            if (refactorIndexWidth == 0f) {
                refactorIndexWidth = 1f
            }

            var refactorIndexHeight = surfaceHeight.toFloat()
            refactorIndexHeight /= DEFAULT_SURFACE_HEIGHT.toFloat()
            // Prevencin de cosas raras
            if (refactorIndexHeight == 0f) {
                refactorIndexHeight = 1f
            }

            OFFSET_X_LIKE = java.lang.Float.valueOf(DEFAULT_OFFSET_X_LIKE * refactorIndexWidth)!!.toInt()
            OFFSET_Y_LIKE = java.lang.Float.valueOf(DEFAULT_OFFSET_Y_LIKE * refactorIndexHeight)!!.toInt()
            OFFSET_X_DISLIKE = java.lang.Float.valueOf(DEFAULT_OFFSET_X_DISLIKE * refactorIndexWidth)!!.toInt()
            OFFSET_Y_DISLIKE = java.lang.Float.valueOf(DEFAULT_OFFSET_Y_DISLIKE * refactorIndexHeight)!!.toInt()

            COUNTER_Y_SPACER = java.lang.Double.valueOf(1.2 * ThingResources.THING_HEIGHT)!!.toInt()
            OFFSET_X_LIKE_COUNTER = OFFSET_X_LIKE + ThingResources.THING_WIDTH
            OFFSET_Y_LIKE_COUNTER = OFFSET_Y_LIKE + java.lang.Double.valueOf((ThingResources.THING_HEIGHT / 2).toDouble())!!.toInt()
            OFFSET_X_DISLIKE_COUNTER = OFFSET_X_DISLIKE + ThingResources.THING_WIDTH
            OFFSET_Y_DISLIKE_COUNTER = OFFSET_Y_DISLIKE + java.lang.Double.valueOf((ThingResources.THING_HEIGHT / 2).toDouble())!!.toInt()
        }
    }
}

/**
 * Classe auxiliar para devolver posicin en cell de un Thing en board
 *
 * @author amarinji
 */
private class CellPosition(i: Int, j: Int) {
    var i = 0
    var j = 0

    init {
        this.i = i
        this.j = j
    }
}
