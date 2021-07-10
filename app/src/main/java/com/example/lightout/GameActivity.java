package com.example.lightout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.example.lightout.logic.Board;

import java.io.Serializable;

public class GameActivity extends AppCompatActivity {
    private TextView txtTimeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        Board board = (Board) getIntent().getSerializableExtra(BoardFragment.boardBundleKey);
        Log.i("lightout-GameActivity", "board size: " + board.getSize());

        Fragment frag = BoardFragment.newInstance(board);
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.fragment_container_game_board, frag);
        tran.addToBackStack(null);
        txtTimeLeft=(TextView) findViewById(R.id.txtTimeLeft);

        new CountDownTimer(65000, 1000) {
            public void onTick(long millisUntilFinished) {
                txtTimeLeft.setText(getTimeLeft(millisUntilFinished));
            }
            public void onFinish() {
                txtTimeLeft.setText("done!");
            }
        }.start();

        tran.commit();
    }

    private String getTimeLeft(long millisUntilFinished){
        long minutes=millisUntilFinished/60000;
        long seconds;
        String strSeconds;
        String strMinutes;
        if(minutes==0)
             seconds=millisUntilFinished/1000;
        else
            seconds=(millisUntilFinished-minutes*60000)/1000;

        return ""+minutes+" : "+seconds;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        //---save whatever you need to persist—
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        //---retrieve the information persisted earlier---
    }
}