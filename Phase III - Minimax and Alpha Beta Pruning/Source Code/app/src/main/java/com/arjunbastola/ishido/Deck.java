package com.arjunbastola.ishido;

import java.util.Random;

/**
 * Created by Arjun Bastola on 2/20/2016.
 */
public class Deck {
    int[] stock=new int[72];
    int stockIndex=0;
    int numberOfStock=0;
    int nextButtonStartsHere=0;

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
    public void clearTheTileFromDeck(int toClear){
        Boolean notFound=true;
        int i=0;
        while(notFound){
            if(stock[i]==toClear){
                stock[i]=0;
                notFound=false;

            }
            i++;
        }

    }

    public int getRandomStock(){
        int i=0;
        boolean somethingReturned=false;

      while (!somethingReturned) {
          if ((stock[stockIndex]) >= 11 && (stock[stockIndex]) <=66) {
              somethingReturned=true;
              int temp=stockIndex;
              stockIndex++;
              return stock[temp];

          } else {
              stockIndex=nextButtonStartsHere;
              i++;

          }
          if(i>72){
              return 0;
          }

      }
        return 0;
    }

    public int returnCurrentPile(){
        if (nextButtonStartsHere<=71) {
            return stock[nextButtonStartsHere];
        }else{
            return 0;
        }
    }


    public int getStock(){
        for (int i=0; i<72; i++){
            if (stock[i] !=0){
                return stock[i];
            }
        }
        return 0;
    }
}
