package com.mya.games.colipop

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.SurfaceHolder

import com.mya.games.colipop.character.Colita
import com.mya.games.colipop.character.Character
import com.mya.games.colipop.board.BubbleResources
import com.mya.games.colipop.board.Cell
import com.mya.games.colipop.board.EfectoResources
import com.mya.games.colipop.board.Board

import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.ConcurrentLinkedQueue

class ColiPopThread : Thread {

    internal val TAG = "ColiPop"

    /**
     * Queue for GameEvents
     */
    private var eventQueue = ConcurrentLinkedQueue<GameEvent>()

    /**
     * Context for processKey to maintain gameState across frames *
     */
    private var keyContext: Any? = null

    /**
     * Message handler used by thread to interact with TextView
     */
    private lateinit var handler: Handler

    /**
     * Handle to the surface manager object we interact with
     */
    private lateinit var surfaceHolder: SurfaceHolder

    /**
     * Handle to the application context, used to e.g. fetch Drawables.
     */
    private lateinit var context: Context

    /**
     * Indicate whether the surface has been created & is ready to draw
     */
    private var run = false

    /**
     * Application main objects
     */

    // updates the screen clock. Also used for tempo timing.
    private lateinit var timer: Timer

    private var timerTask: TimerTask? = null

    // Graphics resources
    private lateinit var res: Resources

    // Players
    private lateinit var character1: Character
//    private lateinit var character2: Character

    // Board game
    private lateinit var board: Board

    // Temp vars to store the last cell touch MotionEvents
    private lateinit var eventActionDown: TouchGameEvent
    private lateinit var eventActionMoveAnterior: TouchGameEvent

    /**
     * Application control flags
     */

    private var initialized = false
    private var numDialogoTutorial = 0

    // the timer display in seconds
    private var timerCount: Int = 0

    // start, play, running, lose are the states we use
    private var gameState: Int = 0
    private var previousState: Int = 0

    // Play Mode; 1 player, versus, etc
    private var playMode: Int = 0

    // string value for timer display
    private var timerValue = "0:00"

    // Indica si la musica est on o off
    private var musicOn = false
    private var soundOn = false

    private var touchFireTime: Long = 0

    // one second - used to update timer
    private var taskIntervalInMillis = 1000

    // Used Limit frame Rate
    // 50 ms - 20 frames second
    // 100 ms - 10 frames second
    private var frameIntervalInMillis = 40

    // Flag de control de turno
    private var turnoCPU: Boolean = false

    private var isGameEnded: Boolean = false

    /**
     * The constructor without parameters
     */
    constructor() {}

    /**
     * This is the constructor
     *
     * @param surfaceHolder The surface
     * @param context The context
     * @param handler The handler
     */
    constructor(surfaceHolder: SurfaceHolder, context: Context, handler: Handler) {
        this.surfaceHolder = surfaceHolder
        this.handler = handler
        this.context = context

        val resources = context.resources
        this.res = resources

        // always set gameState to start, ensure we come in from front door if
        // app gets tucked into background
        this.gameState = STATE_START
        this.playMode = PLAY_MODE_1PLAYER

        ColiPopResources.initializeGraphics(resources)

        this.character1 = Colita(resources, Character.POSICION_LEFT)
        //this.character2 = new Pistacho( resources, Character.POSICION_RIGHT );

        this.board = Board(resources, this.character1)

        setInitialGameState()

        //Log.d(TAG, "ColipopThread initiated");
    }

    private fun setInitialGameState() {
        this.timerCount = 0
        this.timer = Timer()
        this.turnoCPU = false
        this.initialized = true
    }

    private fun doDraw(canvas: Canvas) {
        //long drawInitTime = System.currentTimeMillis();

        val state = this.gameState
        if (state == STATE_RUNNING || state == STATE_GAME_END) {
            doUpdate()
            doDrawRunning(canvas)

        } else if (state == STATE_START) {
            doDrawReady(canvas)

        } else if (state == STATE_PLAY) {
            doDrawPlay(canvas)

        } else if (state == STATE_PAUSE) {
            // Do nothing
        }

        //long drawTime = System.currentTimeMillis() - drawInitTime;
        //Log.d(TAG, "Draw time is " + drawTime + " ms");
    }

