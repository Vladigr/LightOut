package com.example.lightout.logic;

import android.util.Log;

import java.io.Serializable;
import java.util.Arrays;

public class Board extends Subject implements Serializable {
    private boolean state[][];  //pivoted matrix
    public boolean[][] getBoard(){return state;}
    public Boolean getElementInBoard(int iScreen, int jScreen){return state[iScreen+1][jScreen+1];}
    private int size;
    public int getSize(){
        return size;
    }

    private int lightedNum;

    public Board(int size){
        this.size = size;
        state = new boolean[size+2][size+2];
        for(int i=0; i < state.length; ++i){
            for(int j=0; j < state.length; ++j){
                state[i][j] = true;
            }
        }
        Log.i("Board.Board", "board state[0][0]: "+state[0][0]);
        lightedNum = (int) Math.pow(size,2);
        Log.i("Board.Board", "lightedNum: "+lightedNum);

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
        if(i > 0 && i < size + 1 && j > 0 && j < size + 1 ){
            state[i][j] = !state[i][j];
            if (state[i][j]){
                ++lightedNum;
            }else{
                --lightedNum;
            }
            notifyChange(i-1, j-1, state[i][j]); //in screen cord
        }
    }

    public void makeMove(int iScreen, int jScreen){
        int i = iScreen + 1;
        int j = jScreen + 1;

        UpdateElementInMatrix(i-1,j);
        UpdateElementInMatrix(i,j-1);
        UpdateElementInMatrix(i,j);
        UpdateElementInMatrix(i,j+1);
        UpdateElementInMatrix(i+1,j);
        Log.i("Board.makeMove", "lightedNum: "+lightedNum);
        notifyEnd();
    }
}
