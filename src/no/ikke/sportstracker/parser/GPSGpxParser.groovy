package de.saring.polarviewer.parser.impl

import de.saring.polarviewer.core.PVException
import de.saring.util.unitcalc.CalculationUtils;
import de.saring.polarviewer.data.*
import de.saring.polarviewer.parser.*

import java.text.SimpleDateFormat

/**
 * ExerciseParser implementation for reading Garmin TCX v2 exercise files (XML-based).
 * Documentation about the format can be found at the Garmin website
 * ( http://developer.garmin.com/schemas/tcx/v2/ ).
 * 
 * @author  Stefan Saring
 * @version 1.0
 */
class GPSGpxParser extends AbstractExerciseParser {

    /** Informations about this parser. */
    private def info = new ExerciseParserInfo ('GPS GPX', ["gpx", "GPX"] as String[])
    
    /** The date and time parser instance for XML date standard. */
    private def sdFormat = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss"); 
        
    /** {@inheritDoc} */
    @Override
    ExerciseParserInfo getInfo () {
        info
    }
		
    /** {@inheritDoc} */
    @Override
    PVExercise parseExercise (String filename) throws PVException {
        
        try {
            // get GPathResult object by using the XmlSlurper parser
            def path = new XmlSlurper ().parse (new File (filename))
            return parseExercisePath (path)
        }
        catch (Exception e) {
            throw new PVException ("Failed to read the GPX file '${filename}' ...", e)
        }
    }
    
    /**
     * Parses the exercise data from the specified path (root element).
     */
    private PVExercise parseExercisePath (path) {
        
        // parse basic exercise data
        PVExercise exercise = new PVExercise ()
        exercise.fileType = PVExercise.ExerciseFileType.GPS_GPX
        exercise.recordingMode = new RecordingMode ()
		exercise.recordingMode.speed = true
        exercise.speed = new ExerciseSpeed()
        
        def time = path.time
        exercise.date = sdFormat.parse(time.text()) // How to fix timezone?
        
        for (segment in path.trk.trkseg) {
            for (point in segment.trkpt) {
                System.out.println(point.lon)
            }
        }
        
        int trackpointCount = 0
    		        
        // no summary data, everything is stored in laps
        // parse each lap and create a PolarViewer Lap object
        def pvLaps = []
        
        for (lap in activity.Lap) {
            def pvLap = parseLapData(exercise, lap)
            pvLaps << pvLap
            
            if (pvLap.heartRateAVG > 0) {
                double lapDurationSeconds = lap.TotalTimeSeconds.toDouble()
                totalHeartRateSum += pvLap.heartRateAVG * lapDurationSeconds                
            }
			
            double lapAscentMeters = 0
            long previousTrackpointTimestamp = Long.MIN_VALUE
            double previousTrackpointDistanceMeters = Double.MIN_VALUE
            double previousTrackpointAltitudeMeters = Double.MIN_VALUE
            
            // parse all Track elements
			for (track in lap.Track) {
				
				// parse all Trackpoint elements
				for (trackpoint in track.Trackpoint) {
                    trackpointCount++
					
                    // get optional heartrate data
					if (!trackpoint.HeartRateBpm.isEmpty()) {
						pvLap.heartRateSplit = trackpoint.HeartRateBpm.Value.toInteger()
					}
										
					// calculate speed between current and previous trackpoint
                    // (sometimes single trackpoint don't have distance data!)                    
                    if (!trackpoint.DistanceMeters.isEmpty()) {
                        
                        long tpTimestamp = sdFormat.parse(trackpoint.Time.text()).time
                        double tpDistanceMeters = trackpoint.DistanceMeters.toDouble()
                        double tpSpeed = 0                    
                        
                        if (previousTrackpointTimestamp > Long.MIN_VALUE) { 
                            long tpTimestampDiff = tpTimestamp - previousTrackpointTimestamp                        
                            // sometimes computed difference is < 0 => impossible, use 0 instead
                            double tpDistanceDiff = Math.max(tpDistanceMeters - previousTrackpointDistanceMeters, 0d)
                            
                            tpSpeed = CalculationUtils.calculateAvgSpeed(
                                (float) (tpDistanceDiff / 1000f), (int) Math.round(tpTimestampDiff / 1000f))
                        }
                        previousTrackpointTimestamp = tpTimestamp
                        previousTrackpointDistanceMeters = tpDistanceMeters
                        
                        pvLap.speed.speedEnd = tpSpeed
                        exercise.speed.speedMax = Math.max(tpSpeed, exercise.speed.speedMax)
                    }
                    
                    
                    // get optional altitude data
                    if (!trackpoint.AltitudeMeters.isEmpty()) {        
                        double tpAltitude = trackpoint.AltitudeMeters.toDouble()
                        altitudeMetersTotal += Math.round(tpAltitude)

						// create altitude objects for exercise and current lap if not done yet
						if (exercise.altitude == null) {
                            exercise.altitude = new ExerciseAltitude()
						    exercise.recordingMode.altitude = true        
                            
                            exercise.altitude.altitudeMin = Short.MAX_VALUE
						    exercise.altitude.altitudeMax = Short.MIN_VALUE
						    exercise.altitude.altitudeAVG = Math.round(altitudeMetersTotal / trackpointCount)
						    exercise.altitude.ascent = 0
						}
                        
                        if (pvLap.altitude == null) {
                        	pvLap.altitude = new LapAltitude()
                        }                        
                        pvLap.altitude.altitude = Math.round(tpAltitude)
                                                
                        exercise.altitude.altitudeMin = Math.min(tpAltitude, exercise.altitude.altitudeMin)
                    	exercise.altitude.altitudeMax = Math.max(tpAltitude, exercise.altitude.altitudeMax)
                        
                        // calculate lap ascent (need to use double precision here)
                    	if (previousTrackpointAltitudeMeters > Double.MIN_VALUE && 
                			tpAltitude > previousTrackpointAltitudeMeters) {
                            double tpAscent = tpAltitude - previousTrackpointAltitudeMeters                           
                            lapAscentMeters += tpAscent
                        	pvLap.altitude.ascent = Math.round(lapAscentMeters)
                        }
                        previousTrackpointAltitudeMeters = tpAltitude
                    }
					
					// TODO: parse all missing sample data from trackpoints
					// Problem: In Garmin there is no fixed sample rate, the time between
					// 2 samples is dynamic.
					// Solution: Add the timestamp to the PV Sample class.
				}
			}
        }
		
    	exercise.lapList = pvLaps as Lap[]        

		calculateAvgSpeed(exercise)
		calculateAvgHeartrate(exercise, totalHeartRateSum)
        calculateAvgAltitude(exercise, altitudeMetersTotal, trackpointCount)        
        exercise        
    }
    
