import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class ActiveJobsPanel extends JPanel {

    private JList<String> activeJobsList;
    private JScrollPane scrollPane;
    private JLabel lblJobList;
    private JPanel btnPanel;
    private JButton btnAddJobs;
    private String jobBaseDir;
    private JFileChooser fileChooser;
    private ArrayList<String> activeJobs;

    public ActiveJobsPanel(String jobBaseDir) {
        this.jobBaseDir = jobBaseDir;
        this.activeJobs = new ArrayList<>();

        this.setLayout(new BorderLayout());

        this.activeJobsList = new JList<>(activeJobs.toArray(new String[0]));
        this.activeJobsList.setSelectedIndex(0);

        this.scrollPane = new JScrollPane(activeJobsList);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.lblJobList = new JLabel("Active Jobs");
        this.lblJobList.setFont(new Font("Times New Roman", Font.BOLD, 16));

        this.btnPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;

        this.btnAddJobs = new JButton("+");
        this.btnAddJobs.addActionListener(new AddJobsButtonListener());
        this.btnAddJobs.setSize(2,2);

        this.btnPanel.add(this.btnAddJobs, gbc);

        final File fileChooserFile = new File(this.jobBaseDir);
        this.fileChooser = new JFileChooser(fileChooserFile);
        this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.fileChooser.setMultiSelectionEnabled(true);

        this.add(this.lblJobList, BorderLayout.NORTH);
        this.add(this.scrollPane, BorderLayout.CENTER);
        this.add(this.btnPanel, BorderLayout.SOUTH);
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
                    activeJobs.add(f.getName());
                activeJobsList.setListData(activeJobs.toArray(new String[0]));
            } else {
                System.out.println("Open command cancelled by user.");
            }
        }
    }
}