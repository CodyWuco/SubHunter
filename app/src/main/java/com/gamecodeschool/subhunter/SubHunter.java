// This is our package
// If you are copy & pasting the code then this line will probably be different to yours
// If so, keep YOUR line- not this one
package com.gamecodeschool.subhunter;

// These are all the classes of other people's
// (Android) code that we use in Sub Hunt
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.widget.ImageView;

import java.util.Random;


class Grid {


    // Initialize our size based variables based on the screen resolution
    int numberHorizontalPixels;
    int numberVerticalPixels;
    int blockSize;
    int gridWidth = 40;
    int gridHeight;

    public Grid(int x, int y){
        setPixels(x, y);
    }

    // Draw the vertical lines of the grid
    void drawVertGridLines(Canvas canvas, Paint paint) {
        for (int i = 0; i < gridWidth; i++) {
            canvas.drawLine(blockSize * i, 0,
                    blockSize * i, numberVerticalPixels,
                    paint);
        }
    }

    // Draw the horizontal lines of the grid
    void drawHoriGridLines(Canvas canvas, Paint paint) {
        for (int i = 0; i < gridHeight; i++) {
            canvas.drawLine(0, blockSize * i,
                    numberHorizontalPixels, blockSize * i,
                    paint);
        }
    }

    // Initialize our size based variables based on the screen resolution
    public void setPixels(int x, int y){
        numberHorizontalPixels = x;
        numberVerticalPixels = y;
        blockSize = numberHorizontalPixels / gridWidth;
        gridHeight = numberVerticalPixels / blockSize;
    }
}

class Sub {


    int subHorizontalPosition;
    int subVerticalPosition;
    boolean hit;

    public void randLocation(int gridHeight, int gridWidth) {
        Random random = new Random();
        subHorizontalPosition = random.nextInt(gridWidth);
        subVerticalPosition = random.nextInt(gridHeight);
        hit = false;
    }
}



public class SubHunter extends Activity {
    /*
        I want to create a shots class that had takeshot,
        kept track of shot location and number of shots,
        and turning subs into a grid object to use grid
        like a game map, but those were all low priority.
     */

    // These variables can be "seen"
    // throughout the SubHunter class
    // didn't have enough time to localize these
    float horizontalTouched = -100;
    float verticalTouched = -100;
    int distanceFromSub;
    // shots should get be part of a class
    // maybe with the above variables
    int shotsTaken;

    //one stop changes
    boolean debugging = false;
    int subCount = 3;

    // Here are all the objects(instances)
    // of classes that we need to do some drawing
    ImageView gameView;
    Bitmap blankBitmap;
    Canvas canvas;
    Paint paint;

    // of play space objects
    Sub[] sub = new Sub[subCount];
    Grid grid;

    /*
        Android runs this code just before
        the app is seen by the player.
        This makes it a good place to add
        the code that is needed for
        the one-time setup.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSubs();

        setScreenRes();
        initObjDraw();

        // Tell Android to set our drawing
        // as the view for this app
        setContentView(gameView);

        Log.d("Debugging", "In onCreate");
        newGame();
        draw(grid.blockSize);
    }

    void initSubs(){
        for ( int i = 0; i < sub.length; i++) {
            sub[i] = new Sub();
        }
    }

    // Get the current device's screen resolution
    void setScreenRes(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        initGrid(size.x, size.y);
    }

    // Initialize our size based variables based on the screen resolution
    void initGrid(int x, int y){
        grid = new Grid(x, y);
    }

    // Initialize all the objects ready for drawing
    void initObjDraw(){
        blankBitmap = Bitmap.createBitmap(grid.numberHorizontalPixels,
                grid.numberVerticalPixels,
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(blankBitmap);
        gameView = new ImageView(this);
        paint = new Paint();
    }

    /*
        This code will execute when a new
        game needs to be started. It will
        happen when the app is first started
        and after the player wins a game.
     */
    void newGame(){
        respawnSubs();
        resetScore();
        Log.d("Debugging", "In newGame");
    }

