/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ikke.sportstracker.combine.gui.panels;

import no.ikke.sportstracker.dummy.STContextDummy;
import no.ikke.sportstracker.combine.data.CombineExercise;

import de.saring.polarviewer.data.Lap;
import de.saring.polarviewer.data.PVExercise;
import de.saring.polarviewer.gui.panels.DiagramPanel;
import de.saring.polarviewer.data.ExerciseSample;
import de.saring.polarviewer.data.HeartRateLimit;
import de.saring.util.unitcalc.ConvertUtils;
import de.saring.util.unitcalc.FormatUtils;
import de.saring.util.gui.jfreechart.ChartUtils;

import java.awt.BasicStroke;

import org.jfree.data.general.Series;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;



/**
 *
 * @author bolav
 */
public class CompareDiagramPanel extends DiagramPanel {
    public CompareDiagramPanel () {
        super(STContextDummy.getDummy());
    }

     /**
     * Draws the diagram according to the current display settings.
     */
    @Override
    public void updateDiagram ()
    {

        CombineExercise exercise = (CombineExercise)getDocument().getExercise ();

        AxisType axisTypeLeft = (AxisType) cbLeft.getSelectedItem ();
        AxisType axisTypeRight = (AxisType) cbRight.getSelectedItem ();
        AxisType axisTypeBottom = (AxisType) cbBottom.getSelectedItem ();
        boolean fDomainAxisTime = axisTypeBottom == AxisType.Time;

        // create and fill data series according to axis type
        // (right axis only when user selected a different axis type)
        Series sLeft = createSeries(fDomainAxisTime, "left");
        Series sRight = null;

        int graphs = 1;

        if (exercise.getExerciseCount() > 1) {
            sRight = createSeries (fDomainAxisTime, "right");
            graphs++;
        }



        // fill data series with all recorded exercise samples
        if (exercise.getSampleList () != null) {
            for (int j = 0; j < graphs; j++) {
                PVExercise x = exercise.getExercise(j);
                for (int i = 0; i < x.getSampleList().length; i++) {
                    ExerciseSample sample = x.getSampleList()[i];
                    Number value = sample.getSpeed();
                    int timeSeconds = i * x.getRecordingInterval ();
                    Second second = createJFreeChartSecond(timeSeconds);

                    if (j == 0) {
                        ((TimeSeries)sLeft).add(second, value);
                    }
                    else if (j == 1) {
                        ((TimeSeries)sRight).add(second, value);
                        
                    }
                }
            }
        }

        XYDataset dataset = createDataSet (fDomainAxisTime, sLeft);

        // create chart depending on domain axis type
        JFreeChart chart = null;
        if (fDomainAxisTime) {
            chart = ChartFactory.createTimeSeriesChart (
                null,                       // Title
                axisTypeBottom.toString (), // Y-axis label
                axisTypeLeft.toString (),   // X-axis label
                dataset,                    // primary dataset
                false,                      // display legend
                true,                       // display tooltips
                false);                     // URLs
        }

        ChartUtils.customizeChart (chart, chartPanel);

        // set format of time domain axis (if active)
        XYPlot plot = (XYPlot) chart.getPlot ();

        // setup left axis
        ValueAxis axisLeft = plot.getRangeAxis (0);
        axisLeft.setLabelPaint (COLOR_AXIS_LEFT);
        axisLeft.setTickLabelPaint (COLOR_AXIS_LEFT);
        XYItemRenderer rendererLeft = plot.getRenderer (0);
        rendererLeft.setSeriesPaint (0, COLOR_AXIS_LEFT);
        setTooltipGenerator (rendererLeft, axisTypeBottom, axisTypeLeft);

        // setup right axis (when selected)
        if (sRight != null) {
/*
            NumberAxis axisRight = new NumberAxis (axisTypeRight.toString ());
            axisRight.setAutoRangeIncludesZero (false);
            plot.setRangeAxis (1, axisRight);
            plot.setRangeAxisLocation (1, AxisLocation.BOTTOM_OR_RIGHT);
            axisRight.setLabelPaint (COLOR_AXIS_RIGHT);
            axisRight.setTickLabelPaint (COLOR_AXIS_RIGHT);
*/
            // create dataset for right axis
            XYDataset datasetRight = createDataSet(fDomainAxisTime, sRight);
            plot.setDataset(1, datasetRight);
            plot.mapDatasetToRangeAxis(1, 0);

            // set custom renderer
            StandardXYItemRenderer rendererRight = new StandardXYItemRenderer();
            rendererRight.setSeriesPaint(0, COLOR_AXIS_RIGHT);
            plot.setRenderer(1, rendererRight);
            setTooltipGenerator(rendererRight, axisTypeBottom, axisTypeRight);
        }

        // highlight current selected (if presdent) heartrate range when displayed on left axis
        if ((highlightHeartrateRange >= 0) && (axisTypeLeft == AxisType.Heartrate)) {
            HeartRateLimit currentLimit = exercise.getHeartRateLimits ()[highlightHeartrateRange];

            // don't highlight percentual ranges (is not possible, the values
            // are absolute and the maximum heartrate is unknown)
            if (currentLimit.isAbsoluteRange ()) {
                Marker hrRangeMarker = new IntervalMarker (currentLimit.getLowerHeartRate (), currentLimit.getUpperHeartRate ());
                hrRangeMarker.setPaint (COLOR_MARKER_HEARTRATE);
                hrRangeMarker.setAlpha (0.3f);
                plot.addRangeMarker (hrRangeMarker);
            }
        }

        // draw a vertical marker line for each lap (not for the last)
        if (exercise.getLapList ().length > 0) {

            for (int i = 0; i < exercise.getLapList ().length - 1; i++) {
                Lap lap = exercise.getLapList ()[i];
                double lapSplitValue;

                // compute lap split value (different for time or distance mode)
                // (the value must be milliseconds for time domain axis)
                if (fDomainAxisTime) {
                    int lapSplitSeconds = lap.getTimeSplit () / 10;
                    lapSplitValue = createJFreeChartSecond (lapSplitSeconds).getFirstMillisecond ();
                }
                else {
                    lapSplitValue = lap.getSpeed ().getDistance () / 1000D;
                    if (getContext ().getFormatUtils ().getUnitSystem () == FormatUtils.UnitSystem.English) {
                        lapSplitValue = ConvertUtils.convertKilometer2Miles (lapSplitValue, false);
                    }
                }

                // create domain marker
                Marker lapMarker = new ValueMarker (lapSplitValue);
                lapMarker.setPaint (COLOR_MARKER_LAP);
                lapMarker.setStroke (new BasicStroke (1.5f));
                lapMarker.setLabel (getContext ().getResReader ().getString ("pv.diagram.lap", i+1));
                lapMarker.setLabelAnchor (RectangleAnchor.TOP_LEFT);
                lapMarker.setLabelTextAnchor (TextAnchor.TOP_RIGHT);
                plot.addDomainMarker (lapMarker);
            }
        }

        // add chart to panel
        chartPanel.setChart (chart);
    }

