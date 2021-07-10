package com.example.lightout.data;

import com.example.lightout.logic.Board;

import java.io.Serializable;

public class SavedGame implements Serializable {
    private Board board;
    private long Second;

    public SavedGame(Board board, long Second) {
        this.board = board;
        this.Second = Second;
    }
}
