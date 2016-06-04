/*
        ************************************************************
        * Name:  Arjun Bastola                                     *
        * Project:  Project 2 - Ishido                             *
        * Class:  Artificial Intelligence                          *
        * Date:  13th January, 2016                                *
        * Class: Board (Activity Class)                            *
        *                                      *
        ************************************************************
*/



package com.arjunbastola.ishido;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class board extends ActionBarActivity {

    private MoveTile LatestMove = null;
    Tiles[][] boardTiles = new Tiles[10][14];
    Button[][] buttons = new Button[10][14];
    int HumanScore;
    Tiles btnPreview;
    Deck deck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hides the actionbar of the activity
        setContentView(R.layout.activity_board);

        MainActivity mainClass = new MainActivity();
        TextView text = (TextView) findViewById(R.id.textView2);
        if (mainClass.searchmode == 1) {
            text.setText("Depth-First Search");
            RadioGroup rg=(RadioGroup) findViewById(R.id.radioButtonGroup);
            for (int i=0; i<rg.getChildCount();i++){
                rg.getChildAt(i).setEnabled(false);
            }
            EditText edittext=(EditText) findViewById(R.id.editText);
            edittext.setEnabled(false);

        } else if (mainClass.searchmode == 2) {
            text.setText("Breadth-First Search");

            RadioGroup rg=(RadioGroup) findViewById(R.id.radioButtonGroup);
            for (int i=0; i<rg.getChildCount();i++){
                rg.getChildAt(i).setEnabled(false);
            }
            EditText edittext=(EditText) findViewById(R.id.editText);
            edittext.setEnabled(false);

        } else if (mainClass.searchmode == 3) {
            text.setText("Best-First Search");

            RadioGroup rg=(RadioGroup) findViewById(R.id.radioButtonGroup);
            for (int i=0; i<rg.getChildCount();i++){
                rg.getChildAt(i).setEnabled(false);
            }
            EditText edittext=(EditText) findViewById(R.id.editText);
            edittext.setEnabled(false);

        }else if (mainClass.searchmode == 4) {
            text.setText("Branch & Bound Search");

        }

        Button endGame=(Button) findViewById(R.id.option3);
        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(board.this)
                        .setTitle("Game End")
                        .setMessage("Out of Tiles. Your Score: 32")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        //Load State File
        try {
            SetState();

        } catch (Exception ex) {
            Toast.makeText(board.this,
                    "Your Message", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

        //Load Search Algorithms when Next Button is Clicked
        Button nextButton = (Button) findViewById(R.id.nextBtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainClass = new MainActivity();
                TextView text = (TextView) findViewById(R.id.textView2);

                Button nextStock = (Button) findViewById(R.id.previewBtn);

                if (mainClass.searchmode == 1) {
                    DepthFirstSearch(nextStock);

                } else if (mainClass.searchmode == 2) {
                    BreadthFirstSearch(nextStock);

                } else if (mainClass.searchmode == 3) {
                    BestFirstSearch(nextStock);

                }
            }
        });

        for (int i=1; i<=8; i++){
            for (int j=1; j<=12; j++){
                boardTiles[i][j].setButton(buttons[i][j]);
                boardTiles[i][j].button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button ClickedButton=(Button) v; //cast view to Button because we are sure the view is button
                        ButtonClicked(ClickedButton);

                    }
                });
            }
        }
    }

    public void ButtonClicked(Button ClickedButton){
        for (int i=1; i<=8; i++) {
            for (int j = 1; j <= 12; j++) {
                if (boardTiles[i][j].button==ClickedButton){ //Finds the Clicked Button ... goes through all buttons to find the match
                    // Toast.makeText(getApplicationContext(), "Button Found. Row: " + Integer.toString(boardTiles[i][j].getVertical()) + " Column: "+Integer.toString(boardTiles[i][j].getHorizontal()), Toast.LENGTH_LONG).show();
                    Button previewBtn=(Button)findViewById(R.id.previewBtn); //get button from the preview button
                    ColorDrawable buttonColor = (ColorDrawable) previewBtn.getBackground(); //get colordrawable from the preview button
                    int colorId=btnPreview.setColor();
                    Boolean somethingdone=false;
                    String setString=btnPreview.getString(); //get string/symbol/shape from the preview button
                    if (boardTiles[i][j].isFilledUp()==false){ //only use the tile if it has not been previously used
                       
                                        if((boardTiles[i-1][j].getString()==setString) || (boardTiles[i-1][j].setColor()==colorId)){
                                        //see if top has same symbol/color..if yes set properties of the preiew button
                                            Button currentBtn = (Button) boardTiles[i][j].button;
                                            currentBtn.setText(setString);
                                            // currentBtn.setTextColor(getResources().getColor(android.R.color.black));
                                            currentBtn.setBackgroundColor(colorId);
                                            TextView HumanScoreUpdate = (TextView) findViewById(R.id.HumanScore);
                                            HumanScore++; //Increase the score
                                            if((boardTiles[i-1][j].getString()==setString) && (boardTiles[i-1][j].setColor()==colorId)) {
                                                HumanScore++;
                                            }
                                            HumanScoreUpdate.setText(Integer.toString(HumanScore));
                                            boardTiles[i][j].FilledUp=true;
                                            boardTiles[i][j].button.setBackgroundColor(colorId);
                                            boardTiles[i][j].setString(setString);
                                            somethingdone=true;



                                        }
                                        if((boardTiles[i+1][j].getString()==setString) || (boardTiles[i+1][j].setColor()==colorId)){

                                        //see if bottom has same symbol/color..if yes set properties of the preiew button
                                            Button currentBtn = (Button) boardTiles[i][j].button;
                                            currentBtn.setText(setString);
                                           //  currentBtn.setTextColor(getResources().getColor(android.R.color.black));
                                            currentBtn.setBackgroundColor(colorId);
                                            TextView HumanScoreUpdate = (TextView) findViewById(R.id.HumanScore);
                                            HumanScore++;
                                            if((boardTiles[i+1][j].getString()==setString) && (boardTiles[i+1][j].setColor()==colorId)){
                                                HumanScore++;
                                            }
                                            HumanScoreUpdate.setText(Integer.toString(HumanScore));
                                            boardTiles[i][j].FilledUp=true;
                                            boardTiles[i][j].button.setBackgroundColor(colorId);
                                            boardTiles[i][j].setString(setString);
                                            somethingdone=true;


                                        }
                                        if((boardTiles[i][j-1].getString()==setString) || (boardTiles[i][j-1].setColor()==colorId)){
                                        //see if left has same symbol/color..if yes set properties of the preiew button
                                            Button currentBtn = (Button) boardTiles[i][j].button;
                                            currentBtn.setText(setString);
                                            // currentBtn.setTextColor(getResources().getColor(android.R.color.black));
                                            currentBtn.setBackgroundColor(colorId);
                                            TextView HumanScoreUpdate = (TextView) findViewById(R.id.HumanScore);
                                            HumanScore++; //Increase the score
                                            if ((boardTiles[i][j-1].getString()==setString) && (boardTiles[i][j-1].setColor()==colorId)){
                                                HumanScore++;
                                            }
                                            HumanScoreUpdate.setText(Integer.toString(HumanScore));
                                            boardTiles[i][j].FilledUp=true;
                                            boardTiles[i][j].button.setBackgroundColor(colorId);
                                            boardTiles[i][j].setString(setString);
                                            somethingdone=true;


                                        }
                                        if((boardTiles[i][j+1].getString()==setString) || (boardTiles[i][j+1].setColor()==colorId)){
                                        //see if right has same symbol/color..if yes set properties of the preiew button
                                            Button currentBtn = (Button) boardTiles[i][j].button;
                                            currentBtn.setText(setString);
                                            // currentBtn.setTextColor(getResources().getColor(android.R.color.black));
                                            currentBtn.setBackgroundColor(colorId);
                                            TextView HumanScoreUpdate = (TextView) findViewById(R.id.HumanScore);
                                            HumanScore++; //Increase the score
                                            if((boardTiles[i][j+1].getString()==setString) && (boardTiles[i][j+1].setColor()==colorId)){
                                                HumanScore++;
                                            }
                                            HumanScoreUpdate.setText(Integer.toString(HumanScore));
                                            boardTiles[i][j].FilledUp=true;
                                            boardTiles[i][j].button.setBackgroundColor(colorId);
                                            boardTiles[i][j].setString(setString);
                                            somethingdone=true;


                                        }
                                        if(((boardTiles[i-1][j].getString()=="") || (boardTiles[i-1][j].setColor()==7)) && ((boardTiles[i+1][j].getString()=="") || (boardTiles[i+1][j].setColor()==7)) && ((boardTiles[i][j+1].getString()=="") || (boardTiles[i][j+1].setColor()==7)) && ((boardTiles[i][j-1].getString()=="") || (boardTiles[i][j-1].setColor()==7))){
                                            //see if it's surrounding are unoccupied
                                            //score will not be updated in this condition
                                            Button currentBtn = (Button) boardTiles[i][j].button;
                                            currentBtn.setText(setString);
                                            // currentBtn.setTextColor(getResources().getColor(android.R.color.black));
                                            currentBtn.setBackgroundColor(colorId);
                                            TextView HumanScoreUpdate = (TextView) findViewById(R.id.HumanScore);
                                            HumanScoreUpdate.setText(Integer.toString(HumanScore));
                                            boardTiles[i][j].FilledUp=true;
                                            boardTiles[i][j].button.setBackgroundColor(colorId);
                                            boardTiles[i][j].setString(setString);

                                            Button previewButton = (Button) findViewById(R.id.previewBtn);
                                            int setPreview = deck.getStock();
                                            btnPreview = new Tiles(50, 50, setPreview);
                                            previewButton.setBackgroundColor(btnPreview.setColor());
                                            previewButton.setText(Integer.toString(btnPreview.symbol));
                                            somethingdone=true;

                                        }
                                    if(somethingdone){
                                        Button previewButton = (Button) findViewById(R.id.previewBtn);
                                        int setPreview = deck.getStock();
                                        btnPreview = new Tiles(50, 50, setPreview);
                                        previewButton.setBackgroundColor(btnPreview.setColor());
                                        previewButton.setText(Integer.toString(btnPreview.symbol));
                                    }


                                    }else{
                                        //if user clicks initially clicked tile, toast the error message
                                        Toast.makeText(getApplicationContext(), "Invalid Move. This tile has already been set.", Toast.LENGTH_LONG).show();
                                    }


                    }


                }
            }
        }

    //Depth First Search
    public void DepthFirstSearch(Button NextStock) {
        MoveTile moveTile;
        if (LatestMove == null) {

        } else {

        }

    }

    //Breadth First Search
    public void BreadthFirstSearch(Button NextStock) {

    }

    //Best First Search
    public void BestFirstSearch(Button NextStock) {

    }

    public void SetState() throws IOException {
        File dir = Environment.getExternalStorageDirectory();
        File yourFile = new File(dir, "BoardState.txt");

        State newState = new State(yourFile);

        int[] deckstock = new int[72];
        deckstock=newState.stockBoard;

        deck = new Deck(deckstock, 0, newState.numberOfStock);
        Button previewButton = (Button) findViewById(R.id.previewBtn);
        int setPreview = deck.getStock();
        btnPreview = new Tiles(50, 50, setPreview);
        previewButton.setBackgroundColor(btnPreview.setColor());
        previewButton.setText(Integer.toString(btnPreview.symbol));


        for (int i=0; i<10; i++){
            boardTiles[i][0]=new Tiles(i, 0, 70);
        }
        for (int i=0; i<14; i++){
            boardTiles[0][i]=new Tiles(0, i, 70);
        }
        for (int i=0; i<10; i++){
            boardTiles[i][13]=new Tiles(i, 13, 70);
        }
        for (int i=0; i<14; i++){
            boardTiles[9][i]=new Tiles(9, i, 70);
        }

        boardTiles[1][1]=new Tiles(1, 1, newState.layoutBoard[1][1]);
        buttons[1][1]=(Button) findViewById(R.id.btn11);
        buttons[1][1].setText(Integer.toString(boardTiles[1][1].symbol));
        buttons[1][1].setTextColor(Color.WHITE);
        buttons[1][1].setBackgroundColor(boardTiles[1][1].setColor());

        boardTiles[1][2]=new Tiles(1, 2, newState.layoutBoard[1][2]);
        buttons[1][2]=(Button) findViewById(R.id.btn12);
        buttons[1][2].setText(Integer.toString(boardTiles[1][2].symbol));
        buttons[1][2].setTextColor(Color.WHITE);
        buttons[1][2].setBackgroundColor(boardTiles[1][2].setColor());

        boardTiles[1][3]=new Tiles(1, 3, newState.layoutBoard[1][3]);
        buttons[1][3]=(Button) findViewById(R.id.btn13);
        buttons[1][3].setText(Integer.toString(boardTiles[1][3].symbol));
        buttons[1][3].setTextColor(Color.WHITE);
        buttons[1][3].setBackgroundColor(boardTiles[1][3].setColor());

        boardTiles[1][4]=new Tiles(1, 4, newState.layoutBoard[1][4]);
        buttons[1][4]=(Button) findViewById(R.id.btn14);
        buttons[1][4].setText(Integer.toString(boardTiles[1][4].symbol));
        buttons[1][4].setTextColor(Color.WHITE);
        buttons[1][4].setBackgroundColor(boardTiles[1][4].setColor());

        boardTiles[1][5]=new Tiles(1, 5, newState.layoutBoard[1][5]);
        buttons[1][5]=(Button) findViewById(R.id.btn15);
        buttons[1][5].setText(Integer.toString(boardTiles[1][5].symbol));
        buttons[1][5].setTextColor(Color.WHITE);
        buttons[1][5].setBackgroundColor(boardTiles[1][5].setColor());

        boardTiles[1][6]=new Tiles(1, 6, newState.layoutBoard[1][6]);
        buttons[1][6]=(Button) findViewById(R.id.btn16);
        buttons[1][6].setText(Integer.toString(boardTiles[1][6].symbol));
        buttons[1][6].setTextColor(Color.WHITE);
        buttons[1][6].setBackgroundColor(boardTiles[1][6].setColor());

        boardTiles[1][7]=new Tiles(1, 7, newState.layoutBoard[1][7]);
        buttons[1][7]=(Button) findViewById(R.id.btn17);
        buttons[1][7].setText(Integer.toString(boardTiles[1][7].symbol));
        buttons[1][7].setTextColor(Color.WHITE);
        buttons[1][7].setBackgroundColor(boardTiles[1][7].setColor());

        boardTiles[1][8]=new Tiles(1, 8, newState.layoutBoard[1][8]);
        buttons[1][8]=(Button) findViewById(R.id.btn18);
        buttons[1][8].setText(Integer.toString(boardTiles[1][8].symbol));
        buttons[1][8].setTextColor(Color.WHITE);
        buttons[1][8].setBackgroundColor(boardTiles[1][8].setColor());

        boardTiles[1][9]=new Tiles(1, 9, newState.layoutBoard[1][9]);
        buttons[1][9]=(Button) findViewById(R.id.btn19);
        buttons[1][9].setText(Integer.toString(boardTiles[1][9].symbol));
        buttons[1][9].setTextColor(Color.WHITE);
        buttons[1][9].setBackgroundColor(boardTiles[1][9].setColor());

        boardTiles[1][10]=new Tiles(1, 10, newState.layoutBoard[1][10]);
        buttons[1][10]=(Button) findViewById(R.id.btn110);
        buttons[1][10].setText(Integer.toString(boardTiles[1][10].symbol));
        buttons[1][10].setTextColor(Color.WHITE);
        buttons[1][10].setBackgroundColor(boardTiles[1][10].setColor());

        boardTiles[1][11]=new Tiles(1, 11, newState.layoutBoard[1][11]);
        buttons[1][11]=(Button) findViewById(R.id.btn111);
        buttons[1][11].setText(Integer.toString(boardTiles[1][11].symbol));
        buttons[1][11].setTextColor(Color.WHITE);
        buttons[1][11].setBackgroundColor(boardTiles[1][11].setColor());

        boardTiles[1][12]=new Tiles(1, 12, newState.layoutBoard[1][12]);
        buttons[1][12]=(Button) findViewById(R.id.btn112);
        buttons[1][12].setText(Integer.toString(boardTiles[1][12].symbol));
        buttons[1][12].setTextColor(Color.WHITE);
        buttons[1][12].setBackgroundColor(boardTiles[1][12].setColor());

        boardTiles[2][1]=new Tiles(2, 1, newState.layoutBoard[2][1]);
        buttons[2][1]=(Button) findViewById(R.id.btn21);
        buttons[2][1].setText(Integer.toString(boardTiles[2][1].symbol));
        buttons[2][1].setTextColor(Color.WHITE);
        buttons[2][1].setBackgroundColor(boardTiles[2][1].setColor());

        boardTiles[2][2]=new Tiles(2, 2, newState.layoutBoard[2][2]);
        buttons[2][2]=(Button) findViewById(R.id.btn22);
        buttons[2][2].setText(Integer.toString(boardTiles[2][2].symbol));
        buttons[2][2].setTextColor(Color.WHITE);
        buttons[2][2].setBackgroundColor(boardTiles[2][2].setColor());

        boardTiles[2][3]=new Tiles(2, 3, newState.layoutBoard[2][3]);
        buttons[2][3]=(Button) findViewById(R.id.btn23);
        buttons[2][3].setText(Integer.toString(boardTiles[2][3].symbol));
        buttons[2][3].setTextColor(Color.WHITE);
        buttons[2][3].setBackgroundColor(boardTiles[2][3].setColor());

        boardTiles[2][4]=new Tiles(2, 4, newState.layoutBoard[2][4]);
        buttons[2][4]=(Button) findViewById(R.id.btn24);
        buttons[2][4].setText(Integer.toString(boardTiles[2][4].symbol));
        buttons[2][4].setTextColor(Color.WHITE);
        buttons[2][4].setBackgroundColor(boardTiles[2][4].setColor());

        boardTiles[2][5]=new Tiles(2, 5, newState.layoutBoard[2][5]);
        buttons[2][5]=(Button) findViewById(R.id.btn25);
        buttons[2][5].setText(Integer.toString(boardTiles[2][5].symbol));
        buttons[2][5].setTextColor(Color.WHITE);
        buttons[2][5].setBackgroundColor(boardTiles[2][5].setColor());

        boardTiles[2][6]=new Tiles(2, 6, newState.layoutBoard[2][6]);
        buttons[2][6]=(Button) findViewById(R.id.btn26);
        buttons[2][6].setText(Integer.toString(boardTiles[2][6].symbol));
        buttons[2][6].setTextColor(Color.WHITE);
        buttons[2][6].setBackgroundColor(boardTiles[2][6].setColor());

        boardTiles[2][7]=new Tiles(2, 7, newState.layoutBoard[2][7]);
        buttons[2][7]=(Button) findViewById(R.id.btn27);
        buttons[2][7].setText(Integer.toString(boardTiles[2][7].symbol));
        buttons[2][7].setTextColor(Color.WHITE);
        buttons[2][7].setBackgroundColor(boardTiles[2][7].setColor());

        boardTiles[2][8]=new Tiles(2, 8, newState.layoutBoard[2][8]);
        buttons[2][8]=(Button) findViewById(R.id.btn28);
        buttons[2][8].setText(Integer.toString(boardTiles[2][8].symbol));
        buttons[2][8].setTextColor(Color.WHITE);
        buttons[2][8].setBackgroundColor(boardTiles[2][8].setColor());

        boardTiles[2][9]=new Tiles(2, 9, newState.layoutBoard[2][9]);
        buttons[2][9]=(Button) findViewById(R.id.btn29);
        buttons[2][9].setText(Integer.toString(boardTiles[2][9].symbol));
        buttons[2][9].setTextColor(Color.WHITE);
        buttons[2][9].setBackgroundColor(boardTiles[2][9].setColor());

        boardTiles[2][10]=new Tiles(2, 10, newState.layoutBoard[2][10]);
        buttons[2][10]=(Button) findViewById(R.id.btn210);
        buttons[2][10].setText(Integer.toString(boardTiles[2][10].symbol));
        buttons[2][10].setTextColor(Color.WHITE);
        buttons[2][10].setBackgroundColor(boardTiles[2][10].setColor());

        boardTiles[2][11]=new Tiles(2, 11, newState.layoutBoard[2][11]);
        buttons[2][11]=(Button) findViewById(R.id.btn211);
        buttons[2][11].setText(Integer.toString(boardTiles[2][11].symbol));
        buttons[2][11].setTextColor(Color.WHITE);
        buttons[2][11].setBackgroundColor(boardTiles[2][11].setColor());

        boardTiles[2][12]=new Tiles(2, 12, newState.layoutBoard[2][12]);
        buttons[2][12]=(Button) findViewById(R.id.btn212);
        buttons[2][12].setText(Integer.toString(boardTiles[2][12].symbol));
        buttons[2][12].setTextColor(Color.WHITE);
        buttons[2][12].setBackgroundColor(boardTiles[2][12].setColor());

        boardTiles[3][1]=new Tiles(3, 1, newState.layoutBoard[3][1]);
        buttons[3][1]=(Button) findViewById(R.id.btn31);
        buttons[3][1].setText(Integer.toString(boardTiles[3][1].symbol));
        buttons[3][1].setTextColor(Color.WHITE);
        buttons[3][1].setBackgroundColor(boardTiles[3][1].setColor());

        boardTiles[3][2]=new Tiles(3, 2, newState.layoutBoard[3][2]);
        buttons[3][2]=(Button) findViewById(R.id.btn32);
        buttons[3][2].setText(Integer.toString(boardTiles[3][2].symbol));
        buttons[3][2].setTextColor(Color.WHITE);
        buttons[3][2].setBackgroundColor(boardTiles[3][2].setColor());

        boardTiles[3][3]=new Tiles(3, 3, newState.layoutBoard[3][3]);
        buttons[3][3]=(Button) findViewById(R.id.btn33);
        buttons[3][3].setText(Integer.toString(boardTiles[3][3].symbol));
        buttons[3][3].setTextColor(Color.WHITE);
        buttons[3][3].setBackgroundColor(boardTiles[3][3].setColor());

        boardTiles[3][4]=new Tiles(3, 4, newState.layoutBoard[3][4]);
        buttons[3][4]=(Button) findViewById(R.id.btn34);
        buttons[3][4].setText(Integer.toString(boardTiles[3][4].symbol));
        buttons[3][4].setTextColor(Color.WHITE);
        buttons[3][4].setBackgroundColor(boardTiles[3][4].setColor());

        boardTiles[3][5]=new Tiles(3, 5, newState.layoutBoard[3][5]);
        buttons[3][5]=(Button) findViewById(R.id.btn35);
        buttons[3][5].setText(Integer.toString(boardTiles[3][5].symbol));
        buttons[3][5].setTextColor(Color.WHITE);
        buttons[3][5].setBackgroundColor(boardTiles[3][5].setColor());

        boardTiles[3][6]=new Tiles(3, 6, newState.layoutBoard[3][6]);
        buttons[3][6]=(Button) findViewById(R.id.btn36);
        buttons[3][6].setText(Integer.toString(boardTiles[3][6].symbol));
        buttons[3][6].setTextColor(Color.WHITE);
        buttons[3][6].setBackgroundColor(boardTiles[3][6].setColor());

        boardTiles[3][7]=new Tiles(3, 7, newState.layoutBoard[3][7]);
        buttons[3][7]=(Button) findViewById(R.id.btn37);
        buttons[3][7].setText(Integer.toString(boardTiles[3][7].symbol));
        buttons[3][7].setTextColor(Color.WHITE);
        buttons[3][7].setBackgroundColor(boardTiles[3][7].setColor());

        boardTiles[3][8]=new Tiles(3, 8, newState.layoutBoard[3][8]);
        buttons[3][8]=(Button) findViewById(R.id.btn38);
        buttons[3][8].setText(Integer.toString(boardTiles[3][8].symbol));
        buttons[3][8].setTextColor(Color.WHITE);
        buttons[3][8].setBackgroundColor(boardTiles[3][8].setColor());

        boardTiles[3][9]=new Tiles(3, 9, newState.layoutBoard[3][9]);
        buttons[3][9]=(Button) findViewById(R.id.btn39);
        buttons[3][9].setText(Integer.toString(boardTiles[3][9].symbol));
        buttons[3][9].setTextColor(Color.WHITE);
        buttons[3][9].setBackgroundColor(boardTiles[3][9].setColor());

        boardTiles[3][10]=new Tiles(3, 10, newState.layoutBoard[3][10]);
        buttons[3][10]=(Button) findViewById(R.id.btn310);
        buttons[3][10].setText(Integer.toString(boardTiles[3][10].symbol));
        buttons[3][10].setTextColor(Color.WHITE);
        buttons[3][10].setBackgroundColor(boardTiles[3][10].setColor());

        boardTiles[3][11]=new Tiles(3, 11, newState.layoutBoard[3][11]);
        buttons[3][11]=(Button) findViewById(R.id.btn311);
        buttons[3][11].setText(Integer.toString(boardTiles[3][11].symbol));
        buttons[3][11].setTextColor(Color.WHITE);
        buttons[3][11].setBackgroundColor(boardTiles[3][11].setColor());

        boardTiles[3][12]=new Tiles(3, 12, newState.layoutBoard[3][12]);
        buttons[3][12]=(Button) findViewById(R.id.btn312);
        buttons[3][12].setText(Integer.toString(boardTiles[3][12].symbol));
        buttons[3][12].setTextColor(Color.WHITE);
        buttons[3][12].setBackgroundColor(boardTiles[3][12].setColor());

        boardTiles[4][1]=new Tiles(4, 1, newState.layoutBoard[4][1]);
        buttons[4][1]=(Button) findViewById(R.id.btn41);
        buttons[4][1].setText(Integer.toString(boardTiles[4][1].symbol));
        buttons[4][1].setTextColor(Color.WHITE);
        buttons[4][1].setBackgroundColor(boardTiles[4][1].setColor());

        boardTiles[4][2]=new Tiles(4, 2, newState.layoutBoard[4][2]);
        buttons[4][2]=(Button) findViewById(R.id.btn42);
        buttons[4][2].setText(Integer.toString(boardTiles[4][2].symbol));
        buttons[4][2].setTextColor(Color.WHITE);
        buttons[4][2].setBackgroundColor(boardTiles[4][2].setColor());

        boardTiles[4][3]=new Tiles(4, 3, newState.layoutBoard[4][3]);
        buttons[4][3]=(Button) findViewById(R.id.btn43);
        buttons[4][3].setText(Integer.toString(boardTiles[4][3].symbol));
        buttons[4][3].setTextColor(Color.WHITE);
        buttons[4][3].setBackgroundColor(boardTiles[4][3].setColor());

        boardTiles[4][4]=new Tiles(4, 4, newState.layoutBoard[4][4]);
        buttons[4][4]=(Button) findViewById(R.id.btn44);
        buttons[4][4].setText(Integer.toString(boardTiles[4][4].symbol));
        buttons[4][4].setTextColor(Color.WHITE);
        buttons[4][4].setBackgroundColor(boardTiles[4][4].setColor());

        boardTiles[4][5]=new Tiles(4, 5, newState.layoutBoard[4][5]);
        buttons[4][5]=(Button) findViewById(R.id.btn45);
        buttons[4][5].setText(Integer.toString(boardTiles[4][5].symbol));
        buttons[4][5].setTextColor(Color.WHITE);
        buttons[4][5].setBackgroundColor(boardTiles[4][5].setColor());

        boardTiles[4][6]=new Tiles(4, 6, newState.layoutBoard[4][6]);
        buttons[4][6]=(Button) findViewById(R.id.btn46);
        buttons[4][6].setText(Integer.toString(boardTiles[4][6].symbol));
        buttons[4][6].setTextColor(Color.WHITE);
        buttons[4][6].setBackgroundColor(boardTiles[4][6].setColor());

        boardTiles[4][7]=new Tiles(4, 7, newState.layoutBoard[4][7]);
        buttons[4][7]=(Button) findViewById(R.id.btn47);
        buttons[4][7].setText(Integer.toString(boardTiles[4][7].symbol));
        buttons[4][7].setTextColor(Color.WHITE);
        buttons[4][7].setBackgroundColor(boardTiles[4][7].setColor());

        boardTiles[4][8]=new Tiles(4, 8, newState.layoutBoard[4][8]);
        buttons[4][8]=(Button) findViewById(R.id.btn48);
        buttons[4][8].setText(Integer.toString(boardTiles[4][8].symbol));
        buttons[4][8].setTextColor(Color.WHITE);
        buttons[4][8].setBackgroundColor(boardTiles[4][8].setColor());

        boardTiles[4][9]=new Tiles(4, 9, newState.layoutBoard[4][9]);
        buttons[4][9]=(Button) findViewById(R.id.btn49);
        buttons[4][9].setText(Integer.toString(boardTiles[4][9].symbol));
        buttons[4][9].setTextColor(Color.WHITE);
        buttons[4][9].setBackgroundColor(boardTiles[4][9].setColor());

        boardTiles[4][10]=new Tiles(4, 10, newState.layoutBoard[4][10]);
        buttons[4][10]=(Button) findViewById(R.id.btn410);
        buttons[4][10].setText(Integer.toString(boardTiles[4][10].symbol));
        buttons[4][10].setTextColor(Color.WHITE);
        buttons[4][10].setBackgroundColor(boardTiles[4][10].setColor());

        boardTiles[4][11]=new Tiles(4, 11, newState.layoutBoard[4][11]);
        buttons[4][11]=(Button) findViewById(R.id.btn411);
        buttons[4][11].setText(Integer.toString(boardTiles[4][11].symbol));
        buttons[4][11].setTextColor(Color.WHITE);
        buttons[4][11].setBackgroundColor(boardTiles[4][11].setColor());

        boardTiles[4][12]=new Tiles(4, 12, newState.layoutBoard[4][12]);
        buttons[4][12]=(Button) findViewById(R.id.btn412);
        buttons[4][12].setText(Integer.toString(boardTiles[4][12].symbol));
        buttons[4][12].setTextColor(Color.WHITE);
        buttons[4][12].setBackgroundColor(boardTiles[4][12].setColor());

        boardTiles[5][1]=new Tiles(5, 1, newState.layoutBoard[5][1]);
        buttons[5][1]=(Button) findViewById(R.id.btn51);
        buttons[5][1].setText(Integer.toString(boardTiles[5][1].symbol));
        buttons[5][1].setTextColor(Color.WHITE);
        buttons[5][1].setBackgroundColor(boardTiles[5][1].setColor());

        boardTiles[5][2]=new Tiles(5, 2, newState.layoutBoard[5][2]);
        buttons[5][2]=(Button) findViewById(R.id.btn52);
        buttons[5][2].setText(Integer.toString(boardTiles[5][2].symbol));
        buttons[5][2].setTextColor(Color.WHITE);
        buttons[5][2].setBackgroundColor(boardTiles[5][2].setColor());

        boardTiles[5][3]=new Tiles(5, 3, newState.layoutBoard[5][3]);
        buttons[5][3]=(Button) findViewById(R.id.btn53);
        buttons[5][3].setText(Integer.toString(boardTiles[5][3].symbol));
        buttons[5][3].setTextColor(Color.WHITE);
        buttons[5][3].setBackgroundColor(boardTiles[5][3].setColor());

        boardTiles[5][4]=new Tiles(5, 4, newState.layoutBoard[5][4]);
        buttons[5][4]=(Button) findViewById(R.id.btn54);
        buttons[5][4].setText(Integer.toString(boardTiles[5][4].symbol));
        buttons[5][4].setTextColor(Color.WHITE);
        buttons[5][4].setBackgroundColor(boardTiles[5][4].setColor());

        boardTiles[5][5]=new Tiles(5, 5, newState.layoutBoard[5][5]);
        buttons[5][5]=(Button) findViewById(R.id.btn55);
        buttons[5][5].setText(Integer.toString(boardTiles[5][5].symbol));
        buttons[5][5].setTextColor(Color.WHITE);
        buttons[5][5].setBackgroundColor(boardTiles[5][5].setColor());

        boardTiles[5][6]=new Tiles(5, 6, newState.layoutBoard[5][6]);
        buttons[5][6]=(Button) findViewById(R.id.btn56);
        buttons[5][6].setText(Integer.toString(boardTiles[5][6].symbol));
        buttons[5][6].setTextColor(Color.WHITE);
        buttons[5][6].setBackgroundColor(boardTiles[5][6].setColor());

        boardTiles[5][7]=new Tiles(5, 7, newState.layoutBoard[5][7]);
        buttons[5][7]=(Button) findViewById(R.id.btn57);
        buttons[5][7].setText(Integer.toString(boardTiles[5][7].symbol));
        buttons[5][7].setTextColor(Color.WHITE);
        buttons[5][7].setBackgroundColor(boardTiles[5][7].setColor());

        boardTiles[5][8]=new Tiles(5, 8, newState.layoutBoard[5][8]);
        buttons[5][8]=(Button) findViewById(R.id.btn58);
        buttons[5][8].setText(Integer.toString(boardTiles[5][8].symbol));
        buttons[5][8].setTextColor(Color.WHITE);
        buttons[5][8].setBackgroundColor(boardTiles[5][8].setColor());

        boardTiles[5][9]=new Tiles(5, 9, newState.layoutBoard[5][9]);
        buttons[5][9]=(Button) findViewById(R.id.btn59);
        buttons[5][9].setText(Integer.toString(boardTiles[5][9].symbol));
        buttons[5][9].setTextColor(Color.WHITE);
        buttons[5][9].setBackgroundColor(boardTiles[5][9].setColor());

        boardTiles[5][10]=new Tiles(5, 10, newState.layoutBoard[5][10]);
        buttons[5][10]=(Button) findViewById(R.id.btn510);
        buttons[5][10].setText(Integer.toString(boardTiles[5][10].symbol));
        buttons[5][10].setTextColor(Color.WHITE);
        buttons[5][10].setBackgroundColor(boardTiles[5][10].setColor());

        boardTiles[5][11]=new Tiles(5, 11, newState.layoutBoard[5][11]);
        buttons[5][11]=(Button) findViewById(R.id.btn511);
        buttons[5][11].setText(Integer.toString(boardTiles[5][11].symbol));
        buttons[5][11].setTextColor(Color.WHITE);
        buttons[5][11].setBackgroundColor(boardTiles[5][11].setColor());

        boardTiles[5][12]=new Tiles(5, 12, newState.layoutBoard[5][12]);
        buttons[5][12]=(Button) findViewById(R.id.btn512);
        buttons[5][12].setText(Integer.toString(boardTiles[5][12].symbol));
        buttons[5][12].setTextColor(Color.WHITE);
        buttons[5][12].setBackgroundColor(boardTiles[5][12].setColor());

        boardTiles[6][1]=new Tiles(6, 1, newState.layoutBoard[6][1]);
        buttons[6][1]=(Button) findViewById(R.id.btn61);
        buttons[6][1].setText(Integer.toString(boardTiles[6][1].symbol));
        buttons[6][1].setTextColor(Color.WHITE);
        buttons[6][1].setBackgroundColor(boardTiles[6][1].setColor());

        boardTiles[6][2]=new Tiles(6, 2, newState.layoutBoard[6][2]);
        buttons[6][2]=(Button) findViewById(R.id.btn62);
        buttons[6][2].setText(Integer.toString(boardTiles[6][2].symbol));
        buttons[6][2].setTextColor(Color.WHITE);
        buttons[6][2].setBackgroundColor(boardTiles[6][2].setColor());

        boardTiles[6][3]=new Tiles(6, 3, newState.layoutBoard[6][3]);
        buttons[6][3]=(Button) findViewById(R.id.btn63);
        buttons[6][3].setText(Integer.toString(boardTiles[6][3].symbol));
        buttons[6][3].setTextColor(Color.WHITE);
        buttons[6][3].setBackgroundColor(boardTiles[6][3].setColor());

        boardTiles[6][4]=new Tiles(6, 4, newState.layoutBoard[6][4]);
        buttons[6][4]=(Button) findViewById(R.id.btn64);
        buttons[6][4].setText(Integer.toString(boardTiles[6][4].symbol));
        buttons[6][4].setTextColor(Color.WHITE);
        buttons[6][4].setBackgroundColor(boardTiles[6][4].setColor());

        boardTiles[6][5]=new Tiles(6, 5, newState.layoutBoard[6][5]);
        buttons[6][5]=(Button) findViewById(R.id.btn65);
        buttons[6][5].setText(Integer.toString(boardTiles[6][5].symbol));
        buttons[6][5].setTextColor(Color.WHITE);
        buttons[6][5].setBackgroundColor(boardTiles[6][5].setColor());

        boardTiles[6][6]=new Tiles(6, 6, newState.layoutBoard[6][6]);
        buttons[6][6]=(Button) findViewById(R.id.btn66);
        buttons[6][6].setText(Integer.toString(boardTiles[6][6].symbol));
        buttons[6][6].setTextColor(Color.WHITE);
        buttons[6][6].setBackgroundColor(boardTiles[6][6].setColor());

        boardTiles[6][7]=new Tiles(6, 7, newState.layoutBoard[6][7]);
        buttons[6][7]=(Button) findViewById(R.id.btn67);
        buttons[6][7].setText(Integer.toString(boardTiles[6][7].symbol));
        buttons[6][7].setTextColor(Color.WHITE);
        buttons[6][7].setBackgroundColor(boardTiles[6][7].setColor());

        boardTiles[6][8]=new Tiles(6, 8, newState.layoutBoard[6][8]);
        buttons[6][8]=(Button) findViewById(R.id.btn68);
        buttons[6][8].setText(Integer.toString(boardTiles[6][8].symbol));
        buttons[6][8].setTextColor(Color.WHITE);
        buttons[6][8].setBackgroundColor(boardTiles[6][8].setColor());

        boardTiles[6][9]=new Tiles(6, 9, newState.layoutBoard[6][9]);
        buttons[6][9]=(Button) findViewById(R.id.btn69);
        buttons[6][9].setText(Integer.toString(boardTiles[6][9].symbol));
        buttons[6][9].setTextColor(Color.WHITE);
        buttons[6][9].setBackgroundColor(boardTiles[6][9].setColor());

        boardTiles[6][10]=new Tiles(6, 10, newState.layoutBoard[6][10]);
        buttons[6][10]=(Button) findViewById(R.id.btn610);
        buttons[6][10].setText(Integer.toString(boardTiles[6][10].symbol));
        buttons[6][10].setTextColor(Color.WHITE);
        buttons[6][10].setBackgroundColor(boardTiles[6][10].setColor());

        boardTiles[6][11]=new Tiles(6, 11, newState.layoutBoard[6][11]);
        buttons[6][11]=(Button) findViewById(R.id.btn611);
        buttons[6][11].setText(Integer.toString(boardTiles[6][11].symbol));
        buttons[6][11].setTextColor(Color.WHITE);
        buttons[6][11].setBackgroundColor(boardTiles[6][11].setColor());

        boardTiles[6][12]=new Tiles(6, 12, newState.layoutBoard[6][12]);
        buttons[6][12]=(Button) findViewById(R.id.btn612);
        buttons[6][12].setText(Integer.toString(boardTiles[6][12].symbol));
        buttons[6][12].setTextColor(Color.WHITE);
        buttons[6][12].setBackgroundColor(boardTiles[6][12].setColor());

        boardTiles[7][1]=new Tiles(7, 1, newState.layoutBoard[7][1]);
        buttons[7][1]=(Button) findViewById(R.id.btn71);
        buttons[7][1].setText(Integer.toString(boardTiles[7][1].symbol));
        buttons[7][1].setTextColor(Color.WHITE);
        buttons[7][1].setBackgroundColor(boardTiles[7][1].setColor());

        boardTiles[7][2]=new Tiles(7, 2, newState.layoutBoard[7][2]);
        buttons[7][2]=(Button) findViewById(R.id.btn72);
        buttons[7][2].setText(Integer.toString(boardTiles[7][2].symbol));
        buttons[7][2].setTextColor(Color.WHITE);
        buttons[7][2].setBackgroundColor(boardTiles[7][2].setColor());

        boardTiles[7][3]=new Tiles(7, 3, newState.layoutBoard[7][3]);
        buttons[7][3]=(Button) findViewById(R.id.btn73);
        buttons[7][3].setText(Integer.toString(boardTiles[7][3].symbol));
        buttons[7][3].setTextColor(Color.WHITE);
        buttons[7][3].setBackgroundColor(boardTiles[7][3].setColor());

        boardTiles[7][4]=new Tiles(7, 4, newState.layoutBoard[7][4]);
        buttons[7][4]=(Button) findViewById(R.id.btn74);
        buttons[7][4].setText(Integer.toString(boardTiles[7][4].symbol));
        buttons[7][4].setTextColor(Color.WHITE);
        buttons[7][4].setBackgroundColor(boardTiles[7][4].setColor());

        boardTiles[7][5]=new Tiles(7, 5, newState.layoutBoard[7][5]);
        buttons[7][5]=(Button) findViewById(R.id.btn75);
        buttons[7][5].setText(Integer.toString(boardTiles[7][5].symbol));
        buttons[7][5].setTextColor(Color.WHITE);
        buttons[7][5].setBackgroundColor(boardTiles[7][5].setColor());

        boardTiles[7][6]=new Tiles(7, 6, newState.layoutBoard[7][6]);
        buttons[7][6]=(Button) findViewById(R.id.btn76);
        buttons[7][6].setText(Integer.toString(boardTiles[7][6].symbol));
        buttons[7][6].setTextColor(Color.WHITE);
        buttons[7][6].setBackgroundColor(boardTiles[7][6].setColor());

        boardTiles[7][7]=new Tiles(7, 7, newState.layoutBoard[7][7]);
        buttons[7][7]=(Button) findViewById(R.id.btn77);
        buttons[7][7].setText(Integer.toString(boardTiles[7][7].symbol));
        buttons[7][7].setTextColor(Color.WHITE);
        buttons[7][7].setBackgroundColor(boardTiles[7][7].setColor());

        boardTiles[7][8]=new Tiles(7, 8, newState.layoutBoard[7][8]);
        buttons[7][8]=(Button) findViewById(R.id.btn78);
        buttons[7][8].setText(Integer.toString(boardTiles[7][8].symbol));
        buttons[7][8].setTextColor(Color.WHITE);
        buttons[7][8].setBackgroundColor(boardTiles[7][8].setColor());

        boardTiles[7][9]=new Tiles(7, 9, newState.layoutBoard[7][9]);
        buttons[7][9]=(Button) findViewById(R.id.btn79);
        buttons[7][9].setText(Integer.toString(boardTiles[7][9].symbol));
        buttons[7][9].setTextColor(Color.WHITE);
        buttons[7][9].setBackgroundColor(boardTiles[7][9].setColor());

        boardTiles[7][10]=new Tiles(7, 10, newState.layoutBoard[7][10]);
        buttons[7][10]=(Button) findViewById(R.id.btn710);
        buttons[7][10].setText(Integer.toString(boardTiles[7][10].symbol));
        buttons[7][10].setTextColor(Color.WHITE);
        buttons[7][10].setBackgroundColor(boardTiles[7][10].setColor());

        boardTiles[7][11]=new Tiles(7, 11, newState.layoutBoard[7][11]);
        buttons[7][11]=(Button) findViewById(R.id.btn711);
        buttons[7][11].setText(Integer.toString(boardTiles[7][11].symbol));
        buttons[7][11].setTextColor(Color.WHITE);
        buttons[7][11].setBackgroundColor(boardTiles[7][11].setColor());

        boardTiles[7][12]=new Tiles(7, 12, newState.layoutBoard[7][12]);
        buttons[7][12]=(Button) findViewById(R.id.btn712);
        buttons[7][12].setText(Integer.toString(boardTiles[7][12].symbol));
        buttons[7][12].setTextColor(Color.WHITE);
        buttons[7][12].setBackgroundColor(boardTiles[7][12].setColor());

        boardTiles[8][1]=new Tiles(8, 1, newState.layoutBoard[8][1]);
        buttons[8][1]=(Button) findViewById(R.id.btn81);
        buttons[8][1].setText(Integer.toString(boardTiles[8][1].symbol));
        buttons[8][1].setTextColor(Color.WHITE);
        buttons[8][1].setBackgroundColor(boardTiles[8][1].setColor());

        boardTiles[8][2]=new Tiles(8, 2, newState.layoutBoard[8][2]);
        buttons[8][2]=(Button) findViewById(R.id.btn82);
        buttons[8][2].setText(Integer.toString(boardTiles[8][2].symbol));
        buttons[8][2].setTextColor(Color.WHITE);
        buttons[8][2].setBackgroundColor(boardTiles[8][2].setColor());

        boardTiles[8][3]=new Tiles(8, 3, newState.layoutBoard[8][3]);
        buttons[8][3]=(Button) findViewById(R.id.btn83);
        buttons[8][3].setText(Integer.toString(boardTiles[8][3].symbol));
        buttons[8][3].setTextColor(Color.WHITE);
        buttons[8][3].setBackgroundColor(boardTiles[8][3].setColor());

        boardTiles[8][4]=new Tiles(8, 4, newState.layoutBoard[8][4]);
        buttons[8][4]=(Button) findViewById(R.id.btn84);
        buttons[8][4].setText(Integer.toString(boardTiles[8][4].symbol));
        buttons[8][4].setTextColor(Color.WHITE);
        buttons[8][4].setBackgroundColor(boardTiles[8][4].setColor());

        boardTiles[8][5]=new Tiles(8, 5, newState.layoutBoard[8][5]);
        buttons[8][5]=(Button) findViewById(R.id.btn85);
        buttons[8][5].setText(Integer.toString(boardTiles[8][5].symbol));
        buttons[8][5].setTextColor(Color.WHITE);
        buttons[8][5].setBackgroundColor(boardTiles[8][5].setColor());

        boardTiles[8][6]=new Tiles(8, 6, newState.layoutBoard[8][6]);
        buttons[8][6]=(Button) findViewById(R.id.btn86);
        buttons[8][6].setText(Integer.toString(boardTiles[8][6].symbol));
        buttons[8][6].setTextColor(Color.WHITE);
        buttons[8][6].setBackgroundColor(boardTiles[8][6].setColor());

        boardTiles[8][7]=new Tiles(8, 7, newState.layoutBoard[8][7]);
        buttons[8][7]=(Button) findViewById(R.id.btn87);
        buttons[8][7].setText(Integer.toString(boardTiles[8][7].symbol));
        buttons[8][7].setTextColor(Color.WHITE);
        buttons[8][7].setBackgroundColor(boardTiles[8][7].setColor());

        boardTiles[8][8]=new Tiles(8, 8, newState.layoutBoard[8][8]);
        buttons[8][8]=(Button) findViewById(R.id.btn88);
        buttons[8][8].setText(Integer.toString(boardTiles[8][8].symbol));
        buttons[8][8].setTextColor(Color.WHITE);
        buttons[8][8].setBackgroundColor(boardTiles[8][8].setColor());

        boardTiles[8][9]=new Tiles(8, 9, newState.layoutBoard[8][9]);
        buttons[8][9]=(Button) findViewById(R.id.btn89);
        buttons[8][9].setText(Integer.toString(boardTiles[8][9].symbol));
        buttons[8][9].setTextColor(Color.WHITE);
        buttons[8][9].setBackgroundColor(boardTiles[8][9].setColor());

        boardTiles[8][10]=new Tiles(8, 10, newState.layoutBoard[8][10]);
        buttons[8][10]=(Button) findViewById(R.id.btn810);
        buttons[8][10].setText(Integer.toString(boardTiles[8][10].symbol));
        buttons[8][10].setTextColor(Color.WHITE);
        buttons[8][10].setBackgroundColor(boardTiles[8][10].setColor());

        boardTiles[8][11]=new Tiles(8, 11, newState.layoutBoard[8][11]);
        buttons[8][11]=(Button) findViewById(R.id.btn811);
        buttons[8][11].setText(Integer.toString(boardTiles[8][11].symbol));
        buttons[8][11].setTextColor(Color.WHITE);
        buttons[8][11].setBackgroundColor(boardTiles[8][11].setColor());

        boardTiles[8][12]=new Tiles(8, 12, newState.layoutBoard[8][12]);
        buttons[8][12]=(Button) findViewById(R.id.btn812);
        buttons[8][12].setText(Integer.toString(boardTiles[8][12].symbol));
        buttons[8][12].setTextColor(Color.WHITE);
        buttons[8][12].setBackgroundColor(boardTiles[8][12].setColor());

        HumanScore=newState.scoreBoard;


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

