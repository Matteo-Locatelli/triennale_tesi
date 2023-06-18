package com.example.disparities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StartActivity extends AppCompatActivity {

    EditText et;
    Button btn;
    int maxDisp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        et = (EditText) findViewById(R.id.editText);
        btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxDisp = Integer.parseInt( et.getText().toString() );
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("MAX", maxDisp);
                startActivity(intent);
            }
        });

    }
}