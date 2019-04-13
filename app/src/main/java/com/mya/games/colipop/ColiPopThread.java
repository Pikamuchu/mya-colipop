package com.mya.games.colipop;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.mya.games.colipop.personaje.Colita;
import com.mya.games.colipop.personaje.Personaje;
import com.mya.games.colipop.tablero.BurbujaResources;
import com.mya.games.colipop.tablero.Celda;
import com.mya.games.colipop.tablero.EfectoResources;
import com.mya.games.colipop.tablero.Tablero;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ColiPopThread extends Thread {

    //static final String TAG = "ColiPop";

    /**
     * State-tracking constants.
     */
    public static final int STATE_START = -1;
    public static final int STATE_PLAY = 0;
    public static final int STATE_GAME_END = 1;
    public static final int STATE_PAUSE = 2;
    public static final int STATE_RUNNING = 3;

    /**
     * Play-mode constants.
     */
    public static final int PLAY_MODE_1PLAYER = 0;
    public static final int PLAY_MODE_CPU_VERSUS = 1;

    /**
     * Flag de control de inicializacin
     **/
    public boolean initialized = false;
    // the timer display in seconds
    public int timerCount;
    // start, play, running, lose are the states we use
    public int state;
    public int previousState;
    // Play Mode; 1 player, versus, etc
    public int playMode;
    /**
     * Queue for GameEvents
     */
    protected ConcurrentLinkedQueue<GameEvent> eventQueue = new ConcurrentLinkedQueue<GameEvent>();
    /**
     * Context for processKey to maintain state across frames *
     */
    protected Object keyContext = null;
    // string value for timer display
    String timerValue = "0:00";
    // Indica si la musica est on o off
    boolean musicOn = false;
    boolean soundOn = false;

    long touchFireTime = 0;

    /**
     * Message handler used by thread to interact with TextView
     */
    Handler handler;

    /**
     * Handle to the surface manager object we interact with
     */
    SurfaceHolder surfaceHolder;

    /**
     * Handle to the application context, used to e.g. fetch Drawables.
     */
    Context context;

    /**
     * Indicate whether the surface has been created & is ready to draw
     */
    boolean run = false;

    // updates the screen clock. Also used for tempo timing.
    Timer timer = null;

    TimerTask timerTask = null;

    // one second - used to update timer
    int taskIntervalInMillis = 1000;

    // Used Limit frame Rate
    // 50 ms - 20 frames second
    // 100 ms - 10 frames second
    int frameIntervalInMillis = 33;

    // Recursos
    Resources res;

    // Jugadores
    Personaje personaje1;
    Personaje personaje2;

    int numDialogoTutorial = 0;

    // Tablero de juego
    Tablero tablero;

    // Flag de control de turno
    boolean turnoCPU;

    // Variable temporal para almacenar celda de ultimo touch MotionEvent Action Down
    TouchGameEvent eventActionDown;
    TouchGameEvent eventActionMoveAnterior;

    /**
     * This is the constructor
     *
     * @param surfaceHolder
     * @param context
     * @param handler
     */
    public ColiPopThread() {

    }

    /**
     * This is the constructor
     *
     * @param surfaceHolder
     * @param context
     * @param handler
     */
    public ColiPopThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {

        this.surfaceHolder = surfaceHolder;
        this.handler = handler;
        this.context = context;

        Resources resources = context.getResources();
        this.res = resources;

        // always set state to start, ensure we come in from front door if
        // app gets tucked into background
        this.state = STATE_START;
        this.playMode = PLAY_MODE_1PLAYER;

        ColiPopResources.initializeGraphics(resources);

        this.personaje1 = new Colita(resources, Personaje.POSICION_LEFT);
        //this.personaje2 = new Pistacho( resources, Personaje.POSICION_RIGHT );

        this.tablero = new Tablero(resources, this.personaje1, this.personaje2);

        setInitialGameState();

        //Log.d(TAG, "ColipopThread initiated");

    }

    /**
     * Copia el thread finalizado
     *
     * @param thread
     */
    public static ColiPopThread newColiPopThread(ColiPopThread thread) {

        ColiPopThread newThread = new ColiPopThread();

        newThread.surfaceHolder = thread.surfaceHolder;
        newThread.handler = thread.handler;
        newThread.context = thread.context;
        newThread.res = thread.res;

        newThread.initialized = thread.initialized;
        newThread.eventQueue = thread.eventQueue;
        newThread.keyContext = thread.keyContext;
        newThread.timerCount = thread.timerCount;
        newThread.timerValue = thread.timerValue;
        newThread.state = thread.previousState;
        newThread.previousState = thread.state;
        newThread.playMode = thread.playMode;
        newThread.musicOn = thread.musicOn;
        newThread.soundOn = thread.soundOn;
        newThread.touchFireTime = thread.touchFireTime;
        newThread.run = thread.run;
        newThread.timer = thread.timer;
        newThread.timerTask = thread.timerTask;
        newThread.taskIntervalInMillis = thread.taskIntervalInMillis;
        newThread.frameIntervalInMillis = thread.frameIntervalInMillis;
        newThread.tablero = thread.tablero;
        newThread.personaje1 = thread.personaje1;
        newThread.personaje2 = thread.personaje2;
        newThread.turnoCPU = thread.turnoCPU;
        newThread.eventActionDown = thread.eventActionDown;
        newThread.eventActionMoveAnterior = thread.eventActionMoveAnterior;
        newThread.numDialogoTutorial = thread.numDialogoTutorial;

        return newThread;

    }

    void setInitialGameState() {

        this.timerCount = 0;

        this.timer = new Timer();

        this.turnoCPU = false;

        // Flag de control de inicializacin
        this.initialized = true;

    }

    void doDraw(Canvas canvas) {

        //long drawInitTime = System.currentTimeMillis();
        int state = this.state;
        if (state == STATE_RUNNING || state == STATE_GAME_END) {

            doUpdate();

            doDrawRunning(canvas);

        } else if (state == STATE_START) {

            doDrawReady(canvas);

        } else if (state == STATE_PLAY) {

            doDrawPlay(canvas);

        } else if (state == STATE_PAUSE) {

            // Do nothing

        }
        // end state play block

        //long drawTime = System.currentTimeMillis() - drawInitTime;
        //Log.d(TAG, "Draw time is " + drawTime + " ms");

    }

    void doUpdate() {

        //long initTime = System.currentTimeMillis();

        // Process any input and apply it to the game state
        updateGameState();

        //Log.d(TAG, "    updateGameState is " + ( System.currentTimeMillis() - initTime ) + " ms");

        // Create new Burbujas
        doBurbujasCreation();

        // Update Personaje talking
        updatePersonajesTalking();

        //long drawTime = System.currentTimeMillis() - initTime;
        //Log.d(TAG, "  Update time is " + drawTime + " ms");

    }

    /**
     *
     */
    void updatePersonajesTalking() {

        if (playMode == PLAY_MODE_1PLAYER) {

            // Tutorial
            if (timerCount < 60 && personaje1.getStatus() != Personaje.STATUS_TALKING) {
                if (timerCount < 10) {
                    // Nothing
                } else if (timerCount < 20 && numDialogoTutorial < 1) {
                    personaje1.doTalking(R.string.help_explodeEmptyBubbles);
                    numDialogoTutorial++;
                } else if (timerCount < 40 && numDialogoTutorial < 2) {
                    personaje1.doTalking(R.string.help_moveAnyBubble);
                    numDialogoTutorial++;
                } else if (timerCount < 60 && numDialogoTutorial < 3) {
                    personaje1.doTalking(R.string.help_matchFilledBubbles);
                    numDialogoTutorial++;
                }
            } else {
                if (timerCount % 30 == 0 && personaje1.getStatus() != Personaje.STATUS_TALKING) {
                    personaje1.doNormalTalking();
                }
            }

        } else if (playMode == PLAY_MODE_CPU_VERSUS) {

            // TODO

        } else {
            // Do nothing
        }

    }


    /**
     * Draws current state of the game Canvas.
     */
    void doDrawRunning(Canvas canvas) {

        //long initTime = System.currentTimeMillis();

        canvas.drawBitmap(ColiPopResources.backgroundImage, 0, 0, null);

        canvas.drawBitmap(ColiPopResources.tableroImage, 0, 0, null);

        tablero.doTableroAnimation(canvas);


        if (playMode == PLAY_MODE_1PLAYER) {

            personaje1.doPersonajeAnimation(canvas);

        } else if (playMode == PLAY_MODE_CPU_VERSUS) {

            personaje1.doPersonajeAnimation(canvas);

            //personaje2.doPersonajeAnimation(canvas);

        } else {
            // Do nothing
        }

        if (turnoCPU) {
            //canvas.drawText("CPU Turn", 405, 30, new Paint());
            IATouchEvent[] iaEvents = tablero.calculaNextMovement(personaje2);
            if (iaEvents != null && iaEvents.length == 2) {
                eventQueue.add(new TouchGameEvent(TouchGameEvent.PLAYER_CPU, iaEvents[0].action, iaEvents[0].x, iaEvents[0].y));
                eventQueue.add(new TouchGameEvent(TouchGameEvent.PLAYER_CPU, iaEvents[1].action, iaEvents[1].x, iaEvents[1].y));
            }
        } else {
            //canvas.drawText("Player Turn", 20, 30, new Paint());
        }

        //long drawTime = System.currentTimeMillis() - initTime;
        //Log.d(TAG, "  DrawRunning time is " + drawTime + " ms");

    }

    void doDrawReady(Canvas canvas) {
        if (ColiPopResources.titleBG != null) {
            canvas.drawBitmap(ColiPopResources.titleBG, 0, 0, null);
        }
    }

    void doDrawPlay(Canvas canvas) {
        if (ColiPopResources.backgroundImage != null) {
            canvas.drawBitmap(ColiPopResources.backgroundImage, 0, 0, null);
        }
    }

    /**
     * Slepping logic to save battery
     *
     * @param frameInitTime
     */
    void doSleep(long frameInitTime) {

        long frameTime = System.currentTimeMillis() - frameInitTime;
        //Log.d(TAG, "Frame rendered in " + frameTime + " ms");

        if (frameTime < frameIntervalInMillis) {
            try {
                long sleepTime = frameIntervalInMillis - frameTime;
                //Log.d(TAG, "Sleeping " + sleepTime + " ms");
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                //Log.e(TAG, "Exception sleeping frame: " + e.getMessage() );
            }
        }
    }

    /**
     * the heart of the worker bee
     */
    public void run() {
        // while running do stuff in this loop...bzzz!
        while (run) {

            long frameInitTime = System.currentTimeMillis();

            initialize();

            Canvas c = null;
            try {
                c = surfaceHolder.lockCanvas(null);
                //synchronized (surfaceHolder) {
                doDraw(c);
                //}
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }// end finally block

            // Sleep logic
            doSleep(frameInitTime);

        }// end while mrun block
    }

    void initialize() {

        if (state == STATE_RUNNING) {

            // kick off the timer task for counter update if not already
            // initialized
            if (timerTask == null) {
                timerTask = new TimerTask() {
                    public void run() {
                        doTimeCount();
                    }
                };

                timer.schedule(timerTask, taskIntervalInMillis);

            }// end of TimerTask init block

        } else if (state == STATE_PLAY && !initialized) {

            setInitialGameState();

        } else if (state == STATE_GAME_END) {

            initialized = false;

        } else if (state == STATE_PAUSE) {

            // Do nothing

        }
    }

    /**
     * This method handles updating the model of the game state. No
     * rendering is done here only processing of inputs and update of state.
     * This includes positons of all game objects (burbujas, player,
     * explosions), their state (animation frame, hit), creation of new
     * objects, etc.
     */
    protected void updateGameState() {

        // Process any game events and apply them
        while (true) {
            GameEvent event = eventQueue.poll();
            if (event == null) {
                break;
            }

            // Log.d(TAG,"UpdateGameState: event.type=" + event.type );

            if (event.type == GameEvent.TOUCH_EVENT) {

                updateTouch((TouchGameEvent) event);

            }

        }

    }

    /**
     * This method updates the laser status based on user input and shot
     * duration
     */
    protected void updateTouch(TouchGameEvent event) {

        if (event == null) {
            return;
        }

        // Ignoramos los toques de pantalla de los jugadores durante el turno de la CPU
        if (turnoCPU && event.player != TouchGameEvent.PLAYER_CPU) {
            return;
        }

        // Ignoramos los toques de pantalla de los jugadores si el juego ha terminado
        if (state == STATE_GAME_END) {
            return;
        }

        //long initTime = System.currentTimeMillis();

        if (event.motionEvent == MotionEvent.ACTION_DOWN) {
            //Log.d(TAG, "Motion Down en position: x=" + event.x + ", y=" +  event.y );
            eventActionDown = event;
            eventActionMoveAnterior = event;

        } else if (event.motionEvent == MotionEvent.ACTION_MOVE) {
            //Log.d(TAG, "Motion Move en position: x=" + event.x + ", y=" +  event.y );
            tablero.addEfectoTouch(event.x, event.y, EfectoResources.EFECTO_TOUCH_MOVE_OBJECT_TYPE);
            Celda celdaOrigen = tablero.getCeldaInCoordinates(eventActionMoveAnterior.x, eventActionMoveAnterior.y);
            Celda celdaDestino = tablero.getCeldaInCoordinates(event.x, event.y);
            updateTouchTablero(celdaOrigen, celdaDestino, tablero, event.player, true, false);
            if (celdaOrigen == null || celdaDestino == null || celdaOrigen.posX != celdaDestino.posX || celdaOrigen.posY != celdaDestino.posY) {
                eventActionMoveAnterior = event;
            }

        } else if (event.motionEvent == MotionEvent.ACTION_UP) {
            //Log.d(TAG, "Motion Up en position: x=" + event.x + ", y=" +  event.y );
            Celda celdaOrigen = tablero.getCeldaInCoordinates(eventActionDown.x, eventActionDown.y);
            Celda celdaDestino = tablero.getCeldaInCoordinates(event.x, event.y);
            updateTouchTablero(celdaOrigen, celdaDestino, tablero, event.player, false, true);

        }

        //long updateTime = System.currentTimeMillis() - initTime;
        //Log.d(TAG, "UpdateTouch time is " + updateTime + " ms");

    }

    /**
     * @param event
     */
    private void updateTouchTablero(Celda celdaOrigen, Celda celdaDestino, Tablero tablero, int player, boolean ignoreElimina, boolean ignoreMueve) {

        if (celdaOrigen != null && celdaDestino != null) {

            // Caso la burbuja esta en movimiento y ya no esta donde se ha producido el evento
            if (celdaOrigen.burbuja == null) {
                //Log.d(TAG, "Celda sin burbuja: Buscamos posible burbuja en Movimiento.");
                boolean isOtraCelda = false;
                // Miramos celda de arriba
                Celda otraCelda = tablero.getCelda(celdaOrigen.posX, celdaOrigen.posY - 1);
                if (otraCelda != null && otraCelda.burbuja != null && otraCelda.burbuja.move == BurbujaResources.BURBUJA_MOVE_UP) {
                    //Log.d(TAG, "Celda con burbuja en movimiento encontrada arriba.");
                    isOtraCelda = true;
                    celdaOrigen = otraCelda;
                    celdaDestino = tablero.getCelda(celdaDestino.posX, celdaDestino.posY - 1);
                }
                // Miramos celda de la derecha
                otraCelda = tablero.getCelda(celdaOrigen.posX + 1, celdaOrigen.posY);
                if (!isOtraCelda && otraCelda != null && otraCelda.burbuja != null && otraCelda.burbuja.move == BurbujaResources.BURBUJA_MOVE_RIGHT) {
                    //Log.d(TAG, "Celda con burbuja en movimiento encontrada a la derecha.");
                    isOtraCelda = true;
                    celdaOrigen = otraCelda;
                    celdaDestino = tablero.getCelda(celdaDestino.posX + 1, celdaDestino.posY);
                }
                // Miramos celda de la izquierda
                otraCelda = tablero.getCelda(celdaOrigen.posX - 1, celdaOrigen.posY);
                if (!isOtraCelda && otraCelda != null && otraCelda.burbuja != null && otraCelda.burbuja.move == BurbujaResources.BURBUJA_MOVE_LEFT) {
                    //Log.d(TAG, "Celda con burbuja en movimiento encontrada a la izquierda.");
                    isOtraCelda = true;
                    celdaOrigen = otraCelda;
                    celdaDestino = tablero.getCelda(celdaDestino.posX - 1, celdaDestino.posY);
                }
                if (!isOtraCelda || celdaOrigen == null || celdaDestino == null) {
                    //Log.d(TAG, "No se ha encontrado movimiento valido. No hacemos nada.");
                    return;
                }
            }

            // Caso eliminar burbuja
            if (celdaOrigen.posX == celdaDestino.posX && celdaOrigen.posY == celdaDestino.posY) {

                // Solo se pueden eliminar burbujas sin objetos
                if (celdaDestino.objeto == null) {

                    //Log.d(TAG, "Eliminando burbuja en Celda: posX=" + celdaDestino.posX + ", posY=" + celdaDestino.posY );

                    if (turnoCPU) {
                        tablero.addEfectoBurbuja(celdaDestino.burbuja, EfectoResources.EFECTO_CPU_TOUCH_OBJECT_TYPE);
                    } else {
                        tablero.addEfectoBurbuja(celdaDestino.burbuja, EfectoResources.EFECTO_PLAYER_TOUCH_OBJECT_TYPE);
                    }

                    tablero.eliminaBurbuja(celdaDestino);

                    if (playMode == PLAY_MODE_CPU_VERSUS) {
                        if (player == TouchGameEvent.PLAYER_CPU) {
                            turnoCPU = false;
                        } else {
                            turnoCPU = true;
                        }
                    }

                } else {

                    if (!ignoreElimina) {
                        //Log.d(TAG, "No se puede eliminar burbuja en Celda: posX=" + celdaDestino.posX + ", posY=" + celdaDestino.posY );

                        // Si la burbuja no se puede eliminar mostramos efecto bloqueo ( solo para jugadores )
                        if (player != TouchGameEvent.PLAYER_CPU) {
                            tablero.addEfectoBurbuja(celdaDestino.burbuja, EfectoResources.EFECTO_BLOQUEO_OBJECT_TYPE);
                        }
                    }
                }

                // TODO: Trigger explosion sound

                // Caso movimiento de burbujas entre celdas
            } else {

                if (!ignoreMueve) {

                    int deltaX = celdaOrigen.posX - celdaDestino.posX;
                    int deltaY = celdaOrigen.posY - celdaDestino.posY;

                    // Solo se permite movimientos de 1 celda como maximo
                    int celda_i = celdaOrigen.posX;
                    int celda_j = celdaOrigen.posY;
                    if (deltaX > 0) {
                        celda_i -= 1;
                    }
                    if (deltaX < 0) {
                        celda_i += 1;
                    }
                    celdaDestino = tablero.getCelda(celda_i, celda_j);

                    // Control de celda valida
                    if (celdaDestino == null || celdaDestino.objeto != null) {
                        //Log.d(TAG, "Movimiento ignorado: celda destino nula o con burbuja");
                        tablero.tocaBurbuja(celdaOrigen, deltaX, deltaY);

                    } else {

                        //Log.d(TAG, "Moviendo burbuja de Celda: posX=" + celdaOrigen.posX + ", posY=" + celdaOrigen.posY + " a Celda: posX=" + celdaDestino.posX + ", posY=" + celdaDestino.posY );

                        if (turnoCPU) {
                            if (celdaOrigen.posX < celdaDestino.posX) {
                                tablero.addEfectoBurbuja(celdaOrigen.burbuja, EfectoResources.EFECTO_CPU_MOVE_RIGHT_OBJECT_TYPE);
                            } else {
                                tablero.addEfectoBurbuja(celdaOrigen.burbuja, EfectoResources.EFECTO_CPU_MOVE_LEFT_OBJECT_TYPE);
                            }
                        } else {
                            if (celdaOrigen.posX < celdaDestino.posX) {
                                tablero.addEfectoBurbuja(celdaOrigen.burbuja, EfectoResources.EFECTO_PLAYER_MOVE_RIGHT_OBJECT_TYPE);
                            } else {
                                tablero.addEfectoBurbuja(celdaOrigen.burbuja, EfectoResources.EFECTO_PLAYER_MOVE_LEFT_OBJECT_TYPE);
                            }
                        }

                        tablero.mueveBurbujaEntreCeldas(celdaOrigen, celdaDestino);

                        if (playMode == PLAY_MODE_CPU_VERSUS) {
                            if (player == TouchGameEvent.PLAYER_CPU) {
                                turnoCPU = false;
                            } else {
                                turnoCPU = true;
                            }
                        }

                    }

                }

            }

            // Caso movimiento de objeto entre burbujas
		    /* comentado: de momento no movemos objetos entre burbujas, se mueve la burbuja entera
			} else {
				
		        // Si la celda inicial no tiene objeto ignoramos el movimiento
		        if ( celdaOrigen.objeto == null ) {
		        	Log.d(TAG, "Movimiento ignorado: celda origen sin objeto");
		        	
		        } else if ( celdaDestino.objeto != null ){
		        	Log.d(TAG, "Movimiento ignorado: celda destino con objeto");
		        	
		        } else {
		        	
		        	int deltaX = celdaOrigen.posX - celdaDestino.posX; 
		        	int deltaY = celdaOrigen.posY - celdaDestino.posY; 
		        	if ( deltaX > 1 || deltaX < -1 || deltaY > 1 || deltaY < -1 ) {
		        		Log.d(TAG, "Movimiento ignorado: fuera de rango");
		        		
		        	} else {
		        		
		        		// comentado
		            	//if ( turnoCPU ) {
		            	//	tablero.addEfectoFijoCelda(celdaDestino, Tablero.EFECTO_CPU_TOUCH_OBJECT_TYPE );
		            	//} else {
		            	//	tablero.addEfectoFijoCelda(celdaDestino, Tablero.EFECTO_PLAYER_TOUCH_OBJECT_TYPE );
		            	//}
		            	

		            	Log.d(TAG, "Moviendo burbuja de Celda: posX=" + celdaOrigen.posX + ", posY=" + celdaOrigen.posY + " a Celda: posX=" + celdaDestino.posX + ", posY=" + celdaDestino.posY );
		            	
		            	tablero.mueveObjetoEntreBurbujas(celdaOrigen, celdaDestino);

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

    void doBurbujasCreation() {

        // Log.d(TAG, "burbuja created");

        tablero.addInitialBurbuja();

    }


    /**
     * Used to signal the thread whether it should be running or not.
     * Passing true allows the thread to run; passing false will shut it
     * down if it's already running. Calling start() after this was most
     * recently called with false will result in an immediate shutdown.
     *
     * @param b true to run, false to shut down
     */
    public void setRunning(boolean b) {
        run = b;

        if (run == false) {
            if (timerTask != null)
                timerTask.cancel();
        }
    }


    /**
     * returns the current int value of game state as defined by state
     * tracking constants
     *
     * @return
     */
    public int getGameState() {
        synchronized (surfaceHolder) {
            return state;
        }
    }


    /**
     * Sets the game mode. That is, whether we are running, paused, in the
     * failure state, in the victory state, etc.
     *
     * @param mode one of the STATE_* constants
     * @see #setState(int, CharSequence)
     */
    public void setGameState(int mode) {
        synchronized (surfaceHolder) {
            setGameState(mode, null);
        }
    }


    /**
     * Sets state based on input, optionally also passing in a text message.
     *
     * @param state
     * @param message
     */
    public void setGameState(int state, CharSequence message) {

        synchronized (surfaceHolder) {

            // change state if needed
            if (this.state != state) {
                this.previousState = this.state;
                this.state = state;
            }

            if (state == STATE_PLAY) {
                // Nothing to do ?

            } else if (state == STATE_RUNNING) {
                // When we enter the running state we should clear any old
                // events in the queue
                this.eventQueue.clear();

                // And reset the key state so we don't think a button is pressed when it isn't
                this.keyContext = null;

                // Game Initialization
                this.tablero.initTablero();
                this.personaje1.initPersonaje();
                //this.personaje2.initPersonaje();

            }

        }
    }

    boolean isGameEnded() {
    	/*
    	if ( tablero.isFinPartida(personaje1, personaje2) ) {
    		return true;
    	} else {
    		return false;
    	}
    	*/
        return false;

    }


    /**
     * Add key press input to the GameEvent queue
     */
    public boolean doKeyDown(int keyCode, KeyEvent msg) {
        eventQueue.add(new KeyGameEvent(keyCode, false, msg));

        return true;
    }


    /**
     * Add key press input to the GameEvent queue
     */
    public boolean doKeyUp(int keyCode, KeyEvent msg) {
        eventQueue.add(new KeyGameEvent(keyCode, true, msg));

        return true;
    }


    /**
     * Add Touch event to the GameEvent queue
     */
    public boolean doTouchMotion(int motionEvent, int x, int y) {
        eventQueue.add(new TouchGameEvent(motionEvent, x, y));

        return true;
    }


    /* Callback invoked when the surface dimensions change. */
    public void setSurfaceSize(int width, int height) {
        // synchronized to make sure these all change atomically
        synchronized (surfaceHolder) {

            //Log.i(TAG, "setting surface: width=" + width + ", height=" + height);

            // Reescalado de fondos
            ColiPopResources.resizeGraphics(width, height);

            // En principio solo hay una instacia de tablero
            Tablero.resizeTablero(width, height);

            // Reescalado de personajes
            personaje1.resizeGraphics(width, height);
            /* save memory
            personaje2.resizeGraphics(width, height);
            */

        }
    }


    /**
     * Pauses the physics update & animation.
     */
    public void pause() {
        synchronized (surfaceHolder) {
            if (state == STATE_RUNNING)
                setGameState(STATE_PAUSE);
            if (timerTask != null) {
                timerTask.cancel();
            }
        }
    }


    /**
     * Does the work of updating timer
     */
    void doTimeCount() {
        //Log.d(TAG,"Current time is " + timerCount);

        timerCount = timerCount + 1;
        try {
            int minutes = Double.valueOf(timerCount / 60).intValue();
            int seconds = timerCount - Double.valueOf(minutes * 60).intValue();
            if (seconds > 9) {
                timerValue = minutes + ":" + seconds;
            } else {
                timerValue = minutes + ":0" + seconds;
            }
        } catch (Exception e1) {
            //Log.e(TAG, "doCountDown threw " + e1.toString());
        }

        Message msg = handler.obtainMessage();

        Bundle b = new Bundle();
        b.putString("text", timerValue);


        // Condicion de fin de juego
        if (tablero.isTableroLlenoObjetos()) {

            // Enviamos mensaje de juego acabado y ganador
            b.putString("STATE_GAME_END", "" + STATE_GAME_END);
            b.putString("GAME_OVER", "" + true);

            timerTask = null;

            state = STATE_GAME_END;

/*
		// Caso 2 players. Hay algun ganador. Condicion de fin de juego
		} else if ( isGameEnded() ) {
        	
        	// Enviamos mensaje de juego acabado y ganador
            b.putString("STATE_GAME_END", "" + STATE_GAME_END);
            if ( personaje1.isGanador() ) {
            	b.putString("PLAYER_WIN", "" + true);
            }
            if ( personaje2.isGanador() ) {
            	b.putString("CPU_WIN", "" + true);
            }               
             
            timerTask = null;

            state = STATE_GAME_END;
*/
        } else {

            timerTask = new TimerTask() {
                public void run() {
                    doTimeCount();
                }
            };

            timer.schedule(timerTask, taskIntervalInMillis);
        }

        //this is how we send data back up to the main ColiPopView thread.
        //if you look in constructor of ColiPopView you will see code for
        //Handling of messages. This is borrowed directly from lunar lander.
        //Thanks again!
        msg.setData(b);
        handler.sendMessage(msg);

    }

    public void destroy() {
        pause();
        ColiPopResources.destroy();
        if (tablero != null) {
            tablero.destroy();
        }
        if (personaje1 != null) {
            personaje1.destroy();
        }
        if (personaje2 != null) {
            personaje2.destroy();
        }
        //surfaceHolder=null;
        //handler=null;
        //context=null;
        //res=null;
        //eventQueue=null;
        //keyContext=null;
        //timerValue=null;
        //timer=null;
        //timerTask=null;
        //tablero=null;
        //personaje1=null;
        //personaje2=null;
        //eventActionDown=null;
    }

}//end thread class