    private fun doUpdate() {
        //long initTime = System.currentTimeMillis();

        // Process any input and apply it to the game gameState
        updateGameState()

        //Log.d(TAG, "    updateGameState is " + ( System.currentTimeMillis() - initTime ) + " ms");

        // Create new Bubbles
        doBubblesCreation()

        // Update Character talking
        updateCharactersTalking()

        //long drawTime = System.currentTimeMillis() - initTime;
        //Log.d(TAG, "  Update time is " + drawTime + " ms");
    }

    private fun updateCharactersTalking() {
        if (playMode == PLAY_MODE_1PLAYER) {
            // Tutorial
            if (timerCount < 60 && character1.status != Character.STATUS_TALKING) {
                if (timerCount < 10) {
                    // Nothing
                } else if (timerCount < 20 && numDialogoTutorial < 1) {
                    character1.doTalking(R.string.help_explodeEmptyBubbles)
                    numDialogoTutorial++
                } else if (timerCount < 40 && numDialogoTutorial < 2) {
                    character1.doTalking(R.string.help_moveAnyBubble)
                    numDialogoTutorial++
                } else if (timerCount < 60 && numDialogoTutorial < 3) {
                    character1.doTalking(R.string.help_matchFilledBubbles)
                    numDialogoTutorial++
                }
            } else {
                if (timerCount % 30 == 0 && character1.status != Character.STATUS_TALKING) {
                    character1.doNormalTalking()
                }
            }

        } else if (playMode == PLAY_MODE_CPU_VERSUS) {
            // TODO

        } else {
            // Do nothing
        }
    }

    /**
     * Draws current gameState of the game Canvas.
     */
    private fun doDrawRunning(canvas: Canvas) {

        //long initTime = System.currentTimeMillis();

        canvas.drawBitmap(ColiPopResources.backgroundImage!!, 0f, 0f, null)

        canvas.drawBitmap(ColiPopResources.boardImage!!, 0f, 0f, null)

        board.doBoardAnimation(canvas)


        if (playMode == PLAY_MODE_1PLAYER) {

            character1.doCharacterAnimation(canvas)

        } else if (playMode == PLAY_MODE_CPU_VERSUS) {

            character1.doCharacterAnimation(canvas)

            //character2.doCharacterAnimation(canvas);

        } else {
            // Do nothing
        }

/*
        if (turnoCPU) {
            //canvas.drawText("CPU Turn", 405, 30, new Paint());
            val iaEvents = board.calculateNextMovement(character2)
            if (iaEvents != null && iaEvents.size == 2) {
                eventQueue.add(TouchGameEvent(TouchGameEvent.PLAYER_CPU.toInt(), iaEvents[0]!!.action, iaEvents[0]!!.x, iaEvents[0]!!.y))
                eventQueue.add(TouchGameEvent(TouchGameEvent.PLAYER_CPU.toInt(), iaEvents[1]!!.action, iaEvents[1]!!.x, iaEvents[1]!!.y))
            }
        } else {
            //canvas.drawText("Player Turn", 20, 30, new Paint());
        }
*/

        //long drawTime = System.currentTimeMillis() - initTime;
        //Log.d(TAG, "  DrawRunning time is " + drawTime + " ms");

    }

    private fun doDrawReady(canvas: Canvas) {
        if (ColiPopResources.titleBG != null) {
            canvas.drawBitmap(ColiPopResources.titleBG!!, 0f, 0f, null)
        }
    }

    private fun doDrawPlay(canvas: Canvas) {
        if (ColiPopResources.backgroundImage != null) {
            canvas.drawBitmap(ColiPopResources.backgroundImage!!, 0f, 0f, null)
        }
    }

    /**
     * Slepping logic to save battery
     *
     * @param frameInitTime
     */
    private fun doSleep(frameInitTime: Long) {

        val frameTime = System.currentTimeMillis() - frameInitTime
        //Log.d(TAG, "Frame rendered in " + frameTime + " ms");

        if (frameTime < frameIntervalInMillis) {
            try {
                val sleepTime = frameIntervalInMillis - frameTime
                //Log.d(TAG, "Sleeping " + sleepTime + " ms");
                Thread.sleep(sleepTime)
            } catch (e: InterruptedException) {
                //Log.e(TAG, "Exception sleeping frame: " + e.getMessage() );
            }

        }
    }

