package com.example.lightout;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainMenuFragment extends Fragment implements View.OnClickListener {
    private FragmentListener listener; //MainActivity will listen to the click event of the "play" button

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
        return inflater.inflate(R.layout.main_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.btnPlay).setOnClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        listener.OnClickEvent(this);
    }



}