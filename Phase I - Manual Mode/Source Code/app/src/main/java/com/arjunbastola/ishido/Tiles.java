/*
        ************************************************************
        * Name:  Arjun Bastola                                     *
        * Project:  Project 1 - Ishido                             *
        * Class:  Artificial Intelligence                          *
        * Date:  29th January, 2016                                *
        * Class: Tiles(Normal Java Class)                          *
        * Description: Class that stores informaion about each     *
        * tiles like grid values, weather or not it is filled up,  *
        * color of the tiles and symbol/shape of the tile          *
        ************************************************************
*/


package com.arjunbastola.ishido;

import android.graphics.Color;
import android.widget.Button;

/**
 * Created by Arjun Bastola on 2/2/2016.
 */
public class Tiles {
    public int vertical; //store column value ie the y axis
    public int horizontal; //store row value ie the x axis
    public boolean FilledUp; //indicates weather or not the tiles have been already used
    public Button button;  //stores the button from the xml layout
    public int color;  //stores the color of the tile/button
    public String string; //stores the string/shape/symbol


    //constructor
    public Tiles(int vertical, int horizontal, boolean filledUp, Button button, String string, int color) {
        this.vertical = vertical;
        this.horizontal = horizontal;
        this.FilledUp=filledUp;
        this.button=button;
        this.color=color;
        this.string=string;
    }

    //setters and getters of all the values
    public void setVertical(int vertical) {
        this.vertical = vertical;
    }

    public void setHorizontal(int horizontal) {
        this.horizontal = horizontal;
    }

    public void setFilledUp(boolean filledUp) {
        FilledUp = filledUp;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getVertical() {

        return vertical;
    }

    public int getHorizontal() {
        return horizontal;
    }

    public boolean isFilledUp() {
        return FilledUp;
    }

    public Button getButton() {
        return button;
    }

    public int getColor() {
        return color;
    }

    public String getString() {
        return string;
    }
}
