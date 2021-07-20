package com.example.lightout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lightout.data.SavedGame;
import com.example.lightout.logic.Board;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentListener, GameActivity.StarGame {
    private ConstraintLayout fragContainer;
    public static final String timerKey="TimerStatusKey";
    public static final String secondsKey="secondsKey";
    public static final String randomKey="RandomStatusKey";
    public static final String fileNameKey="fileNameKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragContainer = (ConstraintLayout) findViewById(R.id.fragContainer);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragContainer, MainMenuFragment.class, null, "MainMenuFragment")
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void OnClickEvent(Fragment fragment) {
        if(fragment==null) //if null found, do nothing
            return;

        //changing to the next fragment
        if(fragment instanceof MainMenuFragment){ // if the clicked happed in the MainMEnuFragment ("play" button clicked)
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragContainer, ChoosingGameFragment.class, null,"ChoosingGameFragment")
                    .addToBackStack("ChoosingGameFragment")
                    .commit();

        }
        else if(fragment instanceof ChoosingGameFragment)
        {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragContainer, NewGameSetup.class, null,"NewGameSetup")
                    .addToBackStack("NewGameSetup")
                    .commit();
            ///
        }
    }

    private void startGameDemo(){
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra(BoardFragment.boardBundleKey, new Board(4));
        startActivity(intent);
    }


    @Override
    public void startGame(SavedGame sg) {
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra(GameActivity.SOLUTION_TYPE,false);
        intent.putExtra(GameActivity.MAIN_TYPE,true);
        intent.putExtra(BoardFragment.boardBundleKey,sg.getBoard());
        intent.putExtra(MainActivity.timerKey,sg.isTimed());
        intent.putExtra(MainActivity.randomKey,sg.isRandom());
        intent.putExtra(MainActivity.secondsKey,sg.getSecond());
        intent.putExtra(MainActivity.fileNameKey,sg.getFileName());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnExitFromApp:
                finish();
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}