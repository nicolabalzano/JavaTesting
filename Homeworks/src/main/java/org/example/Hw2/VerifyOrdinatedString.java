package org.example.Hw2;
/**
 * La classe VerifyOrdinatedString, è stata creata manualmente, fornisce un metodo per verificare ed estrarre sottostringhe in base agli indici specificati.
 */
public class VerifyOrdinatedString {
    /**
     * Verifica ed estrae una sottostringa dalla stringa fornita in base agli indici specificati.
     *
     * @param str            La stringa di input.
     * @param firstIndex L'indice di inizio della sottostringa.
     * @param lastIndex  L'indice di fine della sottostringa.
     * @return La sottostringa verificata.
     * @throws IllegalArgumentException Se l'input è invalido (stringa vuota, null, indici non validi).
     */
    public static String verifyOrdinatedString(String str, int firstIndex, int lastIndex) {
        if (str==null || str.isEmpty() || firstIndex >= lastIndex || firstIndex * -1 >= lastIndex || lastIndex > str.length()) {
            throw new IllegalArgumentException("Input non valido firstIndex:"+firstIndex+", lastIndex: "+lastIndex);
        }

        if (firstIndex < 0 ) {
            // Se firstIndex è un numero negativo
            String subString = str.substring(firstIndex*(-1), lastIndex);
            if(isPalindrome(str, subString))
                return subString;
        } else if (firstIndex % 2 == 0) {
            // Se firstIndex è positivo ed è un numero pari
            String subString=str.substring(firstIndex, lastIndex);
            if(isStringSorted(str, subString))
                return subString;
        } else {
            // Se firstIndex è positivo ed è un numero dispari
            String subString=str.substring(firstIndex, lastIndex);
            if(isStringSortedReverse(str, subString))
                return subString;
        }
        return str;
    }

    /**
     * Verifica se la sottostringa data è ordinata in ordine crescente.
     *
     * @param subStr La sottostringa da verificare.
     * @return True se la sottostringa è ordinata; false altrimenti.
     */
    private static boolean isStringSorted(String str, String subStr) {
        for (int i = 1; i < subStr.length(); i++) {
            if (subStr.charAt(i - 1) > subStr.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se la sottostringa data è ordinata in ordine decrescente.
     *
     * @param subStr La sottostringa da verificare.
     * @return True se la sottostringa è ordinata al contrario; false altrimenti.
     */
    private static boolean isStringSortedReverse(String str, String subStr) {
        for (int i = 1; i < subStr.length(); i++) {
            if (subStr.charAt(i - 1) < subStr.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se la sottostringa data è un palindromo.
     *
     * @param subStr La sottostringa da verificare.
     * @return True se la sottostringa è un palindromo; false altrimenti.
     */
    private static boolean isPalindrome(String str, String subStr) {
        return subStr.contentEquals(new StringBuilder(subStr).reverse());
    }
}
