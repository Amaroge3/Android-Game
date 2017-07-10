package com.example.amaroge3.project4game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Amaroge3 on 7/21/16.
 */
public class GameFinish extends AppCompatActivity {


    Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_finish);
        myIntent = new Intent(this, MainActivity.class);


    }

    // method to restart game
    public void startAgain(View view){
        this.startActivity(myIntent);
        finish();
    }

    //method to quit game
    public void quitGame(View view){
        finish();
    }

}


