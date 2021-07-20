package com.example.lightout;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


//choosing which game the player wants, saved or new
public class ChoosingGameFragment extends Fragment  {

    private FragmentListener listener; //MainActivity
    private ChooseGameAdapter myGameAdapter;


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
        Log.i("myDebug","ChoosingGameFragment::onCreate()");
        super.onCreate(savedInstanceState);
        myGameAdapter = new ChooseGameAdapter(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.choosing_game, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btnCreateNewGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.FLOnClickMoveToNextFragment(ChoosingGameFragment.this);
            }
        });

        RecyclerView rcvGameList = (RecyclerView) view.findViewById(R.id.rcvGameList);
        rcvGameList.setAdapter(myGameAdapter);
        rcvGameList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        Log.i("myDebug", "ChoosingGameFragment::onResume()");
        super.onResume();
        if (myGameAdapter != null) {
            myGameAdapter.updateFileNameArr(getContext());
            myGameAdapter.notifyDataSetChanged();
        }
    }



}