import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DeedResearchGUI extends JFrame {

    private JLabel lblWater;
    private JTabbedPane tbPanes;
    private JPanel mainPanel;

    private ActiveJobsPanel activeJobsPanel;
    private JobSummaryPanel jobSummaryPanel;
    private SearchParcelsPanel searchParcelsPanel;

    private TaxRollParser parser;
    private TaxRollFormatting formatter;
    private String jobBaseDir;
    private String templateDir;

    public DeedResearchGUI(TaxRollParser parser, TaxRollFormatting formatter, String jobBaseDir, String templateDir) {

        this.parser = parser;
        this.formatter = formatter;
        this.jobBaseDir = jobBaseDir;
        this.templateDir = templateDir;

        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

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
        this.activeJobsPanel = new ActiveJobsPanel(this.jobBaseDir);
        this.activeJobsPanel.setPreferredSize(new Dimension(150, this.getHeight()));
        this.activeJobsPanel.setListListener(new ListListener());
        this.mainPanel.add(activeJobsPanel, BorderLayout.WEST);

        // Initialize JPanel with list of check boxes.
        this.jobSummaryPanel = new JobSummaryPanel(this.jobBaseDir, this.activeJobsPanel.getSelectedJob());
        this.tbPanes.addTab("Job Summary", null, this.jobSummaryPanel, "Job Summary");

        // Initialize JPanel with components to search tax roll parcels.
        this.searchParcelsPanel = new SearchParcelsPanel(this.parser, this.formatter, this.jobBaseDir, this.templateDir, this.activeJobsPanel.getSelectedJob());
        this.tbPanes.addTab("Tax Roll Search", null, this.searchParcelsPanel, "Search Tax Rolls For Parcels");

        this.tbPanes.addChangeListener(new TabChangeListener());

        this.mainPanel.add(tbPanes, BorderLayout.CENTER);

        // Create water-mark label for top of Frame.
        this.lblWater.setText(String.format("%s : %s", "Moore Land Surveying", activeJobsPanel.getSelectedJob() == null ? "No Job" : activeJobsPanel.getSelectedJob()));
        this.lblWater.setFont(new Font("Times New Roman", Font.BOLD, 30));
        this.mainPanel.add(lblWater, BorderLayout.NORTH);

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
                jobSummaryPanel.updateJobNum(activeJobsPanel.getSelectedJob());
                source.setComponentAt(0, jobSummaryPanel);
            } else if (source.getSelectedIndex() == 1) {
                searchParcelsPanel.updateJobNum(activeJobsPanel.getSelectedJob());
                jobSummaryPanel.saveStateOfCheckList();
            }
        }
    }

    private class ListListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(!e.getValueIsAdjusting()) {
                jobSummaryPanel.updateJobNum(activeJobsPanel.getSelectedJob());
                tbPanes.setComponentAt(0, jobSummaryPanel);
                searchParcelsPanel.updateJobNum(activeJobsPanel.getSelectedJob());
                tbPanes.setComponentAt(1, searchParcelsPanel);
                updateWaterMark();
            }
        }
    }
}
