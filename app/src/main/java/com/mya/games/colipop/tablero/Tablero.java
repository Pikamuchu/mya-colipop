package com.mya.games.colipop.tablero;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.mya.games.colipop.ColiPopResources;
import com.mya.games.colipop.IATouchEvent;
import com.mya.games.colipop.personaje.Personaje;

import java.util.Random;

public class Tablero {

    /**
     * Static properties
     */

    //static final String TAG = "ColiPop";

    static final int DEFAULT_SURFACE_WIDTH = 800;
    static final int DEFAULT_SURFACE_HEIGHT = 480;

    // Tamaos de tableros
    static int DEFAULT_TABLERO_WIDTH = 320;
    static int DEFAULT_TABLERO_HEIGHT = 320;

    static int TABLERO_WIDTH = DEFAULT_TABLERO_WIDTH;
    static int TABLERO_HEIGHT = DEFAULT_TABLERO_HEIGHT;

    // Offsets del tablero
    static int DEFAULT_OFFSET_X = 111;
    static int DEFAULT_OFFSET_Y = 20;

    static int OFFSET_X = DEFAULT_OFFSET_X;
    static int OFFSET_Y = DEFAULT_OFFSET_Y;

    // OFFSET DE CONTADORES
    static int DEFAULT_OFFSET_X_LIKE = 650;
    static int DEFAULT_OFFSET_Y_LIKE = 75;
    static int DEFAULT_OFFSET_X_DISLIKE = 650;
    static int DEFAULT_OFFSET_Y_DISLIKE = 250;

    static int OFFSET_X_LIKE = DEFAULT_OFFSET_X_LIKE;
    static int OFFSET_Y_LIKE = DEFAULT_OFFSET_Y_LIKE;
    static int OFFSET_X_DISLIKE = DEFAULT_OFFSET_X_DISLIKE;
    static int OFFSET_Y_DISLIKE = DEFAULT_OFFSET_Y_DISLIKE;

    static int COUNTER_Y_SPACER = Double.valueOf(1.2 * ObjetoResources.OBJETO_HEIGHT).intValue();
    ;
    static int OFFSET_X_LIKE_COUNTER = OFFSET_X_LIKE + ObjetoResources.OBJETO_WIDTH;
    static int OFFSET_Y_LIKE_COUNTER = OFFSET_Y_LIKE + Double.valueOf(ObjetoResources.OBJETO_HEIGHT / 2).intValue();
    static int OFFSET_X_DISLIKE_COUNTER = OFFSET_X_DISLIKE + ObjetoResources.OBJETO_WIDTH;
    static int OFFSET_Y_DISLIKE_COUNTER = OFFSET_Y_DISLIKE + Double.valueOf(ObjetoResources.OBJETO_HEIGHT / 2).intValue();

    // Nmero de celdas del tablero
    static int MAX_WIDTH = 8;
    static int MAX_HEIGHT = 8;

    static Random random = new Random();

    // Numero maximo de objetos animados independientes
    static int MAX_OBJETOS_ANIMADOS = 32;

    static long DEFAULT_TIME_ENTRE_BURBUJAS = 1000;

    /**
     * Class properties
     */

    // Resources
    Resources resources = null;

    int burbujaCommonAnimationIndex = 0;

    Personaje personaje1;
    //Personaje personaje2;

    Celda celdas[][] = new Celda[MAX_WIDTH][MAX_HEIGHT];

    boolean initFullTablero = true;
    boolean tableroInitialized = false;
    boolean tableroEstable = false;
    long tableroEstableTime = 0;
    long lastInitialBurbujaTime = 0;
    boolean tableroLlenoBurbujas = false;
    boolean tableroLlenoObjetos = false;

    Objeto[] objetos_animados = new Objeto[MAX_OBJETOS_ANIMADOS];

    int[] objetos_explotados = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    long timeEntreBurbujas = DEFAULT_TIME_ENTRE_BURBUJAS;

    int lastLevel = 0;

    /**
     * Constructors
     */

    public Tablero(Resources resources, Personaje personaje1, Personaje personaje2) {

        this.resources = resources;

        this.personaje1 = personaje1;
        //this.personaje2 = personaje2;

        BurbujaResources.initializeGraphics(resources);

        //Log.d(TAG, "Graficos Burbujas inicializados");

        ExplosionResources.initializeGraphics(resources);

        //Log.d(TAG, "Graficos Explosiones inicializados");

        ObjetoResources.initializeGraphics(resources);

        //Log.d(TAG, "Graficos Objetos inicializados");

        EfectoResources.initializeGraphics(resources);

        //Log.d(TAG, "Graficos Efectos inicializados");

        //initTablero();

    }

    /**
     * Reescala las celdas del tablero en funcin de los tamaos de pantalla indicados
     *
     * @param canvasWidth
     * @param canvasHeight
     */
    public static void resizeTablero(int surfaceWidth, int surfaceHeight) {

        //Log.d(TAG, "Surface resize: width = " + surfaceWidth + ", height = " + surfaceHeight);

        // Nos quedamos con la proporcion mas pequea ( El tablero siempre ser cuadrado )
        float refactorIndex = 1;
        float indexWidth = surfaceWidth;
        indexWidth /= DEFAULT_TABLERO_WIDTH;
        float indexHeight = surfaceHeight;
        indexHeight /= DEFAULT_TABLERO_HEIGHT;
        if (indexWidth > indexHeight) {
            TABLERO_WIDTH = surfaceHeight;
            TABLERO_HEIGHT = surfaceHeight;
            refactorIndex = indexHeight;
        } else {
            TABLERO_WIDTH = surfaceWidth;
            TABLERO_HEIGHT = surfaceWidth;
            refactorIndex = indexWidth;
        }

        //Log.d(TAG, "Surface resize: TABLERO_WIDTH = " + TABLERO_WIDTH + ", TABLERO_HEIGHT = " + TABLERO_HEIGHT + ", refactor index = " + refactorIndex);

        BurbujaResources.resizeGraphics(refactorIndex);

        //Log.d(TAG, "Graficos Burbujas resizados");

        ExplosionResources.resizeGraphics(refactorIndex);

        //Log.d(TAG, "Graficos Explosiones resizados");

        ObjetoResources.resizeGraphics(refactorIndex);

        //Log.d(TAG, "Graficos Objetos resizados");

        EfectoResources.resizeGraphics(refactorIndex);

        //Log.d(TAG, "Graficos Efectos resizados");

        // Establecemos offsets para centrar el tablero en la pantalla
        float offsetWidth = surfaceWidth;
        offsetWidth -= BurbujaResources.BURBUJA_WIDTH * Tablero.MAX_WIDTH;
        offsetWidth /= 2;
        OFFSET_X = Float.valueOf(offsetWidth).intValue();
        float offsetHeight = surfaceHeight;
        offsetHeight -= BurbujaResources.BURBUJA_HEIGHT * Tablero.MAX_HEIGHT;
        offsetHeight /= 2;
        OFFSET_Y = Float.valueOf(offsetHeight).intValue();

        // Recolocamos contadores
        calculaContadoresOffsets(surfaceWidth, surfaceHeight);

        //Log.d(TAG, "Surface resize: OFFSET_X = " + OFFSET_X + ", OFFSET_Y = " + OFFSET_Y );

    }

    protected static void calculaContadoresOffsets(int surfaceWidth, int surfaceHeight) {

        // Calculamos el refactor indexes
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

        COUNTER_Y_SPACER = Double.valueOf(1.2 * ObjetoResources.OBJETO_HEIGHT).intValue();
        OFFSET_X_LIKE_COUNTER = OFFSET_X_LIKE + ObjetoResources.OBJETO_WIDTH;
        OFFSET_Y_LIKE_COUNTER = OFFSET_Y_LIKE + Double.valueOf(ObjetoResources.OBJETO_HEIGHT / 2).intValue();
        OFFSET_X_DISLIKE_COUNTER = OFFSET_X_DISLIKE + ObjetoResources.OBJETO_WIDTH;
        OFFSET_Y_DISLIKE_COUNTER = OFFSET_Y_DISLIKE + Double.valueOf(ObjetoResources.OBJETO_HEIGHT / 2).intValue();

    }

    /**
     * Inicializa el tablero
     */
    public void initTablero() {

        this.celdas = new Celda[MAX_WIDTH][MAX_HEIGHT];

        // Localizing external variables
        Celda[][] celdas = this.celdas;
        boolean initFullTablero = this.initFullTablero;

        int BURBUJA_WIDTH = BurbujaResources.BURBUJA_WIDTH;
        int BURBUJA_HEIGHT = BurbujaResources.BURBUJA_HEIGHT;
        int OFFSET_X = Tablero.OFFSET_X;
        int OFFSET_Y = Tablero.OFFSET_Y;
        int MAX_WIDTH = Tablero.MAX_WIDTH;
        int MAX_HEIGHT = Tablero.MAX_HEIGHT;

        for (int i = 0; i < MAX_WIDTH; i++) {

            for (int j = 0; j < MAX_HEIGHT; j++) {

                Celda celda = new Celda(i, j);

                // Condicion de inicializar tablero lleno de burbujas
                if (initFullTablero) {

                    Burbuja burbuja = new Burbuja();

                    burbuja.drawX = OFFSET_X + (i * BURBUJA_WIDTH);
                    burbuja.drawY = OFFSET_Y + (j * BURBUJA_HEIGHT);

                    celda.burbuja = burbuja;

                    addRandomObject(celda);

                    // Comprobamos que el objeto no pete nada
                    if (i >= 2 && celda.objeto != null
                            && celdas[i - 1][j].objeto != null && celdas[i - 1][j].objeto.type == celda.objeto.type
                            && celdas[i - 2][j].objeto != null && celdas[i - 2][j].objeto.type == celda.objeto.type) {

                        celda.objeto = null;
                    }
                    if (j >= 2 && celda.objeto != null
                            && celdas[i][j - 1].objeto != null && celdas[i][j - 1].objeto.type == celda.objeto.type
                            && celdas[i][j - 2].objeto != null && celdas[i][j - 2].objeto.type == celda.objeto.type) {

                        celda.objeto = null;
                    }

                }

                celdas[i][j] = celda;

            }

        }

        tableroEstable = true;
        tableroEstableTime = System.currentTimeMillis();

        // Inicializamos marcadores
        objetos_explotados = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        objetos_animados = new Objeto[MAX_OBJETOS_ANIMADOS];

        tableroInitialized = true;

        // Resto variables
        timeEntreBurbujas = DEFAULT_TIME_ENTRE_BURBUJAS;
        lastLevel = 0;

        //Log.d(TAG, "Tablero inicializado");

    }

