import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class JobCheckListGUI extends JFrame {

    ArrayList<JCheckBox> ckList;

    public JobCheckListGUI(String jobNum) {

        ckList = initalizeList(jobNum);
    }

    private ArrayList<JCheckBox> initalizeList(String jobNum) {
        ArrayList<JCheckBox> checks = new ArrayList<JCheckBox>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(jobNum+"/"+jobNum+"-cklist.txt"));

        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        return checks;
    }
}
