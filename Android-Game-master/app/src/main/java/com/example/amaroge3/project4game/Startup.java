package com.example.amaroge3.project4game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
/**
 * Created by Amaroge3 on 7/20/16.
 */
public class Startup extends AppCompatActivity {

    Intent myIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        myIntent = new Intent(this, MainActivity.class);
    }

    //method to start game
    public void startGame(View view){

        Startup.this.startActivity(myIntent);
        finish();
    }


    //method to quit game
    public void quitGame(View view){
        finish();
    }

}
