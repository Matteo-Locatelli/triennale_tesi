package com.example.stereoacuitymultitestapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import p3d4amb.sat.lib.MonitorData;
import p3d4amb.sat.lib.Points;
import p3d4amb.sat.lib.SATTest;
import p3d4amb.sat.lib.SATTestConfiguration;
import p3d4amb.sat.lib.background.PointImage;
import p3d4amb.sat.lib.background.RandomDotImage;
import p3d4amb.sat.lib.background.StripesImage;
import p3d4amb.sat.lib.session.PestDepthCertifierNew;
import p3d4amb.sat.lib.shapes.ImageShape;
import p3d4amb.sat.lib.shapes.Shape;
import p3d4amb.sat.lib.shapes.ShapeSize;

import com.example.stereoacuitymultitestapp.utils.DefaultValues;
import com.example.stereoacuitymultitestapp.utils.CalcolatoreDisparities;
import com.example.stereoacuitymultitestapp.utils.SingleTest;
import com.example.stereoacuitymultitestapp.utils.SingleBitmapBuilder;
import com.example.stereoacuitymultitestapp.utils.BitmapParallelBuilder;
import com.example.stereoacuitymultitestapp.utils.BitmapBuilder;
import com.example.stereoacuitymultitestapp.utils.Session;
import com.example.stereoacuitymultitestapp.utils.AnaglyphSATest;
import com.example.stereoacuitymultitestapp.utils.SingleResult;

import org.json.JSONArray;

public class TestActivity extends AppCompatActivity implements Observer {

    @SuppressLint("StaticFieldLeak")
    static TextView textViewName, tv_status;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6;
    static SingleTest[] tests;
    static boolean [] checked;
    Button btnSubmit, btnSkip;

    /** For debug */
    // static TextView textDisparities;

    /** For the results */
    static int [] seen, requested;
    static SingleResult[] results;

    /** Part of the name of the patient */
    SharedPreferences SPPatient;
    String patientName, patientSurname;

    /** Parameters of the imageset */
    ImageShape.ImageSet imageSet;
    String imageSetName;
    String imageName;
    SharedPreferences SPSettings;
    public int RIDsImages[];
    public int positionImageTest;

    /** Constants loaded from the preferences or set to Default Values */
    public static int MAXDISPARITY;
    public static int MINDISPARITY;
    public static boolean SEVERITY;

    /** Display parameter */
    private int disWidthPix, disHeigthPix;
    MonitorData monitorData;
    double monitorSize10thInc;
    int monitorWidthMM;
    int monitorDistance;

    /** Test parameters */
    /** Enum size of the shape (MEDIUM, SMALL...) */
    ShapeSize shapeSize;
    /** Array di Shapes */
    public Shape[] shapes;
    /** Shape corrente */
    public Shape currentShape;
    /** lista di shapes prelevata dall'imageset */
    List<ImageShape> listShapes;
    /** SATTestConfiguration */
    SATTestConfiguration satTestConfiguration;
    /** The current SATTest */
    private SATTest satTest;
    /** The builder of the image */
    private BitmapBuilder[] builder;
    /** The random points generated */
    public PointImage[] ranPoints;
    /** The stripes generated */
    public StripesImage[] stripes;
    /** The points of the images */
    public Points[] points;

    /** The user is registered: true */
    String isReg = "false";

    /** Actual session (1 test = 3 different disparities) */
    static Session session;
    String usernameTest;
    static int countTest = 0;

    /** la WaitBitmap */
    Bitmap waitBitMap;

    /** Color to be used for the anaglyph glasses */
    private final int[] leftColor = {255, 0, 0};
    private final int[] rightColor = {0, 0, 255};

    /** Constants */
    private int colorshape = 100;
    private static final boolean USE_MULTIPLE_PORCESSES = false;
    private static final String TAG = TestActivity.class.getSimpleName();

    /** The demo mode: view images without bitmap */
    private boolean demo;
    int RIDChosenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent myIntent = getIntent(); // gets the previously created intent
        isReg = myIntent.getStringExtra("IsReg");

        /** Get the name of the patient */
        SPPatient = getSharedPreferences(DefaultValues.SPPATIENT, Context.MODE_PRIVATE);
        patientName = SPPatient.getString(DefaultValues.ACTUAL_PATIENT_NAME, "not found");
        patientSurname = SPPatient.getString(DefaultValues.ACTUAL_PATIENT_SURNAME, "not found");

        /** Get the settings from shared preferences  */
        SPSettings = getSharedPreferences(DefaultValues.SPSETTINGS, Context.MODE_PRIVATE);
        loadPreferences();

