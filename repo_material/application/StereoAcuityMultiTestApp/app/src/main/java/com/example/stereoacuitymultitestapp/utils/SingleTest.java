package com.example.stereoacuitymultitestapp.utils;

import android.widget.CheckBox;
import android.widget.ImageView;

public class SingleTest {

    /** Parameters to biuld a SingleTest */
    public ImageView imageView;
    public CheckBox checkBox;
    public int disparity;

    public SingleTest (ImageView imageView, CheckBox checkBox, int disparity){
        this.imageView = imageView;
        this.checkBox = checkBox;
        this.disparity = disparity;
    }


    /** true if checkbox i selected */
    public boolean isSelected(){
        return checkBox.isChecked();
    }

}
