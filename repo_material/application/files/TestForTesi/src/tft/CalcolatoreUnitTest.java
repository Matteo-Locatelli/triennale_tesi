package tft;

import java.util.Arrays;

public class CalcolatoreUnitTest {
	
	static int values[];
	static CalcolatoreDisparities cal1;

    public static void main(String[] args) {

        cal1 = new CalcolatoreDisparities();
        System.out.println("Cal1 max " + cal1.maxDisparity + "\n" + "Cal1 min " + cal1.minDisparity);

        values = cal1.getCurrentDisparities(cal1.maxDisparity, cal1.minDisparity);

        System.out.println(Arrays.toString(values));

        // questa parte è servita per capire che maxDisparity e minDisaprity non devono essere static
        /**
        CalcolatoreDisparities cal2 = new CalcolatoreDisparities(12);
        System.out.println("Cal2 max " + cal2.maxDisparity + " Cal2 min " + cal2.minDisparity);
        int [] values2;
        values2 = cal2.getCurrentDisparities(cal2.maxDisparity, cal2.minDisparity);
        System.out.println(Arrays.toString(values2));
        */
        
        
        test1();	// 10, 6, 1
        
        /** Test per verificare la correttezza dell'algoritmo
        test2();	// 10, 6, 1 -> 6, 4, 1
        test3();	// 6, 4, 1 -> 6, 5, 4
        test4();	// 6, 5, 4 -> 6, 6, 
        */
        
        /**	Test per verifica re che continuando a sbagliare si finisce a MAXDISPARITY
        test5();	// 10, 6, 1 -> 10, 8, 6
        test6();	// 10, 8, 6 -> 10, 9, 8
        test7();	// 10, 9, 8 -> 10, 10, 9
        */
        
        
        /** 2 testOnes() di fila sevono per verificare che se eseguo 2 test ERRATI o CORRETTI di fila l'algoritmo funziona
        	ENTRMABI CORRETTI: l'algoritmo funziona, lavorando anche su tries e rights e wrongs
        	ENTRAMBI ERRATI:
         */
        
        /** 2 testOnesRight di fila: FINITO A 1, tries e rights e wrongs corretti
        testOnesRight();
        testOnesRight();
         */
        /** 2 testOnesRight ma con un testOnesWrong: FINITO A 1, tries e rights e wrongs corretti (con WRONG come primo o secondo test)
        testOnesRight();	
        testOnesWrong();
        testOnesRight();
         */
        /** 2 testOnesWrong ma con un testOnesRight: RIPARTIRE DA CAPO, tries e rights e wrongs corretti (con RIGHT come primo o secondo test)
        testOnesWrong();
        testOnesRight();
        testOnesWrong();
         */	
        /** 2 testOnesWrong di fila: RIPARTIRE DA CAPO, tries e rights e wrong corretti
        testOnesWrong();
        testOnesWrong();
         */

    }
    
    /** test1() elabora il primo vettore 10-6-1 in funzione delle disparità che vedo (mettendo sol[i] = true)
     * Il test funziona correttamente:
     * - se non si rispetta la sequenzialità il test dà errore
     * - se vedo uno o nessuno 0 il test è considerato corretto per ogni disparità vista (solo 10, 10-6, 10-6-1)
     * - se vengono visti due o tre 0 il test è considerato sbagliato
     * - se non viene vista nessuna disparità il test fa uno SKIP per ogni 0 visto
     * CORRETTO: calcola nuovo vettore (10-8-6, 6-4-1, 1-1-1)
     * ERRATO / SKIP: 10-8-6
     */
    public static void test1() {
    	
    	boolean[] sol = new boolean[6];
        for (int i=0; i<6; i++) {
        	sol[i] = false;
        }
    	
    	// poszione 10
        int first10 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 10) {
        		first10 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 10: " + first10);
        
        // posizione 6
        int first6 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 6) {
        		first6 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 6: " + first6);
        
        // posizione 1
        int first1 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 1) {
        		first1 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 1: " + first1);
        
        // posizione primo 0
        int firstZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0) {
        		firstZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione primo 0: " + firstZero);
        
        // posizione secondo 0
        int secondZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero) {
        		secondZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione secondo 0: " + secondZero);
        
        // posizione terzo 0
        int thirdZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero && i!=secondZero) {
        		thirdZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione terzo 0: " + thirdZero);
        
        
        sol[first10] = true;
        sol[first6] = true;
        sol[first1] = true;
        //sol[firstZero] = true;
        //sol[secondZero] = true;
        //sol[thirdZero] = true;
        
        cal1.setSolutions(sol);
        
        System.out.println("New Array: " + Arrays.toString(cal1.getCurrentDisparities(cal1.maxDisparity, cal1.minDisparity)));
    }
    
