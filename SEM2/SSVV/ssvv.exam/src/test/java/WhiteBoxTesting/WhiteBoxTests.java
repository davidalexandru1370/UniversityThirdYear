package WhiteBoxTesting;

import org.example.AnimalType;
import org.example.Solution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WhiteBoxTests {

    private Solution solution = new Solution();

    @ParameterizedTest
    @MethodSource("getListOfAnimals")
    public void TestInsertCow_ValidIndex_ShouldModifyListWithCowAdded(List<Integer> animals) {
        for (int index = 1; index < animals.size() - 1; index += 2) {
            solution.InsertCow(animals, index);
            Assertions.assertEquals(animals.get(index), AnimalType.Cow);
        }
    }

    @Test
    public void TestInsertCow_InvalidIndex_ShouldThrowIndexOutOfBoundsException() {

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            solution.InsertCow(new ArrayList<>(), -1);
        });

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            solution.InsertCow(new ArrayList<>(), 100);
        });
    }

    public static Stream<Arguments> getListOfAnimals() {
        return Stream.of(
                Arguments.arguments(new ArrayList<>(List.of(1, 2, 3, 3, 2, 1))),
                Arguments.arguments(new ArrayList<>(List.of(1, 1, 1, 1))),
                Arguments.arguments(new ArrayList<>(List.of(1, 2, 3, 4)))
        );
    }
}
