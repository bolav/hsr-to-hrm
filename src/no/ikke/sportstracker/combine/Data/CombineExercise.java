package no.ikke.sportstracker.combine;

import no.ikke.sportstracker.data.Exercise;
import de.saring.polarviewer.data.*;


import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CombineExercise extends Exercise  {
    List<PVExercise> exercises = new ArrayList<PVExercise> ();
    
    public CombineExercise () {
        
    }
    
    public void add (PVExercise x) {
        exercises.add(x);
    }
    
    @Override
    public ExerciseFileType getFileType () {
        return exercises.get(0).getFileType();
    }
    
    @Override
    public RecordingMode getRecordingMode () {
        RecordingMode rm = new RecordingMode();
        rm.setAltitude(false);
        rm.setSpeed(false);
        rm.setCadence(false);
        rm.setPower(false);
        rm.setBikeNumber((byte)0);
        rm.setTemperature(false);
        
        for (int i = 0; i<exercises.size(); i++) {
            RecordingMode m = exercises.get(i).getRecordingMode();
            if (m.isAltitude()) {
                rm.setAltitude(true);
            }
            if (m.isSpeed()) {
                rm.setSpeed(true);
            }
            if (m.isCadence()) {
                rm.setCadence(true);
            }
            if (m.isPower()) {
                rm.setPower(true);
            }
            if (m.isTemperature()) {
                rm.setTemperature(true);
            }
            // BikeNumber
            // Interval
        }
                
        return rm;
    }
    
    @Override
    public Date getDate () {
        Date date = Calendar.getInstance().getTime();
        for (int i = 0; i<exercises.size(); i++) {
            Date d = exercises.get(i).getDate();
            if (d.before(date)) {
                date = d;
            }
        }
        return date;
    }
    
    @Override
    public Lap[] getLapList () {
        return new Lap[0];
    }
    
    @Override
    public ExerciseSample[] getSampleList () {
        
        short ri = getRecordingInterval();
        int dur = getDuration();
        long time = getDate().getTime();
        
        int samples = dur / 10 / ri; // Number of samples is duration in seconds dived by recording interval
        
        ExerciseSample[] sampleList = new ExerciseSample[samples];
        
        // Init samples
        for (int i=0; i<samples; i++) {
            ExerciseSample sample = new ExerciseSample();
            sampleList[i] = sample;
        }
        
        
        // Get files and populate samples
        for (int i = 0; i<exercises.size(); i++) {
            PVExercise x = exercises.get(i);
            long xtime = x.getDate().getTime();
            int xdur = x.getDuration();
            ExerciseSample[] xsl = x.getSampleList();
            RecordingMode xrm = x.getRecordingMode();
            long startoffset = ((xtime - time) / 1000) / ri; // Gives startoffset in recording interval
            int k = startoffset;

            // Calc when in the session this file started
            // System.out.println("time: "+time+" xtime: "+xtime+" offset: "+startoffset);
            for (int ii=0; ii<xsl.length; ii++) {
                // sampleList[]
            }
            for (int i=startoffset; i<samples; i++) {
                
            }
            
            
            // ExerciseSample[] sl = exercises.get(i).getSampleList();
        }
                
        
        return sampleList;
    }
    
    @Override
    public short getRecordingInterval () {
        short recordingInterval = 0;
        for (int i = 0; i<exercises.size(); i++) {
            // Check recordinginterval and number of samples
            short ri = exercises.get(i).getRecordingInterval();
            if ((recordingInterval == 0) || (ri < recordingInterval)) {
                recordingInterval = ri;
            }
        }
        
        return recordingInterval;
    }
    
    @Override
    public int getDuration () {
        int duration = 0;
        for (int i = 0; i<exercises.size(); i++) {
            // Check recordinginterval and number of samples
            int dur = exercises.get(i).getDuration();
            if (dur > duration) {
                duration = dur;
            }
        }
                
        return duration;
    }
    
    
}
