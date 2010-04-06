package no.ikke.sportstracker.writer;

import de.saring.polarviewer.data.PVExercise;
import java.io.File;

public interface WriterInterface {
    public String getString (PVExercise x);
    public WriterInfo getInfo ();
    public void writeFile (File f, PVExercise x);
}