package com.example.lightout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lightout.logic.Board;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        startGameDemo();

    }

    private void startGameDemo(){
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra(BoardFragment.boardBundleKey, new Board(3));
        startActivity(intent);
    }
}