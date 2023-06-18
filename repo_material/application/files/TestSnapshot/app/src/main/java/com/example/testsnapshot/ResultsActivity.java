package com.example.testsnapshot;

import androidx.appcompat.app.AppCompatActivity;

import utils.CalcolatoreDisparities;
import utils.singleResult;

import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity {

    TextView tvResults;

    Map<Integer, singleResult> results;

    singleResult [] res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        tvResults = (TextView) findViewById(R.id.textResults);

        results = new HashMap<>();

        res = new singleResult[CalcolatoreDisparities.absoluteMaxDisparity+1];

        tvResults.setText("\n");
        for (int i = 0; i< CalcolatoreDisparities.absoluteMaxDisparity+1; i++) {
            res[i] = new singleResult(i, ImagesDisplayActivity.seen[i], ImagesDisplayActivity.requested[i]);
            results.put(i, res[i]);
            tvResults.append(res[i].toString() + "\n");
        }

    }
}