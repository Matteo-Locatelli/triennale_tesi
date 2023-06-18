package com.example.stereoacuitymultitestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stereoacuitymultitestapp.utils.DefaultValues;

public class MainActivity extends AppCompatActivity {

    /** Shared Preferences */
    public SharedPreferences SPDoctor;
    public SharedPreferences SPPatient;

    /** Textview of username */
    TextView txt_doctor, txt_patient;

    /** Depends on the mode of login */
    String isReg = "true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_doctor = (TextView)findViewById(R.id.txt_doctor);
        txt_patient = (TextView)findViewById(R.id.txt_patient);

        // SharedPreferences
        /** SPDoctor */
        SPDoctor = getSharedPreferences(DefaultValues.SPDOCTOR, MODE_PRIVATE);
        String doctorMail = SPDoctor.getString(DefaultValues.ACTUAL_DOCTOR_MAIL, "404");
        String doctorName = SPDoctor.getString(DefaultValues.ACTUAL_DOCTOR_NAME, "404");
        String doctorSurname = SPDoctor.getString(DefaultValues.ACTUAL_DOCTOR_SURNAME, "404");

        /** SPPatient */
        SPPatient = getSharedPreferences(DefaultValues.SPPATIENT, MODE_PRIVATE);
        String patientName = SPPatient.getString(DefaultValues.ACTUAL_PATIENT_NAME, "404");
        String patientSurname = SPPatient.getString(DefaultValues.ACTUAL_PATIENT_SURNAME, "404");
        int patientId = SPPatient.getInt(DefaultValues.ACTUAL_PATIENT_ID, -1);

        if (patientId == 0){
            isReg = "false";
            // do not allow to change patient
            Button patient_button = (Button) findViewById(R.id.patient_button);
            patient_button.setEnabled(false);
        }

        // TextView1
        txt_doctor.setText("DOCTOR: " + doctorName + " " + doctorSurname);

        // TextView2
        txt_patient.setText("PATIENT: " + patientName + " " + patientSurname + " [" + patientId + "]");
    }


    /** Open Login Activity */
    public void choosePatient(View view) {
        Intent intent = new Intent(this, PatientActivity.class);
        startActivity(intent);
    }

    /** Open Test Activity */
    public void startTest(View view) {
        SPPatient = getSharedPreferences(DefaultValues.SPPATIENT, MODE_PRIVATE);
        int patientId = SPPatient.getInt(DefaultValues.ACTUAL_PATIENT_ID, -1);

        if (patientId != -1) {
            Intent intent = new Intent(this, ChooseImageActivity.class);
            intent.putExtra("IsReg", isReg);
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, "Select patient before starting the test", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /** Open Settings Activity */
    public void openSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    /** Open Results Activity */
    public void logout(View view) {
        // Delete previous SharedPreferences (SPDoctor)
        SharedPreferences del1 = this.getSharedPreferences("SPDoctor", Context.MODE_PRIVATE);
        del1.edit().clear().apply();

        // Delete previous SharedPreferences (SPPatient)
        SharedPreferences del2 = this.getSharedPreferences("SPPatient", Context.MODE_PRIVATE);
        del2.edit().clear().apply();

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);

        // Toast
        Toast toast = Toast.makeText(MainActivity.this, "Logout succesfull", Toast.LENGTH_LONG);
        toast.show();

        finish();
    }

    /**
     * Method: onResume
     * Used to refresh an activity
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();

        // Refresh stuff here
        SPPatient = getSharedPreferences(DefaultValues.SPPATIENT, MODE_PRIVATE);
        String patientName = SPPatient.getString(DefaultValues.ACTUAL_PATIENT_NAME, "404");
        String patientSurname = SPPatient.getString(DefaultValues.ACTUAL_PATIENT_SURNAME, "404");
        int patientId = SPPatient.getInt(DefaultValues.ACTUAL_PATIENT_ID, -1);

        TextView textView2 = findViewById(R.id.txt_patient);
        textView2.setText("PATIENT: " + patientName + " " + patientSurname + " [" + patientId + "]");
    }

}