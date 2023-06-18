package com.example.stereoacuitymultitestapp.utils;

import com.example.stereoacuitymultitestapp.R;

import p3d4amb.sat.lib.shapes.ImageShape;
import p3d4amb.sat.lib.shapes.ShapeSize;

public class DefaultValues {

    /** Test parameters */
    public static final int CHOICES = 4;
    public static final int NUMBER_IMAGES = 6;
    public static final float THRESHOLD = 0.7f;

    /**
     * SPSETTINGS
     */
    public static final String SPSETTINGS = "SPSettings";
    public static final String PREF_DISTANCE = "pref_distance";
    public static final String PREF_MAXDISPARITY = "pref_maxdisparity";
    public static final String PREF_MINDISPARITY = "pref_mindisparity";
    public static final String PREF_IMAGESET = "pref_imageset";
    public static final int DEFAULT_MAXDISPARITY = 12;
    public static final int DEFAULT_MINDISPARITY = 1;
    public static final int DEFAULT_DISTANCE = 30;
    public static final String CHOSEN_IMAGESET = "chosen_imageset"; // nome dell'imageset scelto
    public static final String CHOSEN_IMAGE = "chosen_image";  // nome dell'immagine scelta
    public static final String RID_CHOSEN_IMAGE = "rid_chosen_image";
    public static final String POSITION_IMAGE_FOR_QUIZ = "position_image";   // Posizione immagine in array
    public static final String USERNAME_TEST = "username_test";
    public static final String CERTIFIED_MAX_DISPARITY = "result_disparity";   // salva il risultato del test
    public static final String SEVERITY = "severity";
    public static final boolean DEFAULT_SEVERITY = false;

    public static final ImageShape.ImageSet DEFAULT_IMAGESET = ImageShape.ImageSet.LANG;
    public static final int DEFAULT_RID_IMAGE_TEST = R.drawable.btn_lang_bird;

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
     * SPDOCTOR
     */
    public static final String SPDOCTOR = "SPDoctor";
    public static final String ACTUAL_DOCTOR_MAIL = "actual_doctor_mail";
    public static final String ACTUAL_DOCTOR_PASSWORD = "actual_doctor_password";
    public static final String ACTUAL_DOCTOR_NAME = "actual_doctor_name";
    public static final String ACTUAL_DOCTOR_SURNAME = "actual_doctor_surname";

    /**
     * SPPATIENT
     */
    public static final String SPPATIENT = "SPPatient";
    public static final String ACTUAL_PATIENT_NAME = "actual_patient_name";
    public static final String ACTUAL_PATIENT_SURNAME = "actual_patient_surname";
    public static final String ACTUAL_PATIENT_ID = "actual_patient_id";

    /**
     * Servlet
     */
    public static final String AUTHORITY = "se4med.unibg.it"; // Local: 192.168.137.1:9997
    public static final String STEREOTEST = "StereoTest";

    public static final String LOGIN_OK = "login_ok";
    public static final String STATUS_OK = "status_ok";

    public static final String ACTION_PARAM_NAME = "action";
    public static final String EMAIL_PARAM = "useremail";
    public static final String IDAPP_PARAM = "idapp";
    public static final String IDPATIENT_PARAM = "idpatient";
    public static final String NAMEPAT_PARAM = "namepat";
    public static final String PASSWORD_PARAM = "password";
    public static final String SURNAMEPAT_PARAM = "surnamepat";
    public static final String DATEANDTIME_PARAM = "dateandtime";

    public static final String AUTHENTICATE_DOCTOR_NS_ACTION = "authenticatedoctorns";
    public static final String GETPATIENTDOCLIST_ACTION = "getpatientdoclist";
    public static final String CREATEPATIENTDOC_ACTION = "createpatientdoc";
    public static final String DELETEPATIENTDOC_ACTION = "deletepatientdoc";

    /**
     * Other values
     */
    public static final String SESSION_DATA = "sessiondata";
    public static final String FINALANGLERESULT = "finalangleresult";
    public static final String FOLDER_RESULTS = "multisattest/results";
    public static final String FILE_RESULTS_NAME = "multitestresults";

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
                texts[1] = "Pacman a sinistra";
                texts[2] = "Pacman a destra";
                texts[3] = "Pacman verso l'alto";
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