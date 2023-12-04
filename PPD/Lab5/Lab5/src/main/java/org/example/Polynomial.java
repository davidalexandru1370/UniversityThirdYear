package org.example;

public class Polynomial {
    //aX^0 + bX^1 + ... + zX^n
    private int[] coefficients;
    private int degree;

    public Polynomial(int degree, int[] coefficients) {
        this.coefficients = coefficients;
        this.degree = degree;
    }

    public Polynomial(int degree) {
        this.degree = degree;
        this.coefficients = new int[degree + 1];
        this.setRandomCoefficients(degree);
    }

    private void setRandomCoefficients(int degree) {
        for (int i = 0; i <= degree; i++) {
//            this.coefficients[i] = (int) (Math.random() * 100);
            this.coefficients[i] = 1;
        }
    }

    public int[] getCoefficients() {
        return coefficients;
    }

    public void setCoefficient(int index, int value) {
        this.coefficients[index] = value;
    }

    public void setCoefficients(int[] coefficients) {
        this.coefficients = coefficients;
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
            sb.append(coefficients[i]);
            if (i != 0) {
                sb.append("x^").append(i);
            }
            if (i != degree) {
                sb.append(" + ");
            }
        }
        return sb.toString();
    }
}
