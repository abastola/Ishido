package com.arjunbastola.ishido;

import android.util.Log;

/**
 * Created by Arjun Bastola on 3/20/2016.
 */
public class Placement {
    MoveTile[] Movement =new MoveTile[96];
    int Stock=0;
    int indexReturn=0;
    board tempClass;
    int totalMoves=0;
    int color;
    int symbol;

    public Placement(){
        for ( int i=0; i<Movement.length; i++) {
            Movement[i]=new MoveTile();
        }
        tempClass=new board();
    }

    public void SetStock(int stock) {
        color=stock/10;
        symbol=stock%10;
        //printOutContentsOfBoard();
        Stock = stock;
        // Log.v("Stock: ",""+Stock);
        for (int i=0; i<96; i++){
            Movement[i].stock=0;
        }
        int index=1;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 12; j++) {
                if (!(tempClass.boardTiles[i][j].isFilledUp())) {
                    int result = CheckIfTileCanBePlacedMiniMax(i, j,Stock);
                    if (result % 10 == 4) {
                        Log.v("Possible ","Place Found "+i+" "+j+" Score: "+result/10);
                        Movement[index].row=i;
                        Movement[index].column=j;
                        Movement[index].score=result/10;
                        Movement[index].exists=true;
                        Movement[index].stock=Stock;

                        index++;
                    }

                }
            }
        }
        totalMoves=index-1;
        //printOutMoves();
    }


   /* public void printOutContentsOfBoard(){
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 12; j++) {
                Log.v("boardTiles: ", ""+i +" "+j+" color: "+ (tempClass.boardTiles[i][j]).color+" symbol: "+(tempClass.boardTiles[i][j]).symbol);
            }
        }
    }
*/

    public void printOutMoves(){
        for (int i=1; i<=totalMoves;i++) {
            Log.v("stockIndex: ", "Move "+i +": row: "+Movement[i].row+" column: "+Movement[i].column+" score: "+Movement[i].score);
        }
    }


    public MoveTile getNextMove(){
        indexReturn++;
       // Log.v("Returned: ", ""+ indexReturn);
        if(indexReturn<=totalMoves) {
            //Log.v("Returned: ", ""+ Movement[indexReturn].row +" " + Movement[indexReturn].column + " " + Movement[indexReturn].score +" " );

            return Movement[indexReturn];
        }else{
            return null;
        }
    }

    public int CheckIfTileCanBePlacedMiniMax(int i, int j, int stock){
        makeAllSurrundingSameAsPreviewButton();
        int match=0;
        int scoreToBeIncreased=0;
        if(((tempClass.boardTiles[i][j-1]).color==(stock/10) || (tempClass.boardTiles[i][j-1]).symbol==(stock%10) || (tempClass.boardTiles[i][j-1]).symbol==0)){
            match++;
            if(((tempClass.boardTiles[i][j-1]).color==(stock/10) || (tempClass.boardTiles[i][j-1]).symbol==(stock%10))){
                scoreToBeIncreased++;
                //Log.v("stockIndex: ",i+" "+j+" "+"Match left");

            }
            // m1="match left ";

        }
        if(((tempClass.boardTiles[i][j+1]).color==(stock/10) || (tempClass.boardTiles[i][j+1]).symbol==(stock%10) || (tempClass.boardTiles[i][j+1]).symbol==0)){
            match++;
            if(((tempClass.boardTiles[i][j+1]).color==(stock/10) || (tempClass.boardTiles[i][j+1]).symbol==(stock%10))){
                scoreToBeIncreased++;
                // Log.v("stockIndex: ",i+" "+j+" "+"Match right");

            }
            // m2="match right ";

        }
        if(((tempClass.boardTiles[i-1][j]).color==(stock/10) || (tempClass.boardTiles[i-1][j]).symbol==(stock%10) || (tempClass.boardTiles[i-1][j]).symbol==0)){
            match++;
            if((tempClass.boardTiles[i-1][j]).color==(stock/10) || (tempClass.boardTiles[i-1][j]).symbol==(stock%10)){
                scoreToBeIncreased++;
                //Log.v("stockIndex: ",i+" "+j+" "+"Match top");

            }
            // m3="match top ";
        }
        if(((tempClass.boardTiles[i+1][j]).color==(stock/10) || (tempClass.boardTiles[i+1][j]).symbol==(stock%10) || (tempClass.boardTiles[i+1][j]).symbol==0)){
            match++;
            if(((tempClass.boardTiles[i+1][j]).color==(stock/10) || (tempClass.boardTiles[i+1][j]).symbol==(stock%10))){
                scoreToBeIncreased++;
                //Log.v("stockIndex: ",i+" "+j+" "+"Match bottom");

            }
            // m4="match bottom ";
        }

        // Toast.makeText(board.this, ""+m1+m2+m3+m4, Toast.LENGTH_LONG).show();
        // Log.v("Score to be Increased: ",""+scoreToBeIncreased);
        return scoreToBeIncreased*10+match;


    }

    public void makeAllSurrundingSameAsPreviewButton(){
        for (int i=0; i<10; i++){
            tempClass.boardTiles[i][0]=new Tiles(i, 0, 0);
        }
        for (int i=0; i<14; i++){
            tempClass.boardTiles[0][i]=new Tiles(0, i, 0);
        }
        for (int i=0; i<10; i++){
            tempClass.boardTiles[i][13]=new Tiles(i, 13, 0);
        }
        for (int i=0; i<14; i++){
            tempClass.boardTiles[9][i]=new Tiles(9, i, 0);
        }
    }


}
