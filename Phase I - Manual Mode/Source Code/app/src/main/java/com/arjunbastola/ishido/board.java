/*
        ************************************************************
        * Name:  Arjun Bastola                                     *
        * Project:  Project 1 - Ishido                             *
        * Class:  Artificial Intelligence                          *
        * Date:  29th January, 2016                                *
        * Class: Board (Activity Class)                            *
        * Description: Contains gesture controls, button placement *
        * controls etc.                                            *
        ************************************************************
*/



package com.arjunbastola.ishido;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.arjunbastola.ishido.Tiles;

import org.w3c.dom.Text;

//there are some errors here with the spinner and spinner's adapter. Still trying to fix this.
public class board extends ActionBarActivity  implements AdapterView.OnItemSelectedListener {
    public board() {
    }

    final Tiles[][] boardTiles=new Tiles[10][14];
    final buttonType[][] availableButtons=new buttonType[6][6];
    public static int HumanScore=0;
    public static int numberOfTilesUsed=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hides the actionbar of the activity
        setContentView(R.layout.activity_board);

        // set the string resources to the spinners
        final Spinner ColorSpinner, SymbolSpinner;  //spinners for two drop down
        ColorSpinner = (Spinner) findViewById(R.id.Color); //spinner for color selection drop down
        SymbolSpinner = (Spinner) findViewById(R.id.Symbo1); //spinner for symbol/shape selection drop down
        ArrayAdapter ColorAdapter=ArrayAdapter.createFromResource(this, R.array.colors, android.R.layout.simple_spinner_item); //assigns string array of colors from the strings.xml file to the spinner
        ArrayAdapter SymbolAdapter=ArrayAdapter.createFromResource(this, R.array.symbols, android.R.layout.simple_spinner_item); //assigns string array of symbols from the strings.xml file to the spinner
        ColorSpinner.setAdapter(ColorAdapter); //assigns the adapter to the listener
        SymbolSpinner.setAdapter(SymbolAdapter); //assigns the adapter to the listener
        ColorSpinner.setOnItemSelectedListener(this); //setOnItemSelectedListener event for color selection drop down. (function at the end of the file)
        SymbolSpinner.setOnItemSelectedListener(this); //setOnItemSelectedListener event for symbol/shape selection drop down. (function at the end of the file)
        //end of spinners

        //creates 36 objects of buttonType class
        int[] btnColors = {Color.WHITE, Color.RED, Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW}; //colors used in the project
        String[] btnSymbols = {"!", "$", "#", "%", "*", "-"}; //symbols used in the project
        for (int i=0; i<6; i++){
            for (int j=0; j<6; j++){
                availableButtons[i][j]=new buttonType(btnColors[i], btnSymbols[j], false); //creates object with successive values from the arrays above
            }
        }




