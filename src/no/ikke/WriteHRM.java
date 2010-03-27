package no.ikke;

import de.saring.polarviewer.core.PVException;
import de.saring.polarviewer.parser.ExerciseParserFactory;
import de.saring.polarviewer.parser.ExerciseParser;
import de.saring.polarviewer.data.PVExercise;
import de.saring.util.unitcalc.FormatUtils;

import java.util.Calendar;

public class WriteHRM {
    
    FormatUtils formatUtils = new FormatUtils(FormatUtils.UnitSystem.Metric, FormatUtils.SpeedView.DistancePerHour);
    
    String inFile;
    PVExercise exercise;
    
    int maxHR = 188;
    int restHR = 88;
    
    public WriteHRM (String fn) {
        inFile = fn;
    }
    
    public void parseFile () throws PVException {
        ExerciseParser parser = ExerciseParserFactory.getParser(inFile);
        exercise = parser.parseExercise(inFile);
    }
    
    public int getMonitor () {
        if (exercise.getFileType() == PVExercise.ExerciseFileType.S510RAW) {
            return 10;
        }
        return 0;
    }
    
    public String getMode () {
        StringBuffer sb = new StringBuffer();
        
        if (exercise.getRecordingMode().isCadence()) {
            sb.append("0");
        } 
        else if (exercise.getRecordingMode().isAltitude()) {
            sb.append("1");
        }
        else {
            sb.append("3");
        }
        
        if (exercise.getRecordingMode().isSpeed()) {
            sb.append("1");
        }
        else {
            sb.append("0");
        }
        
        sb.append("0");
        return sb.toString();
    }
    
    public String getDate () {
        StringBuffer sb = new StringBuffer();
        Calendar date = Calendar.getInstance ();
        date.setTime (exercise.getDate ());
        
        sb.append(date.get(Calendar.YEAR));
        int month = date.get(Calendar.MONTH) + 1;
        if (month < 10) {
            sb.append("0");
        }
        sb.append(month);
        if (date.get(Calendar.DAY_OF_MONTH) < 10) {
            sb.append("0");
        }
        sb.append(date.get(Calendar.DAY_OF_MONTH));
        return sb.toString();
        
    }
    
    public String getTime () {
        StringBuffer sb = new StringBuffer();
        Calendar date = Calendar.getInstance ();
        date.setTime (exercise.getDate ());
        
        if (date.get(Calendar.HOUR_OF_DAY) < 10) {
            sb.append("0");
        }
        sb.append(date.get(Calendar.HOUR_OF_DAY));
        sb.append(":");
        if (date.get(Calendar.MINUTE) < 10) {
            sb.append("0");
        }
        sb.append(date.get(Calendar.MINUTE));
        sb.append(":");
        if (date.get(Calendar.SECOND) < 10) {
            sb.append("0");
        }
        sb.append(date.get(Calendar.SECOND));
        sb.append(".0");
        return sb.toString();
        
    }
    
    public String getDuration () {
        StringBuffer sb = new StringBuffer();
        
        sb.append(formatUtils.tenthSeconds2TimeString(exercise.getDuration()));
        
        return sb.toString();
    }
    
    
    public String getSMode () {
        StringBuffer sb = new StringBuffer();
        
        if (exercise.getRecordingMode().isSpeed()) {
            sb.append("1");
        }
        else {
            sb.append("0");
        }

        if (exercise.getRecordingMode().isCadence()) {
            sb.append("1");
        }
        else {
            sb.append("0");
        }

        if (exercise.getRecordingMode().isAltitude()) {
            sb.append("1");
        }
        else {
            sb.append("0");
        }

        if (exercise.getRecordingMode().isPower()) {
            sb.append("1");
        }
        else {
            sb.append("0");
        }
        
        sb.append("0"); // Power Left Right Balance
        sb.append("0"); // Power Pedalling Index
        
        if (exercise.getRecordingMode().isSpeed()) {
            sb.append("1");
        }
        else {
            sb.append("0");
        }
        
        sb.append("0"); // US / Euro unit
        sb.append("0"); // Air pressure
        
        return sb.toString();
    }
    
