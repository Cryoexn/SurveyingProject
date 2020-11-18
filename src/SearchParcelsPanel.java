import CustomExceptions.TaxRollFileException;
import CustomExceptions.TaxRollFormattingException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

public class SearchParcelsPanel extends JPanel {

    private JTextField txtfParcelInput;
    private JTextArea txtaParcelList;
    private JComboBox<String> comboBoxCTV;
    private JLabel lblParcels;
    private JButton btnClearParcels;
    private JPanel panelMain;

    private String jobNum;
    private String jobBaseDir;
    private String templateDir;

    private TaxRollParser parser;
    private TaxRollFormatting formatter;

    private ArrayList<TaxRollParcel> parcelsSearch;
    private ArrayList<TaxRollParcel> parcelsFound;

    public SearchParcelsPanel(TaxRollParser parser, TaxRollFormatting formatter, String jobBaseDir, String templateDir, String jobNum) {

        this.jobNum = jobNum;
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

        panelMain = new JPanel(new BorderLayout());
        JScrollPane mainFrameScrollPane = new JScrollPane(panelMain, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.add(mainFrameScrollPane);

        panelMain.add(createPanelInput(), BorderLayout.CENTER);

        this.add(panelMain);
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
        lblGbc.insets = new Insets(5,5,0,5);

        inpGbc.ipady = 5;
        inpGbc.fill = GridBagConstraints.HORIZONTAL;
        inpGbc.gridwidth = 2;
        inpGbc.weighty = 2;
        inpGbc.weightx = 1;
        inpGbc.anchor = GridBagConstraints.CENTER;
        inpGbc.insets = new Insets(5,5,0,5);

        JLabel lblSelectTCV = new JLabel("Town/City/Village", JLabel.RIGHT);
        lblSelectTCV.setFont(labelFont);

        lblGbc.gridx = 0;
        lblGbc.gridy = 0;

        panelInput.add(lblSelectTCV, lblGbc);

        comboBoxCTV = new JComboBox<>(CityTownVillageVals.CTV_LIST);
        comboBoxCTV.setFont(labelFont);
        comboBoxCTV.addActionListener(new ComboBoxTownCityVillageListener());

        inpGbc.gridx = 1;
        inpGbc.gridy = 0;

        panelInput.add(comboBoxCTV, inpGbc);

        JLabel lblParcelInput = new JLabel("Enter Parcels", JLabel.RIGHT);
        lblParcelInput.setFont(labelFont);

        lblGbc.gridx = 0;
        lblGbc.gridy = 1;

        panelInput.add(lblParcelInput, lblGbc);

        txtfParcelInput = new JTextField(10);
        txtfParcelInput.addActionListener(new SearchParcelsListener());
        txtfParcelInput.setFont(inputFont);

        inpGbc.gridx = 1;
        inpGbc.gridy = 1;

        panelInput.add(txtfParcelInput, inpGbc);

        lblParcels = new JLabel("Current Parcel List: ");
        lblParcels.setFont(labelFont);

        lblGbc.insets = new Insets(10, 5, 0,5);
        lblGbc.gridx = 0;
        lblGbc.gridy = 2;
        lblGbc.gridwidth = 2;

        panelInput.add(lblParcels, lblGbc);

        btnClearParcels = new JButton("Clear");
        btnClearParcels.setFont(inputFont);
        btnClearParcels.addActionListener(new ClearButtonListener());

        inpGbc.gridx = 2;
        inpGbc.gridy = 2;
        inpGbc.gridwidth = 1;
        inpGbc.anchor = GridBagConstraints.LINE_END;

        panelInput.add(btnClearParcels, inpGbc);

        // Reset GridBagConstraints
        lblGbc.insets = new Insets(0, 5, 0,5);

        txtaParcelList.setFont(inputFont);
        JScrollPane scpParcelList = new JScrollPane(txtaParcelList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        inpGbc.gridx = 0;
        inpGbc.gridy = 3;
        inpGbc.gridwidth = 3;
        inpGbc.anchor = GridBagConstraints.LINE_START;

        panelInput.add(scpParcelList, inpGbc);

        inpGbc.gridwidth = 1;
        inpGbc.insets = new Insets(15,5,15, 5);

        JButton btnCreateFolder = new JButton("Create Job Folder");
        btnCreateFolder.addActionListener(new ButtonCreateJobFolderListener());
        btnCreateFolder.setFont(labelFont);

        inpGbc.gridx = 0;
        inpGbc.gridy = 4;

        panelInput.add(btnCreateFolder, inpGbc);

        JButton btnCreateDeedOutline = new JButton("Create DeedOutline");
        btnCreateDeedOutline.addActionListener(new ButtonCreateDeedOutlineListener());
        btnCreateDeedOutline.setFont(labelFont);

        inpGbc.gridx = 1;
        inpGbc.gridy = 4;

        panelInput.add(btnCreateDeedOutline, inpGbc);

        JButton btnSearchParcels = new JButton("Search Parcels");
        btnSearchParcels.addActionListener(new SearchParcelsListener());
        btnSearchParcels.setFont(labelFont);

        inpGbc.gridx = 2;
        inpGbc.gridy = 4;

        panelInput.add(btnSearchParcels, inpGbc);

        return panelInput;
    }

    private class ClearButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Clearing");
            parcelsFound.clear();
            updateFoundParcels();
        }
    }

    private class ComboBoxTownCityVillageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<String> cmb = (JComboBox<String>) e.getSource();

            String townCityVillage = (String) cmb.getSelectedItem();

            formatter.setTownCity(townCityVillage);
            parser.setCityTownVillage(townCityVillage);

