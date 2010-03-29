package no.ikke.sportstracker.writer

import groovy.xml.MarkupBuilder
import de.saring.polarviewer.data.PVExercise

import no.ikke.sportstracker.data.GPSSample

import java.text.SimpleDateFormat

import java.util.Date

class GarminTCXWriter implements WriterInterface {
    
    PVExercise exercise
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    
    public GarminTCXWriter (PVExercise x) {
        exercise = x;
    }
        
    
    public String getString () {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        def sl = exercise.getSampleList()
        
        def time = exercise.getDate().getTime()
                
        xml.TrainingCenterDatabase(xmlns:"http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2", "xmlns:xsi":"http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation":"http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2 http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd") {
            Activities {
                Activity(Sport:"Other") {
                    Id(sdf.format(exercise.getDate()))
                    Lap(StartTime:sdf.format(exercise.getDate())) {
                        TotalTimeSeconds(exercise.duration / 10)
                        if (exercise.getSpeed()) {
                            DistanceMeters(exercise.getSpeed().distance)
                            MaximumSpeed(exercise.getSpeed().speedMax)
                        } 
                        else {
                            DistanceMeters(0)
                        }
                        Calories(exercise.getEnergyTotal())
                        if (exercise.getHeartRateAVG() > 0) {
                            AverageHeartRateBpm {
                                Value(exercise.getHeartRateAVG())
                            }
                        }
                        if (exercise.getHeartRateMax() > 0) {
                            MaximumHeartRateBpm {
                                Value(exercise.getHeartRateMax())
                            }
                        }
                        Intensity("Active")
                        TriggerMethod("Manual")
                        Track {
                            for (int i=0; i<sl.length; i++) {
                                time += exercise.getRecordingInterval()*1000
                                Trackpoint {
                                    Time(sdf.format(new Date(time)))
                                    
                                    if (sl[i] instanceof GPSSample) {
                                        Position {
                                            LatitudeDegrees(sl[i].getLatitude())
                                            LongitudeDegrees(sl[i].getLongitude())
                                        }                                        
                                    }
                                    AltitudeMeters( sl[i].getAltitude() )
                                    DistanceMeters( sl[i].getDistance() )
                                    if (sl[i].getHeartRate() > 0) {
                                        HeartRateBpm {
                                            Value( sl[i].getHeartRate() )
                                        }                                        
                                    }
                                }

                                
                            }
                        }
                    }
                }
            }
        }

        return '<?xml version="1.0" encoding="UTF-8" standalone="no" ?>'+ "\n" + writer.toString();
    }
}