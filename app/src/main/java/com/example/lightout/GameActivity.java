package com.example.lightout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationManager;
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
    public static final String MAIN_TYPE="MainActivity";
    public static final String SOLUTION_TYPE="solutionFound";
    //our broadcastRecevier
    private  TimerBroadcastReceiver myTimeReceive =null;
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
    long secondsLeftAfterPause=-1;
    private String fileName;
    private boolean flagRestart=false;
    private TextView txtTimeLeft;
    private   boolean isMainActivity;
    private boolean isSolution;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        Log.i("gameActivity","Game Create");
        txtTimeLeft=(TextView) findViewById(R.id.txtTimeLeft);

        isMainActivity = getIntent().getBooleanExtra(MAIN_TYPE,false);
        isSolution = getIntent().getBooleanExtra(SOLUTION_TYPE,false);

        if((isMainActivity==true) && (isSolution==false))
        {
            Toast.makeText(this,"is Main",Toast.LENGTH_LONG);
            //get check parameters
            timerStatus = (boolean) getIntent().getSerializableExtra(MainActivity.timerKey);
            randomStatus = (boolean) getIntent().getSerializableExtra(MainActivity.randomKey);

            if(timerStatus==false)
            {
                txtTimeLeft.setText("--:--");
            }
            else{
                secondsLeft=(long) getIntent().getSerializableExtra(MainActivity.secondsKey);
                startTimer(secondsLeft);
            }


             fileName = (String) getIntent().getSerializableExtra(MainActivity.fileNameKey);


            //kfilut
             board = (Board) getIntent().getSerializableExtra(BoardFragment.boardBundleKey);
             originalBoard= new Board(board);
             Log.i("lightout-GameActivity", "board size: " + board.getSize());
             board = (Board) getIntent().getSerializableExtra(BoardFragment.boardBundleKey);
             originalBoard= new Board(board);
             Log.i("lightout-GameActivity", "board size: " + board.getSize());
             Fragment frag = BoardFragment.newInstance(board);
             FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
             tran.replace(R.id.fragment_container_game_board, frag);
             tran.commit();
        }

        //if the service called the GameActivity
        if((isMainActivity==false) && (isSolution==true))
        {
            txtTimeLeft.setText("--:--");
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(this.NOTIFICATION_SERVICE);
            notificationManager.deleteNotificationChannel(SearchSolutionService.CHANNEL_ID);

            Log.i("newElro","going for solution, solution"+getIntent().getStringExtra(SearchSolutionService.MSG_KEY));
            //get solved board
            Toast.makeText(this, "press the green button from bottom right to bottom left and to the top",Toast.LENGTH_LONG).show();
            //String result=getIntent().getStringExtra(SearchSolutionService.MSG_KEY);
            //Toast.makeText(this, "result = "+result,Toast.LENGTH_LONG).show();
            ArrayList<Board.SolPoints> solPoints = ( ArrayList<Board.SolPoints>) getIntent().getSerializableExtra(SearchSolutionService.SOLUTION_KEY);
            board = (Board) getIntent().getSerializableExtra(SearchSolutionService.BOARD_KEY);
            originalBoard= new Board(board);
            Fragment frag = BoardFragment.newInstance(board);
            FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
            tran.replace(R.id.fragment_container_game_board, frag);
            //adding the runnable because comit doesnt not take place until the function is ended
            //therefore e need runner for initilizing our fragment and than update the board acoring to the solution
            tran.runOnCommit(new Runnable() {
                @Override
                public void run() {
                    ((BoardFragment) frag).drawSolution(solPoints);
                }
            }).commit();
        }
    }

    //build the action bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("gameActivity","Game Resume");
        //if there is a broadcast set it to resume
        if(myTimeReceive !=null && secondsLeftAfterPause!=-1)
        {
            startTimer(secondsLeftAfterPause);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
        Log.i("gameActivity","Game Pause");
        //if there is a broadcast pause the countdown
        if(myTimeReceive !=null && !isSolution)
        {
            unregisterReceiver(myTimeReceive);
        }
         */
    }

    @Override
    public void boardFragOnPause(Board board) {
        CareTakerSave ct = new CareTakerSave();
        long time = -1;
        Log.i("elro","GameActictivity.boardFragOnPause");
        //Log.i("GameActictivity.onDestroy", String.valueOf(getFilesDir()));

        if(flagRestart==false) {
            if (myTimeReceive != null) {
                time = myTimeReceive.getSeconds();
                secondsLeftAfterPause=time;
                //if there is a broadcastRecevier unregister it
                unregisterReceiver(myTimeReceive);
                if (timer != null) {
                    //stop the timer thread
                    timer.cancel();
                }
            }

            try {
                SavedGame sg = new SavedGame(this.board, timerStatus, randomStatus, time, fileName);
                if(!isSolution)
                     ct.SaveData(this, sg, fileName);
                Log.i("GameActictivity.onDestroy", "filename2: " + fileName);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        else
            flagRestart=false;

       if(board.checkWin() == true && fileName != null){
            ct.deleteSave(this, fileName);
        }
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
        //creating a timer reciver for 90 seconds
        //myTimeReceive =new TimerBroadcastReceiver(5,this);
        myTimeReceive =new TimerBroadcastReceiver(seconds,this);
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);

        //adding the filter action for the reciver
        IntentFilter filter = new IntentFilter("com.example.lightout.TICK");
        registerReceiver(myTimeReceive,filter);

    }


    @Override
    public void timerEnded() {
        //do stuff here
        Toast.makeText(this, "lost", Toast.LENGTH_SHORT).show();
        timer.cancel();
        FragmentManager fm = getSupportFragmentManager();
        WinLoseDialog alertDialog = WinLoseDialog.newInstance("Defeat");
        alertDialog.connectGameInterface(this);
        alertDialog.setCancelable(false);
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public void updateTimeOnDisplay(String time) {
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
        alertDialog.setCancelable(false);
        alertDialog.show(fm, "fragment_alert");
    }



    @Override
    public void restartGame() {
        Log.i("elro","game restarts");

        this.board = new Board(originalBoard);
        Log.i("lightout-GameActivity", "board size: " + this.board.getSize());
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
        flagRestart=true;
        //creating the new fragment based on the original board that started the game
        Fragment frag = BoardFragment.newInstance(this.board);
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(R.id.fragment_container_game_board, frag);
        tran.commit();
    }

    @Override
    public void endGame() {
        Log.i("elro","game ended");
        //Todo: check with elroee
        /*this.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);*/
        board.setWin();
        finish();
    }
    @Override
    public void endOnSolve()
    {
        Log.i("endOnSolve","GameActivity::endOnSolve");

        //there is no call for pause therefore end the broadcast and the receiver
        if (myTimeReceive != null) {
            //if there is a broadcastRecevier unregister it
            unregisterReceiver(myTimeReceive);
            if (timer != null) {
                //stop the timer thread
                timer.cancel();
            }
        }
        (new CareTakerSave()).deleteSave(this,fileName);
        finish();
    }

    @Override
    public boolean isMenuNeeded() {
        return isMainActivity;
    }

    public interface StarGame{ //interface for starting the game via the main activity
        public void GAStartGame(SavedGame sg);
    }

}