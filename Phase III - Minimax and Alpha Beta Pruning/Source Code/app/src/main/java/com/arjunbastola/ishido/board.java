/*
        ************************************************************
        * Name:  Arjun Bastola                                     *
        * Project:  Project 2 - Ishido                             *
        * Class:  Artificial Intelligence                          *
        * Date:  13th January, 2016                                *
        * Class: Board (Activity C  lass)                            *
        *                                      *
        ************************************************************
*/


package com.arjunbastola.ishido;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;


public class board extends ActionBarActivity {

    String coinToss, randomGenerated;

    int turn = 0;
    // private MoveTile LatestMove = null;
    static Tiles[][] boardTiles = new Tiles[10][14];
    Button[][] buttons = new Button[10][14];
    int HumanScore;
    Tiles btnPreview;
    Tiles btnCurrentPreview;
    Deck deck;
    Animation animation;
    Boolean firstplacementDone=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hides the actionbar of the activity
        setContentView(R.layout.activity_board);
        MainActivity mainClass = new MainActivity();

        //clear Animation
        //Tile Browse using the Next Button
        Button endAnimation = (Button) findViewById(R.id.EndAnimation);
        endAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i <= 8; i++) {
                    for (int j = 1; j <= 12; j++) {
                        buttons[i][j].clearAnimation();
                    }
                }


            }
        });


        //Save Game State
        Button saveStateFile = (Button) findViewById(R.id.SaveGame);
        saveStateFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskFileNameAlertBox();
            }
        });

        //Activate AI Algorihtm
        //Get the best Node: Score and Position
        Button ActivateButton = (Button) findViewById(R.id.ActivateComputer);
        ActivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Node bestNode = ComputerAlgorithmActivated();
                try {


                    TextView computer_score = (TextView) findViewById(R.id.ComputerScore);
                    int computer_current_score = Integer.parseInt(computer_score.getText().toString());

                    buttons[bestNode.moveTile.row][bestNode.moveTile.column].setBackgroundColor(getColorfromInteger(deck.returnCurrentPile() / 10));
                    buttons[bestNode.moveTile.row][bestNode.moveTile.column].setText("" + deck.returnCurrentPile() % 10);
                    boardTiles[bestNode.moveTile.row][bestNode.moveTile.column].color = deck.returnCurrentPile() / 10;
                    boardTiles[bestNode.moveTile.row][bestNode.moveTile.column].symbol = deck.returnCurrentPile() % 10;
                    boardTiles[bestNode.moveTile.row][bestNode.moveTile.column].FilledUp = true;

                    animation = new AlphaAnimation(0.0f, 1.0f);
                    animation.setDuration(50); //You can manage the blinking time with this parameter
                    animation.setStartOffset(20);
                    animation.setRepeatMode(Animation.REVERSE);
                    animation.setRepeatCount(Animation.INFINITE);

                    buttons[bestNode.moveTile.row][bestNode.moveTile.column].startAnimation(animation);


                    //update Score
                    computer_score.setText(Integer.toString(computer_current_score + bestNode.moveTile.score));

                    deck.nextButtonStartsHere++;

                    //update current stock and preview
                    //change preview Button get new stock
                    Button previewButton = (Button) findViewById(R.id.previewBtn);
                    int setPreview = deck.returnCurrentPile();
                    btnPreview = new Tiles(50, 50, setPreview);
                    previewButton.setBackgroundColor(btnPreview.setColor());
                    previewButton.setText(Integer.toString(btnPreview.symbol));

                    //change the current stock
                    Button CurrentStock = (Button) findViewById(R.id.currentStockwBtn);
                    int setCurrentStock = deck.returnCurrentPile();
                    btnCurrentPreview = new Tiles(50, 50, setCurrentStock);
                    CurrentStock.setBackgroundColor(btnCurrentPreview.setColor());
                    CurrentStock.setText(Integer.toString(btnCurrentPreview.symbol));


                    //set the turn to human after placing the tile
                    TextView playerTurn = (TextView) findViewById(R.id.NextPlayer);
                    playerTurn.setText("Human");
                    turn = 0;
                } catch (NullPointerException e) {
                    Toast.makeText(board.this, "Solution Doesn't Exist.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //Get Help for the user
        //Get the best Node: Score and Position
        //Blink the tile in that position to suggest user to place the tile there
        Button HelpButton = (Button) findViewById(R.id.option1);
        HelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuggestUserTheBestMove();
            }
        });


        //Load State File
        //get the file and load all contents on the board layout
        try {
            SetState();
            if (mainClass.searchmode == 1) {
                int turn = TossCoin();

            }
        } catch (Exception ex) {
            Toast.makeText(board.this,
                    "Your Message", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

        //Tile Browse using the Next Button
        Button nextButton = (Button) findViewById(R.id.nextBtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button previewButton = (Button) findViewById(R.id.previewBtn);
                int setPreview = deck.getRandomStock();
                btnPreview = new Tiles(50, 50, setPreview);
                previewButton.setBackgroundColor(btnPreview.setColor());
                previewButton.setText(Integer.toString(btnPreview.symbol));

            }
        });

        //Current Tile Preview
        Button CurrentStock = (Button) findViewById(R.id.currentStockwBtn);
        int setCurrentStock = deck.returnCurrentPile();
        btnCurrentPreview = new Tiles(50, 50, setCurrentStock);
        CurrentStock.setBackgroundColor(btnCurrentPreview.setColor());
        CurrentStock.setText(Integer.toString(btnCurrentPreview.symbol));


        //Set OnClick Listener to Buttons in the board
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 12; j++) {
                boardTiles[i][j].setButton(buttons[i][j]);
                boardTiles[i][j].button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button ClickedButton = (Button) v; //cast view to Button because we are sure the view is button
                        ButtonClicked(ClickedButton);

                    }
                });
            }
        }


    }




    public void SuggestUserTheBestMove() {
        AlertDialog.Builder alertadd = new AlertDialog.Builder(
                board.this);
        LayoutInflater factory = LayoutInflater.from(board.this);
        final View view = factory.inflate(R.layout.sample, null);
        alertadd.setView(view);
        alertadd.setNeutralButton("Loki Please help me", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                Node bestNode = ComputerAlgorithmActivated();
                Animation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setDuration(50); //You can manage the blinking time with this parameter
                animation.setStartOffset(20);
                animation.setRepeatMode(Animation.REVERSE);
                animation.setRepeatCount(Animation.INFINITE);
                try {
                    buttons[bestNode.moveTile.row][bestNode.moveTile.column].startAnimation(animation);
                } catch (NullPointerException e) {
                    Toast.makeText(board.this, "Solution Doesn't Exist.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        alertadd.show();
    }


    //change the select button when user clicks a button
    public void ButtonClicked(Button ClickedButton) {
        for (int i = 1; i <= 8; i++) {
            Boolean buttonfound = false;
            for (int j = 1; j <= 12; j++) {
                if (boardTiles[i][j].button == ClickedButton) {

                    buttonfound = true;
                    //Toast.makeText(board.this, "BtnPreview Color. The color " + btnPreview.color + " " + btnPreview.symbol + ".", Toast.LENGTH_SHORT).show();

                    //check if filled up
                    if (boardTiles[i][j].isFilledUp()) {
                        Toast.makeText(board.this, "Button once set can't be changed.", Toast.LENGTH_SHORT).show();

                    } else {
                        int match = CheckIfTileCanBePlaced(i, j);

                        if ((match % 10) == 4) {
                            int StockToBeApplied = deck.returnCurrentPile();
                            int a = StockToBeApplied / 10; //color
                            int b = StockToBeApplied % 10; //symbol
                            int returnColor = 0;
                            switch (a) {
                                case 1:
                                    returnColor = Color.RED;
                                    break;
                                case 2:
                                    returnColor = Color.GREEN;
                                    break;
                                case 3:
                                    returnColor = Color.GRAY;
                                    break;
                                case 4:
                                    returnColor = Color.BLACK;
                                    break;
                                case 5:
                                    returnColor = Color.MAGENTA;
                                    break;
                                case 6:
                                    returnColor = Color.BLUE;
                                    break;
                                default:
                                    returnColor = Color.WHITE;
                            }
                            ClickedButton.setBackgroundColor(returnColor);
                            ClickedButton.setText(Integer.toString(b));
                            TextView human_score = (TextView) findViewById(R.id.HumanScore);
                            firstplacementDone=true;

                            boardTiles[i][j].FilledUp = true;
                            boardTiles[i][j].color = btnCurrentPreview.color;
                            boardTiles[i][j].symbol = btnCurrentPreview.symbol;
                            deck.nextButtonStartsHere++;


                            int human_current_score = Integer.parseInt(human_score.getText().toString());
                            int human_update_score = human_current_score + match / 10;
                            human_score.setText(Integer.toString(human_update_score));


                            //change preview Button get new stock
                            Button previewButton = (Button) findViewById(R.id.previewBtn);
                            int setPreview = deck.returnCurrentPile();
                            btnPreview = new Tiles(50, 50, setPreview);
                            previewButton.setBackgroundColor(btnPreview.setColor());
                            previewButton.setText(Integer.toString(btnPreview.symbol));

                            //change the current stock
                            Button CurrentStock = (Button) findViewById(R.id.currentStockwBtn);
                            int setCurrentStock = deck.returnCurrentPile();
                            btnCurrentPreview = new Tiles(50, 50, setCurrentStock);
                            CurrentStock.setBackgroundColor(btnCurrentPreview.setColor());
                            CurrentStock.setText(Integer.toString(btnCurrentPreview.symbol));


                            //change the turn to computer
                            TextView playerTurn = (TextView) findViewById(R.id.NextPlayer);
                            playerTurn.setText("Computer");
                            turn = 1;


                        }
                    }


                }
                if (buttonfound) {
                    break;
                }

            }
            if (buttonfound) {
                break;
            }
        }
    }

    public int getColorfromInteger(int abc) {
        int returnColor = 0;
        switch (abc) {
            case 1:
                returnColor = Color.RED;
                break;
            case 2:
                returnColor = Color.GREEN;
                break;
            case 3:
                returnColor = Color.GRAY;
                break;
            case 4:
                returnColor = Color.BLACK;
                break;
            case 5:
                returnColor = Color.MAGENTA;
                break;
            case 6:
                returnColor = Color.BLUE;
                break;
            default:
                returnColor = Color.WHITE;
        }
        return returnColor;

    }


    //return's if tile can be placed or not also returns how much the score will be if the tile can be returned
    public int CheckIfTileCanBePlaced(int i, int j) {

        makeAllSurrundingSameAsPreviewButton();
        /*
        String m1=" ";

        String m2=" ";
        String m3=" ";
        String m4=" ";
        */

        int match = 0;
        int scoreToBeIncreased = 0;
        if (((boardTiles[i][j - 1]).color == (btnCurrentPreview.color) || (boardTiles[i][j - 1]).symbol == (btnCurrentPreview.symbol) || (boardTiles[i][j - 1]).symbol == 0)) {
            match++;
            if (((boardTiles[i][j - 1]).color == (btnCurrentPreview.color) || (boardTiles[i][j - 1]).symbol == (btnCurrentPreview.symbol))) {
                scoreToBeIncreased++;
            }
            // m1="match left ";

        }
        if (((boardTiles[i][j + 1]).color == (btnCurrentPreview.color) || (boardTiles[i][j + 1]).symbol == (btnCurrentPreview.symbol) || (boardTiles[i][j + 1]).symbol == 0)) {
            match++;
            if (((boardTiles[i][j + 1]).color == (btnCurrentPreview.color) || (boardTiles[i][j + 1]).symbol == (btnCurrentPreview.symbol))) {
                scoreToBeIncreased++;
            }
            // m2="match right ";

        }
        if (((boardTiles[i - 1][j]).color == (btnCurrentPreview.color) || (boardTiles[i - 1][j]).symbol == (btnCurrentPreview.symbol) || (boardTiles[i - 1][j]).symbol == 0)) {
            match++;
            if ((boardTiles[i - 1][j]).color == (btnCurrentPreview.color) || (boardTiles[i - 1][j]).symbol == (btnCurrentPreview.symbol)) {
                scoreToBeIncreased++;
            }
            // m3="match top ";
        }
        if (((boardTiles[i + 1][j]).color == (btnCurrentPreview.color) || (boardTiles[i + 1][j]).symbol == (btnCurrentPreview.symbol) || (boardTiles[i + 1][j]).symbol == 0)) {
            match++;
            if (((boardTiles[i + 1][j]).color == (btnCurrentPreview.color) || (boardTiles[i + 1][j]).symbol == (btnCurrentPreview.symbol))) {
                scoreToBeIncreased++;
            }
            // m4="match bottom ";
        }

        // Toast.makeText(board.this, ""+m1+m2+m3+m4, Toast.LENGTH_LONG).show();
        return scoreToBeIncreased * 10 + match;
    }


    public void makeAllSurrundingSameAsPreviewButton() {

        for (int i = 0; i < 10; i++) {
            boardTiles[i][0] = new Tiles(i, 0, 0);
        }
        for (int i = 0; i < 14; i++) {
            boardTiles[0][i] = new Tiles(0, i, 0);
        }
        for (int i = 0; i < 10; i++) {
            boardTiles[i][13] = new Tiles(i, 13, 0);
        }
        for (int i = 0; i < 14; i++) {
            boardTiles[9][i] = new Tiles(9, i, 0);
        }


    }

    public int TossCoin() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(board.this);
        builder1.setMessage("Coin Toss!! Please select HEAD or TILE:");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "HEAD",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        coinToss = "Head";
                        Random randomGenerator = new Random();
                        int randomNumber = randomGenerator.nextInt(100);
                        if ((randomNumber % 2) == 0) {
                            randomGenerated = "Head";

                        } else {
                            randomGenerated = "Tail";
                        }
                        if (coinToss == randomGenerated) {
                            TextView playerTurn = (TextView) findViewById(R.id.NextPlayer);
                            Toast.makeText(board.this,
                                    "You won the toss. It was " + randomGenerated + ".", Toast.LENGTH_LONG).show();
                            playerTurn.setText("Human");
                            turn = 0;
                        } else {
                            TextView playerTurn = (TextView) findViewById(R.id.NextPlayer);
                            Toast.makeText(board.this,
                                    "You lost the toss. It was " + randomGenerated + ".", Toast.LENGTH_LONG).show();
                            playerTurn.setText("Computer");
                            turn = 1;
                        }

                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "TAIL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        coinToss = "Tail";
                        Random randomGenerator = new Random();
                        int randomNumber = randomGenerator.nextInt(100);
                        if ((randomNumber % 2) == 0) {
                            randomGenerated = "Head";

                        } else {
                            randomGenerated = "Tail";
                        }
                        if (coinToss == randomGenerated) {
                            TextView playerTurn = (TextView) findViewById(R.id.NextPlayer);
                            Toast.makeText(board.this, "You won the toss. It was " + coinToss + ".", Toast.LENGTH_LONG).show();
                            playerTurn.setText("Human");
                            turn = 0;
                        } else {
                            TextView playerTurn = (TextView) findViewById(R.id.NextPlayer);
                            Toast.makeText(board.this,
                                    "You lost the toss. It was " + coinToss + ".", Toast.LENGTH_LONG).show();
                            playerTurn.setText("Computer");
                            turn = 1;
                        }

                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

        return 0;
    }

    public void SaveGameState() {

    }

    public void AskFileNameAlertBox() {

        AlertDialog.Builder builder = new AlertDialog.Builder(board.this);
        builder.setTitle("Enter the filename of the file you want to Load:");

        // Set up the input
        final EditText input = new EditText(board.this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String filename;
                filename = input.getText().toString();
                try {
                    File myFile = new File("/sdcard/" + filename + ".txt");
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter =
                            new OutputStreamWriter(fOut);
                    myOutWriter.append("Layout:");
                    myOutWriter.append(System.getProperty("line.separator"));

                    for (int i = 1; i <= 8; i++) {
                        for (int j = 1; j <= 12; j++) {
                            myOutWriter.append((Integer.toString(boardTiles[i][j].color) + Integer.toString(boardTiles[i][j].symbol) + " "));
                        }
                        myOutWriter.append(System.getProperty("line.separator"));
                    }
                    myOutWriter.append(System.getProperty("line.separator"));
                    myOutWriter.append("Stock:");
                    myOutWriter.append(System.getProperty("line.separator"));
                    int abc = deck.numberOfStock;
                    int def = deck.nextButtonStartsHere;
                    for (int i = def; i < abc; i++) {
                        myOutWriter.append(Integer.toString(deck.returnCurrentPile())+" ");
                        deck.nextButtonStartsHere++;
                    }
                    deck.nextButtonStartsHere=def;
                    //get Score
                    TextView human_score = (TextView) findViewById(R.id.HumanScore);
                    int human_current_score = Integer.parseInt(human_score.getText().toString());

                    TextView computer_score = (TextView) findViewById(R.id.ComputerScore);
                    int computer_current_score = Integer.parseInt(computer_score.getText().toString());
                    myOutWriter.append(System.getProperty("line.separator"));
                    myOutWriter.append(System.getProperty("line.separator"));
                    myOutWriter.append("Human Score:");
                    myOutWriter.append(System.getProperty("line.separator"));
                    myOutWriter.append(Integer.toString(human_current_score));
                    myOutWriter.append(System.getProperty("line.separator"));
                    myOutWriter.append(System.getProperty("line.separator"));
                    myOutWriter.append("Computer Score:");
                    myOutWriter.append(System.getProperty("line.separator"));
                    myOutWriter.append(Integer.toString(computer_current_score));
                    myOutWriter.append(System.getProperty("line.separator"));
                    myOutWriter.append(System.getProperty("line.separator"));
                    myOutWriter.append("Next Player:");
                    myOutWriter.append(System.getProperty("line.separator"));
                    //get next player
                    TextView nextPlayer = (TextView) findViewById(R.id.NextPlayer);
                    String nextPlayeris=nextPlayer.getText().toString();
                    myOutWriter.append((nextPlayeris));



                    myOutWriter.close();
                    fOut.close();
                    Toast.makeText(getBaseContext(),
                            "Done writing File. Filename:" + "/sdcard/" + filename + ".txt",
                            Toast.LENGTH_SHORT).show();
                } catch (
                        Exception e
                        )

                {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }


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

    public void SetState() throws IOException {
        File dir = Environment.getExternalStorageDirectory();
        MainActivity hello = new MainActivity();
        File yourFile;
        if (hello.searchmode == 1) {
            yourFile = new File(dir, "case0.txt");
        } else {
            yourFile = new File(dir, hello.filename + ".txt");
        }
        State newState = new State(yourFile);

        int[] deckstock = new int[72];
        deckstock = newState.stockBoard;

        deck = new Deck(deckstock, 0, newState.numberOfStock);
        Button previewButton = (Button) findViewById(R.id.previewBtn);
        int setPreview = deck.getStock();
        btnPreview = new Tiles(50, 50, setPreview);
        previewButton.setBackgroundColor(btnPreview.setColor());
        previewButton.setText(Integer.toString(btnPreview.symbol));


        for (int i = 0; i < 10; i++) {
            boardTiles[i][0] = new Tiles(i, 0, 70);
        }
        for (int i = 0; i < 14; i++) {
            boardTiles[0][i] = new Tiles(0, i, 70);
        }
        for (int i = 0; i < 10; i++) {
            boardTiles[i][13] = new Tiles(i, 13, 70);
        }
        for (int i = 0; i < 14; i++) {
            boardTiles[9][i] = new Tiles(9, i, 70);
        }

        boardTiles[1][1] = new Tiles(1, 1, newState.layoutBoard[1][1]);
        buttons[1][1] = (Button) findViewById(R.id.btn11);
        buttons[1][1].setText(Integer.toString(boardTiles[1][1].symbol));
        buttons[1][1].setTextColor(Color.WHITE);
        buttons[1][1].setBackgroundColor(boardTiles[1][1].setColor());

        boardTiles[1][2] = new Tiles(1, 2, newState.layoutBoard[1][2]);
        buttons[1][2] = (Button) findViewById(R.id.btn12);
        buttons[1][2].setText(Integer.toString(boardTiles[1][2].symbol));
        buttons[1][2].setTextColor(Color.WHITE);
        buttons[1][2].setBackgroundColor(boardTiles[1][2].setColor());

        boardTiles[1][3] = new Tiles(1, 3, newState.layoutBoard[1][3]);
        buttons[1][3] = (Button) findViewById(R.id.btn13);
        buttons[1][3].setText(Integer.toString(boardTiles[1][3].symbol));
        buttons[1][3].setTextColor(Color.WHITE);
        buttons[1][3].setBackgroundColor(boardTiles[1][3].setColor());

        boardTiles[1][4] = new Tiles(1, 4, newState.layoutBoard[1][4]);
        buttons[1][4] = (Button) findViewById(R.id.btn14);
        buttons[1][4].setText(Integer.toString(boardTiles[1][4].symbol));
        buttons[1][4].setTextColor(Color.WHITE);
        buttons[1][4].setBackgroundColor(boardTiles[1][4].setColor());

        boardTiles[1][5] = new Tiles(1, 5, newState.layoutBoard[1][5]);
        buttons[1][5] = (Button) findViewById(R.id.btn15);
        buttons[1][5].setText(Integer.toString(boardTiles[1][5].symbol));
        buttons[1][5].setTextColor(Color.WHITE);
        buttons[1][5].setBackgroundColor(boardTiles[1][5].setColor());

        boardTiles[1][6] = new Tiles(1, 6, newState.layoutBoard[1][6]);
        buttons[1][6] = (Button) findViewById(R.id.btn16);
        buttons[1][6].setText(Integer.toString(boardTiles[1][6].symbol));
        buttons[1][6].setTextColor(Color.WHITE);
        buttons[1][6].setBackgroundColor(boardTiles[1][6].setColor());

        boardTiles[1][7] = new Tiles(1, 7, newState.layoutBoard[1][7]);
        buttons[1][7] = (Button) findViewById(R.id.btn17);
        buttons[1][7].setText(Integer.toString(boardTiles[1][7].symbol));
        buttons[1][7].setTextColor(Color.WHITE);
        buttons[1][7].setBackgroundColor(boardTiles[1][7].setColor());

        boardTiles[1][8] = new Tiles(1, 8, newState.layoutBoard[1][8]);
        buttons[1][8] = (Button) findViewById(R.id.btn18);
        buttons[1][8].setText(Integer.toString(boardTiles[1][8].symbol));
        buttons[1][8].setTextColor(Color.WHITE);
        buttons[1][8].setBackgroundColor(boardTiles[1][8].setColor());

        boardTiles[1][9] = new Tiles(1, 9, newState.layoutBoard[1][9]);
        buttons[1][9] = (Button) findViewById(R.id.btn19);
        buttons[1][9].setText(Integer.toString(boardTiles[1][9].symbol));
        buttons[1][9].setTextColor(Color.WHITE);
        buttons[1][9].setBackgroundColor(boardTiles[1][9].setColor());

        boardTiles[1][10] = new Tiles(1, 10, newState.layoutBoard[1][10]);
        buttons[1][10] = (Button) findViewById(R.id.btn110);
        buttons[1][10].setText(Integer.toString(boardTiles[1][10].symbol));
        buttons[1][10].setTextColor(Color.WHITE);
        buttons[1][10].setBackgroundColor(boardTiles[1][10].setColor());

        boardTiles[1][11] = new Tiles(1, 11, newState.layoutBoard[1][11]);
        buttons[1][11] = (Button) findViewById(R.id.btn111);
        buttons[1][11].setText(Integer.toString(boardTiles[1][11].symbol));
        buttons[1][11].setTextColor(Color.WHITE);
        buttons[1][11].setBackgroundColor(boardTiles[1][11].setColor());

        boardTiles[1][12] = new Tiles(1, 12, newState.layoutBoard[1][12]);
        buttons[1][12] = (Button) findViewById(R.id.btn112);
        buttons[1][12].setText(Integer.toString(boardTiles[1][12].symbol));
        buttons[1][12].setTextColor(Color.WHITE);
        buttons[1][12].setBackgroundColor(boardTiles[1][12].setColor());

        boardTiles[2][1] = new Tiles(2, 1, newState.layoutBoard[2][1]);
        buttons[2][1] = (Button) findViewById(R.id.btn21);
        buttons[2][1].setText(Integer.toString(boardTiles[2][1].symbol));
        buttons[2][1].setTextColor(Color.WHITE);
        buttons[2][1].setBackgroundColor(boardTiles[2][1].setColor());

        boardTiles[2][2] = new Tiles(2, 2, newState.layoutBoard[2][2]);
        buttons[2][2] = (Button) findViewById(R.id.btn22);
        buttons[2][2].setText(Integer.toString(boardTiles[2][2].symbol));
        buttons[2][2].setTextColor(Color.WHITE);
        buttons[2][2].setBackgroundColor(boardTiles[2][2].setColor());

        boardTiles[2][3] = new Tiles(2, 3, newState.layoutBoard[2][3]);
        buttons[2][3] = (Button) findViewById(R.id.btn23);
        buttons[2][3].setText(Integer.toString(boardTiles[2][3].symbol));
        buttons[2][3].setTextColor(Color.WHITE);
        buttons[2][3].setBackgroundColor(boardTiles[2][3].setColor());

        boardTiles[2][4] = new Tiles(2, 4, newState.layoutBoard[2][4]);
        buttons[2][4] = (Button) findViewById(R.id.btn24);
        buttons[2][4].setText(Integer.toString(boardTiles[2][4].symbol));
        buttons[2][4].setTextColor(Color.WHITE);
        buttons[2][4].setBackgroundColor(boardTiles[2][4].setColor());

        boardTiles[2][5] = new Tiles(2, 5, newState.layoutBoard[2][5]);
        buttons[2][5] = (Button) findViewById(R.id.btn25);
        buttons[2][5].setText(Integer.toString(boardTiles[2][5].symbol));
        buttons[2][5].setTextColor(Color.WHITE);
        buttons[2][5].setBackgroundColor(boardTiles[2][5].setColor());

        boardTiles[2][6] = new Tiles(2, 6, newState.layoutBoard[2][6]);
        buttons[2][6] = (Button) findViewById(R.id.btn26);
        buttons[2][6].setText(Integer.toString(boardTiles[2][6].symbol));
        buttons[2][6].setTextColor(Color.WHITE);
        buttons[2][6].setBackgroundColor(boardTiles[2][6].setColor());

        boardTiles[2][7] = new Tiles(2, 7, newState.layoutBoard[2][7]);
        buttons[2][7] = (Button) findViewById(R.id.btn27);
        buttons[2][7].setText(Integer.toString(boardTiles[2][7].symbol));
        buttons[2][7].setTextColor(Color.WHITE);
        buttons[2][7].setBackgroundColor(boardTiles[2][7].setColor());

        boardTiles[2][8] = new Tiles(2, 8, newState.layoutBoard[2][8]);
        buttons[2][8] = (Button) findViewById(R.id.btn28);
        buttons[2][8].setText(Integer.toString(boardTiles[2][8].symbol));
        buttons[2][8].setTextColor(Color.WHITE);
        buttons[2][8].setBackgroundColor(boardTiles[2][8].setColor());

        boardTiles[2][9] = new Tiles(2, 9, newState.layoutBoard[2][9]);
        buttons[2][9] = (Button) findViewById(R.id.btn29);
        buttons[2][9].setText(Integer.toString(boardTiles[2][9].symbol));
        buttons[2][9].setTextColor(Color.WHITE);
        buttons[2][9].setBackgroundColor(boardTiles[2][9].setColor());

        boardTiles[2][10] = new Tiles(2, 10, newState.layoutBoard[2][10]);
        buttons[2][10] = (Button) findViewById(R.id.btn210);
        buttons[2][10].setText(Integer.toString(boardTiles[2][10].symbol));
        buttons[2][10].setTextColor(Color.WHITE);
        buttons[2][10].setBackgroundColor(boardTiles[2][10].setColor());

        boardTiles[2][11] = new Tiles(2, 11, newState.layoutBoard[2][11]);
        buttons[2][11] = (Button) findViewById(R.id.btn211);
        buttons[2][11].setText(Integer.toString(boardTiles[2][11].symbol));
        buttons[2][11].setTextColor(Color.WHITE);
        buttons[2][11].setBackgroundColor(boardTiles[2][11].setColor());

        boardTiles[2][12] = new Tiles(2, 12, newState.layoutBoard[2][12]);
        buttons[2][12] = (Button) findViewById(R.id.btn212);
        buttons[2][12].setText(Integer.toString(boardTiles[2][12].symbol));
        buttons[2][12].setTextColor(Color.WHITE);
        buttons[2][12].setBackgroundColor(boardTiles[2][12].setColor());

        boardTiles[3][1] = new Tiles(3, 1, newState.layoutBoard[3][1]);
        buttons[3][1] = (Button) findViewById(R.id.btn31);
        buttons[3][1].setText(Integer.toString(boardTiles[3][1].symbol));
        buttons[3][1].setTextColor(Color.WHITE);
        buttons[3][1].setBackgroundColor(boardTiles[3][1].setColor());

        boardTiles[3][2] = new Tiles(3, 2, newState.layoutBoard[3][2]);
        buttons[3][2] = (Button) findViewById(R.id.btn32);
        buttons[3][2].setText(Integer.toString(boardTiles[3][2].symbol));
        buttons[3][2].setTextColor(Color.WHITE);
        buttons[3][2].setBackgroundColor(boardTiles[3][2].setColor());

        boardTiles[3][3] = new Tiles(3, 3, newState.layoutBoard[3][3]);
        buttons[3][3] = (Button) findViewById(R.id.btn33);
        buttons[3][3].setText(Integer.toString(boardTiles[3][3].symbol));
        buttons[3][3].setTextColor(Color.WHITE);
        buttons[3][3].setBackgroundColor(boardTiles[3][3].setColor());

        boardTiles[3][4] = new Tiles(3, 4, newState.layoutBoard[3][4]);
        buttons[3][4] = (Button) findViewById(R.id.btn34);
        buttons[3][4].setText(Integer.toString(boardTiles[3][4].symbol));
        buttons[3][4].setTextColor(Color.WHITE);
        buttons[3][4].setBackgroundColor(boardTiles[3][4].setColor());

        boardTiles[3][5] = new Tiles(3, 5, newState.layoutBoard[3][5]);
        buttons[3][5] = (Button) findViewById(R.id.btn35);
        buttons[3][5].setText(Integer.toString(boardTiles[3][5].symbol));
        buttons[3][5].setTextColor(Color.WHITE);
        buttons[3][5].setBackgroundColor(boardTiles[3][5].setColor());

        boardTiles[3][6] = new Tiles(3, 6, newState.layoutBoard[3][6]);
        buttons[3][6] = (Button) findViewById(R.id.btn36);
        buttons[3][6].setText(Integer.toString(boardTiles[3][6].symbol));
        buttons[3][6].setTextColor(Color.WHITE);
        buttons[3][6].setBackgroundColor(boardTiles[3][6].setColor());

        boardTiles[3][7] = new Tiles(3, 7, newState.layoutBoard[3][7]);
        buttons[3][7] = (Button) findViewById(R.id.btn37);
        buttons[3][7].setText(Integer.toString(boardTiles[3][7].symbol));
        buttons[3][7].setTextColor(Color.WHITE);
        buttons[3][7].setBackgroundColor(boardTiles[3][7].setColor());

        boardTiles[3][8] = new Tiles(3, 8, newState.layoutBoard[3][8]);
        buttons[3][8] = (Button) findViewById(R.id.btn38);
        buttons[3][8].setText(Integer.toString(boardTiles[3][8].symbol));
        buttons[3][8].setTextColor(Color.WHITE);
        buttons[3][8].setBackgroundColor(boardTiles[3][8].setColor());

        boardTiles[3][9] = new Tiles(3, 9, newState.layoutBoard[3][9]);
        buttons[3][9] = (Button) findViewById(R.id.btn39);
        buttons[3][9].setText(Integer.toString(boardTiles[3][9].symbol));
        buttons[3][9].setTextColor(Color.WHITE);
        buttons[3][9].setBackgroundColor(boardTiles[3][9].setColor());

        boardTiles[3][10] = new Tiles(3, 10, newState.layoutBoard[3][10]);
        buttons[3][10] = (Button) findViewById(R.id.btn310);
        buttons[3][10].setText(Integer.toString(boardTiles[3][10].symbol));
        buttons[3][10].setTextColor(Color.WHITE);
        buttons[3][10].setBackgroundColor(boardTiles[3][10].setColor());

        boardTiles[3][11] = new Tiles(3, 11, newState.layoutBoard[3][11]);
        buttons[3][11] = (Button) findViewById(R.id.btn311);
        buttons[3][11].setText(Integer.toString(boardTiles[3][11].symbol));
        buttons[3][11].setTextColor(Color.WHITE);
        buttons[3][11].setBackgroundColor(boardTiles[3][11].setColor());

        boardTiles[3][12] = new Tiles(3, 12, newState.layoutBoard[3][12]);
        buttons[3][12] = (Button) findViewById(R.id.btn312);
        buttons[3][12].setText(Integer.toString(boardTiles[3][12].symbol));
        buttons[3][12].setTextColor(Color.WHITE);
        buttons[3][12].setBackgroundColor(boardTiles[3][12].setColor());

        boardTiles[4][1] = new Tiles(4, 1, newState.layoutBoard[4][1]);
        buttons[4][1] = (Button) findViewById(R.id.btn41);
        buttons[4][1].setText(Integer.toString(boardTiles[4][1].symbol));
        buttons[4][1].setTextColor(Color.WHITE);
        buttons[4][1].setBackgroundColor(boardTiles[4][1].setColor());

        boardTiles[4][2] = new Tiles(4, 2, newState.layoutBoard[4][2]);
        buttons[4][2] = (Button) findViewById(R.id.btn42);
        buttons[4][2].setText(Integer.toString(boardTiles[4][2].symbol));
        buttons[4][2].setTextColor(Color.WHITE);
        buttons[4][2].setBackgroundColor(boardTiles[4][2].setColor());

        boardTiles[4][3] = new Tiles(4, 3, newState.layoutBoard[4][3]);
        buttons[4][3] = (Button) findViewById(R.id.btn43);
        buttons[4][3].setText(Integer.toString(boardTiles[4][3].symbol));
        buttons[4][3].setTextColor(Color.WHITE);
        buttons[4][3].setBackgroundColor(boardTiles[4][3].setColor());

        boardTiles[4][4] = new Tiles(4, 4, newState.layoutBoard[4][4]);
        buttons[4][4] = (Button) findViewById(R.id.btn44);
        buttons[4][4].setText(Integer.toString(boardTiles[4][4].symbol));
        buttons[4][4].setTextColor(Color.WHITE);
        buttons[4][4].setBackgroundColor(boardTiles[4][4].setColor());

        boardTiles[4][5] = new Tiles(4, 5, newState.layoutBoard[4][5]);
        buttons[4][5] = (Button) findViewById(R.id.btn45);
        buttons[4][5].setText(Integer.toString(boardTiles[4][5].symbol));
        buttons[4][5].setTextColor(Color.WHITE);
        buttons[4][5].setBackgroundColor(boardTiles[4][5].setColor());

        boardTiles[4][6] = new Tiles(4, 6, newState.layoutBoard[4][6]);
        buttons[4][6] = (Button) findViewById(R.id.btn46);
        buttons[4][6].setText(Integer.toString(boardTiles[4][6].symbol));
        buttons[4][6].setTextColor(Color.WHITE);
        buttons[4][6].setBackgroundColor(boardTiles[4][6].setColor());

        boardTiles[4][7] = new Tiles(4, 7, newState.layoutBoard[4][7]);
        buttons[4][7] = (Button) findViewById(R.id.btn47);
        buttons[4][7].setText(Integer.toString(boardTiles[4][7].symbol));
        buttons[4][7].setTextColor(Color.WHITE);
        buttons[4][7].setBackgroundColor(boardTiles[4][7].setColor());

        boardTiles[4][8] = new Tiles(4, 8, newState.layoutBoard[4][8]);
        buttons[4][8] = (Button) findViewById(R.id.btn48);
        buttons[4][8].setText(Integer.toString(boardTiles[4][8].symbol));
        buttons[4][8].setTextColor(Color.WHITE);
        buttons[4][8].setBackgroundColor(boardTiles[4][8].setColor());

        boardTiles[4][9] = new Tiles(4, 9, newState.layoutBoard[4][9]);
        buttons[4][9] = (Button) findViewById(R.id.btn49);
        buttons[4][9].setText(Integer.toString(boardTiles[4][9].symbol));
        buttons[4][9].setTextColor(Color.WHITE);
        buttons[4][9].setBackgroundColor(boardTiles[4][9].setColor());

        boardTiles[4][10] = new Tiles(4, 10, newState.layoutBoard[4][10]);
        buttons[4][10] = (Button) findViewById(R.id.btn410);
        buttons[4][10].setText(Integer.toString(boardTiles[4][10].symbol));
        buttons[4][10].setTextColor(Color.WHITE);
        buttons[4][10].setBackgroundColor(boardTiles[4][10].setColor());

        boardTiles[4][11] = new Tiles(4, 11, newState.layoutBoard[4][11]);
        buttons[4][11] = (Button) findViewById(R.id.btn411);
        buttons[4][11].setText(Integer.toString(boardTiles[4][11].symbol));
        buttons[4][11].setTextColor(Color.WHITE);
        buttons[4][11].setBackgroundColor(boardTiles[4][11].setColor());

        boardTiles[4][12] = new Tiles(4, 12, newState.layoutBoard[4][12]);
        buttons[4][12] = (Button) findViewById(R.id.btn412);
        buttons[4][12].setText(Integer.toString(boardTiles[4][12].symbol));
        buttons[4][12].setTextColor(Color.WHITE);
        buttons[4][12].setBackgroundColor(boardTiles[4][12].setColor());

        boardTiles[5][1] = new Tiles(5, 1, newState.layoutBoard[5][1]);
        buttons[5][1] = (Button) findViewById(R.id.btn51);
        buttons[5][1].setText(Integer.toString(boardTiles[5][1].symbol));
        buttons[5][1].setTextColor(Color.WHITE);
        buttons[5][1].setBackgroundColor(boardTiles[5][1].setColor());

        boardTiles[5][2] = new Tiles(5, 2, newState.layoutBoard[5][2]);
        buttons[5][2] = (Button) findViewById(R.id.btn52);
        buttons[5][2].setText(Integer.toString(boardTiles[5][2].symbol));
        buttons[5][2].setTextColor(Color.WHITE);
        buttons[5][2].setBackgroundColor(boardTiles[5][2].setColor());

        boardTiles[5][3] = new Tiles(5, 3, newState.layoutBoard[5][3]);
        buttons[5][3] = (Button) findViewById(R.id.btn53);
        buttons[5][3].setText(Integer.toString(boardTiles[5][3].symbol));
        buttons[5][3].setTextColor(Color.WHITE);
        buttons[5][3].setBackgroundColor(boardTiles[5][3].setColor());

        boardTiles[5][4] = new Tiles(5, 4, newState.layoutBoard[5][4]);
        buttons[5][4] = (Button) findViewById(R.id.btn54);
        buttons[5][4].setText(Integer.toString(boardTiles[5][4].symbol));
        buttons[5][4].setTextColor(Color.WHITE);
        buttons[5][4].setBackgroundColor(boardTiles[5][4].setColor());

        boardTiles[5][5] = new Tiles(5, 5, newState.layoutBoard[5][5]);
        buttons[5][5] = (Button) findViewById(R.id.btn55);
        buttons[5][5].setText(Integer.toString(boardTiles[5][5].symbol));
        buttons[5][5].setTextColor(Color.WHITE);
        buttons[5][5].setBackgroundColor(boardTiles[5][5].setColor());

        boardTiles[5][6] = new Tiles(5, 6, newState.layoutBoard[5][6]);
        buttons[5][6] = (Button) findViewById(R.id.btn56);
        buttons[5][6].setText(Integer.toString(boardTiles[5][6].symbol));
        buttons[5][6].setTextColor(Color.WHITE);
        buttons[5][6].setBackgroundColor(boardTiles[5][6].setColor());

        boardTiles[5][7] = new Tiles(5, 7, newState.layoutBoard[5][7]);
        buttons[5][7] = (Button) findViewById(R.id.btn57);
        buttons[5][7].setText(Integer.toString(boardTiles[5][7].symbol));
        buttons[5][7].setTextColor(Color.WHITE);
        buttons[5][7].setBackgroundColor(boardTiles[5][7].setColor());

        boardTiles[5][8] = new Tiles(5, 8, newState.layoutBoard[5][8]);
        buttons[5][8] = (Button) findViewById(R.id.btn58);
        buttons[5][8].setText(Integer.toString(boardTiles[5][8].symbol));
        buttons[5][8].setTextColor(Color.WHITE);
        buttons[5][8].setBackgroundColor(boardTiles[5][8].setColor());

        boardTiles[5][9] = new Tiles(5, 9, newState.layoutBoard[5][9]);
        buttons[5][9] = (Button) findViewById(R.id.btn59);
        buttons[5][9].setText(Integer.toString(boardTiles[5][9].symbol));
        buttons[5][9].setTextColor(Color.WHITE);
        buttons[5][9].setBackgroundColor(boardTiles[5][9].setColor());

        boardTiles[5][10] = new Tiles(5, 10, newState.layoutBoard[5][10]);
        buttons[5][10] = (Button) findViewById(R.id.btn510);
        buttons[5][10].setText(Integer.toString(boardTiles[5][10].symbol));
        buttons[5][10].setTextColor(Color.WHITE);
        buttons[5][10].setBackgroundColor(boardTiles[5][10].setColor());

        boardTiles[5][11] = new Tiles(5, 11, newState.layoutBoard[5][11]);
        buttons[5][11] = (Button) findViewById(R.id.btn511);
        buttons[5][11].setText(Integer.toString(boardTiles[5][11].symbol));
        buttons[5][11].setTextColor(Color.WHITE);
        buttons[5][11].setBackgroundColor(boardTiles[5][11].setColor());

        boardTiles[5][12] = new Tiles(5, 12, newState.layoutBoard[5][12]);
        buttons[5][12] = (Button) findViewById(R.id.btn512);
        buttons[5][12].setText(Integer.toString(boardTiles[5][12].symbol));
        buttons[5][12].setTextColor(Color.WHITE);
        buttons[5][12].setBackgroundColor(boardTiles[5][12].setColor());

        boardTiles[6][1] = new Tiles(6, 1, newState.layoutBoard[6][1]);
        buttons[6][1] = (Button) findViewById(R.id.btn61);
        buttons[6][1].setText(Integer.toString(boardTiles[6][1].symbol));
        buttons[6][1].setTextColor(Color.WHITE);
        buttons[6][1].setBackgroundColor(boardTiles[6][1].setColor());

        boardTiles[6][2] = new Tiles(6, 2, newState.layoutBoard[6][2]);
        buttons[6][2] = (Button) findViewById(R.id.btn62);
        buttons[6][2].setText(Integer.toString(boardTiles[6][2].symbol));
        buttons[6][2].setTextColor(Color.WHITE);
        buttons[6][2].setBackgroundColor(boardTiles[6][2].setColor());

        boardTiles[6][3] = new Tiles(6, 3, newState.layoutBoard[6][3]);
        buttons[6][3] = (Button) findViewById(R.id.btn63);
        buttons[6][3].setText(Integer.toString(boardTiles[6][3].symbol));
        buttons[6][3].setTextColor(Color.WHITE);
        buttons[6][3].setBackgroundColor(boardTiles[6][3].setColor());

        boardTiles[6][4] = new Tiles(6, 4, newState.layoutBoard[6][4]);
        buttons[6][4] = (Button) findViewById(R.id.btn64);
        buttons[6][4].setText(Integer.toString(boardTiles[6][4].symbol));
        buttons[6][4].setTextColor(Color.WHITE);
        buttons[6][4].setBackgroundColor(boardTiles[6][4].setColor());

        boardTiles[6][5] = new Tiles(6, 5, newState.layoutBoard[6][5]);
        buttons[6][5] = (Button) findViewById(R.id.btn65);
        buttons[6][5].setText(Integer.toString(boardTiles[6][5].symbol));
        buttons[6][5].setTextColor(Color.WHITE);
        buttons[6][5].setBackgroundColor(boardTiles[6][5].setColor());

        boardTiles[6][6] = new Tiles(6, 6, newState.layoutBoard[6][6]);
        buttons[6][6] = (Button) findViewById(R.id.btn66);
        buttons[6][6].setText(Integer.toString(boardTiles[6][6].symbol));
        buttons[6][6].setTextColor(Color.WHITE);
        buttons[6][6].setBackgroundColor(boardTiles[6][6].setColor());

        boardTiles[6][7] = new Tiles(6, 7, newState.layoutBoard[6][7]);
        buttons[6][7] = (Button) findViewById(R.id.btn67);
        buttons[6][7].setText(Integer.toString(boardTiles[6][7].symbol));
        buttons[6][7].setTextColor(Color.WHITE);
        buttons[6][7].setBackgroundColor(boardTiles[6][7].setColor());

        boardTiles[6][8] = new Tiles(6, 8, newState.layoutBoard[6][8]);
        buttons[6][8] = (Button) findViewById(R.id.btn68);
        buttons[6][8].setText(Integer.toString(boardTiles[6][8].symbol));
        buttons[6][8].setTextColor(Color.WHITE);
        buttons[6][8].setBackgroundColor(boardTiles[6][8].setColor());

        boardTiles[6][9] = new Tiles(6, 9, newState.layoutBoard[6][9]);
        buttons[6][9] = (Button) findViewById(R.id.btn69);
        buttons[6][9].setText(Integer.toString(boardTiles[6][9].symbol));
        buttons[6][9].setTextColor(Color.WHITE);
        buttons[6][9].setBackgroundColor(boardTiles[6][9].setColor());

        boardTiles[6][10] = new Tiles(6, 10, newState.layoutBoard[6][10]);
        buttons[6][10] = (Button) findViewById(R.id.btn610);
        buttons[6][10].setText(Integer.toString(boardTiles[6][10].symbol));
        buttons[6][10].setTextColor(Color.WHITE);
        buttons[6][10].setBackgroundColor(boardTiles[6][10].setColor());

        boardTiles[6][11] = new Tiles(6, 11, newState.layoutBoard[6][11]);
        buttons[6][11] = (Button) findViewById(R.id.btn611);
        buttons[6][11].setText(Integer.toString(boardTiles[6][11].symbol));
        buttons[6][11].setTextColor(Color.WHITE);
        buttons[6][11].setBackgroundColor(boardTiles[6][11].setColor());

        boardTiles[6][12] = new Tiles(6, 12, newState.layoutBoard[6][12]);
        buttons[6][12] = (Button) findViewById(R.id.btn612);
        buttons[6][12].setText(Integer.toString(boardTiles[6][12].symbol));
        buttons[6][12].setTextColor(Color.WHITE);
        buttons[6][12].setBackgroundColor(boardTiles[6][12].setColor());

        boardTiles[7][1] = new Tiles(7, 1, newState.layoutBoard[7][1]);
        buttons[7][1] = (Button) findViewById(R.id.btn71);
        buttons[7][1].setText(Integer.toString(boardTiles[7][1].symbol));
        buttons[7][1].setTextColor(Color.WHITE);
        buttons[7][1].setBackgroundColor(boardTiles[7][1].setColor());

        boardTiles[7][2] = new Tiles(7, 2, newState.layoutBoard[7][2]);
        buttons[7][2] = (Button) findViewById(R.id.btn72);
        buttons[7][2].setText(Integer.toString(boardTiles[7][2].symbol));
        buttons[7][2].setTextColor(Color.WHITE);
        buttons[7][2].setBackgroundColor(boardTiles[7][2].setColor());

        boardTiles[7][3] = new Tiles(7, 3, newState.layoutBoard[7][3]);
        buttons[7][3] = (Button) findViewById(R.id.btn73);
        buttons[7][3].setText(Integer.toString(boardTiles[7][3].symbol));
        buttons[7][3].setTextColor(Color.WHITE);
        buttons[7][3].setBackgroundColor(boardTiles[7][3].setColor());

        boardTiles[7][4] = new Tiles(7, 4, newState.layoutBoard[7][4]);
        buttons[7][4] = (Button) findViewById(R.id.btn74);
        buttons[7][4].setText(Integer.toString(boardTiles[7][4].symbol));
        buttons[7][4].setTextColor(Color.WHITE);
        buttons[7][4].setBackgroundColor(boardTiles[7][4].setColor());

        boardTiles[7][5] = new Tiles(7, 5, newState.layoutBoard[7][5]);
        buttons[7][5] = (Button) findViewById(R.id.btn75);
        buttons[7][5].setText(Integer.toString(boardTiles[7][5].symbol));
        buttons[7][5].setTextColor(Color.WHITE);
        buttons[7][5].setBackgroundColor(boardTiles[7][5].setColor());

        boardTiles[7][6] = new Tiles(7, 6, newState.layoutBoard[7][6]);
        buttons[7][6] = (Button) findViewById(R.id.btn76);
        buttons[7][6].setText(Integer.toString(boardTiles[7][6].symbol));
        buttons[7][6].setTextColor(Color.WHITE);
        buttons[7][6].setBackgroundColor(boardTiles[7][6].setColor());

        boardTiles[7][7] = new Tiles(7, 7, newState.layoutBoard[7][7]);
        buttons[7][7] = (Button) findViewById(R.id.btn77);
        buttons[7][7].setText(Integer.toString(boardTiles[7][7].symbol));
        buttons[7][7].setTextColor(Color.WHITE);
        buttons[7][7].setBackgroundColor(boardTiles[7][7].setColor());

        boardTiles[7][8] = new Tiles(7, 8, newState.layoutBoard[7][8]);
        buttons[7][8] = (Button) findViewById(R.id.btn78);
        buttons[7][8].setText(Integer.toString(boardTiles[7][8].symbol));
        buttons[7][8].setTextColor(Color.WHITE);
        buttons[7][8].setBackgroundColor(boardTiles[7][8].setColor());

        boardTiles[7][9] = new Tiles(7, 9, newState.layoutBoard[7][9]);
        buttons[7][9] = (Button) findViewById(R.id.btn79);
        buttons[7][9].setText(Integer.toString(boardTiles[7][9].symbol));
        buttons[7][9].setTextColor(Color.WHITE);
        buttons[7][9].setBackgroundColor(boardTiles[7][9].setColor());

        boardTiles[7][10] = new Tiles(7, 10, newState.layoutBoard[7][10]);
        buttons[7][10] = (Button) findViewById(R.id.btn710);
        buttons[7][10].setText(Integer.toString(boardTiles[7][10].symbol));
        buttons[7][10].setTextColor(Color.WHITE);
        buttons[7][10].setBackgroundColor(boardTiles[7][10].setColor());

        boardTiles[7][11] = new Tiles(7, 11, newState.layoutBoard[7][11]);
        buttons[7][11] = (Button) findViewById(R.id.btn711);
        buttons[7][11].setText(Integer.toString(boardTiles[7][11].symbol));
        buttons[7][11].setTextColor(Color.WHITE);
        buttons[7][11].setBackgroundColor(boardTiles[7][11].setColor());

        boardTiles[7][12] = new Tiles(7, 12, newState.layoutBoard[7][12]);
        buttons[7][12] = (Button) findViewById(R.id.btn712);
        buttons[7][12].setText(Integer.toString(boardTiles[7][12].symbol));
        buttons[7][12].setTextColor(Color.WHITE);
        buttons[7][12].setBackgroundColor(boardTiles[7][12].setColor());

        boardTiles[8][1] = new Tiles(8, 1, newState.layoutBoard[8][1]);
        buttons[8][1] = (Button) findViewById(R.id.btn81);
        buttons[8][1].setText(Integer.toString(boardTiles[8][1].symbol));
        buttons[8][1].setTextColor(Color.WHITE);
        buttons[8][1].setBackgroundColor(boardTiles[8][1].setColor());

        boardTiles[8][2] = new Tiles(8, 2, newState.layoutBoard[8][2]);
        buttons[8][2] = (Button) findViewById(R.id.btn82);
        buttons[8][2].setText(Integer.toString(boardTiles[8][2].symbol));
        buttons[8][2].setTextColor(Color.WHITE);
        buttons[8][2].setBackgroundColor(boardTiles[8][2].setColor());

        boardTiles[8][3] = new Tiles(8, 3, newState.layoutBoard[8][3]);
        buttons[8][3] = (Button) findViewById(R.id.btn83);
        buttons[8][3].setText(Integer.toString(boardTiles[8][3].symbol));
        buttons[8][3].setTextColor(Color.WHITE);
        buttons[8][3].setBackgroundColor(boardTiles[8][3].setColor());

        boardTiles[8][4] = new Tiles(8, 4, newState.layoutBoard[8][4]);
        buttons[8][4] = (Button) findViewById(R.id.btn84);
        buttons[8][4].setText(Integer.toString(boardTiles[8][4].symbol));
        buttons[8][4].setTextColor(Color.WHITE);
        buttons[8][4].setBackgroundColor(boardTiles[8][4].setColor());

        boardTiles[8][5] = new Tiles(8, 5, newState.layoutBoard[8][5]);
        buttons[8][5] = (Button) findViewById(R.id.btn85);
        buttons[8][5].setText(Integer.toString(boardTiles[8][5].symbol));
        buttons[8][5].setTextColor(Color.WHITE);
        buttons[8][5].setBackgroundColor(boardTiles[8][5].setColor());

        boardTiles[8][6] = new Tiles(8, 6, newState.layoutBoard[8][6]);
        buttons[8][6] = (Button) findViewById(R.id.btn86);
        buttons[8][6].setText(Integer.toString(boardTiles[8][6].symbol));
        buttons[8][6].setTextColor(Color.WHITE);
        buttons[8][6].setBackgroundColor(boardTiles[8][6].setColor());

        boardTiles[8][7] = new Tiles(8, 7, newState.layoutBoard[8][7]);
        buttons[8][7] = (Button) findViewById(R.id.btn87);
        buttons[8][7].setText(Integer.toString(boardTiles[8][7].symbol));
        buttons[8][7].setTextColor(Color.WHITE);
        buttons[8][7].setBackgroundColor(boardTiles[8][7].setColor());

        boardTiles[8][8] = new Tiles(8, 8, newState.layoutBoard[8][8]);
        buttons[8][8] = (Button) findViewById(R.id.btn88);
        buttons[8][8].setText(Integer.toString(boardTiles[8][8].symbol));
        buttons[8][8].setTextColor(Color.WHITE);
        buttons[8][8].setBackgroundColor(boardTiles[8][8].setColor());

        boardTiles[8][9] = new Tiles(8, 9, newState.layoutBoard[8][9]);
        buttons[8][9] = (Button) findViewById(R.id.btn89);
        buttons[8][9].setText(Integer.toString(boardTiles[8][9].symbol));
        buttons[8][9].setTextColor(Color.WHITE);
        buttons[8][9].setBackgroundColor(boardTiles[8][9].setColor());

        boardTiles[8][10] = new Tiles(8, 10, newState.layoutBoard[8][10]);
        buttons[8][10] = (Button) findViewById(R.id.btn810);
        buttons[8][10].setText(Integer.toString(boardTiles[8][10].symbol));
        buttons[8][10].setTextColor(Color.WHITE);
        buttons[8][10].setBackgroundColor(boardTiles[8][10].setColor());

        boardTiles[8][11] = new Tiles(8, 11, newState.layoutBoard[8][11]);
        buttons[8][11] = (Button) findViewById(R.id.btn811);
        buttons[8][11].setText(Integer.toString(boardTiles[8][11].symbol));
        buttons[8][11].setTextColor(Color.WHITE);
        buttons[8][11].setBackgroundColor(boardTiles[8][11].setColor());

        boardTiles[8][12] = new Tiles(8, 12, newState.layoutBoard[8][12]);
        buttons[8][12] = (Button) findViewById(R.id.btn812);
        buttons[8][12].setText(Integer.toString(boardTiles[8][12].symbol));
        buttons[8][12].setTextColor(Color.WHITE);
        buttons[8][12].setBackgroundColor(boardTiles[8][12].setColor());

        HumanScore = newState.scoreBoard;
        TextView humanScore = (TextView) findViewById(R.id.HumanScore);
        humanScore.setText(Integer.toString(HumanScore));

        int ComputerScore = newState.ComputerScore;
        TextView Computer_Score = (TextView) findViewById(R.id.ComputerScore);
        Computer_Score.setText(Integer.toString(ComputerScore));

        String NextPlayer = newState.nextPlayer;
        TextView nextPlayer = (TextView) findViewById(R.id.NextPlayer);
        nextPlayer.setText(NextPlayer);


    }

    //this where minimax and alphabeta pruning is going to be
    //plan is to import entire stock from the deck
    //use whatever you want here
    //after using reset the used tile to 0 on the actual deck and keep everything else the same
    //also update the score of human

    //also use best first algorithm to suggest user which move to make


    public Node ComputerAlgorithmActivated() {

        //get current time
        long start = System.currentTimeMillis();

        //get cutoff
        EditText depth_cutoff = (EditText) findViewById(R.id.editText);
        String temp_string = depth_cutoff.getText().toString();
        int temp_cutoff = Integer.parseInt(temp_string);
        int cutoff_temp = temp_cutoff;
        MainActivity mainclass=new MainActivity();
        int searchmode=mainclass.searchmode;
        if(cutoff_temp!=1 || searchmode==1){
            Node bestNode=new Node();
            Toast.makeText(board.this, " NullPointerException", Toast.LENGTH_SHORT).show();
            return bestNode;
        }

        //get scores-int
        TextView human_score = (TextView) findViewById(R.id.HumanScore);
        int human_current_score = Integer.parseInt(human_score.getText().toString());

        TextView computer_score = (TextView) findViewById(R.id.ComputerScore);
        int computer_current_score = Integer.parseInt(computer_score.getText().toString());

        //get turn-boolean
        Boolean computer_Turn;
        if (turn == 0) {
            computer_Turn = false;
        } else {
            computer_Turn = true;
        }

        //root Node
        Node root = new Node();

        //call the MiniMax AI Method
        Node bestNode = MinimaxAlgorithm(root, human_current_score, computer_current_score, cutoff_temp, computer_Turn,  false);

        //Toast.makeText(board.this, " "+bestNode.moveTile.score, Toast.LENGTH_SHORT).show();
        //place tile + blink
        Log.v("Final Return:", " h:" + bestNode.moveTile.score + " r:" + bestNode.moveTile.row + " c:" + bestNode.moveTile.column);
        //Toast.makeText(board.this, " "+deck.returnCurrentPile(), Toast.LENGTH_SHORT).show();

        //get current time
        long end = System.currentTimeMillis();
        long totalTime = ((end - start));
        //Toast.makeText(board.this, "Time Taken in miliseconds: " + totalTime, Toast.LENGTH_SHORT).show();

        TextView timeTaken = (TextView) findViewById(R.id.TimeTaken);
        timeTaken.setText("Time Taken: "+String.valueOf(totalTime)+" ms");


        return bestNode;

    }

    public Node MinimaxAlgorithm(Node RootNode, int Score_Human, int Score_Computer, int Cutoff, Boolean Computer_Turn, Boolean maximizer) {
       //Check if Terminal Node

        Log.v("What function Got: ", " Stock: " + RootNode.moveTile.stock + " Row: " + RootNode.moveTile.row + " Column: " + RootNode.moveTile.column + " Score: " + RootNode.moveTile.score + " ");


        if (Cutoff == 0) {
            int temp; // to store heuristic value
            if (Computer_Turn) {
                temp = Score_Computer - Score_Human;
            } else {
                temp = Score_Human - Score_Computer;
            }
            Log.v("Cutoff = 0 Returning: ", "" + RootNode.moveTile.row + " " + RootNode.moveTile.column + " " + RootNode.moveTile.score + " " + temp);
            return new Node(RootNode.moveTile.row, RootNode.moveTile.column, RootNode.moveTile.score, temp);
        }
        if (maximizer) {
            //Log.v("Maximizer", "");
            Node bestNode = new Node(0, 0, 0, Integer.MIN_VALUE);
            Placement placement = new Placement();
            placement.SetStock(deck.returnCurrentPile());
            MoveTile child = placement.getNextMove();
            deck.nextButtonStartsHere++;

            while (child != null) {
                Log.v("Child Score",""+child.score);
                Log.v("Computer Score",""+Score_Computer);
                Log.v("Human Score:",""+Score_Human);
                if (Computer_Turn) {
                    Score_Computer += child.score;
                    Computer_Turn = false;
                    Log.v("Computer Score",""+Score_Computer);
                    Log.v("Human Score:",""+Score_Human);
                } else {
                    Score_Human += child.score;
                    Computer_Turn = true;
                    Log.v("Computer Score",""+Score_Computer);
                    Log.v("Human Score:",""+Score_Human);
                }
                //Log.v("Computer Score: ", ""+Score_Computer+" Human Score: "+Score_Human);
                boardTiles[child.row][child.column].FilledUp = true;
                boardTiles[child.row][child.column].color = placement.color;
                boardTiles[child.row][child.column].color = placement.symbol;
                Node resultNode = MinimaxAlgorithm(new Node(child), Score_Human, Score_Computer, Cutoff - 1, Computer_Turn, false);
                Log.v("Best Node was: ", "" + bestNode.heuristic + " " + bestNode.moveTile.row + " " + bestNode.moveTile.column);
                if (bestNode.heuristic < resultNode.heuristic) {
                    bestNode = resultNode;
                    bestNode.moveTile = child;
                    Log.v("BestNode:", "Yes");
                    Log.v("Best Node became: ", "" + bestNode.heuristic + " " + bestNode.moveTile.row + " " + bestNode.moveTile.column);
                } else {
                    Log.v("Best Node became same: ", "" + bestNode.heuristic + " " + bestNode.moveTile.row + " " + bestNode.moveTile.column);
                }
                boardTiles[child.row][child.column].FilledUp = false;
                boardTiles[child.row][child.column].color = 0;
                boardTiles[child.row][child.column].color = 0;
                child = placement.getNextMove();
                Computer_Turn=!Computer_Turn;
            }
            Log.v("BestNode Return:", " h:" + bestNode.heuristic + " r:" + bestNode.moveTile.row + " c:" + bestNode.moveTile.column);
            deck.nextButtonStartsHere--;
            return bestNode;

        } else {
            //Log.v("Minimizer", "");
            Node bestNode = new Node(0, 0, 0, Integer.MAX_VALUE);
            Placement placement = new Placement();
            placement.SetStock(deck.returnCurrentPile());
            MoveTile child = placement.getNextMove();
            deck.nextButtonStartsHere++;

            while (child != null) {
                //Log.v("Child Score",""+child.score);
                if (Computer_Turn) {
                    Score_Computer += child.score;
                    Computer_Turn = false;
                } else {

                    Score_Human += child.score;
                    Computer_Turn = true;
                }
                //Log.v("Computer Score: ", ""+Score_Computer+" Human Score: "+Score_Human);
                boardTiles[child.row][child.column].FilledUp = true;
                boardTiles[child.row][child.column].color = placement.color;
                boardTiles[child.row][child.column].color = placement.symbol;
                Node resultNode = MinimaxAlgorithm(new Node(child), Score_Human, Score_Computer, Cutoff - 1, Computer_Turn, true);
                Log.v("Best Node was: ", "" + bestNode.heuristic + " " + bestNode.moveTile.row + " " + bestNode.moveTile.column);
                if (bestNode.heuristic > resultNode.heuristic) {
                    bestNode = resultNode;
                    Log.v("BestNode:", "Yes");
                    Log.v("Best Node became: ", "" + bestNode.heuristic + " " + bestNode.moveTile.row + " " + bestNode.moveTile.column);
                } else {
                    Log.v("Best Node became same: ", "" + bestNode.heuristic + " " + bestNode.moveTile.row + " " + bestNode.moveTile.column);
                }
                boardTiles[child.row][child.column].FilledUp = false;
                boardTiles[child.row][child.column].color = 0;
                boardTiles[child.row][child.column].color = 0;
                child = placement.getNextMove();
                Computer_Turn=!Computer_Turn;

            }
            Log.v("BestNode Return:", " h:" + bestNode.heuristic + " r:" + bestNode.moveTile.row + " c:" + bestNode.moveTile.column);
            deck.nextButtonStartsHere--;
            return bestNode;

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        int i = 0;
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

