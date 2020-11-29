import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class ActiveJobsPanel extends JPanel {

    private final JList<String> activeJobsList;
    private final JScrollPane scrollPane;
    private final String jobBaseDir;
    private final JFileChooser fileChooser;
    private final ArrayList<String> activeJobs;

    public ActiveJobsPanel(String jobBaseDir) {
        this.jobBaseDir = jobBaseDir;
        this.activeJobs = loadActiveJobs();

        this.setLayout(new BorderLayout());

        this.activeJobsList = new JList<>(activeJobs.toArray(new String[0]));
        this.activeJobsList.setSelectedIndex(0);

        this.scrollPane = new JScrollPane(activeJobsList);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JLabel lblJobList = new JLabel("Active Jobs");
        lblJobList.setFont(new Font("Times New Roman", Font.BOLD, 16));

        JPanel btnPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;

        JButton btnAddJobs = new JButton("+");
        btnAddJobs.addActionListener(new AddJobsButtonListener());
        btnAddJobs.setSize(1,1);

        JButton btnRemoveJobs = new JButton("-");
        btnRemoveJobs.addActionListener(new RemoveJobsButtonListener());
        this.setSize(1,1);

        btnPanel.add(btnAddJobs, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        btnPanel.add(btnRemoveJobs, gbc);

        final File fileChooserFile = new File(this.jobBaseDir);
        this.fileChooser = new JFileChooser(fileChooserFile);
        this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.fileChooser.setMultiSelectionEnabled(true);

        this.add(lblJobList, BorderLayout.NORTH);
        this.add(this.scrollPane, BorderLayout.CENTER);
        this.add(btnPanel, BorderLayout.SOUTH);
    }

    private ArrayList<String> loadActiveJobs() {
        ArrayList<String> jobsList = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(this.jobBaseDir+"active-jobs.txt")));
            String activeJobsLine = br.readLine();

            if(activeJobsLine != null)
                jobsList.addAll(Arrays.asList(activeJobsLine.split(",")));

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jobsList;
    }

    public void saveActiveJobs() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.jobBaseDir+"active-jobs.txt")));

            for(String job : this.activeJobs)
                bw.write(job+",");

            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSelectedJob() {
        return this.activeJobsList.getSelectedValue();
    }

    public void setListListener(ListSelectionListener listListener) {
        this.activeJobsList.addListSelectionListener(listListener);
    }

    private class AddJobsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int returnVal = fileChooser.showOpenDialog(scrollPane);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                final File [] files = fileChooser.getSelectedFiles();

                for(final File f : files)
                    if(!activeJobs.contains(f.getName()))
                        activeJobs.add(f.getName());

                activeJobs.sort((o1, o2) -> {
                    try {
                        String[] firstComps = o1.split("-");
                        String[] secondComps = o2.split("-");

                        return Integer.valueOf(firstComps[0]).compareTo(Integer.valueOf(secondComps[0])) + Integer.valueOf(firstComps[1]).compareTo(Integer.valueOf(secondComps[1]));

                    } catch (Exception ignored) {
                        /* Squash Exception */
                        return -1;
                    }
                });

                activeJobsList.setListData(activeJobs.toArray(new String[0]));
            } else {
                System.out.println("Open command cancelled by user.");
            }
        }
    }

    private class RemoveJobsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> values = activeJobsList.getSelectedValuesList();
            for(String value : values) {
                activeJobs.remove(value);
            }
            activeJobsList.setListData(activeJobs.toArray(new String[0]));
        }
    }
}