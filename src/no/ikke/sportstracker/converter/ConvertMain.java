package no.ikke.sportstracker.converter;

import de.saring.polarviewer.data.*;
import de.saring.polarviewer.core.PVException;
import de.saring.polarviewer.parser.ExerciseParserFactory;
import de.saring.polarviewer.parser.ExerciseParser;

import no.ikke.sportstracker.writer.*;

public class ConvertMain {
    
    PVExercise exercise;
    
    public void readExercise (String fn) throws PVException {
        ExerciseParser parser = ExerciseParserFactory.getParser(fn);
        exercise = parser.parseExercise(fn);
    }
    
    public void write () {
        // WriteHRM w = new WriteHRM(exercises);
        WriterInterface w = new GarminTCXWriter(exercise);
        System.out.println(w.getString());
    }
    
    
    public static void main(String[] args) throws PVException {
        
        ConvertMain app = new ConvertMain();
        
        for (int i=0; i<args.length; i++) {
            app.readExercise(args[i]);
        }
        app.write();
        
    }
}