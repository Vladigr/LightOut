package com.example.lightout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.lightout.data.SavedGame;
import com.example.lightout.logic.Board;

public class MainActivity extends AppCompatActivity implements FragmentListener, GameActivity.StarGame {



    private ConstraintLayout fragContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragContainer= (ConstraintLayout) findViewById(R.id.fragContainer);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragContainer, MainMenuFragment.class, null,"MainMenuFragment")
                .addToBackStack("MainMenuFragment")
                .commit();
        //startGameDemo();

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
        intent.putExtra(BoardFragment.boardBundleKey,sg.getBoard());
        startActivity(intent);
    }
}