    /**
     * the heart of the worker bee
     */
    override fun run() {
        // while running do stuff in this loop...bzzz!
        while (run) {

            val frameInitTime = System.currentTimeMillis()

            initialize()

            var c: Canvas? = null
            try {
                c = surfaceHolder.lockCanvas(null)
                //synchronized (surfaceHolder) {
                if (c != null) {
                    doDraw(c)
                }
                //}
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent gameState
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c)
                }
            }// end finally block

            // Sleep logic
            doSleep(frameInitTime)

        }// end while mrun block
    }

    private fun initialize() {

        if (gameState == STATE_RUNNING) {

            // kick off the timer task for counter update if not already
            // initialized
            if (timerTask == null) {
                timerTask = object : TimerTask() {
                    override fun run() {
                        doTimeCount()
                    }
                }

                timer.schedule(timerTask, taskIntervalInMillis.toLong())

            }// end of TimerTask init block

        } else if (gameState == STATE_PLAY && !initialized) {

            setInitialGameState()

        } else if (gameState == STATE_GAME_END) {

            initialized = false

        } else if (gameState == STATE_PAUSE) {

            // Do nothing

        }
    }

    /**
     * This method handles updating the model of the game gameState. No
     * rendering is done here only processing of inputs and update of gameState.
     * This includes positons of all game objects (bubbles, player,
     * explosions), their gameState (animation frame, hit), creation of new
     * objects, etc.
     */
    private fun updateGameState() {
        // Process any game events and apply them
        while (true) {
            val event = eventQueue.poll() ?: break

            // Log.d(TAG,"UpdateGameState: event.type=" + event.type );

            if (event.type == GameEvent.TOUCH_EVENT) {
                updateTouch(event as TouchGameEvent)
            }
        }
    }

    /**
     * This method updates the laser status based on user input and shot
     * duration
     */
    private fun updateTouch(event: TouchGameEvent) {
        // Ignoramos los toques de pantalla de los jugadores durante el turno de la CPU
        if (turnoCPU && event.player != TouchGameEvent.PLAYER_CPU.toInt()) {
            return
        }

        // Ignoramos los toques de pantalla de los jugadores si el juego ha terminado
        if (gameState == STATE_GAME_END) {
            return
        }

        //long initTime = System.currentTimeMillis();

        if (event.motionEvent == MotionEvent.ACTION_DOWN) {
            //Log.d(TAG, "Motion Down en position: x=" + event.x + ", y=" +  event.y );
            eventActionDown = event
            eventActionMoveAnterior = event

        } else if (event.motionEvent == MotionEvent.ACTION_MOVE) {
            //Log.d(TAG, "Motion Move en position: x=" + event.x + ", y=" +  event.y );
            board.addEfectoTouch(event.x, event.y, EfectoResources.EFECTO_TOUCH_MOVE_OBJECT_TYPE)
            val cellOrigin = board.getCellInCoordinates(eventActionMoveAnterior.x, eventActionMoveAnterior.y)
            val cellDestiny = board.getCellInCoordinates(event.x, event.y)
            updateTouchBoard(cellOrigin, cellDestiny, board, event.player, true, false)
            if (cellOrigin == null || cellDestiny == null || cellOrigin.posX != cellDestiny.posX || cellOrigin.posY != cellDestiny.posY) {
                eventActionMoveAnterior = event
            }

        } else if (event.motionEvent == MotionEvent.ACTION_UP) {
            //Log.d(TAG, "Motion Up en position: x=" + event.x + ", y=" +  event.y );
            val cellOrigin = board.getCellInCoordinates(eventActionDown.x, eventActionDown.y)
            val cellDestiny = board.getCellInCoordinates(event.x, event.y)
            updateTouchBoard(cellOrigin, cellDestiny, board, event.player, false, true)
        }

        //long updateTime = System.currentTimeMillis() - initTime;
        //Log.d(TAG, "UpdateTouch time is " + updateTime + " ms");
    }

    private fun updateTouchBoard(cellOrigin: Cell?, cellDestiny: Cell?, board: Board, player: Int, ignoreRemove: Boolean, ignoreMove: Boolean) {
        var cellOrigin = cellOrigin
        var cellDestiny = cellDestiny

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
                if (!isOtherCell || cellOrigin == null || cellDestiny == null) {
                    //Log.d(TAG, "No se ha encontrado movimiento valido. No hacemos nada.");
                    return
                }
            }

            // Caso remover bubble
            if (cellOrigin.posX == cellDestiny.posX && cellOrigin.posY == cellDestiny.posY) {

                // Solo se pueden remover bubbles sin things
                if (cellDestiny.thing == null) {

                    //Log.d(TAG, "Removendo bubble en Cell: posX=" + cellDestiny.posX + ", posY=" + cellDestiny.posY );

                    if (turnoCPU) {
                        board.addEfectoBubble(cellDestiny.bubble, EfectoResources.EFECTO_CPU_TOUCH_OBJECT_TYPE)
                    } else {
                        board.addEfectoBubble(cellDestiny.bubble, EfectoResources.EFECTO_PLAYER_TOUCH_OBJECT_TYPE)
                    }

                    board.removeBubble(cellDestiny)

                    if (playMode == PLAY_MODE_CPU_VERSUS) {
                        if (player == TouchGameEvent.PLAYER_CPU.toInt()) {
                            turnoCPU = false
                        } else {
                            turnoCPU = true
                        }
                    }

                } else {

                    if (!ignoreRemove) {
                        //Log.d(TAG, "No se puede remover bubble en Cell: posX=" + cellDestiny.posX + ", posY=" + cellDestiny.posY );

                        // Si la bubble no se puede remover mostramos efecto bloqueo ( solo para jugadores )
                        if (player != TouchGameEvent.PLAYER_CPU.toInt()) {
                            board.addEfectoBubble(cellDestiny.bubble, EfectoResources.EFECTO_BLOQUEO_OBJECT_TYPE)
                        }
                    }
                }

                // TODO: Trigger explosion sound

            // Caso movimiento de bubbles between cells
            } else {

                if (!ignoreMove) {

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

                        if (turnoCPU) {
                            if (cellOrigin.posX < cellDestiny.posX) {
                                board.addEfectoBubble(cellOrigin.bubble, EfectoResources.EFECTO_CPU_MOVE_RIGHT_OBJECT_TYPE)
                            } else {
                                board.addEfectoBubble(cellOrigin.bubble, EfectoResources.EFECTO_CPU_MOVE_LEFT_OBJECT_TYPE)
                            }
                        } else {
                            if (cellOrigin.posX < cellDestiny.posX) {
                                board.addEfectoBubble(cellOrigin.bubble, EfectoResources.EFECTO_PLAYER_MOVE_RIGHT_OBJECT_TYPE)
                            } else {
                                board.addEfectoBubble(cellOrigin.bubble, EfectoResources.EFECTO_PLAYER_MOVE_LEFT_OBJECT_TYPE)
                            }
                        }

                        board.moveBubbleBetweenCells(cellOrigin, cellDestiny)

                        if (playMode == PLAY_MODE_CPU_VERSUS) {
                            if (player == TouchGameEvent.PLAYER_CPU.toInt()) {
                                turnoCPU = false
                            } else {
                                turnoCPU = true
                            }
                        }

                    }

                }

            }

            // Caso movimiento de thing between bubbles
            /* comentado: de momento no movemos things between bubbles, se move la bubble entera
			} else {

		        // Si la cell inicial no tiene thing ignoramos el movimiento
		        if ( cellOrigin.thing == null ) {
		        	Log.d(TAG, "Movimiento ignorado: cell origin sin thing");

		        } else if ( cellDestiny.thing != null ){
		        	Log.d(TAG, "Movimiento ignorado: cell destiny con thing");

		        } else {

		        	int deltaX = cellOrigin.posX - cellDestiny.posX;
		        	int deltaY = cellOrigin.posY - cellDestiny.posY;
		        	if ( deltaX > 1 || deltaX < -1 || deltaY > 1 || deltaY < -1 ) {
		        		Log.d(TAG, "Movimiento ignorado: fuera de rango");

		        	} else {

		        		// comentado
		            	//if ( turnoCPU ) {
		            	//	board.addEfectoFijoCell(cellDestiny, Board.EFECTO_CPU_TOUCH_OBJECT_TYPE );
		            	//} else {
		            	//	board.addEfectoFijoCell(cellDestiny, Board.EFECTO_PLAYER_TOUCH_OBJECT_TYPE );
		            	//}


		            	Log.d(TAG, "Moviendo bubble de Cell: posX=" + cellOrigin.posX + ", posY=" + cellOrigin.posY + " a Cell: posX=" + cellDestiny.posX + ", posY=" + cellDestiny.posY );

		            	board.moveThingBetweenBubbles(cellOrigin, cellDestiny);

		            	if ( playMode == PLAY_MODE_CPU_VERSUS ) {
		            		if ( event.player == TouchGameEvent.PLAYER_CPU ) {
		            			turnoCPU = false;
		            		} else {
		            			turnoCPU = true;
		            		}
		            	}

		        	}

		        }

			}
			*/

        } else {
            //TODO: Trigger touch sound ?
        }
    }

    private fun doBubblesCreation() {
        // Log.d(TAG, "bubble created");

        board.addInitialBubble()
    }

    /**
     * Used to signal the thread whether it should be running or not.
     * Passing true allows the thread to run; passing false will shut it
     * down if it's already running. Calling start() after this was most
     * recently called with false will result in an immediate shutdown.
     *
     * @param b true to run, false to shut down
     */
    fun setRunning(b: Boolean) {
        run = b
        if (run == false) {
            timerTask?.cancel()
        }
    }


    /**
     * returns the current int value of game gameState as defined by gameState
     * tracking constants
     *
     * @return
     */
    fun getGameState(): Int {
        synchronized(surfaceHolder) {
            return gameState
        }
    }


    /**
     * Sets the game mode. That is, whether we are running, paused, in the
     * failure gameState, in the victory gameState, etc.
     *
     * @param mode one of the STATE_* constants
     * @see .setGameState
     */
    fun setGameState(mode: Int) {
        synchronized(surfaceHolder) {
            setGameState(mode, null)
        }
    }

    /**
     * Sets gameState based on input, optionally also passing in a text message.
     *
     * @param state
     * @param message
     */
    fun setGameState(state: Int, message: CharSequence?) {
        synchronized(surfaceHolder) {
            // change gameState if needed
            if (this.gameState != state) {
                this.previousState = this.gameState
                this.gameState = state
            }

            if (state == STATE_PLAY) {
                // Nothing to do ?

            } else if (state == STATE_RUNNING) {
                // When we enter the running gameState we should clear any old
                // events in the queue
                this.eventQueue.clear()

                // And reset the key gameState so we don't think a button is pressed when it isn't
                this.keyContext = null

                // Game Initialization
                this.board.initBoard()
            }
        }
    }

    /**
     * Add key press input to the GameEvent queue
     */
    fun doKeyDown(keyCode: Int, msg: KeyEvent): Boolean {
        eventQueue.add(KeyGameEvent(keyCode, false, msg))
        return true
    }

    /**
     * Add key press input to the GameEvent queue
     */
    fun doKeyUp(keyCode: Int, msg: KeyEvent): Boolean {
        eventQueue.add(KeyGameEvent(keyCode, true, msg))
        return true
    }

    /**
     * Add Touch event to the GameEvent queue
     */
    fun doTouchMotion(motionEvent: Int, x: Int, y: Int): Boolean {
        eventQueue.add(TouchGameEvent(motionEvent, x, y))
        return true
    }

    /* Callback invoked when the surface dimensions change. */
    fun setSurfaceSize(width: Int, height: Int) {
        // synchronized to make sure these all change atomically
        synchronized(surfaceHolder) {
            //Log.i(TAG, "setting surface: width=" + width + ", height=" + height);

            // Reescalado de fondos
            ColiPopResources.resizeGraphics(width, height)

            // En principio solo hay una instacia de board
            Board.resizeBoard(width, height)

            // Reescalado de characters
            character1.resizeGraphics(width, height)
            /* save memory
            character2.resizeGraphics(width, height);
            */
        }
    }

    /**
     * Pauses the physics update & animation.
     */
    fun pause() {
        synchronized(surfaceHolder) {
            if (gameState == STATE_RUNNING)
                setGameState(STATE_PAUSE)
            timerTask?.cancel()
        }
    }

    /**
     * Does the work of updating timer
     */
    private fun doTimeCount() {
        //Log.d(TAG,"Current time is " + timerCount);

        timerCount = timerCount + 1
        try {
            val minutes = java.lang.Double.valueOf((timerCount / 60).toDouble()).toInt()
            val seconds = timerCount - java.lang.Double.valueOf((minutes * 60).toDouble()).toInt()
            if (seconds > 9) {
                timerValue = "$minutes:$seconds"
            } else {
                timerValue = "$minutes:0$seconds"
            }
        } catch (e1: Exception) {
            //Log.e(TAG, "doCountDown threw " + e1.toString());
        }

        val msg = handler.obtainMessage()

        val b = Bundle()
        b.putString("text", timerValue)

        // Condicion de fin de juego
        if (board.isBoardFullThings) {

            // Enviamos mensaje de juego acabado y winner
            b.putString("STATE_GAME_END", "" + STATE_GAME_END)
            b.putString("GAME_OVER", "" + true)

            timerTask = null

            gameState = STATE_GAME_END

            /*
		// Caso 2 players. Hay algun winner. Condicion de fin de juego
		} else if ( isGameEnded() ) {

        	// Enviamos mensaje de juego acabado y winner
            b.putString("STATE_GAME_END", "" + STATE_GAME_END);
            if ( character1.isWinner() ) {
            	b.putString("PLAYER_WIN", "" + true);
            }
            if ( character2.isWinner() ) {
            	b.putString("CPU_WIN", "" + true);
            }

            timerTask = null;

            gameState = STATE_GAME_END;
*/
        } else {
            timerTask = object : TimerTask() {
                override fun run() {
                    doTimeCount()
                }
            }
            timer.schedule(timerTask, taskIntervalInMillis.toLong())
        }

        //this is how we send data back up to the main ColiPopView thread.
        //if you look in constructor of ColiPopView you will see code for
        //Handling of messages. This is borrowed directly from lunar lander.
        //Thanks again!
        msg.data = b
        handler.sendMessage(msg)
    }

    override fun destroy() {
        pause()
        ColiPopResources.destroy()
        if (board != null) {
            board.destroy()
        }
        if (character1 != null) {
            character1.destroy()
        }
        /*
        if (character2 != null) {
            character2.destroy()
        }
        */
    }

    companion object {
        /**
         * State-tracking constants.
         */
        val STATE_START = -1
        val STATE_PLAY = 0
        val STATE_GAME_END = 1
        val STATE_PAUSE = 2
        val STATE_RUNNING = 3

        /**
         * Play-mode constants.
         */
        val PLAY_MODE_1PLAYER = 0
        val PLAY_MODE_CPU_VERSUS = 1

        /**
         * Creates a ColiPopThread clone
         *
         * @param thread The thread
         */
        fun newColiPopThread(thread: ColiPopThread): ColiPopThread {
            val newThread = ColiPopThread()

            newThread.surfaceHolder = thread.surfaceHolder
            newThread.handler = thread.handler
            newThread.context = thread.context
            newThread.res = thread.res

            newThread.initialized = thread.initialized
            newThread.eventQueue = thread.eventQueue
            newThread.keyContext = thread.keyContext
            newThread.timerCount = thread.timerCount
            newThread.timerValue = thread.timerValue
            newThread.gameState = thread.gameState
            newThread.previousState = thread.previousState
            newThread.playMode = thread.playMode
            newThread.musicOn = thread.musicOn
            newThread.soundOn = thread.soundOn
            newThread.touchFireTime = thread.touchFireTime
            newThread.run = thread.run
            newThread.timer = thread.timer
            newThread.timerTask = thread.timerTask
            newThread.taskIntervalInMillis = thread.taskIntervalInMillis
            newThread.frameIntervalInMillis = thread.frameIntervalInMillis
            newThread.board = thread.board
            newThread.character1 = thread.character1
            //newThread.character2 = thread.character2
            newThread.turnoCPU = thread.turnoCPU
            newThread.eventActionDown = thread.eventActionDown
            newThread.eventActionMoveAnterior = thread.eventActionMoveAnterior
            newThread.numDialogoTutorial = thread.numDialogoTutorial

            return newThread
        }
    }
}
