package com.example.lightout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lightout.logic.Board;

public class gameActivity extends AppCompatActivity {
    private Board board;
    public static final String boardExtraKey = "board_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        board = savedInstanceState.getSerializableExtra(boardExtraKey);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        //---save whatever you need to persist—
        outState("ID", "1234567890");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        //---retrieve the information persisted earlier---
        String ID = savedInstanceState.getString("ID");
    }
}