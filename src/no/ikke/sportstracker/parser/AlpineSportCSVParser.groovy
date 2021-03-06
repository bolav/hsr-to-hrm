package no.ikke.sportstracker.parser

import de.saring.polarviewer.core.PVException
import de.saring.polarviewer.data.*
import de.saring.polarviewer.parser.*
import de.saring.util.unitcalc.ConvertUtils

import no.ikke.sportstracker.data.GPSSample
import no.ikke.sportstracker.util.GPS


import java.util.regex.Pattern

/**
 * This implementation of an ExerciseParser is for reading the CSV files 
 * exported from AlpineSport android software, 
 * These files have the extension ".csv". 
 *
 * Some parts taken from Kai Pastor's AlpineSportCSVParser.groovy (1.0).
 *
 * @author  Bjorn-Olav Strand
 * @version 1.0
 */
class AlpineSportCSVParser extends AbstractExerciseParser {

    /** Informations about this parser. */
    private ExerciseParserInfo info = new ExerciseParserInfo ('AlpineSport CSV', ["as_csv", "AS_CSV"] as String[])

    /** The list of lines (Strings) of the exercise file. */
     private def fileContent
     
     /** The filename */
     private def fn

    /**
     * Returns the informations about this parser implementation.
     * @return the parser informations
     */
    @Override
    ExerciseParserInfo getInfo () {
        info
    }
		
    /**
     * This method parses the specified exercise file and creates an
     * PVExercise object from it.
     *
     * @param filename name of exercise file to parse
     * @return the parsed PVExercise object
     * @throws PVException thrown on read/parse problems
     */
    @Override
    PVExercise parseExercise (String filename) throws PVException
    {
        fn = filename
        try {
            fileContent = new File (filename).readLines ()
            return parseExerciseFromContent ()
        }
        catch (Exception e) {
            System.out.println(e);
            throw new PVException ("Failed to read the CSV activity file '${filename}' ...", e)
        }
    }
    
    /**
     * Parses the exercise data from the file content.
     */
    private PVExercise parseExerciseFromContent ()
    {
        // parse basic exercise data
        PVExercise exercise = new PVExercise ()
        exercise.fileType = PVExercise.ExerciseFileType.ASCSV // FIXME
        def r = new RecordingMode ()
        r.altitude = true
        r.speed = true
        r.cadence = false
        r.power = false
        r.bikeNumber = 0

        exercise.recordingMode = r
        exercise.lapList = []

        def calDate = Calendar.getInstance ()
        def exeYear = calDate.get (Calendar.YEAR)
        
        def p = Pattern.compile("\\/(\\d{2,4})(\\d\\d)(\\d\\d)(\\d\\d)(\\d\\d)(\\d\\d)(\\d\\d)")
        // def p = Pattern.compile("[0-9]")
        def m = p.matcher(fn)
        if (m.find()) {
            def gYear   = m.group(1).toInteger()
            def gMonth  = m.group(2).toInteger() - 1
            def gDay    = m.group(3).toInteger()
            def gHour   = m.group(4).toInteger()
            def gMinute = m.group(5).toInteger()
            def gSecond = m.group(6).toInteger()
            
            
            calDate.set (gYear, gMonth, gDay, gHour, gMinute, gSecond)
        }

        
        
        def sumTime = 0
        def sumDist = 0
        def minInterval = 1000
        
        // For speed
        def speedMax = 0
        def lastlon = null
        def lastlat = null
        
        // For altitude
        def altitudeMin = null
        def altitudeMax = 0
        def ascent = 0
        def lastAltitude = null
        def sumAltitude = 0
        def samplecount = 0

        // create array of exercise sample
        def sampleList = []

        for (line in fileContent) {
            if (line.startsWith ("Time")) {
                // First line
            }
            else if (line.startsWith("#")) {
                
            }
            else {
                // Time (ms), Distance (m), Pace (min/km), Acceleration (kmh/s), Stride (spm), 
                //    0          1               2                 3                  4    
                // Latitude, Longitude, Altitude(m),
                //     5         6          7
                def cols = line.split(",")
                if (cols.length == 8) {
                    
                
                    samplecount++

                    double dist = 0.0 
                    if (cols[1] != "") {
                        dist = cols[1].toDouble()
                    }
                    int ms = cols[0].toInteger()
                    double alt = cols[7].toDouble() 

                    if (minInterval == 0) {
                        minInterval = ms
                    }

                    sumTime += ms
                    sumAltitude += alt

                    if ((lastAltitude)&&( lastAltitude < alt)) {
                        ascent += alt - lastAltitude
                    }

                    if ((altitudeMin == null)||( alt < altitudeMin )) {
                        altitudeMin = alt
                    }

                    if (altitudeMax < alt) {
                        altitudeMax = alt
                    }

                    lastAltitude = alt

                    def speed = ((dist * 3600) / ms)
                    /* Use speed for speed
                    def speed
                    if (cols[2] != "") {
                        speed = cols[2].toDouble()
                    }
                    else {
                        speed = 0
                    }
                    */
                    /* Use GPS for speed
                    def speed = 0

                    double lat = cols[5].toDouble()
                    double lon = cols[6].toDouble()

                    if (lastLat != null) {
                        def calcdist = GPS.haversin(lat, lng, lastlat, lastlon)
                        speed = ((calcdist * 3600) / ms)

                    }

                    lastlat = lat
                    lastlon = lon
                    */

                    if (speed > speedMax) {
                        speedMax = speed
                    }

                    def lon = cols[6].toDouble()
                    def lat = cols[5].toDouble()

                    int n = ms / minInterval          // Get evenly spaced intervals
                    for (int i = 0;i < n; i++) {
                        // System.out.println(i+"/"+n+" "+ms)
                        ExerciseSample exeSample = new GPSSample ()
                        exeSample.altitude = alt
                        exeSample.distance = sumDist + ((dist / n) * i)  // Get evenly spaced distance
                        exeSample.speed = speed

                        if (lastlat != null) {
                            exeSample.latitude = lastlat + (((lat - lastlat)/n)*i)
                            exeSample.longitude = lastlon + (((lon - lastlon)/n)*i)
                        }
                        else {
                            exeSample.latitude = lat
                            exeSample.longitude = lon
                        }
                        
                        sampleList.add (exeSample)
                    }

                    sumDist += dist
                    lastlat = lat
                    lastlon = lon

            }}
        }

        exercise.date = calDate.time
        exercise.recordingInterval = minInterval / 1000
        exercise.sampleList = sampleList
        exercise.duration = sumTime / 100
        
        
        // Set speed
        def speed = new ExerciseSpeed()
        speed.distance = sumDist
        speed.speedMax = speedMax
        speed.speedAVG = ((sumDist * 3600) / sumTime)
        exercise.speed = speed
        
        // Set altitude

        def altitude = new ExerciseAltitude()
        altitude.altitudeMin = altitudeMin
        altitude.altitudeAVG = sumAltitude / samplecount
        altitude.altitudeMax = altitudeMax
        altitude.ascent = ascent
        exercise.altitude = altitude

        // done :-)
        return exercise
    }    

}
