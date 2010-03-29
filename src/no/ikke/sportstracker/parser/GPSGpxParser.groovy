package no.ikke.sportstracker.parser

import de.saring.polarviewer.core.PVException
import de.saring.util.unitcalc.CalculationUtils;
import de.saring.polarviewer.data.*
import de.saring.polarviewer.parser.*

import no.ikke.sportstracker.util.GPS
import no.ikke.sportstracker.data.GPSSample

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
    PVExercise parseExercise (String filename) throws Exception {
        
        try {
            // get GPathResult object by using the XmlSlurper parser
            def path = new XmlSlurper ().parse (new File (filename))
            return parseExercisePath (path)
        }
        catch (Exception e) {
            throw e
            // throw new PVException ("Failed to read the GPX file '${filename}' ...", e)
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
		exercise.recordingMode.altitude = true
        exercise.speed = new ExerciseSpeed()
        exercise.altitude = new ExerciseAltitude()
        exercise.lapList = []
        
        def time = path.time
        exercise.date = sdFormat.parse(time.text()) // How to fix timezone?
        
        double lastlon = 0
        double lastlat = 0
        
        long mints = Long.MAX_VALUE
        long maxts = Long.MIN_VALUE
        
        long prevtimestamp = 0
        int interval = Integer.MAX_VALUE
        
        def totdistance = 0
        def count = 0
        def altSum = 0
        def altLast = 0
        def ascent = 0
        short altMax = Short.MIN_VALUE
        short altMin = Short.MAX_VALUE
        
        float speedMax = 0

        for (segment in path.trk.trkseg) {
            for (point in segment.trkpt) {
                long ts = sdFormat.parse(point.time.text()).getTime()
                
                if (prevtimestamp != 0) {
                    int this_interval = (int)(ts - prevtimestamp)/1000
                    if ((this_interval > 0) && (this_interval < interval)) {
                        interval = this_interval
                    }
                    
                }
                if (ts < mints) {
                    mints = ts
                }
                if (ts > maxts) {
                    maxts = ts
                }
                
                prevtimestamp = ts
                
            }
        }
        
        int numberOfSamples = (maxts-mints) / (interval * 1000)
        exercise.setSampleList (new ExerciseSample[numberOfSamples]);
        
        exercise.recordingInterval = interval

        prevtimestamp = 0
        
        for (segment in path.trk.trkseg) {
            for (point in segment.trkpt) {
                double lon = point.attributes().lon.toDouble()
                double lat = point.attributes().lat.toDouble()
                long ts = sdFormat.parse(point.time.text()).getTime()
                
                def altitude = point.ele.text().toDouble()
                
                altSum += altitude
                count++
                if (altitude > altMax) {
                    altMax = altitude
                }
                if (altitude < altMin) {
                    altMin = altitude
                }
                
                if (prevtimestamp != 0) {
                    if (altitude > altLast) {
                        ascent += (altitude - altLast)
                    }
                    
                    int this_interval = (int)(ts - prevtimestamp)/1000
                    
                    int startoffset = (int)((prevtimestamp - mints) / 1000) / interval;
                    int endoffset = (int)((ts - mints) / 1000) / interval;
                    def distance = GPS.haversin(lat, lon, lastlat, lastlon)
                    
                    float speed = (distance * 3600) / (this_interval * 1000)
                    if (speed > speedMax) {
                        speedMax = speed
                    }

                    for (int j=startoffset; j<endoffset; j++) {
                        GPSSample exeSample = new GPSSample ()
                        exercise.getSampleList()[j] = exeSample
                        exeSample.altitude = (short)point.ele.text().toFloat()
                        exeSample.speed = speed
                        exeSample.distance = (int)totdistance
                        exeSample.latitude = lat
                        exeSample.longitude = lon
                        // + ((distance / (endoffset + 1 - startoffset)) * ((j + 1)-startoffset))
                    }
                    
                    totdistance += distance

                }
                lastlat = lat
                lastlon = lon
                prevtimestamp = ts
                altLast = altitude
                
            }
        }
        
        exercise.altitude.altitudeMin = altMin
        exercise.altitude.altitudeMax = altMax
        exercise.altitude.altitudeAVG = (altSum / count)
        exercise.altitude.ascent = ascent
        
        exercise.duration = (maxts - mints) / 100
        exercise.speed.speedAVG = totdistance * 3600 / (exercise.duration * 100)
        exercise.speed.speedMax = speedMax
        
        exercise        
    }
    
}
