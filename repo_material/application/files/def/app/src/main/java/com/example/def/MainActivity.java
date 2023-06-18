package com.example.def;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

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

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    /** Constants loaded from the preferences or set to Default Values */
    public static int MAXDISPARITY;
    public static int MINDISPARITY;
    public static int OFFSET;
    public static int NCORR_TO_NEXTLEVEL;
    public static int NERR_TO_STOPTEST;

    /** Color to be used for the anaglyph glasses */
    private final int[] leftColor = {255, 0, 0};
    private final int[] rightColor = {0, 0, 255};
    private int colorshape = 100;


    TextView textViewName;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6;
    static SingleTest[] tests;
    Button btnSubmit, btnSkip;

    /** la WaitBitmap */
    Bitmap waitBitMap;

    /**per debug */
    static TextView textDisparities;

    static boolean [] checked;

    /** parte per il risultato */
    static int [] seen, requested;

    /** parte relativa alla sessione attuale (1 test = più sessioni per trovare la disparità) */
    static Session session;
    String usernameTest = "MATTEO";
    static int countTest = 0;

    /** parte relativa all'imageset */
    ImageShape.ImageSet imageSet;   // -> LANG con il metodo stringToImageSet
    String imageSetName = "LANG";

    /** parametri display */
    private int disWidthPix, disHeigthPix;

    /** The builder of the image */
    private BitmapBuilder[] builder;
    /** The random points generated */
    public PointImage[] ranPoints;
    /** The stripes generated */
    public StripesImage[] stripes;
    /** The points of the images */
    public Points[] points;
    /** Bitmap Builder */
    private static final boolean USE_MULTIPLE_PORCESSES = false;

    /** parte relativa al test */
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Carico imageSet e imageSetName dalle shared preferences */
        sharedPreferences = getSharedPreferences(DefaultValues.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        loadPreferences();

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewName.setText("Nome dell'imageset: " + imageSetName);

        session = new Session(usernameTest);

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

        tests = new SingleTest[DefaultValues.NUMBER_IMAGES];
        tests[0] = new SingleTest(imageView1, checkBox1, session.dispForTest[0]);
        tests[1] = new SingleTest(imageView2, checkBox2, session.dispForTest[1]);
        tests[2] = new SingleTest(imageView3, checkBox3, session.dispForTest[2]);
        tests[3] = new SingleTest(imageView4, checkBox4, session.dispForTest[3]);
        tests[4] = new SingleTest(imageView5, checkBox5, session.dispForTest[4]);
        tests[5] = new SingleTest(imageView6, checkBox6, session.dispForTest[5]);

        // debug per capire le disparità e la differenza tra massimo e minimo
        textDisparities = (TextView) findViewById(R.id.textDisparities);

        seen = new int[session.cal.absoluteMaxDisparity + 1];
        requested = new int[session.cal.absoluteMaxDisparity + 1];

        checked = new boolean[DefaultValues.NUMBER_IMAGES];

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSkip = (Button) findViewById(R.id.btnSkip);

        int[] rids = DefaultValues.imageSetToRIDs(imageSet);

        //setBackgroundImages(rids[0]);

        test(session.cal.maxDisparity, session.cal.minDisparity);

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
        double monitorSize10thInc = screenInches * 10;
        int monitorWidthMM = (int) (disWidthPix / dm.xdpi * 25.4);
        int monitorDistance = sharedPreferences.getInt(DefaultValues.PREF_DISTANCE, DefaultValues.DEFAULT_DISTANCE);

        /** Save display measures into monitorData used */
        MonitorData monitorData = new MonitorData(monitorSize10thInc, disWidthPix, monitorWidthMM, dm.heightPixels, monitorDistance);

        /** preparare le shapes */
        shapeSize = DefaultValues.getSize(monitorSize10thInc);
        shapes = new Shape[DefaultValues.CHOICES];

        listShapes = ImageShape.getShapes(imageSet);
        for (int i = 0; i < DefaultValues.CHOICES; i++) {
            shapes[i] = listShapes.get(i);
        }
        currentShape = shapes[0];   //LANG[0]=bird.png

        // Build bitmap for wait message
        waitBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.wait_image);

        satTestConfiguration = new SATTestConfiguration(MAXDISPARITY, Arrays.asList(shapes), false, monitorData, shapeSize, PestDepthCertifierNew.class);
        satTest = new AnaglyphSATest(satTestConfiguration);

        setBackgroundForTest();
    }


    public void setBackgroundForTest(){
        builder = new BitmapBuilder[DefaultValues.NUMBER_IMAGES];
        points = new Points[DefaultValues.NUMBER_IMAGES];
        ranPoints = new PointImage[DefaultValues.NUMBER_IMAGES];
        stripes = new StripesImage[DefaultValues.NUMBER_IMAGES];
        float heigthReduction = (float) 1;   //per fare la bitmap più piccola
        int width, height;
        //satTest.setNextShape();   // fa sparire immagini in test successivi

        for(int i=0; i<DefaultValues.NUMBER_IMAGES; i++) {
            tests[i].imageView.setImageBitmap(waitBitMap);
            width= tests[i].imageView.getLayoutParams().width;       // 150dp
            height = tests[i].imageView.getLayoutParams().height;    // 150dp
            stripes[i] = new StripesImage(width, (int) (height * heigthReduction));
            ranPoints[i] = new RandomDotImage(width, (int) (height * heigthReduction));
            points[i] = SATTest.getPointFromShape(width,(int) (height * heigthReduction),width/2,(int) (height * heigthReduction / 2),session.dispForTest[i],currentShape,ranPoints[i]);
            if (USE_MULTIPLE_PORCESSES)
                builder[i] = new BitmapParallelBuilder(tests[i].imageView, points[i], width, (int) (height * heigthReduction), leftColor, rightColor, colorshape);    //fai costruire la bitmap
            else
                builder[i] = new SingleBitmapBuilder(tests[i].imageView, points[i], width, (int) (height * heigthReduction), leftColor, rightColor, colorshape);    //fai costruire la bitmap
            builder[i].execute();
        }
    }


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
        showSeenRequested();
    }


    private void setBackgroundImages (int RID){
        tests[0].imageView.setBackgroundResource(RID);
        tests[1].imageView.setBackgroundResource(RID);
        tests[2].imageView.setBackgroundResource(RID);
        tests[3].imageView.setBackgroundResource(RID);
        tests[4].imageView.setBackgroundResource(RID);
        tests[5].imageView.setBackgroundResource(RID);
    }


    // metodo per capire quali checkbox sono state cliccate
    public static void evaluateChecked(){
        for(int i=0; i<6; i++){
            if (tests[i].isSelected()) {
                int pos = tests[i].disparity;
                seen[pos]++;
                checked[i] = true;
            }
        }
    }


    public void OnClickSubmit(View view) {
        next();
    }

    public void OnClickSkip(View view) {
        next();
    }

    // metodo dei btns
    public void next(){
        evaluateChecked();
        session.cal.setSolutions(checked);
        session.result = session.cal.certifierStatus.currentResult;
        if (session.result == CalcolatoreDisparities.Result.CONTINUE){
            test(session.cal.maxDisparity, session.cal.minDisparity);
            setBackgroundForTest();
        } else {
            textViewName.setText("FINITO");
        }
    }


    // metodo che aumenta gli interi presenti nel vettore requested -> indica quante volte una disparità è chiesta
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

    // metodo per mostrare seen e requested nella textView
    public static void showSeenRequested(){
        textDisparities.setText("Test numero " + countTest);
        textDisparities.append("\n");
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
        textDisparities.append("\n");
        textDisparities.append("larghezza=" + tests[0].imageView.getWidth() + " altezza=" + tests[0].imageView.getHeight());
    }


    /** metodo per ricavare l'imageset ed il suo nome dalle shared preferences */
    private void loadPreferences() {
        imageSetName = sharedPreferences.getString(DefaultValues.CHOSEN_IMAGESET, "not found");
        imageSet = DefaultValues.stringToImageSet(imageSetName);
        usernameTest = sharedPreferences.getString(DefaultValues.USERNAME_TEST, "username_test");
        MAXDISPARITY = sharedPreferences.getInt(DefaultValues.PREF_MAXDISPARITY, DefaultValues.DEFAULT_MAXDISPARITY);
        MINDISPARITY = sharedPreferences.getInt(DefaultValues.PREF_MINDISPARITY, DefaultValues.DEFAULT_MINDISPARITY);
        OFFSET = sharedPreferences.getInt(DefaultValues.PREF_OFFSET,DefaultValues.DEFAULT_OFFSET);
        NCORR_TO_NEXTLEVEL = sharedPreferences.getInt(DefaultValues.PREF_NCORR_TO_NEXTLEVEL,DefaultValues.DEFAULT_NCORR_TONEXTLEVE);
        NERR_TO_STOPTEST = sharedPreferences.getInt(DefaultValues.PREF_NERR_TOSTOPTEST,DefaultValues.DEFAULT_NERR_TOSTOPTEST);
        leftColor[0] = sharedPreferences.getInt(DefaultValues.RED_L, DefaultValues.CURRENT_RED_L);
        leftColor[1] = sharedPreferences.getInt(DefaultValues.GREEN_L, DefaultValues.CURRENT_GREEN_L);
        leftColor[2] = sharedPreferences.getInt(DefaultValues.BLUE_L, DefaultValues.CURRENT_BLUE_L);
        rightColor[0] = sharedPreferences.getInt(DefaultValues.RED_R, DefaultValues.CURRENT_RED_R);
        rightColor[1] = sharedPreferences.getInt(DefaultValues.GREEN_R, DefaultValues.CURRENT_GREEN_R);
        rightColor[2] = sharedPreferences.getInt(DefaultValues.BLUE_R, DefaultValues.CURRENT_BLUE_R);
        savePreferences();
    }

    private void savePreferences() {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putInt(DefaultValues.PREF_MAXDISPARITY, MAXDISPARITY);
        e.putInt(DefaultValues.PREF_MINDISPARITY, MINDISPARITY);
        e.putInt(DefaultValues.PREF_OFFSET, OFFSET);
        e.putInt(DefaultValues.PREF_NERR_TOSTOPTEST, NERR_TO_STOPTEST);
        e.putInt(DefaultValues.PREF_NCORR_TO_NEXTLEVEL, NCORR_TO_NEXTLEVEL);
        e.putString(DefaultValues.CHOSEN_IMAGESET, imageSet.name());
        e.apply();
    }
}