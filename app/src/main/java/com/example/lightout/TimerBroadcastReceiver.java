package com.example.lightout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class TimerBroadcastReceiver extends BroadcastReceiver {
    private long timeLeftInSeconds;
    private boolean pause;
    private ListenForTimer listener;
    TimerBroadcastReceiver(long timeLeftInSeconds,ListenForTimer listener)
    {
        this.timeLeftInSeconds =timeLeftInSeconds;
        this.listener=listener;
        pause=false;
    }
    public void setResume(){
        pause=false;
    }
    public void setPause(){
        pause=true;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().compareTo("com.example.lightout.TICK")==0){
            if(pause==false)
            {
                //Toast.makeText(context,"tick",Toast.LENGTH_SHORT).show();
                if(timeLeftInSeconds !=-1)
                {
                    Log.i("elro","Got the tick, "+ timeLeftInSeconds);
                    getTimeLeft();
                    timeLeftInSeconds--;
                }

            }
            else
                Toast.makeText(context,"paused",Toast.LENGTH_SHORT).show();
        }

    }
    //TODO: was private change to public ask Elroyee
    public long getSeconds(){
        return this.timeLeftInSeconds;
    }

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

    public interface ListenForTimer{
        public void timerEnded();
        public void getTime(String time);
    }




}