    void respawnSubs(){
        for( int i = 0; i < sub.length; i++) { sub[i].randLocation(grid.gridHeight, grid.gridWidth); }
    }

    void resetScore(){ shotsTaken = 0; }

    /*
        Here we will do all the drawing.
        The grid lines, the HUD and
        the touch indicator
     */
    void draw(int blockSize) {
        gameView.setImageBitmap(blankBitmap);

        setScreenWhite();

        //draw black grid lines
        setPaintBlack();
        grid.drawVertGridLines(canvas, paint);
        grid.drawHoriGridLines(canvas, paint);

        drawPlayerShot(blockSize);

        setPaintRed();
        drawSunkSubs(blockSize);

        setPaintBlue();
        drawHub(blockSize);


        Log.d("Debugging", "In draw");
        if (debugging) {
            printDebuggingText();
        }
    }

    void setPaintColor(int alpha, int red, int green, int blue){
        paint.setColor(Color.argb(alpha, red, green, blue));
    }

    // Wipe the screen with a color
    void setScreenColor(int alpha, int red, int green, int blue){
        canvas.drawColor(Color.argb(alpha, red, green, blue));
    }

    void setScreenWhite(){
        setScreenColor(255, 255, 255, 255);
    }

    void setScreenRed(){
        setPaintColor(255, 255, 0, 0);
    }

    void setPaintBlack(){
        setPaintColor(255, 0, 0, 0);
    }
    void setPaintWhite(){
        setPaintColor(255, 255, 255, 255);
    }

    void setPaintRed(){
        setPaintColor(255, 255, 0, 0);
    }
    void setPaintBlue(){
        setPaintColor(255, 0, 0, 255);
    }

    void drawPlayerShot(int blockSize){
        canvas.drawRect(horizontalTouched * blockSize,
                verticalTouched * blockSize,
                (horizontalTouched * blockSize) + blockSize,
                (verticalTouched * blockSize)+ blockSize,
                paint );
    }

    void drawSunkSubs(int blockSize){
        for( int i = 0; i < sub.length; i++) {
            if(sub[i].hit == true) {
                // Draw the player's shot
                canvas.drawRect(sub[i].subHorizontalPosition * blockSize,
                        sub[i].subVerticalPosition * blockSize,
                        (sub[i].subHorizontalPosition * blockSize) + blockSize,
                        (sub[i].subVerticalPosition * blockSize) + blockSize,
                        paint);
            }
        }
    }

    void drawHub(int blockSize){
        // Re-size the text appropriate for the
        // score and distance text
        paint.setTextSize(blockSize * 2);
        canvas.drawText(
                "Shots Taken: " + shotsTaken +
                        "  Distance: " + distanceFromSub,
                blockSize, blockSize * 1.75f,
                paint);
    }

    /*
        This part of the code will
        handle detecting that the player
        has tapped the screen
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Log.d("Debugging", "In onTouchEvent");
        onPlayerTap(motionEvent);

        return true;
    }

    void onPlayerTap(MotionEvent motionEvent){
        // Has the player removed their finger from the screen?
        if((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
            // Process the player's shot by passing the
            // coordinates of the player's finger to takeShot
            takeShot(motionEvent.getX(), motionEvent.getY());
        }
    }

    /*
        The code here will execute when
        the player taps the screen It will
        calculate distance from the sub'
        and determine a hit or miss
     */
    void takeShot(float touchX, float touchY){
        Log.d("Debugging", "In takeShot");

        incrementShotCount();

        touchGridCoor(touchX, touchY, grid.blockSize);
        distanceFromSub = findClosest();

        // If there is a hit call boom
        if(isAllHit())
            boom(grid.blockSize);
            // Otherwise call draw as usual
        else draw(grid.blockSize);
    }

    void incrementShotCount(){ shotsTaken ++;}

    // Convert the float screen coordinates
    // into int grid coordinates
    void touchGridCoor(float touchX, float touchY, int blockSize){
        horizontalTouched = (int) touchX / blockSize;
        verticalTouched = (int) touchY / blockSize;
    }

