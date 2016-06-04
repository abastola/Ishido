package com.arjunbastola.ishido;

/**
 * Created by Arjun Bastola on 2/20/2016.
 */
public class Deck {
    int[] stock=new int[72];
    int stockIndex=0;
    int numberOfStock=0;

    public void setStock(int[] stock) {
        this.stock = stock;
    }

    public void setNumberOfStock(int numberOfStock) {
        this.numberOfStock = numberOfStock;
    }

    public Deck() {

    }

    public Deck(int[] stock, int stockIndex, int numberOfStock) {

        this.stock = stock;
        this.stockIndex = stockIndex;
        this.numberOfStock = numberOfStock;
    }

    public int getStock(){
        return stock[stockIndex++];
    }
}
