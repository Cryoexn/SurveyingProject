import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

// When a line is a Deed Book and the next line is not a Star make the next line a Star after getting
// the PayRollParcels. The mailing address loop is not able to differ from end of file and last parcel.
// remove the Sub - Total replace line.

public class ConvertTaxRolls {
    public static void main(String []args) {
        try {
            File pdfFolder            = new File(args[0] + "taxrolls-pdf/");
            File[] pdfFiles = Objects.requireNonNull(pdfFolder.listFiles());

            for (File pdfFile : pdfFiles) {

                System.out.println("--");

                ArrayList<String> lineList = convertPDFToTxt(pdfFile);
                ArrayList<String> listCopy = new ArrayList<>(lineList);

                removeUnwantedText(lineList);
                lineList = getPayRollParcels(lineList);

                // Change the pdf Dir String to the txt Dir
                final String parcelTxtDir = pdfFile.toString().replace("taxrolls-pdf" + File.separator, "taxrolls-txt" + File.separator);

                writeTxtToFile(lineList, new File(parcelTxtDir.replace(".pdf", ".txt")));
                createFormattedParcelFile(lineList, listCopy, new File(parcelTxtDir.replace(".pdf", "-Parcels.txt")));
            }
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("--");
        System.out.println("Complete.");
    }

    private static void removeUnwantedText(ArrayList<String> list) {

        System.out.print("Removing Unwanted Lines... ");

        list.removeIf(line -> line.equals("************************************************************************************************************************************ "));
        list.removeIf(line -> line.equals(" ************************************************************************************************************************************"));
        list.removeIf(line -> line.contains("COUNTY -"));
        list.removeIf(line -> line.contains("TOWN -"));
        list.removeIf(line -> line.contains("VILLAGE -"));
        list.removeIf(line -> line.contains("CITY -"));
        list.removeIf(line -> line.contains("SWIS -"));
        list.removeIf(line -> line.contains("CODE -"));
        list.removeIf(line -> line.contains("INSIDE -") || line.contains("INSIDE  -"));
        list.removeIf(line -> line.contains("UNDER AGDIST"));
        list.removeIf(line -> line.contains("CURRENT OWNERS"));
        list.removeIf(line -> line.contains("STATE OF NEW YORK"));
        list.removeIf(line -> line.contains("TAX MAP PARCEL NUMBER"));
        list.removeIf(line -> line.contains("TAX MAP PARCEL NUMBER"));
        list.removeIf(line -> line.strip().equals(","));
        list.removeIf(line -> line.charAt(0) == ' ' && line.contains("EAST") || line.contains("NRTH") || line.contains("FULL MARKET VALUE") || line.contains("DPTH") || line.contains("FRNT"));

        System.out.println("Done");
    }

    private static ArrayList<String> getPayRollParcels(ArrayList<String> list) {
        ArrayList<String> updatedText = new ArrayList<>();

        String sepSBL;
        String laSep;

        System.out.print("Separating Parcels... ");

        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).contains("DEED") || list.get(i).contains("BOOK") || list.get(i).contains("ACRES") || list.get(i).contains("LIBER")) {
                list.set(i, list.get(i).strip());
            } else if(list.get(i).contains("******************************************************************************************************* ")) {
                 sepSBL = list.get(i).split("( +)")[1].strip().replace("*", "");
                 laSep = list.get(i+1).split("( +)")[0].strip();

                 if(!laSep.equals(sepSBL)) {
                     list.set(i + 1, list.get(i+1).strip());
                 } else {
                     list.set(i + 1, "No Address");
                     String tempName = list.get(i + 2);
                     list.set(i + 2, laSep.strip());
                     list.set(i + 3, tempName.split("(  +)")[0] + ", " + list.get(i+3).split("(  +)")[0].strip());
                 }
            }
        }

        for(String line : list) {
            if (!(line.contains("DEED") || line.contains("BOOK") || line.contains("ACRES") || line.contains("LIBER"))) {
                String firstComp = line.split(" {3}")[0];
                if (!firstComp.isBlank())
                    updatedText.add(firstComp);

            } else if (line.contains("ACRES") && line.replaceAll("( +)", " ").split(" ")[0].equals("ACRES")) {
                String[] acresComps = line.replaceAll("( +)", " ").split(" ");
                updatedText.add(acresComps[0] + " " + acresComps[1]);
            } else if (line.contains("ACRES") && !line.replaceAll("( +)", " ").split(" ")[0].equals("ACRES")) {
                String[] acresComps = line.replaceAll("( +)", " ").split(" ");
                StringBuilder aLine = new StringBuilder();
                int i = 0;

                while (!acresComps[i].equals("ACRES")) {
                    aLine.append(" ").append(acresComps[i]);
                    i++;
                }

                updatedText.add(aLine.toString().trim());
                updatedText.add(acresComps[i] + " " + acresComps[i + 1]);

            } else if((line.contains("DEED BOOK") || line.contains("LIBER")) && line.replaceAll("( +)", " ").split(" ")[0].equals("DEED") || line.replaceAll("( +)", " ").split(" ")[0].equals("LIBER")) {
                String [] deedComps = line.replaceAll("( +)", " ").split(" ");

                if (!deedComps[2].equals("00000") && deedComps.length > 3)
                    updatedText.add(deedComps[0] + " " + deedComps[1] + " " + deedComps[2] + " " + deedComps[3]);
                else
                    updatedText.add(deedComps[0] + " " + deedComps[1] + " " + deedComps[2]);

            } else if(line.contains("DEED BOOK") && !line.replaceAll("( +)", " ").split(" ")[0].equals("DEED")) {
                String [] deedComps = line.replaceAll("( +)", " ").split(" ");
                StringBuilder aLine = new StringBuilder();
                int i = 0;

                while (!deedComps[i].equals("DEED")) {
                    aLine.append(" ").append(deedComps[i]);
                    i++;
                }
                updatedText.add(aLine.toString().trim());
                if(!deedComps[2].equals("00000"))
                    updatedText.add(deedComps[i] + " " + deedComps[i + 1] + " " + deedComps[i + 2] + " " + deedComps[i + 3]);
                else
                    updatedText.add(deedComps[i] + " " + deedComps[i + 1] + " " + deedComps[i + 2]);

            } else {
                updatedText.add(line);
            }
        }

        for(int i = 0; i < updatedText.size() - 1; i++) {
            boolean lineContainsAcres = updatedText.get(i).split(" ")[0].equals("ACRES");
            boolean nextLineDoesntContainDeedLiberStar = !(updatedText.get(i+1).split(" ")[0].equals("DEED") ||
                                                         updatedText.get(i+1).split(" ")[0].equals("LIBER") ||
                                                         updatedText.get(i+1).split(" ")[0].equals("*".repeat(103)));
            if(lineContainsAcres && nextLineDoesntContainDeedLiberStar) {
                String temp = updatedText.get(i+1);
                updatedText.set(i+1, updatedText.get(i));
                updatedText.set(i, temp);
            }
        }

        System.out.println("Done");

        return updatedText;
    }

    private static ArrayList<String> convertPDFToTxt(File pdf) throws IOException {
        PDDocument document = PDDocument.load(pdf);
        PDFTextStripper pdfStripper = new PDFTextStripper();

        System.out.printf("Converting %s to Txt... ", pdf.toString());

        ArrayList<String> lines = new ArrayList<>(Arrays.asList(pdfStripper.getText(document).split("\n")));

        document.close();

        System.out.println("Done");

        return lines;
    }

    private static void writeTxtToFile(ArrayList<String> text, File file) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        System.out.print("Writing Un-Formatted Parcels to File... ");

        for (String line : text) {
            bw.write(line + "\n");
        }

        bw.flush();
        bw.close();

        System.out.println("Done");
    }

    private static void createFormattedParcelFile(ArrayList<String> lineList, ArrayList<String> copy, File formattedParcelFile) {
        System.out.print("Creating Formatted Parcels File... ");

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(formattedParcelFile));

            boolean containedAcres;
            boolean containedDeedbook;
            int numLines;

            for(int i = 0; i < lineList.size(); i++) {

                //Reset flags
                containedAcres = false;
                containedDeedbook = false;

                if (lineList.get(i).contains("******************************************************************************************************* ")) {
                    // Maybe Change the get sec-blk-pcl to the separator numbers.
                    String address = lineList.get(++i);
                    String secBlkPcl = lineList.get(++i);
                    String name = lineList.get(++i);

                    StringBuilder mailing = new StringBuilder();
                    numLines = 0;

                    i++;

                    while (!lineList.get(i).contains("ACRES") && !lineList.get(i).contains("DEED BOOK") && numLines <= 10 && !lineList.get(i).contains("******************************************************************************************************* ")) {
                        mailing.append(lineList.get(i));
                        numLines++;
                        i++;
                    }

                    String acres = null;

                    if (lineList.get(i).contains("ACRES")) {
                        acres = lineList.get(i).replace("ACRES", "").trim();
                        containedAcres = true;
                        i++;
                    }

                    String instrType = null;
                    String instrNum  = null;
                    String page = null;

                    if (lineList.get(i).contains("DEED BOOK")) {
                        String[] deedComps = lineList.get(i).split(" ");
                        page = !deedComps[2].equals("00000") && deedComps.length > 3 ? deedComps[3].replace("PG-", "") : "ChkRolls";
                        instrType = deedComps[0] + " " + deedComps[1];
                        instrNum = deedComps[2];
                        containedDeedbook = true;
                    }

                    bw.write(String.format("%s|%s|%s|%s|%s|%s|%s|%s\n", secBlkPcl, name, address, mailing.toString(), acres != null ? acres : "NOT IN ROLLS", instrType != null ? instrType : "NOT IN ROLLS", instrNum != null ? instrNum : "NOT IN ROLLS", page != null ? page : "NOT IN ROLLS"));

                    if(containedAcres && !containedDeedbook)
                        i--;
                    else if ((!containedAcres && !containedDeedbook))
                        i-=2;
                }
            }

            bw.flush();
            bw.close();

            System.out.println("Done");

            checkNumOfParcelsConverted(formattedParcelFile, copy);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void checkNumOfParcelsConverted(File fmtdParcels, ArrayList<String> pdflist) {
        pdflist.removeIf(line -> line.equals("************************************************************************************************************************************ "));
        pdflist.removeIf(line -> !line.contains("******************************************************************************************************* "));

        ArrayList<String> conSBLS = getConvertedSBLS(fmtdParcels);
        ArrayList<String> notMatched = new ArrayList<>();

        boolean didMatch = false;
        int matched = 0;

        System.out.print("Checking Number of Parcels Converted Successfully... ");

        for(String pdfline : pdflist) {
            pdfline = pdfline.replaceAll("\\*", "");
            pdfline = pdfline.strip();
            for(String conline : conSBLS) {
                if(pdfline.equals(conline)){
                    didMatch = true;
                    matched++;
                }
            }
            if(!didMatch)
                notMatched.add(pdfline);

            didMatch = false;
        }

        System.out.println("Done");
        System.out.printf("\nTotal PDF SBLs: %d, Total CON SBLs: %d, Matched: %d/%d\n", pdflist.size(), conSBLS.size(), matched, pdflist.size());

        if(notMatched.size() > 0) {
            System.out.println("\nDidnt Find Match For - ");
            for(String line : notMatched)
                System.out.println(line);
        }
    }

    private static ArrayList<String> getConvertedSBLS(File fmtdParcels) {

        ArrayList<String> sbls = new ArrayList<>();

        try {
            sbls = (ArrayList<String>) Files.readAllLines(fmtdParcels.toPath(), Charset.defaultCharset());

            for(int i = 0; i < sbls.size(); i++) {
                sbls.set(i, sbls.get(i).split("\\|")[0].strip());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sbls;
    }
}