package asu.gunma.ui.util;

public class GradeSystem {

    /*
    * Takes in a String array of all possible phonetic similar words, and the speech recognized word
    * Returns a boolean of true or false, if any array word matches the recognized word.
    */
    public static boolean grade(String arr[], String s2) {

        if(s2 == null){
            return false;
        }

        for (int i = 0; i < arr.length; i++) {

            if(arr[i].toLowerCase().equals(s2.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
