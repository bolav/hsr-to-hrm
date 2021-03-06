/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CombineView.java
 *
 * Created on Apr 3, 2010, 2:57:01 PM
 */

package no.ikke.sportstracker.combine.gui;

import no.ikke.sportstracker.combine.data.CombineExercise;
import no.ikke.sportstracker.combine.gui.panels.CompareDiagramPanel;
import no.ikke.sportstracker.combine.gui.panels.EditSamplePanel;
import no.ikke.sportstracker.dummy.STContextDummy;
import no.ikke.sportstracker.dummy.DummyDocument;
import no.ikke.sportstracker.writer.*;


import de.saring.polarviewer.gui.PVDocument;

import de.saring.polarviewer.parser.impl.PolarHRMParser;

import java.io.File;
import java.util.Vector;
import de.saring.sportstracker.gui.dialogs.HRMFileOpenDialog;
import de.saring.sportstracker.core.STOptions;
import de.saring.polarviewer.gui.panels.DiagramPanel;
import de.saring.polarviewer.gui.panels.SamplePanel;

/**
 *
 * @author bolav
 */
public class CombineView extends javax.swing.JFrame {

    private DummyDocument document = new DummyDocument();
    private Vector fileList = new Vector();
    private STOptions opts = new STOptions();

    private DiagramPanel diagramPanel;
    private SamplePanel samplePanel;
    private CompareDiagramPanel comparePanel;

    private CombineExercise exercises = new CombineExercise();


    /** Creates new form CombineView */
    public CombineView() {
        initComponents();
        initView();
    }

    private void initView() {
        samplePanel = new SamplePanel(STContextDummy.getDummy());
        diagramPanel = new DiagramPanel(STContextDummy.getDummy());
        comparePanel = new CompareDiagramPanel();

        samplePanel.setDocument(document);
        diagramPanel.setDocument(document);
        comparePanel.setDocument(document);

        jTabbedPane.add("View", diagramPanel);
        jTabbedPane.add("Edit", samplePanel);
        jTabbedPane.add("Compare", comparePanel);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        tfFileName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        liFilelist = new javax.swing.JList(fileList);
        bAdd = new javax.swing.JButton();
        bFile = new javax.swing.JButton();
        bView = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        tfOutfile = new javax.swing.JTextField();
        bFileout = new javax.swing.JButton();
        bSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form"); // NOI18N

        jTabbedPane.setName("jTabbedPane"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        tfFileName.setText("/Users/bolav/Documents/training/logs/");
        tfFileName.setName("tfFileName"); // NOI18N

        jLabel1.setText("Exercise file");
        jLabel1.setName("jLabel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        liFilelist.setName("liFilelist"); // NOI18N
        jScrollPane1.setViewportView(liFilelist);

        bAdd.setText("Add");
        bAdd.setName("bAdd"); // NOI18N
        bAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddActionPerformed(evt);
            }
        });

        bFile.setText("...");
        bFile.setName("bFile"); // NOI18N
        bFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bFileActionPerformed(evt);
            }
        });

        bView.setText("View");
        bView.setName("bView"); // NOI18N
        bView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bViewActionPerformed(evt);
            }
        });

        jLabel2.setText("Output");
        jLabel2.setName("jLabel2"); // NOI18N

        tfOutfile.setText("/Users/bolav/Documents/training/logs/");
        tfOutfile.setName("tfOutfile"); // NOI18N

        bFileout.setText("...");
        bFileout.setName("bFileout"); // NOI18N
        bFileout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bFileoutActionPerformed(evt);
            }
        });

        bSave.setText("Save");
        bSave.setName("bSave"); // NOI18N
        bSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSaveActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 288, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jLabel2))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(tfOutfile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 187, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(tfFileName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 187, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(bFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(bAdd))
                    .add(bView)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(bFileout, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(bSave)))
                .addContainerGap(132, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(tfFileName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(bAdd)
                    .add(bFile))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel2)
                            .add(tfOutfile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(16, 16, 16))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(bFileout)
                            .add(bSave))
                        .add(18, 18, 18)))
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(bView))
                .addContainerGap(141, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Files", jPanel1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jTabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFileActionPerformed
        browseHRMFile(tfFileName);
}//GEN-LAST:event_bFileActionPerformed

    private void bAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddActionPerformed


        String strHRMFile = tfFileName.getText().trim();
        fileList.add(strHRMFile);
        liFilelist.setListData(fileList);
        document.setOptions(opts);
        try {
            exercises.readExercise(strHRMFile);
            document.setExercise(exercises);
            // document.openExerciseFile(strHRMFile);

            DummyDocument newdoc = new DummyDocument();
            newdoc.setOptions(opts);
            newdoc.setExercise(exercises.getExercise(exercises.getExerciseCount()-1));
            EditSamplePanel newsp = new EditSamplePanel();
            newsp.setDocument(newdoc);
            newsp.displayExercise();
            jTabbedPane.add("Samples "+exercises.getExerciseCount(), newsp);

        }
        catch (de.saring.polarviewer.core.PVException e) {
            System.err.println("Ex: "+e);
        }

}//GEN-LAST:event_bAddActionPerformed

    private void bViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bViewActionPerformed
        diagramPanel.displayExercise();
        samplePanel.displayExercise();
        comparePanel.displayExercise();

    }//GEN-LAST:event_bViewActionPerformed

    private void bFileoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFileoutActionPerformed
        browseHRMFile(tfOutfile);

    }//GEN-LAST:event_bFileoutActionPerformed

    private void bSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSaveActionPerformed
        File initialFile = null;
        String strHRMFile = tfOutfile.getText().trim();
        if (strHRMFile.length () > 0) {
            initialFile = new File(strHRMFile);
        }

        try {
            WriterInterface w;
            w = WriterFactory.getWriter(strHRMFile);
            w.writeFile(initialFile, exercises);
        }
        catch (de.saring.polarviewer.core.PVException pve) {
            System.out.println(pve);
        }
        
    }//GEN-LAST:event_bSaveActionPerformed


    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CombineView().setVisible(true);
            }
        });
    }

    public void browseHRMFile (javax.swing.JTextField field) {
               // do we have an initial HRM file for the dialog?
        STContextDummy ctx = STContextDummy.getDummy();
        HRMFileOpenDialog dlg = new HRMFileOpenDialog(ctx);

        File initialFile = null;
        String strHRMFile = field.getText().trim();
        if (strHRMFile.length () > 0) {
            initialFile = new File(strHRMFile);
        }

        // show file open dialog and display selected filename
        // File selectedFile = null;
        File selectedFile = dlg.selectHRMFile(opts, initialFile);
        if (selectedFile != null) {
            field.setText(selectedFile.getAbsolutePath ());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAdd;
    private javax.swing.JButton bFile;
    private javax.swing.JButton bFileout;
    private javax.swing.JButton bSave;
    private javax.swing.JButton bView;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JList liFilelist;
    private javax.swing.JTextField tfFileName;
    private javax.swing.JTextField tfOutfile;
    // End of variables declaration//GEN-END:variables

}
