package com.example.lightout.logic;

import java.io.Serializable;

public class Board extends Subject implements Serializable {
    private boolean state[][];  //pivoted matrix
    public boolean[][] getBoard(){return state;}
    private int size;
    public int getSize(){
        return size;
    }

    private int lightedNum;

    public Board(int size){
        this.size = size;
        state = new boolean[size+2][size+2];
        lightedNum = size^2;
    }

    public Board(boolean state[][]){
        this.state = state;
        size = state.length - 2;
        int iterSize = size + 1;
        lightedNum = 0;
        for(int i =1; i < iterSize; ++i){
            for(int j =1; j < iterSize; ++j){
                if(state[i][j] == true) ++lightedNum;
            }
        }
    }

    public boolean checkWin(){
        return lightedNum == 0;
    }

    private void UpdateElementInMatrix(int i, int j){
        state[i][j] = !state[i][j];
        if (state[i][j]){
            ++lightedNum;
        }else{
            --lightedNum;
        }
        notifyChange(i-1, j-1, state[i][j]); //in screen cord
    }

    public void makeMove(int iScreen, int jScreen){
        int i = iScreen + 1;
        int j = jScreen + 1;
        UpdateElementInMatrix(i,j);
        UpdateElementInMatrix(i+1,j);
        UpdateElementInMatrix(i,j-1);
        UpdateElementInMatrix(i+1,j);
        UpdateElementInMatrix(i,j+1);
        notifyEnd();
    }
}
