package com.example.stereoacuitymultitestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.stereoacuitymultitestapp.utils.DefaultValues;

import java.util.ArrayList;

import p3d4amb.sat.lib.shapes.ImageShape;

public class ChooseImageActivity extends AppCompatActivity {

    /** 2 spinners */
    Spinner spinner_imageset, spinner_image;

    /** Objects of the activity*/
    Button btn_set, btn_change;
    ImageView imageView;

    /** List and adapetr for each spinner */
    ArrayList<String> arrayList_imageset;
    ArrayAdapter<String> arrayAdapter_imageset;

    ArrayList<String> arrayList_lang, arrayList_lea, arrayList_leacontour, arrayList_letters, arrayList_pacman, arrayList_tno;
    ArrayAdapter<String> arrayAdapter_images;

    /** Shared Preferences*/
    SharedPreferences sp_image;

    int RIDImage;

    /** Depends on the mode of the login */
    String isReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);

        Intent intent = getIntent();
        isReg = intent.getStringExtra("IsReg");

        spinner_imageset = (Spinner) findViewById(R.id.spinner_imageset);
        spinner_image = (Spinner) findViewById(R.id.spinner_image);
        btn_set = (Button) findViewById(R.id.btn_set);
        btn_change = (Button) findViewById(R.id.btn_change);
        imageView = (ImageView) findViewById(R.id.imageView);

        btn_set.setEnabled(false);

        /** Create firts spinner */
        arrayList_imageset = new ArrayList<>();
        arrayList_imageset.add("LANG");
        arrayList_imageset.add("LEA");
        arrayList_imageset.add("LEA_CONTORNO");
        arrayList_imageset.add("LETTERS");
        arrayList_imageset.add("PACMAN");
        arrayList_imageset.add("TNO");

        arrayAdapter_imageset = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_imageset);

        spinner_imageset.setAdapter(arrayAdapter_imageset);

        // spinner.getSelectedItem().toString(); per ottenere il testo da uno spinner

        /** Second spinner for images of an imageset */

        arrayList_lang = new ArrayList<>();
        arrayList_lang.add("BIRD");
        arrayList_lang.add("CAR");
        arrayList_lang.add("CAT");
        arrayList_lang.add("CIRCLE");
        // arrayList_lang.add("MAN");
        // arrayList_lang.add("STAR");

        arrayList_lea = new ArrayList<>();
        arrayList_lea.add("APPLE");
        arrayList_lea.add("CIRCLE");
        arrayList_lea.add("HOUSE");
        arrayList_lea.add("SQUARE");

        arrayList_leacontour = new ArrayList<>();
        arrayList_leacontour.add("APPLE CONTOUR");
        arrayList_leacontour.add("CIRCLE CONTOUR");
        arrayList_leacontour.add("HOUSE CONTOUR");
        arrayList_leacontour.add("SQUARE CONTOUR");

        arrayList_letters = new ArrayList<>();
        arrayList_letters.add("LETTER A");
        arrayList_letters.add("LETTER C");
        arrayList_letters.add("LETTER E");
        arrayList_letters.add("LETTER K");
        // arrayList_letters.add("LETTER M");
        // arrayList_letters.add("LETTER Z");

        arrayList_pacman = new ArrayList<>();
        arrayList_pacman.add("PACMAN DOWN");
        arrayList_pacman.add("PACMAN LEFT");
        arrayList_pacman.add("PACMAN RIGHT");
        arrayList_pacman.add("PACMAN UP");

        arrayList_tno = new ArrayList<>();
        arrayList_tno.add("CIRCLE");
        arrayList_tno.add("SQUARE");
        arrayList_tno.add("STAR");
        arrayList_tno.add("TRIANGLE");

        /** Relates imageset spinner to images spinner */
        spinner_imageset.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        arrayAdapter_images = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_lang);
                        break;
                    case 1:
                        arrayAdapter_images = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_lea);
                        break;
                    case 2:
                        arrayAdapter_images = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_leacontour);
                        break;
                    case 3:
                        arrayAdapter_images = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_letters);
                        break;
                    case 4:
                        arrayAdapter_images = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_pacman);
                        break;
                    case 5:
                        arrayAdapter_images = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_tno);
                        break;
                }
                spinner_image.setAdapter(arrayAdapter_images);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void change(View view) {

        /** Get the string of the imageSet and convert to ImageSet */
        String imageset_name = spinner_imageset.getSelectedItem().toString();
        ImageShape.ImageSet currImageSet = DefaultValues.stringToImageSet(imageset_name);

        int [] Rids =  DefaultValues.imageSetToRIDs(currImageSet);

        /** Now get the image name and set the new image */
        String image_name =spinner_image.getSelectedItem().toString();
        RIDImage = Rids[spinner_image.getSelectedItemPosition()];
        imageView.setImageResource(RIDImage);

        btn_set.setEnabled(true);

    }

    public void test(View view) {

        /** Load in the shared preferences the imageSetName and the ImageName and the position of the Image in an array */
        /** Takes the latest imageset and position of the drawable when CHANGE is pressed*/

        sp_image = getSharedPreferences(DefaultValues.SPSETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp_image.edit();

        String imageset_name = spinner_imageset.getSelectedItem().toString();
        String image_name = spinner_image.getSelectedItem().toString();

        // RIDImage = sp_image.getInt(DefaultValues.RID_CHOSEN_IMAGE, DefaultValues.DEFAULT_RID_IMAGE_TEST);

        editor.putString(DefaultValues.CHOSEN_IMAGESET, imageset_name);
        editor.putString(DefaultValues.CHOSEN_IMAGE, image_name);
        editor.putInt(DefaultValues.POSITION_IMAGE_FOR_QUIZ, spinner_image.getSelectedItemPosition());
        editor.putInt(DefaultValues.RID_CHOSEN_IMAGE, RIDImage);

        editor.apply();

        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra("IsReg", isReg);
        startActivity(intent);
    }

}