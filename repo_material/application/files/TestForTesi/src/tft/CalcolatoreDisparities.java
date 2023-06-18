package tft;

public class CalcolatoreDisparities {

    /** The Enum Solution */
    public enum Solution {
        /** The right. */
        RIGHT,
        /** The wrong. */
        WRONG,
        /** The null. */
        NULL
    }

    /** The Enum Result of the session at the end */
    public enum Result {
        /** The continue. */
        CONTINUE,
        /** The finish certified. */
        FINISH_CERTIFIED,
        /** The finish not certified. */
        FINISH_NOT_CERTIFIED // NO depth could be certified
    }

    /** The Class CertifierStatus */
    static public class CertifierStatus {

        /** The current depth. */
        // current Depth
        public static int currentDepth;

        /** The current result. */
        // current status
        public static Result currentResult;

        @Override
        public String toString() {
            switch (currentResult) {
                case FINISH_CERTIFIED:
                    return "CERTIFICATO a livello: " + currentDepth;
                case FINISH_NOT_CERTIFIED:
                    return "FINITO ma NON CERTIFICATO fino a livello: " + currentDepth;
                case CONTINUE:
                    return "INCONCLUSIVO (testando " + currentDepth + ")";
            }
            return "";
        }
    }

    /** The certifier status */
    protected CertifierStatus certifierStatus;

    /** The solution og the test */
    Solution solution;


    /** number of disparities */
    private final static int N_DISP = 6;

    /** disparities of the starting test */
    static int absoluteMaxDisparity;
    static int absoluteMinDisparity = 1;
    int maxDisparity;
    int minDisparity;

    /** arrays used to create the final array of disparity */
    static int [] disparities;
    static int [] zeros = {0, 0, 0};

    /** final array */
    static int [] result = new int [N_DISP];

    /** array of selected images */
    static boolean [] selected;

    /** the lowest seen disparity (no check in test execution) */
    static int lastDisparity;

    /** variable to understand the point of the test */
    static boolean firstPhase = true;

    /** variables used when there are three 1 to suggest 3 same tests */
    static int tries = 2, rights = 0, wrongs = 0;


    public CalcolatoreDisparities(){
        absoluteMaxDisparity = 10;
        maxDisparity = absoluteMaxDisparity;
        minDisparity = absoluteMinDisparity;
    }

    public CalcolatoreDisparities(int absoluteMaxDisparity){
        this.absoluteMaxDisparity = absoluteMaxDisparity;
        maxDisparity = this.absoluteMaxDisparity;
        minDisparity = absoluteMinDisparity;
    }

    /** metodo che costruisce il vettore di disparit�
    * dato il corrente min e massimo
     * esempio : min 1 , max 10 -> [0,10,5,1,0]
     */
    public int[] getCurrentDisparities(int max, int min){
        // TODO riempi il result  con gli interi opportuni

        // calcola i 3 valori di disparit� da usare nel test
        float diff = max - min;
        disparities = new int[zeros.length];
        disparities[0] = max;
        disparities[1] = Math.round(max - (diff)/2);
        disparities[2] = min;

        // contatori usati nel ciclo per rimepire il vettore finale di tre 0 e tre valori di disparit�
        int countDisp = 0;
        int countZeros = 0;
        int counter = 0;

        // variabili usate nel ciclo per capire se nella i-esima posizione bisogna inserire uno 0
        // o un valore di disparit�
        double rnd;
        int tc;

        while (counter < N_DISP) {
            rnd = Math.random();
            tc = (int) Math.round(rnd);
            // inserire uno 0
            if(tc==0) {
                if (countZeros<3) {
                    result[counter] = zeros[countZeros];
                    countZeros++;
                    counter++;
                }
            }
            //inserire un valore di disparit�
            else {	// tc==1
                if (countDisp<3) {
                    result[counter] = disparities[countDisp];
                    countDisp++;
                    counter++;
                }
            }
        }
        return result;
    }

    /** setta le soluzioni scelte dall'utente
     * selected[i] � true se e solo se l'utente ha selezionato l'immagine i-esima
     * sar� il vettore di checkbox cliccate
     * Se la checkbox in posizone i � cliccata, il valore di disparit� in tale posizione � "visto"
     * - calcola il nuovo massimo e minimo
     */