    public Number getSampleValue(AxisType axisType, ExerciseSample sample) {

        FormatUtils formatUtils = getContext ().getFormatUtils ();

        switch (axisType) {
            case Heartrate:
                return sample.getHeartRate ();
            case Altitude:
                if (formatUtils.getUnitSystem () == FormatUtils.UnitSystem.Metric) {
                    return sample.getAltitude ();
                }
                else {
                    return ConvertUtils.convertMeter2Feet (sample.getAltitude ());
                }
            case Speed:
                float speed = sample.getSpeed ();
                if (formatUtils.getUnitSystem () != FormatUtils.UnitSystem.Metric) {
                    speed = (float) ConvertUtils.convertKilometer2Miles (speed, false);
                }
                if (formatUtils.getSpeedView () == FormatUtils.SpeedView.MinutesPerDistance) {
                    // convert speed to minutes per distance
                    if (speed != 0f) {
                        speed = 60 / speed;
                    }
                }
                return speed;
            case Cadence:
                return sample.getCadence ();
            case Temperature:
                if (formatUtils.getUnitSystem () == FormatUtils.UnitSystem.Metric) {
                    return sample.getTemperature ();
                }
                else {
                    return ConvertUtils.convertCelsius2Fahrenheit (sample.getTemperature ());
                }
            default:
                return 0;
        }
    }


}
