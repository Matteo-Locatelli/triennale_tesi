package com.example.stereoacuitymultitestapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


import com.example.stereoacuitymultitestapp.utils.DefaultValues;

public class TestResultActivity extends AppCompatActivity {

    /**
     * Variables
     */
    private int APID;

    TextView tv_actualuser, tv_sessioninfo;

    String isReg;

    /**
     * Method: isExternalStorageReadOnly
     *
     * @return boolean
     */
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    /**
     * Method: isExternalStorageAvailable
     *
     * @return boolean
     */
    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    /**
     * Method onCreate
     *
     * @param savedInstanceState
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        Intent intent = getIntent();
        isReg = intent.getStringExtra("IsReg");

        // SharedPreferences (SPPatient)
        SharedPreferences SPPatient = getSharedPreferences(DefaultValues.SPPATIENT, MODE_PRIVATE);
        String APName = SPPatient.getString(DefaultValues.ACTUAL_PATIENT_NAME, DefaultValues.ACTUAL_PATIENT_NAME);
        String APSurname = SPPatient.getString(DefaultValues.ACTUAL_PATIENT_SURNAME, DefaultValues.ACTUAL_PATIENT_SURNAME);
        APID = SPPatient.getInt(DefaultValues.ACTUAL_PATIENT_ID, -1);

        // SharedPreferences (SPSettings)
        SharedPreferences SPSettings = getSharedPreferences(DefaultValues.SPSETTINGS, MODE_PRIVATE);
        String distance = String.valueOf(SPSettings.getInt(DefaultValues.PREF_DISTANCE, DefaultValues.DEFAULT_DISTANCE));
        String imageSet = SPSettings.getString(DefaultValues.CHOSEN_IMAGESET, "not found");
        String image = SPSettings.getString(DefaultValues.CHOSEN_IMAGE, "not found");
        boolean sev = SPSettings.getBoolean(DefaultValues.SEVERITY, DefaultValues.DEFAULT_SEVERITY);
        float finalangleresult = SPSettings.getFloat(DefaultValues.FINALANGLERESULT, DefaultValues.DEFAULT_MAXDISPARITY);
        int certDisp = SPSettings.getInt(DefaultValues.CERTIFIED_MAX_DISPARITY, DefaultValues.DEFAULT_MAXDISPARITY);

        String typeTest;
        if(sev){
            typeTest = "Test severo";
        } else {
            typeTest = "Test permissivo";
        }

        tv_actualuser = (TextView) findViewById(R.id.tv_actualuser);
        tv_sessioninfo = (TextView) findViewById(R.id.tv_sessioninfo);

        tv_actualuser.setText(APName + " " + APSurname + " [" + APID + "]");

        SimpleDateFormat sdf = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        Date date = new Date();

        try {
            // JSON Session data
            JSONArray jsonArray = new JSONArray(getIntent().getStringExtra(DefaultValues.SESSION_DATA));
            System.out.println("Array: " + jsonArray.toString());

            // Get short version for list
            ArrayList<String> sessionsStringShort = new ArrayList<>();
            final ListView sessionsListView = findViewById(R.id.listview_sessions);

            tv_sessioninfo.setText(typeTest + ". " + imageSet + ": " + image + "\n"
                                    + "Final angle: " + ((int)finalangleresult) + "' (" + certDisp + ")" + "\n"
                                    + sdf.format(date));

            for (int i = 0; i < jsonArray.length(); i++) {
                String singleanswer = jsonArray.get(i).toString();
                sessionsStringShort.add(singleanswer);

                // Set ListView
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sessionsStringShort);
                sessionsListView.setAdapter(adapter);

                sessionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    /**
                     * Method: onItemClick
                     *
                     * @param adapter
                     * @param component
                     * @param pos
                     * @param id
                     */
                    @Override
                    public void onItemClick(AdapterView<?> adapter, final View component, int pos, long id) {
                        // Nothing
                    }
                });
            }

            // JSON Session data (Complete)
            JSONObject sessiondatacomplete = new JSONObject();
            sessiondatacomplete.put(DefaultValues.ACTUAL_PATIENT_NAME, APName);
            sessiondatacomplete.put(DefaultValues.ACTUAL_PATIENT_SURNAME, APSurname);
            sessiondatacomplete.put(DefaultValues.ACTUAL_PATIENT_ID, APID);
            sessiondatacomplete.put("IsReg", isReg);
            sessiondatacomplete.put(DefaultValues.DATEANDTIME_PARAM, sdf.format(date));
            sessiondatacomplete.put(DefaultValues.SEVERITY, typeTest);
            sessiondatacomplete.put(DefaultValues.CHOSEN_IMAGESET, imageSet);
            sessiondatacomplete.put(DefaultValues.CHOSEN_IMAGE, image);
            sessiondatacomplete.put(DefaultValues.SESSION_DATA, jsonArray);
            sessiondatacomplete.put(DefaultValues.FINALANGLERESULT, finalangleresult);
            sessiondatacomplete.put(DefaultValues.CERTIFIED_MAX_DISPARITY, certDisp);

            // Save session data (Complete) to file
            saveResultsOnInternalFile(sessiondatacomplete);

            // Buttons
            Button retry = findViewById(R.id.retry);
            Button backMenu = findViewById(R.id.back_menu);

            retry.setVisibility(View.INVISIBLE);
            retry.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                }
            });

            backMenu.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Intent
                    Intent intent = new Intent(TestResultActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Method: saveResultsOnInternalFile
     *
     * @param sessiondatacomplete
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveResultsOnInternalFile(@NonNull JSONObject sessiondatacomplete) {
        File myfile = null;

        if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            myfile = new File(getExternalFilesDir(DefaultValues.FOLDER_RESULTS), DefaultValues.FILE_RESULTS_NAME + "_" + APID + ".txt");
        }

        FileOutputStream outputStream;

        try {
            if (!Objects.requireNonNull(myfile).exists()) {
                myfile.createNewFile();
            }
            outputStream = new FileOutputStream(myfile, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(outputStream);
            myOutWriter.append(sessiondatacomplete.toString()).append("\n");
            myOutWriter.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}