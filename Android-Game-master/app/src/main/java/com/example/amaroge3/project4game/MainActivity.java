package com.example.amaroge3.project4game;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Display;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //gameview instance
    GameView gv;
    //new paint instance
    Paint drawPaint = new Paint();
    //bitmap of ball and background image
    Bitmap rainingBall;
    Bitmap backgroundimage, bgbitmap;
    //jar bitmap
    static Bitmap jarBitmap;
    //coordinates and speed of gumball
    int graphic1X = 0, graphic1Y = 300, graphic1YSpeed = 10;
    //jar coordinates
    static int jarLocX = 300;
    static int jarLocY = 1200;
    //score tracking
    int score = 0;
    int misses = 0;

    //screen height and width tracking
    int screenX, screenY;
    Point size = new Point();

    //sound variables
    SoundPool soundPool;
    int mySound = 0;


    //ontouch variables
    Boolean bitmapSelected;
    float x, y;

    //start different activity variable
    Intent myIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        screenX = size.x;
        screenY = size.y;
        gv = new GameView(this);
        this.setContentView(gv);


        //create initial image randomly
        changeImageRandom(getRandom(1, 3));

        //background image set
        backgroundimage = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundimage);
        bgbitmap = Bitmap.createScaledBitmap(backgroundimage,screenX,screenY-50,true);

        //set bitmap of the jar
        jarBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gumball_jar);
        jarBitmap = Bitmap.createScaledBitmap(jarBitmap,350,350,true);


        graphic1X = getRandom(0, screenX - 100);

        initSound();

    }

    @Override
    protected void onPause(){
        super.onPause();;
        gv.pause();
    }



    @Override
    protected void onResume(){
        super.onResume();
        gv.resume();

    }

    public class GameView extends SurfaceView implements Runnable{
        Thread ViewThread = null;
        SurfaceHolder holder;
        int randomImageNumber = getRandom(1, 3);
        boolean threadOK = true;

        public GameView(Context context){
            super(context);
            holder = this.getHolder();
        }

        @Override
        public void run(){
            while(threadOK == true){
                if (!holder.getSurface().isValid()){
                    continue;

                }
                Canvas gameCanvas = holder.lockCanvas();
                myOnDraw(gameCanvas);
                holder.unlockCanvasAndPost(gameCanvas);
            }
        }



        /*
        Draw method that draws on the canvas
         */
        protected void myOnDraw(Canvas canvas){
            drawPaint.setAlpha(200);

            //draws the background image on canvas
            canvas.drawBitmap(bgbitmap, 0,  0, drawPaint);
            changeImageRandom(randomImageNumber);
            //draws the raining ball on canvas
            canvas.drawBitmap(rainingBall, graphic1X, graphic1Y, drawPaint);
            //draws the jar on canvas
            canvas.drawBitmap(jarBitmap, jarLocX, jarLocY, drawPaint);
            //move the ball
            graphic1Y += graphic1YSpeed;
            //set the text size for the score
            drawPaint.setTextSize(50);


            //call the isInJar method to check if the ball has entered the jar
            if (isInJar()){
                graphic1Y = 300; //if so, set the y to 300 to drop another one
                score++;  //increase player's score
                playSound(); //play sound of ball entering the jar
                randomImageNumber = getRandom(1, 3);   //generate random gum ball to drop next
                graphic1X = getRandom(0, screenX - 100);  //generate a new random x value for the gumball
                graphic1YSpeed += 2; //increase speed
            }

            canvas.drawText("Score: " + String.valueOf(score), 50, 100, drawPaint);
            canvas.drawText("Misses: " + String.valueOf(misses), 50, 150, drawPaint);

            //if the player misses the ball
            if (graphic1Y > screenY){
                graphic1Y = 350;
                graphic1X = getRandom(0, screenX - 100);
                randomImageNumber = getRandom(1, 3);
                graphic1YSpeed += 2; //increase speed
                misses++;

                checkIfLostGame();

                //maximum speed of gumball is set to 60
                if (graphic1YSpeed > 60){
                    graphic1YSpeed = 60;
                }

            }
        }

        //pause method
        public void pause(){
            threadOK = false;
            while(true){
                try{
                    ViewThread.join();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                break;
            }
            ViewThread = null;
        }


        //resume method.
        public void resume(){
            threadOK = true;
            ViewThread = new Thread (this);
            ViewThread.start();
        }
    }


    /*
    Helper method for random integer.
     */
    public static int getRandom (int from, int to){

        Random rand = new Random();
        int random = rand.nextInt(to) + from;
        return random;
    }

    //get jar y location
    public static int getJarLocY(){ return jarLocY; }
    //set jar location x
    public static void setJarLocX(int loc){ jarLocX = loc; }
    //get jar location x
    public static int getJarLocX(){ return jarLocX; }
    //get bitmap jar image.
    public static Bitmap getJarBitmap(){ return jarBitmap; }


    /*
    Method to change the gumball picture to a random one between green, orange, and red gumballs
     */
    public void changeImageRandom(int randomNumber){

        switch (randomNumber) {
            case 1:
                rainingBall = BitmapFactory.decodeResource(getResources(), R.drawable.green_gum);
                rainingBall = rainingBall.createScaledBitmap(rainingBall, 50, 50, false);

                break;
            case 2:
                rainingBall = BitmapFactory.decodeResource(getResources(), R.drawable.orange_gum);
                rainingBall = rainingBall.createScaledBitmap(rainingBall, 50, 50, false);

                break;
            case 3:
                rainingBall = BitmapFactory.decodeResource(getResources(), R.drawable.red_gum);
                rainingBall = rainingBall.createScaledBitmap(rainingBall, 50, 50, false);

                break;
        }


    }

    //initialize sound method
    public void initSound(){
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        mySound = soundPool.load(this, R.raw.candy, 1);

    }
    //play sound method
    public void playSound(){
        soundPool.play(mySound, 1, 1, 1, 0, 1);
    }

    /*
     method that checks if ball enters the jar
      */
    public boolean isInJar(){
        if (graphic1X > getJarLocX()
                && (graphic1X + 50) < (getJarLocX() + jarBitmap.getWidth())
                && graphic1Y > getJarLocY()
                && graphic1Y < (getJarLocY() + (jarBitmap.getHeight() / 3)) //check if ball enters the topmost of jar
                ){
            return true;
        }
        return false;
    }

    /*
    Motion event method for moving the jar
     */
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                x = event.getRawX();
                y = event.getRawY() - 108;
                bitmapSelected = GraphicTools.graphicSelected(MainActivity.getJarBitmap(), x, y);
            }

            break;
            case MotionEvent.ACTION_MOVE: {
                x = event.getRawX() - 200;
                y = event.getRawY();
            }
            if (bitmapSelected) {
                MainActivity.setJarLocX((int) x);
            }
            break;
            case MotionEvent.ACTION_UP: {
                bitmapSelected = false;
            }
            break;
        }
        return true;
    }

    //checks if the user loses the game
    public void checkIfLostGame() {
        if (misses == 3) {
            myIntent = new Intent(this, GameFinish.class);
            startActivity(myIntent);
            finish();
        }
    }
}