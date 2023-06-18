package com.example.def;


import p3d4amb.sat.lib.shapes.ImageShape;
import p3d4amb.sat.lib.shapes.ShapeSize;

public class DefaultValues {

    //Test parameters
    public static final int DEFAULT_MAXDISPARITY = 10;
    public static final int DEFAULT_MINDISPARITY = 1;
    public static final int DEFAULT_OFFSET = 1;
    public static final int DEFAULT_NCORR_TONEXTLEVE = 1;
    public static final int DEFAULT_NERR_TOSTOPTEST = 3;

    public static final ImageShape.ImageSet DEFAULT_IMAGESET = ImageShape.ImageSet.LETTERS;

    public static final int CHOICES = 4;
    public static final int NUMBER_IMAGES = 6;

    /**
     * Shared Preferences
     */
    public static final String SHARED_PREFS_NAME = "prefs"; // nome delle sharedPreferences
    public static final String CHOSEN_IMAGESET = "chosen_imageset"; // nome dell'imageset scelto
    public static final String DRAWABLE_IMAGE_FOR_QUIZ = "drawable_image_for_quiz";   // R.drawable dell'immagine per il quiz
    public static final String USERNAME_TEST = "username_test";
    public static final String PREF_MAXDISPARITY = "pref_maxdisparity";
    public static final String PREF_MINDISPARITY = "pref_mindisparity";
    public static final String PREF_OFFSET = "pref_offset";
    public static final String PREF_NCORR_TO_NEXTLEVEL = "pref_ncorrtonextlevel";
    public static final String PREF_NERR_TOSTOPTEST = "pref_nerrtostoptest";
    public static final String RESULT_DISPARITY = "result_disparity";   // salva il risultato del test
    public static final String PREF_DISTANCE = "pref_distance";
    public static final int DEFAULT_DISTANCE = 30;

    /**
     * SetColors default values
     */
    public static final String RED_L = "red_l";
    public static final String RED_R = "red_r";
    public static final String GREEN_L = "green_l";
    public static final String GREEN_R = "green_r";
    public static final String BLUE_L = "blue_l";
    public static final String BLUE_R = "blue_r";
    public static final int CURRENT_RED_L = 255;
    public static final int CURRENT_RED_R = 0;
    public static final int CURRENT_GREEN_L = 0;
    public static final int CURRENT_GREEN_R = 0;
    public static final int CURRENT_BLUE_L = 0;
    public static final int CURRENT_BLUE_R = 255;

    /**
     * Static util method that link the string name of an ImageSet to the given ImageSet
     *
     * @param name the name of the ImageSet
     * @return the currespundant ImageSet object
     */
    public static ImageShape.ImageSet stringToImageSet(String name) {
        if (name.equals("LANG")) return ImageShape.ImageSet.LANG;
        if (name.equals("LEA")) return ImageShape.ImageSet.LEA;
        if (name.equals("LEA_CONTORNO")) return ImageShape.ImageSet.LEA_CONTORNO;
        if (name.equals("LETTERS")) return ImageShape.ImageSet.LETTERS;
        if (name.equals("PACMAN")) return ImageShape.ImageSet.PACMAN;
        if (name.equals("TNO")) return ImageShape.ImageSet.TNO;
        return ImageShape.ImageSet.LANG; //default value
    }


    /**
     * Static util method that link the chosen ImageSet to the RIDs of the images of the ImageSet through R.drawable.x
     *
     * @param imageSet the name of the ImageSet
     * @return the RIDs of the chosen imageSet
     */
    public static int[] imageSetToRIDs(ImageShape.ImageSet imageSet) {
        int[] Rids = new int[CHOICES];
        switch (imageSet) {
            case LEA_CONTORNO: {
                Rids[0] = R.drawable.btn_contour_apple;
                Rids[1] = R.drawable.btn_contour_circle;
                Rids[2] = R.drawable.btn_contour_house;
                Rids[3] = R.drawable.btn_contour_square;
                break;
            }
            case LANG: {
                Rids[0] = R.drawable.btn_lang_bird;
                Rids[1] = R.drawable.btn_lang_car;
                Rids[2] = R.drawable.btn_lang_cat;
                Rids[3] = R.drawable.btn_lang_circle;
                break;
            }
            case LEA: {
                Rids[0] = R.drawable.btn_lea_apple;
                Rids[1] = R.drawable.btn_lea_circle;
                Rids[2] = R.drawable.btn_lea_house;
                Rids[3] = R.drawable.btn_lea_square;
                break;
            }
            case TNO: {
                Rids[0] = R.drawable.btn_tno_circle;
                Rids[1] = R.drawable.btn_tno_square;
                Rids[2] = R.drawable.btn_tno_star;
                Rids[3] = R.drawable.btn_tno_triangle;
                break;
            }
            case PACMAN: {
                Rids[0] = R.drawable.btn_pacman_d;
                Rids[1] = R.drawable.btn_pacman_l;
                Rids[2] = R.drawable.btn_pacman_r;
                Rids[3] = R.drawable.btn_pacman_u;
                break;
            }
            case LETTERS: {
                Rids[0] = R.drawable.btn_letter_a;
                Rids[1] = R.drawable.btn_letter_c;
                Rids[2] = R.drawable.btn_letter_e;
                Rids[3] = R.drawable.btn_letter_k;
                break;
            }
        }
        return Rids;
    }


    /**
     * Static util method that link the chosen ImageSet to the RIDs of the images of the ImageSet
     *
     * @param imageSet the name of the ImageSet
     * @return the names of the images of the chosen imageSet
     */
    public static String[] setTextsFromImageSet(ImageShape.ImageSet imageSet) {
        String[] texts = new String[CHOICES];
        switch (imageSet) {
            case LEA_CONTORNO: {
                texts[0] = "Contorno mela";
                texts[1] = "Contorno cerchio";
                texts[2] = "Contorno casa";
                texts[3] = "Contorno quadrato";
                break;
            }
            case LANG: {
                texts[0] = "Uccello";
                texts[1] = "Macchina";
                texts[2] = "Gatto";
                texts[3] = "Cerchio";
                break;
            }
            case LEA: {
                texts[0] = "Mela";
                texts[1] = "Cerchio";
                texts[2] = "Casa";
                texts[3] = "Quadrato";
                break;
            }
            case TNO: {
                texts[0] = "Cerchio";
                texts[1] = "Quadrato";
                texts[2] = "Stella";
                texts[3] = "Triangolo";
                break;
            }
            case PACMAN: {
                texts[0] = "Pacman verso il basso";
                ;
                texts[1] = "Pacman a sinistra";
                ;
                texts[2] = "Pacman a destra";
                ;
                texts[3] = "Pacman verso l'alto";
                ;
                break;
            }
            case LETTERS: {
                texts[0] = "Lettera A";
                texts[1] = "Lettera C";
                texts[2] = "Lettera E";
                texts[3] = "Lettera K";
                break;
            }
        }
        return texts;
    }


    public static ShapeSize getSize(double monitorSize10thInc) {
        if(monitorSize10thInc<=45) return ShapeSize.SMALL; else
        if(monitorSize10thInc<=60) return ShapeSize.MEDIUM; else
            return ShapeSize.BIG;
    }


}