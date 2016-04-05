/*
    Point of Sale System Project
    Authors: Clayton Barber, Brandon Barton, Declan Brennan, Maximilian Hasselbusch, Eric Metcalf
    Last Updated: 20 November 2015
 */
package pos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OWriter {

    private static OWriter OWriterInstance;
    File file;

    private OWriter() {
        file = new File("towrite.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

        } catch (IOException e) {
            System.err.println("BADNESS:\n" + e.getMessage());
        }
    }

    public synchronized static OWriter getInstance() {
        if (OWriterInstance == null) {
            OWriterInstance = new OWriter();
        }
        return OWriterInstance;
    }

    public void writeQuerry(String querry) {
        System.err.println("In writeQuerry: " + querry);
        try {
            FileWriter writer = new FileWriter(file);
            BufferedWriter bwriter = new BufferedWriter(writer);
            bwriter.write(querry);
            bwriter.newLine();
            bwriter.close();
        } catch (IOException ex) {
            System.err.println("BADNESS WHILE WRITING\n" + ex.getMessage());
        }
    }

}
