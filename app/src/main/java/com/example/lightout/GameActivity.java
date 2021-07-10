package com.example.lightout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lightout.data.CareTakerSave;
import com.example.lightout.logic.Board;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements TimerBroadcastReceiver.ListenForTimer ,BoardFragment.BoardListener{
    //our broadcastRecevier
    private  TimerBroadcastReceiver myTimeReceive =null;
    //handler for counting down a second
    final Handler handler = new Handler();
    //a timer for the cound down
    Timer timer = new Timer(false);

    private TextView txtTimeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        Log.i("elro","Game Create");
        txtTimeLeft=(TextView) findViewById(R.id.txtTimeLeft);

        //creating a time that will tick every 1 second
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Log.i("elro","passed 5 sec");
                        Intent intent=new Intent("com.example.lightout.TICK");
                        sendBroadcast(intent);
                    }
                });
            }
        };
        // every 1 seconds.
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);

        //creating a timer reciver for 90 seconds
        myTimeReceive =new TimerBroadcastReceiver(90,this);


        //adding the filter action for the reciver
        IntentFilter filter = new IntentFilter("com.example.lightout.TICK");
        registerReceiver(myTimeReceive,filter);
        myTimeReceive.setResume();



        Board board = (Board) getIntent().getSerializableExtra(BoardFragment.boardBundleKey);
        Log.i("lightout-GameActivity", "board size: " + board.getSize());

        Fragment frag = BoardFragment.newInstance(board);
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.fragment_container_game_board, frag);
        tran.addToBackStack(null);
        tran.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("elro","Game Resume");
        //if there is a broadcast set it to resume
        if(myTimeReceive !=null)
        {
            myTimeReceive.setResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("elro","Game Pause");
        //if there is a broadcast pause the countdown
        if(myTimeReceive !=null)
        {
            myTimeReceive.setPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("elro","Game Destroy");
        //if there is a broadcastRecevier unregister it
        if(myTimeReceive !=null)
        {
            unregisterReceiver(myTimeReceive);
        }
        timer.cancel();

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


    @Override
    public void timerEnded() {
        //do stuff here
    }

    @Override
    public void getTime(String time) {
        this.txtTimeLeft.setText(time);
    }

    @Override
    public void won() {
        Log.i("GameActivity.won", "won ran");
        Toast.makeText(this, "won", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy(Board board) {
        CareTakerSave ct = new CareTakerSave();
        //ct.SaveData();
    }
}