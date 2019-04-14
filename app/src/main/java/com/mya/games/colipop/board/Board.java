package com.mya.games.colipop.board;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.mya.games.colipop.ColiPopResources;
import com.mya.games.colipop.IATouchEvent;
import com.mya.games.colipop.character.Character;

import java.util.Random;

public class Board {
    static final int DEFAULT_SURFACE_WIDTH = 800;
    static final int DEFAULT_SURFACE_HEIGHT = 480;

    // Tamaos de boards
    static int DEFAULT_BOARD_WIDTH = 320;
    static int DEFAULT_BOARD_HEIGHT = 320;

    static int BOARD_WIDTH = DEFAULT_BOARD_WIDTH;
    static int BOARD_HEIGHT = DEFAULT_BOARD_HEIGHT;

    // Offsets del board
    static int DEFAULT_OFFSET_X = 111;
    static int DEFAULT_OFFSET_Y = 20;

    static int OFFSET_X = DEFAULT_OFFSET_X;
    static int OFFSET_Y = DEFAULT_OFFSET_Y;

    // OFFSET DE COUNTERS
    static int DEFAULT_OFFSET_X_LIKE = 650;
    static int DEFAULT_OFFSET_Y_LIKE = 75;
    static int DEFAULT_OFFSET_X_DISLIKE = 650;
    static int DEFAULT_OFFSET_Y_DISLIKE = 250;

    static int OFFSET_X_LIKE = DEFAULT_OFFSET_X_LIKE;
    static int OFFSET_Y_LIKE = DEFAULT_OFFSET_Y_LIKE;
    static int OFFSET_X_DISLIKE = DEFAULT_OFFSET_X_DISLIKE;
    static int OFFSET_Y_DISLIKE = DEFAULT_OFFSET_Y_DISLIKE;

    static int COUNTER_Y_SPACER = Double.valueOf(1.2 * ThingResources.THING_HEIGHT).intValue();

    static int OFFSET_X_LIKE_COUNTER = OFFSET_X_LIKE + ThingResources.THING_WIDTH;
    static int OFFSET_Y_LIKE_COUNTER = OFFSET_Y_LIKE + Double.valueOf(ThingResources.THING_HEIGHT / 2).intValue();
    static int OFFSET_X_DISLIKE_COUNTER = OFFSET_X_DISLIKE + ThingResources.THING_WIDTH;
    static int OFFSET_Y_DISLIKE_COUNTER = OFFSET_Y_DISLIKE + Double.valueOf(ThingResources.THING_HEIGHT / 2).intValue();

    // Board num cells
    static int MAX_WIDTH = 8;
    static int MAX_HEIGHT = 8;

    static Random random = new Random();

    // Numero maximo de things animated independents
    static int MAX_THINGS_ANIMATED = 32;

    static long DEFAULT_TIME_BETWEEN_BUBBLES = 1000;

    /**
     * Class properties
     */

    // Resources
    Resources resources = null;

    int bubbleCommonAnimationIndex = 0;

    Character character1;
    //Character character2;

    Cell cells[][] = new Cell[MAX_WIDTH][MAX_HEIGHT];

    boolean initFullBoard = true;
    boolean boardInitialized = false;
    boolean boardStable = false;
    long boardStableTime = 0;
    long lastInitialBubbleTime = 0;
    boolean boardFullBubbles = false;
    boolean boardFullThings = false;

    Thing[] things_animated = new Thing[MAX_THINGS_ANIMATED];

    int[] things_explodes = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    long timeBetweenBubbles = DEFAULT_TIME_BETWEEN_BUBBLES;

    int lastLevel = 0;

    /**
     * Constructors
     */

    public Board(Resources resources, Character character1, Character character2) {
        this.resources = resources;

        this.character1 = character1;
        //this.character2 = character2;

        BubbleResources.initializeGraphics(resources);

        //Log.d(TAG, "Graficos Bubbles inicializados");

        ExplosionResources.initializeGraphics(resources);

        //Log.d(TAG, "Graficos Explosiones inicializados");

        ThingResources.initializeGraphics(resources);

        //Log.d(TAG, "Graficos Things inicializados");

        EfectoResources.initializeGraphics(resources);

        //Log.d(TAG, "Graficos Efectos inicializados");

        //initBoard();
    }

    public static void resizeBoard(int surfaceWidth, int surfaceHeight) {
        //Log.d(TAG, "Surface resize: width = " + surfaceWidth + ", height = " + surfaceHeight);

        // Nos quedamos con la proporcion mas pequea ( El board siempre ser cuadrado )
        float refactorIndex = 1;
        float indexWidth = surfaceWidth;
        indexWidth /= DEFAULT_BOARD_WIDTH;
        float indexHeight = surfaceHeight;
        indexHeight /= DEFAULT_BOARD_HEIGHT;
        if (indexWidth > indexHeight) {
            BOARD_WIDTH = surfaceHeight;
            BOARD_HEIGHT = surfaceHeight;
            refactorIndex = indexHeight;
        } else {
            BOARD_WIDTH = surfaceWidth;
            BOARD_HEIGHT = surfaceWidth;
            refactorIndex = indexWidth;
        }

        //Log.d(TAG, "Surface resize: BOARD_WIDTH = " + BOARD_WIDTH + ", BOARD_HEIGHT = " + BOARD_HEIGHT + ", refactor index = " + refactorIndex);

        BubbleResources.resizeGraphics(refactorIndex);

        //Log.d(TAG, "Graficos Bubbles resizados");

        ExplosionResources.resizeGraphics(refactorIndex);

        //Log.d(TAG, "Graficos Explosiones resizados");

        ThingResources.resizeGraphics(refactorIndex);

        //Log.d(TAG, "Graficos Things resizados");

        EfectoResources.resizeGraphics(refactorIndex);

        //Log.d(TAG, "Graficos Efectos resizados");

        // Stablecemos offsets para centrar el board en la pantalla
        float offsetWidth = surfaceWidth;
        offsetWidth -= BubbleResources.BUBBLE_WIDTH * Board.MAX_WIDTH;
        offsetWidth /= 2;
        OFFSET_X = Float.valueOf(offsetWidth).intValue();
        float offsetHeight = surfaceHeight;
        offsetHeight -= BubbleResources.BUBBLE_HEIGHT * Board.MAX_HEIGHT;
        offsetHeight /= 2;
        OFFSET_Y = Float.valueOf(offsetHeight).intValue();

        // Recolocamos counters
        calculateCountersOffsets(surfaceWidth, surfaceHeight);

        //Log.d(TAG, "Surface resize: OFFSET_X = " + OFFSET_X + ", OFFSET_Y = " + OFFSET_Y );
    }

    protected static void calculateCountersOffsets(int surfaceWidth, int surfaceHeight) {
        // Calculatemos el refactor indexes
        float refactorIndexWidth = surfaceWidth;
        refactorIndexWidth /= DEFAULT_SURFACE_WIDTH;
        // Prevencin de cosas raras
        if (refactorIndexWidth == 0) {
            refactorIndexWidth = 1;
        }

        float refactorIndexHeight = surfaceHeight;
        refactorIndexHeight /= DEFAULT_SURFACE_HEIGHT;
        // Prevencin de cosas raras
        if (refactorIndexHeight == 0) {
            refactorIndexHeight = 1;
        }

        OFFSET_X_LIKE = Float.valueOf(DEFAULT_OFFSET_X_LIKE * refactorIndexWidth).intValue();
        OFFSET_Y_LIKE = Float.valueOf(DEFAULT_OFFSET_Y_LIKE * refactorIndexHeight).intValue();
        OFFSET_X_DISLIKE = Float.valueOf(DEFAULT_OFFSET_X_DISLIKE * refactorIndexWidth).intValue();
        OFFSET_Y_DISLIKE = Float.valueOf(DEFAULT_OFFSET_Y_DISLIKE * refactorIndexHeight).intValue();

        COUNTER_Y_SPACER = Double.valueOf(1.2 * ThingResources.THING_HEIGHT).intValue();
        OFFSET_X_LIKE_COUNTER = OFFSET_X_LIKE + ThingResources.THING_WIDTH;
        OFFSET_Y_LIKE_COUNTER = OFFSET_Y_LIKE + Double.valueOf(ThingResources.THING_HEIGHT / 2).intValue();
        OFFSET_X_DISLIKE_COUNTER = OFFSET_X_DISLIKE + ThingResources.THING_WIDTH;
        OFFSET_Y_DISLIKE_COUNTER = OFFSET_Y_DISLIKE + Double.valueOf(ThingResources.THING_HEIGHT / 2).intValue();
    }