    public String getRecInterval () {
        StringBuffer sb = new StringBuffer();
        //if (exercise.getRecordingInterval() == ) {
            
        // }
        if (exercise.getRecordingInterval() == 60) {
            sb.append("60");
        }
        else if (exercise.getRecordingInterval() == 120) {
            sb.append("120");
        }
        else {
            System.err.println(exercise.getRecordingInterval());
            sb.append("0");
        }
        return sb.toString();
    }
    
    public String getHRLimits (int i) {
        StringBuffer sb = new StringBuffer();
        
        sb.append("Upper");
        sb.append(i);
        sb.append("=");
        if (exercise.getHeartRateLimits() != null) {
            sb.append(exercise.getHeartRateLimits()[i-1].getUpperHeartRate());
        }
        else {
            sb.append("0");
        }
        sb.append("\r\n");
        
        sb.append("Lower");
        sb.append(i);
        sb.append("=");
        if (exercise.getHeartRateLimits() != null) {
            sb.append(exercise.getHeartRateLimits()[i-1].getLowerHeartRate());
        }
        else {
            sb.append("0");
        }
        sb.append("\r\n");
        
        return sb.toString();
    }
    
    public String getHRZones () {
        StringBuffer sb = new StringBuffer();
        sb.append("[HRZones]\r\n");
        sb.append("190\r\n"); // TODO: config
        sb.append("180\r\n");// TODO: config
        sb.append("170\r\n");// TODO: config
        sb.append("160\r\n");// TODO: config
        sb.append("150\r\n");// TODO: config
        sb.append("140\r\n");// TODO: config
        sb.append("0\r\n");// TODO: config
        sb.append("0\r\n");// TODO: config
        sb.append("0\r\n");// TODO: config
        sb.append("0\r\n");// TODO: config
        sb.append("0\r\n");// TODO: config
        sb.append("\r\n");
        
        return sb.toString();
    }
    
    public String getIntTimes () {
        StringBuffer sb = new StringBuffer();
        
        sb.append("[IntTimes]\r\n");
        
        for (int i=0; i < exercise.getLapList().length; i++ ) {
            sb.append(formatUtils.tenthSeconds2TimeString(exercise.getLapList()[i].getTimeSplit())); // laptime
            sb.append("\t");
            sb.append(exercise.getLapList()[i].getHeartRateSplit());
            sb.append("\t");
            sb.append(0);
            sb.append("\t");
            sb.append(exercise.getLapList()[i].getHeartRateAVG());
            sb.append("\t");
            sb.append(exercise.getLapList()[i].getHeartRateMax());
            sb.append("\r\n");

            sb.append("32\t"); // Flags
            sb.append("0\t"); // rec time
            sb.append("0\t"); // rec hr
            if (exercise.getLapList()[i].getSpeed() != null) {                
                sb.append(Math.round(exercise.getLapList()[i].getSpeed().getSpeedEnd() * 128));
                sb.append("\t");
                sb.append(exercise.getLapList()[i].getSpeed().getCadence());
                sb.append("\t");
            }
            else {
                sb.append("0\t0\t");
            }
            if (exercise.getLapList()[i].getAltitude() != null) {
                sb.append(exercise.getLapList()[i].getAltitude().getAltitude());
            } 
            else {
                sb.append(0);
            }
            sb.append("\r\n");

            sb.append("0\t");
            sb.append("0\t");
            sb.append("0\t");
            if (exercise.getLapList()[i].getAltitude() != null) {
                sb.append(Math.round(exercise.getLapList()[i].getAltitude().getAscent() / 10));
            } 
            else {
                sb.append(0);
            }
            sb.append("\t"); // ascent 10m
            if (exercise.getLapList()[i].getSpeed() != null) {                
                sb.append(Math.round(exercise.getLapList()[i].getSpeed().getDistance() / 100));
            }
            else {
                sb.append("0");
            }
            sb.append("\r\n");  // distance 0.1km

            sb.append("0\t"); // Lap type
            sb.append("0\t"); // Lap dist
            sb.append("0\t"); // Power
            if (exercise.getLapList()[i].getTemperature() != null) {
                sb.append(exercise.getLapList()[i].getTemperature().getTemperature());
            } 
            else {
                sb.append(0);
            }
            sb.append("\t"); // Temp
            sb.append("0\t"); //  PhaseLap
            sb.append("0\r\n"); // AirPR

            sb.append("0\t"); // Stride average in cm
            sb.append("0\t"); // Autom.lap
            sb.append("0\t");
            sb.append("0\t");
            sb.append("0\t");
            sb.append("0\r\n");
            
        }
        sb.append("\r\n");

        return sb.toString();
    }
    
