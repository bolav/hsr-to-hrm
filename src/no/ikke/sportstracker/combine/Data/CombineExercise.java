package no.ikke.sportstracker.combine.data;

import no.ikke.sportstracker.data.Exercise;
import no.ikke.sportstracker.data.GPSSample;
import de.saring.polarviewer.data.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.saring.polarviewer.core.PVException;
import de.saring.polarviewer.parser.ExerciseParserFactory;
import de.saring.polarviewer.parser.ExerciseParser;


public class CombineExercise extends Exercise  {
    List<PVExercise> exercises = new ArrayList<PVExercise> ();
    private int maxSamples = 500;

    public CombineExercise () {
        
    }

    public void reset () {
        setSampleList(null);
        setSpeed(null);
        setRecordingInterval((short)0);

    }
    
    public void add (PVExercise x) {
        exercises.add(x);
        reset();
    }

    public PVExercise getExercise (int i) {
        return exercises.get(i);
    }

    public int getExerciseCount () {
        return exercises.size() + 1;
    }

    public void readExercise (String fn) throws PVException {
        ExerciseParser parser = ExerciseParserFactory.getParser(fn);
        add(parser.parseExercise(fn));

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
    public ExerciseSpeed getSpeed () {
        ExerciseSpeed speed = super.getSpeed();

        if (speed != null) {
            return speed;
        }

        ExerciseSample[] sl = getSampleList();
        speed = new ExerciseSpeed();

        int maxDistance = 0;
        for (int i=0; i<sl.length;i++) {
            if (sl[i].getDistance() > maxDistance) {
                maxDistance = sl[i].getDistance();
            }
        }
        speed.setDistance(maxDistance);
        speed.setSpeedAVG((maxDistance*3600) / (getDuration()*100));
        
        float max = 0;
        for (int i = 0; i<exercises.size(); i++) {
            PVExercise x = exercises.get(i);
            ExerciseSpeed xs = x.getSpeed();
            if (xs != null) {
                if (xs.getSpeedMax() > max) {
                    max = xs.getSpeedMax();
                }
            }
        }
        speed.setSpeedMax(max);
        setSpeed(speed);
        return speed;
    }
     
    @Override
    public ExerciseSample[] getSampleList () {
        
        GPSSample[] sampleList = (GPSSample[])super.getSampleList();
        if (sampleList != null) {
            return sampleList;
        }
        
        short ri = getRecordingInterval();
        int dur = getDuration();
        long time = getDate().getTime();
        
        int samples = dur / 10 / ri; // Number of samples is duration in seconds dived by recording interval
        System.out.println("samples "+samples+ " dur: "+dur+" ri "+ri);
        sampleList = new GPSSample[samples];
        
        // Init samples
        for (int i=0; i<samples; i++) {
            GPSSample sample = new GPSSample();
            sampleList[i] = sample;
        }
        
        
        // Get files and populate samples
        for (int i = 0; i<exercises.size(); i++) {
            PVExercise x = exercises.get(i);
            long xtime = x.getDate().getTime();
            int xdur = x.getDuration();
            short xri = x.getRecordingInterval();
            ExerciseSample[] xsl = x.getSampleList();
            RecordingMode xrm = x.getRecordingMode();
            long k = xtime;

            // Calc when in the session this file started
            // System.out.println("time: "+time+" xtime: "+xtime+" offset: "+startoffset);
            for (int ii=0; ii<xsl.length; ii++) {
                short hr = xsl[ii].getHeartRate();
                short alt = xsl[ii].getAltitude();
                float speed = xsl[ii].getSpeed();
                int dist = xsl[ii].getDistance();
                
                
                long nextk = k + (xri * 1000); // k and nextk are ms
                
                // Which samples shall we populate?
                int startoffset = (int)((k - time) / 1000) / ri;
                int endoffset = (int)((nextk - time) / 1000) / ri;
                
                for (int j=startoffset; j<endoffset; j++) {
                    if (hr > 0) {
                        sampleList[j].setHeartRate(hr);
                    }
                    if (alt > 0) {
                        sampleList[j].setAltitude(alt);
                    }
                    if (speed > 0) {
                        sampleList[j].setSpeed(speed);
                    }
                    if (dist > 0) {
                        sampleList[j].setDistance(dist);
                    }
                    if (xsl[ii] instanceof GPSSample) {
                        sampleList[j].setLongitude(((GPSSample)(xsl[ii])).getLongitude());
                        sampleList[j].setLatitude(((GPSSample)(xsl[ii])).getLatitude());
                    }
                }
                
                k = nextk;
                // sampleList[]
            }
            
            
            // ExerciseSample[] sl = exercises.get(i).getSampleList();
        }
        
        // Iterate all and fix?
                
        setSampleList(sampleList);
        return sampleList;
    }
    
    @Override
    public short getRecordingInterval () {

        short recordingInterval = super.getRecordingInterval();
        if (recordingInterval > 0) return recordingInterval;

        int duration = getDuration() / 10; // Duration is in 0.1s
        for (int i = 0; i<exercises.size(); i++) {

            short ri = exercises.get(i).getRecordingInterval();
            if ((recordingInterval == 0) || (ri < recordingInterval)) {
                if (ri > 0) recordingInterval = ri;
            }
        }

        /*
        int ms = getMaxSamples();
        System.out.println("Max "+ms);
        int mydiv = duration / recordingInterval;
        System.out.println("Current "+mydiv);

        if (mydiv > ms) {
            recordingInterval = (short)(duration / ms); // Minste felles multiplum?
            // Check for goodlooking?
        }
        System.out.println("ri "+recordingInterval);
        */
        setRecordingInterval(recordingInterval);
        return recordingInterval;
    }
    
    @Override
    public int getDuration () {
        long time = getDate().getTime();
        
        int duration = 0;
        for (int i = 0; i<exercises.size(); i++) {
            int dur = exercises.get(i).getDuration();
            long xtime = exercises.get(i).getDate().getTime();
            
            dur += (xtime-time)/100;
            
            if (dur > duration) {
                duration = dur;
            }
        }
                
        return duration;
    }

    public int getMaxSamples() {
        return maxSamples;
    }

    public void setMaxSamples(int maxSamples) {
        this.maxSamples = maxSamples;
    }
    
    public static void main(String[] args) throws PVException {

        CombineExercise ex = new CombineExercise();
        ex.readExercise("/Users/bolav/Documents/training/logs/20100405-rr.hsr");
        ex.readExercise("/Users/bolav/Documents/training/logs/2010040512241401_mtb.as_csv");
        System.out.println(ex.getSampleList());
    }

}