    public void initBoard() {
        this.cells = new Cell[MAX_WIDTH][MAX_HEIGHT];

        // Localizing external variables
        Cell[][] cells = this.cells;
        boolean initFullBoard = this.initFullBoard;

        int BUBBLE_WIDTH = BubbleResources.BUBBLE_WIDTH;
        int BUBBLE_HEIGHT = BubbleResources.BUBBLE_HEIGHT;
        int OFFSET_X = Board.OFFSET_X;
        int OFFSET_Y = Board.OFFSET_Y;
        int MAX_WIDTH = Board.MAX_WIDTH;
        int MAX_HEIGHT = Board.MAX_HEIGHT;

        for (int i = 0; i < MAX_WIDTH; i++) {

            for (int j = 0; j < MAX_HEIGHT; j++) {

                Cell cell = new Cell(i, j);

                // Condicion de inicializar board full de bubbles
                if (initFullBoard) {

                    Bubble bubble = new Bubble();

                    bubble.drawX = OFFSET_X + (i * BUBBLE_WIDTH);
                    bubble.drawY = OFFSET_Y + (j * BUBBLE_HEIGHT);

                    cell.bubble = bubble;

                    addRandomObject(cell);

                    // Comprobamos que el thing no pete nada
                    if (i >= 2 && cell.thing != null
                            && cells[i - 1][j].thing != null && cells[i - 1][j].thing.type == cell.thing.type
                            && cells[i - 2][j].thing != null && cells[i - 2][j].thing.type == cell.thing.type) {

                        cell.thing = null;
                    }
                    if (j >= 2 && cell.thing != null
                            && cells[i][j - 1].thing != null && cells[i][j - 1].thing.type == cell.thing.type
                            && cells[i][j - 2].thing != null && cells[i][j - 2].thing.type == cell.thing.type) {

                        cell.thing = null;
                    }

                }

                cells[i][j] = cell;

            }

        }

        boardStable = true;
        boardStableTime = System.currentTimeMillis();

        // Inicializamos marcadores
        things_explodes = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        things_animated = new Thing[MAX_THINGS_ANIMATED];

        boardInitialized = true;

        // Resto variables
        timeBetweenBubbles = DEFAULT_TIME_BETWEEN_BUBBLES;
        lastLevel = 0;

        //Log.d(TAG, "Board inicializado");
    }

    public Cell getCell(int i, int j) {
        if (i < 0 || i >= MAX_WIDTH || j < 0 || j >= MAX_HEIGHT) {
            return null;
        }
        return cells[i][j];
    }

