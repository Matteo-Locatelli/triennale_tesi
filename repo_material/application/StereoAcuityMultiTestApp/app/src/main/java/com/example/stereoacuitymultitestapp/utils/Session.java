package com.example.stereoacuitymultitestapp.utils;

public class Session {

    /** Name of the person who will i doing the test*/
    String username;

    /** Session values of disparity */
    int maxDisparity;
    int minDisparity;

    /** The disparities for a single iteration of the test */
    public int [] dispForTest;

    /** Test parameters */
    public CalcolatoreDisparities cal;
    public CalcolatoreDisparities.Result result;
    public CalcolatoreDisparities.Solution solution;

    public Session (String username, int maxDisparity){
        this.username = username;
        this.maxDisparity = maxDisparity;
        this.minDisparity = DefaultValues.DEFAULT_MINDISPARITY;
        cal = new CalcolatoreDisparities(this.maxDisparity);
        result = CalcolatoreDisparities.Result.CONTINUE;
        solution = CalcolatoreDisparities.Solution.NULL;
        this.dispForTest = new int[DefaultValues.NUMBER_IMAGES];
    }

}
