package com.gamecodeschool.subhunter;

import android.util.Log;

import java.util.Random;

public class CurrGame {

    int subHorizontalPosition;
    int subVerticalPosition;
    int shotsTaken;
    int gridWidth = 40;
    int gridHeight;

    /*
    This code will execute when a new
    game needs to be started. It will
    happen when the app is first started
    and after the player wins a game.
    */
    void newGame(){
        Random random = new Random();
        subHorizontalPosition = random.nextInt(gridWidth);
        subVerticalPosition = random.nextInt(gridHeight);
        shotsTaken = 0;

        Log.d("Debugging", "In newGame");

    }


}
