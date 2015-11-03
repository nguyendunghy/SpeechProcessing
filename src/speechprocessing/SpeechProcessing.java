/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speechprocessing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NguyenVanDung
 */
public class SpeechProcessing {

    public static String fName = "C:\\Users\\NguyenVanDung\\Documents\\GitHub\\SpeechProcessing\\a96.wav";
    public static int count = 3450 * 2 +1000;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            FileInputStream stream = new FileInputStream(new File(fName));
            byte[] data = new byte[count];
            for(int i=0; i< count; i++){
              data[i] = 0;
            }
            stream.read(data, 0, count);
            for (int i = 0; i < count; i++) {
                System.out.println(data[i]);
            
            }
        } catch (Exception ex) {

        }
    }

}
