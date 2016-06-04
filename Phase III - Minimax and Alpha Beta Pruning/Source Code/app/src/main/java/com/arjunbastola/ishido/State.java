package com.arjunbastola.ishido;

import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;

/**
 * Created by Arjun Bastola on 2/20/2016.
 */
public class State {


    static int[][] layoutBoard = new int[10][14];
    static int[] stockBoard = new int[72];
    static int numberOfStock = 0;
    static int scoreBoard = 5;
    static int ComputerScore=0;
    static String nextPlayer;
    static Tiles[][] boardTiles = new Tiles[10][14];


    public int[] getStockBoard(){
        return stockBoard;
    }

    public static void read(File file) throws IOException {
        try {
            Scanner scanner = new Scanner(file);
            int i = 0;
            String[] random = scanner.nextLine().split(" ");
            while (scanner.hasNext()) {
                String[] tokens = scanner.nextLine().split(" ");
                for (int j = 0; j < 12; j++) {
                    String last = tokens[tokens.length - (12 - j)];
                    layoutBoard[i+1][j+1] = Integer.parseInt(last);
                    boardTiles[i+1][j+1] = new Tiles(i+1, j+1, layoutBoard[i+1][j+1]);
                }
                i++;

                if (i == 8) {
                    break;
                }

            }


            random = scanner.nextLine().split(" ");
            random = scanner.nextLine().split(" ");
            i = 0;
            while (scanner.hasNext()) {
                String[] tokens = scanner.nextLine().split(" ");
                numberOfStock = tokens.length;
                for (int j = 0; j < tokens.length; j++) {
                    String last = tokens[tokens.length - (tokens.length - j)];
                    stockBoard[j] = Integer.parseInt(last);

                }
                i++;
                if (i == 1) {
                    break;
                }

            }
            random = scanner.nextLine().split(" ");
            random = scanner.nextLine().split(" ");

            i = 0;
            while (scanner.hasNext()) {
                String[] tokens = scanner.nextLine().split(" ");
                for (int j = tokens.length; j > 0; j--) {
                    String last = tokens[tokens.length - j];
                    scoreBoard = Integer.parseInt(last);
                }

                i++;

                if (i == 1) {
                    break;
                }

            }
            random = scanner.nextLine().split(" ");
            random = scanner.nextLine().split(" ");

            i = 0;
            while (scanner.hasNext()) {
                String[] tokens = scanner.nextLine().split(" ");
                for (int j = tokens.length; j > 0; j--) {
                    String last = tokens[tokens.length - j];
                    ComputerScore = Integer.parseInt(last);
                }

                i++;

                if (i == 1) {
                    break;
                }

            }

            random = scanner.nextLine().split(" ");
            random = scanner.nextLine().split(" ");

            i = 0;
            while (scanner.hasNext()) {

                    String last = scanner.nextLine();
                    nextPlayer=last;

                i++;

                if (i == 1) {
                    break;
                }

            }
        }catch (Exception ex) {
            Log.e("File Error: ", "State.java Upper");
        }
    }

    public State(File file) throws IOException{

        read(file);

    }
}