        // Enable Color/Symbol Selection according to the mode
        RadioGroup modeSelect =(RadioGroup) findViewById(R.id.modeSelect);  //
        modeSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { //when a mode is selected
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.Random) { //if mode selected is random, random tile is generated
                    ColorSpinner.setEnabled(false); //disables the color selection drop down
                    SymbolSpinner.setEnabled(false); //disables the symbol/shape selection drop down
                    Toast.makeText(getApplicationContext(), "Random Mode Selected!", Toast.LENGTH_SHORT).show(); //shows message about which mode is selected

                    //finds a random available tile
                    boolean unsedTileFound=false;
                    for (int a=0; a<6; a++){
                        for (int b=0; b<6; b++){
                            if(availableButtons[a][b].counter!=0){
                                Button previewBtn=(Button)findViewById(R.id.btnsample);
                                previewBtn.setBackgroundColor(availableButtons[a][b].getColor());
                                previewBtn.setText(availableButtons[a][b].getSymbol());
                                unsedTileFound=true;
                            }
                            if(unsedTileFound==true){
                                break;
                            }
                        }
                        if(unsedTileFound==true){
                            break;
                        }
                    }
                    unsedTileFound=false;


                } else if (checkedId == R.id.Select) {
                    ColorSpinner.setEnabled(true); //enables the color selection drop down
                    SymbolSpinner.setEnabled(true); //enables the symbol/shape selection drop down
                    Toast.makeText(getApplicationContext(), "Select Mode Selected!", Toast.LENGTH_SHORT).show();  //shows message about which mode is selected
                }
            }
        });

        // end of the disable/enable using radio button selection

        // A 2D array of Tiles Object with their location on grid

        Button[][] buttons=new Button[9][13];
        buttons[1][1]=(Button) findViewById(R.id.btn11); //assigns the given View from xml file to the given button. Same for next 95 Button Views
        buttons[1][2]=(Button) findViewById(R.id.btn12);
        buttons[1][3]=(Button) findViewById(R.id.btn13);
        buttons[1][4]=(Button) findViewById(R.id.btn14);
        buttons[1][5]=(Button) findViewById(R.id.btn15);
        buttons[1][6]=(Button) findViewById(R.id.btn16);
        buttons[1][7]=(Button) findViewById(R.id.btn17);
        buttons[1][8]=(Button) findViewById(R.id.btn18);
        buttons[1][9]=(Button) findViewById(R.id.btn19);
        buttons[1][10]=(Button) findViewById(R.id.btn110);
        buttons[1][11]=(Button) findViewById(R.id.btn111);
        buttons[1][12]=(Button) findViewById(R.id.btn112);

        buttons[2][1]=(Button) findViewById(R.id.btn21);
        buttons[2][2]=(Button) findViewById(R.id.btn22);
        buttons[2][3]=(Button) findViewById(R.id.btn23);
        buttons[2][4]=(Button) findViewById(R.id.btn24);
        buttons[2][5]=(Button) findViewById(R.id.btn25);
        buttons[2][6]=(Button) findViewById(R.id.btn26);
        buttons[2][7]=(Button) findViewById(R.id.btn27);
        buttons[2][8]=(Button) findViewById(R.id.btn28);
        buttons[2][9]=(Button) findViewById(R.id.btn29);
        buttons[2][10]=(Button) findViewById(R.id.btn210);
        buttons[2][11]=(Button) findViewById(R.id.btn211);
        buttons[2][12]=(Button) findViewById(R.id.btn212);

        buttons[3][1]=(Button) findViewById(R.id.btn31);
        buttons[3][2]=(Button) findViewById(R.id.btn32);
        buttons[3][3]=(Button) findViewById(R.id.btn33);
        buttons[3][4]=(Button) findViewById(R.id.btn34);
        buttons[3][5]=(Button) findViewById(R.id.btn35);
        buttons[3][6]=(Button) findViewById(R.id.btn36);
        buttons[3][7]=(Button) findViewById(R.id.btn37);
        buttons[3][8]=(Button) findViewById(R.id.btn38);
        buttons[3][9]=(Button) findViewById(R.id.btn39);
        buttons[3][10]=(Button) findViewById(R.id.btn310);
        buttons[3][11]=(Button) findViewById(R.id.btn311);
        buttons[3][12]=(Button) findViewById(R.id.btn312);

        buttons[4][1]=(Button) findViewById(R.id.btn41);
        buttons[4][2]=(Button) findViewById(R.id.btn42);
        buttons[4][3]=(Button) findViewById(R.id.btn43);
        buttons[4][4]=(Button) findViewById(R.id.btn44);
        buttons[4][5]=(Button) findViewById(R.id.btn45);
        buttons[4][6]=(Button) findViewById(R.id.btn46);
        buttons[4][7]=(Button) findViewById(R.id.btn47);
        buttons[4][8]=(Button) findViewById(R.id.btn48);
        buttons[4][9]=(Button) findViewById(R.id.btn49);
        buttons[4][10]=(Button) findViewById(R.id.btn410);
        buttons[4][11]=(Button) findViewById(R.id.btn411);
        buttons[4][12]=(Button) findViewById(R.id.btn412);

        buttons[5][1]=(Button) findViewById(R.id.btn51);
        buttons[5][2]=(Button) findViewById(R.id.btn52);
        buttons[5][3]=(Button) findViewById(R.id.btn53);
        buttons[5][4]=(Button) findViewById(R.id.btn54);
        buttons[5][5]=(Button) findViewById(R.id.btn55);
        buttons[5][6]=(Button) findViewById(R.id.btn56);
        buttons[5][7]=(Button) findViewById(R.id.btn57);
        buttons[5][8]=(Button) findViewById(R.id.btn58);
        buttons[5][9]=(Button) findViewById(R.id.btn59);
        buttons[5][10]=(Button) findViewById(R.id.btn510);
        buttons[5][11]=(Button) findViewById(R.id.btn511);
        buttons[5][12]=(Button) findViewById(R.id.btn512);

        buttons[6][1]=(Button) findViewById(R.id.btn61);
        buttons[6][2]=(Button) findViewById(R.id.btn62);
        buttons[6][3]=(Button) findViewById(R.id.btn63);
        buttons[6][4]=(Button) findViewById(R.id.btn64);
        buttons[6][5]=(Button) findViewById(R.id.btn65);
        buttons[6][6]=(Button) findViewById(R.id.btn66);
        buttons[6][7]=(Button) findViewById(R.id.btn67);
        buttons[6][8]=(Button) findViewById(R.id.btn68);
        buttons[6][9]=(Button) findViewById(R.id.btn69);
        buttons[6][10]=(Button) findViewById(R.id.btn610);
        buttons[6][11]=(Button) findViewById(R.id.btn611);
        buttons[6][12]=(Button) findViewById(R.id.btn612);

        buttons[7][1]=(Button) findViewById(R.id.btn71);
        buttons[7][2]=(Button) findViewById(R.id.btn72);
        buttons[7][3]=(Button) findViewById(R.id.btn73);
        buttons[7][4]=(Button) findViewById(R.id.btn74);
        buttons[7][5]=(Button) findViewById(R.id.btn75);
        buttons[7][6]=(Button) findViewById(R.id.btn76);
        buttons[7][7]=(Button) findViewById(R.id.btn77);
        buttons[7][8]=(Button) findViewById(R.id.btn78);
        buttons[7][9]=(Button) findViewById(R.id.btn79);
        buttons[7][10]=(Button) findViewById(R.id.btn710);
        buttons[7][11]=(Button) findViewById(R.id.btn711);
        buttons[7][12]=(Button) findViewById(R.id.btn712);

        buttons[8][1]=(Button) findViewById(R.id.btn81);
        buttons[8][2]=(Button) findViewById(R.id.btn82);
        buttons[8][3]=(Button) findViewById(R.id.btn83);
        buttons[8][4]=(Button) findViewById(R.id.btn84);
        buttons[8][5]=(Button) findViewById(R.id.btn85);
        buttons[8][6]=(Button) findViewById(R.id.btn86);
        buttons[8][7]=(Button) findViewById(R.id.btn87);
        buttons[8][8]=(Button) findViewById(R.id.btn88);
        buttons[8][9]=(Button) findViewById(R.id.btn89);
        buttons[8][10]=(Button) findViewById(R.id.btn810);
        buttons[8][11]=(Button) findViewById(R.id.btn811);
        buttons[8][12]=(Button) findViewById(R.id.btn812);

        //sets border of the tiles as used-false and a random color
        //did this in order to make selection on the ends easier
        //makes sure that the edges tiles can be customized
        Button tempBtn=(Button)findViewById(R.id.btnsample);
        for (int i=0; i<10; i++){
            boardTiles[i][0]=new Tiles(i, 0, false, tempBtn, "", 123456789);
        }
        for (int i=0; i<14; i++){
            boardTiles[0][i]=new Tiles(0, i, false, tempBtn, "", 123456789);
        }
        for (int i=0; i<10; i++){
            boardTiles[i][13]=new Tiles(i, 13, false, tempBtn, "", 123456789);
        }
        for (int i=0; i<14; i++){
            boardTiles[9][i]=new Tiles(9, i, false, tempBtn, "", 123456789);
        }

        //end of edges customization

        //sets onClick event listener to all the tiles (96 tiles) present in the board xml file
        for (int i=1; i<=8; i++){
            for (int j=1; j<=12; j++){
                boardTiles[i][j]=new Tiles(i, j, false, buttons[i][j], "", 123456789);
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
                    Button previewBtn=(Button)findViewById(R.id.btnsample); //get button from the preview button
                    ColorDrawable buttonColor = (ColorDrawable) previewBtn.getBackground(); //get colordrawable from the preview button
                    int colorId = buttonColor.getColor(); ////get color from the preview button
                    String setString=(String) previewBtn.getText(); //get string/symbol/shape from the preview button

                    for (int a=0; a<6; a++){
                        for (int b=0; b<6; b++){
                            if(((availableButtons[a][b].getColor())==colorId) && ((availableButtons[a][b].getSymbol())==setString)){ //analyze which sample buttonType has been used
                                if(availableButtons[a][b].counter>0){ //use the preview button's profile only if no more than 2 times used
                                    if (boardTiles[i][j].isFilledUp()==false){ //only use the tile if it has not been previously used
                                        if((boardTiles[i-1][j].getString()==setString) || (boardTiles[i-1][j].getColor()==colorId)){ //see if top has same symbol/color..if yes set properties of the preiew button
                                            Button currentBtn = (Button) boardTiles[i][j].button;
                                            currentBtn.setText(setString);
                                            currentBtn.setTextColor(getResources().getColor(android.R.color.black));
                                            currentBtn.setBackgroundColor(colorId);
                                            TextView HumanScoreUpdate = (TextView) findViewById(R.id.HumanScore);
                                            HumanScore++; //Increase the score
                                            HumanScoreUpdate.setText(Integer.toString(HumanScore));
                                            boardTiles[i][j].FilledUp=true;
                                            boardTiles[i][j].setColor(colorId);
                                            boardTiles[i][j].setString(setString);
                                            if(availableButtons[a][b].isUsed()==false) {
                                                availableButtons[a][b].setUsed(true);
                                                (availableButtons[a][b].counter)--; //decrease the available count of the particular buttonType
                                                numberOfTilesUsed++;
                                            }

                                        }
                                        if((boardTiles[i+1][j].getString()==setString) || (boardTiles[i+1][j].getColor()==colorId)){ //see if bottom has same symbol/color..if yes set properties of the preiew button
                                            Button currentBtn = (Button) boardTiles[i][j].button;
                                            currentBtn.setText(setString);
                                            currentBtn.setTextColor(getResources().getColor(android.R.color.black));
                                            currentBtn.setBackgroundColor(colorId);
                                            TextView HumanScoreUpdate = (TextView) findViewById(R.id.HumanScore);
                                            HumanScore++;
                                            HumanScoreUpdate.setText(Integer.toString(HumanScore));
                                            boardTiles[i][j].FilledUp=true;
                                            boardTiles[i][j].setColor(colorId);
                                            boardTiles[i][j].setString(setString);
                                            if(availableButtons[a][b].isUsed()==false) {
                                                availableButtons[a][b].setUsed(true);
                                                (availableButtons[a][b].counter)--; //decrease the available count of the particular buttonType
                                                numberOfTilesUsed++;
                                            }

                                        }
                                        if((boardTiles[i][j-1].getString()==setString) || (boardTiles[i][j-1].getColor()==colorId)){ //see if left has same symbol/color..if yes set properties of the preiew button
                                            Button currentBtn = (Button) boardTiles[i][j].button;
                                            currentBtn.setText(setString);
                                            currentBtn.setTextColor(getResources().getColor(android.R.color.black));
                                            currentBtn.setBackgroundColor(colorId);
                                            TextView HumanScoreUpdate = (TextView) findViewById(R.id.HumanScore);
                                            HumanScore++; //Increase the score
                                            HumanScoreUpdate.setText(Integer.toString(HumanScore));
                                            boardTiles[i][j].FilledUp=true;
                                            boardTiles[i][j].setColor(colorId);
                                            boardTiles[i][j].setString(setString);
                                            if(availableButtons[a][b].isUsed()==false) {
                                                availableButtons[a][b].setUsed(true);
                                                (availableButtons[a][b].counter)--; //decrease the available count of the particular buttonType
                                                numberOfTilesUsed++;
                                            }

                                        }
                                        if((boardTiles[i][j+1].getString()==setString) || (boardTiles[i][j+1].getColor()==colorId)){ //see if right has same symbol/color..if yes set properties of the preiew button
                                            Button currentBtn = (Button) boardTiles[i][j].button;
                                            currentBtn.setText(setString);
                                            currentBtn.setTextColor(getResources().getColor(android.R.color.black));
                                            currentBtn.setBackgroundColor(colorId);
                                            TextView HumanScoreUpdate = (TextView) findViewById(R.id.HumanScore);
                                            HumanScore++; //Increase the score
                                            HumanScoreUpdate.setText(Integer.toString(HumanScore));
                                            boardTiles[i][j].FilledUp=true;
                                            boardTiles[i][j].setColor(colorId);
                                            boardTiles[i][j].setString(setString);
                                            if(availableButtons[a][b].isUsed()==false) {
                                                availableButtons[a][b].setUsed(true);
                                                (availableButtons[a][b].counter)--; //decrease the available count of the particular buttonType
                                                numberOfTilesUsed++;
                                            }

                                        }
                                        if(((boardTiles[i-1][j].getString()=="") || (boardTiles[i-1][j].getColor()==123456789)) && ((boardTiles[i+1][j].getString()=="") || (boardTiles[i+1][j].getColor()==123456789)) && ((boardTiles[i][j+1].getString()=="") || (boardTiles[i][j+1].getColor()==123456789)) && ((boardTiles[i][j-1].getString()=="") || (boardTiles[i][j-1].getColor()==123456789))){
                                            //see if it's surrounding are unoccupied
                                            //score will not be updated in this condition
                                            Button currentBtn = (Button) boardTiles[i][j].button;
                                            currentBtn.setText(setString);
                                            currentBtn.setTextColor(getResources().getColor(android.R.color.black));
                                            currentBtn.setBackgroundColor(colorId);
                                            TextView HumanScoreUpdate = (TextView) findViewById(R.id.HumanScore);
                                            HumanScoreUpdate.setText(Integer.toString(HumanScore));
                                            boardTiles[i][j].FilledUp=true;
                                            boardTiles[i][j].setColor(colorId);
                                            boardTiles[i][j].setString(setString);
                                            if(availableButtons[a][b].isUsed()==false) {
                                                availableButtons[a][b].setUsed(true);
                                                (availableButtons[a][b].counter)--; //decrease the available count of the particular buttonType
                                                numberOfTilesUsed++;
                                            }
                                        }
                                        availableButtons[a][b].setUsed(false);

                                        /*
                                        else{
                                            // if user doesn't follow rule of placement, toast the error message
                                            Toast.makeText(getApplicationContext(), "Place the tile adjacent to (left/right/above/below) another tile if the color or the symbol of the two tiles match", Toast.LENGTH_LONG).show();
                                        }
                                        */

                                    }else{
                                            //if user clicks initially clicked tile, toast the error message
                                        Toast.makeText(getApplicationContext(), "Invalid Move. This tile has already been set.", Toast.LENGTH_LONG).show();
                                    }


                                }else{
                                    // if the particular button type has been used more than 2 times, toast the user message
                                    Toast.makeText(getApplicationContext(), "This combination has already been used two times.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }


                }
            }
        }

        Button endthegame = (Button) findViewById(R.id.endtheGame);
        endthegame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntentonEndGame = new Intent(board.this, GameEnd.class);
                startActivity(myIntentonEndGame); //starts activity board
            }
        });



        if (numberOfTilesUsed==72){
            //launch new Activity Here

            Intent myIntent=new Intent(board.this, GameEnd.class);
            startActivity(myIntent); //starts activity board
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK); //sets the color of the drop down elements to black
        ((TextView) parent.getChildAt(0)).setTextSize(15); //sets the text size of the drop down to 15

        //WHITE, RED, BLUE, GREEN, CYAN, YELLOW
        //change the color and text of the preview button according to the user selections through the drop down menus
        TextView myText=(TextView) view;
        Button previewBtn=(Button) findViewById(R.id.btnsample);
        switch (parent.getItemAtPosition(position).toString()){
            case "White":
                previewBtn.setBackgroundColor(Color.WHITE);
                break;
            case "Red":
                previewBtn.setBackgroundColor(Color.RED);
                break;
            case "Blue":
                previewBtn.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                previewBtn.setBackgroundColor(Color.GREEN);
                break;
            case "Cyan":
                previewBtn.setBackgroundColor(Color.CYAN);
                break;
            case "Yellow":
                previewBtn.setBackgroundColor(Color.YELLOW);
                break;
            case "!":
                previewBtn.setText("!");
                break;
            case "$":
                previewBtn.setText("$");
                break;
            case "#":
                previewBtn.setText("#");
                break;
            case "%":
                previewBtn.setText("%");
                break;
            case "*":
                previewBtn.setText("*");
                break;
            case "-":
                previewBtn.setText("-");
            default:
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}

//end of the program
//Thank You!



