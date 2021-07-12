package com.example.lightout.data;

import com.example.lightout.logic.Board;

import java.io.Serializable;

public class SavedGame implements Serializable {
    public Board getBoard() {
        return board;
    }

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
}
