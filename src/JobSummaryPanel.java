import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class JobSummaryPanel extends JPanel {

    private final int NUM_ITEMS = 10;

    private String jobBaseDir;
    private String jobNum;

    private ArrayList<JCheckBox> checks;


    public JobSummaryPanel(String jobBaseDir, String jobNum) {
        this.jobBaseDir = jobBaseDir;
        this.jobNum = jobNum;

        this.setLayout(new GridBagLayout());

        checks = new ArrayList<JCheckBox>();

        if(this.jobNum != null)
            createCheckList();
    }

    public void updateJobNum(String jobNum) {
        this.jobNum = jobNum;
        if(this.jobNum != null)
            createCheckList();
    }

    private void createCheckList() {
        Font listFont = new Font("Times New Roman", Font.BOLD, 15);

        this.checks.clear();
        this.removeAll();

        try {
            File checkList = new File(this.jobBaseDir+this.jobNum+"/"+this.jobNum+"-ck-list.txt");

            if(checkList.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(checkList));

                String [] lines = new String[NUM_ITEMS];
                String lineComps[];

                for(int i = 0; i < lines.length; i++)
                    lines[i] = br.readLine();

                JCheckBox tempCk;

                for(int i = 0; i < lines.length - 1; i ++) {
                    lineComps = lines[i].split(",");
                    tempCk = new JCheckBox(lineComps[0].strip(), lineComps[1].strip().equals("X"));
                    tempCk.setForeground(tempCk.isSelected() ? Color.GREEN : Color.RED);
                    tempCk.setFont(listFont);
                    tempCk.addActionListener(new CheckBoxListener());
                    checks.add(tempCk);
                }

                JPanel checkBoxPanel = new JPanel();
                checkBoxPanel.setLayout(new GridBagLayout());

                GridBagConstraints gc = new GridBagConstraints();

                gc.gridx = 0;
                gc.gridy = 0;
                gc.gridwidth = 1;
                gc.weighty = 1;
                gc.anchor  = GridBagConstraints.LINE_START;

                for(int i = 0; i < checks.size(); i++) {
                    gc.gridy = i;
                    checkBoxPanel.add(checks.get(i), gc);
                }

                br.close();

                JScrollPane scp = new JScrollPane(checkBoxPanel);

                scp.setBorder(null);
                scp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

                gc.gridx = 0;
                gc.gridy = 0;
                gc.gridwidth = 1;
                gc.weightx = 1;
                gc.anchor = GridBagConstraints.FIRST_LINE_START;

                this.add(scp, gc);

            } else {
                createCheckListForJobNumber();
                createCheckList();
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void saveStateOfCheckList() {
        StringBuilder checkList = new StringBuilder();

        for(JCheckBox check : this.checks) {
            checkList.append(check.getText());
            checkList.append(",");
            checkList.append(check.isSelected() ? "X" : " ");
            checkList.append("\n");
        }
        try {
            File checkListFile = new File(this.jobBaseDir + this.jobNum + "/" + this.jobNum + "-ck-list.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(checkListFile));
            bw.write(checkList.toString());

            bw.flush();
            bw.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void createCheckListForJobNumber() {
        StringBuilder checkList = new StringBuilder();

        File checkListFile = new File(this.jobBaseDir+this.jobNum+"/"+this.jobNum+"-ck-list.txt");

        if(!checkListFile.exists()) {
            checkList.append("Check in Job-Book, \n");
            checkList.append("Create Job-Book entry, \n");
            checkList.append("Get Tax Map Printout, \n");
            checkList.append("Create Deed Outline, \n");
            checkList.append("Get Deeds parcels, \n");
            checkList.append("Note Stored Maps/Deeds, \n");
            checkList.append("Enter Deeds to CAD, \n");
            checkList.append("Create Plot Map, \n");
            checkList.append("Band related Files, \n");
            checkList.append("Sticky Note Stored Items, \n");

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(checkListFile));

                bw.write(checkList.toString());
                bw.flush();
                bw.close();

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("File exists");
        }
    }

    private static class CheckBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((JCheckBox)e.getSource()).setForeground(((JCheckBox)e.getSource()).isSelected() ? Color.GREEN : Color.RED);
        }
    }
}