    public String getHRData () { 
        StringBuffer sb = new StringBuffer();
        
        sb.append("[HRData]\r\n");
        
        for (int i=0; i < exercise.getSampleList().length; i++) {
            sb.append(exercise.getSampleList()[i].getHeartRate());
            sb.append("\t"); // BPM
            sb.append(Math.round(exercise.getSampleList()[i].getSpeed () * 10));
            sb.append("\t"); // Speed
            sb.append(exercise.getSampleList()[i].getCadence());
            sb.append("\t"); // Cadence
            sb.append(exercise.getSampleList()[i].getAltitude());
            sb.append("\t"); // Altitude
            sb.append("0\t"); // Power
            sb.append("0"); // Balance
            sb.append("\t");
            sb.append("0");  // Pressure
            sb.append("\r\n");
            
        }
        
        sb.append("\r\n");
        
        return sb.toString();
    }
    
    public String getTrip () { 
        StringBuffer sb = new StringBuffer();
        
        sb.append("[Trip]\r\n");
        if (exercise.getSpeed() != null) {
            sb.append(Math.round(exercise.getSpeed().getDistance() / 100));
        }
        else {
            sb.append("0");
        }
        sb.append("\r\n"); // Distance
        sb.append("0\r\n"); // Ascent 1m
        sb.append(Math.round(exercise.getDuration() / 10));
        sb.append("\r\n"); // Total time in seconds
        sb.append("0\r\n"); // Average altitude 1m
        sb.append("0\r\n"); // Maximum altitude 1m

        if (exercise.getSpeed() != null) {
            sb.append(Math.round(exercise.getSpeed().getSpeedAVG() * 128));
            sb.append("\r\n"); // Average speed
            sb.append(Math.round(exercise.getSpeed().getSpeedMax() * 128));
            sb.append("\r\n"); // Maximum speed
        }
        else {
            sb.append("0\r\n0\r\n");
        }
        sb.append(exercise.getOdometer());
        sb.append("\r\n"); // Odometer at the end
        sb.append("\r\n");
        
        return sb.toString();
        
    }
    
    public String getSummaryTH () { // TODO
        StringBuffer sb = new StringBuffer();
        
        sb.append("[Summary-TH]\r\n");
        sb.append("0\t"); // Total time for selection
        sb.append("0\t"); // 
        sb.append("0\t"); // 
        sb.append("0\t"); // 
        sb.append("0\t"); // 
        sb.append("0\r\n"); // 
        sb.append("0\t"); // 
        sb.append("0\t"); // 
        sb.append("0\t"); // 
        sb.append("0\r\n"); // 
        sb.append("0\t"); // 
        sb.append("0\r\n"); // 
        sb.append("\r\n");
        
        return sb.toString();
    }
    
