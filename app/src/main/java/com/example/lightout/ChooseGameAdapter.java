package com.example.lightout;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lightout.data.CareTakerSave;
import com.example.lightout.data.SavedGame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class ChooseGameAdapter extends RecyclerView.Adapter<ChooseGameAdapter.ViewHolder>{
    private String[] fileName;
    private GameActivity.StarGame listener;

    public ChooseGameAdapter(Context context) {
        updateFileNameArr(context);
        try{
            this.listener = (GameActivity.StarGame)context;
        }catch(ClassCastException e){
            throw new ClassCastException("the class " +
                    context.getClass().getName() +
                    " must implements the interface 'FragAListener'");
        }
    }

    public void updateFileNameArr(Context context){
        ArrayList<String> fileNameList = new ArrayList<>();
        File[] fileList = context.getFilesDir().listFiles();
        for( File file : fileList){
            fileNameList.add(file.getName());
        }
        fileName = new String[fileNameList.size()];
        this.fileName = fileNameList.toArray(fileName);
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
        return fileName.length;
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
            txtDataOfGame.setText("Info: "+ fileName[position]);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        SavedGame sg = (new CareTakerSave()).getSave(v.getContext(), fileName[position]);
                        listener.startGame(sg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                    Log.i("elro", fileName[position]);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                  new CareTakerSave().deleteSave(v.getContext(), fileName[position]);
                  updateFileNameArr(v.getContext());
                  return true;
                }
            });

        }
    }



}
