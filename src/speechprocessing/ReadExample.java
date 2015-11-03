package speechprocessing;

import java.io.*;

public class ReadExample {

    public static String fName = "C:\\Users\\NguyenVanDung\\Documents\\GitHub\\SpeechProcessing\\a96.wav";

    public static void main(String[] args) {
        try {
            // Open the wav file specified as the first argument
            WavFile wavFile = WavFile.openWavFile(new File(fName));

            // Display information about the wav file
            wavFile.display();

            // Get the number of audio channels in the wav file
            int numChannels = wavFile.getNumChannels();

            // Create a buffer of 100 frames
            long[] buffer = new long[(int) wavFile.getNumFrames() * numChannels];

            int framesRead;
            //double min = Double.MAX_VALUE;
            //double max = Double.MIN_VALUE;

//            do {
//                // Read frames into buffer
//                framesRead = wavFile.readFrames(buffer, 100);
//
//                // Loop through frames and look for minimum and maximum value
//                for (int s = 0; s < framesRead * numChannels; s++) {
//                    if (buffer[s] > max) {
//                        max = buffer[s];
//                    }
//                    if (buffer[s] < min) {
//                        min = buffer[s];
//                    }
//                }
//            } while (framesRead != 0);
            //Read frames into buffer
            framesRead = wavFile.readFrames(buffer, (int) wavFile.getNumFrames());
            for (int i = 0; i < framesRead; i++) {
                System.out.print(((int) buffer[i]) + " ");
                if (i != 0 && i % 50 == 0) {
                    System.out.println("");
                }
            }
            //Close the wavFile 
            wavFile.close();

            // Output the minimum and maximum value
            //System.out.printf("Min: %f, Max: %f\n", min, max);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
