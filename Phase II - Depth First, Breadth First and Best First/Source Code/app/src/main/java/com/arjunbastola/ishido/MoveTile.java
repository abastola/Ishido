package com.arjunbastola.ishido;

/**
 * Created by Arjun Bastola on 2/22/2016.
 */
public class MoveTile {
    int row;
    int column;
    Tiles tile;
    Tiles parentTile;

    public MoveTile(){
        row=-10;
        column=-10;
        tile=null;
    }

    public MoveTile(int row, int column, Tiles tile, Tiles parentTile) {
        this.row = row;
        this.column = column;
        this.tile = tile;
        this.parentTile = parentTile;
    }

}