    public Celda getCelda(int i, int j) {
        if (i < 0 || i >= MAX_WIDTH || j < 0 || j >= MAX_HEIGHT) {
            return null;
        }
        return celdas[i][j];
    }

    public void doTableroAnimation(Canvas canvas) {

        if (canvas == null || !tableroInitialized) {
            return;
        }

        // utilizamos un indice de animacin de burbujas comn
        this.burbujaCommonAnimationIndex++;
        // Hacemos loop
        if (this.burbujaCommonAnimationIndex >= BurbujaResources.BURBUJA_ANIMATION_SEQUENCE.length) {
            this.burbujaCommonAnimationIndex = 0;
        }

        // Localizing external variables
        Celda[][] celdas = this.celdas;
        int OFFSET_X = Tablero.OFFSET_X;
        int OFFSET_Y = Tablero.OFFSET_Y;
        int MAX_WIDTH = Tablero.MAX_WIDTH;
        int MAX_HEIGHT = Tablero.MAX_HEIGHT;
        boolean tableroEstable = this.tableroEstable;

        // Localizing external variables
        int BURBUJA_WIDTH = BurbujaResources.BURBUJA_WIDTH;
        int BURBUJA_HEIGHT = BurbujaResources.BURBUJA_HEIGHT;
		/* Save memory
		int BURBUJA_STATUS_GRAPHICS_SIZE = BurbujaResources.BURBUJA_STATUS_GRAPHICS_SIZE;
		*/
        int BURBUJA_UNION_UP = BurbujaResources.BURBUJA_UNION_UP;
        int BURBUJA_UNION_LEFT = BurbujaResources.BURBUJA_UNION_LEFT;
        int BURBUJA_UNION_RIGHT = BurbujaResources.BURBUJA_UNION_RIGHT;
        int BURBUJA_UNION_DOWN = BurbujaResources.BURBUJA_UNION_DOWN;
        int BURBUJA_UNION_NONE = BurbujaResources.BURBUJA_UNION_NONE;
        int BURBUJA_MOVE_UP = BurbujaResources.BURBUJA_MOVE_UP;
        int BURBUJA_MOVE_LEFT = BurbujaResources.BURBUJA_MOVE_LEFT;
        int BURBUJA_MOVE_RIGHT = BurbujaResources.BURBUJA_MOVE_RIGHT;
        int BURBUJA_MOVE_DOWN = BurbujaResources.BURBUJA_MOVE_DOWN;
        int BURBUJA_MOVE_NONE = BurbujaResources.BURBUJA_MOVE_NONE;
        Bitmap[] BURBUJA_GRAPHICS_BITMAP = BurbujaResources.BURBUJA_GRAPHICS_BITMAP;
        Bitmap[] BURBUJA_MOVE_GRAPHICS_BITMAP = BurbujaResources.BURBUJA_MOVE_GRAPHICS_BITMAP;
		/* Save memory
		Bitmap[] BURBUJA_UNION_GRAPHICS_BITMAP = BurbujaResources.BURBUJA_UNION_GRAPHICS_BITMAP;
		Bitmap[] BURBUJA_STATUS_GRAPHICS_BITMAP = BurbujaResources.BURBUJA_STATUS_GRAPHICS_BITMAP;
		*/

        int EXPLOSION_GRAPHICS_SIZE = ExplosionResources.EXPLOSION_GRAPHICS_SIZE;
        Bitmap[] EXPLOSION_GRAPHICS_BITMAP = ExplosionResources.EXPLOSION_GRAPHICS_BITMAP;

        int OBJETO_ESTADO_BURBUJA = ObjetoResources.OBJETO_ESTADO_BURBUJA;
        int OBJETO_ESTADO_MOVIDO = ObjetoResources.OBJETO_ESTADO_MOVIDO;
        int OBJETO_ESTADO_EXPLOTADO = ObjetoResources.OBJETO_ESTADO_EXPLOTADO;
        int CARAMELO_OBJECT_TYPE = ObjetoResources.CARAMELO_OBJECT_TYPE;
        Bitmap[] CARAMELO_GRAPHICS_BITMAP = ObjetoResources.CARAMELO_GRAPHICS_BITMAP;
        Bitmap[] CARAMELO_GRANDE_GRAPHICS_BITMAP = ObjetoResources.CARAMELO_GRANDE_GRAPHICS_BITMAP;
        int PIRULETA_OBJECT_TYPE = ObjetoResources.PIRULETA_OBJECT_TYPE;
        Bitmap[] PIRULETA_GRAPHICS_BITMAP = ObjetoResources.PIRULETA_GRAPHICS_BITMAP;
        Bitmap[] PIRULETA_GRANDE_GRAPHICS_BITMAP = ObjetoResources.PIRULETA_GRANDE_GRAPHICS_BITMAP;
        int RASPA_OBJECT_TYPE = ObjetoResources.RASPA_OBJECT_TYPE;
        Bitmap[] RASPA_GRAPHICS_BITMAP = ObjetoResources.RASPA_GRAPHICS_BITMAP;
        Bitmap[] RASPA_GRANDE_GRAPHICS_BITMAP = ObjetoResources.RASPA_GRANDE_GRAPHICS_BITMAP;
        int PEINE_OBJECT_TYPE = ObjetoResources.PEINE_OBJECT_TYPE;
        Bitmap[] PEINE_GRAPHICS_BITMAP = ObjetoResources.PEINE_GRAPHICS_BITMAP;
        Bitmap[] PEINE_GRANDE_GRAPHICS_BITMAP = ObjetoResources.PEINE_GRANDE_GRAPHICS_BITMAP;

        boolean tableroLlenoBurbujas = true;
        boolean tableroLlenoObjetos = true;
        boolean hayMovimiento = false;
        for (int i = 0; i < MAX_WIDTH; i++) {

            for (int j = 0; j < MAX_HEIGHT; j++) {

                Celda celda = celdas[i][j];
                if (celda == null) continue;

                // Marca para no processar la celda ( solo 1 vez )
                if (celda.ignore) {
                    celda.ignore = false;
                    continue;
                }

                Objeto objeto = celda.objeto;
                if (objeto == null) {
                    tableroLlenoObjetos = false;
                }

                Burbuja burbuja = celda.burbuja;
                if (burbuja == null) {
                    tableroLlenoBurbujas = false;
                } else {
                    updateBurbujaAnimation(burbuja, objeto);

                    /**
                     * Update burbuja position
                     */

                    // Si la burbuja esta centrada verticalmente
                    // Nota: no se como esto esta as, pero si lo tocas se descuaringa todo
                    int burbujaCeldaPosX = OFFSET_X + (i * BURBUJA_HEIGHT);
                    if (burbuja.drawX == burbujaCeldaPosX && canBurbujaMoveUp(celdas, burbuja, i, j)) {

                        moveAndUpdateBurbujaPosition(celdas, burbuja, objeto, i, j, BURBUJA_MOVE_UP, false);
                        // indicamos que hay movimiento de burbujas
                        hayMovimiento = true;
                        // Marcamos burbuja como que se mueve
                        burbuja.move = BURBUJA_MOVE_UP;
	            		
/* amarinji: deshabilito la logica de movimientos laterales automaticos de las burbujas, ya que hace cosas raras y tiene bugs que no se como solucionar 
		            // Miramos esto solo si la burbuja no se est moviendo o se esta moviendo en la direccion adecuada
		            } else if ( ( burbuja.move == BURBUJA_MOVE_NONE || burbuja.move == BURBUJA_MOVE_LEFT ) && canBurbujaMoveLeft( celdas, burbuja, i, j ) ) {
			            	
		            	moveAndUpdateBurbujaPosition( celdas, burbuja, objeto, i, j, BURBUJA_MOVE_LEFT, false );
		            	// indicamos que hay movimiento de burbujas
		            	hayMovimiento = true;
	            		// Marcamos burbuja como que se mueve
	            		burbuja.move = BURBUJA_MOVE_LEFT;
			        
		            // Miramos esto solo si la burbuja no se est moviendo o se esta moviendo en la direccion adecuada
		            } else if ( ( burbuja.move == BURBUJA_MOVE_NONE || burbuja.move == BURBUJA_MOVE_RIGHT ) && canBurbujaMoveRight( celdas, burbuja, i, j ) ) {
				        // Ojo! por aqu hay un bug; no te deja mover la burbuja hacia una posicin donde la burbuja puede caer a la derecha
		            	moveAndUpdateBurbujaPosition( celdas, burbuja, objeto, i, j, BURBUJA_MOVE_RIGHT, true );
		            	// indicamos que hay movimiento de burbujas
		            	hayMovimiento = true;
	            		// Marcamos burbuja como que se mueve
	            		burbuja.move = BURBUJA_MOVE_RIGHT;
*/

                    } else {
                        // Centramos la burbuja en su celda si no lo est
                        int burbujaCeldaPosY = OFFSET_Y + (j * BURBUJA_HEIGHT);
                        if (burbuja.drawY != burbujaCeldaPosY || burbuja.drawX != burbujaCeldaPosX) {
                            centraBurbujaEnPosicion(burbuja, burbujaCeldaPosX, burbujaCeldaPosY);
                            // indicamos que hay movimiento de burbujas
                            hayMovimiento = true;
                        } else {
                            // Marcamos burbuja como que no se mueve
                            burbuja.move = BURBUJA_MOVE_NONE;
                        }

                    }
		            
		            /* comento lo de los fondos
		            // Fondo burbuja
		            if ( burbuja.status < BURBUJA_STATUS_GRAPHICS_SIZE ) {
		            	canvas.drawBitmap(BURBUJA_STATUS_GRAPHICS_BITMAP[burbuja.status], burbuja.drawX, burbuja.drawY, null);
		            }
		            */

                    if (objeto != null && (objeto.status == OBJETO_ESTADO_BURBUJA || objeto.status == OBJETO_ESTADO_MOVIDO)) {

                        Bitmap bitmap = null;

                        int type = objeto.type;
                        if (type == CARAMELO_OBJECT_TYPE) {
                            bitmap = CARAMELO_GRAPHICS_BITMAP[objeto.graphicIndex];
                        } else if (type == PIRULETA_OBJECT_TYPE) {
                            bitmap = PIRULETA_GRAPHICS_BITMAP[objeto.graphicIndex];
                        } else if (type == RASPA_OBJECT_TYPE) {
                            bitmap = RASPA_GRAPHICS_BITMAP[objeto.graphicIndex];
                        } else if (type == PEINE_OBJECT_TYPE) {
                            bitmap = PEINE_GRAPHICS_BITMAP[objeto.graphicIndex];
                        }

                        if (objeto.status == OBJETO_ESTADO_MOVIDO) {
                            // Movemos burbuja hasta su destino
                            if (mueveObjetoEntreBurbujas(objeto)) {
                                // Si el objeto est ubicado reseteamos estados union de las burbujas que intervienen
                                objeto.status = OBJETO_ESTADO_BURBUJA;
                                if (burbuja.union == BURBUJA_UNION_UP) {
                                    celdas[i][j + 1].burbuja.union = BURBUJA_UNION_NONE;
                                } else if (burbuja.union == BURBUJA_UNION_DOWN) {
                                    celdas[i][j - 1].burbuja.union = BURBUJA_UNION_NONE;
                                } else if (burbuja.union == BURBUJA_UNION_RIGHT) {
                                    celdas[i + 1][j].burbuja.union = BURBUJA_UNION_NONE;
                                } else if (burbuja.union == BURBUJA_UNION_LEFT) {
                                    celdas[i - 1][j].burbuja.union = BURBUJA_UNION_NONE;
                                }
                                burbuja.union = BURBUJA_UNION_NONE;
                            }
                            // indicamos que hay movimiento de burbujas
                            hayMovimiento = true;

                        } else {

                            // Sincronizamos posiciones de objeto y burbuja
                            if (objeto.drawX != burbuja.drawX) {
                                objeto.drawX = burbuja.drawX;
                            }
                            if (objeto.drawY != burbuja.drawY) {
                                objeto.drawY = burbuja.drawY;
                            }

                        }

                        // Anima objeto
                        canvas.drawBitmap(bitmap, objeto.drawX, objeto.drawY, null);

                    }

                    Bitmap bitmap = null;


                    // Caso burbuja en movimiento
                    if (burbuja.move != BURBUJA_MOVE_NONE) {
                        bitmap = BURBUJA_MOVE_GRAPHICS_BITMAP[burbuja.graphicIndex];

                        // Caso burbuja quieta
                    } else {
            			/* Save memory
                		int union = burbuja.union;
                		// Caso burbuja unida
                		if ( union != BURBUJA_UNION_NONE ) {
                			bitmap = BURBUJA_UNION_GRAPHICS_BITMAP[union];            			
	            		// Caso burbuja quieta y sin unir
	            		} else {
	            			bitmap = BURBUJA_GRAPHICS_BITMAP[burbuja.graphicIndex];
	            		}
	            		*/
                        bitmap = BURBUJA_GRAPHICS_BITMAP[burbuja.graphicIndex];
                    }

                    // Anima burbuja
                    canvas.drawBitmap(bitmap, burbuja.drawX, burbuja.drawY, null);
                }

                Explosion explosion = celda.explosion;
                if (explosion != null) {

                    // cambiamos el indice de animacin
                    explosion.graphicIndex++;

                    // Caso burbuja ha acabado de explotar
                    if (explosion.graphicIndex >= EXPLOSION_GRAPHICS_SIZE) {

                        celda.explosion = null;

                        if (objeto != null && objeto.status == OBJETO_ESTADO_EXPLOTADO) {
                            objetos_explotados[objeto.type]++;
                            celda.objeto = null;

                        }
	                	/* amarinji: comento explosion en cadena de burbujas vacias
	                	// Caso burbuja eliminada
	                	else {
	            		
		                	// Miramos si hay explosion en cadena de burbujas sin objetos
				            if ( i >= 1 && objeto == null 
				            		&& celdas[i-1][j].burbuja != null && celdas[i-1][j].objeto == null ) {
				            	
				            	eliminaBurbuja( celdas[i-1][j] );
				            	hayMovimiento = true;
				            	
				            }
				            if ( i < (MAX_WIDTH-1) && objeto == null 
				            		&& celdas[i+1][j].burbuja != null && celdas[i+1][j].objeto == null ) {
				            	
				            	eliminaBurbuja( celdas[i+1][j] );
				            	hayMovimiento = true;
				            	
				            }
				            if ( j >= 1 && objeto == null 
				            		&& celdas[i][j-1].burbuja != null && celdas[i][j-1].objeto == null ) {
				            	
				            	eliminaBurbuja( celdas[i][j-1] );
				            	hayMovimiento = true;
				            	
				            }
				            if ( j < (MAX_WIDTH-1) && objeto == null 
				            		&& celdas[i][j+1].burbuja != null && celdas[i][j+1].objeto == null ) {
				            	
				            	eliminaBurbuja( celdas[i][j+1] );
				            	hayMovimiento = true;
				            	
				            }
	                	
	                	} */
	                	
	                	/* amarinji: comento caso  de una burbuja vacia afecta al estado de las de alrededor
	                	// Caso la explosion de una burbuja vacia afecta al estado de las de alrededor
	                	else {
				            if ( i > 0 ) {
				            	actualizaEstadoBurbuja( celdas[i-1][j] );
				            }
				            if ( i < (MAX_WIDTH-1) ) {
				            	actualizaEstadoBurbuja( celdas[i+1][j] );
				            }
				            if ( j > 0 ) {
				            	actualizaEstadoBurbuja( celdas[i][j-1] );
				            }
				            if ( j < (MAX_WIDTH-1) ) {
				            	actualizaEstadoBurbuja( celdas[i][j+1] );
				            }
	                	}
	            		*/

                        // Caso animamos la burbuja explotando
                    } else {

                        // Anima explosion
                        canvas.drawBitmap(EXPLOSION_GRAPHICS_BITMAP[explosion.graphicIndex], explosion.drawX, explosion.drawY, null);

                        // Anima objeto grande
                        if (objeto != null && objeto.status == OBJETO_ESTADO_EXPLOTADO) {

                            Bitmap bitmap = null;

                            int type = objeto.type;
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

                        // indicamos que hay movimiento en el tablero
                        hayMovimiento = true;

                    }

                }

                // ahora no hace falta que el tablero est estable, solo que las burbujas no se muevan
                //if ( tableroEstable ) {

                /**
                 * Logica matcheado horizontal de objetos
                 */

                // Miramos si la burbuja tiene objeto
                if (i < (MAX_WIDTH - 2) && burbuja != null && burbuja.move == BURBUJA_MOVE_NONE && objeto != null && objeto.status == OBJETO_ESTADO_BURBUJA) {
                    // Miramos si la burbuja de la derecha tiene el mismo objeto
                    int objetoType = objeto.type;
                    Celda celdaRight1 = celdas[i + 1][j];
                    if (celdaRight1 != null && celdaRight1.burbuja != null && celdaRight1.burbuja.move == BURBUJA_MOVE_NONE
                            && celdaRight1.objeto != null && celdaRight1.objeto.type == objetoType && celdaRight1.objeto.status == OBJETO_ESTADO_BURBUJA) {
                        // Miramos si la burbuja de mas a la derecha tiene el mismo objeto
                        Celda celdaRight2 = celdas[i + 2][j];
                        if (celdaRight2 != null && celdaRight2.burbuja != null && celdaRight2.burbuja.move == BURBUJA_MOVE_NONE
                                && celdaRight2.objeto != null && celdaRight2.objeto.type == objetoType && celdaRight2.objeto.status == OBJETO_ESTADO_BURBUJA) {
                            // Explotamos burbujas
                            explotaBurbuja(celda);
                            explotaBurbuja(celdaRight1);
                            explotaBurbuja(celdaRight2);
                            // variable para contabilizar objetos explotados
                            int numObjetos = 3;
                            // ignoramos las celdas de la derecha para evitar efecto raro
                            celdaRight1.ignore = true;
                            celdaRight2.ignore = true;
                            // Si hay mas burbujas con el mismo objeto las explotamos tambien
                            for (int k = i + 3; k < MAX_WIDTH; k++) {
                                Celda celdaRight = celdas[k][j];
                                if (celdaRight != null && celdaRight.burbuja != null && celdaRight.burbuja.move == BURBUJA_MOVE_NONE
                                        && celdaRight.objeto != null && celdaRight.objeto.type == objetoType && celdaRight.objeto.status == OBJETO_ESTADO_BURBUJA) {
                                    explotaBurbuja(celdaRight);
                                    numObjetos++;
                                    // ignoramos las celdas de la derecha para evitar efecto raro
                                    celdaRight.ignore = true;
                                } else {
                                    // En el momento en que no encontremos una burbuja a explotar salimos del for
                                    break;
                                }
                            }
                            // Miramos tambien a la izquierda por si acaso
                            for (int k = i - 1; k >= 0; k--) {
                                Celda celdaLeft = celdas[k][j];
                                if (celdaLeft != null && celdaLeft.burbuja != null && celdaLeft.burbuja.move == BURBUJA_MOVE_NONE
                                        && celdaLeft.objeto != null && celdaLeft.objeto.type == objetoType && celdaLeft.objeto.status == OBJETO_ESTADO_BURBUJA) {
                                    explotaBurbuja(celdaLeft);
                                    numObjetos++;
                                } else {
                                    // En el momento en que no encontremos una burbuja a explotar salimos del for
                                    break;
                                }
                            }
                            // Marcamos como que hay movimiento
                            hayMovimiento = true;
                            // Actualizamos meters de jugadores
                            this.personaje1.updateMeter(objetoType, numObjetos);
                        }
                    }
                }

                /**
                 * Logica matcheado vertical de objetos
                 */

                // Miramos si la burbuja tiene objeto
                if (j < (MAX_HEIGHT - 2) && burbuja != null && burbuja.move == BURBUJA_MOVE_NONE && objeto != null && objeto.status == OBJETO_ESTADO_BURBUJA) {
                    // Miramos si la burbuja de la arriba tiene el mismo objeto
                    int objetoType = objeto.type;
                    Celda celdaDown1 = celdas[i][j + 1];
                    if (celdaDown1 != null && celdaDown1.burbuja != null && celdaDown1.burbuja.move == BURBUJA_MOVE_NONE
                            && celdaDown1.objeto != null && celdaDown1.objeto.type == objetoType && celdaDown1.objeto.status == OBJETO_ESTADO_BURBUJA) {
                        // Miramos si la burbuja de mas a la arriba tiene el mismo objeto
                        Celda celdaDown2 = celdas[i][j + 2];
                        if (celdaDown2 != null && celdaDown2.burbuja != null && celdaDown2.burbuja.move == BURBUJA_MOVE_NONE
                                && celdaDown2.objeto != null && celdaDown2.objeto.type == objetoType && celdaDown2.objeto.status == OBJETO_ESTADO_BURBUJA) {
                            // Explotamos burbujas
                            explotaBurbuja(celda);
                            explotaBurbuja(celdaDown1);
                            explotaBurbuja(celdaDown2);
                            // variable para contabilizar objetos explotados
                            int numObjetos = 3;
                            // ignoramos las celdas de abajo para evitar efectos raros
                            celdaDown1.ignore = true;
                            celdaDown2.ignore = true;
                            // Si hay mas burbujas con el mismo objeto las explotamos tambien
                            for (int k = j + 3; k < MAX_HEIGHT; k++) {
                                Celda celdaDown = celdas[i][k];
                                if (celdaDown != null && celdaDown.burbuja != null && celdaDown.burbuja.move == BURBUJA_MOVE_NONE
                                        && celdaDown.objeto != null && celdaDown.objeto.type == objetoType && celdaDown.objeto.status == OBJETO_ESTADO_BURBUJA) {
                                    explotaBurbuja(celdaDown);
                                    numObjetos++;
                                    // ignoramos las celdas de abajo para evitar efectos raros
                                    celdaDown.ignore = true;
                                } else {
                                    // En el momento en que no encontremos una burbuja a explotar salimos del for
                                    break;
                                }
                            }
                            // Miramos tambien abajo por si acaso
                            for (int k = j - 1; k >= 0; k--) {
                                Celda celdaDown = celdas[i][k];
                                if (celdaDown != null && celdaDown.burbuja != null && celdaDown.burbuja.move == BURBUJA_MOVE_NONE
                                        && celdaDown.objeto != null && celdaDown.objeto.type == objetoType && celdaDown.objeto.status == OBJETO_ESTADO_BURBUJA) {
                                    explotaBurbuja(celdaDown);
                                    numObjetos++;
                                } else {
                                    // En el momento en que no encontremos una burbuja a explotar salimos del for
                                    break;
                                }
                            }
                            // Marcamos como que hay movimiento
                            hayMovimiento = true;
                            // Actualizamos meters de jugadores
                            this.personaje1.updateMeter(objetoType, numObjetos);
                        }
                    }
                }

                //}

            }

        }

        // Control de movimientos en el tablero
        if (hayMovimiento) {
            this.tableroEstable = false;
        } else {
            // Solo actualizamos a true el flag una vez para no falsear tableroEstableTime
            if (!this.tableroEstable) {
                this.tableroEstable = true;
                this.tableroEstableTime = System.currentTimeMillis();
            }
        }

        // Tablero lleno de burbujas
        this.tableroLlenoObjetos = tableroLlenoObjetos;
        this.tableroLlenoBurbujas = tableroLlenoBurbujas;

        /**
         *  Animacin de otros objetos
         */

        animateObjectosIndependientes(canvas);


        /**
         *  Dibujamos contadores
         */

        //LIKE
        int offset_x_like = OFFSET_X_LIKE;
        int offset_y_like = OFFSET_Y_LIKE;
        int offset_x_like_counter = OFFSET_X_LIKE_COUNTER;
        int offset_y_like_counter = OFFSET_Y_LIKE_COUNTER;

        canvas.drawBitmap(CARAMELO_GRAPHICS_BITMAP[0], offset_x_like, offset_y_like, null);
        canvas.drawText(" = " + objetos_explotados[CARAMELO_OBJECT_TYPE], offset_x_like_counter, offset_y_like_counter, ColiPopResources.paint);

        offset_y_like += COUNTER_Y_SPACER;
        offset_y_like_counter += COUNTER_Y_SPACER;

        canvas.drawBitmap(PIRULETA_GRAPHICS_BITMAP[0], offset_x_like, offset_y_like, null);
        canvas.drawText(" = " + objetos_explotados[PIRULETA_OBJECT_TYPE], offset_x_like_counter, offset_y_like_counter, ColiPopResources.paint);

        // DISLIKE
        int offset_x_dislike = OFFSET_X_DISLIKE;
        int offset_y_dislike = OFFSET_Y_DISLIKE;
        int offset_x_dislike_counter = OFFSET_X_DISLIKE_COUNTER;
        int offset_y_dislike_counter = OFFSET_Y_DISLIKE_COUNTER;

        canvas.drawBitmap(RASPA_GRAPHICS_BITMAP[0], offset_x_dislike, offset_y_dislike, null);
        canvas.drawText(" = " + objetos_explotados[RASPA_OBJECT_TYPE], offset_x_dislike_counter, offset_y_dislike_counter, ColiPopResources.paint);

        offset_y_dislike += COUNTER_Y_SPACER;
        offset_y_dislike_counter += COUNTER_Y_SPACER;

        canvas.drawBitmap(PEINE_GRAPHICS_BITMAP[0], offset_x_dislike, offset_y_dislike, null);
        canvas.drawText(" = " + objetos_explotados[PEINE_OBJECT_TYPE], offset_x_dislike_counter, offset_y_dislike_counter, ColiPopResources.paint);

    }

    /**
     * Actualiza estado de la burbuja en la celda.
     * Si el estado pasa del BURBUJA_ESTADO_ROJO la elimina
     */
    void actualizaEstadoBurbuja(Celda celda) {
        if (celda == null || celda.burbuja == null) {
            return;
        }
        celda.burbuja.status++;
        if (celda.burbuja.status > BurbujaResources.BURBUJA_ESTADO_ROJO) {
            eliminaBurbuja(celda);
        }
    }

    /**
     * Actualiza el grafico de animacin de la burbuja
     *
     * @param burbuja
     */
    void updateBurbujaAnimation(Burbuja burbuja, Objeto objeto) {

        if (burbuja.move == BurbujaResources.BURBUJA_MOVE_NONE) {
            // Todas las burbujas que estn quietas utilizan el mismo indice
            burbuja.graphicIndex = BurbujaResources.BURBUJA_ANIMATION_SEQUENCE[this.burbujaCommonAnimationIndex];
            if (burbuja.graphicIndex >= BurbujaResources.BURBUJA_GRAPHICS_SIZE) {
                burbuja.graphicIndex = 0;
            }
        } else {
            // Las burbujas en movimento van de forma independiente
            burbuja.animationIndex++;
            // Hacemos loop
            if (burbuja.animationIndex >= BurbujaResources.BURBUJA_MOVE_ANIMATION_SEQUENCE.length) {
                burbuja.animationIndex = 0;
            }
            burbuja.graphicIndex = BurbujaResources.BURBUJA_MOVE_ANIMATION_SEQUENCE[this.burbujaCommonAnimationIndex];
            if (burbuja.graphicIndex >= BurbujaResources.BURBUJA_MOVE_GRAPHICS_SIZE) {
                burbuja.graphicIndex = 0;
            }
        }

        if (objeto != null) {

            // utilizamos un indice de animacin comun
            objeto.animationIndex++;
            // Hacemos loop
            if (objeto.animationIndex >= ObjetoResources.OBJETO_ANIMATION_SEQUENCE.length) {
                objeto.animationIndex = 0;
            }
            objeto.graphicIndex = ObjetoResources.OBJETO_ANIMATION_SEQUENCE[objeto.animationIndex];
            if (objeto.graphicIndex >= ObjetoResources.OBJETO_GRAPHICS_SIZE) {
                objeto.graphicIndex = 0;
            }

        }

    }

    /**
     * Indica si una burbuja puede moverse hacia arriba
     *
     * @param burbuja
     * @return
     */
    boolean canBurbujaMoveUp(Celda[][] celdas, Burbuja burbuja, int i, int j) {
        if (burbuja == null) {
            // TODO: revisar esto
            return false;
        }
        if (j == 0) {
            // Esta arriba del todo, no se puede mover mas
            return false;
        }
        Celda upCelda = celdas[i][j - 1];
        if (upCelda == null || (upCelda.burbuja == null && upCelda.explosion == null)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Indica si una burbuja puede moverse hacia abajo
     *
     * @param burbuja
     * @return
     */
    boolean canBurbujaMoveDown(Celda[][] celdas, Burbuja burbuja, int i, int j) {
        if (burbuja == null) {
            // TODO: revisar esto
            return false;
        }
        if (j == (MAX_HEIGHT - 1)) {
            // Esta arriba del todo, no se puede mover mas
            return false;
        }
        Celda downCelda = celdas[i][j + 1];
        if (downCelda == null || (downCelda.burbuja == null && downCelda.explosion == null)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Indica si una burbuja puede moverse hacia la derecha
     *
     * @param burbuja
     * @return
     */
    boolean canBurbujaMoveRight(Celda[][] celdas, Burbuja burbuja, int i, int j) {
        if (burbuja == null) {
            // TODO: revisar esto
            return false;
        }
        // Miramos que hacia la derecha haya sitio para moverse
        if (i == (MAX_WIDTH - 1)) {
            // Esta a la derecha del todo, no se puede mover mas
            return false;
        }
        Celda rightCelda = celdas[i + 1][j];
        if (rightCelda == null || (rightCelda.burbuja == null && rightCelda.explosion == null)) {
            // Miramos que hacia arriba a la derecha haya un hueco donde meterse
            if (j == 0) {
                // No puede haber hueco ya que esta arriba del todo
                return false;
            }
            Celda rightUpCelda = celdas[i + 1][j - 1];
            if (rightUpCelda == null || (rightUpCelda.burbuja == null && rightUpCelda.explosion == null)) {
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

    /**
     * Indica si una burbuja puede moverse hacia la izquierda
     *
     * @param burbuja
     * @return
     */
    boolean canBurbujaMoveLeft(Celda[][] celdas, Burbuja burbuja, int i, int j) {
        if (burbuja == null) {
            // TODO: revisar esto
            return false;
        }
        // Miramos que hacia la izquierda haya sitio para moverse
        if (i == 0) {
            // Esta a la izquierda del todo, no se puede mover mas
            return false;
        }
        Celda leftCelda = celdas[i - 1][j];
        if (leftCelda == null || (leftCelda.burbuja == null && leftCelda.explosion == null)) {
            // Miramos que hacia arriba a la izquierda haya un hueco donde meterse
            if (j == 0) {
                // No puede haber hueco ya que esta arriba del todo
                return false;
            }
            Celda leftUpCelda = celdas[i - 1][j - 1];
            if (leftUpCelda == null || (leftUpCelda.burbuja == null && leftUpCelda.explosion == null)) {
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

    /**
     * Centra la burbuja a la posicion indicada.
     * Mueve la burbuja BURBUJA_PIXEL_MOVE pixels como mximo.
     *
     * @param burbuja
     * @param x
     * @param y
     */
    void centraBurbujaEnPosicion(Burbuja burbuja, int x, int y) {

        if (burbuja == null) {
            return;
        }

        // Localizing external variables
        int BURBUJA_PIXEL_MOVE = BurbujaResources.BURBUJA_PIXEL_MOVE;

        boolean finX = false;
        int drawX = burbuja.drawX;
        if (x == burbuja.drawX) {
            finX = true;

        } else if (x > burbuja.drawX) {
            drawX += BURBUJA_PIXEL_MOVE;
            if ((x - drawX) < BURBUJA_PIXEL_MOVE) {
                finX = true;
            } else {
                burbuja.drawX = drawX;
            }

        } else if (x < burbuja.drawX) {
            drawX -= BURBUJA_PIXEL_MOVE;
            if ((burbuja.drawX - x) < BURBUJA_PIXEL_MOVE) {
                finX = true;
            } else {
                burbuja.drawX = drawX;
            }
        }
        boolean finY = false;
        int drawY = burbuja.drawY;
        if (y == burbuja.drawY) {
            finY = true;

        } else if (y > burbuja.drawY) {
            drawY += BURBUJA_PIXEL_MOVE;
            if ((y - burbuja.drawY) < BURBUJA_PIXEL_MOVE) {
                finY = true;
            } else {
                burbuja.drawY = drawY;
            }

        } else if (y < burbuja.drawY) {
            drawY -= BURBUJA_PIXEL_MOVE;
            if ((burbuja.drawY - y) < BURBUJA_PIXEL_MOVE) {
                finY = true;
            } else {
                burbuja.drawY = drawY;
            }
        }

        if (finX && finY) {
            // Forzamos centrado por si acaso
            burbuja.drawX = x;
            burbuja.drawY = y;
        }

    }

    /**
     * Devuelve la posicin de una celda inicial vacia ( sin burbuja )
     *
     * @return
     */
    int getInitialCeldaXPos() {

        // Localizing external variables
        Celda[][] celdas = this.celdas;
        int MAX_WIDTH = Tablero.MAX_WIDTH;
        int MAX_HEIGHT = Tablero.MAX_HEIGHT;

        int init = random.nextInt(MAX_WIDTH - 1);

        for (int i = init; i < MAX_WIDTH; i++) {
            Celda leftCelda = celdas[i][MAX_HEIGHT - 1];
            if (leftCelda == null || leftCelda.burbuja == null) {
                return i;
            }
        }
        // No se ha encontrado celda inicial probamos otra vez desde el 0
        for (int i = 0; i < init - 1; i++) {
            Celda leftCelda = celdas[i][MAX_HEIGHT - 1];
            if (leftCelda == null || leftCelda.burbuja == null) {
                return i;
            }
        }
        // No se ha encontrado celda inicial ?!!??
        return -1;

    }

    /**
     * Si hay una celda de posicin inicial libre, crea una nueva burbuja
     * y la coloca en la primera celda inicial libre
     *
     * @param burbuja
     * @param xPos
     */
    public void addInitialBurbuja() {

        if (!tableroInitialized) {
            return;
        }

        int level = this.personaje1.getCurrentLevel();
        if (level != lastLevel) {
            timeEntreBurbujas -= Double.valueOf(0.2 * timeEntreBurbujas).longValue();
            lastLevel = level;
        }

        long difTime = System.currentTimeMillis() - lastInitialBurbujaTime;
        if (difTime < timeEntreBurbujas) {
            //No ha pasado el tiempo suficiente para aadir otra burbuja
            return;
        }

        int xPos = getInitialCeldaXPos();
        if (xPos < 0) {
            // No se ha encontrado posicion inicial
            return;
        }

        Burbuja burbuja = new Burbuja();

        burbuja.drawX = OFFSET_X + (xPos * BurbujaResources.BURBUJA_WIDTH);
        burbuja.drawY = OFFSET_Y + (MAX_HEIGHT * BurbujaResources.BURBUJA_HEIGHT);

        Celda celda = celdas[xPos][MAX_HEIGHT - 1];
        if (celda == null) {
            return;
        }

        celda.burbuja = burbuja;
        addRandomObject(celda);

        lastInitialBurbujaTime = System.currentTimeMillis();

    }

    /**
     * @param celda
     */
    void addRandomObject(Celda celda) {

        if (celda == null || celda.burbuja == null) {
            return;
        }

        Objeto objeto = new Objeto();

        objeto.status = ObjetoResources.OBJETO_ESTADO_BURBUJA;

        int level = this.personaje1.getCurrentLevel();

        int porcentaje = random.nextInt(100);
        if (porcentaje < (ObjetoResources.CARAMELO_PORCENTAJE + level)) {

            objeto.type = ObjetoResources.CARAMELO_OBJECT_TYPE;

        } else if (porcentaje < ObjetoResources.CARAMELO_PORCENTAJE + ObjetoResources.PIRULETA_PORCENTAJE + (2 * level)) {
            if (level >= 1) {
                objeto.type = ObjetoResources.PIRULETA_OBJECT_TYPE;
            } else {
                objeto.type = ObjetoResources.CARAMELO_OBJECT_TYPE;
            }

        } else if (level >= 2 && porcentaje < ObjetoResources.CARAMELO_PORCENTAJE + ObjetoResources.PIRULETA_PORCENTAJE + ObjetoResources.RASPA_PORCENTAJE + (3 * level)) {
            if (level >= 2) {
                objeto.type = ObjetoResources.RASPA_OBJECT_TYPE;
            } else {
                if (level >= 1) {
                    objeto.type = ObjetoResources.PIRULETA_OBJECT_TYPE;
                } else {
                    objeto.type = ObjetoResources.CARAMELO_OBJECT_TYPE;
                }
            }

        } else if (level >= 4 && porcentaje < ObjetoResources.CARAMELO_PORCENTAJE + ObjetoResources.PIRULETA_PORCENTAJE + ObjetoResources.RASPA_PORCENTAJE + ObjetoResources.PEINE_PORCENTAJE + (4 * level)) {
            if (level >= 4) {
                objeto.type = ObjetoResources.PEINE_OBJECT_TYPE;
            } else {
                if (level >= 2) {
                    objeto.type = ObjetoResources.PIRULETA_OBJECT_TYPE;
                } else {
                    objeto.type = ObjetoResources.CARAMELO_OBJECT_TYPE;
                }
            }

        } else {
            // Sin objeto
            return;
        }

        Burbuja burbuja = celda.burbuja;

        objeto.drawX = burbuja.drawX;
        objeto.drawY = burbuja.drawY;

        // Randomizamos indices animacin
        objeto.animationIndex = random.nextInt(ObjetoResources.OBJETO_ANIMATION_SEQUENCE.length);

        celda.objeto = objeto;

    }

    /**
     * Mueve y Actualiza la posicion de la burbuja y el objeto indicados
     *
     * @param burbuja
     * @param canvasHeight
     * @param burbujaMinX
     */
    void moveAndUpdateBurbujaPosition(Celda[][] celdas, Burbuja burbuja, Objeto objeto, int i, int j, int direccion, boolean ignoreNewCelda) {

        // Movemos burbuja
        if (direccion == BurbujaResources.BURBUJA_MOVE_UP) {
            burbuja.drawY -= BurbujaResources.BURBUJA_PIXEL_MOVE;
        } else if (direccion == BurbujaResources.BURBUJA_MOVE_LEFT) {
            burbuja.drawX -= BurbujaResources.BURBUJA_PIXEL_MOVE;
        } else if (direccion == BurbujaResources.BURBUJA_MOVE_RIGHT) {
            burbuja.drawX += BurbujaResources.BURBUJA_PIXEL_MOVE;
        } else if (direccion == BurbujaResources.BURBUJA_MOVE_DOWN) {
            burbuja.drawY += BurbujaResources.BURBUJA_PIXEL_MOVE;
        }

        // Recalculamos posicin en tablero
        CeldaPosition newCeldaPos = calculaPosBurbujaInTablero(burbuja);
        if (newCeldaPos == null) {
            // Error calculo posicion
            return;
        }

        Celda newCelda = celdas[newCeldaPos.i][newCeldaPos.j];
        if (newCelda.burbuja != null) {
            // La celda ya est ocupada
            return;
        }

        // Quitamos la burbuja de su celda anterior
        Celda celda = celdas[i][j];
        celda.burbuja = null;

        // Ponemos la burbuja en la nueva posicion
        newCelda.burbuja = burbuja;

        // Updateamos tambien el objeto ( si hay )
        if (objeto != null) {
            if (direccion == BurbujaResources.BURBUJA_MOVE_UP) {
                objeto.drawY -= BurbujaResources.BURBUJA_PIXEL_MOVE;
            } else if (direccion == BurbujaResources.BURBUJA_MOVE_LEFT) {
                objeto.drawX -= BurbujaResources.BURBUJA_PIXEL_MOVE;
            } else if (direccion == BurbujaResources.BURBUJA_MOVE_RIGHT) {
                objeto.drawX += BurbujaResources.BURBUJA_PIXEL_MOVE;
            } else if (direccion == BurbujaResources.BURBUJA_MOVE_DOWN) {
                objeto.drawY += BurbujaResources.BURBUJA_PIXEL_MOVE;
            }
            celda.objeto = null;
            newCelda.objeto = objeto;
        }

        if (ignoreNewCelda) {
            newCelda.ignore = true;
        }

    }

    /**
     * Calcula la posicion de la burbuja en el tablero
     *
     * @param burbuja
     * @param canvasHeight
     * @param burbujaMinX
     * @return
     */
    CeldaPosition calculaPosBurbujaInTablero(Burbuja burbuja) {

        int xPos = (burbuja.drawX - OFFSET_X) / BurbujaResources.BURBUJA_WIDTH;
        int yPos = (burbuja.drawY - OFFSET_Y) / BurbujaResources.BURBUJA_HEIGHT;

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

        return new CeldaPosition(xPos, yPos);

    }

    /**
     * Reemplaza la burbuja indicada por una explosion
     *
     * @param burbuja
     */
    public Celda getCeldaInCoordinates(int x, int y) {

        if (!tableroInitialized) {
            return null;
        }

        int normX = (x - Tablero.OFFSET_X) / BurbujaResources.BURBUJA_WIDTH;
        int normY = (y - Tablero.OFFSET_Y) / BurbujaResources.BURBUJA_HEIGHT;

        //Log.d(TAG, "getCeldaInCoordinates: x=" + x + ", y=" + y + ", normX=" + normX + ", normY=" + normY );

        if (normX < Tablero.MAX_WIDTH && normX >= 0 && normY < Tablero.MAX_HEIGHT && normY >= 0) {
            Celda celda = celdas[normX][normY];
            return celda;
        }

        return null;

    }

    /**
     * @param celda
     */
    public void explotaBurbuja(Celda celda) {

        if (celda == null || celda.burbuja == null) {
            return;
        }

        Explosion explosion = new Explosion();

        explosion.graphicIndex = 0;

        Burbuja burbuja = celda.burbuja;
        explosion.drawX = burbuja.drawX - ((ExplosionResources.EXPLOSION_WIDTH - BurbujaResources.BURBUJA_WIDTH) / 2);
        explosion.drawY = burbuja.drawY - ((ExplosionResources.EXPLOSION_HEIGHT - BurbujaResources.BURBUJA_HEIGHT) / 2);

        celda.explosion = explosion;

        Objeto objeto = celda.objeto;
        if (objeto != null) {

            objeto.animationIndex = 0;
            objeto.graphicIndex = 0;

            objeto.drawX = explosion.drawX;
            objeto.drawY = explosion.drawY;

            objeto.targetX = 5 + (ObjetoResources.OBJETO_WIDTH / 2);
            objeto.targetY = 120 + (ObjetoResources.OBJETO_HEIGHT / 2);

            objeto.status = ObjetoResources.OBJETO_ESTADO_EXPLOTADO;

        }

        celda.burbuja = null;

    }

    /**
     * @param celda
     */
    public void eliminaBurbuja(Celda celda) {

        if (celda == null || celda.burbuja == null) {
            return;
        }

        explotaBurbuja(celda);

        Objeto objeto = celda.objeto;
        if (objeto != null) {

            objeto.targetX = objeto.drawX;
            objeto.targetY = TABLERO_HEIGHT + ObjetoResources.OBJETO_HEIGHT;

            objeto.status = ObjetoResources.OBJETO_ESTADO_ELIMINADO;

        }

        // Localizing external variables
        Objeto[] objetos_animados = this.objetos_animados;

        // Aadimos objeto a los animados independientemente y lo quitamos de la celda
        boolean objetoAdded = false;
        for (int i = 0; i < MAX_OBJETOS_ANIMADOS; i++) {
            if (objetos_animados[i] == null) {
                objetos_animados[i] = objeto;
                objetoAdded = true;
                break;
            }
        }
        // Si no encontramos posicin libre, lo metemos en una posicin aleatoria
        if (!objetoAdded) {
            objetos_animados[random.nextInt(MAX_OBJETOS_ANIMADOS - 1)] = objeto;
        }
        celda.objeto = null;

        // Tambien controla la velocidad del turno de la CPU
        tableroEstable = false;

    }

    /**
     * @param celda
     */
    public void mueveObjetoEntreBurbujas(Celda origen, Celda destino) {

        if (origen == null || destino == null) {
            return;
        }

        int deltaX = origen.posX - destino.posX;
        int deltaY = origen.posY - destino.posY;
        if (deltaX > 1 || deltaX < -1 || deltaY > 1 || deltaY < -1) {
            return;
        }

        Objeto objeto = origen.objeto;
        if (objeto == null) {
            return;
        }

        Burbuja burbujaDestino = destino.burbuja;
        if (burbujaDestino == null) {
            return;
        }

        objeto.targetX = burbujaDestino.drawX;
        objeto.targetY = burbujaDestino.drawY;

        objeto.status = ObjetoResources.OBJETO_ESTADO_MOVIDO;

        // Quitamos el objeto de la celda origen y lo ponemos en la celda destino
        origen.objeto = null;
        destino.objeto = objeto;

        // Actualizamos estado de movimiento de las burbujas
        if (deltaX == 0 && deltaY > 0) {
            origen.burbuja.union = BurbujaResources.BURBUJA_UNION_DOWN;
            destino.burbuja.union = BurbujaResources.BURBUJA_UNION_UP;
        } else if (deltaX == 0 && deltaY < 0) {
            origen.burbuja.union = BurbujaResources.BURBUJA_UNION_UP;
            destino.burbuja.union = BurbujaResources.BURBUJA_UNION_DOWN;
        } else if (deltaY == 0 && deltaX > 0) {
            origen.burbuja.union = BurbujaResources.BURBUJA_UNION_LEFT;
            destino.burbuja.union = BurbujaResources.BURBUJA_UNION_RIGHT;
        } else if (deltaY == 0 && deltaX < 0) {
            origen.burbuja.union = BurbujaResources.BURBUJA_UNION_RIGHT;
            destino.burbuja.union = BurbujaResources.BURBUJA_UNION_LEFT;
        }

        // Tambien controla la velocidad del turno de la CPU
        tableroEstable = false;

    }

    /**
     * @param celda
     */
    public void tocaBurbuja(Celda celda, int direccionX, int direccionY) {

        if (celda == null) {
            return;
        }

        Burbuja burbuja = celda.burbuja;
        if (burbuja == null) {
            return;
        }

        // Limitamos maximo de movimiento
        int movimientoX = 0;
        if (direccionX > 0) {
            movimientoX = BurbujaResources.BURBUJA_WIDTH_MEDIOS;
        } else if (direccionX < 0) {
            movimientoX = -BurbujaResources.BURBUJA_WIDTH_MEDIOS;
        }
        int movimientoY = 0;
        if (direccionY > 0) {
            movimientoY = BurbujaResources.BURBUJA_WIDTH_MEDIOS;
        } else if (direccionY < 0) {
            movimientoY = -BurbujaResources.BURBUJA_WIDTH_MEDIOS;
        }

        burbuja.drawX -= movimientoX;
        burbuja.drawY -= movimientoY;

        Objeto objeto = celda.objeto;
        if (objeto == null) {
            return;
        }

        objeto.drawX -= movimientoX;
        objeto.drawY -= movimientoY;

    }

    /**
     * @param celda
     */
    public void mueveBurbujaEntreCeldas(Celda origen, Celda destino) {

        if (origen == null || destino == null) {
            return;
        }

        int deltaX = destino.posX - origen.posX;
        int deltaY = destino.posY - origen.posY;
        if (deltaX > 1 || deltaX < -1 || deltaY > 1 || deltaY < -1) {
            return;
        }

        Burbuja burbuja = origen.burbuja;
        if (burbuja == null) {
            return;
        }

        Objeto objetoDestino = destino.objeto;
        if (objetoDestino != null) {
            return;
        }

        // Movemos burbuja a la nueva celda
        destino.burbuja = origen.burbuja;
        destino.objeto = origen.objeto;
        // Quitamos burbuja de la celda origen
        origen.burbuja = null;
        origen.objeto = null;


        // Actualizamos estado de movimiento de la burbuja
        if (deltaX == 0 && deltaY > 0) {
            //Log.d(TAG,"Moving burbuja down: deltaX = " + deltaX + ", deltaY = " + deltaY);
            destino.burbuja.move = BurbujaResources.BURBUJA_MOVE_DOWN;
        } else if (deltaX == 0 && deltaY < 0) {
            //Log.d(TAG,"Moving burbuja up: deltaX = " + deltaX + ", deltaY = " + deltaY);
            destino.burbuja.move = BurbujaResources.BURBUJA_MOVE_UP;
        } else if (deltaY == 0 && deltaX > 0) {
            //Log.d(TAG,"Moving burbuja right: deltaX = " + deltaX + ", deltaY = " + deltaY);
            destino.burbuja.move = BurbujaResources.BURBUJA_MOVE_RIGHT;
        } else if (deltaY == 0 && deltaX < 0) {
            //Log.d(TAG,"Moving burbuja left: deltaX = " + deltaX + ", deltaY = " + deltaY);
            destino.burbuja.move = BurbujaResources.BURBUJA_MOVE_LEFT;
        }

        // Tambien controla la velocidad del turno de la CPU
        tableroEstable = false;

    }

    /**
     * @param celda
     */
    public void addEfectoBurbuja(Burbuja burbuja, int efectoFijoType) {

        if (burbuja == null) {
            return;
        }

        Objeto objeto = new Objeto();

        objeto.burbuja = burbuja;
        objeto.drawX = burbuja.drawX;
        objeto.drawY = burbuja.drawY;
        objeto.type = efectoFijoType;
        objeto.status = ObjetoResources.OBJETO_ESTADO_EFECTO_FIJO;

        // Localizing external variables
        Objeto[] objetos_animados = this.objetos_animados;

        // Aadimos objeto a los animados independientemente
        boolean objetoAdded = false;
        for (int i = 0; i < MAX_OBJETOS_ANIMADOS; i++) {
            if (objetos_animados[i] == null) {
                objetos_animados[i] = objeto;
                objetoAdded = true;
                break;
            }
        }
        // Si no encontramos posicin libre, lo metemos en una posicin aleatoria
        if (!objetoAdded) {
            objetos_animados[random.nextInt(MAX_OBJETOS_ANIMADOS - 1)] = objeto;
        }

    }

    /**
     * @param celda
     */
    public void addEfectoTouch(int drawX, int drawY, int efectoFijoType) {

        Objeto objeto = new Objeto();

        objeto.drawX = drawX - ObjetoResources.OBJETO_WIDTH_MEDIO;
        objeto.drawY = drawY - ObjetoResources.OBJETO_HEIGHT_MEDIO;
        objeto.type = efectoFijoType;
        objeto.status = ObjetoResources.OBJETO_ESTADO_EFECTO_FIJO;

        // Localizing external variables
        Objeto[] objetos_animados = this.objetos_animados;

        // Aadimos objeto a los animados independientemente
        boolean objetoAdded = false;
        for (int i = 0; i < MAX_OBJETOS_ANIMADOS; i++) {
            if (objetos_animados[i] == null) {
                objetos_animados[i] = objeto;
                objetoAdded = true;
                break;
            }
        }
        // Si no encontramos posicin libre, lo metemos en una posicin aleatoria
        if (!objetoAdded) {
            objetos_animados[random.nextInt(MAX_OBJETOS_ANIMADOS - 1)] = objeto;
        }

    }

    /**
     * Anima los objetos independientes.
     *
     * @param canvas
     */
    void animateObjectosIndependientes(Canvas canvas) {

        // Animamos los objetos independientes
        Objeto[] objetos_animados = this.objetos_animados;
        if (objetos_animados == null || objetos_animados.length == 0) {
            return;
        }

        //Localizing extenal variables
        int MAX_OBJETOS_ANIMADOS = Tablero.MAX_OBJETOS_ANIMADOS;
        int OBJETO_ESTADO_ELIMINADO = ObjetoResources.OBJETO_ESTADO_ELIMINADO;
        int OBJETO_ESTADO_EFECTO_FIJO = ObjetoResources.OBJETO_ESTADO_EFECTO_FIJO;

        for (int i = 0; i < MAX_OBJETOS_ANIMADOS; i++) {

            Objeto objeto = objetos_animados[i];
            if (objeto == null) {
                continue;
            }
            // De momento animamos solo los objetos eliminados
            if (objeto.status == OBJETO_ESTADO_ELIMINADO) {

                animaObjetoEliminado(canvas, i, objeto);

            } else if (objeto.status == OBJETO_ESTADO_EFECTO_FIJO) {

                animaObjetoEfectoFijo(canvas, i, objeto);

            }

        }

    }

    /**
     * @param canvas
     * @param i
     * @param objeto
     */
    void animaObjetoEliminado(Canvas canvas, int i, Objeto objeto) {

        objeto.animationIndex++;
        // Hacemos loop
        if (objeto.animationIndex >= ObjetoResources.OBJETO_ELIMINADO_ANIMATION_SEQUENCE.length) {
            objeto.animationIndex = 0;
        }
        objeto.graphicIndex = ObjetoResources.OBJETO_ELIMINADO_ANIMATION_SEQUENCE[objeto.animationIndex];
        if (objeto.graphicIndex >= ObjetoResources.OBJETO_GRAPHICS_SIZE) {
            objeto.graphicIndex = 0;
        }

        boolean finX = false;
        int drawX = objeto.drawX;
        if (objeto.targetX == objeto.drawX) {
            finX = true;

        } else if (objeto.targetX > objeto.drawX) {
            drawX += ObjetoResources.OBJETO_PIXEL_MOVE;
            if ((objeto.targetX - drawX) < ObjetoResources.OBJETO_ELIMINADO_GRAPHICS_SIZE) {
                finX = true;
            } else {
                objeto.drawX = drawX;
            }

        } else if (objeto.targetX < objeto.drawX) {
            drawX -= ObjetoResources.OBJETO_PIXEL_MOVE;
            if ((objeto.drawX - objeto.targetX) < ObjetoResources.OBJETO_ELIMINADO_GRAPHICS_SIZE) {
                finX = true;
            } else {
                objeto.drawX = drawX;
            }
        }
        boolean finY = false;
        int drawY = objeto.drawY;
        if (objeto.targetY == objeto.drawY) {
            finY = true;

        } else if (objeto.targetY > objeto.drawY) {
            drawY += ObjetoResources.OBJETO_PIXEL_MOVE;
            if ((objeto.targetY - objeto.drawY) < ObjetoResources.OBJETO_ELIMINADO_GRAPHICS_SIZE) {
                finY = true;
            } else {
                objeto.drawY = drawY;
            }

        } else if (objeto.targetY < objeto.drawY) {
            drawY -= ObjetoResources.OBJETO_PIXEL_MOVE;
            if ((objeto.drawY - objeto.targetY) < ObjetoResources.OBJETO_ELIMINADO_GRAPHICS_SIZE) {
                finY = true;
            } else {
                objeto.drawY = drawY;
            }
        }

        if (finX && finY) {

            objetos_animados[i] = null;

        } else {

            Bitmap bitmap = null;

            int type = objeto.type;
            if (type == ObjetoResources.CARAMELO_OBJECT_TYPE) {
                bitmap = ObjetoResources.CARAMELO_GRANDE_GRAPHICS_BITMAP[objeto.graphicIndex];
            } else if (type == ObjetoResources.PIRULETA_OBJECT_TYPE) {
                bitmap = ObjetoResources.PIRULETA_GRANDE_GRAPHICS_BITMAP[objeto.graphicIndex];
            } else if (type == ObjetoResources.RASPA_OBJECT_TYPE) {
                bitmap = ObjetoResources.RASPA_GRANDE_GRAPHICS_BITMAP[objeto.graphicIndex];
            } else if (type == ObjetoResources.PEINE_OBJECT_TYPE) {
                bitmap = ObjetoResources.PEINE_GRANDE_GRAPHICS_BITMAP[objeto.graphicIndex];
            }


            canvas.drawBitmap(bitmap, drawX, drawY, null);

        }
    }

    /**
     * @param canvas
     * @param i
     * @param objeto
     */
    void animaObjetoEfectoFijo(Canvas canvas, int i, Objeto objeto) {

        objeto.animationIndex++;
        // Sin loop, lo quitamos
        if (objeto.animationIndex >= EfectoResources.EFECTO_FIJO_ANIMATION_SEQUENCE.length) {
            objetos_animados[i] = null;
            return;
        }
        objeto.graphicIndex = EfectoResources.EFECTO_FIJO_ANIMATION_SEQUENCE[objeto.animationIndex];
        if (objeto.graphicIndex >= EfectoResources.EFECTO_FIJO_ANIMATION_SEQUENCE.length) {
            objeto.graphicIndex = 0;
        }

        Bitmap bitmap = null;

        int type = objeto.type;
        if (type == EfectoResources.EFECTO_BLOQUEO_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_BLOQUEO_GRAPHICS_BITMAP[objeto.graphicIndex];

        } else if (type == EfectoResources.EFECTO_PLAYER_TOUCH_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_PLAYER_TOUCH_GRAPHICS_BITMAP[objeto.graphicIndex];
		/* save memory
		} else if ( type == EfectoResources.EFECTO_CPU_TOUCH_OBJECT_TYPE ) {
			bitmap = EfectoResources.EFECTO_CPU_TOUCH_GRAPHICS_BITMAP[ objeto.graphicIndex ];
		*/
        } else if (type == EfectoResources.EFECTO_PLAYER_MOVE_LEFT_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_PLAYER_MOVE_LEFT_GRAPHICS_BITMAP[objeto.graphicIndex];

        } else if (type == EfectoResources.EFECTO_PLAYER_MOVE_RIGHT_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_PLAYER_MOVE_RIGHT_GRAPHICS_BITMAP[objeto.graphicIndex];

        } else if (type == EfectoResources.EFECTO_TOUCH_MOVE_OBJECT_TYPE) {
            bitmap = EfectoResources.EFECTO_TOUCH_MOVE_GRAPHICS_BITMAP[objeto.graphicIndex];
        }

        // Si el objeto tiene asociado una burbuja seguimos su posicion
        if (objeto.burbuja != null) {
            objeto.drawX = objeto.burbuja.drawX;
            objeto.drawY = objeto.burbuja.drawY;
        }

        canvas.drawBitmap(bitmap, objeto.drawX, objeto.drawY, null);

    }

    /**
     * Actualiza posicin del objeto.
     * Si ha llegado a su destino devuelve true.
     *
     * @param objeto
     */
    boolean mueveObjetoEntreBurbujas(Objeto objeto) {

        if (objeto == null) {
            return false;
        }

        boolean finX = false;
        int drawX = objeto.drawX;
        if (objeto.targetX == objeto.drawX) {
            finX = true;

        } else if (objeto.targetX > objeto.drawX) {
            drawX += ObjetoResources.OBJETO_PIXEL_MOVE;
            if ((objeto.targetX - drawX) < ObjetoResources.OBJETO_GRAPHICS_SIZE) {
                finX = true;
            } else {
                objeto.drawX = drawX;
            }

        } else if (objeto.targetX < objeto.drawX) {
            drawX -= ObjetoResources.OBJETO_PIXEL_MOVE;
            if ((objeto.drawX - objeto.targetX) < ObjetoResources.OBJETO_GRAPHICS_SIZE) {
                finX = true;
            } else {
                objeto.drawX = drawX;
            }
        }
        boolean finY = false;
        int drawY = objeto.drawY;
        if (objeto.targetY == objeto.drawY) {
            finY = true;

        } else if (objeto.targetY > objeto.drawY) {
            drawY += ObjetoResources.OBJETO_PIXEL_MOVE;
            if ((objeto.targetY - objeto.drawY) < ObjetoResources.OBJETO_GRAPHICS_SIZE) {
                finY = true;
            } else {
                objeto.drawY = drawY;
            }

        } else if (objeto.targetY < objeto.drawY) {
            drawY -= ObjetoResources.OBJETO_PIXEL_MOVE;
            if ((objeto.drawY - objeto.targetY) < ObjetoResources.OBJETO_GRAPHICS_SIZE) {
                finY = true;
            } else {
                objeto.drawY = drawY;
            }
        }

        if (finX && finY) {

            return true;

        } else {

            return false;

        }
    }

    /**
     * @param personaje
     * @return
     */
    public IATouchEvent[] calculaNextMovement(Personaje personaje) {

        // Tablero no estable
        if (!tableroEstable) {
            return null;
        }

        // Consideramos un retardo mnimo a partir de que el tablero se estabilice
        long retardo = System.currentTimeMillis() - tableroEstableTime;
        if (retardo < 1000) {
            return null;
        }

        //Log.d(TAG, "calculaNextMovement: retardo is " + retardo + " ms");

        // Localizing external variables
        Celda[][] celdas = this.celdas;
        int BURBUJA_WIDTH = BurbujaResources.BURBUJA_WIDTH;
        int BURBUJA_HEIGHT = BurbujaResources.BURBUJA_HEIGHT;
        int MAX_WIDTH = Tablero.MAX_WIDTH;
        int MAX_HEIGHT = Tablero.MAX_HEIGHT;
        Random random = Tablero.random;

        // Contador de seguridad para evitar bucles infinitos
        int maxIterations = MAX_WIDTH * MAX_HEIGHT;
        while (maxIterations > 0) {

            // Actualizamos numero iteraciones;
            maxIterations--;

            // Calculamos movimiento movimiento aleatorio
            Celda celda = celdas[random.nextInt(MAX_WIDTH - 1)][random.nextInt(MAX_HEIGHT - 1)];
            if (celda == null || celda.burbuja == null) {
                // Movimiento invalido
                continue;
            }

            // Caso la celda tiene un objeto
            if (celda.objeto != null) {
                // Burbuja con objeto
                continue;
            }

            IATouchEvent[] events = new IATouchEvent[2];

            events[0] = new IATouchEvent(MotionEvent.ACTION_DOWN, celda.burbuja.drawX + (BURBUJA_WIDTH / 2), celda.burbuja.drawY + (BURBUJA_HEIGHT / 2));
            events[1] = new IATouchEvent(MotionEvent.ACTION_UP, celda.burbuja.drawX + (BURBUJA_WIDTH / 2), celda.burbuja.drawY + (BURBUJA_HEIGHT / 2));

            return events;

        }

        return null;

    }

    /**
     * @param p1
     * @param p2
     * @return
     */
    public boolean isFinPartida(Personaje p1, Personaje p2) {

        int like = objetos_explotados[ObjetoResources.CARAMELO_OBJECT_TYPE] + objetos_explotados[ObjetoResources.PIRULETA_OBJECT_TYPE];
        int dislike = objetos_explotados[ObjetoResources.RASPA_OBJECT_TYPE] + objetos_explotados[ObjetoResources.PEINE_OBJECT_TYPE];

        //Log.d(TAG,"Like = " + like + ", Dislike = " + dislike);

        // De momento no se gana nunca
		/*if ((like - dislike) > 20 ) {
			p1.setGanador(true);
			return true;
		} else*/
        if ((like - dislike) < -20) {
            p2.setGanador(true);
            return true;
        } else {
            return false;
        }

    }

    public boolean isTableroLlenoBurbujas() {
        return tableroLlenoBurbujas;
    }

    public void setTableroLlenoBurbujas(boolean tableroLlenoBurbujas) {
        this.tableroLlenoBurbujas = tableroLlenoBurbujas;
    }

    public boolean isTableroLlenoObjetos() {
        return tableroLlenoObjetos;
    }

    public void setTableroLlenoObjetos(boolean tableroLlenoObjetos) {
        this.tableroLlenoObjetos = tableroLlenoObjetos;
    }

    public void destroy() {
        BurbujaResources.destroy();
        EfectoResources.destroy();
        ExplosionResources.destroy();
        ObjetoResources.destroy();
        //resources=null;
        //personaje1=null;
        //personaje2=null;
        //celdas=null;
        //objetos_animados=null;
        //objetos_explotados=null;
    }

}

/**
 * Classe auxiliar para devolver posicin en celda de un Objeto en tablero
 *
 * @author amarinji
 */
class CeldaPosition {

    public int i = 0;
    public int j = 0;

    public CeldaPosition(int i, int j) {
        this.i = i;
        this.j = j;
    }

}