    public void setSolutions(boolean[] selected){
        // assert selected.length = N_DISP;
        this.selected = selected;

        /** TODO calcolare il nuovo massimo e minimo
         *  TODO inoltre vedere nelle classi su gitHub gli enumerativi relativi allo
         *  TODO stato e al risultato
         *  Come per le altre classi in funzione dei valori di risultato (CONTINUE; SKIP;...) o dello
         *  stato (RIGHT; WRONG;...) valutare nuovi massimo e minimo
         */

        // contare quanti 0 e quante disparit� sono state cliccate
        int countDisp;
        int countZeros;
        countDisp = countSimils(1);
        countZeros = countSimils(0);

        // calcola l'ultima disparit� vista
        lastDisparity = lastDisp();

        // estrai i valori di disparit� dal vettore
        int currMax = getDisparities(0);
        int currMid = getDisparities(1);
        int currMin = getDisparities(2);

        // verifica la correttezza del test riguardo le disparit� viste
        boolean checkSequentiality = checkSequence();

        if (checkSequentiality){    // test fatto correttamente
        	limitsOneStep();
            // fase con disparit� tutte diverse
            if (firstPhase) {
                if (countZeros <= 1 && countDisp >= 1) {
                    // se si vedono uno o nessuno 0 e almeno 1 disparit�: proseguire nel test
                    // First phase e test corretto
                    // nuovo test con newMax e newMin
                    solution = Solution.RIGHT;
                    certifierStatus.currentResult = Result.CONTINUE;
                    maxDisparity = getMaxDisparity();
                    minDisparity = getMinDisparity();
                    System.out.println("New max = " + maxDisparity + " e nuovo min = " + minDisparity);
                    System.out.println("Numero 0 = " + countZeros + " e numero disparit� = " + countDisp + " (CORRETTO)");
                    result = getCurrentDisparities(maxDisparity, minDisparity);
                } else if (countDisp == 0) {
                		// per qualunque 0 cliccato, se non viene vista nessuna disparit� � come fare uno skip
                		// First phase e nessuna disparit�
                		solution = Solution.NULL;
                		certifierStatus.currentResult = Result.CONTINUE;
                		maxDisparity = absoluteMaxDisparity;
                		minDisparity = currMid;
                		System.out.println("New max = " + maxDisparity + " e nuovo min = " + minDisparity);
                		System.out.println("Numero 0 = " + countZeros + " e numero disparit� = " + countDisp + " (SKIP)");
                		result = getCurrentDisparities(maxDisparity, minDisparity);
                	} else {    // countZeros>=2
                		// First phase e test con troppi 0
                		solution = Solution.WRONG;
                		certifierStatus.currentResult = Result.CONTINUE;
                		maxDisparity = absoluteMaxDisparity;
                		minDisparity = currMid;
                		System.out.println("New max = " + maxDisparity + " e nuovo min = " + minDisparity);
                		System.out.println("Numero 0 = " + countZeros + " e numero disparit� = " + countDisp + " (TROPPI 0)");
                		result = getCurrentDisparities(maxDisparity, minDisparity);
                	}
            }
            // fase con 2 o 3 disparit� uguali
            else {
                int same = countSameDisparities();
                if (same==2) {  // 2 disparit� uguali e 1 diversa
                    // verifica che siano viste entrambe le disparit� uguali
                    boolean bothChecked = checkSameDisparities(currMax);    // currMax == currMid
                    if (bothChecked && countZeros<=1) {     // (countDisp==2) si vedono solo le 2 immagini alla stesssa disparit� && countzeros<=1
                        // Second phase, 2 disparit� uguali e 2 viste
                        solution = Solution.RIGHT;
                        certifierStatus.currentResult = Result.FINISH_CERTIFIED;
                        certifierStatus.currentDepth = currMid;
                        System.out.println("FINITO A " + currMid);
                        System.out.println("Numero 0 = " + countZeros + " e numero disparit� = " + countDisp + " (2 UGUALI E 2 VISTE)");
                    } else {
                    	if(countDisp == 3 && countZeros <= 1){     // && !bothChecked
                    		// Second phase, 2 disparit� uguali e 3 viste
                    		solution = Solution.RIGHT;
                    		certifierStatus.currentResult = Result.CONTINUE;
                    		maxDisparity = currMin;
                    		minDisparity = absoluteMinDisparity;
                    		System.out.println("New max = " + maxDisparity + " e nuovo min = " + minDisparity);
                    		System.out.println("Numero 0 = " + countZeros + " e numero disparit� = " + countDisp + " (2 UGUALI E 3 VISTE)");
                    		result = getCurrentDisparities(maxDisparity, minDisparity);   // currMin perch� non avr� mai tutti 1 in questa parte del codice
                    	} else { // countZeros>=2 || !bothChecked || countDisp<=1 (premo una sola checkbox a caso che contiene una disparit�)
                    		// Second phase, 2 disparit� uguali e test scorretto
                    		if (currMid == absoluteMaxDisparity) {
                        		// errore nel test e siamo gi� al valore pi� alto di disparit�
                        		solution = Solution.WRONG;
                        		certifierStatus.currentResult = Result.FINISH_NOT_CERTIFIED;
                        		System.out.println("NON CERTIFICATO A " + currMid);
                                System.out.println("Numero 0 = " + countZeros + " e numero disparit� = " + countDisp + " (2 UGUALI A 10 + ERRORE)");	
                        	} else {
                        		solution = Solution.WRONG;
                        		certifierStatus.currentResult = Result.CONTINUE;
                        		maxDisparity = absoluteMaxDisparity;
                        		minDisparity = currMid;
                        		System.out.println("New max = " + maxDisparity + " e nuovo min = " + minDisparity);
                        		System.out.println("Numero 0 = " + countZeros + " e numero disparit� = " + countDisp + " (2 UGUALI E ERRORE)");
                        		result = getCurrentDisparities(maxDisparity, minDisparity);    // currMid == currMax & currMin == currMax-1
                        	}
                    	}
                    }
                }    
                else {  // sar� sempre same==3: disparities sar� popolato di soli 1
                    boolean trio = checkSameDisparities(currMin);   // currMax == currMid == currMin == 1
                    // non vedo le 3 disparit� (countZeros per controllare che non vengno
                    // premute tutte le checkbox a caso), ma ho ancora tentativi
                    if( tries>0 && (!trio || countZeros > 1) && wrongs<1 ) { // !trio && countZeros<=1 || countZeros>1
                    	tries--;
                    	wrongs++;
                        solution = Solution.WRONG;
                        certifierStatus.currentResult = Result.CONTINUE;
                        maxDisparity = currMax;
                        minDisparity = currMin;
                        System.out.println("New max = " + maxDisparity + " e nuovo min = " + minDisparity);
                        System.out.println("Numero 0 = " + countZeros + " e numero disparit� = " + countDisp + " (3 UGUALI: " + tries + "(+1) tentativi rimanenti)");
                        result = getCurrentDisparities(currMax, currMin); // ripropongo il test con 1 tentativo in meno
                    }
                    // se finisco i tentativi  e sbaglio riparto da capo resettando tries
                    else if ((!trio || countZeros>1) && wrongs>=1) {
                    	wrongs++;
                        solution = Solution.WRONG;
                        certifierStatus.currentResult = Result.CONTINUE;
                        maxDisparity = absoluteMaxDisparity;
                        minDisparity = absoluteMinDisparity;
                        System.out.println("New max = " + maxDisparity + " e nuovo min = " + minDisparity);
                        System.out.println("Numero 0 = " + countZeros + " e numero disparit� = " + countDisp + " (3 UGUALI: " + tries + " tentnativi rimanenti e " + wrongs + " errori)");
                        tries = 2;	
                        rights = 0;
                        wrongs = 0;
                        System.out.println("Ripartire da capo con " + tries + "(+1) tentativi e " + rights +" risposte giuste e " + wrongs + " risposte sbagliate");
                        result = getCurrentDisparities(maxDisparity, minDisparity);
                    }
                    else {  // test corretto
                        // trio && countZeros<=1 per ogni valore di tries
                        if (trio && countZeros<=1 && rights==1) {   // indovina e ne ha gi� indovinato 1
                        	rights ++;
                            solution = Solution.RIGHT;
                            certifierStatus.currentResult = Result.FINISH_CERTIFIED;
                            System.out.println("FINITO A " + currMid);
                            System.out.println("Numero 0 = " + countZeros + " e numero disparit� = " + countDisp + " (3 UGUALI E 3 VISTE PER 2 VOLTE)");
                            System.out.println("Tentativi rimasti = " + tries + " Giuste = " + rights + " Errori = " + wrongs);
                        }
                        else {
                        	tries--;
                            rights ++;
                            solution = Solution.RIGHT;
                            certifierStatus.currentResult = Result.CONTINUE;
                            maxDisparity = currMax;
                            minDisparity = currMin;
                            System.out.println("New max = " + maxDisparity + " e nuovo min = " + minDisparity);
                            System.out.println("Numero 0 = " + countZeros + " e numero disparit� = " + countDisp + " (3 UGUALI: prima volta giusta)");
                            System.out.println("Ancora " + tries + "(+1) tentativi e " + rights +" risposte giuste");
                            result = getCurrentDisparities(maxDisparity, minDisparity);
                        }
                    }
                }
                /** dato che il test � arrivato al punto finale verifico che vengano viste tutte
                 * le disparit� a 1 con la tolleranza di al massimo uno 0
                 * Ripeto per 2 volte questa verifica
                 */
            }
        }

        else {
            solution = Solution.WRONG;
            certifierStatus.currentResult = Result.CONTINUE;
            maxDisparity = absoluteMaxDisparity;
            minDisparity = currMid;
            System.out.println("New max = " + maxDisparity + " e nuovo min = " + minDisparity);
            System.out.println("Numero 0 = " + countZeros + " e numero disparit� = " + countDisp + " (ERRORE SEQUENZIALITA)");
            result = getCurrentDisparities(maxDisparity, minDisparity);
        }

    }


