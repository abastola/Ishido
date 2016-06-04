/*
        ************************************************************
        * Name:  Arjun Bastola                                     *
        * Project:  Project 1 - Ishido                             *
        * Class:  Artificial Intelligence                          *
        * Date:  29th January, 2016                                *
        * Class: MainActivity  (Initiates the welcome screen)      *
        * Description: Contains a button which when clicked sends  *
        * the user to the board activity                           *
        ************************************************************
*/


package com.arjunbastola.ishido;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends ActionBarActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hides the system bar
        setContentView(R.layout.activity_main);

        //When Button is clicked it sends us to new activity with java class- board
        Button onClickBoardActivity= (Button) findViewById(R.id.angry_btn);
        onClickBoardActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //on click listener to onClickBoardActivity button
                Intent myIntent=new Intent(MainActivity.this, board.class);
                startActivity(myIntent); //starts activity board
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
