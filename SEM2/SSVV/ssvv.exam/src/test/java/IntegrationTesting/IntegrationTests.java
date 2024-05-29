package IntegrationTesting;

import org.example.AnimalType;
import org.example.Pair;
import org.example.Solution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

public class IntegrationTests {
    private Solution solution = new Solution();


    @ParameterizedTest
    @MethodSource("conflictBetweenNeighboursArguments")
    public void TestBeFriends_ConflictInput_ShouldReturnAnimalsWithCowsBetween(Pair<List<Integer>, List<Integer>> pair) {
        List<Integer> animals = pair.getFirst();
        var expectedAnimals = pair.getSecond();

        var result = solution.BeFriends(animals);
        Assertions.assertEquals(expectedAnimals.size(), result.size());
        for (int index = 0; index < expectedAnimals.size(); index++) {
            Assertions.assertEquals(expectedAnimals.get(index), result.get(index));
        }
    }

    @ParameterizedTest
    @MethodSource("noConflictBetweenNeighboursArguments")
    public void TestBeFriends_NoConflictInInput_ShouldReturnTheSameList(List<Integer> animals) {
        var result = solution.BeFriends(animals);
        Assertions.assertEquals(result.size(), animals.size());

        for (int index = 0; index < result.size(); index++) {
            Assertions.assertEquals(result.get(index), animals.get(index));
        }
    }

    @ParameterizedTest
    @MethodSource("sameResultArguments")
    public void TestBeFriends_EmptyOrOneElementInInput_ShouldReturnTheSameInputAsOutput(List<Integer> animals) {
        var result = solution.BeFriends(animals);

        Assertions.assertEquals(animals.size(), result.size());
        Assertions.assertEquals(animals, result);
    }

    private static Stream<Arguments> sameResultArguments() {
        return Stream.of(
                Arguments.arguments(List.of(AnimalType.Cat)),
                Arguments.arguments(List.of(AnimalType.Dog)),
                Arguments.arguments(List.of(AnimalType.Mouse)),
                Arguments.arguments(List.of(AnimalType.Cow)),
                Arguments.arguments(List.of())
        );
    }

    private static Stream<Arguments> noConflictBetweenNeighboursArguments() {
        return Stream.of(
                Arguments.arguments(List.of(1, 4, 2, 4, 3, 4, 3, 4, 2, 4, 1, 1, 4, 2, 4, 3, 1)),
                Arguments.arguments(List.of(4, 4, 4, 4, 4)),
                Arguments.arguments(List.of(1, 1, 1, 1)),
                Arguments.arguments(List.of(2, 2, 2, 2)),
                Arguments.arguments(List.of(3, 3, 3, 3, 3)),
                Arguments.arguments(List.of(1, 3, 1, 3, 1)),
                Arguments.arguments(List.of(3, 1, 1, 3, 1, 1, 3, 3)),
                Arguments.arguments(List.of(4, 3, 1, 3, 4, 1, 3, 3, 3, 4, 4, 4, 1, 1, 1, 3, 4, 4, 1, 4, 1, 4, 1, 3, 1, 4, 1, 1, 1, 3))
        );
    }

    private static Stream<Arguments> conflictBetweenNeighboursArguments() {
        return Stream.of(
                Arguments.arguments(new Pair<List<Integer>, List<Integer>>(
                        List.of(1, 2),
                        List.of(1, 4, 2))),
                Arguments.arguments(new Pair<List<Integer>, List<Integer>>(
                        List.of(1, 2, 3, 4),
                        List.of(1, 4, 2, 4, 3, 4))),
                Arguments.arguments(new Pair<List<Integer>, List<Integer>>(
                        List.of(2, 3, 1),
                        List.of(2, 4, 3, 1))),
                Arguments.arguments(new Pair<List<Integer>, List<Integer>>(
                        List.of(1, 3, 2),
                        List.of(1, 3, 4, 2))),
                Arguments.arguments(new Pair<List<Integer>, List<Integer>>(
                        List.of(1, 2, 3, 2, 1),
                        List.of(1, 4, 2, 4, 3, 4, 2, 4, 1))),
                Arguments.arguments(new Pair<List<Integer>, List<Integer>>(
                        List.of(2, 3, 2),
                        List.of(2, 4, 3, 4, 2))),
                Arguments.arguments(new Pair<List<Integer>, List<Integer>>(
                        List.of(1, 2, 3, 4, 3, 2, 1, 1, 2, 3, 1),
                        List.of(1, 4, 2, 4, 3, 4, 3, 4, 2, 4, 1, 1, 4, 2, 4, 3, 1))));

    }
}
