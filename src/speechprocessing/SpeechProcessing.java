/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speechprocessing;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NguyenVanDung
 */
public class SpeechProcessing {

    public static String fName = "C:\\Users\\NguyenVanDung\\Documents\\GitHub\\SpeechProcessing\\a96.wav";
    public static String foutName = "C:\\Users\\NguyenVanDung\\Documents\\GitHub\\SpeechProcessing\\a97.wav";
    //Trong bài này file wav có tốc độ lấy mẫu là 10000 mẫu/s.Xét cửa sổ 20ms và xét độ dịch 10ms
    public static int N = 100; //Số mẫu được xét trong một cửa sổ 
    public static int T = 100; //Độ dịch của cửa sổ
    public static int P = 12; //Xấp xỉ một mẫu tín hiệu bằng P mẫu trước đó p khoảng 12 đến 18
    public static int count = 3450;
    public static float Coefficient[][]; //Mảng chứa giá trị các bộ trọng số lấy ra từ các cửa sổ
    public static float Error[][];//Mảng chứa sai số
    public static int numChannels;
    public static long numFrames;
    public static int validBits;
    public static long sampleRate;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        try {
//            FileInputStream stream = new FileInputStream(new File(fName));
//            byte[] data = new byte[count];
//            for (int i = 0; i < count; i++) {
//                data[i] = 0;
//            }
//            stream.read(data, 0, count);
//
//            for (int i = 0; i < count; i++) {
//                System.out.println(data[i]);
//
//            }
//        } catch (Exception ex) {
//            System.out.println(ex.toString());
//
//        }
//        float[] input = new float[N];
//        for (int i = 0; i < N; i++) {
//            input[i] = 1;
//        }
//        float[] output = new float[N];
//        SpeechProcessing sp = new SpeechProcessing();
//        output = sp.Hamming(input);
//        for (int i = 0; i < N; i++) {
//            System.out.print(output[i] + " ");
//        
        SpeechProcessing spe = new SpeechProcessing();
        spe.extractWavFile();
        spe.restoreWav();

    }

    //Hàm Khôi phục lại dữ liệu từ bộ trọng số và bộ sai số tiên đoán
    public void restoreWav() {
        long[] wav = new long[Error.length * Error[0].length];
        int count = 0;
        int row = Coefficient.length;
        int p = Coefficient[0].length;
        int col = Error[0].length;
        for (int i = 0; i < row; i++) {
            double[] data = new double[col];
            for (int j = 0; j < col; j++) {
                double tmp = 0;
                for (int t = 0; t < p; t++) {
                    double val = (j - t - 1) >= 0 ? data[j - t - 1] : 0;
                    tmp += val * Coefficient[i][t];
                }
                data[j] = Error[i][j] + tmp;
            }

            //Luu vao mang wav
            for (int t = 0; t < data.length; t++) {
                wav[count++] = (long) Math.round(data[t]); //Cho nay phai sua lai neu chuyen sang double
            }                                              //Neu chuyen sang double phai xem phan scale nhu the nao
        }
        //Luu lai file 
        writeWavFile(wav, numChannels, wav.length, validBits, sampleRate);

        //In gia tri mang wav ra ngoai
        for (int i = 0; i < wav.length; i++) {
            System.out.print(wav[i] + " ");
        }

    }

    //Hàm phân tích file wav lấy ra bộ trọng số a và sai số tiên đoán
    public void extractWavFile() {
        try {
            // Open the wav file and get the feature of the file
            WavFile wavFile = WavFile.openWavFile(new File(fName));
            numChannels = wavFile.getNumChannels();
            numFrames = wavFile.getNumFrames();
            validBits = wavFile.getValidBits();
            sampleRate = wavFile.getSampleRate();

            long num = wavFile.getNumFrames();
            long[] raw = new long[(int) num];
            Coefficient = new float[(int) (num / T + 1)][(int) P];
            Error = new float[(int) (num / T + 1)][(int) N];
            wavFile.readFrames(raw, (int) num);
            for (int i = 0; i <= num / T; i++) {
                long[] data;
                //Lấy  một đoạn dữ liệu độ dài N và cách nhau khoảng T
                if ((num - i * T) >= N) {
                    data = Arrays.copyOfRange(raw, i * T, i * T + N);
                } else {
                    data = Arrays.copyOfRange(raw, (int) (num - N), (int) num);
                }

                //convert mang long sang mang float
                float[] data1 = new float[data.length];
                for (int t = 0; t < data.length; t++) {
                    data1[t] = (float) data[t];
                }

                for (int t = 0; t < data1.length; t++) {
                    System.out.print(data1[t] + "     ");
                }
                System.out.println("");
                //lay ket qua cac he so
                float[] result = getCoefficient(data1);
                //Luu vao trong mang chua cac he so
                for (int t = 0; t < result.length; t++) {
                    Coefficient[i][t] = result[t];
                }
                //In ket qua cac he so
                for (int k = 0; k < result.length; k++) {
                    System.out.print(result[k] + "     ");
                }
                System.out.println("");

                //Tinh sai so tien doan
                float[] error = new float[data.length];
                for (int t = 0; t < error.length; t++) {
                    float tmp = 0;
                    for (int k = 0; k < P; k++) {
                        float val = (t - k - 1) >= 0 ? (float) data[t - k - 1] : (float) 0;
                        tmp += val * result[k];
                    }
                    error[t] = (float) data[t] - tmp;
                }
                //Luu sai so tien doan
                for (int t = 0; t < data.length; t++) {
                    Error[i][t] = error[t];
                }
                //In sai so du doan
                for (int t = 0; t < error.length; t++) {
                    System.out.print(error[t] + "     ");
                }
                System.out.println("");
                System.out.println("");
            }
            wavFile.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    //Hàm tổng hợp tiếng nói từ bộ dữ liệu đã biết gồm các bộ trọng số và sai số tiên đoán
    public void writeWavFile(long[] data, int numChannels, long numFrames, int validBits, long sampleRate) {
        try {
            WavFile wavFile = WavFile.newWavFile(new File(foutName), numChannels, numFrames, validBits, sampleRate);
            wavFile.writeFrames(data, data.length);
            wavFile.close();
        } catch (IOException ex) {
            Logger.getLogger(SpeechProcessing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WavFileException ex) {
            Logger.getLogger(SpeechProcessing.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Hàm lấy bộ trọng số trong một khung cửa sổ
    public float[] getCoefficient(float[] data) {
        float[] value = Hamming(data);
        float[][] matrix = new float[P][P];
        float[] Y = new float[P];
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < P; j++) {
                matrix[i][j] = Autocorrelation(value, Math.abs(i - j));
            }
        }

        for (int i = 0; i < P; i++) {
            Y[i] = Autocorrelation(value, i + 1);
        }

        LevinsonRecursion le = new LevinsonRecursion(matrix, Y, P);
        return le.getResult();
    }

    //Hàm cài đặt cửa sổ Hamming
    private float[] Hamming(float[] in) {
        float alpha = (float) 0.54;
        float beta = (float) 0.46;
        float[] out = new float[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = (float) (in[i] * (alpha - beta * Math.cos(((2 * Math.PI * i) / (N - 1)))));
        }
        return out;
    }

    //Hàm cài đặt hàm tính giá trị tự tương quan
    private float Autocorrelation(float[] data, int k) {
        float result = 0;
        for (int i = 0; i < data.length - k; i++) {
            result += data[i] * data[i + k];
        }
        return result;
    }

}
