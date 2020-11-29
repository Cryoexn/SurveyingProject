import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class JobSummaryPanel extends JPanel {

    private final String jobBaseDir;
    private String jobNum;
    private JScrollPane scp;
    private JTextField txtAddItem;
    private Box box;
    private ArrayList<JCheckBox> checks;

    public JobSummaryPanel(String jobBaseDir, String jobNum) {
        this.jobBaseDir = jobBaseDir;
        this.jobNum = jobNum;
        this.box = Box.createVerticalBox();

        this.txtAddItem = new JTextField();
        this.txtAddItem.addActionListener(e -> {
            this.checks.add(new JCheckBox(this.txtAddItem.getText()));
            this.txtAddItem.setText("");
            this.txtAddItem.requestFocus();
            saveStateOfCheckList();
            updateChecklist();
        });

        this.scp = new JScrollPane(box);
        this.scp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.setLayout(new BorderLayout());

        this.checks = new ArrayList<>();

        if(this.jobNum != null)
            createCheckList();

        this.add(this.txtAddItem, BorderLayout.SOUTH);
    }

    private void updateChecklist() {
        this.box.removeAll();

        for(JCheckBox ck : checks)
            this.box.add(ck);

        this.revalidate();
    }

    public void updateJobNum(String jobNum) {
        this.jobNum = jobNum;
        if(this.jobNum != null)
            createCheckList();
    }

    private void createCheckList() {
        Font listFont = new Font("Times New Roman", Font.BOLD, 15);

        this.checks.clear();
        this.box.removeAll();

        try {
            File checkList = new File(this.jobBaseDir+this.jobNum+File.separator+this.jobNum+"-ck-list.txt");

            if(checkList.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(checkList));

                String [] lineComps;
                String line;
                JCheckBox tempCk;

                while((line = br.readLine()) != null) {
                    lineComps = line.split(",");
                    tempCk = new JCheckBox(lineComps[0].strip(), lineComps[1].strip().equals("X"));
                    tempCk.setForeground(tempCk.isSelected() ? Color.GRAY : Color.BLACK);
                    tempCk.setFont(listFont);
                    tempCk.addActionListener(new CheckBoxListener());
                    this.checks.add(tempCk);
                    this.box.add(tempCk);
                }

                this.add(scp, BorderLayout.CENTER);

                br.close();

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
            checkList.append(check.isSelected() ? "X" : "O");
            checkList.append("\n");
        }

        try {
            if(this.jobNum != null) {
                File checkListFile = new File(this.jobBaseDir + this.jobNum + "/" + this.jobNum + "-ck-list.txt");
                BufferedWriter bw = new BufferedWriter(new FileWriter(checkListFile));
                bw.write(checkList.toString());

                bw.flush();
                bw.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void createCheckListForJobNumber() {
        StringBuilder checkList = new StringBuilder();

        File checkListFile = new File(this.jobBaseDir+this.jobNum+"/"+this.jobNum+"-ck-list.txt");

        if(!checkListFile.exists()) {
            checkList.append("Check in Job-Book,O\n");
            checkList.append("Create Job-Book entry,O\n");
            checkList.append("Get Tax Map Printout,O\n");
            checkList.append("Create Deed Outline,O\n");
            checkList.append("Get Deeds parcels,O\n");
            checkList.append("Note Stored Maps/Deeds,O\n");
            checkList.append("Enter Deeds to CAD,O\n");
            checkList.append("Create Plot Map,O\n");
            checkList.append("Band related Files,O\n");
            checkList.append("Sticky Note Stored Items,O\n");

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
            ((JCheckBox)e.getSource()).setForeground(((JCheckBox)e.getSource()).isSelected() ? Color.LIGHT_GRAY : Color.BLACK);
        }
    }
}