        session = new Session(usernameTest, MAXDISPARITY);

        tv_status = (TextView) findViewById(R.id.tv_status);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewName.setText(imageSetName + " : " + imageName);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        imageView6 = (ImageView) findViewById(R.id.imageView6);

        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox) findViewById(R.id.checkBox5);
        checkBox6 = (CheckBox) findViewById(R.id.checkBox6);

        /** Create 6 SingleTest */
        tests = new SingleTest[DefaultValues.NUMBER_IMAGES];
        tests[0] = new SingleTest(imageView1, checkBox1, session.dispForTest[0]);
        tests[1] = new SingleTest(imageView2, checkBox2, session.dispForTest[1]);
        tests[2] = new SingleTest(imageView3, checkBox3, session.dispForTest[2]);
        tests[3] = new SingleTest(imageView4, checkBox4, session.dispForTest[3]);
        tests[4] = new SingleTest(imageView5, checkBox5, session.dispForTest[4]);
        tests[5] = new SingleTest(imageView6, checkBox6, session.dispForTest[5]);

        // debug per capire le disparità e la differenza tra massimo e minimo
        // textDisparities = (TextView) findViewById(R.id.textDisparities);

        /** Create arrays for result */
        seen = new int[session.cal.absoluteMaxDisparity + 1];
        requested = new int[session.cal.absoluteMaxDisparity + 1];

        checked = new boolean[DefaultValues.NUMBER_IMAGES];

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSkip = (Button) findViewById(R.id.btnSkip);

        RIDsImages = new int[DefaultValues.CHOICES];

        // posiziono nel vettore gli RIDS tramite R.drawable a seconda dell'imageSet
        RIDsImages = DefaultValues.imageSetToRIDs(imageSet);

        // setBackgroundImages(RIDImageTest);

        /** measure display */
        DisplayMetrics dm = getResources().getDisplayMetrics();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        Point realSize = new Point();
        try {
            Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        disWidthPix = realSize.x;
        disHeigthPix = realSize.y;
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(disWidthPix / dm.xdpi, 2);
        double y = Math.pow(disHeigthPix / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);

        /** Set monitorData */
        monitorSize10thInc = screenInches * 10;
        monitorWidthMM = (int) (disWidthPix / dm.xdpi * 25.4);
        monitorDistance = SPSettings.getInt(DefaultValues.PREF_DISTANCE, DefaultValues.DEFAULT_DISTANCE);

        /** Save display measures into monitorData used */
        monitorData = new MonitorData(monitorSize10thInc, disWidthPix, monitorWidthMM, dm.heightPixels, monitorDistance);

        /** Preparare Shapes */
        shapeSize = DefaultValues.getSize(monitorSize10thInc);
        shapes = new Shape[DefaultValues.CHOICES];

        listShapes = ImageShape.getShapes(imageSet);
        for (int i = 0; i < DefaultValues.CHOICES; i++) {
            shapes[i] = listShapes.get(i);
        }

        currentShape = shapes[positionImageTest];

        // Build bitmap for wait message
        waitBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.wait_image);

        satTestConfiguration = new SATTestConfiguration(MAXDISPARITY, Arrays.asList(shapes), false, monitorData, shapeSize, PestDepthCertifierNew.class);
        satTest = new AnaglyphSATest(satTestConfiguration);

        // Register this
        satTest.addObserver((Observer) this);

        demo = true;

        demoMode();

    }


    /** Get preferences of the settings */
    private void loadPreferences() {
        imageSetName = SPSettings.getString(DefaultValues.CHOSEN_IMAGESET, "not found");
        imageSet = DefaultValues.stringToImageSet(imageSetName);
        imageName = SPSettings.getString(DefaultValues.CHOSEN_IMAGE, "BIRD");
        positionImageTest = SPSettings.getInt(DefaultValues.POSITION_IMAGE_FOR_QUIZ, 0);
        RIDChosenImage = SPSettings.getInt(DefaultValues.RID_CHOSEN_IMAGE, DefaultValues.DEFAULT_RID_IMAGE_TEST);
        usernameTest = SPSettings.getString(DefaultValues.USERNAME_TEST, patientName + " " + patientSurname);
        MAXDISPARITY = SPSettings.getInt(DefaultValues.PREF_MAXDISPARITY, DefaultValues.DEFAULT_MAXDISPARITY);
        MINDISPARITY = SPSettings.getInt(DefaultValues.PREF_MINDISPARITY, DefaultValues.DEFAULT_MINDISPARITY);
        SEVERITY = SPSettings.getBoolean(DefaultValues.SEVERITY, DefaultValues.DEFAULT_SEVERITY);
        leftColor[0] = SPSettings.getInt(DefaultValues.RED_L, DefaultValues.CURRENT_RED_L);
        leftColor[1] = SPSettings.getInt(DefaultValues.GREEN_L, DefaultValues.CURRENT_GREEN_L);
        leftColor[2] = SPSettings.getInt(DefaultValues.BLUE_L, DefaultValues.CURRENT_BLUE_L);
        rightColor[0] = SPSettings.getInt(DefaultValues.RED_R, DefaultValues.CURRENT_RED_R);
        rightColor[1] = SPSettings.getInt(DefaultValues.GREEN_R, DefaultValues.CURRENT_GREEN_R);
        rightColor[2] = SPSettings.getInt(DefaultValues.BLUE_R, DefaultValues.CURRENT_BLUE_R);
        savePreferences();
    }

    /** Save the preferences */
    private void savePreferences() {
        SharedPreferences.Editor e = SPSettings.edit();
        e.putInt(DefaultValues.PREF_MAXDISPARITY, MAXDISPARITY);
        e.putInt(DefaultValues.PREF_MINDISPARITY, MINDISPARITY);
        e.putString(DefaultValues.CHOSEN_IMAGESET, imageSet.name());
        e.putString(DefaultValues.CHOSEN_IMAGE, imageName);
        e.putInt(DefaultValues.RID_CHOSEN_IMAGE, RIDChosenImage);
        e.putBoolean(DefaultValues.SEVERITY, SEVERITY);
        e.apply();
    }

    /** Set 3 images out of 6 for demo mode */
    private void setBackgroundImages (int RID, int p1, int p2, int p3){
        tests[p1].imageView.setBackgroundResource(RID);
        tests[p2].imageView.setBackgroundResource(RID);
        tests[p3].imageView.setBackgroundResource(RID);
    }

    /** Demo mode */
    public void demoMode(){
        assert demo ;
        int pos1, pos2, pos3;
        do {
            pos1 = (int)(Math.random()*5);
            pos2 = (int)(Math.random()*5);
            pos3 = (int)(Math.random()*5);
        } while (pos1 == pos2 || pos2 == pos3 || pos1 == pos3);
        setBackgroundImages(RIDChosenImage, pos1, pos2, pos3);
    }

    /** Reset the images when press skip or submit in demo mode */
    public void resetDemoImages(){
        tests[0].imageView.setBackgroundResource(0);
        tests[1].imageView.setBackgroundResource(0);
        tests[2].imageView.setBackgroundResource(0);
        tests[3].imageView.setBackgroundResource(0);
        tests[4].imageView.setBackgroundResource(0);
        tests[5].imageView.setBackgroundResource(0);
    }

    /** Reset the checkbox to "unclicked" */
    public void resetChecks(){
        for(int i=0; i<DefaultValues.NUMBER_IMAGES; i++) {
            if(tests[i].checkBox.isChecked()) {
                tests[i].checkBox.setChecked(false);
            }
        }
    }

    /** Method to create the 6 bitmapbuilders */
    public void setBackgroundForTest(){
        builder = new BitmapBuilder[DefaultValues.NUMBER_IMAGES];
        points = new Points[DefaultValues.NUMBER_IMAGES];
        ranPoints = new PointImage[DefaultValues.NUMBER_IMAGES];
        stripes = new StripesImage[DefaultValues.NUMBER_IMAGES];
        float heigthReduction = 0.75f;   //per fare la bitmap più piccola
        int width, height;
        //satTest.setNextShape();   // fa sparire immagini in test successivi

        for(int i=0; i<DefaultValues.NUMBER_IMAGES; i++) {
            width= tests[i].imageView.getLayoutParams().width;       // 150dp
            height = tests[i].imageView.getLayoutParams().height;    // 150dp
            stripes[i] = new StripesImage(width, (int) (height * heigthReduction));
            ranPoints[i] = new RandomDotImage(width, (int) (height * heigthReduction));
            points[i] = SATTest.getPointFromShape(width,(int) (height * heigthReduction),width/2,(int) (height * heigthReduction / 2),session.dispForTest[i],currentShape,ranPoints[i]);
            tests[i].imageView.setImageBitmap(waitBitMap);
            if (USE_MULTIPLE_PORCESSES)
                builder[i] = new BitmapParallelBuilder(tests[i].imageView, points[i], width, (int) (height * heigthReduction), leftColor, rightColor, colorshape);    //fai costruire la bitmap
            else
                builder[i] = new SingleBitmapBuilder(tests[i].imageView, points[i], width, (int) (height * heigthReduction), leftColor, rightColor, colorshape);    //fai costruire la bitmap
            builder[i].execute();
        }

    }


    @SuppressLint("SetTextI18n")
    /** Method that creates a new array of disparities for the next iteration of the test
     *
     * @param max for the maximum disparity of that iteration
     * @param min for the minmimum disparity of the iteration*/
    public static void test(int max, int min) {
        countTest++;
        // calcola il vettore di disparità
        session.dispForTest = session.cal.getCurrentDisparities(max, min);
        for(int i=0; i<6; i++){
            tests[i].disparity = session.dispForTest[i];
            checked[i] = false;
        }
        // mostra i valori delle disparità che aumentano (numero volte visto e richiesto per ogni valore)
        updateRequested();
        // showSeenRequested();

        tv_status.setText("Severità: " + SEVERITY);
    }


    /** Increases the number of times a disparity is requested */
    public static void updateRequested() {
        // i è la posizione nel vettore (requested)
        // j è il contatore usato per scorrere nel vettore dispForTest
        for (int i=0; i<session.cal.absoluteMaxDisparity+1; i++) {
            for (int j=0; j<6; j++) {
                if (i == session.dispForTest[j]) {
                    requested[i]++;
                }
            }
        }
    }

    /** For debug: show the textDisparity that shows the test parameters ad current results
    public static void showSeenRequested(){
        textDisparities.setText("Test numero " + countTest);
        textDisparities.append("\n");
        textDisparities.append("Disparità massima inserita: " + MAXDISPARITY + "\n");
        textDisparities.append("Max cal " + session.cal.absoluteMaxDisparity + " Min cal " + session.cal.absoluteMinDisparity);
        textDisparities.append("\n");
        for (int k=0; k<6; k++){
            textDisparities.append(session.dispForTest[k] + " ");
        }
        textDisparities.append("\n");
        for (int i=0; i<session.cal.absoluteMaxDisparity+1; i++){
            textDisparities.append(seen[i] + " ");
        }
        textDisparities.append("\n");
        for (int j=0; j<session.cal.absoluteMaxDisparity+1; j++){
            textDisparities.append(requested[j] + " ");
        }
    } */

    /** Evaluetes which checkboxes are clicked */
    public static void evaluateChecked(){
        for(int i=0; i<6; i++){
            if (tests[i].isSelected()) {
                checked[i] = true;
            }
        }
    }

    /** Updates the number of times a disparity is seen */
    public static void updateSeen(){
        for(int i=0; i<6; i++){
            if (tests[i].isSelected()) {
                int pos = tests[i].disparity;
                seen[pos]++;
            }
        }
    }

    /** Method called when the button sumbit is pressed */
    public void OnClickSubmit(View view) {
        Log.d(TAG, "Pressed submit button");
        next();
    }

    /** Method called when the button skip is pressed */
    public void OnClickSkip(View view) {
        Log.d(TAG, "Pressed skip button");
        next();
    }

    /** Method called by buttons of Skip and Submit to progress in the test */
    public void next(){
        if(!demo) {
            if(SEVERITY) {  // test strict
                evaluateChecked();
                session.cal.setSolutions(checked);
                session.result = session.cal.certifierStatus.currentResult;
                session.solution = session.cal.solution;
                if (session.solution == CalcolatoreDisparities.Solution.RIGHT) {
                    updateSeen();
                }
                if (session.result == CalcolatoreDisparities.Result.CONTINUE) {
                    resetChecks();
                    test(session.cal.maxDisparity, session.cal.minDisparity);
                    setBackgroundForTest();
                } else {
                    finishTest();
                }
            } else {    // test not strict
                evaluateChecked();
                updateSeen();
                session.cal.setSolutions(checked);
                session.result = session.cal.certifierStatus.currentResult;
                if (session.result == CalcolatoreDisparities.Result.CONTINUE) {
                    resetChecks();
                    test(session.cal.maxDisparity, session.cal.minDisparity);
                    setBackgroundForTest();
                } else {
                    finishTest();
                }
            }
        } else {    // in demo mode
            resetChecks();
            resetDemoImages();
            demoMode();
        }
    }

    /** Method called when the button quit is pressed */
    public void OnClickQuit(View view) {
        Log.d(TAG, "Pressed quit button");
        if (isReg.equalsIgnoreCase("false")) {
            Intent intent= new Intent(TestActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }else {
            if (!demo) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TestActivity.this);
                alertDialog.setTitle("Saving results");
                alertDialog.setMessage("Do you want to save the results?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finishTest();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                goToMain();
                            }
                        });
                alertDialog.show();
            } else {
                goToMain();
            }}
    }

    /** Method called when the button start is pressed
     * From demo mode to test mode */
    public void OnClickStart(View view) {
        assert demo ;

        demo = false;
        resetDemoImages();

        test(session.cal.maxDisparity, session.cal.minDisparity);
        setBackgroundForTest();
        // Disable button, test started
        Button button = findViewById(R.id.btn_start_test);
        button.setEnabled(false);


        Log.d(TAG, "Starting actual test");
    }

    /** Method to finish the test */
    private void finishTest(){
        Log.d(TAG, "TestActivity finished");

        // Intent
        Intent i = new Intent(TestActivity.this, TestResultActivity.class);

        // Store the session data
        List<String> sessionResults = new ArrayList<>();
        sessionResults = getSessionResultsAL();

        // JSON Session data
        JSONArray jasonresults = new JSONArray();

        for (String s : sessionResults) {
            jasonresults.put(s);
        }

        float finalAngle = computeAngle();
        int maxDisparity = evaluateMaxDisparity();

        SharedPreferences.Editor e = SPSettings.edit();
        e.putFloat(DefaultValues.FINALANGLERESULT, finalAngle);
        e.putInt(DefaultValues.CERTIFIED_MAX_DISPARITY, maxDisparity);
        e.apply();

        i.putExtra(DefaultValues.SESSION_DATA, jasonresults.toString());
        i.putExtra("IsReg", isReg);

        finish();
        startActivity(i);

        this.finish();
    }

    /** Gets the results for each value of disparity */
    private List<String> getSessionResultsAL(){
        List<String> s = new ArrayList<>();
        results = new SingleResult[MAXDISPARITY+1];
        for (int i=0; i<MAXDISPARITY+1; i++) {
            results[i] = new SingleResult(i, seen[i], requested[i]);
            s.add(results[i].toString());
        }
        return s;
    }

    /** Compute the final angle */
    private float computeAngle(){
        int maxDisp = evaluateMaxDisparity();
        float depth = depthAngle(maxDisp);
        return depth;
    }

    /** Gets the maximum disparity certified */
    private int evaluateMaxDisparity(){
        int max = MAXDISPARITY;
        for (int i=0; i<MAXDISPARITY+1; i++){
            double percentage = ((double)seen[i])/((double)requested[i]);
            if (percentage >= DefaultValues.THRESHOLD){
                max = i;
                break;
            }
        }
        return max;
    }

    /** Compute final angle*/
    public float depthAngle(int pixeltransl) {
        // get the monitor size
        double ms = monitorSize10thInc / 10.0;
        if (ms == 0)
            return 0;
        float alphaSec = getAngleSec(pixeltransl, disWidthPix, monitorWidthMM, monitorDistance);
        return alphaSec;
    }

    /**
     * Gets the angle sec.
     *
     * @param pixeltransl
     *            number of pixels of translation
     * @param pixelWidth
     *            the pixel width of the monitor
     * @param monitorWidthMM
     *            monitor width in mm
     * @param monitorDistance
     *            in centimeters
     * @return the angle sec di grado
     */
    public static float getAngleSec(int pixeltransl, int pixelWidth, int monitorWidthMM, int monitorDistance) {
        // get number of pixels in diagonal
        // double npd =
        // Math.sqrt(monitorWidthMM.getHeight()*monitorWidthMM.getHeight() +
        // monitorWidthMM.getWidth()*monitorWidthMM.getWidth());
        // get the size of a pixel in mm = diagonal/ number of pixels
        // double ps = (ms * 25.4) / npd;
        // get the deltaLayers in mm
        double deltaMM = pixeltransl * monitorWidthMM / (double) pixelWidth;
        // distance is default 40 cm
        double distanceMM = monitorDistance * 10;
        // compute angle
        // atan2(y, x) is the angle in radians between the positive x-axis of a
        // plane and the point given by the coordinates (x, y) on it.
        double alpha = Math.atan2(deltaMM, distanceMM);
        // convert to degree gardes in seconds
        float alphaSec = ((float)Math.toDegrees(alpha) * 3600);
        return alphaSec;
    }

    /** Method to return to main */
    private void goToMain() {
        // Intent
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void update(Observable observable, Object o) {
        if (observable == satTest) {
            SATTest test = (SATTest) observable;

            // For now, only the finished event (Demo mode)
            if (!test.isIndemomode())
                test(MAXDISPARITY, MINDISPARITY);
        }
    }

}