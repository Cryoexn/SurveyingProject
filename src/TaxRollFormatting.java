import CustomExceptions.TaxRollFormattingException;

import java.util.ArrayList;

public class TaxRollFormatting {

    public static final String[] DEFAULT_FMT   = new String[] { "%d.%03d", "%04d", "%03d"};
    public static final String[] DCML_FMT = new String[] { "%d.%03d", "%d", "%d.%d"};
    public static final String[] DCML_SLASH_FMT = new String[] { "%d.%03d", "%d", "%d.%d/%d"};

    public static ArrayList<TaxRollParcel> getFormattedUserInput(String [] userInput) {
        if(userInput != null) {

            if(!userInput[0].contains(".")) {
                userInput[0] = userInput[0] + ".0";
            }

            ArrayList<TaxRollParcel> formattedValues = new ArrayList<>();

            String[] sec = userInput[0].split("\\.");

            if(userInput.length > 2) {
                String[] parcels = userInput[2].split(",");

                // Split parcels into multiples.

                for (String pcl : parcels) {
                    if (pcl.contains(".")) {
                        String[] tempPcls = pcl.split("\\.");
                        String[] tempSlash;
                        if(tempPcls.length > 1) {
                            if (tempPcls[1].contains("/")) {
                                tempSlash = tempPcls[1].split("/");

                                formattedValues.add(getFormattedTaxNumbers(
                                        Integer.parseInt(sec[0]),
                                        Integer.parseInt(sec[1]),
                                        Integer.parseInt(userInput[1]),
                                        Integer.parseInt(tempPcls[0]),
                                        Integer.parseInt(tempSlash[0]), Integer.parseInt(tempSlash[1])));
                            } else {
                                formattedValues.add(getFormattedTaxNumbers(
                                        Integer.parseInt(sec[0]),
                                        Integer.parseInt(sec[1]),
                                        Integer.parseInt(userInput[1]),
                                        Integer.parseInt(tempPcls[0]),
                                        Integer.parseInt(tempPcls[1])));
                            }
                        } else {
                            System.out.println(tempPcls.toString());
                        }
                    } else {
                        formattedValues.add(getFormattedTaxNumbers(
                                Integer.parseInt(sec[0]),
                                Integer.parseInt(sec[1]),
                                Integer.parseInt(userInput[1]),
                                Integer.parseInt(pcl)));
                    }
                }
            } else {
                formattedValues.add(getFormattedTaxNumbers(
                        Integer.parseInt(sec[0]),
                        Integer.parseInt(sec[1]),
                        Integer.parseInt(userInput[1])));
            }

            return formattedValues;
        }
        return null;
    }

    private static TaxRollParcel getFormattedTaxNumbers(int section1, int section2, int block, int parcel1, int parcel2, int parcel3) {
        String [] fmt = DCML_SLASH_FMT;
        return new TaxRollParcel(String.format(fmt[0], section1, section2), String.format(fmt[1], block), String.format(fmt[2], parcel1, parcel2, parcel3), "Not Found! Double Check Rolls", "X", "X", "X", "X", "X");
    }
    private static TaxRollParcel getFormattedTaxNumbers(int section1, int section2, int block, int parcel1, int parcel2) {
        String [] fmt = DCML_FMT;
        return new TaxRollParcel(String.format(fmt[0], section1, section2), String.format(fmt[1], block), String.format(fmt[2], parcel1, parcel2), "Not Found! Double Check Rolls", "X", "X", "X", "X", "X");
    }

    private static TaxRollParcel getFormattedTaxNumbers(int section1, int section2, int block, int parcel) {
        String [] fmt = DEFAULT_FMT;
        return new TaxRollParcel(String.format(fmt[0], section1, section2), String.format(fmt[1], block), String.format(fmt[2], parcel), "Not Found! Double Check Rolls", "X", "X", "X", "X", "X");
    }

    private static TaxRollParcel getFormattedTaxNumbers(int section1, int section2, int block) {
        String [] fmt = DEFAULT_FMT;
        return new TaxRollParcel(String.format(fmt[0], section1, section2), String.format(fmt[1], block), null, "Not Found! Double Check Rolls", "X", "X", "X", "X", "X");
    }
}
