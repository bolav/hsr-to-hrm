package no.ikke.sportstracker.combine;

import de.saring.polarviewer.data.*;

import de.saring.polarviewer.core.PVException;
import de.saring.polarviewer.parser.ExerciseParserFactory;
import de.saring.polarviewer.parser.ExerciseParser;

import no.ikke.sportstracker.writer.*;

public class CombineMain {
    
    CombineExercise exercises = new CombineExercise();
    
    public void readExercise (String fn) throws PVException {
        ExerciseParser parser = ExerciseParserFactory.getParser(fn);
        exercises.add(parser.parseExercise(fn));
        
    }
    
    public void write () {
        // WriteHRM w = new WriteHRM(exercises);
        WriterInterface w = new GarminTCXWriter(exercises);
        System.out.println(w.getString());
    }
    
    public static void main(String[] args) throws PVException {
        
        CombineMain app = new CombineMain();
        for (int i=0; i<args.length; i++) {
            app.readExercise(args[i]);
        }
        app.write();
    }
}