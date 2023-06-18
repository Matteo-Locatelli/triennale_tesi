package com.example.stereoacuitymultitestapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.stereoacuitymultitestapp.utils.DefaultValues;

public class ResultsActivity extends AppCompatActivity {

    private int APID;
    @Nullable
    private String doctorMail;
    @Nullable
    private String doctorPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // SharedPreferences (SPDoctor)
        SharedPreferences SPDoctor = getSharedPreferences(DefaultValues.SPDOCTOR, MODE_PRIVATE);
        doctorMail = SPDoctor.getString(DefaultValues.ACTUAL_DOCTOR_MAIL, "404");
        doctorPassword = SPDoctor.getString(DefaultValues.ACTUAL_DOCTOR_PASSWORD, "404");

        // SharedPreferences (SPPatient)
        SharedPreferences SPPatient = getSharedPreferences(DefaultValues.SPPATIENT, MODE_PRIVATE);
        APID = SPPatient.getInt(DefaultValues.ACTUAL_PATIENT_ID, -1);

    }

    public void returnMain(View view) {
        Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void deleteAllResults(View view) {

    }

}