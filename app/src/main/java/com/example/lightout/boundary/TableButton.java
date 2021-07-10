package com.example.lightout.boundary;

import android.content.Context;
import android.widget.Button;

import androidx.annotation.NonNull;

public class TableButton extends androidx.appcompat.widget.AppCompatButton {
    public int getI() {
        return i;
    }

    private int i;

    public int getJ() {
        return j;
    }

    private int j;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    boolean state;

    public TableButton(@NonNull Context context, int i, int j, boolean state) {
        super(context);
        this.i =i;
        this.j =j;
        this.state = state;
    }

}
