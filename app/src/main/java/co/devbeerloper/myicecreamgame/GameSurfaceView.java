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

    private ArrayList<Cloud> clouds;

    private ArrayList<NaveEnemiga> naveEnemigas;
    private ArrayList<Asteroide> asteroides;
    ArrayList<Integer> removeID;
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

        this.clouds = new ArrayList<Cloud>();

        this.naveEnemigas = new ArrayList<NaveEnemiga>();
        this.asteroides = new ArrayList<Asteroide>();
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

        for (Cloud c : clouds) {
            c.updateInfo();
        }

        for (NaveEnemiga a : naveEnemigas) {
            a.updateInfo();
        }

        for (Asteroide b : asteroides) {
            b.updateInfo();
        }


    }

    private void paintFrame() {


        ArrayList<Integer> PosY = new ArrayList<Integer>();

        for (int i = 100; i < screenHeight; i += 150)
            PosY.add(i);

        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.rgb(52, 153, 255));
            Paint text = new Paint();
            text.setTextSize(45);
            text.setColor(Color.RED);
            canvas.drawText("Score: " + score, 60, 60, text);



            //holder.unlockCanvasAndPost(canvas);
            removeID = new ArrayList<Integer>();


            //Si está en pantalla se pinta, sino se borra de la lista
            for (int i = 0; i < clouds.size(); i++) {
                if (clouds.get(i).isVisible())
                    canvas.drawBitmap(clouds.get(i).getSpriteIcecreamCar(), clouds.get(i).getPositionX(), clouds.get(i).getPositionY(), new Paint());
                else
                    removeID.add(i);
            }

            for (int i = 0; i < naveEnemigas.size(); i++) {
                if (naveEnemigas.get(i).isVisible())
                    canvas.drawBitmap(naveEnemigas.get(i).getSpriteNaveEnemiga(), naveEnemigas.get(i).getPositionX(), naveEnemigas.get(i).getPositionY(), new Paint());
                else
                    removeID.add(i);
            }
            for (int i = 0; i < asteroides.size(); i++) {
                if (asteroides.get(i).isVisible())
                    canvas.drawBitmap(asteroides.get(i).getSpriteAsteroide(), asteroides.get(i).getPositionX(), asteroides.get(i).getPositionY(), new Paint());
                else
                    removeID.add(i);
            }


            for (int i = 0; i < removeID.size(); i++)
                clouds.remove(removeID.get(i));
            for (int i = 0; i < removeID.size(); i++)
                naveEnemigas.remove(removeID.get(i));
            for (int i = 0; i < removeID.size(); i++)
                asteroides.remove(removeID.get(i));


            removeID.clear();


            double porcentajeProbabilidad = 0.98;
            boolean end = false;
            canvas.drawBitmap(nave.getSpriteNave(), nave.getPositionX(), nave.getPositionY(), paint);
            int r = rd.nextInt(10000);
            if (r > 10000 * porcentajeProbabilidad) {
                Cloud cloud = new Cloud(getContext(), screenWith, screenHeight);

                int pos = rd.nextInt(PosY.size() - 1);
                NaveEnemiga naveEnemiga = new NaveEnemiga(getContext(), screenWith, screenHeight, PosY.get(pos));
                Boolean b = PosY.remove((Object) pos);
                pos = rd.nextInt(PosY.size() - 1);

                if (r % 2 == 0 || r % 3 == 0 ) {
                    Asteroide asteroide = new Asteroide(getContext(), screenWith, screenHeight, PosY.get(pos));
                    asteroides.add(asteroide);
                }

                //canvas.drawBitmap(cloud.getSpriteNave(),cloud.getPositionX(),cloud.getPositionY(),paintCloud);
                clouds.add(cloud);
                naveEnemigas.add(naveEnemiga);


                canvas.drawBitmap(cloud.getSpriteIcecreamCar(), cloud.getPositionX(), cloud.getPositionY(), new Paint());



                ArrayList<Asteroide> newAsteroide = new ArrayList<Asteroide>();
                for (Asteroide asteroide1 : asteroides)
                    if (checkBoyCollision(asteroide1))
                        score += 10;
                    else
                        newAsteroide.add(asteroide1);


                asteroides = newAsteroide;

                ArrayList<NaveEnemiga> newNaveEnemiga = new ArrayList<NaveEnemiga>();

                for (NaveEnemiga naveEnemiga1 : naveEnemigas) {
                    if (checkAdultCollision(naveEnemiga1)) {
                        isPlaying = false;
                        end = true;
                    } else
                        newNaveEnemiga.add(naveEnemiga1);
                }


                naveEnemigas = newNaveEnemiga;

                if(end) {
                    Paint endtext = new Paint();
                    endtext.setTextSize(145);
                    endtext.setColor(Color.RED);
                    canvas.drawText("Game Over!", (screenWith / 2) - endtext.getTextSize()*5/2, screenHeight / 2, endtext);
                }
                holder.unlockCanvasAndPost(canvas);




            } else {
                holder.unlockCanvasAndPost(canvas);
            }



        }


    }


    public void pause() {
        isPlaying = false;
        try {
            gameplayThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void resume() {

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
    public boolean onTouchEvent(MotionEvent motionEvent) {
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
        return true;
    }

    public boolean checkBoyCollision(Asteroide asteroide) {

        return nave.getPositionX() + nave.getSpriteNave().getWidth() > asteroide.getPositionX() &&
                nave.getPositionY() + nave.getSpriteNave().getHeight() >= asteroide.getPositionY() + asteroide.getSpriteAsteroide().getHeight() &&
                nave.getPositionY() <= asteroide.getPositionY();

    }

    public boolean checkAdultCollision(NaveEnemiga naveEnemiga) {
        return nave.getPositionX() + nave.getSpriteNave().getWidth() > naveEnemiga.getPositionX() &&
                nave.getPositionY() + nave.getSpriteNave().getHeight() >= naveEnemiga.getPositionY() + naveEnemiga.getSpriteNaveEnemiga().getHeight() &&
                nave.getPositionY() <= naveEnemiga.getPositionY();
    }

}
