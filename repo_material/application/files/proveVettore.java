package Simulazioni;

import java.util.Scanner;

public class proveVettore {
	
	static Scanner input = new Scanner (System.in);
	
	static int maxDisparity;
	static int minDisparity = 1;
	static int currDisparity;
	
	static int [] disparities;
	
	static float diff;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		int n;
		System.out.print("Inserire la massima disparit�: ");
		maxDisparity = input.nextInt();
		
		disparities = new int[6];
		
		disparities = setDisparities(minDisparity, maxDisparity);
		
		continueTest();
		
	}
	
	public static int[] setDisparities(int min, int max){
        int [] disp = new int[6];

        disp[0] = max;  // primo ha disparit� massima
        disp[5] = min;  // ultimo ha disparit� minima

        // ora bisogna calcolare le 4 disparit� degli altri SingleTest
        diff = (float)(max-min) / 6;
        float floatedDisparity = max;

        for (int i = 1; i<5; i++){
            floatedDisparity = floatedDisparity - diff;
            disp[i] = Math.round(floatedDisparity);
        }
        return disp;
    }
	
	public static void continueTest() {
		showDisparities();
		
		System.out.print("Inserire il valore di disparit� visto: ");
		currDisparity = input.nextInt();
		System.out.println("Disparit� corrente: " + currDisparity);
		
		// si presuppone che la disparit� inserita sia presente nel vettore: nell'applicazione tale 
		// disparit� viene presa con un ciclo for facendo scorrere il vettore e le checkbox selezionate
		
		
        // 2 metodi per prendere la minima e massima disparit� tra quelle dei SingleTest selezionati
        int min = getMinDisparity();
        int max = getMaxDisparity();
        System.out.println("Minimo: " + min + "    Massimo: " + max);
        
        if (min == max){    // se uguali vai al risultato
            System.out.print("Risultato: " + max);
            
        } else if (min == max-1){   // se differenziano di un'unit� (differenza = 0.166666)
            // met� hanno disparit� max e met� hanno disparit� min
        	disparities = setDisparities(min, max);
        	showDisparities();
            
            // parte di debug che servir� per capire se tutte le immagini con la stessa disparit� sono viste
            int numChecked = 0;
            numChecked = checkCasuality(min, max);
            System.out.println("NumChecked: " + numChecked);
            
            /** dato numChecked guardo i suoi possibili valori studiati su carta */
            
            if(numChecked == 4){	// 4 e non 3 per via del metodo Math.round che approssima i decimali dopo 0.5 all'intero successivo
                System.out.print("Risultato: " + max);
            } else if (numChecked == 6){	// tutte le immagini sono viste
                System.out.print("Risultato: " + min);
            } else {	// o nessuna immagine � vista oppure ne sono state viste alcune (ma non tutte) con la stessa disparit�, quindi riparte il test
            	setDisparities(max++, maxDisparity);
            	continueTest();
            }

        } else {    // se diversi calcola nuove disparit� e ripeti il test
            int newMin = getMinDisparity();
            int newMax = getMaxDisparity();
            System.out.println("Nuovo Minimo: " + newMin + "    Nuovo Massimo: " + newMax);
            disparities = setDisparities(newMin, newMax);
            continueTest();
        }
    }
	

	private static int getMaxDisparity() {
        int max = maxDisparity;
        // parto da tests[0] e modifico max se il checkbox � cliccato
        // per questa classe di esempio modifico max se � maggiore o uguale alla disparit� selezionata
        // il ciclo servir� nell'applicazione per scorrere le varie checkbox
        for (int i=0; i<6; i++){
            if(currDisparity <= disparities[i]){	// finche currDisparity � minore del valore nel vettore
            	max = disparities[i];
            }
        }
        return max;
    }

    private static int getMinDisparity() {
        int min = minDisparity;
        // parto da tests[5] e modifico min se il checkbox non � cliccato
        // per questa classe di esempio modifico min se � minore della disparit� selezionata
        for (int i=0; i<6; i++){
            if( currDisparity > disparities[i] ){	// appena disparities[i] diventa minore di currDisparity 
            	min = disparities[i];	// prelevo il primo valore del vettore
                break;
            }
        }
        return min;
    }
    
    public static void showDisparities(){
    	System.out.println("Disparit�: " + disparities[0] + " " + disparities[1] + " " + disparities[2] + " " + 
				disparities[3] + " " + disparities[4] + " " + disparities[5] + " ");
    }
    
    
    /** questo metodo calcola quante immagini con la stessa disparit� sono presenti nel vettore */ 
    private static int checkCasuality(int min, int max) {
    	int x = 0;
    	for(int i=0; i<6; i++) {
    		if(currDisparity == disparities[i]) {
    			x++;
    		} else if (currDisparity == disparities[i]) {
    			x+=2;
    		} else {
    			break;
    		}
    	}
		return x;
	}
    
}
