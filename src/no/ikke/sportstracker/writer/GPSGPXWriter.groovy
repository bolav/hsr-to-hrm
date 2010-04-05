package no.ikke.sportstracker.writer

import groovy.xml.MarkupBuilder
import de.saring.polarviewer.data.PVExercise

import no.ikke.sportstracker.data.GPSSample

import java.text.SimpleDateFormat

import java.util.Date

class GPSGPXWriter extends AbstractWriter {
    
    PVExercise exercise
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    
    public GPSGPXWriter (PVExercise x) {
        exercise = x;
    }
        
    
    public String getString () {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        def sl = exercise.getSampleList()
        
        def time = exercise.getDate().getTime()
        xml.gpx(creator:"GPSGPXWriter", "xmlns:xsi":"http://www.w3.org/2001/XMLSchema-instance", xmlns:"http://www.topografix.com/GPX/1/0", "xsi:schemaLocation":"http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd") {
            xml.time(sdf.format(new Date(time)))
            name(sdf.format(new Date(time)))
            desc("Converted file")
            trk {
                trkseg {
                    for (int i=0; i<sl.length; i++) {
                        time += exercise.getRecordingInterval()*1000
                        
                        trkpt(lat:sl[i].getLatitude(), lon:sl[i].getLongitude()) {
                            ele(sl[i].getAltitude())
                            xml.time(sdf.format(new Date(time)))
                        }
                    }
                }
            }
        }        

        return '<?xml version="1.0" encoding="UTF-8" standalone="no" ?>'+ "\n" + writer.toString();
    }
}