    /** test2() elabora un secondo vettore 6-4-1 (dopo aver visto 10., 6) in funzione delle disparità che vedo (mettendo sol[i] = true)
     * Il test funziona correttamente:
     * - se non si rispetta la sequenzialità il test dà errore
     * - se vedo uno o nessuno 0 il test è considerato corretto per ogni disparità vista (solo 6, 6-4, 6-4-1)
     * - se vengono visti due o tre 0 il test è considerato sbagliato
     * - se non viene vista nessuna disparità il test fa uno SKIP per ogni 0 visto
     * CORRETTO: calcola nuovo vettore (6-5-4, 4-3-1, 1-1-1)
     * ERRATO / SKIP: 10-7-4
     */
    public static void test2() {
    	boolean[] sol = new boolean[6];
        for (int i=0; i<6; i++) {
        	sol[i] = false;
        }
    	
    	// poszione 6
        int first6 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 6) {
        		first6 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 6: " + first6);
        
        // posizione 4
        int first4 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 4) {
        		first4 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 4: " + first4);
        
        // posizione 1
        int first1 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 1) {
        		first1 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 1: " + first1);
        
        // posizione primo 0
        int firstZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0) {
        		firstZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione primo 0: " + firstZero);
        
        // posizione secondo 0
        int secondZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero) {
        		secondZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione secondo 0: " + secondZero);
        
        // posizione terzo 0
        int thirdZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero && i!=secondZero) {
        		thirdZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione terzo 0: " + thirdZero);
        
        
        sol[first6] = true;
        //sol[first4] = true;
        //sol[first1] = true;
        //sol[firstZero] = true;
        //sol[secondZero] = true;
        //sol[thirdZero] = true;
        
        cal1.setSolutions(sol);
        
        System.out.println("New Array: " + Arrays.toString(cal1.getCurrentDisparities(cal1.maxDisparity, cal1.minDisparity)));
    	
    }
    
    
    /** test1() elabora il terzo vettore 6-5-4 (dopo aver visto solo 6) in funzione delle disparità che vedo (mettendo sol[i] = true)
     * Il test funziona correttamente:
     * - se non si rispetta la sequenzialità il test dà errore
     * - se vedo uno o nessuno 0 il test è considerato corretto per ogni disparità vista (solo 6, 6-5, 6-5-4)
     * - se vengono visti due o tre 0 il test è considerato sbagliato
     * - se non viene vista nessuna disparità il test fa uno SKIP per ogni 0 visto
     * CORRETTO: calcola nuovo vettore (6-6-5, 5-5-4-, 4-3-1)
     * ERRATO / SKIP: 10-8-5
     */
    public static void test3() {
    	
    	boolean[] sol = new boolean[6];
        for (int i=0; i<6; i++) {
        	sol[i] = false;
        }
    	
    	// poszione 6
        int first6 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 6) {
        		first6 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 6: " + first6);
        
        // posizione 5
        int first5 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 5) {
        		first5 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 5: " + first5);
        
        // posizione 4
        int first4 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 4) {
        		first4 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 4: " + first4);
        
        // posizione primo 0
        int firstZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0) {
        		firstZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione primo 0: " + firstZero);
        
        // posizione secondo 0
        int secondZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero) {
        		secondZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione secondo 0: " + secondZero);
        
        // posizione terzo 0
        int thirdZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero && i!=secondZero) {
        		thirdZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione terzo 0: " + thirdZero);
        
        
        sol[first6] = true;
        //sol[first5] = true;
        //sol[first4] = true;
        //sol[firstZero] = true;
        //sol[secondZero] = true;
        //sol[thirdZero] = true;
        
        cal1.setSolutions(sol);
        
        System.out.println("New Array: " + Arrays.toString(cal1.getCurrentDisparities(cal1.maxDisparity, cal1.minDisparity)));
    }
    
