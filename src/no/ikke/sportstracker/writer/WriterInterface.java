package no.ikke.sportstracker.writer;

import de.saring.polarviewer.data.PVExercise;

public interface WriterInterface {
    public String getString (PVExercise x);
    public WriterInfo getInfo ();
}