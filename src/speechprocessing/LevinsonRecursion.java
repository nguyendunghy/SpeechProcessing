/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speechprocessing;

import java.util.ArrayList;

/**
 * Chức năng của lớp này là cài đặt thuật giải Levinson recursion để giải phương
 * trình T.x=y với x,y là các vector và T là matrix toeplitz
 *
 * @author NguyenVanDung
 */
public class LevinsonRecursion {

    private float[][] matrix;  // matrix toeplitz
    private float[] Y;         // vector Y
    private int N;             // chieu cua vetor X,Y

    public LevinsonRecursion(float[][] matrix, float[] Y, int N) {
        if (Y.length != N || matrix.length * matrix[0].length != N * N) {
            System.out.println("Input error , error in matrix or vector length");
        } else {
            this.matrix = matrix;
            this.Y = Y;
            this.N = N;
        }
    }

    //Ham kiem tra xem matrix co phai la matrix toeplitiz hay khong
    private boolean checkMatrix() {
        /*Thuat toan
         *kiem tra cac duong cac phan tu cua duong cheo cua nua tren matran
         *ta so sanh voi phan tu dau tien cua duong cheo lam tren hang dau tien
         ************************************************************************       
         *kiem tra cac phan tu cua duong cheo cua nua duoi ma tran
         *ta so sanh voi cac phan tu cuoi cung cua duong treo lam tren hang cuoi cung
         *cua ma tran*/
        for (int i = 0; i < N; i++) {
            int j;
            int t;
            for (j = 0, t = i; j < N && t < N; j++, t++) {
                if (matrix[j][t] != matrix[0][i]) {
                    return false;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            int j;
            int t;
            for (j = N - 1, t = i; j >= 0 && t >= 0; j--, t--) {
                if (matrix[j][t] != matrix[N - 1][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    public float[] getResult() {
        float Ef;
        float Eb;
        float Ex;
        ArrayList<Float> F = new ArrayList<>();
        ArrayList<Float> B = new ArrayList<>();
        ArrayList<Float> XX = new ArrayList<>();

        ArrayList<Float> tempF = new ArrayList<>();
        ArrayList<Float> tempB = new ArrayList<>();
        ArrayList<Float> tempX = new ArrayList<>();

        //Khoi tao, hay f,B deu bang 1/matrix[0][0] voi gia thiet cac
        //Khoi tao mang X bang Y[0]/matrix[0][0]
        //phan tu tren duong cheo chinh khac 0
        if (matrix[0][0] == 0) {
            System.out.println("The main diagonal equal 0");
            return null;
        }

        F.add(1 / matrix[0][0]);
        B.add((1 / matrix[0][0]));
        XX.add((Y[0] / matrix[0][0]));

        for (int n = 2; n <= N; n++) {

            //Them 0 vao dau vector F
            tempF.clear();
            tempF.addAll(F);
            tempF.add((float) 0);
            //Them 0 vao duoi vector B
            tempB.clear();
            tempB.add((float) 0);
            tempB.addAll(B);
            //Them 0 vao duoi vector X
            tempX.clear();
            tempX.addAll(XX);
            tempX.add((float) 0);

            // tinh Ef
            Ef = 0;
            for (int i = 0; i < n - 1; i++) {
                Ef += matrix[n - 1][i] * tempF.get(i);
            }
            //tinh Eb
            Eb = 0;
            for (int i = 1; i < n; i++) {
                Eb += matrix[0][i] * tempB.get(i);
            }
            //Tinh Ex
            Ex = 0;
            for (int i = 0; i < n - 1; i++) {
                Ex += matrix[n - 1][i] * tempX.get(i);
            }

            F.clear();
            B.clear();
            XX.clear();
            //Tao vector Fn va Vector Bn 
            for (int i = 0; i < n; i++) {
                float valF = (1 / (1 - Ef * Eb)) * tempF.get(i) - (Ef / (1 - Ef * Eb)) * tempB.get(i);
                F.add(valF);

                float valB = (1 / (1 - Ef * Eb)) * tempB.get(i) - (Eb / (1 - Ef * Eb)) * tempF.get(i);
                B.add(valB);
            }

            //Tao vector Xn
            for (int i = 0; i < n; i++) {
                float valX = tempX.get(i) + (Y[n - 1] - Ex) * B.get(i);
                XX.add(valX);
            }

        }
        //convert sang mang mot chieu
        float[] result = new float[N];
        for (int i = 0; i < N; i++) {
            result[i] = XX.get(i);
        }
        return result;
    }

    public float[] checkResult(float[] X) {
        float[] YY = new float[N];
        for (float ele : YY) {
            ele = 0;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                YY[i] += matrix[i][j] * X[j];
            }
        }
        return YY;
    }

    public static void main(String[] args) {
        float[][] matrix = {
            new float[]{1, 2, 3, 4, 5},
            new float[]{6, 1, 2, 3, 4},
            new float[]{11, 6, 1, 2, 3},
            new float[]{22, 11, 6, 1, 2},
            new float[]{21, 22, 11, 6, 1}};

        float[] X = {1, 2, 3, 4, 5};
        float[] Y = {16, 17, 18, 19, 100};
        LevinsonRecursion le = new LevinsonRecursion(matrix, Y, 5);
        for (int i = 0;
                i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(le.matrix[i][j] + "  ");
            }
        }
        System.out.println(le.checkMatrix());

        float[] result = le.checkResult(le.getResult());
        for (int i = 0; i < le.N; i++) {
            System.out.print(result[i] + " ");
        }

    }

}
