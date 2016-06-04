package com.arjunbastola.ishido;

import android.graphics.Color;
import android.widget.Button;

/**
 * Created by Arjun Bastola on 2/20/2016.
 */
public class Tiles {


    int xaxis;
    int yaxis;
    int color;
    int symbol;
    Boolean FilledUp;
    Button button;

    public Tiles(int x, int y, int layout) {
        this.xaxis = x;
        this.yaxis = y;
        int a = layout / 10;
        int b = layout % 10;
        this.color = a;
        this.symbol = b;
        if (layout != 0) {
            FilledUp = true;
        } else {
            FilledUp = false;
        }
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public int setColor() {
        int returnColor = 0;
        switch (color) {

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
                returnColor=Color.WHITE;

        }
        return returnColor;
    }


    public String getString() {
        if(symbol==0){
            return "";
        }else {
            return Integer.toString(symbol);
        }
    }

    public int getColor() {
        int returnColor = 0;
        switch (color) {
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
                returnColor=Color.WHITE;
        }
        return returnColor;
    }

    public void setUsed(Boolean isSet) {
        this.FilledUp = isSet;
    }

    public void setString(String symbol) {
        this.symbol = Integer.parseInt(symbol);
    }

    public Boolean isFilledUp() {
        return FilledUp;
    }
}