    /** test4() elabora il quarto vettore 6-6-5 (dopo aver visto solo 6) in funzione delle disparità che vedo (mettendo sol[i] = true)
     * Il test funziona correttamente:
     * - se non si rispetta la sequenzialità o si vede un solo 6 il test dà errore
     * - se vedo uno o nessuno 0 il test è considerato corretto per ogni disparità vista (6-6, 6-6-5)
     * - se vengono visti due o tre 0 il test è considerato sbagliato
     * - se non viene vista nessuna disparità il test fa uno SKIP per ogni 0 visto
     * CORRETTO: se si vedono solo i due 6 -> TEST FINITO A 6; altrimenti nuovo vettore 5-3-1
     * ERRATO / SKIP: 10-8-6
     */
    public static void test4() {
    	
    	boolean[] sol = new boolean[6];
        for (int i=0; i<6; i++) {
        	sol[i] = false;
        }
    	
    	// poszione primo 6
        int first6 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 6) {
        		first6 = i;
        	    break;
        	}
        }
        System.out.println("Posizione primo 6: " + first6);
        
        // posizione secondo 6
        int second6 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 6 && i!=first6) {
        		second6 = i;
        	    break;
        	}
        }
        System.out.println("Posizione secondo 6: " + second6);
        
        // posizione 5
        int first5 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 5) {
        		first5 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 5: " + first5);
        
        // posizione primo 0
        int firstZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0) {
        		firstZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione primo 0: " + firstZero);
        
        // posizione secondo 0
        int secondZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero) {
        		secondZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione secondo 0: " + secondZero);
        
        // posizione terzo 0
        int thirdZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero && i!=secondZero) {
        		thirdZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione terzo 0: " + thirdZero);
        
        
        //sol[first6] = true;
        //sol[second6] = true;
        //sol[first5] = true;
        //sol[firstZero] = true;
        //sol[secondZero] = true;
        //sol[thirdZero] = true;
        
        cal1.setSolutions(sol);
        
        System.out.println("New Array: " + Arrays.toString(cal1.getCurrentDisparities(cal1.maxDisparity, cal1.minDisparity)));
    }
    
    
    /** testOnes() elabora il vettore 1-1-1 (dopo aver visto 10-6-1) in funzione delle disparità che vedo (mettendo sol[i] = true)
     * Il test funziona correttamente:
     * - se vengono visti tre 1 e nessuno o uno 0, allora il test è considerato corretto
     * - se non vengono visti tre 1, oppure vengono visti due o tre 0, allora il test è considerato sbagliato
     * CORRETTO: diminuisce di 1 tries, aumenta di 1 rights
     * ERRATO: diminuisce di 1 tries
     * A questo punto bisgona ripetere il testOnes 1 o 2 volte.
     * In totale testOnes sarà fatto 2 o 3 volte: 2 se sono entrambe ERRATE, 3 se una delle prime 2 volte il test risulta CORRETTO
     */
    public static void testOnesRight() {
    	
    	boolean[] sol = new boolean[6];
        for (int i=0; i<6; i++) {
        	sol[i] = false;
        }
    	
    	// posizione primo 1
        int firstOne = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 1) {
        		firstOne = i;
        	    break;
        	}
        }
        System.out.println("Posizione primo 1: " + firstOne);
        
        // posizione secondo 1
        int secondOne = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 1 && i!=firstOne) {
        		secondOne = i;
        	    break;
        	}
        }
        System.out.println("Posizione secondo 1: " + secondOne);
        
        // posizione terzo 1
        int thirdOne = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 1 && i!=firstOne && i!=secondOne) {
        		thirdOne = i;
        	    break;
        	}
        }
        System.out.println("Posizione terzo 1: " + thirdOne);
        
        // posizione primo 0
        int firstZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0) {
        		firstZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione primo 0: " + firstZero);
        
        // posizione secondo 0
        int secondZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero) {
        		secondZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione secondo 0: " + secondZero);
        
        // posizione terzo 0
        int thirdZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero && i!=secondZero) {
        		thirdZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione terzo 0: " + thirdZero);
        
        sol[firstOne] = true;
        sol[secondOne] = true;
        sol[thirdOne] = true;
        //sol[firstZero] = true;
        //sol[secondZero] = true;
        //sol[thirdZero] = true;
        
        cal1.setSolutions(sol);
        
        System.out.println("New Array: " + Arrays.toString(cal1.getCurrentDisparities(cal1.maxDisparity, cal1.minDisparity)));
    }
    
    
    public static void testOnesWrong() {
    	
    	boolean[] sol = new boolean[6];
        for (int i=0; i<6; i++) {
        	sol[i] = false;
        }
    	
    	// posizione primo 1
        int firstOne = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 1) {
        		firstOne = i;
        	    break;
        	}
        }
        System.out.println("Posizione primo 1: " + firstOne);
        
        // posizione secondo 1
        int secondOne = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 1 && i!=firstOne) {
        		secondOne = i;
        	    break;
        	}
        }
        System.out.println("Posizione secondo 1: " + secondOne);
        
        // posizione terzo 1
        int thirdOne = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 1 && i!=firstOne && i!=secondOne) {
        		thirdOne = i;
        	    break;
        	}
        }
        System.out.println("Posizione terzo 1: " + thirdOne);
        
        // posizione primo 0
        int firstZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0) {
        		firstZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione primo 0: " + firstZero);
        
        // posizione secondo 0
        int secondZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero) {
        		secondZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione secondo 0: " + secondZero);
        
        // posizione terzo 0
        int thirdZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero && i!=secondZero) {
        		thirdZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione terzo 0: " + thirdZero);
        
        sol[firstOne] = true;
        sol[secondOne] = true;
        //sol[thirdOne] = true;
        sol[firstZero] = true;
        sol[secondZero] = true;
        //sol[thirdZero] = true;
        
        cal1.setSolutions(sol);
        
        System.out.println("New Array: " + Arrays.toString(cal1.getCurrentDisparities(cal1.maxDisparity, cal1.minDisparity)));
    }
    
    
    public static void test5() {
    	
    	boolean[] sol = new boolean[6];
        for (int i=0; i<6; i++) {
        	sol[i] = false;
        }
    	
    	// poszione 10
        int first10 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 10) {
        		first10 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 10: " + first10);
        
        // posizione 8
        int first8 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 8) {
        		first8 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 8: " + first8);
        
        // posizione 6
        int first6 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 6) {
        		first6 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 6: " + first6);
        
        // posizione primo 0
        int firstZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0) {
        		firstZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione primo 0: " + firstZero);
        
        // posizione secondo 0
        int secondZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero) {
        		secondZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione secondo 0: " + secondZero);
        
        // posizione terzo 0
        int thirdZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero && i!=secondZero) {
        		thirdZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione terzo 0: " + thirdZero);
        
        
        sol[first10] = true;
        sol[first8] = true;
        sol[first6] = true;
        sol[firstZero] = true;
        sol[secondZero] = true;
        sol[thirdZero] = true;
        
        cal1.setSolutions(sol);
        
        System.out.println("New Array: " + Arrays.toString(cal1.getCurrentDisparities(cal1.maxDisparity, cal1.minDisparity)));
    }
    
    
    public static void test6() {
    	
    	boolean[] sol = new boolean[6];
        for (int i=0; i<6; i++) {
        	sol[i] = false;
        }
    	
    	// poszione 10
        int first10 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 10) {
        		first10 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 10: " + first10);
        
        // posizione 9
        int first9 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 9) {
        		first9 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 9: " + first9);
        
        // posizione 8
        int first8 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 8) {
        		first8 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 8: " + first8);
        
        // posizione primo 0
        int firstZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0) {
        		firstZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione primo 0: " + firstZero);
        
        // posizione secondo 0
        int secondZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero) {
        		secondZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione secondo 0: " + secondZero);
        
        // posizione terzo 0
        int thirdZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero && i!=secondZero) {
        		thirdZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione terzo 0: " + thirdZero);
        
        
        sol[first10] = true;
        sol[first9] = true;
        sol[first8] = true;
        sol[firstZero] = true;
        sol[secondZero] = true;
        sol[thirdZero] = true;
        
        cal1.setSolutions(sol);
        
        System.out.println("New Array: " + Arrays.toString(cal1.getCurrentDisparities(cal1.maxDisparity, cal1.minDisparity)));
    }

    
    public static void test7() {
    	
    	boolean[] sol = new boolean[6];
        for (int i=0; i<6; i++) {
        	sol[i] = false;
        }
    	
    	// poszione primo 10
        int first10 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 10) {
        		first10 = i;
        	    break;
        	}
        }
        System.out.println("Posizione primoo 10: " + first10);
        
        // posizione secondo 10
        int second10 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 10 && i!=first10) {
        		second10 = i;
        	    break;
        	}
        }
        System.out.println("Posizione secondo 10: " + second10);
        
        // posizione 9
        int first9 = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 9) {
        		first9 = i;
        	    break;
        	}
        }
        System.out.println("Posizione 9: " + first9);
        
        // posizione primo 0
        int firstZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0) {
        		firstZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione primo 0: " + firstZero);
        
        // posizione secondo 0
        int secondZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero) {
        		secondZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione secondo 0: " + secondZero);
        
        // posizione terzo 0
        int thirdZero = 0;
        for (int i = 0; i < values.length; i++) {
        	if (values[i] == 0 && i!=firstZero && i!=secondZero) {
        		thirdZero = i;
        	    break;
        	}
        }
        System.out.println("Posizione terzo 0: " + thirdZero);
        
        
        sol[first10] = true;
        sol[second10] = true;
        sol[first9] = true;
        sol[firstZero] = true;
        sol[secondZero] = true;
        sol[thirdZero] = true;
        
        cal1.setSolutions(sol);
        
        System.out.println("New Array: " + Arrays.toString(cal1.getCurrentDisparities(cal1.maxDisparity, cal1.minDisparity)));
    }
	

}
