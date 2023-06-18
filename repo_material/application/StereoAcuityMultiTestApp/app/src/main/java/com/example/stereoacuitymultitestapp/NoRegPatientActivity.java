package com.example.stereoacuitymultitestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stereoacuitymultitestapp.utils.DefaultValues;

public class NoRegPatientActivity extends AppCompatActivity {

    /** Thet 2 EditText*/
    EditText et_name, et_surname;

    /** Parameters of the user */
    String name, surname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_reg_patient);

        et_name = (EditText) findViewById(R.id.et_name);
        et_surname = (EditText) findViewById(R.id.et_surname);
    }

    /** Method of the button to enter the application */
    public void enterWithoutRegistration(View view) {
        // Get the parameters
        name = et_name.getText().toString();
        surname = et_surname.getText().toString();

        if(!(name.equals("") || surname.equals(""))){
            // Put the parameters in the Shared Preferences
            SharedPreferences SPPatient = getSharedPreferences(DefaultValues.SPPATIENT, MODE_PRIVATE);
            SharedPreferences.Editor e = SPPatient.edit();
            e.putString(DefaultValues.ACTUAL_PATIENT_NAME, name);
            e.putString(DefaultValues.ACTUAL_PATIENT_SURNAME, surname);
            e.putInt(DefaultValues.ACTUAL_PATIENT_ID, 0);
            e.apply();

            Intent intent = new Intent(NoRegPatientActivity.this, MainActivity.class);
            intent.putExtra("IsReg","false");
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Complete both fields", Toast.LENGTH_LONG).show();
        }
    }
}