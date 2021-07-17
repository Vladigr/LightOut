package com.example.lightout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lightout.data.CareTakerSave;
import com.example.lightout.data.SavedGame;
import com.example.lightout.logic.Board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements TimerBroadcastReceiver.ListenForTimer ,BoardFragment.BoardListener,GameInterface{

    public interface StarGame{ //interface for starting the game via the main activity
        public void startGame(SavedGame sg);
    }
    //our broadcastRecevier
    private  TimerBroadcastReceiver myTimeReceive =null;
    private Button btn;
    //the original board
    private Board originalBoard;

    //handler for counting down a second
    final Handler handler = new Handler();
    //a timer for the cound down
    private Timer timer;
    private Board board;
    boolean timerStatus=false;
    boolean randomStatus=false;
    long secondsLeft=0;


    private TextView txtTimeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        Log.i("elro","Game Create");
        txtTimeLeft=(TextView) findViewById(R.id.txtTimeLeft);

        timerStatus = (boolean) getIntent().getSerializableExtra(MainActivity.timerKey);
        randomStatus = (boolean) getIntent().getSerializableExtra(MainActivity.randomKey);

        if(timerStatus==false)
        {
            txtTimeLeft.setText("--:--");
        }
        else{
            startTimer(secondsLeft=(long) getIntent().getSerializableExtra(MainActivity.secondsKey));
        }

        board = (Board) getIntent().getSerializableExtra(BoardFragment.boardBundleKey);
        originalBoard= new Board(board);
        Log.i("lightout-GameActivity", "board size: " + board.getSize());


        Fragment frag = BoardFragment.newInstance(board);

        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.fragment_container_game_board, frag);
        tran.commit();


         btn=(Button)findViewById(R.id.btnStart);
         btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startMySerivice();
             }
         });
        //ewfwf
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);

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
        Log.i("elro","GameActivity::onDestroy()");

        long time =0;
        if(myTimeReceive!=null) {
            time =  myTimeReceive.getSeconds();

            //if there is a broadcastRecevier unregister it
            unregisterReceiver(myTimeReceive);
        }
        if(timer!=null) {
            timer.cancel();
        }

        CareTakerSave ct = new CareTakerSave();
        SavedGame sg = new SavedGame(board, time);
        //Todo: change to general case
        try {
            int num = this.getFilesDir().listFiles().length;
            ct.SaveData(this, sg, Integer.toString(num)+".dat");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        startMySerivice();

    }
    @Override
    public void boardFragOnDestroy(Board board) {
        CareTakerSave ct = new CareTakerSave();
        //Log.i("elro","GameActictivity.onDestroy");
        //Log.i("GameActictivity.onDestroy", String.valueOf(getFilesDir()));
        if(myTimeReceive!=null)
        {
            SavedGame sg = new SavedGame(board, myTimeReceive.getSeconds());
            //Todo: change to general case
            try {
                int num = this.getFilesDir().listFiles().length;
                ct.SaveData(this, sg, Integer.toString(num)+".dat");
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

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

    private void startMySerivice(){
        Log.i("elro","GameActivity::startService()");
        Intent serviceIntent= new Intent(this,SearchSolutionService.class);
        serviceIntent.putExtra(SearchSolutionService.IS_THREAD_KEY,false);
        serviceIntent.putExtra(SearchSolutionService.BOARD_KEY,board);
        startService(serviceIntent);
    }
    private void startTimer(long seconds){
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
        timer = new Timer(false);
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);

        //creating a timer reciver for 90 seconds
        //myTimeReceive =new TimerBroadcastReceiver(seconds,this);
        myTimeReceive =new TimerBroadcastReceiver(5,this);
        //adding the filter action for the reciver
        IntentFilter filter = new IntentFilter("com.example.lightout.TICK");
        registerReceiver(myTimeReceive,filter);
        myTimeReceive.setResume();

    }


    @Override
    public void timerEnded() {
        //do stuff here
        Toast.makeText(this, "lost", Toast.LENGTH_SHORT).show();
        timer.cancel();
        FragmentManager fm = getSupportFragmentManager();
        WinLoseDialog alertDialog = WinLoseDialog.newInstance("Defeat");
        alertDialog.connectGameInterface(this);
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public void getTime(String time) {

        this.txtTimeLeft.setText(time);
    }

    @Override
    public void won() {
        Log.i("GameActivity.won", "won ran");
        Toast.makeText(this, "won", Toast.LENGTH_SHORT).show();
        if(timer!=null)
            timer.cancel();
        FragmentManager fm = getSupportFragmentManager();
        WinLoseDialog alertDialog = WinLoseDialog.newInstance("You Are Victorious");
        alertDialog.connectGameInterface(this);
        alertDialog.show(fm, "fragment_alert");
    }



    @Override
    public void restartGame() {
        Log.i("elro","game restarts");

        Board newBoard = new Board(originalBoard);
        Log.i("lightout-GameActivity", "board size: " + newBoard.getSize());
        if(timerStatus==false)
        {
            txtTimeLeft.setText("--:--");
        }
        else{
            if(myTimeReceive !=null) //if there is a broadcast receiver than cancel the timer
            {
                timer.cancel(); //canceling the timer thread
                unregisterReceiver(myTimeReceive); //removing the current listener of the thread in order to register a new one
                startTimer(secondsLeft=(long) getIntent().getSerializableExtra(MainActivity.secondsKey));
            }
        }
        //creating the new fragment based on the original board that started the game
        Fragment frag = BoardFragment.newInstance(newBoard);
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.fragment_container_game_board, frag);
        tran.commit();
    }

    @Override
    public void endGame() {
        Log.i("elro","game ended");
        this.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}