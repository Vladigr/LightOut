package com.example.lightout.data;

import com.example.lightout.logic.Board;

import java.io.Serializable;

public class SavedGame implements Serializable {


    private Board board;



    private long Second;
    boolean isTimed, isRandom;

    public SavedGame(Board board, long Second) {
        this.board = board;
        this.Second = Second;
    }
    public SavedGame(Board board, boolean isTimed, boolean isRandom, long Second) {
        this.board = board;
        this.Second = Second;
        this.isTimed = isTimed;
        this.isRandom = isRandom;
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
}
