package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameSurfaceView extends SurfaceView implements Runnable {

    private boolean isPlaying;
    private Nave nave;

    private ArrayList<Star> stars;
    private ArrayList<NaveEnemiga> naveEnemigas;
    private ArrayList<Asteroide> asteroides;
    private ArrayList<DisparoNave> disparos;
    private ArrayList<DisparoEnemigo> disparosEnemigos;

    ArrayList<Star> starsToKeep;
    ArrayList<NaveEnemiga> enemiesToKeep;
    ArrayList<Asteroide> asteroidesToKeep;
    ArrayList<DisparoNave> disparosToKeep;
    ArrayList<DisparoEnemigo> disparosEnemigosToKeep;

    private Paint paint;
    private Paint paintCloud;
    private Paint paintAdult;
    private Paint paintBoy;

    private Canvas canvas;
    private SurfaceHolder holder;
    private Thread gameplayThread = null;
    private Random rd;
    private Context context;
    private float screenWith;
    private float screenHeight;
    private int score;


    /**
     * Contructor
     *
     * @param context
     */
    public GameSurfaceView(Context context, float screenWith, float screenHeight) {
        super(context);
        nave = new Nave(context, screenWith, screenHeight);

        this.stars = new ArrayList<Star>();

        this.naveEnemigas = new ArrayList<NaveEnemiga>();
        this.asteroides = new ArrayList<Asteroide>();
        this.disparos = new ArrayList<DisparoNave>();
        this.disparosEnemigos = new ArrayList<DisparoEnemigo>();

        paint = new Paint();
        paintCloud = new Paint();
        paintAdult = new Paint();
        paintBoy = new Paint();

        holder = getHolder();
        isPlaying = true;
        rd = new Random();
        this.context = context;
        this.screenWith = screenWith;
        this.screenHeight = screenHeight;
        this.score = 0;


    }

    /**
     * Method implemented from runnable interface
     */
    @Override
    public void run() {
        while (isPlaying) {
            updateInfo();
            paintFrame();

        }


    }

    private void updateInfo() {
        nave.updateInfo();

        for (Star c : stars) {
            c.updateInfo();
        }

        for (NaveEnemiga a : naveEnemigas) {
            a.updateInfo();
        }

        for (Asteroide b : asteroides) {
            b.updateInfo();
        }

        for (DisparoNave disparoNave : disparos)
            disparoNave.updateInfo();

        for (DisparoEnemigo disparoEnemigo : disparosEnemigos)
            disparoEnemigo.updateInfo();
    }

    private void paintFrame() {


        ArrayList<Integer> PosY = new ArrayList<Integer>();

        for (int i = 100; i < screenHeight; i += 150)
            PosY.add(i);

        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.rgb(0, 0, 0));
            Paint text = new Paint();
            text.setTextSize(45);
            text.setColor(Color.RED);
            canvas.drawText("Score: " + score, 60, 60, text);


            //holder.unlockCanvasAndPost(canvas);
            starsToKeep = new ArrayList<Star>();
            enemiesToKeep = new ArrayList<NaveEnemiga>();
            asteroidesToKeep = new ArrayList<Asteroide>();
            disparosToKeep = new ArrayList<DisparoNave>();
            disparosEnemigosToKeep = new ArrayList<DisparoEnemigo>();

            //Si está en pantalla se pinta, sino se borra de la lista
            for (int i = 0; i < stars.size(); i++) {
                if (stars.get(i).isVisible()) {
                    canvas.drawBitmap(stars.get(i).getSprite(), stars.get(i).getPositionX(), stars.get(i).getPositionY(), new Paint());
                    starsToKeep.add(stars.get(i));
                } else
                    score -= 10;
            }

            for (int i = 0; i < naveEnemigas.size(); i++) {
                if (naveEnemigas.get(i).isVisible()) {
                    canvas.drawBitmap(naveEnemigas.get(i).getSpriteNaveEnemiga(), naveEnemigas.get(i).getPositionX(), naveEnemigas.get(i).getPositionY(), new Paint());
                    enemiesToKeep.add(naveEnemigas.get(i));
                } else {
                    score -= 10;
                }
            }
            for (int i = 0; i < asteroides.size(); i++) {
                if (asteroides.get(i).isVisible()) {
                    canvas.drawBitmap(asteroides.get(i).getSpriteAsteroide(), asteroides.get(i).getPositionX(), asteroides.get(i).getPositionY(), new Paint());
                    asteroidesToKeep.add(asteroides.get(i));
                } else {
                    score -= 10;
                }
            }

            for (int i = 0; i < disparos.size(); i++) {
                if (disparos.get(i).isVisible()) {
                    canvas.drawBitmap(disparos.get(i).getSpriteDisparoNave(), disparos.get(i).getPositionX(), disparos.get(i).getPositionY(), new Paint());
                    disparosToKeep.add(disparos.get(i));
                }
            }

            for (int i = 0; i < disparosEnemigos.size(); i++) {
                if (disparosEnemigos.get(i).isVisible()) {
                    canvas.drawBitmap(disparosEnemigos.get(i).getSpriteDisparoEnemigo(), disparosEnemigos.get(i).getPositionX(), disparosEnemigos.get(i).getPositionY(), new Paint());
                    disparosEnemigosToKeep.add(disparosEnemigos.get(i));
                }
            }

                stars = starsToKeep;
                naveEnemigas = enemiesToKeep;
                asteroides = asteroidesToKeep;

                disparos=disparosToKeep;
                disparosEnemigos=disparosEnemigosToKeep;

                double porcentajeProbabilidad = 0.98;
                boolean end = false;
                canvas.drawBitmap(nave.getSpriteNave(), nave.getPositionX(), nave.getPositionY(), paint);
                int r = rd.nextInt(10000);
                if (r > 10000 * porcentajeProbabilidad) {


                    int pos = rd.nextInt(PosY.size() - 1);
                    NaveEnemiga naveEnemiga = new NaveEnemiga(getContext(), screenWith, screenHeight, PosY.get(pos));
                    Boolean b = PosY.remove((Object) pos);
                    pos = rd.nextInt(PosY.size() - 1);

                    if (r % 2 == 0 || r % 3 == 0) {
                        Asteroide asteroide = new Asteroide(getContext(), screenWith, screenHeight, PosY.get(pos));
                        asteroides.add(asteroide);
                    }


                    naveEnemigas.add(naveEnemiga);

                    int starsBound = rd.nextInt(5);

                    for (int i = 0; i < starsBound; i++) {
                        Star star = new Star(getContext(), screenWith, screenHeight);
                        stars.add(star);
                        canvas.drawBitmap(star.getSprite(), star.getPositionX(), star.getPositionY(), new Paint());
                    }


                    ArrayList<Asteroide> newAsteroide = new ArrayList<Asteroide>();
                    for (Asteroide asteroide1 : asteroides)
                        if (checkAsteroidCollision(asteroide1)) {
                            isPlaying = false;
                            end = true;
                        } else
                            newAsteroide.add(asteroide1);


                    asteroides = newAsteroide;

                    ArrayList<NaveEnemiga> newNaveEnemiga = new ArrayList<NaveEnemiga>();

                    for (NaveEnemiga naveEnemiga1 : naveEnemigas) {
                        if (checkEnemyCollision(naveEnemiga1)) {
                            isPlaying = false;
                            end = true;
                        } else
                            newNaveEnemiga.add(naveEnemiga1);
                    }


                    naveEnemigas = newNaveEnemiga;

                    if (end) {
                        Paint endtext = new Paint();
                        endtext.setTextSize(145);
                        endtext.setColor(Color.RED);
                        canvas.drawText("Game Over!", (screenWith / 2) - endtext.getTextSize() * 5 / 2, screenHeight / 2, endtext);
                    }
                    holder.unlockCanvasAndPost(canvas);


                } else {
                    holder.unlockCanvasAndPost(canvas);
                }


            }


        }


        public void pause () {
            isPlaying = false;
            try {
                gameplayThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        public void resume () {

            isPlaying = true;
            gameplayThread = new Thread(this);
            gameplayThread.start();
        }

        /**
         * Detect the action of the touch event
         *
         * @param motionEvent
         * @return
         */
        @Override
        public boolean onTouchEvent (MotionEvent motionEvent){
            //Movimiento
            if (motionEvent.getX() < screenWith / 2)
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:

                        System.out.println("TOUCH UP - STOP JUMPING");
                        nave.setJumping(false);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        System.out.println("TOUCH DOWN - JUMP");
                        nave.setJumping(true);
                        break;
                }
            //Disparo
            if (motionEvent.getX() > screenWith / 2)
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:

                        System.out.println("TOUCH UP - NOTHING");
                        //Nada
                        break;
                    case MotionEvent.ACTION_DOWN:
                        System.out.println("TOUCH DOWN - FIRE!");
                        DisparoNave disparo = new DisparoNave(this.context, nave.getPositionX() + nave.getSpriteNave().getWidth(), nave.getPositionY()+ nave.getSpriteNave().getHeight()/2, screenWith, screenHeight);
                        disparos.add(disparo);
                        break;
                }
            return true;
        }

        public boolean checkAsteroidCollision (Asteroide asteroide){

            return nave.getPositionX() + nave.getSpriteNave().getWidth() > asteroide.getPositionX() &&
                    nave.getPositionX() < asteroide.getPositionX()  &&
                    nave.getPositionY() + nave.getSpriteNave().getHeight() >= asteroide.getPositionY() &&
                    nave.getPositionY() <= asteroide.getPositionY();

        }

        public boolean checkEnemyCollision (NaveEnemiga naveEnemiga){
            return nave.getPositionX() + nave.getSpriteNave().getWidth() > naveEnemiga.getPositionX() &&
                    nave.getPositionX() < naveEnemiga.getPositionX()  &&
                    nave.getPositionY() + nave.getSpriteNave().getHeight() >= naveEnemiga.getPositionY() &&
                    nave.getPositionY() <= naveEnemiga.getPositionY();
        }

    }
