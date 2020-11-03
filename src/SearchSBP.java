import CustomExceptions.TaxRollFileException;
import CustomExceptions.TaxRollFormattingException;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchSBP {

    public static void main(String [] args) {

        // work computer path to jobs. - "Z:\";
        String jobsPath = "/home/cryoexn/IdeaProjects/DeedResearch/data/";
        String templatePath = "/home/cryoexn/IdeaProjects/DeedResearch/data/";

//        if(args.length == 1) {
            SearchSBPGUI gui = new SearchSBPGUI(new TaxRollParser(), new TaxRollFormatting(), jobsPath, templatePath);

//            if(!townCityVillage.equals("q")) {
//
//
//
//                ArrayList<TaxRollParcel> foundParcels = new ArrayList<>();
//                ArrayList<TaxRollParcel> formattedParcels;
//
//                displayMainLoopMenu(jobNum, townCityVillage);
//                String mainLoopInput = scanIn.nextLine();
//                System.out.println();
//
//                while (!mainLoopInput.equals("8")) {
//
//                    switch (mainLoopInput) {
//                        case "1":
//                            if (parser.getTaxRollFile() != null) {
//                                try {
//                                    formattedParcels = formatting.getFormattedUserInput(getUserInput());
//                                    if (formattedParcels != null) {
//                                        foundParcels = parser.searchTaxRollsForValues(formattedParcels);
//                                    }
//                                } catch (TaxRollFormattingException | TaxRollFileException ex) {
//                                    JOptionPane.showMessageDialog(null, ex.getMessage());
//                                }
//                            } else {
//                                JOptionPane.showMessageDialog(null, "Make Sure Your City/Town/Village is Correct.\n");
//                            }
//                            break;
//                        case "2":
//                            if (parser.getTaxRollFile() != null) {
//                                try {
//                                    formattedParcels = formatting.getFormattedUserInput(getUserInput());
//                                    if (formattedParcels != null) {
//                                        foundParcels.addAll(parser.searchTaxRollsForValues(formattedParcels));
//                                    }
//                                } catch (TaxRollFormattingException | TaxRollFileException ex) {
//                                    System.out.println(ex.getMessage());
//                                }
//                            } else {
//                                JOptionPane.showMessageDialog(null, "Make Sure Town Correct.");
//                            }
//                            break;
//                        case "3":
//                            removeParcelsFromFoundList(foundParcels);
//                            break;
//                        case "4":
//                            System.out.println(createTaxParcelSummary(foundParcels));
//                            break;
//                        case "5":
//                            try {
//                                createDirForJobNumber(jobsPath, jobNum);
//                            } catch (TaxRollFileException ex) {
//                                JOptionPane.showMessageDialog(null, ex.getMessage());
//                            }
//                            break;
//                        case "6":
//                            try {
//                                createDeedOutlineForJobNumber(jobsPath, jobNum, foundParcels);
//                            } catch (TaxRollFileException ex) {
//                                JOptionPane.showMessageDialog(null, ex.getMessage());
//                            }
//                            break;
//                        case "7":
//                            try {
//                                createCADTemplateForJobNumber(jobsPath, jobNum);
//                            } catch (TaxRollFileException ex) {
//                                JOptionPane.showMessageDialog(null, ex.getMessage());
//                            }
//                            break;
//                        default:
//                            System.out.println("Invalid Menu Choice.");
//                            break;
//                    }
//
//                    displayMainLoopMenu(jobNum, townCityVillage);
//                    mainLoopInput = scanIn.nextLine();
//                    System.out.println();
//                }
//            }
//
//            System.out.println("Goodbye.");
//
//        } else {
//            StringBuilder output = new StringBuilder();
//            output.append("Invalid Arguments - ");
//            for(String s : args) {
//                output.append(s);
//            }
//            System.out.println(output);
//            System.out.println("Usage - java SearchSBP Job# County City/Town/Village");
//        }
    }

    private static String[] getUserInput() {
        Scanner scanIn = new Scanner(System.in);

        System.out.print("Section-Block-OurParcel,Parcel2,... or b - back\n(ex:127.012-1-1,2)\n> ");
        String userInput = scanIn.nextLine();
        System.out.println();

        while(!isValid(userInput) && !userInput.equals("b")) {
            JOptionPane.showMessageDialog(null, "Invalid Input.");
            System.out.print("\nSection-Block-OurParcel,Parcel2,... or b - back\n(ex:127.012-1-1,2)\n> ");
            userInput = scanIn.nextLine();
            System.out.println();
        }

        if(userInput.equals("b")) {
            return null;
        }

        return userInput.split("-");
    }

    private static void removeParcelsFromFoundList(ArrayList<TaxRollParcel> foundParcels) {
        Scanner scanIn = new Scanner(System.in);

        displayParcelRemovePrompt(foundParcels);
        String choice = scanIn.nextLine();
        System.out.println();

        while(!choice.equals("b")) {
            if(isChoiceValid(choice)) {
                if(Integer.parseInt(choice) > 0 && Integer.parseInt(choice) <= foundParcels.size()) {
                    foundParcels.remove(Integer.parseInt(choice) - 1);
                } else {
                    System.out.println("Invalid Input. Choice not in range.\n");
                }
            } else {
                System.out.println("Invalid Input.\n");
            }

            displayParcelRemovePrompt(foundParcels);
            choice = scanIn.nextLine();
            System.out.println();
        }
    }

    private static void displayParcelRemovePrompt(ArrayList<TaxRollParcel> foundParcels) {
        if(foundParcels.size() > 0) {
            System.out.println("Parcels");
            for(int i = 0; i < foundParcels.size(); i++) {
                System.out.println((i + 1) + " - " + foundParcels.get(i).getSecBlkPcl());
            }
            System.out.println();
        } else {
            System.out.println("No parcels currently in list.\n");
        }

        System.out.println("Enter number for parcel to remove or b - back\n(ex:1)");
        System.out.print(">");
    }

    private static boolean isChoiceValid(String choice) {
        try {
            Integer.parseInt(choice);
            return true;
        } catch (NumberFormatException ignored) {
            // Squash Exception
            return false;
        }
    }

    private static void displayMainLoopMenu(String jobNum, String cityTownVillage) {
        System.out.printf("Options For Job \"%s\" in %s\n", jobNum, cityTownVillage);
        System.out.println("1.) Start New Parcel List");
        System.out.println("2.) Append Parcels To List");
        System.out.println("3.) Remove Parcels");
        System.out.println("4.) Display Parcels");
        System.out.println("5.) Create Job Folder");
        System.out.println("6.) Create DeedOutline");
        System.out.println("7.) Create CADTemplate");
        System.out.println("8.) Quit");
        System.out.print("> ");
    }

    private static boolean isValid(String userInput) {
        try{
            String [] nums = userInput.split("-");

            if (nums.length == 3) {

                //Check that the input was numbers.
                Double.parseDouble(nums[0]);
                Double.parseDouble(nums[1]);

                for(String num : nums[2].split(","))
                    Double.parseDouble(num);

                return true;
            }

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) { /* Squash Exceptions. */ }

        return false;
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

    private static void createDirForJobNumber(String baseDir, String jobNum) throws TaxRollFileException {

        File jobDir = new File(baseDir + jobNum + "/");

        if(jobDir.mkdir())
            System.out.println("Directory for job: " + jobNum + " Created in \"" + baseDir + "\"\n");
        else
            throw new TaxRollFileException("Folder Could Not Be Created! Check that there is not already a folder named \"" + jobNum + "\" in \"" + baseDir + "\"\n");
    }

    private static void createCADTemplateForJobNumber(String baseDir, String jobNum) throws TaxRollFileException {
        String programResourceDir = baseDir + "template.txt";

        File cadTemplateSrc = new File(programResourceDir);
        File jobDir = new File(baseDir + jobNum + "/");
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

    private static void createDeedOutlineForJobNumber(String baseDir, String jobNum, ArrayList<TaxRollParcel> parcels) throws TaxRollFileException {

        File jobDir = new File(baseDir + jobNum + "/");
        File deedOutline = new File(jobDir + "/" + "DeedOutline.txt");
        BufferedWriter bw;

        try {
            if (deedOutline.createNewFile()) {
                System.out.println("Deed Outline: DeedOutline.txt Created in \"" + jobDir + "/\"\n");
            }

            bw = new BufferedWriter(new FileWriter(deedOutline));
            bw.write(createTaxParcelSummary(parcels));

            System.out.println(parcels.size() + " Parcels written to \"DeedOutline.txt\"\n");

            bw.close();

        } catch (IOException ex) {
            if(ex.getMessage().equals("No such file or directory"))
                throw new TaxRollFileException("Job Directory For \"" + jobNum + "\" Has Not Been Created, Make Sure To Do That First.\n");
        }
    }
}
