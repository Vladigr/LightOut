package com.example.lightout.boundary;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

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
        int pxSize = (int) dpToPixel(50);
        Log.i("tableButton", "pxsize: " + pxSize);
        this.setLayoutParams(new TableRow.LayoutParams(pxSize,pxSize));
        this.i =i;
        this.j =j;
        this.state = state;
    }

    float dpToPixel(float dp){
        // Converts 14 dip into its equivalent px
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }
}
