package com.example.disparities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.Math;

public class MainActivity extends AppCompatActivity {

    static TextView tv1, tv2, tv3, tv4, tv5, tv6, textDisparities, textDebug;
    static CheckBox cb1, cb2, cb3, cb4, cb5, cb6;
    Button btnSubmit, btnSkip;

    static SingleTest [] tests;

    static int maxDisparity;
    static int minDisparity = 1;

    static int [] disparities;

    static int [] zeros = {0, 0, 0};

    static int [] dispForTest;

    static int [] seen, requested;

    static singleResult [] res;

    static int countTest = 0;

    static int lastDisparity;

    static boolean checkSequentiality;

    static boolean firstPhase = true;

    static int tries = 2, rights = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        tv4 = (TextView) findViewById(R.id.textView4);
        tv5 = (TextView) findViewById(R.id.textView5);
        tv6 = (TextView) findViewById(R.id.textView6);

        cb1 = (CheckBox) findViewById(R.id.checkBox1);
        cb2 = (CheckBox) findViewById(R.id.checkBox2);
        cb3 = (CheckBox) findViewById(R.id.checkBox3);
        cb4 = (CheckBox) findViewById(R.id.checkBox4);
        cb5 = (CheckBox) findViewById(R.id.checkBox5);
        cb6 = (CheckBox) findViewById(R.id.checkBox6);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSkip = (Button) findViewById(R.id.btnSkip);
        textDisparities = (TextView) findViewById(R.id.textDisparities);
        textDebug = (TextView) findViewById(R.id.textDebug);

        tests = new SingleTest[6];

        tests[0] = new SingleTest(tv1, cb1, 0);
        tests[1] = new SingleTest(tv2, cb2, 0);
        tests[2] = new SingleTest(tv3, cb3, 0);
        tests[3] = new SingleTest(tv4, cb4, 0);
        tests[4] = new SingleTest(tv5, cb5, 0);
        tests[5] = new SingleTest(tv6, cb6, 0);

        Intent intent = getIntent();
        maxDisparity = intent.getIntExtra("MAX", 10);

        disparities = new int[3];
        dispForTest = new int[6];
        seen = new int[maxDisparity + 1];
        requested = new int[maxDisparity + 1];
        res = new singleResult[maxDisparity + 1];