    def parseLapData(exercise, lapElement) {
        def pvLap = new Lap()
        pvLap.speed = new LapSpeed()
        
        double lapDurationSeconds = lapElement.TotalTimeSeconds.toDouble()
        double distanceMeters = lapElement.DistanceMeters.toDouble()
        exercise.duration += Math.round(lapDurationSeconds * 10)
        pvLap.timeSplit = exercise.duration
        exercise.speed.distance += Math.round(distanceMeters)
        pvLap.speed.distance = exercise.speed.distance
        exercise.energy += lapElement.Calories.toInteger()
        
        // stored maximum lap speed in XML is wrong, will be calculated
        
        // calculate average speed of lap
        pvLap.speed.speedAVG = CalculationUtils.calculateAvgSpeed(
	        (float) (distanceMeters / 1000f), 
	        (int) Math.round(lapDurationSeconds))
        
        // parse optional heartrate data of lap
        parseLapHeartRateData(exercise, pvLap, lapElement)
        pvLap
    }
    
    def parseLapHeartRateData(exercise, pvLap, lapElement) {        
	    if (!lapElement.AverageHeartRateBpm.isEmpty()) {
	        pvLap.heartRateAVG = lapElement.AverageHeartRateBpm.Value.toInteger()   
	    }
	    if (!lapElement.MaximumHeartRateBpm.isEmpty()) {
	        pvLap.heartRateMax = lapElement.MaximumHeartRateBpm.Value.toInteger()
	        exercise.heartRateMax = Math.max(pvLap.heartRateMax, exercise.heartRateMax)
	    }
    }    
        
    def calculateAvgSpeed(exercise) {
        exercise.speed.speedAVG = CalculationUtils.calculateAvgSpeed(
            (float) (exercise.speed.distance / 1000f), (int) Math.round(exercise.duration / 10f))
    }
    
    def calculateAvgHeartrate(exercise, totalHeartRateSum) {
        // calculate average heartrate for full exercise if available
        if (totalHeartRateSum > 0) {
            exercise.heartRateAVG = Math.round(totalHeartRateSum / (exercise.duration / 10d))
        }        
    }
            
    def calculateAvgAltitude(exercise, altitudeMetersTotal, trackpointCount) {
        // calculate average altitude and total ascent (if recorded)
        if (exercise.altitude != null) {
            exercise.altitude.altitudeAVG = Math.round(altitudeMetersTotal / trackpointCount)
            
            for (pvLap in exercise.lapList) {
                exercise.altitude.ascent += pvLap.altitude.ascent
            }
        }                
    }
}
