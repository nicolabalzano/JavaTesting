package Hw1Test;

import org.example.Hw1.ConcatenateLists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class ConcatenateListsTest {
    @Nested
    class HappyPathTests {
        @ParameterizedTest
        @MethodSource("happyDifferentListsProvider")
        @DisplayName("happy different length between lists")
        void happyDifferentListsOfStrings(List<String> listOfConcatenated, List<String> listLeft, List<String> listRight) {
            assertEquals(listOfConcatenated, ConcatenateLists.concatenateLists(listLeft, listRight));
        }

        static Stream<Arguments> happyDifferentListsProvider() {
            return Stream.of(
                    Arguments.of(null, List.of("sinistra", "sinistra sinistra"), null),            //T1
                    Arguments.of(null, null, List.of("destra", "destra destra")),                    //T2
                    Arguments.of(List.of("destra", "sinistradestra"), List.of("", "sinistra"), List.of("destra", "destra")),  //T3
                    Arguments.of(List.of("sinistradestra", "sinistra"), List.of("sinistra", "sinistra"), List.of("destra", "")),  //T4
                    Arguments.of(List.of("sinistra", "sinistra"), List.of("sinistra", "sinistra"), List.of()),                    //T5
                    Arguments.of(List.of("destra", "destra"), List.of(), List.of("destra", "destra")),                    //T6
                    Arguments.of(List.of("sinistradestra"), List.of("sinistra"), List.of("destra")),                    //T7
                    Arguments.of(List.of("sinistra 1destra", "1 sinistradestra"), List.of("sinistra1", "1sinistra"), List.of("destra", "destra")),                    //T8
                    Arguments.of(List.of("sinistradestra", "sinistradestra"), List.of("sinistra", "sinistra"), List.of("destrano", "des tra")),                    //T9
                    Arguments.of(List.of("sinistradestra", "destra"), List.of("sinistra"), List.of("destra", "destra")),             //T10
                    Arguments.of(List.of("sinistradestra", "sinistra"), List.of("sinistra", "sinistra"), List.of("destra")),                    //T11
                    Arguments.of(List.of("sinistradestradestra"), List.of("sinistra"), List.of("destradestra")),                 //T12
                    Arguments.of(List.of("sinistraDestradestra"), List.of("sinistra "), List.of("destradestra")),  //T13
                    Arguments.of(List.of("1destra", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"), List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"), List.of("destra")), //T14
                    Arguments.of(List.of("sinistra1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"), List.of("sinistra"), List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14")),  //15
                    Arguments.of(List.of("sinistra- 1destra", "sinistra 1 -destra", "sinistra- 1 -destra"), List.of("sinistra-1", "sinistra 1-", "sinistra-1-" ), List.of("destra", "destra", "destra")),  //T18
                    Arguments.of(List.of("sinistra! 1destra", "sinistra 1 !destra", "sinistra! 1 !destra"), List.of("sinistra!1", "sinistra 1!", "sinistra!1!" ), List.of("destra", "destra", "destra")),  //T19
                    Arguments.of(List.of("sinistra@ 1destra", "sinistra 1 @destra", "sinistra@ 1 @destra"), List.of("sinistra@1", "sinistra 1@", "sinistra@1@" ), List.of("destra", "destra", "destra")),  //T20
                    Arguments.of(List.of("sinistradestra", "sinistradestra", "sinistradestra", "sinistradestra"), List.of("sinistra", "sinistra", "sinistra", "sinistra"), List.of("destra no", "no destra", "nodestra", "desnotra")),  //T21
                    Arguments.of(List.of("s 1"+String.valueOf(Integer.MAX_VALUE)+"destra"), List.of("s1"+String.valueOf(Integer.MAX_VALUE)), List.of("destra")),        //T22
                    Arguments.of(List.of("sinistra 1 destra"),List.of("sinistra1 "), List.of("destra")),                     //T23
                    Arguments.of(List.of("sinistra@sinistraSinistradestradestradestra"),List.of("sinistra @sinistra sinistra"), List.of("destradestradestra")),                     //T24
                    Arguments.of(List.of("sinistra1SinistraSinistradestradestradestra"),List.of("sinistra 1sinistra sinistra"), List.of("destradestradestra"))                      //T25
            );
        }




    }
    @Nested
    class UnhappyPathTests{
        @ParameterizedTest
        @MethodSource("unhappyDifferentListsProvider")
        @DisplayName("unhappy different length between lists")
        void unhappyDifferentListsOfStrings(List<String> listOfConcatenated, List<String> listLeft, List<String> listRight) {
            assertEquals(listOfConcatenated, ConcatenateLists.concatenateLists(listLeft, listRight));
        }
        static Stream<Arguments> unhappyDifferentListsProvider() {
            return Stream.of(
                    Arguments.of(null, List.of("sinistra", "sinistra sinistra"), null),            //T1
                    Arguments.of(null, null, List.of("destra", "destra destra"))                    //T2
            );
        }

        @ParameterizedTest
        @MethodSource("illegalArugumentListsProvider")
        @DisplayName("more than max number of strings in lists")
        void moreThanMaxNumberOfStringsInLists(List<String> listLeft, List<String> listRight) {
            assertThrows(IllegalArgumentException.class, () -> ConcatenateLists.concatenateLists(listLeft, listRight));
        }

        public static Stream<Arguments> illegalArugumentListsProvider() {

            return Stream.of(
                    Arguments.of(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"), List.of("destra")),  //T16
                    Arguments.of(List.of("sinistra"), List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"))   //17
            );
        }
    }

}
