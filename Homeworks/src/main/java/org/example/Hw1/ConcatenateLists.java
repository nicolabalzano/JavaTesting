package org.example.Hw1;

import java.util.ArrayList;
import java.util.List;

public class ConcatenateLists {

    private static final int N_MAX =14;

    /**
     * Concatena le stringhe nella stessa posizione tra due liste di stringhe,
     * rispettando i seguenti vincoli:
     * Se una delle liste è nulla, restituisce null.
     * Se le liste non hanno la stessa dimensione tratta le stringhe che non sono presenti come empty
     * Rimuove gli spazi e la parola "no" dalle stringhe della seconda lista.
     * Nelle stringhe della prima lista, se un numero è attaccato a una parola senza spazio, aggiunge uno spazio.
     * Se la lunghezza della stringa risultante è superiore a 20 caratteri, la trasforma in camel case se incontra
     * uno spazio tra stringhe nella stringa concatenata
     * Se una delle due liste sono > di N_MAX lancia eccezione
     * @param listOfLeftStrings  La prima lista di stringhe da concatenare.
     * @param listOfRightStrings La seconda lista di stringhe da concatenare.
     * @return Una lista di stringhe ottenuta concatenando le stringhe nelle stesse posizioni
     * tra le due liste, seguendo i vincoli specificati.
     */
    public static List<String> concatenateLists(List<String> listOfLeftStrings, List<String> listOfRightStrings) {

        // Verifica se una delle liste è nulla
        if (listOfLeftStrings == null || listOfRightStrings == null) {
            return null;
        }

        List<String> listLeft= new ArrayList<>(listOfLeftStrings);
        List<String> listRight= new ArrayList<>(listOfRightStrings);

        // Verifica se le liste hanno lunghezza maggiore di n
        if (listLeft.size() > N_MAX || listRight.size() > N_MAX) {
            throw new IllegalArgumentException("Le liste non possono avere dimensione maggiore di " + N_MAX);
        }

        int max=addEmptyStringsToMinorList(listLeft, listRight);
        List<String> listOfConcatenatedStrings = new ArrayList<>();

        // Concatena le stringhe nella stessa posizione tra le due liste
        for (int i = 0; i < max; i++) {
            String leftString = addSpaceToNumberInString(listLeft.get(i));
            String rightString = removeSpacesAndNoFromRightString(listRight.get(i));

            String concatenatedString = leftString + rightString;

            // Se la lunghezza della stringa risultante è superiore a 20 caratteri, trasformala in camel case
            if (concatenatedString.length() > 20) {
                concatenatedString = toCamelCase(concatenatedString);
            }

            listOfConcatenatedStrings.add(concatenatedString);
        }

        return listOfConcatenatedStrings;
    }

    /**
     * Aggiunge uno spazio tra una parola e un numero attaccato senza spazio nella stringa di input.
     *
     * @param inputString La stringa di input.
     * @return La stringa di input con uno spazio aggiunto tra una parola e un numero attaccato senza spazio.
     */
    private static String addSpaceToNumberInString(String inputString) {
        StringBuilder stringBuilder = new StringBuilder(inputString);

        for (int j = 0; j < inputString.length(); j++) {
            char currentChar = inputString.charAt(j);

            if(Character.isDigit(currentChar)) {
                if (j>0 && !(Character.isDigit(inputString.charAt(j - 1))) && inputString.charAt(j-1)!=' ') {
                    stringBuilder.insert(j, " ");
                    inputString=stringBuilder.toString();
                    //j++;
                }
                if (j<inputString.length()-1 && !(Character.isDigit(inputString.charAt(j + 1))) && inputString.charAt(j+1)!=' ') {
                    stringBuilder.insert(j + 1, " ");
                    inputString=stringBuilder.toString();
                }
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Rimuove gli spazi e la parola "no" dalla stringa di input.
     *
     * @param inputString La stringa di input.
     * @return La stringa di input senza spazi e senza la parola "no".
     */
    private static String removeSpacesAndNoFromRightString(String inputString) {
        // Rimuove gli spazi e la parola "no"
        return inputString.replaceAll("\\s+|no", "");
    }

    /**
     * Trasforma una stringa in camel case.
     *
     * @param inputString La stringa di input.
     * @return La stringa di input trasformata in camel case.
     */
    private static String toCamelCase(String inputString) {
        String[] words = inputString.split("\\s+");
        StringBuilder camelCaseString = new StringBuilder(words[0].toLowerCase());

        for (int i = 1; i < words.length; i++) {
            camelCaseString.append(words[i].substring(0, 1).toUpperCase()).append(words[i].substring(1).toLowerCase());
        }

        return camelCaseString.toString();
    }


    private static int addEmptyStringsToMinorList(List<String> listOfLeftStrings, List<String> listOfRightStrings){
        int sizeListLeft=listOfLeftStrings.size();
        int sizeListRight=listOfRightStrings.size();
        int biggerList=2;
        int differenza=0;
        int max=listOfLeftStrings.size();

        //capisci quale è la lista più grande
        if(sizeListRight>sizeListLeft) {
            biggerList = 1;
            max= sizeListRight;
            differenza=max-sizeListLeft;
        }
        else if(sizeListRight<sizeListLeft){
            differenza=max-sizeListRight;
        }

        //aggiunge le stringhe nella lista più corta
        if(biggerList==1){
            for (int i=0; i<differenza; i++)
                listOfLeftStrings.add("");
        }
        else{
            for (int i=0; i<differenza; i++)
                listOfRightStrings.add("");
        }
        return max;
    }
}

