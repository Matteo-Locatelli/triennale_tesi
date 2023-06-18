package com.example.def;

import android.widget.CheckBox;
import android.widget.ImageView;

public class SingleTest {

    // variabili della classe
    public ImageView imageView;
    public CheckBox checkBox;
    public int disparity;

    public SingleTest (ImageView imageView, CheckBox checkBox, int disparity){
        this.imageView = imageView;
        this.checkBox = checkBox;
        this.disparity = disparity;
    }


    /** metodo per capire se la checkbox è stata selezionata */
    public boolean isSelected(){
        return checkBox.isChecked();
    }

}
