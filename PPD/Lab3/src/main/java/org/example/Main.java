package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final int NUMBER_OF_THREADS = 4;
        final TaskType TASK_TYPE = TaskType.ColumnByColumn;
        int rowsMatrix1;
        int columnsMatrix1;
        int rowsMatrix2;
        int columnsMatrix2;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of rows of the first matrix: ");
        rowsMatrix1 = scanner.nextInt();
        System.out.println("Enter the number of columns of the first matrix: ");
        columnsMatrix1 = scanner.nextInt();
        System.out.println("Enter the number of rows of the second matrix: ");
        rowsMatrix2 = scanner.nextInt();
        System.out.println("Enter the number of columns of the second matrix: ");
        columnsMatrix2 = scanner.nextInt();

        Matrix matrix1 = new Matrix(rowsMatrix1, columnsMatrix1);
        Matrix matrix2 = new Matrix(rowsMatrix2, columnsMatrix2);

        if (columnsMatrix1 != rowsMatrix2) {
            System.err.println("The number of columns of the first matrix must be equal to the number of rows of the second matrix!");
            return;
        }

        matrix1.setRandomValues();
        matrix2.setRandomValues();

        Matrix resultNormal = new Matrix(rowsMatrix1, columnsMatrix2);
        Matrix resultThreadPool = new Matrix(rowsMatrix1, columnsMatrix2);

        float startTime = System.nanoTime();
        resultNormal = ThreadStrategy.run(matrix1, matrix2, NUMBER_OF_THREADS, TASK_TYPE);
        float endTime = System.nanoTime();
        System.out.println("Time for normal threads: " + (endTime - startTime) / 1000000 + " ms");

        startTime = System.nanoTime();
        resultThreadPool = ThreadPoolStrategy.run(matrix1, matrix2, NUMBER_OF_THREADS, TASK_TYPE);
        endTime = System.nanoTime();
        resultNormal = ThreadStrategy.run(matrix1, matrix2, NUMBER_OF_THREADS, TASK_TYPE);
        System.out.println("Time for thread pool: " + (endTime - startTime) / 1000000 + " ms");

        System.out.println("Matrix 1: ");
        System.out.println(matrix1);
        System.out.println("Matrix 2: ");
        System.out.println(matrix2);
        System.out.println("Result: ");
        System.out.println(resultNormal.equals(resultThreadPool));
    }

}