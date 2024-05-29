package BlackBoxTesting;

import org.example.AnimalType;
import org.example.Pair;
import org.example.Solution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

public class BlackBoxTests {
    private Solution solution = new Solution();

    @ParameterizedTest
    @MethodSource("getListsWithNoConflictPairs")
    public void TestFindPair_NotFoundPair_ShouldReturnEmpty(List<Integer> animals) {
        var result = solution.FindPair(animals);

        Assertions.assertEquals(result.isPresent(), false);
    }

    @ParameterizedTest
    @MethodSource("getListsWithConflictsNeighbours")
    public void TestFindPair_FoundPair_ShouldReturnFirstPair(Pair<List<Integer>, Pair<Integer, Integer>> animals) {
        var result = solution.FindPair(animals.getFirst());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(result.get(), animals.getSecond());
    }

    private static Stream<Arguments> getListsWithConflictsNeighbours() {
        return Stream.of(
                Arguments.arguments(new Pair<List<Integer>, Pair<Integer, Integer>>(
                        List.of(1, 2, 3),
                        new Pair<>(0, 1))),
                Arguments.arguments(new Pair<List<Integer>, Pair<Integer, Integer>>(
                        List.of(2, 3, 3, 2),
                        new Pair<>(0, 1))),
                Arguments.arguments(new Pair<List<Integer>, Pair<Integer, Integer>>(
                        List.of(2, 1, 2),
                        new Pair<>(0, 1)))
        );
    }

    private static Stream<Arguments> getListsWithNoConflictPairs() {
        return Stream.of(
                Arguments.arguments(List.of()),
                Arguments.arguments(List.of(AnimalType.Mouse)),
                Arguments.arguments(List.of(AnimalType.Cat)),
                Arguments.arguments(List.of(AnimalType.Dog)),
                Arguments.arguments(List.of(AnimalType.Cow)),
                Arguments.arguments(List.of(1, 1, 1, 1)),
                Arguments.arguments(List.of(2, 2, 2, 2)),
                Arguments.arguments(List.of(3, 3, 3, 3)),
                Arguments.arguments(List.of(4, 4, 4, 4)),
                Arguments.arguments(List.of(1, 4, 3, 4, 2, 4, 1, 4))
        );
    }
}
