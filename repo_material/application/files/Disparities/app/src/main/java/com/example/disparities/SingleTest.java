package com.example.disparities;

import android.widget.CheckBox;
import android.widget.TextView;

public class SingleTest {

    // variabili della classe
    TextView textView;
    CheckBox checkBox;
    int disparity;

    public SingleTest (TextView textView, CheckBox checkBox, int disparity){
        this.textView = textView;
        this.checkBox = checkBox;
        this.disparity = disparity;
    }


    /** metodo per capire se la checkbox è stata selezionata */
    public boolean isSelected(){
        return checkBox.isChecked();
    }

    public void removeCheck() {

    }
}
