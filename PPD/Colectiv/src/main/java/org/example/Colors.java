package org.example;

import java.util.HashMap;
import java.util.Map;

public class Colors {
    private final Map<Integer, String> colors = new HashMap<>();

    public Colors(int n) {

        for (int i = 0; i < n; i++) {
            colors.put(i, "color " + i);
        }
    }

    public void addColor(int code, String color) {
        colors.put(code, color);
    }

    public Map<Integer, String> getColors() {
        return colors;
    }

    public Map<Integer, String> getColorsForNodes(int[] nodes) {
        Map<Integer, String> colorsForNodes = new HashMap<>();
        for (int node : nodes) {
            colorsForNodes.put(node, colors.get(node));
        }
        return colorsForNodes;
    }
}
