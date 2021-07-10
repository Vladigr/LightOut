package com.example.lightout.data;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CareTakerSave {

    public SavedGame getSave(Context context, String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream is = new ObjectInputStream(fis);
        SavedGame sg = (SavedGame) is.readObject();
        is.close();
        fis.close();
        return sg;
    }

    public void SaveData(Context context, SavedGame sg, String fileName) throws IOException {
        File file = new File(context.getFilesDir(),fileName);
        Log.i("CareTaker.SaveData",context.getFilesDir().toString());
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(sg);
        out.flush();
        out.close();
    }
}