        test(maxDisparity, minDisparity);
    }

    public static void test(int max, int min) {

        countTest++;

        // calcola il vettore di disparità
        updateDisparities (max, min);
        createDispForTest();
        limitsOneStep();
        updateRequested();
        updateTextView();
        showSeenRequested();
        textDebug.setText("Tries " + tries + " Rights " + rights);
    }


    // metodo per calcolare le 3 disparità (max, mid, min)
    public static void updateDisparities (int max, int min) {
        float diff = max-min;
        disparities[0] = max;
        disparities[1] = Math.round(max - (diff)/2);
        disparities[2] = min;
    }


    // metodo che crea il vettore di 6 disparità (3 valori e 3 zeri in modo casuale)
    public static void createDispForTest() {
        int countDisp = 0;
        int countZeros = 0;
        int counter = 0;

        double rnd;
        int tc;

        while (counter<6) {
            rnd = Math.random();
            tc = (int) Math.round(rnd);
            // inserire uno 0
            if(tc==0) {
                if (countZeros<3) {
                    dispForTest[counter] = zeros[countZeros];
                    countZeros++;
                    counter++;
                }
            }
            //inserire un valore di disparità
            else {	// tc==1
                if (countDisp<3) {
                    dispForTest[counter] = disparities[countDisp];
                    countDisp++;
                    counter++;
                }
            }
        }

        for(int i=0; i<6; i++){
            tests[i].disparity = dispForTest[i];
        }

    }


    // metodo che aumenta gli interi presenti nel vettore requested -> indica quante volte una disparità è chiesta
    public static void updateRequested() {
        // i è la posizione nel vettore (requested)
        // j è il contatore usato per scorrere nel vettore dispForTest
        for (int i=0; i<maxDisparity+1; i++) {
            for (int j=0; j<6; j++) {
                if (i == dispForTest[j]) {
                    requested[i]++;
                }
            }
        }
    }


    // metodo per stampare nelle textView le varie disparità
    public static void updateTextView(){
        tv1.setText("" + dispForTest[0]);
        tv2.setText("" + dispForTest[1]);
        tv3.setText("" + dispForTest[2]);
        tv4.setText("" + dispForTest[3]);
        tv5.setText("" + dispForTest[4]);
        tv6.setText("" + dispForTest[5]);
    }


    // metodo per mostrare seen e requested nella textView
    public static void showSeenRequested(){
        textDisparities.setText("Test numero " + countTest);
        textDisparities.append("\n");
        for (int k=0; k<6; k++){
            textDisparities.append(dispForTest[k] + " ");
        }
        textDisparities.append("\n");
        for (int i=0; i<maxDisparity+1; i++){
            textDisparities.append(seen[i] + " ");
        }
        textDisparities.append("\n");
        for (int j=0; j<maxDisparity+1; j++){
            textDisparities.append(requested[j] + " ");
        }
    }

    // metodo del btnSubmit
    public void onClickSubmit(View view) {
        int countDisp;
        int countZeros;

        countDisp = countSimils(1);
        countZeros = countSimils(0);

        // aggiorna seen in funzione delle checkbox cliccate
        evaluateChecked();

        // calcola l'ultima disparità vista
        lastDisparity = lastDisp();

        // verifica la correttezza del test riguardo le disparità viste
        checkSequentiality = checkSequence();
        int newMax = getMaxDisparity();
        int newMin = getMinDisparity();
        int currMax = getDisparities(0);
        int currMid = getDisparities(1);
        int currMin = getDisparities(2);

        if (checkSequentiality){    // test fatto correttamente
                // fase con disparità tutte diverse
            if (firstPhase) {
                if (countZeros <= 1 && countDisp >= 1) {    // se si vedono uno o nessuno 0 e almeno 1 disparità: proseguire nel test
                    textDebug.setText("First phase e test corretto \n");
                    test(newMax, newMin);
                } else if (countDisp == 0) {    // per qualunque 0 cliccato, se non viene vista nessuna disparità è come fare uno skip
                    textDebug.setText("First phase e nessuna disparità \n");
                    onClickSkip(view);
                } else {    // countZeros>=2
                    textDebug.setText("First phase e test con troppi 0 \n" );
                    test(maxDisparity, currMid);
                }
            }
                // fase con 2 o 3 disparità uguali
            else {
                int same = countSameDisparities();
                if (same==2) {  // 2 disparità uguali e 1 diversa
                        // verifica che siano viste entrambe le disparità uguali
                    boolean bothChecked = checkSameDisparities(currMax);    // currMax == currMid
                    if (bothChecked && countZeros<=1) {     // (countDisp==2) si vedono solo le 2 immagini alla stesssa disparità && countzeros<=1
                        textDebug.setText("Second phase, 2 disparità uguali e 2 viste \n");
                        goToResults();
                    }
                    else if(countDisp == 3 && countZeros <= 1){     // && !bothChecked
                        textDebug.setText("Second phase, 2 disparità uguali e 3 viste \n");
                        test(currMin, 1);   // currMin perchè non avrò mai tutti 1 in questa parte del codice
                    }
                    else { // countZeros>=2 || !bothChecked || countDisp<=1 (premo una sola checkbox a caso che contiene una disparità)
                        textDebug.setText("Second phase, 2 disparità uguali e test scorretto \n");
                        test(maxDisparity, currMid);    // currMid == currMax & currMin == currMax-1
                    }
                }
                else {  // sarà sempre same==3: disparities sarà popolato di soli 1
                    boolean trio = checkSameDisparities(currMin);   // currMax == currMid == currMin == 1
                        // non vedo le 3 disparità (countZeros per controllare che non vengno
                        // premute tutte le checkbox a caso), ma ho ancora tentativi
                    if( tries>0 && (!trio || countZeros > 1) ) { // !trio && countZeros<=1 || countZeros>1
                        tries--;
                        test(currMax, currMin); // ripropongo il test con 1 tentativo in meno
                    }
                        // se finisco i tentativi  e sbaglio riparto da capo resettando tries
                    else if (tries==0 && (!trio || countZeros>1) ) {
                        tries = 2;
                        rights = 0;
                        test(maxDisparity, minDisparity);
                    }
                    else {  // test corretto
                        // trio && countZeros<=1 per ogni valore di tries
                        if (trio && countZeros<=1 && rights==1) {   // indovina e ne ha già indovinato 1
                            goToResults();
                        }
                        else {
                            tries --;
                            rights ++;
                            test(currMax, currMin);
                        }
                    }
                }
                    /** dato che il test è arrivato al punto finale verifico che vengano viste tutte
                     * le disparità a 1 con la tolleranza di al massimo uno 0
                     * Ripeto per 2 volte questa verifica
                     */
            }
        }

        else {  // non sequenzialità nel test
            Toast.makeText(this, "Errore nella sequenzialità", Toast.LENGTH_SHORT).show();
            if (currMid == maxDisparity) {
                onClickSkip(view);
            } else {
                test(maxDisparity, currMid);
            }
        }
    }


    // metodo del btnSkip
    public void onClickSkip(View view) {
        int cMid = getDisparities(1);
        if (cMid == maxDisparity){
            goToResults();
        }
        else {
            test(maxDisparity, cMid);
        }

    }


    // metodo per calcolare quante disparità/zero sono stati cliccati
    public static int countSimils(int sim){
        int count = 0;
        if (sim==0) {   // conta zero cliccati
            for(int i=0; i<6; i++){
                if (tests[i].disparity == 0 && tests[i].isSelected()){
                    count++;
                }
            }
        }
        else {  // conta disparità cliccate
            for(int j=0; j<6; j++){
                if (tests[j].disparity != 0 && tests[j].isSelected()){
                    count++;
                }
            }
        }
        return count;
    }


    // metodo per capire quali checkbox sono state cliccate
    public static void evaluateChecked(){
        for(int i=0; i<6; i++){
            if (tests[i].isSelected()) {
                int pos = tests[i].disparity;
                seen[pos]++;
            }
        }
    }


    // metodo per verificare la disparità più piccola vista
    public static int lastDisp(){
        int lastDisparity = maxDisparity;
        for(int i=0; i<6; i++){
            if(tests[i].disparity != 0 && tests[i].isSelected() && tests[i].disparity<=lastDisparity){
                lastDisparity = tests[i].disparity;
            }
        }
        return lastDisparity;
    }

    public static boolean checkSequence(){
        boolean seq = true;
        // verifica la correttezza del test riguardo le disparità viste
        for(int i=0; i<6; i++){
            if(tests[i].disparity!=0 && !tests[i].isSelected() && tests[i].disparity>lastDisparity){
                seq = false;
                break;
            }
        }
        return seq;
    }


    // ritorna la massima disparità vista:
    // celle (con disparità diversa da 0) cliccate sono usate per calcolare il massimo
    public static int getMaxDisparity(){
        int max = maxDisparity;
        for (int i=0; i<6; i++){
            if(tests[i].disparity!=0 && tests[i].isSelected() && tests[i].disparity<max){
                max = tests[i].disparity;
            }
        }
        return max;
    }


    // ritorna la massima disparità non vista
    // celle (con disparità diversa da 0) non cliccate usate per calcolare il minimo
    public static int getMinDisparity(){
        int min = 1;
        for (int i=0; i<6; i++){
            if(!tests[i].isSelected() && tests[i].disparity!=0){
                min = tests[i].disparity;
                break;  // sequenzialità nei 3 valori di disparità
            }
        }
        return min;
    }


    // metodo che ritorna un valore del vettore disparities
    public static int getDisparities(int x){
        if (x==0){
            return disparities[0];
        }
        else if (x==1){
            return disparities[1];
        }
        else if (x==2){
            return disparities[2];
        }
        else {
            return maxDisparity;
        }
    }


    // metodo che ritorna il numero di disparità uguali in disparities
    public static int countSameDisparities(){
        if(disparities[0] == disparities[1] && disparities[0] == disparities[2]){
            return 3;
        }
        else if (disparities[0] == disparities[1] && disparities[0] != disparities[2]){
            return 2;
        }
        else {
            return 1;
        }
    }


    // metodo per capire se sono verso la fine del test
    public static void limitsOneStep(){
        if (disparities[0]-disparities[2] <= 1){
            firstPhase = false;
        }
        else {
            firstPhase = true;
        }
    }


    // metodo per capire se quando ci sono 2 disparità uguali sono entrambe cliccate
    public static boolean checkSameDisparities(int disp){
        boolean all = true;
        for (int i=0; i<6; i++){
            if (tests[i].disparity == disp && !tests[i].isSelected() || tests[i].disparity!=0 && tests[i].disparity != disp && tests[i].isSelected()){
                all = false;
                return all;
            }
        }
        return all;
    }


    // metodo per andare ai risultati
    public void goToResults(){
        Intent intent = new Intent (MainActivity.this, ResultsActivity.class);
        startActivity(intent);
    }

}