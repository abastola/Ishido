package com.arjunbastola.ishido;

import android.util.Log;

/**
 * Created by Arjun Bastola on 3/19/2016.
 */
public class Node {
    int layout=0;
    int x_axis=0;
    int y_axis=0;
    int score=0;
    int heuristic=0;
    MoveTile moveTile;
    boolean Solutionexists=false;

    public Node(int row, int column, int score, int heuristic) {
        //Log.v("Velue to use: ", "" +row + " " + column + " " + score + " " + heuristic);
        moveTile=new MoveTile();
        moveTile.column=column;
        moveTile.row=row;
        this.score = score;
        this.heuristic = heuristic;

       // Log.v("New Created: ", "" + moveTile.row + " " + moveTile.column + " " + moveTile.score + " " + heuristic);
    }


    public Node(MoveTile abc, int score, int heuristic) {
        moveTile=new MoveTile();
        moveTile=abc;
        this.score = score;
        this.heuristic = heuristic;
        moveTile=new MoveTile();
    }

    public Node(int heuristic, boolean solutionexists) {
        this.heuristic = heuristic;
        Solutionexists = solutionexists;
        moveTile=new MoveTile();
    }

    public Node(MoveTile child){
        moveTile=new MoveTile();
        moveTile=child;
    }
    public Node(){
        moveTile=new MoveTile();
    }


}
