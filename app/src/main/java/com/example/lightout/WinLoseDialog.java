package com.example.lightout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

//dialog alert that will alert the user if he won or lost the game
//also enables it to chhose what to do in the game (reset or end)
public class WinLoseDialog extends DialogFragment {

    private GameInterface gameInterface;

    public static WinLoseDialog newInstance(String title){
        WinLoseDialog frag = new WinLoseDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void connectGameInterface(GameInterface gameInterface)
    {
        this.gameInterface=gameInterface;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        if(title.equals("You Are Victorious"))
        {
            alertDialogBuilder.setMessage("GG WP my friend");
            alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("elro","OK pressed");
                    gameInterface.endGame();
                }
            });
        }
        else
        {
            alertDialogBuilder.setMessage("Maybe next time :)");
            alertDialogBuilder.setPositiveButton("Retry?",  new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("elro","Retry pressed");
                    gameInterface.restartGame();
                }
            });
            alertDialogBuilder.setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialog != null) {
                        Log.i("elro","Finish pressed");
                        gameInterface.endGame();

                    }
                }

            });
        }
        return alertDialogBuilder.create();
    }
}
