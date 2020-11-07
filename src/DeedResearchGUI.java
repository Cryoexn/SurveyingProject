import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class DeedResearchGUI extends JFrame {

    private final int NUM_ITEMS = 10;

    private JTextField txtJobNumInput;
    private JButton btnJobNumInput;

    JTabbedPane tbPanes;

    private JPanel jobInpPanel;
    private JPanel taxRollPanel;
    private JPanel ckListPanel;

    private TaxRollParser parser;
    private TaxRollFormatting formatter;
    private String jobBaseDir;
    private String templateDir;

    public DeedResearchGUI(TaxRollParser parser, TaxRollFormatting formatter, String jobBaseDir, String templateDir) {

        this.parser = parser;
        this.formatter = formatter;
        this.jobBaseDir = jobBaseDir;
        this.templateDir = templateDir;

        this.setName("DeedResearchHelper");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(600, 360));

        this.tbPanes = new JTabbedPane();

        this.jobInpPanel = new JPanel();
        this.taxRollPanel = null;
        this.ckListPanel = null;

        Font inputFont = new Font("Courier New", Font.BOLD, 30);

        txtJobNumInput = new JTextField(15);
        txtJobNumInput.setFont(inputFont);
        txtJobNumInput.addActionListener(new JobNumSubmitListener());
        jobInpPanel.add(txtJobNumInput);

        btnJobNumInput = new JButton("Submit");
        btnJobNumInput.setFont(inputFont);
        btnJobNumInput.addActionListener(new JobNumSubmitListener());
        this.jobInpPanel.add(btnJobNumInput);

        tbPanes.addTab("Job Num", null, this.jobInpPanel, "Enter Job Number");
        tbPanes.addTab("Tax Roll", null, this.taxRollPanel, "Search Tax Rolls For Parcels");
        tbPanes.addTab("Check List", null, this.ckListPanel, "Check list for specified job");

        tbPanes.setEnabledAt(1, false);
        tbPanes.setEnabledAt(2, false);

        tbPanes.addChangeListener(new TabChangeListener());

        this.add(tbPanes);

        this.setVisible(true);

        this.txtJobNumInput.requestFocus();
    }

    private class TabChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JTabbedPane source = (JTabbedPane)e.getSource();

            if(source.getSelectedIndex() == 2) {
                tbPanes.setComponentAt(source.getSelectedIndex(), initalizeListPanel(txtJobNumInput.getText()));
            }
        }
    }

    private class JobNumSubmitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tbPanes.setComponentAt(1, new SearchSBPGUI(parser, formatter, jobBaseDir, templateDir, txtJobNumInput.getText()));
            tbPanes.setComponentAt(2, initalizeListPanel(txtJobNumInput.getText()));

            tbPanes.setEnabledAt(1, true);
            tbPanes.setEnabledAt(2, true);

            tbPanes.requestFocus();
        }
    }

    private JPanel initalizeListPanel(String jobNum) {
        ArrayList<JCheckBox> checks = new ArrayList<JCheckBox>();
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_START;

        Font listFont = new Font("Times New Roman", Font.BOLD, 15);
        try {
            File checkList = new File(jobBaseDir+jobNum+"/"+jobNum+"-ck-list.txt");

            if(checkList.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(checkList));

                String [] lines = new String[NUM_ITEMS + 4];
                String lineComps[];

                for(int i = 0; i < lines.length; i++)
                    lines[i] = br.readLine();

                JCheckBox tempCk;

                for(int i = 3; i < lines.length - 1; i ++) {
                    lineComps = lines[i].replace("|", "").split(":");
                    tempCk = new JCheckBox(lineComps[0].strip(), lineComps[1].strip().equals("X"));
                    tempCk.setFont(listFont);
                    checks.add(tempCk);
                }

                for(int i = 0; i < checks.size(); i++){
                    gc.gridy = i;
                    panel.add(checks.get(i), gc);
                }

                br.close();
            } else {
                createCheckListForJobNumber(jobBaseDir, txtJobNumInput.getText());
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        return panel;
    }
    private void createCheckListForJobNumber(String jobBaseDir, String jobNum) {
        StringBuilder checkList = new StringBuilder();

        File checkListFile = new File(jobBaseDir+jobNum+"-ck-list.txt");

        if(!checkListFile.exists()) {
            checkList.append("+------------------------------+\n");
            checkList.append(String.format("| Job:                  %s |\n", jobNum));
            checkList.append("+------------------------------+\n");
            checkList.append("| Check in Job-Book        :   |\n");
            checkList.append("| Create Job-Book entry    :   |\n");
            checkList.append("| Get Tax Map Printout     :   |\n");
            checkList.append("| Create Deed Outline      :   |\n");
            checkList.append("| Get Deeds parcels        :   |\n");
            checkList.append("| Note Stored Maps/Deeds   :   |\n");
            checkList.append("| Enter Deeds to CAD       :   |\n");
            checkList.append("| Create Plot Map          :   |\n");
            checkList.append("| Band related Files       :   |\n");
            checkList.append("| Sticky Note Stored Items :   |\n");
            checkList.append("+------------------------------+");

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(jobBaseDir+jobNum+"/"+jobNum+"-ck-list.txt"));

                bw.write(checkList.toString());
                bw.flush();
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File exists");
        }
    }
}
