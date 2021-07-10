package com.example.lightout;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//setup the board of the game
public class NewGameSetup extends Fragment {
    private FragmentListener listener;
    @Override
    public void onAttach(@NonNull Context context) {
        try{
            this.listener = (FragmentListener)context;
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
        super.onViewCreated(view, savedInstanceState);
    }

}