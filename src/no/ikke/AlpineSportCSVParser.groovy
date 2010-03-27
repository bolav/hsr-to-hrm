package no.ikke

import de.saring.polarviewer.core.PVException
import de.saring.polarviewer.data.*
import de.saring.polarviewer.parser.*
import de.saring.util.unitcalc.ConvertUtils

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
            def gMonth  = m.group(2).toInteger()
            def gDay    = m.group(3).toInteger()
            def gHour   = m.group(4).toInteger()
            def gMinute = m.group(5).toInteger()
            def gSecond = m.group(6).toInteger()
            
            
            calDate.set (gYear, gMonth, gDay, gHour, gMinute, gSecond)
        }

        
        
        def sumTime = 0
        def sumDist = 0
        def minInterval = 0

        // create array of exercise sample
        def sampleList = []

        for (line in fileContent) {
            // System.out.println(line)
            // most frequent element first
            if (line.startsWith (",") ) {
                ExerciseSample exeSample = new ExerciseSample ()
                exeSample.heartRate = line.substring (1).toInteger ()
                sampleList.add (exeSample)
            }
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
                
                double dist = cols[1].toDouble()
                int ms = cols[0].toInteger()

                if (minInterval == 0) {
                    minInterval = ms
                }

                sumTime += ms
                
                def speed = ((dist * 3600) / ms)
                
                int n = ms / minInterval          // Get evenly spaced intervals
                for (int i = 0;i < n; i++) {
                    // System.out.println(i+"/"+n+" "+ms)
                    ExerciseSample exeSample = new ExerciseSample ()
                    exeSample.altitude = cols[7].toDouble() 
                    exeSample.distance = sumDist + ((dist / n) * i)  // Get evenly spaced distance
                    exeSample.speed = speed
                    sampleList.add (exeSample)
                }


                sumDist += dist

            }
        }

        exercise.date = calDate.time
        
        exercise.recordingInterval = minInterval / 1000
        exercise.sampleList = sampleList
        exercise.duration = sumTime / 100

  
        // done :-)
        return exercise
    }    

}
