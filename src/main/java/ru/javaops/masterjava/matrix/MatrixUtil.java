package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        if (matrixA.length != matrixA[1].length || matrixA.length != matrixB[1].length) {
            throw new IllegalArgumentException("Matrix must be same size and square");
        }
        class ColumnOfMyltiplyMatrix {
            private final int columnIndex;
            private final int[] columnResults;

            private ColumnOfMyltiplyMatrix(int columnIndex, int[] result) {
                this.columnIndex = columnIndex;
                this.columnResults = result;
            }

            public int getColumnIndex() {
                return columnIndex;
            }

            public int[] getColumnResults() {
                return columnResults;
            }
        }
        final CompletionService<ColumnOfMyltiplyMatrix> completionService = new ExecutorCompletionService<>(executor);

        for (int j = 0; j < matrixSize; j++) {
            int[] thatColumn = transposition(matrixB, j);
            int finalJ = j;
            completionService.submit(() -> {
                final int[] resultColumn = new int[matrixSize];
                for (int i = 0; i < matrixSize; i++) {
                    int sum = 0;
                    final int[] thisRow = matrixA[i];
                    for (int k = 0; k < matrixSize; k++) {
                        sum += thisRow[k] * thatColumn[k];
                    }
                    resultColumn[i] = sum;
                }
                return new ColumnOfMyltiplyMatrix(finalJ, resultColumn);
            });
        }
        for (int i = 0; i < matrixSize; i++) {
            ColumnOfMyltiplyMatrix columnOfMyltiplyMatrix = completionService.take().get();
            for (int j = 0; j < matrixSize; j++) {
                matrixC[j][columnOfMyltiplyMatrix.getColumnIndex()] = columnOfMyltiplyMatrix.getColumnResults()[j];
            }
        }
        return matrixC;
    }

    private static int[] transposition(int[][] matrix, int columbIndex) {
        final int matrixSize = matrix.length;
        final int[] result = new int[matrixSize];
        for (int k = 0; k < matrixSize; k++) {
            result[k] = matrix[k][columbIndex];
        }
        return result;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        int[] thatColumn = new int[matrixSize];
        try {
            for (int j = 0; ; j++) {
                for (int k = 0; k < matrixSize; k++) {
                    thatColumn[k] = matrixB[k][j];
                }

                for (int i = 0; i < matrixSize; i++) {
                    int[] thisRow = matrixA[i];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += thisRow[k] * thatColumn[k];
                    }
                    matrixC[i][j] = sum;
                }
            }
        } catch (IndexOutOfBoundsException ignored) {
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
