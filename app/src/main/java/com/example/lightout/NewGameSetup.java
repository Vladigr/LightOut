package com.example.lightout;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.lightout.data.SavedGame;
import com.example.lightout.logic.Board;

//setup the board of the game
public class NewGameSetup extends Fragment {
    private GameActivity.StarGame listener;
    private Button btn3on3;
    private Button btn4on4;
    private Button btn5on5;
    private CheckBox cboxTimerMode;
    private CheckBox cboxRandomMode;


    @Override
    public void onAttach(@NonNull Context context) {
        try{
            this.listener = (GameActivity.StarGame)context;
        }catch(ClassCastException e){
            throw new ClassCastException("the class " +
                    context.getClass().getName() +
                    " must implements the interface 'FragAListener'");
        }
        super.onAttach(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.new_game_screen_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //view.findViewById(R.id.btnCreateNewGame).setOnClickListener(this);
        btn3on3= (Button) view.findViewById(R.id.btn3on3);
        btn4on4= (Button) view.findViewById(R.id.btn4on4);
        btn5on5= (Button) view.findViewById(R.id.btn5on5);
        cboxTimerMode=(CheckBox)view.findViewById(R.id.cboxTimerMode);
        cboxRandomMode=(CheckBox)view.findViewById(R.id.cboxRandomMode);
        // by cookbook:
        btn3on3.setOnClickListener(new Listener3on3());
        btn4on4.setOnClickListener(new Listener4on4());
        btn5on5.setOnClickListener(new Listener5on5());
        super.onViewCreated(view, savedInstanceState);
    }



    private void buttonClicked(int size){
        Toast.makeText(getActivity(),"The size is = "+size+" Random = "+cboxRandomMode.isChecked()+" Timer = "+cboxTimerMode.isChecked(),Toast.LENGTH_SHORT).show();
        //do things here
        Board board = new Board(size, cboxRandomMode.isChecked());
        SavedGame sg = new SavedGame(board,cboxTimerMode.isChecked(),cboxRandomMode.isChecked(),100, null);
        listener.startGame(sg);

    }


    //creating the classes of listeners for the buttons
    private class Listener3on3 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            buttonClicked(3);
        }
    }
    private class Listener4on4 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            buttonClicked(4);
        }
    }
    private class Listener5on5 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            buttonClicked(5);
        }
    }

}