/*
        ************************************************************
        * Name:  Arjun Bastola                                     *
        * Project:  Project 1 - Ishido                             *
        * Class:  Artificial Intelligence                          *
        * Date:  29th January, 2016                                *
        * Class: buttonType(Normal Java Classs)                    *
        * Description: Holds available button properties           *
        ************************************************************
*/


package com.arjunbastola.ishido;

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

/**
 * Created by Arjun Bastola on 1/31/2016.
 */

//each butonn has 3 properties: symbol, color and counter
public class buttonType {
    int color;              //color of the button
    String symbol;          //symbol/shape used in the button
    boolean used;           //weather or not the button has been used
    int counter;            //number of times button has been used

    //constructor
    public buttonType(int color, String symbol, boolean used) {

        this.color = color;
        this.symbol = symbol;
        this.used = used;
        this.counter=2;
    }
    //getters and setters for colors, symbols and counters;
    //color getter
    public int getColor() {
        return color;
    }
    //color setter

    public int getCounter() {
        return counter;
    }

    public void setColor(int color) {
        this.color = color;
    }

    //symbol/shape/string getter
    public String getSymbol() {
        return symbol;
    }
    //symbol/shape/string setter
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    //used getter
    public boolean isUsed() {
        return used;
    }
    //used setter
    public void setUsed(boolean used) {
        this.used = used;
    }


}