    /** metodo per calcolare quante disparit�/zero sono stati cliccati */
    public static int countSimils(int sim){
        int count = 0;
        if (sim==0) {   // conta zero cliccati
            for(int i=0; i<6; i++){
                if (result[i] == 0  && selected[i]) {   // cella con disparit� a 0 cliccata
                    count++;
                }
            }
        }
        else {  // conta disparit� cliccate
            for(int i=0; i<6; i++){
                if (result[i] != 0 && selected[i]){    // cella con disparit� non a 0 cliccata
                    count++;
                }
            }
        }
        return count;
    }

    /** metodo per verificare la disparit� pi� piccola vista */
    public int lastDisp(){
        int lastDisparity = maxDisparity;
        for(int i=0; i<6; i++){
            if(result[i] != 0 && selected[i] && result[i]<=lastDisparity){
                lastDisparity = result[i];
            }
        }
        return lastDisparity;
    }

    /** metodo che ritorna un valore del vettore disparities */
    public int getDisparities(int x){
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
            return this.maxDisparity;
        }
    }

    /** metodo che verifica la correttezza nella sequenzialit� delle disparit� viste */
    public static boolean checkSequence(){
        boolean seq = true;
        // verifica la correttezza del test riguardo le disparit� viste
        for(int i=0; i<6; i++){
            if(result[i] != 0 && result[i] > lastDisparity && !selected[i]){
                seq = false;
                break;
            }
        }
        return seq;
    }

    /** metodo che ritorna il numero di disparit� uguali in disparities */
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

    /** metodo per capire se sono verso la fine del test */
    public static void limitsOneStep(){
        if (disparities[0]-disparities[2] <= 1){
            firstPhase = false;
        }
        else {
            firstPhase = true;
        }
    }

    /** metodo per capire se quando ci sono 2 o 3 disparit� uguali sono tutte cliccate
     * se ci sono 2 disparit� uguali ed 1 diversa e vengono viste tutte e 3 il metodo ritorna false */
    public static boolean checkSameDisparities(int disp){
        boolean all = true;
        for (int i=0; i<6; i++){
            if (result[i] == disp && !selected[i] || result[i] != 0 && result[i] != disp && selected[i]){
                all = false;
                return all;
            }
        }
        return all;
    }

    /** ritorna la massima disparit� vista:
     * celle (con disparit� diversa da 0) cliccate sono usate per calcolare il massimo */
    public int getMaxDisparity(){
        int max = absoluteMaxDisparity;
        for (int i=0; i<6; i++){
            if(result[i]!=0 && result[i]<max && selected[i]){
                max = result[i];
            }
        }
        return max;
    }


    /** ritorna la massima disparit� non vista
     * celle (con disparit� diversa da 0) non cliccate usate per calcolare il minimo */
    public int getMinDisparity(){
        int min = absoluteMinDisparity;
        for (int i=0; i<6; i++){
            if(result[i]!=0 && !selected[i]){
                min = result[i];
                break;  // sequenzialit� nei 3 valori di disparit�
            }
        }
        return min;
    }


    /** */
    // TODO
    public boolean isFinished(){
        boolean finished = false;

        return finished;
    }


}
