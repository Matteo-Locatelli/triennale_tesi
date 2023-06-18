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
		System.out.print("Inserire la massima disparità: ");
		maxDisparity = input.nextInt();
		
		disparities = new int[6];
		
		disparities = setDisparities(minDisparity, maxDisparity);
		
		continueTest();
		
	}
	
	public static int[] setDisparities(int min, int max){
        int [] disp = new int[6];

        disp[0] = max;  // primo ha disparità massima
        disp[5] = min;  // ultimo ha disparità minima

        // ora bisogna calcolare le 4 disparità degli altri SingleTest
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
		
		System.out.print("Inserire il valore di disparità visto: ");
		currDisparity = input.nextInt();
		System.out.println("Disparità corrente: " + currDisparity);
		
		// si presuppone che la disparità inserita sia presente nel vettore: nell'applicazione tale 
		// disparità viene presa con un ciclo for facendo scorrere il vettore e le checkbox selezionate
		
		
        // 2 metodi per prendere la minima e massima disparità tra quelle dei SingleTest selezionati
        int min = getMinDisparity();
        int max = getMaxDisparity();
        System.out.println("Minimo: " + min + "    Massimo: " + max);
        
        if (min == max){    // se uguali vai al risultato
            System.out.print("Risultato: " + max);
            
        } else if (min == max-1){   // se differenziano di un'unità (differenza = 0.166666)
            // metà hanno disparità max e metà hanno disparità min
        	disparities = setDisparities(min, max);
        	showDisparities();
            
            // parte di debug che servirà per capire se tutte le immagini con la stessa disparità sono viste
            int numChecked = 0;
            numChecked = checkCasuality(min, max);
            System.out.println("NumChecked: " + numChecked);
            
            /** dato numChecked guardo i suoi possibili valori studiati su carta */
            
            if(numChecked == 4){	// 4 e non 3 per via del metodo Math.round che approssima i decimali dopo 0.5 all'intero successivo
                System.out.print("Risultato: " + max);
            } else if (numChecked == 6){	// tutte le immagini sono viste
                System.out.print("Risultato: " + min);
            } else {	// o nessuna immagine è vista oppure ne sono state viste alcune (ma non tutte) con la stessa disparità, quindi riparte il test
            	setDisparities(max++, maxDisparity);
            	continueTest();
            }

        } else {    // se diversi calcola nuove disparità e ripeti il test
            int newMin = getMinDisparity();
            int newMax = getMaxDisparity();
            System.out.println("Nuovo Minimo: " + newMin + "    Nuovo Massimo: " + newMax);
            disparities = setDisparities(newMin, newMax);
            continueTest();
        }
    }
	

	private static int getMaxDisparity() {
        int max = maxDisparity;
        // parto da tests[0] e modifico max se il checkbox è cliccato
        // per questa classe di esempio modifico max se è maggiore o uguale alla disparità selezionata
        // il ciclo servirà nell'applicazione per scorrere le varie checkbox
        for (int i=0; i<6; i++){
            if(currDisparity <= disparities[i]){	// finche currDisparity è minore del valore nel vettore
            	max = disparities[i];
            }
        }
        return max;
    }

    private static int getMinDisparity() {
        int min = minDisparity;
        // parto da tests[5] e modifico min se il checkbox non è cliccato
        // per questa classe di esempio modifico min se è minore della disparità selezionata
        for (int i=0; i<6; i++){
            if( currDisparity > disparities[i] ){	// appena disparities[i] diventa minore di currDisparity 
            	min = disparities[i];	// prelevo il primo valore del vettore
                break;
            }
        }
        return min;
    }
    
    public static void showDisparities(){
    	System.out.println("Disparità: " + disparities[0] + " " + disparities[1] + " " + disparities[2] + " " + 
				disparities[3] + " " + disparities[4] + " " + disparities[5] + " ");
    }
    
    
    /** questo metodo calcola quante immagini con la stessa disparità sono presenti nel vettore */ 
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
