/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ikke.sportstracker.util;

import de.saring.polarviewer.data.PVExercise;
import de.saring.polarviewer.data.ExerciseSample;

import no.ikke.sportstracker.data.GPSSample;

/**
 *
 * @author bolav
 */
public class ExerciseUtil {

    public static void zeroSample (PVExercise x, Integer time) {
        int i = time.intValue() / x.getRecordingInterval();
        ExerciseSample[] sl = x.getSampleList();

        int olddist = sl[i].getDistance();

        short alt = 0;
        int dist = 0;
        if (i > 0) {
            alt = sl[i-1].getAltitude();
            dist = sl[i-1].getDistance();
        }

        sl[i].setHeartRate((short)0);
        sl[i].setSpeed(0);
        sl[i].setAltitude(alt);
        sl[i].setCadence((short)0);
        sl[i].setDistance(dist);
        sl[i].setTemperature((short)0);

        if (sl[i] instanceof GPSSample) {

            ((GPSSample)sl[i]).setLatitude(0);
            ((GPSSample)sl[i]).setLongitude(0);
        }

        int distdiff = olddist - dist;

        for (int j=i+1; j<sl.length; j++) {
            sl[j].setDistance(sl[j].getDistance() - distdiff);
        }

    }
}
