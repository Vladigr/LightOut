package com.example.lightout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

//broadcastReceiver for the timer of the game, every tick will be received and the timer will go
//down by 1 for every second
public class TimerBroadcastReceiver extends BroadcastReceiver {
    //the seconds left for the end of the game
    private long timeLeftInSeconds;
    //a flag for detrming pausing the count or not
    private boolean pause;
    //the activity that will use this broadcastReceiver
    private ListenForTimer listener; // GameActivity

    TimerBroadcastReceiver(long timeLeftInSeconds,ListenForTimer listener)
    {
        this.timeLeftInSeconds =timeLeftInSeconds;
        this.listener=listener;
        pause=false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //check if the intnet is the action of tick every one second
        if(intent.getAction().compareTo("com.example.lightout.TICK")==0){//our custom broadcast action
            if(pause==false)
            {
                if(timeLeftInSeconds !=-1)
                {
                    Log.i("elro","Got the tick, "+ timeLeftInSeconds);
                    getTimeLeft();
                    timeLeftInSeconds--;
                }

            }
           // else
                //Toast.makeText(context,"paused",Toast.LENGTH_SHORT).show();
        }
    }
    public void setResume(){
        pause=false;
    }
    public void setPause(){
        pause=true;
    }
    //return how many seconds left ot the game
    public long getSeconds(){
        return this.timeLeftInSeconds;
    }
    //get how much time left
    private void getTimeLeft(){
        long minutes= timeLeftInSeconds /60;
        long seconds;
        if(minutes==0)
            seconds= timeLeftInSeconds;
        else
            seconds=(timeLeftInSeconds - minutes*60);
        listener.getTime(""+minutes+" : "+seconds);
        Log.i("elro",""+minutes+" : "+seconds);
        if(minutes==0 && seconds==0)
            listener.timerEnded();
    }
    //an interface that the activity must implement to use such broadcast
    public interface ListenForTimer{
        //when the time ended
        public void timerEnded();
        //get the current time that is left in: "minutes : seconds"
        public void getTime(String time);
    }




}
