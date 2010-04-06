package no.ikke.sportstracker.writer;

import de.saring.polarviewer.data.PVExercise;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;


public class AbstractWriter implements WriterInterface {
    WriterInfo info;
    
    public String getString (PVExercise x)  {
        return null;
    }
    
    public WriterInfo getInfo () {
        return info;
    }
    
    public void writeFile (File f, PVExercise x) {
        String s = getString(x);
        try {
            if (!f.createNewFile()) {
                return;
            }
            FileWriter fstream = new FileWriter(f);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(s);
            out.close();
            fstream.close();
        }
        catch (Exception e) {
            System.err.println("Exception: "+e);
        }
    }

}