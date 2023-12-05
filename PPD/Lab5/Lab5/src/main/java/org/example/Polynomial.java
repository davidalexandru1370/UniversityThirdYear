package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Polynomial {
    //aX^0 + bX^1 + ... + zX^n
    private ArrayList<Integer> coefficients = new ArrayList<>();
    private int degree;

    public Polynomial(List<Integer> coefficients) {
        this.coefficients = (ArrayList<Integer>) coefficients;
        this.degree = coefficients.size() - 1;
    }

    public Polynomial(int degree, List<Integer> coefficients) {
        this.coefficients = (ArrayList<Integer>) coefficients;
        this.degree = degree;
    }

    public Polynomial(int degree) {
        this.degree = degree;
        this.coefficients = new ArrayList<>(degree + 1);
        this.setRandomCoefficients(degree);
    }

    private void setRandomCoefficients(int degree) {
        for (int i = 0; i <= degree; i++) {
//            this.coefficients[i] = (int) (Math.random() * 100);
            if (i >= coefficients.size()) {
                this.coefficients.add(1);
            } else {
                this.coefficients.set(i, 1);
            }
        }
    }

    public List<Integer> getCoefficients() {
        return coefficients;
    }

    public void setCoefficient(int index, int value) {
        this.coefficients.set(index, value);
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= degree; i++) {
            sb.append(coefficients.get(i));
            if (i != 0) {
                sb.append("x^").append(i);
            }
            if (i != degree) {
                sb.append(" + ");
            }
        }
        return sb.toString();
    }

    public static Polynomial add(final Polynomial p1, final Polynomial p2) {
        int addDegree = Math.max(p1.degree, p2.degree);
        List<Integer> result = new ArrayList<>(Collections.nCopies(addDegree + 1, 0));

        for (int i = 0; i <= p1.degree; i++) {
            result.set(i, p1.coefficients.get(i) + result.get(i));
        }
        for (int i = 0; i <= p2.degree; i++) {
            result.set(i, p2.coefficients.get(i) + result.get(i));
        }

        return new Polynomial(addDegree, result);
    }

    public static Polynomial subtract(final Polynomial p1, final Polynomial p2) {
        int subtractDegree = Math.max(p1.degree, p2.degree);
        List<Integer> result = new ArrayList<>(Collections.nCopies(subtractDegree + 1, 0));
        for (int i = 0; i <= p1.degree; i++) {
            result.set(i, p1.getCoefficients().get(i) + result.get(i));
        }
        for (int i = 0; i <= p2.degree; i++) {
            result.set(i, -p2.getCoefficients().get(i) + result.get(i));
        }

        return new Polynomial(subtractDegree, result);
    }

    public static Polynomial extendLeft(Polynomial p, int size) {
        List<Integer> zeros = new ArrayList<>(Collections.nCopies(size, 0));
        zeros.addAll(p.getCoefficients());
        return new Polynomial(zeros);
    }
}
