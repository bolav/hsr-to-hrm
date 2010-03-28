package no.ikke.sportstracker.combine;

import de.saring.polarviewer.data.*;

import de.saring.polarviewer.core.PVException;
import de.saring.polarviewer.parser.ExerciseParserFactory;
import de.saring.polarviewer.parser.ExerciseParser;

import no.ikke.sportstracker.writer.WriteHRM;

public class CombineMain {
    
    CombineExercise exercises = new CombineExercise();
    
    public void readExercise (String fn) throws PVException {
        ExerciseParser parser = ExerciseParserFactory.getParser(fn);
        exercises.add(parser.parseExercise(fn));
        
    }
    
    public void write () {
        WriteHRM w = new WriteHRM(exercises);
        System.out.println(w.getHRM());
    }
    
    public static void main(String[] args) throws PVException {
        System.out.println("main");
        
        CombineMain app = new CombineMain();
        System.out.println("args: "+args.length);
        for (int i=0; i<args.length; i++) {
            System.out.println(i+": reading "+args[i]);
            app.readExercise(args[i]);
        }
        app.write();
    }
}