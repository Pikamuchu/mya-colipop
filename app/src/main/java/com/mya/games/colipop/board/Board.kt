package com.mya.games.colipop.board

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.MotionEvent

import com.mya.games.colipop.ColiPopResources
import com.mya.games.colipop.IATouchEvent
import com.mya.games.colipop.character.Character

import java.util.Random

class Board
/**
 * Constructors
 */
(resources: Resources, internal var character1: Character, character2: Character) {

    /**
     * Class properties
     */

    // Resources
    internal var resources: Resources? = null

    internal var bubbleCommonAnimationIndex = 0
    //Character character2;

    internal var cells = Array<Array<Cell>>(MAX_WIDTH) { arrayOfNulls(MAX_HEIGHT) }

    internal var initFullBoard = true
    internal var boardInitialized = false
    internal var boardStable = false
    internal var boardStableTime: Long = 0
    internal var lastInitialBubbleTime: Long = 0
    var isBoardFullBubbles = false
    var isBoardFullThings = false

    internal var things_animated: Array<Thing>? = arrayOfNulls(MAX_THINGS_ANIMATED)

    internal var things_explodes = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    internal var timeBetweenBubbles = DEFAULT_TIME_BETWEEN_BUBBLES

    internal var lastLevel = 0

    // Localizing external variables
    // No se ha encontrado cell inicial probamos otra vez desde el 0
    // No se ha encontrado cell inicial ?!!??
    val initialCellXPos: Int
        get() {
            val cells = this.cells
            val MAX_WIDTH = Board.MAX_WIDTH
            val MAX_HEIGHT = Board.MAX_HEIGHT

            val init = random.nextInt(MAX_WIDTH - 1)

            for (i in init until MAX_WIDTH) {
                val leftCell = cells[i][MAX_HEIGHT - 1]
                if (leftCell == null || leftCell.bubble == null) {
                    return i
                }
            }
            for (i in 0 until init - 1) {
                val leftCell = cells[i][MAX_HEIGHT - 1]
                if (leftCell == null || leftCell.bubble == null) {
                    return i
                }
            }
            return -1
        }

    init {
        this.resources = resources
        //this.character2 = character2;

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

    fun initBoard() {
        this.cells = Array(MAX_WIDTH) { arrayOfNulls(MAX_HEIGHT) }

        this.character1.initCharacter()
        //this.character2.initCharacter();

        // Localizing external variables
        val cells = this.cells
        val initFullBoard = this.initFullBoard

        val BUBBLE_WIDTH = BubbleResources.BUBBLE_WIDTH
        val BUBBLE_HEIGHT = BubbleResources.BUBBLE_HEIGHT
        val OFFSET_X = Board.OFFSET_X
        val OFFSET_Y = Board.OFFSET_Y
        val MAX_WIDTH = Board.MAX_WIDTH
        val MAX_HEIGHT = Board.MAX_HEIGHT

        for (i in 0 until MAX_WIDTH) {

            for (j in 0 until MAX_HEIGHT) {

                val cell = Cell(i, j)

                // Condicion de inicializar board full de bubbles
                if (initFullBoard) {

                    val bubble = Bubble()

                    bubble.drawX = OFFSET_X + i * BUBBLE_WIDTH
                    bubble.drawY = OFFSET_Y + j * BUBBLE_HEIGHT

                    cell.bubble = bubble

                    addRandomObject(cell)

                    // Comprobamos que el thing no pete nada
                    if (i >= 2 && cell.thing != null
                            && cells[i - 1][j].thing != null && cells[i - 1][j].thing!!.type == cell.thing!!.type
                            && cells[i - 2][j].thing != null && cells[i - 2][j].thing!!.type == cell.thing!!.type) {

                        cell.thing = null
                    }
                    if (j >= 2 && cell.thing != null
                            && cells[i][j - 1].thing != null && cells[i][j - 1].thing!!.type == cell.thing!!.type
                            && cells[i][j - 2].thing != null && cells[i][j - 2].thing!!.type == cell.thing!!.type) {

                        cell.thing = null
                    }

                }

                cells[i][j] = cell

            }

        }

        boardStable = true
        boardStableTime = System.currentTimeMillis()

        // Inicializamos marcadores
        things_explodes = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

        things_animated = arrayOfNulls(MAX_THINGS_ANIMATED)

        boardInitialized = true

        // Resto variables
        timeBetweenBubbles = DEFAULT_TIME_BETWEEN_BUBBLES
        lastLevel = 0

        //Log.d(TAG, "Board inicializado");
    }

    fun getCell(i: Int, j: Int): Cell? {
        return if (i < 0 || i >= MAX_WIDTH || j < 0 || j >= MAX_HEIGHT) {
            null
        } else cells[i][j]
    }

    fun doBoardAnimation(canvas: Canvas?) {
        if (canvas == null || !boardInitialized) {
            return
        }

        this.bubbleCommonAnimationIndex++
        // Doing loop
        if (this.bubbleCommonAnimationIndex >= BubbleResources.BUBBLE_ANIMATION_SEQUENCE.size) {
            this.bubbleCommonAnimationIndex = 0
        }

        // Localizing external variables
        val cells = this.cells
        val OFFSET_X = Board.OFFSET_X
        val OFFSET_Y = Board.OFFSET_Y
        val MAX_WIDTH = Board.MAX_WIDTH
        val MAX_HEIGHT = Board.MAX_HEIGHT
        val boardStable = this.boardStable

        // Localizing external variables
        val BUBBLE_WIDTH = BubbleResources.BUBBLE_WIDTH
        val BUBBLE_HEIGHT = BubbleResources.BUBBLE_HEIGHT
        /* Save memory
		int BUBBLE_STATUS_GRAPHICS_SIZE = BubbleResources.BUBBLE_STATUS_GRAPHICS_SIZE;
		*/
        val BUBBLE_UNION_UP = BubbleResources.BUBBLE_UNION_UP
        val BUBBLE_UNION_LEFT = BubbleResources.BUBBLE_UNION_LEFT
        val BUBBLE_UNION_RIGHT = BubbleResources.BUBBLE_UNION_RIGHT
        val BUBBLE_UNION_DOWN = BubbleResources.BUBBLE_UNION_DOWN
        val BUBBLE_UNION_NONE = BubbleResources.BUBBLE_UNION_NONE
        val BUBBLE_MOVE_UP = BubbleResources.BUBBLE_MOVE_UP
        val BUBBLE_MOVE_LEFT = BubbleResources.BUBBLE_MOVE_LEFT
        val BUBBLE_MOVE_RIGHT = BubbleResources.BUBBLE_MOVE_RIGHT
        val BUBBLE_MOVE_DOWN = BubbleResources.BUBBLE_MOVE_DOWN
        val BUBBLE_MOVE_NONE = BubbleResources.BUBBLE_MOVE_NONE
        val BUBBLE_GRAPHICS_BITMAP = BubbleResources.BUBBLE_GRAPHICS_BITMAP
        val BUBBLE_MOVE_GRAPHICS_BITMAP = BubbleResources.BUBBLE_MOVE_GRAPHICS_BITMAP
        /* Save memory
		Bitmap[] BUBBLE_UNION_GRAPHICS_BITMAP = BubbleResources.BUBBLE_UNION_GRAPHICS_BITMAP;
		Bitmap[] BUBBLE_STATUS_GRAPHICS_BITMAP = BubbleResources.BUBBLE_STATUS_GRAPHICS_BITMAP;
		*/

        val EXPLOSION_GRAPHICS_SIZE = ExplosionResources.EXPLOSION_GRAPHICS_SIZE
        val EXPLOSION_GRAPHICS_BITMAP = ExplosionResources.EXPLOSION_GRAPHICS_BITMAP

        val THING_STATUS_BUBBLE = ThingResources.THING_STATUS_BUBBLE
        val THING_STATUS_MOVIDO = ThingResources.THING_STATUS_MOVIDO
        val THING_STATUS_EXPLODE = ThingResources.THING_STATUS_EXPLODE
        val CARAMELO_OBJECT_TYPE = ThingResources.CARAMELO_OBJECT_TYPE
        val CARAMELO_GRAPHICS_BITMAP = ThingResources.CARAMELO_GRAPHICS_BITMAP
        val CARAMELO_GRANDE_GRAPHICS_BITMAP = ThingResources.CARAMELO_GRANDE_GRAPHICS_BITMAP
        val PIRULETA_OBJECT_TYPE = ThingResources.PIRULETA_OBJECT_TYPE
        val PIRULETA_GRAPHICS_BITMAP = ThingResources.PIRULETA_GRAPHICS_BITMAP
        val PIRULETA_GRANDE_GRAPHICS_BITMAP = ThingResources.PIRULETA_GRANDE_GRAPHICS_BITMAP
        val RASPA_OBJECT_TYPE = ThingResources.RASPA_OBJECT_TYPE
        val RASPA_GRAPHICS_BITMAP = ThingResources.RASPA_GRAPHICS_BITMAP
        val RASPA_GRANDE_GRAPHICS_BITMAP = ThingResources.RASPA_GRANDE_GRAPHICS_BITMAP
        val PEINE_OBJECT_TYPE = ThingResources.PEINE_OBJECT_TYPE
        val PEINE_GRAPHICS_BITMAP = ThingResources.PEINE_GRAPHICS_BITMAP
        val PEINE_GRANDE_GRAPHICS_BITMAP = ThingResources.PEINE_GRANDE_GRAPHICS_BITMAP

        var boardFullBubbles = true
        var boardFullThings = true
        var hayMovimiento = false
        for (i in 0 until MAX_WIDTH) {
            for (j in 0 until MAX_HEIGHT) {
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
                    val bubbleCellPosX = OFFSET_X + i * BUBBLE_HEIGHT
                    if (bubble.drawX == bubbleCellPosX && canBubbleMoveUp(cells, bubble, i, j)) {

                        moveAndUpdateBubblePosition(cells, bubble, thing, i, j, BUBBLE_MOVE_UP, false)
                        // indicamos que hay movimiento de bubbles
                        hayMovimiento = true
                        // Marcamos bubble como que se move
                        bubble.move = BUBBLE_MOVE_UP

                        /* amarinji: deshabilito la logica de movimientos laterales automaticos de las bubbles, ya que hace cosas raras y tiene bugs que no se como solucionar
		            // Miramos esto solo si la bubble no se est moviendo o se esta moviendo en la direction adecuada
		            } else if ( ( bubble.move == BUBBLE_MOVE_NONE || bubble.move == BUBBLE_MOVE_LEFT ) && canBubbleMoveLeft( cells, bubble, i, j ) ) {

		            	moveAndUpdateBubblePosition( cells, bubble, thing, i, j, BUBBLE_MOVE_LEFT, false );
		            	// indicamos que hay movimiento de bubbles
		            	hayMovimiento = true;
	            		// Marcamos bubble como que se move
	            		bubble.move = BUBBLE_MOVE_LEFT;

		            // Miramos esto solo si la bubble no se est moviendo o se esta moviendo en la direction adecuada
		            } else if ( ( bubble.move == BUBBLE_MOVE_NONE || bubble.move == BUBBLE_MOVE_RIGHT ) && canBubbleMoveRight( cells, bubble, i, j ) ) {
				        // Ojo! por aqu hay un bug; no te deja mover la bubble hacia una posicin donde la bubble puede caer a la derecha
		            	moveAndUpdateBubblePosition( cells, bubble, thing, i, j, BUBBLE_MOVE_RIGHT, true );
		            	// indicamos que hay movimiento de bubbles
		            	hayMovimiento = true;
	            		// Marcamos bubble como que se move
	            		bubble.move = BUBBLE_MOVE_RIGHT;
*/

                    } else {
                        // Centramos la bubble en su cell si no lo est
                        val bubbleCellPosY = OFFSET_Y + j * BUBBLE_HEIGHT
                        if (bubble.drawY != bubbleCellPosY || bubble.drawX != bubbleCellPosX) {
                            centraBubbleEnPosicion(bubble, bubbleCellPosX, bubbleCellPosY)
                            // indicamos que hay movimiento de bubbles
                            hayMovimiento = true
                        } else {
                            // Marcamos bubble como que no se move
                            bubble.move = BUBBLE_MOVE_NONE
                        }
                    }

                    /* comento lo de los fondos
		            // Fondo bubble
		            if ( bubble.status < BUBBLE_STATUS_GRAPHICS_SIZE ) {
		            	canvas.drawBitmap(BUBBLE_STATUS_GRAPHICS_BITMAP[bubble.status], bubble.drawX, bubble.drawY, null);
		            }
		            */

                    if (thing != null && (thing.status == THING_STATUS_BUBBLE || thing.status == THING_STATUS_MOVIDO)) {
                        var bitmap: Bitmap? = null

                        val type = thing.type
                        if (type == CARAMELO_OBJECT_TYPE) {
                            bitmap = CARAMELO_GRAPHICS_BITMAP[thing.graphicIndex]
                        } else if (type == PIRULETA_OBJECT_TYPE) {
                            bitmap = PIRULETA_GRAPHICS_BITMAP[thing.graphicIndex]
                        } else if (type == RASPA_OBJECT_TYPE) {
                            bitmap = RASPA_GRAPHICS_BITMAP[thing.graphicIndex]
                        } else if (type == PEINE_OBJECT_TYPE) {
                            bitmap = PEINE_GRAPHICS_BITMAP[thing.graphicIndex]
                        }

                        if (thing.status == THING_STATUS_MOVIDO) {
                            // Movemos bubble hasta su destiny
                            if (moveThingBetweenBubbles(thing)) {
                                // Si el thing est ubicado reseteamos statuss union de las bubbles que intervienen
                                thing.status = THING_STATUS_BUBBLE
                                if (bubble.union == BUBBLE_UNION_UP) {
                                    cells[i][j + 1].bubble!!.union = BUBBLE_UNION_NONE
                                } else if (bubble.union == BUBBLE_UNION_DOWN) {
                                    cells[i][j - 1].bubble!!.union = BUBBLE_UNION_NONE
                                } else if (bubble.union == BUBBLE_UNION_RIGHT) {
                                    cells[i + 1][j].bubble!!.union = BUBBLE_UNION_NONE
                                } else if (bubble.union == BUBBLE_UNION_LEFT) {
                                    cells[i - 1][j].bubble!!.union = BUBBLE_UNION_NONE
                                }
                                bubble.union = BUBBLE_UNION_NONE
                            }
                            // indicamos que hay movimiento de bubbles
                            hayMovimiento = true

                        } else {
                            // Sincronizamos posiciones de thing y bubble
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

                    var bitmap: Bitmap? = null
                    // Caso bubble en movimiento
                    if (bubble.move != BUBBLE_MOVE_NONE) {
                        bitmap = BUBBLE_MOVE_GRAPHICS_BITMAP[bubble.graphicIndex]

                        // Caso bubble quieta
                    } else {
                        /* Save memory
                		int union = bubble.union;
                		// Caso bubble unida
                		if ( union != BUBBLE_UNION_NONE ) {
                			bitmap = BUBBLE_UNION_GRAPHICS_BITMAP[union];
	            		// Caso bubble quieta y sin unir
	            		} else {
	            			bitmap = BUBBLE_GRAPHICS_BITMAP[bubble.graphicIndex];
	            		}
	            		*/
                        bitmap = BUBBLE_GRAPHICS_BITMAP[bubble.graphicIndex]
                    }

                    // Animate bubble
                    canvas.drawBitmap(bitmap!!, bubble.drawX.toFloat(), bubble.drawY.toFloat(), null)
                }

                val explosion = cell.explosion
                if (explosion != null) {

                    // cambiamos el indice de animacin
                    explosion.graphicIndex++

                    // Caso bubble ha acabado de exploder
                    if (explosion.graphicIndex >= EXPLOSION_GRAPHICS_SIZE) {

                        cell.explosion = null

                        if (thing != null && thing.status == THING_STATUS_EXPLODE) {
                            things_explodes[thing.type]++
                            cell.thing = null

                        }
                        /* amarinji: comento explosion en cadena de bubbles vacias
	                	// Caso bubble removeda
	                	else {

		                	// Miramos si hay explosion en cadena de bubbles sin things
				            if ( i >= 1 && thing == null
				            		&& cells[i-1][j].bubble != null && cells[i-1][j].thing == null ) {

				            	removeBubble( cells[i-1][j] );
				            	hayMovimiento = true;

				            }
				            if ( i < (MAX_WIDTH-1) && thing == null
				            		&& cells[i+1][j].bubble != null && cells[i+1][j].thing == null ) {

				            	removeBubble( cells[i+1][j] );
				            	hayMovimiento = true;

				            }
				            if ( j >= 1 && thing == null
				            		&& cells[i][j-1].bubble != null && cells[i][j-1].thing == null ) {

				            	removeBubble( cells[i][j-1] );
				            	hayMovimiento = true;

				            }
				            if ( j < (MAX_WIDTH-1) && thing == null
				            		&& cells[i][j+1].bubble != null && cells[i][j+1].thing == null ) {

				            	removeBubble( cells[i][j+1] );
				            	hayMovimiento = true;

				            }

	                	} */

                        /* amarinji: comento caso  de una bubble vacia afecta al status de las de alrededor
	                	// Caso la explosion de una bubble vacia afecta al status de las de alrededor
	                	else {
				            if ( i > 0 ) {
				            	updateStatusBubble( cells[i-1][j] );
				            }
				            if ( i < (MAX_WIDTH-1) ) {
				            	updateStatusBubble( cells[i+1][j] );
				            }
				            if ( j > 0 ) {
				            	updateStatusBubble( cells[i][j-1] );
				            }
				            if ( j < (MAX_WIDTH-1) ) {
				            	updateStatusBubble( cells[i][j+1] );
				            }
	                	}
	            		*/

                        // Caso animating la bubble explodendo
                    } else {

                        // Animate explosion
                        canvas.drawBitmap(EXPLOSION_GRAPHICS_BITMAP[explosion.graphicIndex], explosion.drawX.toFloat(), explosion.drawY.toFloat(), null)

                        // Animate thing grande
                        if (thing != null && thing.status == THING_STATUS_EXPLODE) {

                            var bitmap: Bitmap? = null

                            val type = thing.type
                            if (type == CARAMELO_OBJECT_TYPE) {
                                bitmap = CARAMELO_GRANDE_GRAPHICS_BITMAP[0]
                            } else if (type == PIRULETA_OBJECT_TYPE) {
                                bitmap = PIRULETA_GRANDE_GRAPHICS_BITMAP[0]
                            } else if (type == RASPA_OBJECT_TYPE) {
                                bitmap = RASPA_GRANDE_GRAPHICS_BITMAP[0]
                            } else if (type == PEINE_OBJECT_TYPE) {
                                bitmap = PEINE_GRANDE_GRAPHICS_BITMAP[0]
                            }

                            canvas.drawBitmap(bitmap!!, explosion.drawX.toFloat(), explosion.drawY.toFloat(), null)
                        }

                        // indicamos que hay movimiento en el board
                        hayMovimiento = true

                    }

                }

                // ahora no hace falta que el board est stable, solo que las bubbles no se muevan
                //if ( boardStable ) {

                /**
                 * Logica matcheado horizontal de things
                 */

                // Miramos si la bubble tiene thing
                if (i < MAX_WIDTH - 2 && bubble != null && bubble.move == BUBBLE_MOVE_NONE && thing != null && thing.status == THING_STATUS_BUBBLE) {
                    // Miramos si la bubble de la derecha tiene el mismo thing
                    val thingType = thing.type
                    val cellRight1 = cells[i + 1][j]
                    if (cellRight1 != null && cellRight1.bubble != null && cellRight1.bubble!!.move == BUBBLE_MOVE_NONE
                            && cellRight1.thing != null && cellRight1.thing!!.type == thingType && cellRight1.thing!!.status == THING_STATUS_BUBBLE) {
                        // Miramos si la bubble de mas a la derecha tiene el mismo thing
                        val cellRight2 = cells[i + 2][j]
                        if (cellRight2 != null && cellRight2.bubble != null && cellRight2.bubble!!.move == BUBBLE_MOVE_NONE
                                && cellRight2.thing != null && cellRight2.thing!!.type == thingType && cellRight2.thing!!.status == THING_STATUS_BUBBLE) {
                            // Explodemos bubbles
                            explodeBubble(cell)
                            explodeBubble(cellRight1)
                            explodeBubble(cellRight2)
                            // variable para contabilizar things explodes
                            var numThings = 3
                            // ignoramos las cells de la derecha para evitar efecto raro
                            cellRight1.ignore = true
                            cellRight2.ignore = true
                            // Si hay mas bubbles con el mismo thing las explodemos tambien
                            for (k in i + 3 until MAX_WIDTH) {
                                val cellRight = cells[k][j]
                                if (cellRight != null && cellRight.bubble != null && cellRight.bubble!!.move == BUBBLE_MOVE_NONE
                                        && cellRight.thing != null && cellRight.thing!!.type == thingType && cellRight.thing!!.status == THING_STATUS_BUBBLE) {
                                    explodeBubble(cellRight)
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
                                if (cellLeft != null && cellLeft.bubble != null && cellLeft.bubble!!.move == BUBBLE_MOVE_NONE
                                        && cellLeft.thing != null && cellLeft.thing!!.type == thingType && cellLeft.thing!!.status == THING_STATUS_BUBBLE) {
                                    explodeBubble(cellLeft)
                                    numThings++
                                } else {
                                    // En el momento en que no encontremos una bubble a exploder salimos del for
                                    break
                                }
                            }
                            // Marcamos como que hay movimiento
                            hayMovimiento = true
                            // Updatemos meters de jugadores
                            this.character1.updateMeter(thingType, numThings)
                        }
                    }
                }

                /**
                 * Logica matcheado vertical de things
                 */

                // Miramos si la bubble tiene thing
                if (j < MAX_HEIGHT - 2 && bubble != null && bubble.move == BUBBLE_MOVE_NONE && thing != null && thing.status == THING_STATUS_BUBBLE) {
                    // Miramos si la bubble de la arriba tiene el mismo thing
                    val thingType = thing.type
                    val cellDown1 = cells[i][j + 1]
                    if (cellDown1 != null && cellDown1.bubble != null && cellDown1.bubble!!.move == BUBBLE_MOVE_NONE
                            && cellDown1.thing != null && cellDown1.thing!!.type == thingType && cellDown1.thing!!.status == THING_STATUS_BUBBLE) {
                        // Miramos si la bubble de mas a la arriba tiene el mismo thing
                        val cellDown2 = cells[i][j + 2]
                        if (cellDown2 != null && cellDown2.bubble != null && cellDown2.bubble!!.move == BUBBLE_MOVE_NONE
                                && cellDown2.thing != null && cellDown2.thing!!.type == thingType && cellDown2.thing!!.status == THING_STATUS_BUBBLE) {
                            // Explodemos bubbles
                            explodeBubble(cell)
                            explodeBubble(cellDown1)
                            explodeBubble(cellDown2)
                            // variable para contabilizar things explodes
                            var numThings = 3
                            // ignoramos las cells de abajo para evitar efectos raros
                            cellDown1.ignore = true
                            cellDown2.ignore = true
                            // Si hay mas bubbles con el mismo thing las explodemos tambien
                            for (k in j + 3 until MAX_HEIGHT) {
                                val cellDown = cells[i][k]
                                if (cellDown != null && cellDown.bubble != null && cellDown.bubble!!.move == BUBBLE_MOVE_NONE
                                        && cellDown.thing != null && cellDown.thing!!.type == thingType && cellDown.thing!!.status == THING_STATUS_BUBBLE) {
                                    explodeBubble(cellDown)
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
                                if (cellDown != null && cellDown.bubble != null && cellDown.bubble!!.move == BUBBLE_MOVE_NONE
                                        && cellDown.thing != null && cellDown.thing!!.type == thingType && cellDown.thing!!.status == THING_STATUS_BUBBLE) {
                                    explodeBubble(cellDown)
                                    numThings++
                                } else {
                                    // En el momento en que no encontremos una bubble a exploder salimos del for
                                    break
                                }
                            }
                            // Marcamos como que hay movimiento
                            hayMovimiento = true
                            // Updatemos meters de jugadores
                            this.character1.updateMeter(thingType, numThings)
                        }
                    }
                }

                //}

            }

        }

        // Control de movimientos en el board
        if (hayMovimiento) {
            this.boardStable = false
        } else {
            // Solo updatemos a true el flag una vez para no falsear boardStableTime
            if (!this.boardStable) {
                this.boardStable = true
                this.boardStableTime = System.currentTimeMillis()
            }
        }

        // Board full de bubbles
        this.isBoardFullThings = boardFullThings
        this.isBoardFullBubbles = boardFullBubbles

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

        canvas.drawBitmap(CARAMELO_GRAPHICS_BITMAP[0], offset_x_like.toFloat(), offset_y_like.toFloat(), null)
        canvas.drawText(" = " + things_explodes[CARAMELO_OBJECT_TYPE], offset_x_like_counter.toFloat(), offset_y_like_counter.toFloat(), ColiPopResources.paint)

        offset_y_like += COUNTER_Y_SPACER
        offset_y_like_counter += COUNTER_Y_SPACER

        canvas.drawBitmap(PIRULETA_GRAPHICS_BITMAP[0], offset_x_like.toFloat(), offset_y_like.toFloat(), null)
        canvas.drawText(" = " + things_explodes[PIRULETA_OBJECT_TYPE], offset_x_like_counter.toFloat(), offset_y_like_counter.toFloat(), ColiPopResources.paint)

        // DISLIKE
        val offset_x_dislike = OFFSET_X_DISLIKE
        var offset_y_dislike = OFFSET_Y_DISLIKE
        val offset_x_dislike_counter = OFFSET_X_DISLIKE_COUNTER
        var offset_y_dislike_counter = OFFSET_Y_DISLIKE_COUNTER

        canvas.drawBitmap(RASPA_GRAPHICS_BITMAP[0], offset_x_dislike.toFloat(), offset_y_dislike.toFloat(), null)
        canvas.drawText(" = " + things_explodes[RASPA_OBJECT_TYPE], offset_x_dislike_counter.toFloat(), offset_y_dislike_counter.toFloat(), ColiPopResources.paint)

        offset_y_dislike += COUNTER_Y_SPACER
        offset_y_dislike_counter += COUNTER_Y_SPACER

        canvas.drawBitmap(PEINE_GRAPHICS_BITMAP[0], offset_x_dislike.toFloat(), offset_y_dislike.toFloat(), null)
        canvas.drawText(" = " + things_explodes[PEINE_OBJECT_TYPE], offset_x_dislike_counter.toFloat(), offset_y_dislike_counter.toFloat(), ColiPopResources.paint)
    }

    fun updateStatusBubble(cell: Cell?) {
        if (cell == null || cell.bubble == null) {
            return
        }
        cell.bubble!!.status++
        if (cell.bubble!!.status > BubbleResources.BUBBLE_STATUS_ROJO) {
            removeBubble(cell)
        }
    }

    fun updateBubbleAnimation(bubble: Bubble, thing: Thing?) {
        if (bubble.move == BubbleResources.BUBBLE_MOVE_NONE) {
            // Todas las bubbles que estn quietas utilizan el mismo indice
            bubble.graphicIndex = BubbleResources.BUBBLE_ANIMATION_SEQUENCE[this.bubbleCommonAnimationIndex]
            if (bubble.graphicIndex >= BubbleResources.BUBBLE_GRAPHICS_SIZE) {
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
            if (bubble.graphicIndex >= BubbleResources.BUBBLE_MOVE_GRAPHICS_SIZE) {
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

    fun canBubbleMoveUp(cells: Array<Array<Cell>>, bubble: Bubble?, i: Int, j: Int): Boolean {
        if (bubble == null) {
            // TODO: revisar esto
            return false
        }
        if (j == 0) {
            // Esta arriba del todo, no se puede mover mas
            return false
        }
        val upCell = cells[i][j - 1]
        return if (upCell == null || upCell.bubble == null && upCell.explosion == null) {
            true
        } else {
            false
        }
    }

    fun canBubbleMoveDown(cells: Array<Array<Cell>>, bubble: Bubble?, i: Int, j: Int): Boolean {
        if (bubble == null) {
            // TODO: revisar esto
            return false
        }
        if (j == MAX_HEIGHT - 1) {
            // Esta arriba del todo, no se puede mover mas
            return false
        }
        val downCell = cells[i][j + 1]
        return if (downCell == null || downCell.bubble == null && downCell.explosion == null) {
            true
        } else {
            false
        }
    }

    fun canBubbleMoveRight(cells: Array<Array<Cell>>, bubble: Bubble?, i: Int, j: Int): Boolean {
        if (bubble == null) {
            // TODO: revisar esto
            return false
        }
        // Miramos que hacia la derecha haya sitio para moverse
        if (i == MAX_WIDTH - 1) {
            // Esta a la derecha del todo, no se puede mover mas
            return false
        }
        val rightCell = cells[i + 1][j]
        if (rightCell == null || rightCell.bubble == null && rightCell.explosion == null) {
            // Miramos que hacia arriba a la derecha haya un hueco donde meterse
            if (j == 0) {
                // No puede haber hueco ya que esta arriba del todo
                return false
            }
            val rightUpCell = cells[i + 1][j - 1]
            return if (rightUpCell == null || rightUpCell.bubble == null && rightUpCell.explosion == null) {
                // OK !!!
                true
            } else {
                // No hay hueco arriba a la derecha
                false
            }
        } else {
            // No hay sitio a la derecha
            return false
        }
    }

    fun canBubbleMoveLeft(cells: Array<Array<Cell>>, bubble: Bubble?, i: Int, j: Int): Boolean {
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
        if (leftCell == null || leftCell.bubble == null && leftCell.explosion == null) {
            // Miramos que hacia arriba a la izquierda haya un hueco donde meterse
            if (j == 0) {
                // No puede haber hueco ya que esta arriba del todo
                return false
            }
            val leftUpCell = cells[i - 1][j - 1]
            return if (leftUpCell == null || leftUpCell.bubble == null && leftUpCell.explosion == null) {
                // OK !!!
                true
            } else {
                // No hay hueco arriba a la izquierda
                false
            }
        } else {
            return false
        }
    }

    fun centraBubbleEnPosicion(bubble: Bubble?, x: Int, y: Int) {
        if (bubble == null) {
            return
        }

        // Localizing external variables
        val BUBBLE_PIXEL_MOVE = BubbleResources.BUBBLE_PIXEL_MOVE

        var finX = false
        var drawX = bubble.drawX
        if (x == bubble.drawX) {
            finX = true

        } else if (x > bubble.drawX) {
            drawX += BUBBLE_PIXEL_MOVE
            if (x - drawX < BUBBLE_PIXEL_MOVE) {
                finX = true
            } else {
                bubble.drawX = drawX
            }

        } else if (x < bubble.drawX) {
            drawX -= BUBBLE_PIXEL_MOVE
            if (bubble.drawX - x < BUBBLE_PIXEL_MOVE) {
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
            drawY += BUBBLE_PIXEL_MOVE
            if (y - bubble.drawY < BUBBLE_PIXEL_MOVE) {
                finY = true
            } else {
                bubble.drawY = drawY
            }

        } else if (y < bubble.drawY) {
            drawY -= BUBBLE_PIXEL_MOVE
            if (bubble.drawY - y < BUBBLE_PIXEL_MOVE) {
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
            // No se ha encontrado posicion inicial
            return
        }

        val bubble = Bubble()

        bubble.drawX = OFFSET_X + xPos * BubbleResources.BUBBLE_WIDTH
        bubble.drawY = OFFSET_Y + MAX_HEIGHT * BubbleResources.BUBBLE_HEIGHT

        val cell = cells[xPos][MAX_HEIGHT - 1] ?: return

        cell.bubble = bubble
        addRandomObject(cell)

        lastInitialBubbleTime = System.currentTimeMillis()
    }

    internal fun addRandomObject(cell: Cell?) {
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

    fun moveAndUpdateBubblePosition(cells: Array<Array<Cell>>, bubble: Bubble, thing: Thing?, i: Int, j: Int, direction: Int, ignoreNewCell: Boolean) {
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
                ?: // Error calculo posicion
                return

        val newCell = cells[newCellPos.i][newCellPos.j]
        if (newCell.bubble != null) {
            // La cell ya est ocupada
            return
        }

        // Quitamos la bubble de su cell anterior
        val cell = cells[i][j]
        cell.bubble = null

        // Ponemos la bubble en la nueva posicion
        newCell.bubble = bubble

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
            cell.thing = null
            newCell.thing = thing
        }

        if (ignoreNewCell) {
            newCell.ignore = true
        }
    }

    fun calculatePosBubbleInBoard(bubble: Bubble): CellPosition {
        var xPos = (bubble.drawX - OFFSET_X) / BubbleResources.BUBBLE_WIDTH
        var yPos = (bubble.drawY - OFFSET_Y) / BubbleResources.BUBBLE_HEIGHT

        if (xPos < 0) {
            xPos = 0
        }
        if (xPos >= MAX_WIDTH) {
            xPos = MAX_WIDTH - 1
        }

        if (yPos < 0) {
            yPos = 0
        }
        if (yPos >= MAX_HEIGHT) {
            yPos = MAX_HEIGHT - 1
        }

        return CellPosition(xPos, yPos)
    }

    fun getCellInCoordinates(x: Int, y: Int): Cell? {
        if (!boardInitialized) {
            return null
        }

        val normX = (x - Board.OFFSET_X) / BubbleResources.BUBBLE_WIDTH
        val normY = (y - Board.OFFSET_Y) / BubbleResources.BUBBLE_HEIGHT

        //Log.d(TAG, "getCellInCoordinates: x=" + x + ", y=" + y + ", normX=" + normX + ", normY=" + normY );

        return if (normX < Board.MAX_WIDTH && normX >= 0 && normY < Board.MAX_HEIGHT && normY >= 0) {
            cells[normX][normY]
        } else null

    }

    fun explodeBubble(cell: Cell?) {
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

        // Localizing external variables
        val things_animated = this.things_animated

        // Aadimos thing a los animated independentmente y lo quitamos de la cell
        var thingAdded = false
        for (i in 0 until MAX_THINGS_ANIMATED) {
            if (things_animated!![i] == null) {
                things_animated[i] = thing
                thingAdded = true
                break
            }
        }
        // Si no encontramos posicin libre, lo metemos en una posicin aleatoria
        if (!thingAdded) {
            things_animated[random.nextInt(MAX_THINGS_ANIMATED - 1)] = thing
        }
        cell.thing = null

        // Tambien controla la velocidad del turno de la CPU
        boardStable = false
    }

    fun moveThingBetweenBubbles(origin: Cell?, destiny: Cell?) {
        if (origin == null || destiny == null) {
            return
        }

        val deltaX = origin.posX - destiny.posX
        val deltaY = origin.posY - destiny.posY
        if (deltaX > 1 || deltaX < -1 || deltaY > 1 || deltaY < -1) {
            return
        }

        val thing = origin.thing ?: return

        val bubbleDestiny = destiny.bubble ?: return

        thing.targetX = bubbleDestiny.drawX
        thing.targetY = bubbleDestiny.drawY

        thing.status = ThingResources.THING_STATUS_MOVIDO

        // Quitamos el thing de la cell origin y lo ponemos en la cell destiny
        origin.thing = null
        destiny.thing = thing

        // Updatemos status de movimiento de las bubbles
        if (deltaX == 0 && deltaY > 0) {
            origin.bubble!!.union = BubbleResources.BUBBLE_UNION_DOWN
            destiny.bubble!!.union = BubbleResources.BUBBLE_UNION_UP
        } else if (deltaX == 0 && deltaY < 0) {
            origin.bubble!!.union = BubbleResources.BUBBLE_UNION_UP
            destiny.bubble!!.union = BubbleResources.BUBBLE_UNION_DOWN
        } else if (deltaY == 0 && deltaX > 0) {
            origin.bubble!!.union = BubbleResources.BUBBLE_UNION_LEFT
            destiny.bubble!!.union = BubbleResources.BUBBLE_UNION_RIGHT
        } else if (deltaY == 0 && deltaX < 0) {
            origin.bubble!!.union = BubbleResources.BUBBLE_UNION_RIGHT
            destiny.bubble!!.union = BubbleResources.BUBBLE_UNION_LEFT
        }

        // Tambien controla la velocidad del turno de la CPU
        this.boardStable = false
    }

    fun touchBubble(cell: Cell?, directionX: Int, directionY: Int) {
        if (cell == null) {
            return
        }

        val bubble = cell.bubble ?: return

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

        val bubble = origin.bubble ?: return

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
        val things_animated = this.things_animated

        // Aadimos thing a los animated independentmente
        var thingAdded = false
        for (i in 0 until MAX_THINGS_ANIMATED) {
            if (things_animated!![i] == null) {
                things_animated[i] = thing
                thingAdded = true
                break
            }
        }
        // Si no encontramos posicin libre, lo metemos en una posicin aleatoria
        if (!thingAdded) {
            things_animated[random.nextInt(MAX_THINGS_ANIMATED - 1)] = thing
        }
    }

    fun addEfectoTouch(drawX: Int, drawY: Int, efectoFijoType: Int) {
        val thing = Thing()

        thing.drawX = drawX - ThingResources.THING_WIDTH_MEDIO
        thing.drawY = drawY - ThingResources.THING_HEIGHT_MEDIO
        thing.type = efectoFijoType
        thing.status = ThingResources.THING_STATUS_EFECTO_FIJO

        // Localizing external variables
        val things_animated = this.things_animated

        // Aadimos thing a los animated independentmente
        var thingAdded = false
        for (i in 0 until MAX_THINGS_ANIMATED) {
            if (things_animated!![i] == null) {
                things_animated[i] = thing
                thingAdded = true
                break
            }
        }
        // Si no encontramos posicin libre, lo metemos en una posicin aleatoria
        if (!thingAdded) {
            things_animated[random.nextInt(MAX_THINGS_ANIMATED - 1)] = thing
        }
    }

    fun animateThingsIndependents(canvas: Canvas?) {
        // Animating los things independents
        val things_animated = this.things_animated
        if (things_animated == null || things_animated.size == 0) {
            return
        }

        //Localizing extenal variables
        val MAX_THINGS_ANIMATED = Board.MAX_THINGS_ANIMATED
        val THING_STATUS_REMOVED = ThingResources.THING_STATUS_REMOVED
        val THING_STATUS_EFECTO_FIJO = ThingResources.THING_STATUS_EFECTO_FIJO

        for (i in 0 until MAX_THINGS_ANIMATED) {
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
            things_animated[i] = null

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

    internal fun animateThingEfectoFijo(canvas: Canvas?, i: Int, thing: Thing) {
        thing.animationIndex++
        // Sin loop, lo quitamos
        if (thing.animationIndex >= EfectoResources.EFECTO_FIJO_ANIMATION_SEQUENCE.size) {
            things_animated[i] = null
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
            /* save memory
		} else if ( type == EfectoResources.EFECTO_CPU_TOUCH_OBJECT_TYPE ) {
			bitmap = EfectoResources.EFECTO_CPU_TOUCH_GRAPHICS_BITMAP[ thing.graphicIndex ];
		*/
        } else if (type == EfectoResources.EFECTO_PLAYER_MOVE_LEFT_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP[thing.graphicIndex]

        } else if (type == EfectoResources.EFECTO_PLAYER_MOVE_RIGHT_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP[thing.graphicIndex]

        } else if (type == EfectoResources.EFECTO_TOUCH_MOVE_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP[thing.graphicIndex]
        }

        // Si el thing tiene asociado una bubble seguimos su posicion
        if (thing.bubble != null) {
            thing.drawX = thing.bubble!!.drawX
            thing.drawY = thing.bubble!!.drawY
        }

        canvas!!.drawBitmap(bitmap!!, thing.drawX.toFloat(), thing.drawY.toFloat(), null)
    }

    fun moveThingBetweenBubbles(thing: Thing?): Boolean {
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

        return if (finX && finY) {
            true
        } else {
            false
        }
    }

    fun calculateNextMovement(character: Character): Array<IATouchEvent>? {
        // Board no stable
        if (!boardStable) {
            return null
        }

        // Consideramos un retardo mnimo a partir de que el board se estabilice
        val retardo = System.currentTimeMillis() - boardStableTime
        if (retardo < 1000) {
            return null
        }

        //Log.d(TAG, "calculateNextMovement: retardo is " + retardo + " ms");

        // Localizing external variables
        val cells = this.cells
        val BUBBLE_WIDTH = BubbleResources.BUBBLE_WIDTH
        val BUBBLE_HEIGHT = BubbleResources.BUBBLE_HEIGHT
        val MAX_WIDTH = Board.MAX_WIDTH
        val MAX_HEIGHT = Board.MAX_HEIGHT
        val random = Board.random

        // Contador de seguridad para evitar bucles infinitos
        var maxIterations = MAX_WIDTH * MAX_HEIGHT
        while (maxIterations > 0) {
            // Updatemos numero iteraciones;
            maxIterations--

            // Calculatemos movimiento movimiento aleatorio
            val cell = cells[random.nextInt(MAX_WIDTH - 1)][random.nextInt(MAX_HEIGHT - 1)]
            if (cell == null || cell.bubble == null) {
                // Movimiento invalido
                continue
            }

            // Caso la cell tiene un thing
            if (cell.thing != null) {
                // Bubble con thing
                continue
            }

            val events = arrayOfNulls<IATouchEvent>(2)

            events[0] = IATouchEvent(MotionEvent.ACTION_DOWN, cell.bubble!!.drawX + BUBBLE_WIDTH / 2, cell.bubble!!.drawY + BUBBLE_HEIGHT / 2)
            events[1] = IATouchEvent(MotionEvent.ACTION_UP, cell.bubble!!.drawX + BUBBLE_WIDTH / 2, cell.bubble!!.drawY + BUBBLE_HEIGHT / 2)

            return events
        }

        return null
    }

    fun isFinPartida(p1: Character, p2: Character): Boolean {
        val like = things_explodes[ThingResources.CARAMELO_OBJECT_TYPE] + things_explodes[ThingResources.PIRULETA_OBJECT_TYPE]
        val dislike = things_explodes[ThingResources.RASPA_OBJECT_TYPE] + things_explodes[ThingResources.PEINE_OBJECT_TYPE]

        //Log.d(TAG,"Like = " + like + ", Dislike = " + dislike);

        // De momento no se gana nunca
        /*if ((like - dislike) > 20 ) {
			p1.setWinner(true);
			return true;
		} else*/
        if (like - dislike < -20) {
            p2.isWinner = true
            return true
        } else {
            return false
        }

    }

    fun destroy() {
        BubbleResources.destroy()
        EfectoResources.destroy()
        ExplosionResources.destroy()
        ThingResources.destroy()
    }

    companion object {

        internal val TAG = "ColiPop"

        internal val DEFAULT_SURFACE_WIDTH = 800
        internal val DEFAULT_SURFACE_HEIGHT = 480

        // Tamaos de boards
        internal var DEFAULT_BOARD_WIDTH = 320
        internal var DEFAULT_BOARD_HEIGHT = 320

        internal var BOARD_WIDTH = DEFAULT_BOARD_WIDTH
        internal var BOARD_HEIGHT = DEFAULT_BOARD_HEIGHT

        // Offsets del board
        internal var DEFAULT_OFFSET_X = 111
        internal var DEFAULT_OFFSET_Y = 20

        internal var OFFSET_X = DEFAULT_OFFSET_X
        internal var OFFSET_Y = DEFAULT_OFFSET_Y

        // OFFSET DE COUNTERS
        internal var DEFAULT_OFFSET_X_LIKE = 650
        internal var DEFAULT_OFFSET_Y_LIKE = 75
        internal var DEFAULT_OFFSET_X_DISLIKE = 650
        internal var DEFAULT_OFFSET_Y_DISLIKE = 250

        internal var OFFSET_X_LIKE = DEFAULT_OFFSET_X_LIKE
        internal var OFFSET_Y_LIKE = DEFAULT_OFFSET_Y_LIKE
        internal var OFFSET_X_DISLIKE = DEFAULT_OFFSET_X_DISLIKE
        internal var OFFSET_Y_DISLIKE = DEFAULT_OFFSET_Y_DISLIKE

        internal var COUNTER_Y_SPACER = java.lang.Double.valueOf(1.2 * ThingResources.THING_HEIGHT)!!.toInt()

        internal var OFFSET_X_LIKE_COUNTER = OFFSET_X_LIKE + ThingResources.THING_WIDTH
        internal var OFFSET_Y_LIKE_COUNTER = OFFSET_Y_LIKE + java.lang.Double.valueOf((ThingResources.THING_HEIGHT / 2).toDouble())!!.toInt()
        internal var OFFSET_X_DISLIKE_COUNTER = OFFSET_X_DISLIKE + ThingResources.THING_WIDTH
        internal var OFFSET_Y_DISLIKE_COUNTER = OFFSET_Y_DISLIKE + java.lang.Double.valueOf((ThingResources.THING_HEIGHT / 2).toDouble())!!.toInt()

        // Board num cells
        internal var MAX_WIDTH = 8
        internal var MAX_HEIGHT = 8

        internal var random = Random()

        // Numero maximo de things animated independents
        internal var MAX_THINGS_ANIMATED = 32

        internal var DEFAULT_TIME_BETWEEN_BUBBLES: Long = 1000

        fun resizeBoard(surfaceWidth: Int, surfaceHeight: Int) {
            //Log.d(TAG, "Surface resize: width = " + surfaceWidth + ", height = " + surfaceHeight);

            // Nos quedamos con la proporcion mas pequea ( El board siempre ser cuadrado )
            var refactorIndex = 1f
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
internal class CellPosition(i: Int, j: Int) {
    var i = 0
    var j = 0

    init {
        this.i = i
        this.j = j
    }
}