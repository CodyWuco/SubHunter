package com.gamecodeschool.subhunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;

public class Draw {

    int blockSize;

    // Here are all the objects(instances)
    // of classes that we need to do some drawing
    ImageView gameView;
    Bitmap blankBitmap;
    Canvas canvas;
    Paint paint;

    void draw(Grid grid, Sub[] sub, float horizontalTouched, float verticalTouched, int shotsTaken,int distanceFromSub) {
        gameView.setImageBitmap(blankBitmap);

        // Wipe the screen with a white color
        canvas.drawColor(Color.argb(255, 255, 255, 255));

        // Change the paint color to black
        paint.setColor(Color.argb(255, 0, 0, 0));

        // Draw the vertical lines of the grid
        grid.drawVertGridLines(canvas, paint);

        // Draw the horizontal lines of the grid
        grid.drawHoriGridLines(canvas, paint);

        // Draw the player's shot
        canvas.drawRect(horizontalTouched * blockSize,
                verticalTouched * blockSize,
                (horizontalTouched * blockSize) + blockSize,
                (verticalTouched * blockSize)+ blockSize,
                paint );

        drawSunk(sub);

        // Re-size the text appropriate for the
        // score and distance text
        paint.setTextSize(blockSize * 2);
        paint.setColor(Color.argb(255, 0, 0, 255));
        canvas.drawText(
                "Shots Taken: " + shotsTaken +
                        "  Distance: " + distanceFromSub,
                blockSize, blockSize * 1.75f,
                paint);


        Log.d("Debugging", "In draw");
      /*  if (debugging) {
            printDebuggingText();
        }*/
    }

    void drawSunk(Sub[] sub){
        // Change the paint color to red
        paint.setColor(Color.argb(255, 255, 0, 0));
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
        // Change the paint color to black
        paint.setColor(Color.argb(255, 0, 0, 0));
    }


}