            if(!txtfParcelInput.getText().equals("")) {
                searchRolls();
            }
        }
    }

    private class SearchParcelsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            searchRolls();
        }
    }

    private void searchRolls() {
        if(!txtfParcelInput.getText().equals("") && isValidFmt(txtfParcelInput.getText())) {
            try {
                String ctv = (String) comboBoxCTV.getSelectedItem();

                formatter.setTownCity(ctv);
                parser.setCityTownVillage(ctv);

                parcelsSearch = formatter.getFormattedUserInput(txtfParcelInput.getText().split("-"));

                ArrayList<TaxRollParcel> parcelsGotten = parser.searchTaxRollsForValues(parcelsSearch);

                boolean foundFlag = false;

                for(TaxRollParcel pclGotten : parcelsGotten){
                    for(TaxRollParcel pclFound : parcelsFound) {
                        if(pclGotten.getSecBlkPcl().equals(pclFound.getSecBlkPcl())){
                            foundFlag = true;
                        }
                    }

                    if(!foundFlag)
                        parcelsFound.add(pclGotten);

                    foundFlag = false;
                }

                if(parcelsFound != null) {
                    updateFoundParcels();
                } else {
                    lblParcels.setText(String.format("Current Parcel List: %s", "No Tax Roll File"));
                }

            } catch (TaxRollFormattingException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } else {
            lblParcels.setText(String.format("Current Parcel List: %s", "Invalid Format"));
        }
    }

    private void updateFoundParcels() {
        txtaParcelList.setText("");
        if(parcelsFound.size() != 0){
            for (TaxRollParcel parcel : parcelsFound) {
                if (!parcel.getName().equals("Not Found! Double Check Rolls") && !txtaParcelList.getText().contains(parcel.getSecBlkPcl())) {
                    txtaParcelList.append(parcel.getSecBlkPcl() + "\n");
                }
            }
            lblParcels.setText(String.format("Current Parcel List: Found (%s)", getNumFound()));
        } else {
            lblParcels.setText(String.format("Current Parcel List: ", getNumFound()));
        }
    }

    private boolean isValidFmt(String parcels) {
        try {
            return parcels.split("-").length == 3 || parcels.split("-").length == 2;
        } catch (PatternSyntaxException ignored){
            return false;
        }
    }

    private String getNumFound() {
        int found = 0;

        if(parcelsFound != null) {
            for (TaxRollParcel parcel : parcelsFound)
                if (!parcel.getName().equals("Not Found! Double Check Rolls"))
                    found++;

            return String.format("%d/%d", found, parcelsSearch.size());
        }

        return null;
    }

    private class ButtonCreateDeedOutlineListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                createDeedOutlineForJobNumber(jobBaseDir, jobNum, parcelsFound);
            } catch (TaxRollFileException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    private class ButtonCreateJobFolderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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

    private void createDirForJobNumber(String jobFileDir, String jobNum) throws TaxRollFileException {

        File jobDir = new File(jobFileDir + jobNum + "/");

        if(jobDir.mkdir())
            JOptionPane.showMessageDialog(null, "Directory for job: " + jobNum + " Created in \"" + jobFileDir + "\"\n");
        else
            throw new TaxRollFileException("Folder Could Not Be Created! Check that there is not already a folder named \"" + jobNum + "\" in \"" + jobFileDir + "\"\n");
    }

    private void createCADTemplateForJobNumber(String templateDir, String jobNum) throws TaxRollFileException {
        String programResourceDir = templateDir + "template.dwg";

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

    private String createTaxParcelSummary(ArrayList<TaxRollParcel> parcels) {
        StringBuilder summary = new StringBuilder();

        summary.append("Job #: " + this.jobNum + "\n");
        summary.append("\n");

        if(parcels.size() > 0) {
            for (int i = 0; i < parcels.size(); i++) {
                TaxRollParcel pcl = parcels.get(i);
                summary.append(pcl.getSecBlkPcl() + "\n");
                summary.append(pcl.getName() + "\n");
                summary.append(pcl.getAddress() + "\n");
                summary.append(pcl.getAcres() + "\n");
                summary.append(String.format("BOOK %s\tPG %s", pcl.getBookNo(), pcl.getPageNo()));
                summary.append("\n\n");
            }
        } else {
            summary.append("No Parcels In List.\n");
        }

        return summary.toString();
    }

    private void createDeedOutlineForJobNumber(String jobBaseDir, String jobNum, ArrayList<TaxRollParcel> parcels) throws TaxRollFileException {

        if(!jobNum.equals("")) {
            File jobDir = new File(jobBaseDir + jobNum + File.separator);
            File deedOutline = new File(jobDir + File.separator + jobNum +"-Deed-Outline.txt");
            BufferedWriter bw;
            StringBuilder msg = new StringBuilder();

            try {
                if (deedOutline.createNewFile()) {
                    msg.append(jobNum + "-Deed-Outline.txt Created in \"" + jobDir + "/\"\n");
                }

                bw = new BufferedWriter(new FileWriter(deedOutline));
                bw.write(createTaxParcelSummary(parcels));

                msg.append(parcels.size() + " Parcels written to \"" + jobNum + "-Deed-Outline.txt\"\n");

                bw.flush();
                bw.close();

                JOptionPane.showMessageDialog(null, msg.toString());

            } catch (IOException ex) {
                if (ex.getMessage().equals("No such file or directory"))
                    throw new TaxRollFileException("Job Directory For \"" + jobNum + "\" Has Not Been Created, Make Sure To Do That First.\n");
            }
        } else {
            throw new TaxRollFileException("Enter a Job # First");
        }
    }

    public void updateJobNum(String jobNum) {
        this.jobNum = jobNum;
    }
}
