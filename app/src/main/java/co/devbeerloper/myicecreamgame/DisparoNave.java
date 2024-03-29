package co.devbeerloper.myicecreamgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class DisparoNave {

    public static final float INIT_X = 100;
    public static final float INIT_Y = 100;
    public static final int SPRITE_SIZE_WIDTH = 100;
    public static final int SPRITE_SIZE_HEIGTH = 10;

    private float speed = 0;
    private float positionX;
    private float positionY;
    private Bitmap spriteDisparoNave;
    private boolean visible = true;
    private float screenWidth;
    private float screenHeigth;

    public DisparoNave(Context context, float x, float y, float screenWidth, float screenHeigth) {
        speed = 3;
        positionX = x;
        positionY = y;
        this.screenWidth=screenWidth;
        this.screenHeigth=screenHeigth;
        //Getting bitmap from resource
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.balanave);
        spriteDisparoNave = Bitmap.createScaledBitmap(originalBitmap, SPRITE_SIZE_WIDTH, SPRITE_SIZE_HEIGTH, false);
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

    public Bitmap getSpriteDisparoNave() {
        return spriteDisparoNave;
    }

    public void setSpriteDisparoNave(Bitmap spriteDisparoNave) {
        this.spriteDisparoNave = spriteDisparoNave;
    }


    /**
     * Control the position and behaviour of the icecream car
     */
    public void updateInfo() {


        if (this.positionX < this.screenWidth)
            this.positionX += 7;
        else
            this.visible = false;

    }

    public boolean isVisible() {
        return visible;
    }


    public void setVisible(boolean visible) {
        this.visible = visible;
    }


}