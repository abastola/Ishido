/*
        ************************************************************
        * Name:  Arjun Bastola                                     *
        * Project:  Project 2 - Ishido                             *
        * Class:  Artificial Intelligence                          *
        * Date:  13th January, 2016                                *
        * Class: Main(Activity Class)                            *
        *                                      *
        ************************************************************
*/

package com.arjunbastola.ishido;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends ActionBarActivity {
    static int searchmode=0;
    static String filename;
    static boolean TextEntered=false;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hides the system bar
        setContentView(R.layout.activity_main);

        //When Button is clicked it sends us to new activity with java class- board
        Button DepF= (Button) findViewById(R.id.newgame);
        Button BreF= (Button) findViewById(R.id.loadgame);


        DepF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //on click listener to onClickBoardActivity button
                searchmode=1;
                //ask user to input the file name


                Intent myIntent=new Intent(MainActivity.this, board.class);
                startActivity(myIntent); //starts activity board

            }
        });
        BreF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //on click listener to onClickBoardActivity button
                searchmode=2;

                AlertBox();



            }
        });


    }

    public void AlertBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter the filename of the file you want to Load:");

        // Set up the input
        final EditText input = new EditText(MainActivity.this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filename = input.getText().toString();
                    Intent myIntent = new Intent(MainActivity.this, board.class);
                    startActivity(myIntent); //starts activity board


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

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
