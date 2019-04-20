package com.mya.colipop

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.mya.colipop.board.Board
import com.mya.colipop.board.EfectoResources
import com.mya.colipop.character.Character
import com.mya.colipop.character.Colita
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Manages colipop thread logic.
 */
class ColiPopThread : Thread {

    private val TAG = "ColiPop"

    // Queue for GameEvents
    private var eventQueue = ConcurrentLinkedQueue<GameEvent>()
    // Context for processKey to maintain gameState across frames *
    private var keyContext: Any? = null
    // Message handler used by thread to interact with TextView
    private lateinit var handler: Handler
    // The surface manager object we interact with
    private lateinit var surfaceHolder: SurfaceHolder
    // The application context, used to e.g. fetch Drawables.
    private lateinit var context: Context
    // Indicate whether the surface has been created & is ready to draw
    private var run = false

    /*
     * Application main objects
     */

    // updates the screen clock. Also used for tempo timing.
    private var timerTask: TimerTask? = null
    private lateinit var timer: Timer
    // Graphics resources
    private lateinit var res: Resources
    // Players
    private lateinit var character1: Character
    // Board game
    private lateinit var board: Board
    // Temp vars to store the last cell touch MotionEvents
    private var eventActionDown: TouchGameEvent? = null
    private var eventActionMoveAnterior: TouchGameEvent? = null

    /*
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
    private var taskIntervalInMillis: Long = 1000
    // Used Limit frame Rate
    // 50 ms - 20 frames second
    // 100 ms - 10 frames second
    private var frameIntervalInMillis = 40

    /**
     * The constructor without parameters
     */
    constructor()

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

        ColiPopResources.initializeGraphics(resources)

        this.character1 = Colita(resources, Character.POSITION_LEFT)

        this.board = Board(resources, this.character1)

        setInitialGameState()

