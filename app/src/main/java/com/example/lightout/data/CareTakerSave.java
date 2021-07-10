package com.example.lightout.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CareTakerSave {

    public SavedGame getSave(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
        SavedGame sg = (SavedGame) in.readObject();
        in.close();
        return sg;
    }

    public void SaveData(SavedGame sg, String fileName) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
        out.writeObject(sg);
        out.flush();
        out.close();
    }
}
