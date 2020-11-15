import org.bouncycastle.crypto.agreement.jpake.JPAKEParticipant;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.File;
import java.util.ArrayList;

public class CreateComponents {

    static JPanel createActiveJobsPanel(String jobsBaseDir) {
        JPanel activeJobs = new JPanel();
        JList<String> jobs = new JList<>(getActiveJobs(jobsBaseDir));
        JScrollPane jobsScroll = new JScrollPane(jobs);



        return activeJobs;
    }

    private static String[] getActiveJobs(String jobBaseDir) {
        File jobsDir = new File(jobBaseDir);
        ArrayList<String> activeJobs = new ArrayList<>();

        int i = 0;

        for(File jobFolder : jobsDir.listFiles()) {
            if(jobFolder.isDirectory()) {
                activeJobs.add(jobFolder.getName());
                i++;
            }
        }

        return activeJobs.toArray(new String[activeJobs.size()]);
    }

    static JPanel createSummaryPanel() {
        return null;
    }

    static JPanel createParcelSearchPanel() {
        return null;
    }
}