    public void doBoardAnimation(Canvas canvas) {
        if (canvas == null || !boardInitialized) {
            return;
        }

        this.bubbleCommonAnimationIndex++;
        // Doing loop
        if (this.bubbleCommonAnimationIndex >= BubbleResources.BUBBLE_ANIMATION_SEQUENCE.length) {
            this.bubbleCommonAnimationIndex = 0;
        }

        // Localizing external variables
        Cell[][] cells = this.cells;
        int OFFSET_X = Board.OFFSET_X;
        int OFFSET_Y = Board.OFFSET_Y;
        int MAX_WIDTH = Board.MAX_WIDTH;
        int MAX_HEIGHT = Board.MAX_HEIGHT;
        boolean boardStable = this.boardStable;

        // Localizing external variables
        int BUBBLE_WIDTH = BubbleResources.BUBBLE_WIDTH;
        int BUBBLE_HEIGHT = BubbleResources.BUBBLE_HEIGHT;
		/* Save memory
		int BUBBLE_STATUS_GRAPHICS_SIZE = BubbleResources.BUBBLE_STATUS_GRAPHICS_SIZE;
		*/
        int BUBBLE_UNION_UP = BubbleResources.BUBBLE_UNION_UP;
        int BUBBLE_UNION_LEFT = BubbleResources.BUBBLE_UNION_LEFT;
        int BUBBLE_UNION_RIGHT = BubbleResources.BUBBLE_UNION_RIGHT;
        int BUBBLE_UNION_DOWN = BubbleResources.BUBBLE_UNION_DOWN;
        int BUBBLE_UNION_NONE = BubbleResources.BUBBLE_UNION_NONE;
        int BUBBLE_MOVE_UP = BubbleResources.BUBBLE_MOVE_UP;
        int BUBBLE_MOVE_LEFT = BubbleResources.BUBBLE_MOVE_LEFT;
        int BUBBLE_MOVE_RIGHT = BubbleResources.BUBBLE_MOVE_RIGHT;
        int BUBBLE_MOVE_DOWN = BubbleResources.BUBBLE_MOVE_DOWN;
        int BUBBLE_MOVE_NONE = BubbleResources.BUBBLE_MOVE_NONE;
        Bitmap[] BUBBLE_GRAPHICS_BITMAP = BubbleResources.BUBBLE_GRAPHICS_BITMAP;
        Bitmap[] BUBBLE_MOVE_GRAPHICS_BITMAP = BubbleResources.BUBBLE_MOVE_GRAPHICS_BITMAP;
		/* Save memory
		Bitmap[] BUBBLE_UNION_GRAPHICS_BITMAP = BubbleResources.BUBBLE_UNION_GRAPHICS_BITMAP;
		Bitmap[] BUBBLE_STATUS_GRAPHICS_BITMAP = BubbleResources.BUBBLE_STATUS_GRAPHICS_BITMAP;
		*/

        int EXPLOSION_GRAPHICS_SIZE = ExplosionResources.EXPLOSION_GRAPHICS_SIZE;
        Bitmap[] EXPLOSION_GRAPHICS_BITMAP = ExplosionResources.EXPLOSION_GRAPHICS_BITMAP;

        int THING_STATUS_BUBBLE = ThingResources.THING_STATUS_BUBBLE;
        int THING_STATUS_MOVIDO = ThingResources.THING_STATUS_MOVIDO;
        int THING_STATUS_EXPLODE = ThingResources.THING_STATUS_EXPLODE;
        int CARAMELO_OBJECT_TYPE = ThingResources.CARAMELO_OBJECT_TYPE;
        Bitmap[] CARAMELO_GRAPHICS_BITMAP = ThingResources.CARAMELO_GRAPHICS_BITMAP;
        Bitmap[] CARAMELO_GRANDE_GRAPHICS_BITMAP = ThingResources.CARAMELO_GRANDE_GRAPHICS_BITMAP;
        int PIRULETA_OBJECT_TYPE = ThingResources.PIRULETA_OBJECT_TYPE;
        Bitmap[] PIRULETA_GRAPHICS_BITMAP = ThingResources.PIRULETA_GRAPHICS_BITMAP;
        Bitmap[] PIRULETA_GRANDE_GRAPHICS_BITMAP = ThingResources.PIRULETA_GRANDE_GRAPHICS_BITMAP;
        int RASPA_OBJECT_TYPE = ThingResources.RASPA_OBJECT_TYPE;
        Bitmap[] RASPA_GRAPHICS_BITMAP = ThingResources.RASPA_GRAPHICS_BITMAP;
        Bitmap[] RASPA_GRANDE_GRAPHICS_BITMAP = ThingResources.RASPA_GRANDE_GRAPHICS_BITMAP;
        int PEINE_OBJECT_TYPE = ThingResources.PEINE_OBJECT_TYPE;
        Bitmap[] PEINE_GRAPHICS_BITMAP = ThingResources.PEINE_GRAPHICS_BITMAP;
        Bitmap[] PEINE_GRANDE_GRAPHICS_BITMAP = ThingResources.PEINE_GRANDE_GRAPHICS_BITMAP;

        boolean boardFullBubbles = true;
        boolean boardFullThings = true;
        boolean hayMovimiento = false;
        for (int i = 0; i < MAX_WIDTH; i++) {
            for (int j = 0; j < MAX_HEIGHT; j++) {
                Cell cell = cells[i][j];
                if (cell == null) continue;

                // Marca para no processar la cell ( solo 1 vez )
                if (cell.ignore) {
                    cell.ignore = false;
                    continue;
                }

                Thing thing = cell.thing;
                if (thing == null) {
                    boardFullThings = false;
                }

                Bubble bubble = cell.bubble;
                if (bubble == null) {
                    boardFullBubbles = false;
                } else {
                    updateBubbleAnimation(bubble, thing);

                    /**
                     * Update bubble position
                     */

                    // Si la bubble esta centrada verticalmente
                    // Nota: no se como esto esta as, pero si lo touchs se descuaringa todo
                    int bubbleCellPosX = OFFSET_X + (i * BUBBLE_HEIGHT);
                    if (bubble.drawX == bubbleCellPosX && canBubbleMoveUp(cells, bubble, i, j)) {

                        moveAndUpdateBubblePosition(cells, bubble, thing, i, j, BUBBLE_MOVE_UP, false);
                        // indicamos que hay movimiento de bubbles
                        hayMovimiento = true;
                        // Marcamos bubble como que se move
                        bubble.move = BUBBLE_MOVE_UP;
	            		
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
                        int bubbleCellPosY = OFFSET_Y + (j * BUBBLE_HEIGHT);
                        if (bubble.drawY != bubbleCellPosY || bubble.drawX != bubbleCellPosX) {
                            centraBubbleEnPosicion(bubble, bubbleCellPosX, bubbleCellPosY);
                            // indicamos que hay movimiento de bubbles
                            hayMovimiento = true;
                        } else {
                            // Marcamos bubble como que no se move
                            bubble.move = BUBBLE_MOVE_NONE;
                        }
                    }
		            
		            /* comento lo de los fondos
		            // Fondo bubble
		            if ( bubble.status < BUBBLE_STATUS_GRAPHICS_SIZE ) {
		            	canvas.drawBitmap(BUBBLE_STATUS_GRAPHICS_BITMAP[bubble.status], bubble.drawX, bubble.drawY, null);
		            }
		            */

                    if (thing != null && (thing.status == THING_STATUS_BUBBLE || thing.status == THING_STATUS_MOVIDO)) {
                        Bitmap bitmap = null;

                        int type = thing.type;
                        if (type == CARAMELO_OBJECT_TYPE) {
                            bitmap = CARAMELO_GRAPHICS_BITMAP[thing.graphicIndex];
                        } else if (type == PIRULETA_OBJECT_TYPE) {
                            bitmap = PIRULETA_GRAPHICS_BITMAP[thing.graphicIndex];
                        } else if (type == RASPA_OBJECT_TYPE) {
                            bitmap = RASPA_GRAPHICS_BITMAP[thing.graphicIndex];
                        } else if (type == PEINE_OBJECT_TYPE) {
                            bitmap = PEINE_GRAPHICS_BITMAP[thing.graphicIndex];
                        }

                        if (thing.status == THING_STATUS_MOVIDO) {
                            // Movemos bubble hasta su destiny
                            if (moveThingBetweenBubbles(thing)) {
                                // Si el thing est ubicado reseteamos statuss union de las bubbles que intervienen
                                thing.status = THING_STATUS_BUBBLE;
                                if (bubble.union == BUBBLE_UNION_UP) {
                                    cells[i][j + 1].bubble.union = BUBBLE_UNION_NONE;
                                } else if (bubble.union == BUBBLE_UNION_DOWN) {
                                    cells[i][j - 1].bubble.union = BUBBLE_UNION_NONE;
                                } else if (bubble.union == BUBBLE_UNION_RIGHT) {
                                    cells[i + 1][j].bubble.union = BUBBLE_UNION_NONE;
                                } else if (bubble.union == BUBBLE_UNION_LEFT) {
                                    cells[i - 1][j].bubble.union = BUBBLE_UNION_NONE;
                                }
                                bubble.union = BUBBLE_UNION_NONE;
                            }
                            // indicamos que hay movimiento de bubbles
                            hayMovimiento = true;

                        } else {
                            // Sincronizamos posiciones de thing y bubble
                            if (thing.drawX != bubble.drawX) {
                                thing.drawX = bubble.drawX;
                            }
                            if (thing.drawY != bubble.drawY) {
                                thing.drawY = bubble.drawY;
                            }

                        }

                        // Animate thing
                        canvas.drawBitmap(bitmap, thing.drawX, thing.drawY, null);
                    }

                    Bitmap bitmap = null;
                    // Caso bubble en movimiento
                    if (bubble.move != BUBBLE_MOVE_NONE) {
                        bitmap = BUBBLE_MOVE_GRAPHICS_BITMAP[bubble.graphicIndex];

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
                        bitmap = BUBBLE_GRAPHICS_BITMAP[bubble.graphicIndex];
                    }

                    // Animate bubble
                    canvas.drawBitmap(bitmap, bubble.drawX, bubble.drawY, null);
                }

                Explosion explosion = cell.explosion;
                if (explosion != null) {

                    // cambiamos el indice de animacin
                    explosion.graphicIndex++;

                    // Caso bubble ha acabado de exploder
                    if (explosion.graphicIndex >= EXPLOSION_GRAPHICS_SIZE) {

                        cell.explosion = null;

                        if (thing != null && thing.status == THING_STATUS_EXPLODE) {
                            things_explodes[thing.type]++;
                            cell.thing = null;

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
                        canvas.drawBitmap(EXPLOSION_GRAPHICS_BITMAP[explosion.graphicIndex], explosion.drawX, explosion.drawY, null);

                        // Animate thing grande
                        if (thing != null && thing.status == THING_STATUS_EXPLODE) {

                            Bitmap bitmap = null;

                            int type = thing.type;
                            if (type == CARAMELO_OBJECT_TYPE) {
                                bitmap = CARAMELO_GRANDE_GRAPHICS_BITMAP[0];
                            } else if (type == PIRULETA_OBJECT_TYPE) {
                                bitmap = PIRULETA_GRANDE_GRAPHICS_BITMAP[0];
                            } else if (type == RASPA_OBJECT_TYPE) {
                                bitmap = RASPA_GRANDE_GRAPHICS_BITMAP[0];
                            } else if (type == PEINE_OBJECT_TYPE) {
                                bitmap = PEINE_GRANDE_GRAPHICS_BITMAP[0];
                            }

                            canvas.drawBitmap(bitmap, explosion.drawX, explosion.drawY, null);
                        }

                        // indicamos que hay movimiento en el board
                        hayMovimiento = true;

                    }

                }

                // ahora no hace falta que el board est stable, solo que las bubbles no se muevan
                //if ( boardStable ) {

                /**
                 * Logica matcheado horizontal de things
                 */

                // Miramos si la bubble tiene thing
                if (i < (MAX_WIDTH - 2) && bubble != null && bubble.move == BUBBLE_MOVE_NONE && thing != null && thing.status == THING_STATUS_BUBBLE) {
                    // Miramos si la bubble de la derecha tiene el mismo thing
                    int thingType = thing.type;
                    Cell cellRight1 = cells[i + 1][j];
                    if (cellRight1 != null && cellRight1.bubble != null && cellRight1.bubble.move == BUBBLE_MOVE_NONE
                            && cellRight1.thing != null && cellRight1.thing.type == thingType && cellRight1.thing.status == THING_STATUS_BUBBLE) {
                        // Miramos si la bubble de mas a la derecha tiene el mismo thing
                        Cell cellRight2 = cells[i + 2][j];
                        if (cellRight2 != null && cellRight2.bubble != null && cellRight2.bubble.move == BUBBLE_MOVE_NONE
                                && cellRight2.thing != null && cellRight2.thing.type == thingType && cellRight2.thing.status == THING_STATUS_BUBBLE) {
                            // Explodemos bubbles
                            explodeBubble(cell);
                            explodeBubble(cellRight1);
                            explodeBubble(cellRight2);
                            // variable para contabilizar things explodes
                            int numThings = 3;
                            // ignoramos las cells de la derecha para evitar efecto raro
                            cellRight1.ignore = true;
                            cellRight2.ignore = true;
                            // Si hay mas bubbles con el mismo thing las explodemos tambien
                            for (int k = i + 3; k < MAX_WIDTH; k++) {
                                Cell cellRight = cells[k][j];
                                if (cellRight != null && cellRight.bubble != null && cellRight.bubble.move == BUBBLE_MOVE_NONE
                                        && cellRight.thing != null && cellRight.thing.type == thingType && cellRight.thing.status == THING_STATUS_BUBBLE) {
                                    explodeBubble(cellRight);
                                    numThings++;
                                    // ignoramos las cells de la derecha para evitar efecto raro
                                    cellRight.ignore = true;
                                } else {
                                    // En el momento en que no encontremos una bubble a exploder salimos del for
                                    break;
                                }
                            }
                            // Miramos tambien a la izquierda por si acaso
                            for (int k = i - 1; k >= 0; k--) {
                                Cell cellLeft = cells[k][j];
                                if (cellLeft != null && cellLeft.bubble != null && cellLeft.bubble.move == BUBBLE_MOVE_NONE
                                        && cellLeft.thing != null && cellLeft.thing.type == thingType && cellLeft.thing.status == THING_STATUS_BUBBLE) {
                                    explodeBubble(cellLeft);
                                    numThings++;
                                } else {
                                    // En el momento en que no encontremos una bubble a exploder salimos del for
                                    break;
                                }
                            }
                            // Marcamos como que hay movimiento
                            hayMovimiento = true;
                            // Updatemos meters de jugadores
                            this.character1.updateMeter(thingType, numThings);
                        }
                    }
                }

                /**
                 * Logica matcheado vertical de things
                 */

                // Miramos si la bubble tiene thing
                if (j < (MAX_HEIGHT - 2) && bubble != null && bubble.move == BUBBLE_MOVE_NONE && thing != null && thing.status == THING_STATUS_BUBBLE) {
                    // Miramos si la bubble de la arriba tiene el mismo thing
                    int thingType = thing.type;
                    Cell cellDown1 = cells[i][j + 1];
                    if (cellDown1 != null && cellDown1.bubble != null && cellDown1.bubble.move == BUBBLE_MOVE_NONE
                            && cellDown1.thing != null && cellDown1.thing.type == thingType && cellDown1.thing.status == THING_STATUS_BUBBLE) {
                        // Miramos si la bubble de mas a la arriba tiene el mismo thing
                        Cell cellDown2 = cells[i][j + 2];
                        if (cellDown2 != null && cellDown2.bubble != null && cellDown2.bubble.move == BUBBLE_MOVE_NONE
                                && cellDown2.thing != null && cellDown2.thing.type == thingType && cellDown2.thing.status == THING_STATUS_BUBBLE) {
                            // Explodemos bubbles
                            explodeBubble(cell);
                            explodeBubble(cellDown1);
                            explodeBubble(cellDown2);
                            // variable para contabilizar things explodes
                            int numThings = 3;
                            // ignoramos las cells de abajo para evitar efectos raros
                            cellDown1.ignore = true;
                            cellDown2.ignore = true;
                            // Si hay mas bubbles con el mismo thing las explodemos tambien
                            for (int k = j + 3; k < MAX_HEIGHT; k++) {
                                Cell cellDown = cells[i][k];
                                if (cellDown != null && cellDown.bubble != null && cellDown.bubble.move == BUBBLE_MOVE_NONE
                                        && cellDown.thing != null && cellDown.thing.type == thingType && cellDown.thing.status == THING_STATUS_BUBBLE) {
                                    explodeBubble(cellDown);
                                    numThings++;
                                    // ignoramos las cells de abajo para evitar efectos raros
                                    cellDown.ignore = true;
                                } else {
                                    // En el momento en que no encontremos una bubble a exploder salimos del for
                                    break;
                                }
                            }
                            // Miramos tambien abajo por si acaso
                            for (int k = j - 1; k >= 0; k--) {
                                Cell cellDown = cells[i][k];
                                if (cellDown != null && cellDown.bubble != null && cellDown.bubble.move == BUBBLE_MOVE_NONE
                                        && cellDown.thing != null && cellDown.thing.type == thingType && cellDown.thing.status == THING_STATUS_BUBBLE) {
                                    explodeBubble(cellDown);
                                    numThings++;
                                } else {
                                    // En el momento en que no encontremos una bubble a exploder salimos del for
                                    break;
                                }
                            }
                            // Marcamos como que hay movimiento
                            hayMovimiento = true;
                            // Updatemos meters de jugadores
                            this.character1.updateMeter(thingType, numThings);
                        }
                    }
                }

                //}

            }

        }

        // Control de movimientos en el board
        if (hayMovimiento) {
            this.boardStable = false;
        } else {
            // Solo updatemos a true el flag una vez para no falsear boardStableTime
            if (!this.boardStable) {
                this.boardStable = true;
                this.boardStableTime = System.currentTimeMillis();
            }
        }

        // Board full de bubbles
        this.boardFullThings = boardFullThings;
        this.boardFullBubbles = boardFullBubbles;

        /**
         *  Animacin de otros things
         */

        animateThingsIndependents(canvas);


        /**
         *  Dibujamos counters
         */

        //LIKE
        int offset_x_like = OFFSET_X_LIKE;
        int offset_y_like = OFFSET_Y_LIKE;
        int offset_x_like_counter = OFFSET_X_LIKE_COUNTER;
        int offset_y_like_counter = OFFSET_Y_LIKE_COUNTER;

        canvas.drawBitmap(CARAMELO_GRAPHICS_BITMAP[0], offset_x_like, offset_y_like, null);
        canvas.drawText(" = " + things_explodes[CARAMELO_OBJECT_TYPE], offset_x_like_counter, offset_y_like_counter, ColiPopResources.paint);

        offset_y_like += COUNTER_Y_SPACER;
        offset_y_like_counter += COUNTER_Y_SPACER;

        canvas.drawBitmap(PIRULETA_GRAPHICS_BITMAP[0], offset_x_like, offset_y_like, null);
        canvas.drawText(" = " + things_explodes[PIRULETA_OBJECT_TYPE], offset_x_like_counter, offset_y_like_counter, ColiPopResources.paint);

        // DISLIKE
        int offset_x_dislike = OFFSET_X_DISLIKE;
        int offset_y_dislike = OFFSET_Y_DISLIKE;
        int offset_x_dislike_counter = OFFSET_X_DISLIKE_COUNTER;
        int offset_y_dislike_counter = OFFSET_Y_DISLIKE_COUNTER;

        canvas.drawBitmap(RASPA_GRAPHICS_BITMAP[0], offset_x_dislike, offset_y_dislike, null);
        canvas.drawText(" = " + things_explodes[RASPA_OBJECT_TYPE], offset_x_dislike_counter, offset_y_dislike_counter, ColiPopResources.paint);

        offset_y_dislike += COUNTER_Y_SPACER;
        offset_y_dislike_counter += COUNTER_Y_SPACER;

        canvas.drawBitmap(PEINE_GRAPHICS_BITMAP[0], offset_x_dislike, offset_y_dislike, null);
        canvas.drawText(" = " + things_explodes[PEINE_OBJECT_TYPE], offset_x_dislike_counter, offset_y_dislike_counter, ColiPopResources.paint);
    }

    public void updateStatusBubble(Cell cell) {
        if (cell == null || cell.bubble == null) {
            return;
        }
        cell.bubble.status++;
        if (cell.bubble.status > BubbleResources.BUBBLE_STATUS_ROJO) {
            removeBubble(cell);
        }
    }

    public void updateBubbleAnimation(Bubble bubble, Thing thing) {
        if (bubble.move == BubbleResources.BUBBLE_MOVE_NONE) {
            // Todas las bubbles que estn quietas utilizan el mismo indice
            bubble.graphicIndex = BubbleResources.BUBBLE_ANIMATION_SEQUENCE[this.bubbleCommonAnimationIndex];
            if (bubble.graphicIndex >= BubbleResources.BUBBLE_GRAPHICS_SIZE) {
                bubble.graphicIndex = 0;
            }
        } else {
            // Las bubbles en movimento van de forma independent
            bubble.animationIndex++;
            // Hacemos loop
            if (bubble.animationIndex >= BubbleResources.BUBBLE_MOVE_ANIMATION_SEQUENCE.length) {
                bubble.animationIndex = 0;
            }
            bubble.graphicIndex = BubbleResources.BUBBLE_MOVE_ANIMATION_SEQUENCE[this.bubbleCommonAnimationIndex];
            if (bubble.graphicIndex >= BubbleResources.BUBBLE_MOVE_GRAPHICS_SIZE) {
                bubble.graphicIndex = 0;
            }
        }

        if (thing != null) {
            // utilizamos un indice de animacin comun
            thing.animationIndex++;
            // Hacemos loop
            if (thing.animationIndex >= ThingResources.THING_ANIMATION_SEQUENCE.length) {
                thing.animationIndex = 0;
            }
            thing.graphicIndex = ThingResources.THING_ANIMATION_SEQUENCE[thing.animationIndex];
            if (thing.graphicIndex >= ThingResources.THING_GRAPHICS_SIZE) {
                thing.graphicIndex = 0;
            }
        }
    }

    public boolean canBubbleMoveUp(Cell[][] cells, Bubble bubble, int i, int j) {
        if (bubble == null) {
            // TODO: revisar esto
            return false;
        }
        if (j == 0) {
            // Esta arriba del todo, no se puede mover mas
            return false;
        }
        Cell upCell = cells[i][j - 1];
        if (upCell == null || (upCell.bubble == null && upCell.explosion == null)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean canBubbleMoveDown(Cell[][] cells, Bubble bubble, int i, int j) {
        if (bubble == null) {
            // TODO: revisar esto
            return false;
        }
        if (j == (MAX_HEIGHT - 1)) {
            // Esta arriba del todo, no se puede mover mas
            return false;
        }
        Cell downCell = cells[i][j + 1];
        if (downCell == null || (downCell.bubble == null && downCell.explosion == null)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean canBubbleMoveRight(Cell[][] cells, Bubble bubble, int i, int j) {
        if (bubble == null) {
            // TODO: revisar esto
            return false;
        }
        // Miramos que hacia la derecha haya sitio para moverse
        if (i == (MAX_WIDTH - 1)) {
            // Esta a la derecha del todo, no se puede mover mas
            return false;
        }
        Cell rightCell = cells[i + 1][j];
        if (rightCell == null || (rightCell.bubble == null && rightCell.explosion == null)) {
            // Miramos que hacia arriba a la derecha haya un hueco donde meterse
            if (j == 0) {
                // No puede haber hueco ya que esta arriba del todo
                return false;
            }
            Cell rightUpCell = cells[i + 1][j - 1];
            if (rightUpCell == null || (rightUpCell.bubble == null && rightUpCell.explosion == null)) {
                // OK !!!
                return true;
            } else {
                // No hay hueco arriba a la derecha
                return false;
            }
        } else {
            // No hay sitio a la derecha
            return false;
        }
    }

    public boolean canBubbleMoveLeft(Cell[][] cells, Bubble bubble, int i, int j) {
        if (bubble == null) {
            // TODO: revisar esto
            return false;
        }
        // Miramos que hacia la izquierda haya sitio para moverse
        if (i == 0) {
            // Esta a la izquierda del todo, no se puede mover mas
            return false;
        }
        Cell leftCell = cells[i - 1][j];
        if (leftCell == null || (leftCell.bubble == null && leftCell.explosion == null)) {
            // Miramos que hacia arriba a la izquierda haya un hueco donde meterse
            if (j == 0) {
                // No puede haber hueco ya que esta arriba del todo
                return false;
            }
            Cell leftUpCell = cells[i - 1][j - 1];
            if (leftUpCell == null || (leftUpCell.bubble == null && leftUpCell.explosion == null)) {
                // OK !!!
                return true;
            } else {
                // No hay hueco arriba a la izquierda
                return false;
            }
        } else {
            return false;
        }
    }

    public void centraBubbleEnPosicion(Bubble bubble, int x, int y) {
        if (bubble == null) {
            return;
        }

        // Localizing external variables
        int BUBBLE_PIXEL_MOVE = BubbleResources.BUBBLE_PIXEL_MOVE;

        boolean finX = false;
        int drawX = bubble.drawX;
        if (x == bubble.drawX) {
            finX = true;

        } else if (x > bubble.drawX) {
            drawX += BUBBLE_PIXEL_MOVE;
            if ((x - drawX) < BUBBLE_PIXEL_MOVE) {
                finX = true;
            } else {
                bubble.drawX = drawX;
            }

        } else if (x < bubble.drawX) {
            drawX -= BUBBLE_PIXEL_MOVE;
            if ((bubble.drawX - x) < BUBBLE_PIXEL_MOVE) {
                finX = true;
            } else {
                bubble.drawX = drawX;
            }
        }
        boolean finY = false;
        int drawY = bubble.drawY;
        if (y == bubble.drawY) {
            finY = true;

        } else if (y > bubble.drawY) {
            drawY += BUBBLE_PIXEL_MOVE;
            if ((y - bubble.drawY) < BUBBLE_PIXEL_MOVE) {
                finY = true;
            } else {
                bubble.drawY = drawY;
            }

        } else if (y < bubble.drawY) {
            drawY -= BUBBLE_PIXEL_MOVE;
            if ((bubble.drawY - y) < BUBBLE_PIXEL_MOVE) {
                finY = true;
            } else {
                bubble.drawY = drawY;
            }
        }

        if (finX && finY) {
            // Forzamos centrado por si acaso
            bubble.drawX = x;
            bubble.drawY = y;
        }
    }

    public int getInitialCellXPos() {
        // Localizing external variables
        Cell[][] cells = this.cells;
        int MAX_WIDTH = Board.MAX_WIDTH;
        int MAX_HEIGHT = Board.MAX_HEIGHT;

        int init = random.nextInt(MAX_WIDTH - 1);

        for (int i = init; i < MAX_WIDTH; i++) {
            Cell leftCell = cells[i][MAX_HEIGHT - 1];
            if (leftCell == null || leftCell.bubble == null) {
                return i;
            }
        }
        // No se ha encontrado cell inicial probamos otra vez desde el 0
        for (int i = 0; i < init - 1; i++) {
            Cell leftCell = cells[i][MAX_HEIGHT - 1];
            if (leftCell == null || leftCell.bubble == null) {
                return i;
            }
        }
        // No se ha encontrado cell inicial ?!!??
        return -1;
    }

    public void addInitialBubble() {
        if (!boardInitialized) {
            return;
        }

        int level = this.character1.getCurrentLevel();
        if (level != lastLevel) {
            timeBetweenBubbles -= Double.valueOf(0.2 * timeBetweenBubbles).longValue();
            lastLevel = level;
        }

        long difTime = System.currentTimeMillis() - lastInitialBubbleTime;
        if (difTime < timeBetweenBubbles) {
            //No ha pasado el tiempo suficiente para aadir otra bubble
            return;
        }

        int xPos = getInitialCellXPos();
        if (xPos < 0) {
            // No se ha encontrado posicion inicial
            return;
        }

        Bubble bubble = new Bubble();

        bubble.drawX = OFFSET_X + (xPos * BubbleResources.BUBBLE_WIDTH);
        bubble.drawY = OFFSET_Y + (MAX_HEIGHT * BubbleResources.BUBBLE_HEIGHT);

        Cell cell = cells[xPos][MAX_HEIGHT - 1];
        if (cell == null) {
            return;
        }

        cell.bubble = bubble;
        addRandomObject(cell);

        lastInitialBubbleTime = System.currentTimeMillis();
    }

    void addRandomObject(Cell cell) {
        if (cell == null || cell.bubble == null) {
            return;
        }

        Thing thing = new Thing();

        thing.status = ThingResources.THING_STATUS_BUBBLE;

        int level = this.character1.getCurrentLevel();

        int porcentaje = random.nextInt(100);
        if (porcentaje < (ThingResources.CARAMELO_PORCENTAJE + level)) {
            thing.type = ThingResources.CARAMELO_OBJECT_TYPE;

        } else if (porcentaje < ThingResources.CARAMELO_PORCENTAJE + ThingResources.PIRULETA_PORCENTAJE + (2 * level)) {
            if (level >= 1) {
                thing.type = ThingResources.PIRULETA_OBJECT_TYPE;
            } else {
                thing.type = ThingResources.CARAMELO_OBJECT_TYPE;
            }

        } else if (porcentaje < ThingResources.CARAMELO_PORCENTAJE + ThingResources.PIRULETA_PORCENTAJE + ThingResources.RASPA_PORCENTAJE + (3 * level)) {
            if (level >= 2) {
                thing.type = ThingResources.RASPA_OBJECT_TYPE;
            } else {
                if (level >= 1) {
                    thing.type = ThingResources.PIRULETA_OBJECT_TYPE;
                } else {
                    thing.type = ThingResources.CARAMELO_OBJECT_TYPE;
                }
            }

        } else if (porcentaje < ThingResources.CARAMELO_PORCENTAJE + ThingResources.PIRULETA_PORCENTAJE + ThingResources.RASPA_PORCENTAJE + ThingResources.PEINE_PORCENTAJE + (4 * level)) {
            if (level >= 4) {
                thing.type = ThingResources.PEINE_OBJECT_TYPE;
            } else {
                if (level >= 2) {
                    thing.type = ThingResources.PIRULETA_OBJECT_TYPE;
                } else {
                    thing.type = ThingResources.CARAMELO_OBJECT_TYPE;
                }
            }

        } else {
            // Sin thing
            return;
        }

        Bubble bubble = cell.bubble;

        thing.drawX = bubble.drawX;
        thing.drawY = bubble.drawY;

        // Randomizamos indices animacin
        thing.animationIndex = random.nextInt(ThingResources.THING_ANIMATION_SEQUENCE.length);

        cell.thing = thing;
    }

    public void moveAndUpdateBubblePosition(Cell[][] cells, Bubble bubble, Thing thing, int i, int j, int direction, boolean ignoreNewCell) {
        // Movemos bubble
        if (direction == BubbleResources.BUBBLE_MOVE_UP) {
            bubble.drawY -= BubbleResources.BUBBLE_PIXEL_MOVE;
        } else if (direction == BubbleResources.BUBBLE_MOVE_LEFT) {
            bubble.drawX -= BubbleResources.BUBBLE_PIXEL_MOVE;
        } else if (direction == BubbleResources.BUBBLE_MOVE_RIGHT) {
            bubble.drawX += BubbleResources.BUBBLE_PIXEL_MOVE;
        } else if (direction == BubbleResources.BUBBLE_MOVE_DOWN) {
            bubble.drawY += BubbleResources.BUBBLE_PIXEL_MOVE;
        }

        // Recalculatemos posicin en board
        CellPosition newCellPos = calculatePosBubbleInBoard(bubble);
        if (newCellPos == null) {
            // Error calculo posicion
            return;
        }

        Cell newCell = cells[newCellPos.i][newCellPos.j];
        if (newCell.bubble != null) {
            // La cell ya est ocupada
            return;
        }

        // Quitamos la bubble de su cell anterior
        Cell cell = cells[i][j];
        cell.bubble = null;

        // Ponemos la bubble en la nueva posicion
        newCell.bubble = bubble;

        // Updateamos tambien el thing ( si hay )
        if (thing != null) {
            if (direction == BubbleResources.BUBBLE_MOVE_UP) {
                thing.drawY -= BubbleResources.BUBBLE_PIXEL_MOVE;
            } else if (direction == BubbleResources.BUBBLE_MOVE_LEFT) {
                thing.drawX -= BubbleResources.BUBBLE_PIXEL_MOVE;
            } else if (direction == BubbleResources.BUBBLE_MOVE_RIGHT) {
                thing.drawX += BubbleResources.BUBBLE_PIXEL_MOVE;
            } else if (direction == BubbleResources.BUBBLE_MOVE_DOWN) {
                thing.drawY += BubbleResources.BUBBLE_PIXEL_MOVE;
            }
            cell.thing = null;
            newCell.thing = thing;
        }

        if (ignoreNewCell) {
            newCell.ignore = true;
        }
    }

    public CellPosition calculatePosBubbleInBoard(Bubble bubble) {
        int xPos = (bubble.drawX - OFFSET_X) / BubbleResources.BUBBLE_WIDTH;
        int yPos = (bubble.drawY - OFFSET_Y) / BubbleResources.BUBBLE_HEIGHT;

        if (xPos < 0) {
            xPos = 0;
        }
        if (xPos >= MAX_WIDTH) {
            xPos = MAX_WIDTH - 1;
        }

        if (yPos < 0) {
            yPos = 0;
        }
        if (yPos >= MAX_HEIGHT) {
            yPos = MAX_HEIGHT - 1;
        }

        return new CellPosition(xPos, yPos);
    }

    public Cell getCellInCoordinates(int x, int y) {
        if (!boardInitialized) {
            return null;
        }

        int normX = (x - Board.OFFSET_X) / BubbleResources.BUBBLE_WIDTH;
        int normY = (y - Board.OFFSET_Y) / BubbleResources.BUBBLE_HEIGHT;

        //Log.d(TAG, "getCellInCoordinates: x=" + x + ", y=" + y + ", normX=" + normX + ", normY=" + normY );

        if (normX < Board.MAX_WIDTH && normX >= 0 && normY < Board.MAX_HEIGHT && normY >= 0) {
            Cell cell = cells[normX][normY];
            return cell;
        }

        return null;
    }

    public void explodeBubble(Cell cell) {
        if (cell == null || cell.bubble == null) {
            return;
        }

        Explosion explosion = new Explosion();

        explosion.graphicIndex = 0;

        Bubble bubble = cell.bubble;
        explosion.drawX = bubble.drawX - ((ExplosionResources.EXPLOSION_WIDTH - BubbleResources.BUBBLE_WIDTH) / 2);
        explosion.drawY = bubble.drawY - ((ExplosionResources.EXPLOSION_HEIGHT - BubbleResources.BUBBLE_HEIGHT) / 2);

        cell.explosion = explosion;

        Thing thing = cell.thing;
        if (thing != null) {
            thing.animationIndex = 0;
            thing.graphicIndex = 0;

            thing.drawX = explosion.drawX;
            thing.drawY = explosion.drawY;

            thing.targetX = 5 + (ThingResources.THING_WIDTH / 2);
            thing.targetY = 120 + (ThingResources.THING_HEIGHT / 2);

            thing.status = ThingResources.THING_STATUS_EXPLODE;
        }

        cell.bubble = null;
    }

    public void removeBubble(Cell cell) {
        if (cell == null || cell.bubble == null) {
            return;
        }

        explodeBubble(cell);

        Thing thing = cell.thing;
        if (thing != null) {
            thing.targetX = thing.drawX;
            thing.targetY = BOARD_HEIGHT + ThingResources.THING_HEIGHT;

            thing.status = ThingResources.THING_STATUS_REMOVED;
        }

        // Localizing external variables
        Thing[] things_animated = this.things_animated;

        // Aadimos thing a los animated independentmente y lo quitamos de la cell
        boolean thingAdded = false;
        for (int i = 0; i < MAX_THINGS_ANIMATED; i++) {
            if (things_animated[i] == null) {
                things_animated[i] = thing;
                thingAdded = true;
                break;
            }
        }
        // Si no encontramos posicin libre, lo metemos en una posicin aleatoria
        if (!thingAdded) {
            things_animated[random.nextInt(MAX_THINGS_ANIMATED - 1)] = thing;
        }
        cell.thing = null;

        // Tambien controla la velocidad del turno de la CPU
        boardStable = false;
    }

    public void moveThingBetweenBubbles(Cell origin, Cell destiny) {
        if (origin == null || destiny == null) {
            return;
        }

        int deltaX = origin.posX - destiny.posX;
        int deltaY = origin.posY - destiny.posY;
        if (deltaX > 1 || deltaX < -1 || deltaY > 1 || deltaY < -1) {
            return;
        }

        Thing thing = origin.thing;
        if (thing == null) {
            return;
        }

        Bubble bubbleDestiny = destiny.bubble;
        if (bubbleDestiny == null) {
            return;
        }

        thing.targetX = bubbleDestiny.drawX;
        thing.targetY = bubbleDestiny.drawY;

        thing.status = ThingResources.THING_STATUS_MOVIDO;

        // Quitamos el thing de la cell origin y lo ponemos en la cell destiny
        origin.thing = null;
        destiny.thing = thing;

        // Updatemos status de movimiento de las bubbles
        if (deltaX == 0 && deltaY > 0) {
            origin.bubble.union = BubbleResources.BUBBLE_UNION_DOWN;
            destiny.bubble.union = BubbleResources.BUBBLE_UNION_UP;
        } else if (deltaX == 0 && deltaY < 0) {
            origin.bubble.union = BubbleResources.BUBBLE_UNION_UP;
            destiny.bubble.union = BubbleResources.BUBBLE_UNION_DOWN;
        } else if (deltaY == 0 && deltaX > 0) {
            origin.bubble.union = BubbleResources.BUBBLE_UNION_LEFT;
            destiny.bubble.union = BubbleResources.BUBBLE_UNION_RIGHT;
        } else if (deltaY == 0 && deltaX < 0) {
            origin.bubble.union = BubbleResources.BUBBLE_UNION_RIGHT;
            destiny.bubble.union = BubbleResources.BUBBLE_UNION_LEFT;
        }

        // Tambien controla la velocidad del turno de la CPU
        this.boardStable = false;
    }

    public void touchBubble(Cell cell, int directionX, int directionY) {
        if (cell == null) {
            return;
        }

        Bubble bubble = cell.bubble;
        if (bubble == null) {
            return;
        }

        // Limitamos maximo de movimiento
        int movimientoX = 0;
        if (directionX > 0) {
            movimientoX = BubbleResources.BUBBLE_WIDTH_MEDIOS;
        } else if (directionX < 0) {
            movimientoX = -BubbleResources.BUBBLE_WIDTH_MEDIOS;
        }
        int movimientoY = 0;
        if (directionY > 0) {
            movimientoY = BubbleResources.BUBBLE_WIDTH_MEDIOS;
        } else if (directionY < 0) {
            movimientoY = -BubbleResources.BUBBLE_WIDTH_MEDIOS;
        }

        bubble.drawX -= movimientoX;
        bubble.drawY -= movimientoY;

        Thing thing = cell.thing;
        if (thing == null) {
            return;
        }

        thing.drawX -= movimientoX;
        thing.drawY -= movimientoY;
    }

    public void moveBubbleBetweenCells(Cell origin, Cell destiny) {
        if (origin == null || destiny == null) {
            return;
        }

        int deltaX = destiny.posX - origin.posX;
        int deltaY = destiny.posY - origin.posY;
        if (deltaX > 1 || deltaX < -1 || deltaY > 1 || deltaY < -1) {
            return;
        }

        Bubble bubble = origin.bubble;
        if (bubble == null) {
            return;
        }

        Thing thingDestiny = destiny.thing;
        if (thingDestiny != null) {
            return;
        }

        // Movemos bubble a la nueva cell
        destiny.bubble = origin.bubble;
        destiny.thing = origin.thing;
        // Quitamos bubble de la cell origin
        origin.bubble = null;
        origin.thing = null;

        // Updatemos status de movimiento de la bubble
        if (deltaX == 0 && deltaY > 0) {
            //Log.d(TAG,"Moving bubble down: deltaX = " + deltaX + ", deltaY = " + deltaY);
            destiny.bubble.move = BubbleResources.BUBBLE_MOVE_DOWN;
        } else if (deltaX == 0 && deltaY < 0) {
            //Log.d(TAG,"Moving bubble up: deltaX = " + deltaX + ", deltaY = " + deltaY);
            destiny.bubble.move = BubbleResources.BUBBLE_MOVE_UP;
        } else if (deltaY == 0 && deltaX > 0) {
            //Log.d(TAG,"Moving bubble right: deltaX = " + deltaX + ", deltaY = " + deltaY);
            destiny.bubble.move = BubbleResources.BUBBLE_MOVE_RIGHT;
        } else if (deltaY == 0 && deltaX < 0) {
            //Log.d(TAG,"Moving bubble left: deltaX = " + deltaX + ", deltaY = " + deltaY);
            destiny.bubble.move = BubbleResources.BUBBLE_MOVE_LEFT;
        }

        // Tambien controla la velocidad del turno de la CPU
        boardStable = false;
    }

    public void addEfectoBubble(Bubble bubble, int efectoFijoType) {
        if (bubble == null) {
            return;
        }

        Thing thing = new Thing();

        thing.bubble = bubble;
        thing.drawX = bubble.drawX;
        thing.drawY = bubble.drawY;
        thing.type = efectoFijoType;
        thing.status = ThingResources.THING_STATUS_EFECTO_FIJO;

        // Localizing external variables
        Thing[] things_animated = this.things_animated;

        // Aadimos thing a los animated independentmente
        boolean thingAdded = false;
        for (int i = 0; i < MAX_THINGS_ANIMATED; i++) {
            if (things_animated[i] == null) {
                things_animated[i] = thing;
                thingAdded = true;
                break;
            }
        }
        // Si no encontramos posicin libre, lo metemos en una posicin aleatoria
        if (!thingAdded) {
            things_animated[random.nextInt(MAX_THINGS_ANIMATED - 1)] = thing;
        }
    }

    public void addEfectoTouch(int drawX, int drawY, int efectoFijoType) {
        Thing thing = new Thing();

        thing.drawX = drawX - ThingResources.THING_WIDTH_MEDIO;
        thing.drawY = drawY - ThingResources.THING_HEIGHT_MEDIO;
        thing.type = efectoFijoType;
        thing.status = ThingResources.THING_STATUS_EFECTO_FIJO;

        // Localizing external variables
        Thing[] things_animated = this.things_animated;

        // Aadimos thing a los animated independentmente
        boolean thingAdded = false;
        for (int i = 0; i < MAX_THINGS_ANIMATED; i++) {
            if (things_animated[i] == null) {
                things_animated[i] = thing;
                thingAdded = true;
                break;
            }
        }
        // Si no encontramos posicin libre, lo metemos en una posicin aleatoria
        if (!thingAdded) {
            things_animated[random.nextInt(MAX_THINGS_ANIMATED - 1)] = thing;
        }
    }

    public void animateThingsIndependents(Canvas canvas) {
        // Animating los things independents
        Thing[] things_animated = this.things_animated;
        if (things_animated == null || things_animated.length == 0) {
            return;
        }

        //Localizing extenal variables
        int MAX_THINGS_ANIMATED = Board.MAX_THINGS_ANIMATED;
        int THING_STATUS_REMOVED = ThingResources.THING_STATUS_REMOVED;
        int THING_STATUS_EFECTO_FIJO = ThingResources.THING_STATUS_EFECTO_FIJO;

        for (int i = 0; i < MAX_THINGS_ANIMATED; i++) {
            Thing thing = things_animated[i];
            if (thing == null) {
                continue;
            }
            // De momento animating solo los things removeds
            if (thing.status == THING_STATUS_REMOVED) {
                animateThingRemoved(canvas, i, thing);

            } else if (thing.status == THING_STATUS_EFECTO_FIJO) {
                animateThingEfectoFijo(canvas, i, thing);
            }
        }
    }

    public void animateThingRemoved(Canvas canvas, int i, Thing thing) {
        thing.animationIndex++;
        // Hacemos loop
        if (thing.animationIndex >= ThingResources.THING_REMOVED_ANIMATION_SEQUENCE.length) {
            thing.animationIndex = 0;
        }
        thing.graphicIndex = ThingResources.THING_REMOVED_ANIMATION_SEQUENCE[thing.animationIndex];
        if (thing.graphicIndex >= ThingResources.THING_GRAPHICS_SIZE) {
            thing.graphicIndex = 0;
        }

        boolean finX = false;
        int drawX = thing.drawX;
        if (thing.targetX == thing.drawX) {
            finX = true;

        } else if (thing.targetX > thing.drawX) {
            drawX += ThingResources.THING_PIXEL_MOVE;
            if ((thing.targetX - drawX) < ThingResources.THING_REMOVED_GRAPHICS_SIZE) {
                finX = true;
            } else {
                thing.drawX = drawX;
            }

        } else if (thing.targetX < thing.drawX) {
            drawX -= ThingResources.THING_PIXEL_MOVE;
            if ((thing.drawX - thing.targetX) < ThingResources.THING_REMOVED_GRAPHICS_SIZE) {
                finX = true;
            } else {
                thing.drawX = drawX;
            }
        }
        boolean finY = false;
        int drawY = thing.drawY;
        if (thing.targetY == thing.drawY) {
            finY = true;

        } else if (thing.targetY > thing.drawY) {
            drawY += ThingResources.THING_PIXEL_MOVE;
            if ((thing.targetY - thing.drawY) < ThingResources.THING_REMOVED_GRAPHICS_SIZE) {
                finY = true;
            } else {
                thing.drawY = drawY;
            }

        } else if (thing.targetY < thing.drawY) {
            drawY -= ThingResources.THING_PIXEL_MOVE;
            if ((thing.drawY - thing.targetY) < ThingResources.THING_REMOVED_GRAPHICS_SIZE) {
                finY = true;
            } else {
                thing.drawY = drawY;
            }
        }

        if (finX && finY) {
            things_animated[i] = null;

        } else {
            Bitmap bitmap = null;

            int type = thing.type;
            if (type == ThingResources.CARAMELO_OBJECT_TYPE) {
                bitmap = ThingResources.CARAMELO_GRANDE_GRAPHICS_BITMAP[thing.graphicIndex];
            } else if (type == ThingResources.PIRULETA_OBJECT_TYPE) {
                bitmap = ThingResources.PIRULETA_GRANDE_GRAPHICS_BITMAP[thing.graphicIndex];
            } else if (type == ThingResources.RASPA_OBJECT_TYPE) {
                bitmap = ThingResources.RASPA_GRANDE_GRAPHICS_BITMAP[thing.graphicIndex];
            } else if (type == ThingResources.PEINE_OBJECT_TYPE) {
                bitmap = ThingResources.PEINE_GRANDE_GRAPHICS_BITMAP[thing.graphicIndex];
            }

            canvas.drawBitmap(bitmap, drawX, drawY, null);
        }
    }

    void animateThingEfectoFijo(Canvas canvas, int i, Thing thing) {
        thing.animationIndex++;
        // Sin loop, lo quitamos
        if (thing.animationIndex >= EfectoResources.EFECTO_FIJO_ANIMATION_SEQUENCE.length) {
            things_animated[i] = null;
            return;
        }
        thing.graphicIndex = EfectoResources.EFECTO_FIJO_ANIMATION_SEQUENCE[thing.animationIndex];
        if (thing.graphicIndex >= EfectoResources.EFECTO_FIJO_ANIMATION_SEQUENCE.length) {
            thing.graphicIndex = 0;
        }

        Bitmap bitmap = null;

        int type = thing.type;
        if (type == EfectoResources.EFECTO_BLOQUEO_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_BLOQUEO_GRAPHICS_BITMAP[thing.graphicIndex];

        } else if (type == EfectoResources.EFECTO_PLAYER_TOUCH_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP[thing.graphicIndex];
		/* save memory
		} else if ( type == EfectoResources.EFECTO_CPU_TOUCH_OBJECT_TYPE ) {
			bitmap = EfectoResources.EFECTO_CPU_TOUCH_GRAPHICS_BITMAP[ thing.graphicIndex ];
		*/
        } else if (type == EfectoResources.EFECTO_PLAYER_MOVE_LEFT_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP[thing.graphicIndex];

        } else if (type == EfectoResources.EFECTO_PLAYER_MOVE_RIGHT_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP[thing.graphicIndex];

        } else if (type == EfectoResources.EFECTO_TOUCH_MOVE_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP[thing.graphicIndex];
        }

        // Si el thing tiene asociado una bubble seguimos su posicion
        if (thing.bubble != null) {
            thing.drawX = thing.bubble.drawX;
            thing.drawY = thing.bubble.drawY;
        }

        canvas.drawBitmap(bitmap, thing.drawX, thing.drawY, null);
    }

    public boolean moveThingBetweenBubbles(Thing thing) {
        if (thing == null) {
            return false;
        }

        boolean finX = false;
        int drawX = thing.drawX;
        if (thing.targetX == thing.drawX) {
            finX = true;

        } else if (thing.targetX > thing.drawX) {
            drawX += ThingResources.THING_PIXEL_MOVE;
            if ((thing.targetX - drawX) < ThingResources.THING_GRAPHICS_SIZE) {
                finX = true;
            } else {
                thing.drawX = drawX;
            }

        } else if (thing.targetX < thing.drawX) {
            drawX -= ThingResources.THING_PIXEL_MOVE;
            if ((thing.drawX - thing.targetX) < ThingResources.THING_GRAPHICS_SIZE) {
                finX = true;
            } else {
                thing.drawX = drawX;
            }
        }
        boolean finY = false;
        int drawY = thing.drawY;
        if (thing.targetY == thing.drawY) {
            finY = true;

        } else if (thing.targetY > thing.drawY) {
            drawY += ThingResources.THING_PIXEL_MOVE;
            if ((thing.targetY - thing.drawY) < ThingResources.THING_GRAPHICS_SIZE) {
                finY = true;
            } else {
                thing.drawY = drawY;
            }

        } else if (thing.targetY < thing.drawY) {
            drawY -= ThingResources.THING_PIXEL_MOVE;
            if ((thing.drawY - thing.targetY) < ThingResources.THING_GRAPHICS_SIZE) {
                finY = true;
            } else {
                thing.drawY = drawY;
            }
        }

        if (finX && finY) {
            return true;
        } else {
            return false;
        }
    }

    public IATouchEvent[] calculateNextMovement(Character character) {
        // Board no stable
        if (!boardStable) {
            return null;
        }

        // Consideramos un retardo mnimo a partir de que el board se estabilice
        long retardo = System.currentTimeMillis() - boardStableTime;
        if (retardo < 1000) {
            return null;
        }

        //Log.d(TAG, "calculateNextMovement: retardo is " + retardo + " ms");

        // Localizing external variables
        Cell[][] cells = this.cells;
        int BUBBLE_WIDTH = BubbleResources.BUBBLE_WIDTH;
        int BUBBLE_HEIGHT = BubbleResources.BUBBLE_HEIGHT;
        int MAX_WIDTH = Board.MAX_WIDTH;
        int MAX_HEIGHT = Board.MAX_HEIGHT;
        Random random = Board.random;

        // Contador de seguridad para evitar bucles infinitos
        int maxIterations = MAX_WIDTH * MAX_HEIGHT;
        while (maxIterations > 0) {
            // Updatemos numero iteraciones;
            maxIterations--;

            // Calculatemos movimiento movimiento aleatorio
            Cell cell = cells[random.nextInt(MAX_WIDTH - 1)][random.nextInt(MAX_HEIGHT - 1)];
            if (cell == null || cell.bubble == null) {
                // Movimiento invalido
                continue;
            }

            // Caso la cell tiene un thing
            if (cell.thing != null) {
                // Bubble con thing
                continue;
            }

            IATouchEvent[] events = new IATouchEvent[2];

            events[0] = new IATouchEvent(MotionEvent.ACTION_DOWN, cell.bubble.drawX + (BUBBLE_WIDTH / 2), cell.bubble.drawY + (BUBBLE_HEIGHT / 2));
            events[1] = new IATouchEvent(MotionEvent.ACTION_UP, cell.bubble.drawX + (BUBBLE_WIDTH / 2), cell.bubble.drawY + (BUBBLE_HEIGHT / 2));

            return events;
        }

        return null;
    }

    public boolean isFinPartida(Character p1, Character p2) {
        int like = things_explodes[ThingResources.CARAMELO_OBJECT_TYPE] + things_explodes[ThingResources.PIRULETA_OBJECT_TYPE];
        int dislike = things_explodes[ThingResources.RASPA_OBJECT_TYPE] + things_explodes[ThingResources.PEINE_OBJECT_TYPE];

        //Log.d(TAG,"Like = " + like + ", Dislike = " + dislike);

        // De momento no se gana nunca
		/*if ((like - dislike) > 20 ) {
			p1.setWinner(true);
			return true;
		} else*/
        if ((like - dislike) < -20) {
            p2.setWinner(true);
            return true;
        } else {
            return false;
        }

    }

    public boolean isBoardFullBubbles() {
        return boardFullBubbles;
    }

    public void setBoardFullBubbles(boolean boardFullBubbles) {
        this.boardFullBubbles = boardFullBubbles;
    }

    public boolean isBoardFullThings() {
        return boardFullThings;
    }

    public void setBoardFullThings(boolean boardFullThings) {
        this.boardFullThings = boardFullThings;
    }

    public void destroy() {
        BubbleResources.destroy();
        EfectoResources.destroy();
        ExplosionResources.destroy();
        ThingResources.destroy();
    }
}

/**
 * Classe auxiliar para devolver posicin en cell de un Thing en board
 *
 * @author amarinji
 */
class CellPosition {
    public int i = 0;
    public int j = 0;

    public CellPosition(int i, int j) {
        this.i = i;
        this.j = j;
    }
}
