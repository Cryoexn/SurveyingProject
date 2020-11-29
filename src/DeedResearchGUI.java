import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DeedResearchGUI extends JFrame {

    private final JLabel lblWater;
    private final JTabbedPane tbPanes;

    private final ActiveJobsPanel activeJobsPanel;
    private final JobSummaryPanel jobSummaryPanel;
    private final SearchParcelsPanel searchParcelsPanel;

    public DeedResearchGUI(TaxRollParser parser, TaxRollFormatting formatter, String jobBaseDir, String templateDir) {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        // Set Values for Main Frame.
        this.setName("Job-Helper");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(600, 320));
        this.setLayout(new BorderLayout(0,10));
        this.setResizable(true);
        this.setIconImage(new ImageIcon(jobBaseDir+"moore_logo.png").getImage());
        this.setLocationRelativeTo(null);

        this.lblWater = new JLabel();
        this.tbPanes = new JTabbedPane();

        // Initialize JPanel with list of active jobs.
        this.activeJobsPanel = new ActiveJobsPanel(jobBaseDir);
        this.activeJobsPanel.setPreferredSize(new Dimension(150, this.getHeight()));
        this.activeJobsPanel.setListListener(new ListListener());
        mainPanel.add(activeJobsPanel, BorderLayout.WEST);

        // Initialize JPanel with components to search tax roll parcels.
        this.searchParcelsPanel = new SearchParcelsPanel(parser, formatter, jobBaseDir, templateDir, this.activeJobsPanel.getSelectedJob());
        this.tbPanes.addTab("Tax Roll Search", null, this.searchParcelsPanel, "Search Tax Rolls For Parcels");

        // Initialize JPanel with list of check boxes.
        this.jobSummaryPanel = new JobSummaryPanel(jobBaseDir, this.activeJobsPanel.getSelectedJob());
        this.tbPanes.addTab("Job Summary", null, this.jobSummaryPanel, "Job Summary");

        this.tbPanes.addChangeListener(new TabChangeListener());

        mainPanel.add(tbPanes, BorderLayout.CENTER);

        // Create water-mark label for top of Frame.
        this.lblWater.setText(String.format("%s : %s", "Moore Land Surveying", activeJobsPanel.getSelectedJob() == null ? "No Job" : activeJobsPanel.getSelectedJob()));
        this.lblWater.setFont(new Font("Times New Roman", Font.BOLD, 30));
        mainPanel.add(lblWater, BorderLayout.NORTH);

        this.add(mainPanel, BorderLayout.CENTER);
        this.setVisible(true);
        this.pack();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                jobSummaryPanel.saveStateOfCheckList();
                activeJobsPanel.saveActiveJobs();
            }
        });
    }

    private void updateWaterMark() {
        lblWater.setText(String.format("%s : %s", "Moore Land Surveying", activeJobsPanel.getSelectedJob() == null ? "No Job" : activeJobsPanel.getSelectedJob()));
    }

    private class TabChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JTabbedPane source = (JTabbedPane)e.getSource();
            if(source.getSelectedIndex() == 0) {
                jobSummaryPanel.saveStateOfCheckList();
            } else if (source.getSelectedIndex() == 1) {
                source.setComponentAt(1, jobSummaryPanel);
            }
        }
    }

    private class ListListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(!e.getValueIsAdjusting()) {
                jobSummaryPanel.updateJobNum(activeJobsPanel.getSelectedJob());
                tbPanes.setComponentAt(1, jobSummaryPanel);
                searchParcelsPanel.updateJobNum(activeJobsPanel.getSelectedJob());
                tbPanes.setComponentAt(0, searchParcelsPanel);
                updateWaterMark();
            }
        }
    }
}
