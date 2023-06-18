package com.example.testsnapshot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import utils.DefaultValues;

public class MainActivity extends AppCompatActivity {

    Button btnStartTest;
    EditText editText;
    TextView tvName;
    String usernameTest;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartTest = (Button) findViewById(R.id.btnStartTest);
        editText = (EditText) findViewById(R.id.editText);
        tvName = (TextView) findViewById(R.id.tvName);

        sharedPref = getSharedPreferences(DefaultValues.SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        usernameTest = sharedPref.getString(DefaultValues.USERNAME_TEST, "username_test");
        tvName.setText("Benvenuto " + usernameTest);

        btnStartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //salvo il nome dell'imageset e lo carico nelle shared preferences
                String nameImageSet = editText.getText().toString();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(DefaultValues.CHOSEN_IMAGESET, nameImageSet);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, ImagesDisplayActivity.class);
                startActivity(intent);
            }
        });
    }
}