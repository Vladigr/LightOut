package com.example.lightout.data;

import com.example.lightout.logic.Board;

import java.io.Serializable;

//SavedGane : class that represents the raw file for us
//the SavedGame is saved as a class in a raw file and when we read form the file
//we get a SavedGame
public class SavedGame implements Serializable {


    private Board board;



    private long Second;
    private boolean isTimed, isRandom;
    private String fileName;

    public SavedGame(Board board, boolean isTimed, boolean isRandom, long Second, String fileName) {
        this.board = board;
        this.isTimed = isTimed;
        this.isRandom = isRandom;
        this.fileName = fileName;
        this.Second=Second;
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