        //Log.d(TAG, "ColipopThread initiated");
    }

    private fun setInitialGameState() {
        this.timerCount = 0
        this.timer = Timer()
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
        this.board.addInitialBubble()

        // Update Character talking
        updateCharactersTalking()

        //long drawTime = System.currentTimeMillis() - initTime;
        //Log.d(TAG, "  Update time is " + drawTime + " ms");
    }

    private fun updateCharactersTalking() {
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
    }

    private fun doDrawRunning(canvas: Canvas) {
        //long initTime = System.currentTimeMillis();

        canvas.drawBitmap(ColiPopResources.backgroundImage!!, 0f, 0f, null)

        canvas.drawBitmap(ColiPopResources.boardImage!!, 0f, 0f, null)

        this.board.doBoardAnimation(canvas)

        this.character1.doCharacterAnimation(canvas)

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

    override fun run() {
        // while running do stuff in this loop...
        while (run) {
            val frameInitTime = System.currentTimeMillis()

            initialize()

            var canvas: Canvas? = null
            try {
                canvas = surfaceHolder.lockCanvas(null)
                synchronized(surfaceHolder) {
                    if (canvas != null) {
                        doDraw(canvas)
                    }
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent gameState
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }

            // Sleep logic
            doSleep(frameInitTime)
        }
    }

    private fun initialize() {
        if (gameState == STATE_RUNNING) {
            doTimerTasks()

        } else if (gameState == STATE_PLAY && !initialized) {
            setInitialGameState()

        } else if (gameState == STATE_GAME_END) {
            this.initialized = false
            this.timerTask = null

        } else if (gameState == STATE_PAUSE) {
            // Do nothing
        }
    }

    private fun doTimerTasks() {
        if (this.timerTask == null) {
            this.timerTask = object : TimerTask() {
                override fun run() {
                    doTimeCount()
                }
            }
            this.timer.schedule(this.timerTask, this.taskIntervalInMillis)
        }
    }

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

    private fun updateTouch(event: TouchGameEvent) {
        // Ignoramos los toques de pantalla de los jugadores si el juego ha terminado
        if (this.gameState == STATE_GAME_END) {
            return
        }

        //long initTime = System.currentTimeMillis();
        val board = this.board
        if (event.motionEvent == MotionEvent.ACTION_DOWN) {
            //Log.d(TAG, "Motion Down en position: x=" + event.x + ", y=" +  event.y );
            this.eventActionDown = event
            this.eventActionMoveAnterior = event

        } else if (event.motionEvent == MotionEvent.ACTION_MOVE) {
            //Log.d(TAG, "Motion Move en position: x=" + event.x + ", y=" +  event.y );
            board.addEfectoTouch(event.x, event.y, EfectoResources.EFECTO_TOUCH_MOVE_OBJECT_TYPE)
            val cellOrigin = board.getCellInCoordinates(this.eventActionMoveAnterior!!.x, this.eventActionMoveAnterior!!.y)
            val cellDestiny = board.getCellInCoordinates(event.x, event.y)
            ColiPopActions.updateTouchBoard(cellOrigin, cellDestiny, board, true, false)
            ColiPopActions.tiltOtherCells(board, cellDestiny)
            if (cellOrigin == null || cellDestiny == null || cellOrigin.posX != cellDestiny.posX || cellOrigin.posY != cellDestiny.posY) {
                this.eventActionMoveAnterior = event
            }

        } else if (event.motionEvent == MotionEvent.ACTION_UP) {
            //Log.d(TAG, "Motion Up en position: x=" + event.x + ", y=" +  event.y );
            val cellOrigin = board.getCellInCoordinates(this.eventActionDown!!.x, this.eventActionDown!!.y)
            val cellDestiny = board.getCellInCoordinates(event.x, event.y)
            ColiPopActions.updateTouchBoard(cellOrigin, cellDestiny, board, false, true)
            ColiPopActions.tiltOtherCells(board, cellDestiny)
        }

        //long updateTime = System.currentTimeMillis() - initTime;
        //Log.d(TAG, "UpdateTouch time is " + updateTime + " ms");
    }

    /**
     * Used to signal the thread whether it should be running or not.
     * Passing true allows the thread to run; passing false will shut it
     * down if it's already running. Calling start() after this was most
     * recently called with false will result in an immediate shutdown.
     *
     * @param run true to run, false to shut down
     */
    fun setRunning(run: Boolean) {
        this.run = run
        if (!this.run) {
            this.timerTask?.cancel()
        }
    }

    /**
     * Returns the current int value of game gameState as defined by gameState
     * tracking constants
     *
     * @return
     */
    fun getGameState(): Int {
        synchronized(this.surfaceHolder) {
            return this.gameState
        }
    }

    /**
     * Sets the game mode. That is, whether we are running, paused, in the
     * failure gameState, in the victory gameState, etc.
     *
     * @param mode one of the STATE_* constants
     */
    fun setGameState(mode: Int) {
        synchronized(this.surfaceHolder) {
            // change gameState if needed
            if (this.gameState != mode) {
                this.previousState = this.gameState
                this.gameState = mode
            }

            if (mode == STATE_PLAY) {
                // Nothing to do ?

            } else if (mode == STATE_RUNNING) {
                // When we enter the running gameState we should clear any old
                // events in the queue
                this.eventQueue.clear()
                // And reset the key gameState so we don't think a button is pressed when it isn't
                this.keyContext = null
                // Game Initialization
                if (this.previousState == STATE_PLAY) {
                    this.board.initBoard()
                }
            }
        }
    }

    /**
     * Add Touch event to the GameEvent queue
     */
    fun doTouchMotion(motionEvent: Int, x: Int, y: Int): Boolean {
        this.eventQueue.add(TouchGameEvent(motionEvent, x, y))
        return true
    }

    /**
     *  Callback to invoked when the surface dimensions change.
     *
     *  @param width
     *  @param height
     */
    fun setSurfaceSize(width: Int, height: Int) {
        // synchronized to make sure these all change atomically
        synchronized(this.surfaceHolder) {
            //Log.i(TAG, "setting surface: width=" + width + ", height=" + height);

            // Reescalado de fondos
            ColiPopResources.resizeGraphics(width, height)

            // En principio solo hay una instacia de board
            Board.resizeBoard(width, height)

            // Reescalado de characters
            this.character1.resizeGraphics(width, height)
        }
    }

    /**
     * Pauses the physics update & animation.
     */
    fun pause() {
        synchronized(this.surfaceHolder) {
            if (this.gameState == STATE_RUNNING) {
                this.setGameState(STATE_PAUSE)
                this.timerTask?.cancel()
                this.timerTask = null
            }
        }
    }

    /**
     * Continues the physics update & animation.
     */
    fun restore() {
        synchronized(this.surfaceHolder) {
            if (this.previousState == STATE_RUNNING) {
                this.setGameState(this.previousState)
                this.doTimerTasks()
            }
        }
    }

    private fun doTimeCount() {
        //Log.d(TAG,"Current time is " + timerCount);

        this.timerCount = this.timerCount + 1
        try {
            val minutes = java.lang.Double.valueOf((this.timerCount / 60).toDouble()).toInt()
            val seconds = this.timerCount - java.lang.Double.valueOf((minutes * 60).toDouble()).toInt()
            if (seconds > 9) {
                this.timerValue = "$minutes:$seconds"
            } else {
                this.timerValue = "$minutes:0$seconds"
            }
        } catch (e1: Exception) {
            //Log.e(TAG, "doCountDown threw " + e1.toString());
        }

        val msg = this.handler.obtainMessage()

        val bundle = Bundle()
        bundle.putString("text", this.timerValue)

        // Game over condition
        if (this.board.isBoardFullThings) {
            // Enviamos mensaje de juego acabado y winner
            bundle.putString("STATE_GAME_END", "" + STATE_GAME_END)
            bundle.putString("GAME_OVER", "" + true)

            this.gameState = STATE_GAME_END

        } else {
            this.timerTask = object : TimerTask() {
                override fun run() {
                    doTimeCount()
                }
            }
            this.timer.schedule(this.timerTask, this.taskIntervalInMillis)
        }

        // this is how we send data back up to the main ColiPopView thread.
        msg.data = bundle
        this.handler.sendMessage(msg)
    }

    override fun destroy() {
        pause()
        ColiPopResources.destroy()
        this.board.destroy()
        this.character1.destroy()
    }

    companion object {
        // State-tracking constants.
        const val STATE_START = -1
        const val STATE_PLAY = 0
        const val STATE_GAME_END = 1
        const val STATE_PAUSE = 2
        const val STATE_RUNNING = 3

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
            newThread.eventActionDown = thread.eventActionDown
            newThread.eventActionMoveAnterior = thread.eventActionMoveAnterior
            newThread.numDialogoTutorial = thread.numDialogoTutorial

            return newThread
        }
    }
}
