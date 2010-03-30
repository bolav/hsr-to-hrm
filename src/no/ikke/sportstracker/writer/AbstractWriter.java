package no.ikke.sportstracker.writer;

import de.saring.polarviewer.data.PVExercise;

public class AbstractWriter implements WriterInterface {
    WriterInfo info;
    
    public String getString (PVExercise x)  {
        return null;
    }
    
    public WriterInfo getInfo () {
        return info;
    }
    
}