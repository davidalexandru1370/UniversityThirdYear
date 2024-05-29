package org.example;

import java.util.*;


public class Solution {
    private final Hashtable<Pair<Integer, Integer>, Integer> neighboursPairs = new Hashtable<>();

    public Solution() {
        fillNeighboursPairs();
    }

    public List<Integer> BeFriends(List<Integer> animals) {
        if (animals.size() <= 1) {
            return animals;
        }

        List<Integer> newNeighbours = new LinkedList<>(animals);
        boolean notFoundConflicts = true;

        do {
            notFoundConflicts = true;
            Optional<Pair<Integer, Integer>> foundAnimalsPair = FindPair(newNeighbours);
            if (foundAnimalsPair.isPresent()) {
                notFoundConflicts = false;
                int first = foundAnimalsPair.get().getFirst();
                int second = foundAnimalsPair.get().getSecond();
                InsertCow(newNeighbours, Math.min(first, second) + 1);
            }
        } while (notFoundConflicts == false);

        return newNeighbours;
    }

    public void InsertCow(List<Integer> animals, int position) {
        if(position < 0 || position >= animals.size()){
            throw new IndexOutOfBoundsException();
        }
        animals.add(position, AnimalType.Cow);
    }

    public Optional<Pair<Integer, Integer>> FindPair(List<Integer> animals) {
        for (int index = 0; index < animals.size() - 1; index++) {
            int currentAnimal = animals.get(index);
            int nextAnimal = animals.get(index + 1);
            Pair<Integer, Integer> neighbours = new Pair<>(currentAnimal, nextAnimal);
            Pair<Integer, Integer> neighboursReverse = new Pair<>(nextAnimal, currentAnimal);

            if (neighboursPairs.containsKey(neighbours) || neighboursPairs.containsKey(neighboursReverse)) {
                return Optional.of(new Pair<>(index, index + 1));
            }
        }
        return Optional.empty();
    }

    private void fillNeighboursPairs() {
        neighboursPairs.put(new Pair<>(AnimalType.Mouse, AnimalType.Cat), 1);
        neighboursPairs.put(new Pair<>(AnimalType.Cat, AnimalType.Dog), 1);
    }
}
