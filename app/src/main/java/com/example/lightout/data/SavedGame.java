package com.example.lightout.data;

import com.example.lightout.logic.Board;

import java.io.Serializable;

public class SavedGame implements Serializable {


    private Board board;



    private long Second;
    private boolean isTimed, isRandom;
    private String fileName;

    public SavedGame(Board board, boolean isTimed, boolean isRandom, long Second, String fileName) {
        this.board = board;
        if(Second<5)
            this.Second=5;
        else
            this.Second = Second;
        this.isTimed = isTimed;
        this.isRandom = isRandom;
        this.fileName = fileName;
    }


    public Board getBoard() {
        return board;
    }

    public boolean isTimed() {
        return isTimed;
    }

    public boolean isRandom() {
        return isRandom;
    }
    public long getSecond() {
        return Second;
    }
    public String getFileName() {
        return fileName;
    }
}
