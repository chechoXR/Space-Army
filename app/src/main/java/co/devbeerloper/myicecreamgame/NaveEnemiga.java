package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;


public class NaveEnemiga {

    public static final float INIT_X =100;
    public static final float INIT_Y =100;
    public static final int SPRITE_SIZE_WIDTH =100;
    public static final int SPRITE_SIZE_HEIGTH=101;
    private float speed = 0;
    private float positionX;
    private float positionY;
    private Bitmap spriteNaveEnemiga;
    private boolean isJumping;
    private boolean visible = true;


    public NaveEnemiga(Context context, float screenWidth, float screenHeigth){
        Random rd = new Random();

        speed = 1;
        positionX =screenWidth;
        positionY =rd.nextInt((int)(screenHeigth));
        isJumping = false;
        //Getting bitmap from resource


        Bitmap originalBitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.enemigo);

        spriteNaveEnemiga = Bitmap.createScaledBitmap(originalBitmap, SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGTH, false);


    }


    public NaveEnemiga(Context context, float screenWidth, float screenHeigth, int y){
        Random rd = new Random();

        speed = 1;
        positionX =screenWidth;
        positionY =y;
        isJumping = false;
        //Getting bitmap from resource


        Bitmap originalBitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.enemigo);

        spriteNaveEnemiga = Bitmap.createScaledBitmap(originalBitmap, SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGTH, false);
    }


    public NaveEnemiga(Context context, float initialX, float initialY, float screenWidth, float screenHeigth){

        speed = 1;
        positionX = initialX;
        positionY = initialY;


        Bitmap originalBitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.enemigo);

        spriteNaveEnemiga = Bitmap.createScaledBitmap(originalBitmap, SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGTH, false);
    }

    public static float getInitX() {
        return INIT_X;
    }

    public static float getInitY() {
        return INIT_Y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public Bitmap getSpriteNaveEnemiga() {
        return spriteNaveEnemiga;
    }

    public void setSpriteNaveEnemiga(Bitmap spriteNaveEnemiga) {
        this.spriteNaveEnemiga = spriteNaveEnemiga;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    /**
     * Control the position and behaviour of the icecream car
     */
    public void updateInfo () {


        if(this.positionX>0)
            this.positionX-=5;
        else
            this.visible=false;


    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