    int findClosest(){
        int closest = 100000;
        for( int i = 0; i < sub.length; i++) {
            if(sub[i].hit == false) {
                // Did the shot hit the sub?
                isHit(i);
                findGap(i);
                if (closest > distanceFromSub) {
                    closest = distanceFromSub;
                }
            }
        }
        return closest;
    }

    // Did the shot hit the sub?
    void isHit(int i){
        sub[i].hit = horizontalTouched == sub[i].subHorizontalPosition
                && verticalTouched == sub[i].subVerticalPosition;
    }

    void findGap(int i){
        // How far away horizontally and vertically
        // was the shot from the sub
        int horizontalGap = (int) horizontalTouched -
                sub[i].subHorizontalPosition;
        int verticalGap = (int) verticalTouched -
                sub[i].subVerticalPosition;

        // Use Pythagoras's theorem to get the
        // distance travelled in a straight line
        distanceFromSub = (int) Math.sqrt(
                ((horizontalGap * horizontalGap) +
                        (verticalGap * verticalGap)));
    }

    boolean isAllHit(){
        int hitCount = 0;
        for( int i = 0; i < sub.length; i++) {
            if (sub[i].hit) {
                hitCount++;
            }
        }return (hitCount == subCount);
    }

    // This code says "BOOM!"
    void boom(int blockSize){

        gameView.setImageBitmap(blankBitmap);

        setScreenRed();
        setPaintWhite();

        //Draw huge Boom! text
        largeTextSize(blockSize);
        drawBoomText(blockSize);

        // Draw some text to prompt restarting
        drawResetPromt(blockSize);

        // Start a new game
        newGame();
    }

    void largeTextSize(int blockSize){ paint.setTextSize(blockSize * 10); }

    void drawBoomText(int blockSize){
        canvas.drawText("BOOM!", blockSize * 4,
                blockSize * 14, paint);
    }

    void drawResetPromt(int blockSize){
        paint.setTextSize(blockSize * 2);
        canvas.drawText("Take a shot to start again",
                blockSize * 8,
                blockSize * 18, paint);
    }

    // This code prints the debugging text
    // Should be tightly coupled to verify that
    // the original values are being shown
    public void printDebuggingText(){
        paint.setTextSize(grid.blockSize);
        canvas.drawText("numberHorizontalPixels = "
                        + grid.numberHorizontalPixels,
                50, grid.blockSize * 3, paint);
        canvas.drawText("numberVerticalPixels = "
                        + grid.numberVerticalPixels,
                50, grid.blockSize * 4, paint);
        canvas.drawText("blockSize = " + grid.blockSize,
                50, grid.blockSize * 5, paint);
        canvas.drawText("gridWidth = " + grid.gridWidth,
                50, grid.blockSize * 6, paint);
        canvas.drawText("gridHeight = " + grid.gridHeight,
                50, grid.blockSize * 7, paint);
        canvas.drawText("horizontalTouched = " +
                        horizontalTouched, 50,
                grid.blockSize * 8, paint);
        canvas.drawText("verticalTouched = " +
                        verticalTouched, 50,
                grid.blockSize * 9, paint);
        canvas.drawText("hit = " + isAllHit(),
                50, grid.blockSize * 10, paint);
        canvas.drawText("shotsTaken = " +
                        shotsTaken,
                50, grid.blockSize * 11, paint);
        canvas.drawText("debugging = " + debugging,
                50, grid.blockSize * 12, paint);
        for( int i = 0; i < sub.length; i++) {
            canvas.drawText("sub" + i + "HorizontalPosition = " +
                            sub[i].subHorizontalPosition, 50,
                    grid.blockSize * (13 + 2 * i), paint);
            canvas.drawText("sub" + i + "VerticalPosition = " +
                            sub[i].subVerticalPosition, 50,
                    grid.blockSize * (14 + 2 * i), paint);
        }

    }
}

