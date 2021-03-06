package com.example.lightout.logic;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.pow;
import static java.lang.Math.random;

//Board : logic representation of the current board
public class Board extends Subject implements Serializable {
    private boolean state[][];  //pivoted matrix
    public boolean[][] getBoard(){return state;}
    public Boolean getElementInBoard(int i, int j){return state[i][j];}
    private int size;
    public int getSize(){
        return size;
    }
    private int lightedNum;

    public Board(int size){
        this.size = size;
        state = new boolean[size][size];
        lightAll(size);
    }

    private void lightAll(int size){
        for(int i=0; i < state.length; ++i){
            for(int j=0; j < state.length; ++j){
                state[i][j] = true;
            }
        }
        Log.i("Board.Board", "board state[0][0]: "+state[0][0]);
        lightedNum = (int) pow(size,2);
        Log.i("Board.Board", "lightedNum: "+lightedNum);
    }

    public Board(int size, boolean isRandom){
        this.size = size;
        state = new boolean[size][size];
        Observer temp = observer;
        observer = new NullObserver();
        if (isRandom == false){
            lightAll(size);
        }else {
            Random rand = new Random();
            for (int i = 0; i < state.length; ++i) {
                for (int j = 0; j < state[0].length; ++j) {
                    if(rand.nextBoolean()) makeMove(i,j);
                }
            }
            lightedNum = 0;
            for (int i = 0; i < state.length; ++i) {
                for (int j = 0; j < state[0].length; ++j) {
                    if (state[i][j]) ++lightedNum;
                }
            }
        }
        observer = temp;
    }

    public Board(boolean state[][]){
        this.state = state;
        size = state.length;
        lightedNum = 0;
        for(int i =0; i < size; ++i){
            for(int j =0; j < size; ++j){
                if(state[i][j] == true) ++lightedNum;
            }
        }
    }

    //duplicate the board
    public Board(Board newBoard){
        this.size = newBoard.size;
        state = new boolean[size][size];

        Log.i("Board.Board", "board state[0][0]: "+state[0][0]);
        lightedNum = (int) Math.pow(size,2);
        Log.i("Board.Board", "lightedNum: "+lightedNum);

        lightedNum =0;
        for(int i=0; i < state.length; ++i){
            for(int j=0; j < state.length; ++j){
                state[i][j] = newBoard.state[i][j];
                if(state[i][j] == true){
                    ++lightedNum;
                }
            }
        }

    }

    public boolean checkWin(){
        return lightedNum == 0;
    }

    public void setWin()
    {
        lightedNum=0;
    }

    private void UpdateElementInMatrix(int i, int j){
        if(i > -1 && i < size && j > -1 && j < size ){
            state[i][j] = !state[i][j];
            if (state[i][j]){
                ++lightedNum;
            }else{
                --lightedNum;
            }
            notifyChange(i, j, state[i][j]); //in screen cord
        }
    }

    public void makeMove(int i, int j){

        UpdateElementInMatrix(i-1,j);
        UpdateElementInMatrix(i,j-1);
        UpdateElementInMatrix(i,j);
        UpdateElementInMatrix(i,j+1);
        UpdateElementInMatrix(i+1,j);
        Log.i("Board.makeMove", "lightedNum: "+lightedNum);
        notifyEnd();
    }

    public class SolPoints implements Serializable {
        public int getiScreen() {
            return i;
        }

        private int i;
        private int j;

        public int getjScreen() {
            return j;
        }

        public SolPoints(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public String toString() {
            return '<' + i +
                    ", "+ j +
                    '>';
        }
    }

    //solve the board (wrapper for the recSolve)
    public ArrayList<SolPoints> solve(){
        Observer saveObserver = observer;
        observer = new NullObserver();
        ArrayList<SolPoints> res  = recSolve((int)pow(size,2)-1);
        observer = saveObserver;
        return res;
    }


    //recSolve: recursive function to search for solution
    private ArrayList<SolPoints> recSolve(int n){
        if (n==-1){
            if(checkWin() == true) return new ArrayList<>();
            return null;
        }

        int iScreen,jScreen;
        ArrayList<SolPoints> res;
        iScreen = n / size;
        jScreen = n % size;

        //test whiteout press
        res=  recSolve(n-1);
        if(res != null){
            // this element didn't pressed
            return res;
        }

        //test white press
        makeMove(iScreen,jScreen);
        res =  recSolve(n-1);
        if(res != null){
            res.add(new SolPoints(iScreen,jScreen));
        }
        //turn the board back
        makeMove(iScreen,jScreen);
        return res;
    }

}
