/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ikke.sportstracker.combine.gui.panels;

import no.ikke.sportstracker.dummy.STContextDummy;
import no.ikke.sportstracker.util.ExerciseUtil;

import de.saring.polarviewer.data.ExerciseSample;
import de.saring.polarviewer.data.PVExercise;
import de.saring.polarviewer.gui.PVContext;
import de.saring.util.ResourceReader;
import de.saring.util.gui.ListUtils;
import de.saring.util.gui.TableCellRendererOddEven;
import de.saring.util.unitcalc.FormatUtils;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import java.awt.event.KeyEvent;
import de.saring.polarviewer.gui.panels.SamplePanel;


/**
 *
 * @author bolav
 */
public class EditSamplePanel extends SamplePanel {


    public EditSamplePanel () {
        super(STContextDummy.getDummy());

        tbSamples.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                tbSamplesKeyPressed(evt);
            }
        });

        tbSamples.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    }

    public void tbSamplesKeyPressed (KeyEvent evt) {
        if ((evt.getKeyCode() == KeyEvent.VK_DELETE)||(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
            System.out.println("Delete rows");
            int[] rows = tbSamples.getSelectedRows();


            PVExercise exercise = getDocument().getExercise();

            for (int i=0; i<rows.length; i++) {
                System.out.print(rows[i]+", ");
                ExerciseUtil.zeroSample(exercise, (Integer)tbSamples.getValueAt(rows[i], 0));
            }
            System.out.println("");

            System.out.println(tbSamples.getValueAt(rows[0], 0));
            System.out.println(tbSamples.getValueAt(rows[0], 1));
            // tbSamples.repaint
            
        }

    }


}
