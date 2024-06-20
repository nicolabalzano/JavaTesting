package Hw2Test;

import net.jqwik.api.*;
import net.jqwik.api.statistics.Histogram;
import net.jqwik.api.statistics.Statistics;
import net.jqwik.api.statistics.StatisticsReport;
import org.example.Hw2.VerifyOrdinatedString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VerifyOrdinatedStringTest {

    static class Utils {
        static class InputObject {
            private final String baseString;
            private final String injectedString;
            private final int firstIndex;

            public InputObject(String string, String ordinatedString, int firstIndex) {
                this.baseString = string;
                this.injectedString = ordinatedString;
                this.firstIndex = firstIndex;
            }

            public String getInjectedString() {
                return injectedString;
            }

            public int getFirstIndex() {
                return firstIndex;
            }

            public String getBaseString() {
                return baseString;
            }

            @Override
            public String toString() {
                return "InputObject{" +
                        "string='" + baseString + '\'' +
                        ", injectedString='" + injectedString + '\'' +
                        ", firstIndex=" + firstIndex +
                        '}';
            }
        }

        static boolean checkStringLength(int firstIndex, int stringLength) {
            //altrimenti  (-2147483648) * -1  lanciava eccezione quando cercava di verificarlo tramite il filter
            long unsignedFirstIndex = Integer.toUnsignedLong(firstIndex);
            long unsignedStringLength = Integer.toUnsignedLong(stringLength);
            return unsignedFirstIndex < unsignedStringLength;
        }

        static boolean checkNotOrderedString(String inputString, int fristIndex) {
            String subStr=null;
            if(fristIndex<=-1)
                subStr = inputString.substring(fristIndex*-1);
            else
                subStr = inputString.substring(fristIndex);
            boolean isOrdered = false, isReverseOrdered = false;
            //check ordered String
            char[] charArray = subStr.toCharArray();
            Arrays.sort(charArray);
            String orderedString = String.valueOf(charArray);
            isOrdered = subStr.equals(orderedString);
            //check reverse ordered String
            isReverseOrdered = subStr.contentEquals(new StringBuilder(orderedString).reverse());
            return !isOrdered && !isReverseOrdered;
        }
    }

    @Nested
    @DisplayName("Unhappy path tests")
    class UnhappyPathTests {
        //ILLEGAL INPUT
        @ParameterizedTest
        @MethodSource("illeagalInputProvider")
        @DisplayName("illegal input/example based")
        void illegalInput(String inputString, int firstIndex, int lastIndex) {
            assertThrows(IllegalArgumentException.class, () -> VerifyOrdinatedString.verifyOrdinatedString(inputString, firstIndex, lastIndex));
        }

        static Stream<Arguments> illeagalInputProvider() {
            return Stream.of(
                    //T1
                    Arguments.of("aaa", 2, 2),
                    //T2
                    Arguments.of("aaa", -2, 2),
                    //T3
                    Arguments.of("aaa", 1, 4),
                    //T4
                    Arguments.of("", 0, 0),
                    //T5
                    Arguments.of(null, 0, 0)
            );
        }
    }

    @Group
    @Label("Happy path tests")
    class HappyPathTests {

        private Arbitrary<String> getArbitraryString(){
            return Arbitraries.strings().withCharRange('!', '~').ofMinLength(0).ofMaxLength(50);
        }

        private Arbitrary<String> getInjectedString(){
            return Arbitraries.strings().withCharRange('!', '~').ofMinLength(1).ofMaxLength(50);
        }

        //T6
        @Provide
        private Arbitrary<Utils.InputObject> evenIntegerProvider() {
            Arbitrary<String> arbitraryString = getArbitraryString();
            Arbitrary<String> injectedString = getInjectedString();
            Arbitrary<Integer> firstIndex = Arbitraries.integers().greaterOrEqual(0).filter(n -> n % 2 == 0);
            return Combinators.combine(arbitraryString, injectedString, firstIndex).as(Utils.InputObject::new)
                    .filter(o -> Utils.checkStringLength(o.getFirstIndex(), o.getBaseString().length()));
        }

        @Label("ordered string with even firstIndex")
        @Property(tries = 1000, edgeCases = EdgeCasesMode.FIRST)
        @Report(Reporting.GENERATED)
        @StatisticsReport(format = Histogram.class)
        void orderedString(@ForAll("evenIntegerProvider") Utils.InputObject o) {
            StringBuilder inputString = new StringBuilder();
            String injectedString = o.getInjectedString();

            // Aggiungi i caratteri dalla stringa originale fino a firstIndex
            inputString.append(o.getBaseString(), 0, o.getFirstIndex());

            // Ordina e Aggiungi il contenuto di manipulatedString
            char[] charArray = injectedString.toCharArray();
            Arrays.sort(charArray);
            injectedString = String.valueOf(charArray);
            inputString.append(injectedString);

            // Aggiungi i caratteri rimanenti dalla stringa originale dopo firstIndex
            inputString.append(o.getBaseString(), o.getFirstIndex(), o.getBaseString().length());
            int lastIndex = o.getFirstIndex() + injectedString.length();

            Statistics.label("firstIndex: ").collect(o.getFirstIndex());

            assertEquals(VerifyOrdinatedString.verifyOrdinatedString(inputString.toString(), o.getFirstIndex(), lastIndex), injectedString);
        }

        //T7
        @Provide
        private Arbitrary<Utils.InputObject> oddIntegerProvider() {
            Arbitrary<String> arbitraryString = getArbitraryString();
            Arbitrary<String> injectedString = getInjectedString();
            Arbitrary<Integer> firstIndex = Arbitraries.integers().greaterOrEqual(0).filter(n -> n % 2 == 1);
            return Combinators.combine(arbitraryString, injectedString, firstIndex).as(Utils.InputObject::new)
                    .filter(o -> Utils.checkStringLength(o.getFirstIndex(), o.getBaseString().length()));
        }

        @Property(tries = 1000, edgeCases = EdgeCasesMode.FIRST)
        @Report(Reporting.GENERATED)
        @Label("descending ordered string with odd firstIndex")
        @StatisticsReport(format = Histogram.class)
        void reverseOrderedString(@ForAll("oddIntegerProvider") Utils.InputObject o) {
            StringBuilder inputString = new StringBuilder();
            String mainpuletedString = o.getInjectedString();

            // Aggiungi i caratteri dalla stringa originale fino a firstIndex
            inputString.append(o.getBaseString(), 0, o.getFirstIndex());

            // Ordina e Aggiungi il contenuto di manipulatedString
            char[] charArray = mainpuletedString.toCharArray();
            Arrays.sort(charArray);
            mainpuletedString = String.valueOf(charArray);
            mainpuletedString = new StringBuilder(String.valueOf(charArray)).reverse().toString();
            inputString.append(mainpuletedString);

            // Aggiungi i caratteri rimanenti dalla stringa originale dopo firstIndex
            inputString.append(o.getBaseString(), o.getFirstIndex(), o.getBaseString().length());
            int lastIndex = o.getFirstIndex() + mainpuletedString.length();

            Statistics.label("firstIndex: ").collect(o.getFirstIndex());

            assertEquals(VerifyOrdinatedString.verifyOrdinatedString(inputString.toString(), o.getFirstIndex(), lastIndex), mainpuletedString);
        }


        //T8
        @Provide
        private Arbitrary<Utils.InputObject> negativeIntegerPalindromeProvider() {
            Arbitrary<String> arbitraryString = getArbitraryString();
            Arbitrary<String> injectedString = getInjectedString();
            Arbitrary<Integer> firstIndex = Arbitraries.integers().lessOrEqual(-1);
            return Combinators.combine(arbitraryString, injectedString, firstIndex).as(Utils.InputObject::new)
                    .filter(o -> Utils.checkStringLength(o.getFirstIndex() * -1, o.getBaseString().length()));
        }

        @Property(tries = 1000, edgeCases = EdgeCasesMode.FIRST)
        @Report(Reporting.GENERATED)
        @Label("even palindrome string with negative firstIndex")
        @StatisticsReport(format = Histogram.class)
        void evenPalindromeString(@ForAll("negativeIntegerPalindromeProvider") Utils.InputObject o) {
            StringBuilder inputString = new StringBuilder();
            String injectedString = o.getInjectedString();
            int firstIndexPositive = o.getFirstIndex() * -1;

            // Aggiungi i caratteri dalla stringa originale fino a firstIndex
            inputString.append(o.getBaseString(), 0, firstIndexPositive);

            //Crea la stringa palindroma e aggiungila alla stringa di input
            String reverseString = new StringBuilder(injectedString).reverse().toString();
            injectedString += reverseString;
            inputString.append(injectedString);

            // Aggiungi i caratteri rimanenti dalla stringa originale dopo firstIndex
            inputString.append(o.getBaseString(), firstIndexPositive, o.getBaseString().length());
            int lastIndex = firstIndexPositive + injectedString.length();

            Statistics.label("firstIndex: ").collect(o.getFirstIndex());

            assertEquals(VerifyOrdinatedString.verifyOrdinatedString(inputString.toString(), o.getFirstIndex(), lastIndex), injectedString);
        }

        //T9
        @Property(tries = 1000, edgeCases = EdgeCasesMode.FIRST)
        @Report(Reporting.GENERATED)
        @Label("odd palindrome string with negative firstIndex")
        @StatisticsReport(format = Histogram.class)
        void oddPalindromeString(@ForAll("negativeIntegerPalindromeProvider") Utils.InputObject o) {
            StringBuilder inputString = new StringBuilder();
            String injectedString = o.getInjectedString();
            int firstIndexPositive = o.getFirstIndex() * -1;

            // Aggiungi i caratteri dalla stringa originale fino a firstIndex
            inputString.append(o.getBaseString(), 0, firstIndexPositive);

            //Crea la stringa palindroma e aggiungila alla stringa di input
            String reverseString = new StringBuilder(injectedString.substring(0, injectedString.length() - 1)).reverse().toString();
            injectedString += reverseString;
            inputString.append(injectedString);

            // Aggiungi i caratteri rimanenti dalla stringa originale dopo firstIndex
            inputString.append(o.getBaseString(), firstIndexPositive, o.getBaseString().length());
            int lastIndex = firstIndexPositive + injectedString.length();

            Statistics.label("firstIndex: ").collect(o.getFirstIndex());

            assertEquals(VerifyOrdinatedString.verifyOrdinatedString(inputString.toString(), o.getFirstIndex(), lastIndex), injectedString);
        }

        //T10
        @Provide
        private Arbitrary<Utils.InputObject> noSpecialSubStringProvider() {
            Arbitrary<String> arbitraryString = Arbitraries.strings().alpha().uniqueChars().ofMinLength(0).ofMaxLength(20);
            Arbitrary<String> injectedString = Arbitraries.strings().ofLength(0);
            Arbitrary<Integer> firstIndex = Arbitraries.integers();
            return Combinators.combine(arbitraryString, injectedString, firstIndex).as(Utils.InputObject::new)
                    .filter(o ->
                            (Utils.checkStringLength(o.getFirstIndex(), o.getBaseString().length()) ||
                                    Utils.checkStringLength(o.getFirstIndex()*-1, o.getBaseString().length())
                            ) &&
                                    (Utils.checkNotOrderedString(o.getBaseString(), o.getFirstIndex()))
                    );
        }

        @Property(tries = 1000, edgeCases = EdgeCasesMode.FIRST)
        @Report(Reporting.GENERATED)
        @Label("other cases, no ordered or palindrome string")
        @StatisticsReport(format = Histogram.class)
        void noSpecialSubString(@ForAll("noSpecialSubStringProvider") Utils.InputObject o) {
            Statistics.label("firstIndex: ").collect(o.getFirstIndex());

            assertEquals(VerifyOrdinatedString.verifyOrdinatedString(o.getBaseString(), o.getFirstIndex(), o.getBaseString().length()), o.getBaseString());
        }

    }
}
