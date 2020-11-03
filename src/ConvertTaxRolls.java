import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

// When a line is a Deed Book and the next line is not a Star make the next line a Star after getting
// the PayRollParcels. The mailing address loop is not able to differ from end of file and last parcel.
// remove the Sub - Total replace line.

public class ConvertTaxRolls {
    public static void main(String []args) {

        if(args.length == 1) {
            File pdfFile            = new File(args[0]);
            File textFile           = new File(args[0].replace(".pdf", "") + ".txt");
            File textParcelFmtdFile = new File(args[0].replace(".pdf", "-") + "Parcels.txt");

            try {

                ArrayList<String> lineList = convertPDFToTxt(pdfFile);

                removeUnwantedText(lineList);

                lineList = getPayRollParcels(lineList);

                lineList.removeIf(line -> line.contains("UNDER AGDIST"));

                writeTxtToFile(lineList, textFile);

                createFormattedParcelFile(lineList, textParcelFmtdFile);

            } catch(IOException ex) {
                System.out.println(ex.getMessage());
            }

            System.out.println("Complete.");

        } else {
            System.out.println("Usage - java ConvertTaxRolls \"City/Town/Village\"-Tax-Rolls.pdf <- pdf file");
        }
    }

    private static void removeUnwantedText(ArrayList<String> list) {
        list.removeIf(line -> line.length() != 132 && line.contains("*"));
        list.removeIf(line -> line.contains("COUNTY -"));
        list.removeIf(line -> line.contains("TOWN -"));
        list.removeIf(line -> line.contains("VILLAGE -"));
        list.removeIf(line -> line.contains("SWIS -"));
        list.removeIf(line -> line.contains("CODE-"));
        list.removeIf(line -> line.contains("CURRENT OWNERS"));
        list.removeIf(line -> line.contains("STATE OF NEW YORK"));
        list.removeIf(line -> line.charAt(0) == ' ' && line.contains("EAST") || line.contains("NRTH") || line.contains("FULL MARKET VALUE") || line.contains("DPTH") || line.contains("FRNT") );
    }

    private static ArrayList<String> getPayRollParcels(ArrayList<String> list) {
        ArrayList<String> updatedText = new ArrayList<>();

        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).contains("DEED") || list.get(i).contains("BOOK") || list.get(i).contains("ACRES") || list.get(i).contains("LIBER")) {
                list.set(i, list.get(i).strip());
            } else if(list.get(i).contains("******************************************************************************************************* ")) {
                list.set(i+1, list.get(i+1).strip());
            } else if(list.get(i).contains("S U B - T O T A L")) {
                list.set(i, "******************************************************************************************************* ");
            }
        }

        for(String line : list) {
            if (!(line.contains("DEED") || line.contains("BOOK") || line.contains("ACRES") || line.contains("LIBER"))) {
                String firstComp = line.split(" {2}")[0];
                if (!firstComp.isBlank())
                    updatedText.add(line.split(" {2}")[0]);

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

                for(String comp : deedComps)
                    System.out.println(comp);

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

        return updatedText;
    }

    private static ArrayList<String> convertPDFToTxt(File pdf) throws IOException {
        PDDocument document = PDDocument.load(pdf);
        PDFTextStripper pdfStripper = new PDFTextStripper();

        System.out.println("Converting PDF to Txt...");

        ArrayList<String> lines = new ArrayList<String>(Arrays.asList(pdfStripper.getText(document).split("\n")));

        document.close();

        return lines;
    }

    private static void writeTxtToFile(ArrayList<String> text, File file) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        System.out.println("Writing Unformatted Parcels to File...");

        for (String line : text) {
            bw.write(line + "\n");
        }

        bw.flush();
        bw.close();
    }

    private static void createFormattedParcelFile(ArrayList<String> lineList, File formattedParcelFile) {
        System.out.println("Creating Formatted Parcels File...");

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(formattedParcelFile));

            boolean containedAcres;
            boolean containedDeedbook;

            for(int i = 0; i < lineList.size(); i++) {

                //Reset flags
                containedAcres = false;
                containedDeedbook = false;

                if (lineList.get(i).contains("******************************************************************************************************* ")) {
                    i++;
                    String address = lineList.get(i);
                    i++;
                    String secBlkPcl = lineList.get(i);
                    i++;
                    String name = lineList.get(i);
                    i++;

                    StringBuilder mailing = new StringBuilder();

                    while (!lineList.get(i).contains("ACRES") && !lineList.get(i).contains("DEED BOOK") && !lineList.get(i).contains("******************************************************************************************************* ")) {
                        mailing.append(lineList.get(i));
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
                }
            }

            bw.flush();
            bw.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static ArrayList<String> readTxtFileToArrayList(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        ArrayList<String> list = new ArrayList<>();

        String line;

        System.out.println("Reading In Unformatted Text From File ...");

        while((line = br.readLine()) != null) {
            list.add(line);
        }

        br.close();

        return list;
    }
}