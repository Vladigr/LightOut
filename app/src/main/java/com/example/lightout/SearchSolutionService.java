package com.example.lightout;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.lightout.logic.Board;

import java.util.ArrayList;

public class SearchSolutionService extends Service {
    private Thread myThread;
    public static final String CHANNEL_ID="SearchSolutionService";
    public static final String IS_THREAD_KEY="isThread";
    public static final String IS_BOARD_SOLVED="isBOardSolved";
    public static final String SOLUTION_KEY="solutiongKey";
    public static final String MSG_KEY="msgKey";
    public static final String BOARD_KEY="boardKey";

    private Board mBoard=null;

    //will be called once
    @Override
    public void onCreate() {
        super.onCreate();
        //creating the notification chanel
        NotificationChannel serviceChanel = new NotificationChannel(
                SearchSolutionService.CHANNEL_ID,
                "Search Solution Serivce",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChanel);
        //adding this chanel in the manifests
    }

    //when the service is stopped
    @Override
    public void onDestroy() {
        super.onDestroy();
        myThread.stop();
    }

    //happens any time we start a service
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("elro","Starting Notification");
        //return super.onStartCommand(intent, flags, startId);

        //check if it is a thread that updates the notification
        boolean isThread=intent.getBooleanExtra("isThread",false);
        Intent notificationIntent = new Intent(this,GameActivity.class);
        Notification notification=null;


        //create a thread to run the functionality
        if(isThread==false )
        {

            if(myThread==null) {
                mBoard = (Board) intent.getSerializableExtra(BOARD_KEY);
                notification = new Notification.Builder(this,CHANNEL_ID)
                        .setContentTitle("Searching Solution")
                        .setContentText("Board size = "+mBoard.getSize()+"x"+mBoard.getSize())
                        .setSmallIcon(R.drawable.ic_baseline_search_24)
                        .build();
                myThread = new Thread(new Runnable() {
                    public void run() {
                        Log.i("elro", "Service Thread Running");

                        //adding the solve function here
                        ArrayList<Board.SolPoints> solPoints = mBoard.solve();
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Intent serviceIntent = new Intent(getApplicationContext(), SearchSolutionService.class);
                        serviceIntent.putExtra(SearchSolutionService.IS_THREAD_KEY, true);
                        serviceIntent.putExtra(SearchSolutionService.IS_BOARD_SOLVED, true);
                        //serviceIntent.putExtra(SearchSolutionService.SOLUTION_KEY, solPoints.toArray());
                        serviceIntent.putExtra(MSG_KEY,"I found the solution");
                        startService(serviceIntent);
                    }
                });
                myThread.start();
            }
        }
        else
        {
            myThread=null;

            //add extars for the solution

            notificationIntent.putExtra(IS_BOARD_SOLVED,intent.getBooleanExtra(IS_BOARD_SOLVED,false));
            notificationIntent.putExtra(GameActivity.SOLUTION_TYPE,true);
            notificationIntent.putExtra(GameActivity.MAIN_TYPE,false);
            //adding the board solution
            notificationIntent.putExtra(MSG_KEY,intent.getStringExtra(MSG_KEY));


            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            notification = new Notification.Builder(this,CHANNEL_ID)
                    .setContentTitle("Board Solved!")
                    .setSmallIcon(R.drawable.ic_baseline_check_24)
                    .setContentIntent(pendingIntent)
                    .build();

            //Toast.makeText(getApplicationContext(),"sending to back",Toast.LENGTH_LONG).show();

        }
        startForeground(1,notification);
        return START_NOT_STICKY;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
