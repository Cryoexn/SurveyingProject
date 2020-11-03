import CustomExceptions.TaxRollFileException;
import CustomExceptions.TaxRollFormattingException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class SearchSBPGUI extends JFrame {

    private final int INPUT_H_GAP = 30;
    private final int INPUT_V_GAP = 20;

    private final int DISPLAY_H_GAP = 10;
    private final int DISPLAY_V_GAP = 10;

    private final int BUTTON_H_GAP = 20;
    private final int BUTTON_V_GAP = 20;

    private final int FRAME_X = 700;
    private final int FRAME_Y = 345;

    private JTextField txtfJobNumEntry;
    private JTextField txtfParcelInput;
    private JTextArea txtaParcelList;

    private JLabel lblParcels;

    private String jobNum;
    private String jobBaseDir;
    private String templateDir;

    private TaxRollParser parser;
    private TaxRollFormatting formatter;

    private ArrayList<TaxRollParcel> parcelsSearch;
    private ArrayList<TaxRollParcel> parcelsFound;

    public SearchSBPGUI(TaxRollParser parser, TaxRollFormatting formatter, String jobBaseDir, String templateDir) {

        this.jobNum = "";
        this.jobBaseDir = jobBaseDir;
        this.templateDir = templateDir;
        this.parcelsSearch = new ArrayList<>();
        this.parcelsFound = new ArrayList<>();

        this.txtaParcelList = new JTextArea(5,5);
        this.txtaParcelList.setEditable(false);

        this.parser = parser;
        this.formatter = formatter;

        this.parser.setCityTownVillage(CityTownVillageVals.CTV_LIST[0]);
        this.formatter.setTownCity(CityTownVillageVals.CTV_LIST[0]);

        this.setName("SearchSBP");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_X, FRAME_Y);

        JPanel panelMain = new JPanel(new BorderLayout());
        JScrollPane mainFrameScrollPane = new JScrollPane(panelMain, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.add(mainFrameScrollPane);

        panelMain.add(createWaterMark(), BorderLayout.NORTH);
        panelMain.add(createPanelInput(), BorderLayout.CENTER);

        // Use for errors that occur.
        // JOptionPane.showMessageDialog(null, "My Goodness, this is so concise");

        this.setVisible(true);
        this.setResizable(true);
    }

    private JPanel createPanelInput() {
        JPanel panelInput = new JPanel(new GridBagLayout());

        Font inputFont = new Font("Courier New", Font.BOLD, 15);
        Font labelFont = new Font("Times New Roman", Font.BOLD, 15);

        GridBagConstraints lblGbc = new GridBagConstraints();
        GridBagConstraints inpGbc = new GridBagConstraints();

        lblGbc.ipady = 5;
        lblGbc.fill = GridBagConstraints.NONE;
        lblGbc.gridwidth = 1;
        lblGbc.weighty = 2;
        lblGbc.weightx = 1;
        lblGbc.anchor = GridBagConstraints.LINE_START;
        lblGbc.insets = new Insets(0,5,0,5);

        inpGbc.ipady = 5;
        inpGbc.fill = GridBagConstraints.HORIZONTAL;
        inpGbc.gridwidth = 2;
        inpGbc.weighty = 2;
        inpGbc.weightx = 1;
        inpGbc.anchor = GridBagConstraints.CENTER;
        inpGbc.insets = new Insets(0,5,0,5);

        JLabel lblJobNum = new JLabel("Enter Job #", JLabel.RIGHT);
        lblJobNum.setFont(labelFont);

        lblGbc.gridx = 0;
        lblGbc.gridy = 0;

        panelInput.add(lblJobNum, lblGbc);

        txtfJobNumEntry = new JTextField();
        txtfJobNumEntry.addActionListener(new TextFieldJobNumDocListener());
        txtfJobNumEntry.setFont(inputFont);

        inpGbc.gridx = 1;
        inpGbc.gridy = 0;

        panelInput.add(txtfJobNumEntry, inpGbc);

        JLabel lblSelectTCV = new JLabel("Town/City/Village", JLabel.RIGHT);
        lblSelectTCV.setFont(labelFont);

        lblGbc.gridx = 0;
        lblGbc.gridy = 1;

        panelInput.add(lblSelectTCV, lblGbc);

        JComboBox<String> comboBoxCTV = new JComboBox<>(CityTownVillageVals.CTV_LIST);
        comboBoxCTV.setFont(labelFont);
        comboBoxCTV.addActionListener(new ComboBoxTownCityVillageListener());

        inpGbc.gridx = 1;
        inpGbc.gridy = 1;

        panelInput.add(comboBoxCTV, inpGbc);

        JLabel lblParcelInput = new JLabel("Enter Parcels", JLabel.RIGHT);
        lblParcelInput.setFont(labelFont);

        lblGbc.gridx = 0;
        lblGbc.gridy = 2;

        panelInput.add(lblParcelInput, lblGbc);

        txtfParcelInput = new JTextField(10);
        txtfParcelInput.addActionListener(new TextFieldParcelInputListener());
        txtfParcelInput.setFont(inputFont);

        inpGbc.gridx = 1;
        inpGbc.gridy = 2;

        panelInput.add(txtfParcelInput, inpGbc);

        lblParcels = new JLabel("Current Parcel List: ");
        lblParcels.setFont(labelFont);

        lblGbc.insets = new Insets(10, 5, 0,5);
        lblGbc.gridx = 0;
        lblGbc.gridy = 3;
        lblGbc.gridwidth = 2;

        panelInput.add(lblParcels, lblGbc);

        // Reset GridBagConstraints
        lblGbc.insets = new Insets(0, 5, 0,5);

        txtaParcelList.setFont(inputFont);
        JScrollPane scpParcelList = new JScrollPane(txtaParcelList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        inpGbc.gridx = 0;
        inpGbc.gridy = 4;
        inpGbc.gridwidth = 3;

        panelInput.add(scpParcelList, inpGbc);

        inpGbc.gridwidth = 1;

        JButton btnCreateFolder = new JButton("Create Job Folder");
        btnCreateFolder.addActionListener(new ButtonCreateJobFolderListener());
        btnCreateFolder.setFont(labelFont);

        inpGbc.gridx = 0;
        inpGbc.gridy = 5;

        panelInput.add(btnCreateFolder, inpGbc);

        JButton btnCreateDeedOutline = new JButton("Create DeedOutline");
        btnCreateDeedOutline.addActionListener(new ButtonCreateDeedOutlineListener());
        btnCreateDeedOutline.setFont(labelFont);

        inpGbc.gridx = 1;
        inpGbc.gridy = 5;

        panelInput.add(btnCreateDeedOutline, inpGbc);

        JButton btnSearchParcels = new JButton("Search Parcels");
        btnSearchParcels.addActionListener(new ButtonSearchParcelsListener());
        btnSearchParcels.setFont(labelFont);

        inpGbc.gridx = 2;
        inpGbc.gridy = 5;

        panelInput.add(btnSearchParcels, inpGbc);

        return panelInput;
    }

    private JLabel createWaterMark() {
        // new JLabel(new ImageIcon("output-onlinepngtools.png"));
        JLabel lbl = new JLabel("Moore Land Surveying");
        lbl.setFont(new Font("Times New Roman", Font.BOLD, 30));
        return lbl;
    }

    private class TextFieldJobNumDocListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            jobNum = txtfJobNumEntry.getText();
        }
    }

    private class ComboBoxTownCityVillageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<String> cmb = (JComboBox<String>) e.getSource();

            String townCityVillage = (String) cmb.getSelectedItem();

            formatter.setTownCity(townCityVillage);
            parser.setCityTownVillage(townCityVillage);
        }
    }

    private class TextFieldParcelInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField txtF = (JTextField) e.getSource();

            String sbls = txtF.getText();

            if(!sbls.equals("")) {
                try {
                    parcelsSearch = formatter.getFormattedUserInput(sbls.split("-"));
                    parcelsFound = parser.searchTaxRollsForValues(parcelsSearch);

                    txtaParcelList.setText("");
                    lblParcels.setText(String.format("Current Parcel List: Found (%s)", getNumFound()));

                    for (TaxRollParcel parcel : parcelsFound)
                        if(!parcel.getName().equals("Not Found! Double Check Rolls"))
                            txtaParcelList.append(parcel.getSecBlkPcl() + "\n");

                } catch (TaxRollFormattingException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        }
    }

    private class ButtonSearchParcelsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!txtfParcelInput.getText().equals("")) {
                try {
                    parcelsSearch = formatter.getFormattedUserInput(txtfParcelInput.getText().split("-"));
                    parcelsFound = parser.searchTaxRollsForValues(parcelsSearch);

                    txtaParcelList.setText("");
                    lblParcels.setText(String.format("Current Parcel List: Found (%s)", getNumFound()));


                    for (TaxRollParcel parcel : parcelsFound)
                        if(!parcel.getName().equals("Not Found! Double Check Rolls"))
                            txtaParcelList.append(parcel.getSecBlkPcl() + "\n");

                } catch (TaxRollFormattingException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        }
    }

    private String getNumFound() {
        int found = 0;

        for(TaxRollParcel parcel : parcelsFound)
            if(!parcel.getName().equals("Not Found! Double Check Rolls"))
                found++;

        return String.format("%d/%d", found, parcelsSearch.size());
    }

    private class ButtonCreateDeedOutlineListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                jobNum = txtfJobNumEntry.getText();
                createDeedOutlineForJobNumber(jobBaseDir, jobNum, parcelsFound);
            } catch (TaxRollFileException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    private class ButtonCreateJobFolderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            jobNum = txtfJobNumEntry.getText();

            if(!jobNum.equals("")) {
                try {
                    createDirForJobNumber(jobBaseDir, jobNum);
                    createCADTemplateForJobNumber(templateDir, jobNum);
                } catch (TaxRollFileException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        }
    }

    private static void createDirForJobNumber(String jobFileDir, String jobNum) throws TaxRollFileException {

        File jobDir = new File(jobFileDir + jobNum + "/");

        if(jobDir.mkdir())
            JOptionPane.showMessageDialog(null, "Directory for job: " + jobNum + " Created in \"" + jobFileDir + "\"\n");
        else
            throw new TaxRollFileException("Folder Could Not Be Created! Check that there is not already a folder named \"" + jobNum + "\" in \"" + jobFileDir + "\"\n");
    }

    private static void createCADTemplateForJobNumber(String templateDir, String jobNum) throws TaxRollFileException {
        String programResourceDir = templateDir + "template.txt";

        File cadTemplateSrc = new File(programResourceDir);
        File jobDir = new File(templateDir + jobNum + "/");
        File cadTemplate = new File(jobDir + "/" + jobNum + ".txt");

        try {
            if(cadTemplate.createNewFile()) {
                Files.copy(cadTemplateSrc.toPath(), cadTemplate.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("CAD Template Copied to \"" + jobDir + "/\" as: \"" + jobNum + ".dwg\"\n");
            } else {
                throw new TaxRollFileException("CAD Template Could Not Be Created! Check that there is not already a file named \"" + jobNum + ".dwg\" in \"" + jobDir + "/\"\n");
            }
        } catch (IOException ex) {
            if(ex.getMessage().equals("No such file or directory"))
                throw new TaxRollFileException("Job Directory For \"" + jobNum + "\" Has Not Been Created, Make Sure To Do That First.\n");
        }
    }

    private static String createTaxParcelSummary(ArrayList<TaxRollParcel> parcels) {
        StringBuilder summary = new StringBuilder();

        if(parcels.size() > 0) {
            summary.append("+---------------------------------------------------------------------------------------------------------------+\n");
            summary.append("| Our Parcel                                                                                                    |\n");
            summary.append("+--------------------------+------------------------------------------------+--------------+--------------------+\n");
            summary.append("|     Section-Block-Parcel |                                           Name |        Acres |            BOOK-PG |\n");
            summary.append("+--------------------------+------------------------------------------------+--------------+--------------------+\n");
            summary.append(String.format("| %24s | %46s | %12s | %18s |\n", parcels.get(0).getSecBlkPcl(), parcels.get(0).getName() != null ? parcels.get(0).getName() : "!! Manual Intervention Recommended !!", parcels.get(0).getAcres(), parcels.get(0).getInstrument().equals("NOT IN ROLLS") ? "NOT IN ROLLS" : String.format("%s-%s", parcels.get(0).getInstrumentNo(), parcels.get(0).getPageNo())));
            summary.append("+--------------------------+------------------------------------------------+--------------+--------------------+\n\n");

            if (parcels.size() > 1) {
                summary.append("+---------------------------------------------------------------------------------------------------------------+\n");
                summary.append("| Parcels Of Interest                                                                                           |\n");
                summary.append("+--------------------------+------------------------------------------------+--------------+--------------------+\n");
                summary.append("| Section-Block-Parcel     |                                           Name |        Acres |            BOOK-PG |\n");
                summary.append("+--------------------------+------------------------------------------------+--------------+--------------------+\n");

                for (int i = 1; i < parcels.size(); i++) {

                    summary.append(String.format("| %24s | %46s | %12s | %18s |\n", parcels.get(i).getSecBlkPcl(), parcels.get(i).getName() != null ? parcels.get(i).getName() : "!! Manual Intervention Recommended !!", parcels.get(i).getAcres(), parcels.get(i).getInstrument().equals("NOT IN ROLLS") ? "NOT IN ROLLS" : String.format("%s-%s", parcels.get(i).getInstrumentNo(), parcels.get(i).getPageNo())));
                    summary.append("+--------------------------+------------------------------------------------+--------------+--------------------+\n");

                }
            }
        } else {
            summary.append("No Parcels In List.\n");
        }

        return summary.toString();
    }

    private static void createDeedOutlineForJobNumber(String jobBaseDir, String jobNum, ArrayList<TaxRollParcel> parcels) throws TaxRollFileException {

        if(!jobNum.equals("")) {
            File jobDir = new File(jobBaseDir + jobNum + "/");
            File deedOutline = new File(jobDir + "/" + "DeedOutline.txt");
            BufferedWriter bw;

            try {
                if (deedOutline.createNewFile()) {
                    JOptionPane.showMessageDialog(null, "Deed Outline: DeedOutline.txt Created in \"" + jobDir + "/\"\n");
                }

                bw = new BufferedWriter(new FileWriter(deedOutline));
                bw.write(createTaxParcelSummary(parcels));

                JOptionPane.showMessageDialog(null, parcels.size() + " Parcels written to \"DeedOutline.txt\"\n");

                bw.close();

            } catch (IOException ex) {
                if (ex.getMessage().equals("No such file or directory"))
                    throw new TaxRollFileException("Job Directory For \"" + jobNum + "\" Has Not Been Created, Make Sure To Do That First.\n");
            }
        } else {
            throw new TaxRollFileException("Enter a Job # First");
        }
    }
}
