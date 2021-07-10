package com.example.lightout;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ChooseGameAdapter extends RecyclerView.Adapter<ChooseGameAdapter.ViewHolder>{
    private String[] stubArray;

    public ChooseGameAdapter( String[] stubArray) {
        this.stubArray=  stubArray;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View gameView = inflater.inflate(R.layout.game_row, parent, false);

        // Return a new holder instance
        return new ViewHolder(gameView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // not for binding data, data will be binded in ViewHolder.bindData()
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return stubArray.length;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView txtIndexRow;
        private TextView txtDataOfGame;
        private View itemView;
        RecyclerView.Adapter<ChooseGameAdapter.ViewHolder> adapter;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView , RecyclerView.Adapter<ChooseGameAdapter.ViewHolder> adapter) {
            super(itemView);
            this.itemView = itemView;
            this.adapter = adapter;
            txtIndexRow = itemView.findViewById(R.id.txtIndexRow);
            txtDataOfGame = itemView.findViewById(R.id.txtDataOfGame);

        }

        public void bindData(int position){
            txtIndexRow.setText(""+position);
            txtDataOfGame.setText("Info: "+stubArray[position]);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("elro",stubArray[position]);
                }
            });

        }
    }



}
