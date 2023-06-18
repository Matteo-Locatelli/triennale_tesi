package com.example.def;

public class Session {

    // nome della persona che esegue il test
    String username;

    // session variables
    int maxDisparity;
    int minDisparity;

    public int [] dispForTest;

    // test
    public CalcolatoreDisparities cal;
    public CalcolatoreDisparities.Result result;

    public Session (String username){
        this.username = username;
        this.maxDisparity = DefaultValues.DEFAULT_MAXDISPARITY;
        this.minDisparity = DefaultValues.DEFAULT_MINDISPARITY;
        cal = new CalcolatoreDisparities();
        result = CalcolatoreDisparities.Result.CONTINUE;
        this.dispForTest = new int[DefaultValues.NUMBER_IMAGES];
    }

}