    public String getHRSummary (int i) { // TODO
        StringBuffer sb = new StringBuffer();
        
        // Summary for limits 1
        if (exercise.getHeartRateLimits() != null) {
            sb.append(exercise.getHeartRateLimits()[i-1].getTimeWithin());
            sb.append("\t");
            sb.append(exercise.getHeartRateLimits()[i-1].getTimeAbove());
            sb.append("\t"); // 
        }
        else {
            sb.append("0\t0\t");
        }
        sb.append("0\t"); // 
        sb.append("0\t"); // 
        sb.append("0\t"); // 
        sb.append("0\r\n"); // 
        
        sb.append(maxHR);
        sb.append("\t"); // 
        if (exercise.getHeartRateLimits() != null) {
        
            sb.append(exercise.getHeartRateLimits()[i-1].getUpperHeartRate());
            sb.append("\t"); // 
            sb.append(exercise.getHeartRateLimits()[i-1].getLowerHeartRate());
            sb.append("\t"); // 
        } 
        else {
            sb.append("0\t0\t");
        }
        sb.append(restHR);
        sb.append("\r\n"); // 
        
        
        return sb.toString();
    }
    
    public String getSummary123 () { 
        StringBuffer sb = new StringBuffer();
        
        sb.append("[Summary-123]\r\n");
        sb.append(getHRSummary(1));
        sb.append(getHRSummary(2));
        sb.append(getHRSummary(3));
        sb.append("0\t");
        sb.append("0\r\n");
        sb.append("\r\n");
        
        return sb.toString();
    }
    
    public String getExtraData () { 
        StringBuffer sb = new StringBuffer();
        
        sb.append("[ExtraData]\r\n");
        sb.append("\r\n");
        return sb.toString();
    }

    public String getSwapTimes () { 
        StringBuffer sb = new StringBuffer();
        
        sb.append("[SwapTimes]\r\n");
        sb.append("\r\n");
        return sb.toString();
    }
    
    public String getIntNotes () { 
        StringBuffer sb = new StringBuffer();
        
        sb.append("[IntNotes]\r\n");
        sb.append("\r\n");
        return sb.toString();
    }
    
    public String getHRM () {
        StringBuffer sb = new StringBuffer();
        sb.append("[Params]\r\n");
        sb.append("Version=107\r\n");
        // sb.append("Version=106\r\n");
        sb.append("Monitor="); sb.append(getMonitor()); sb.append("\r\n");
        sb.append("Mode="); sb.append(getMode()); sb.append("\r\n"); // GetMode
        sb.append("SMode="); sb.append(getSMode()); sb.append("\r\n"); 
        sb.append("Date="); sb.append(getDate()); sb.append("\r\n"); 
        sb.append("StartTime="); sb.append(getTime()); sb.append("\r\n"); 
        sb.append("Length="); sb.append(getDuration()); sb.append("\r\n"); 
        sb.append("Interval="); sb.append(getRecInterval()); sb.append("\r\n");
        sb.append(getHRLimits(1));
        sb.append(getHRLimits(2));
        sb.append(getHRLimits(3));
        sb.append("Timer1=00:00\r\n");
        sb.append("Timer2=00:00\r\n");
        sb.append("Timer3=00:00\r\n");
        sb.append("ActiveLimit=0\r\n");
        sb.append("MaxHR="); sb.append(maxHR); sb.append("\r\n"); // TODO: config
        sb.append("RestHR="); sb.append(restHR); sb.append("\r\n"); // TODO: config
        sb.append("StartDelay=0\r\n");
        sb.append("VO2max=0\r\n"); // TODO: config
        sb.append("Weight=75\r\n"); // TODO: config
        sb.append("\r\n");
        sb.append("[Note]\r\n");
        sb.append("Converted from "); sb.append(inFile); sb.append("\r\n");
        sb.append("\r\n");
        
        sb.append(getHRZones());
        sb.append(getSwapTimes());
        sb.append(getIntTimes());
        sb.append(getIntNotes());
        sb.append(getExtraData());
        sb.append(getSummary123());
        sb.append(getSummaryTH());
        sb.append(getTrip());
        sb.append(getHRData());
        
        return sb.toString();
    }
    
    public static void main (String[] args) throws PVException {
        WriteHRM app = new WriteHRM(args[0]);
        app.parseFile();
        System.out.println(app.getHRM());
    }
}