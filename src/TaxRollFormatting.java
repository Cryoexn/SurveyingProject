import CustomExceptions.TaxRollFormattingException;

import java.util.ArrayList;

public class TaxRollFormatting {

    private String townCityVillage;

    public TaxRollFormatting() { this.townCityVillage = null; }
    public TaxRollFormatting(String townCity) {
        this.townCityVillage = townCity;
    }

    public ArrayList<TaxRollParcel> getFormattedUserInput(String [] userInput) throws TaxRollFormattingException {
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
                        formattedValues.add(this.getFormattedTaxNumbers(
                                Integer.parseInt(sec[0]),
                                Integer.parseInt(sec[1]),
                                Integer.parseInt(userInput[1]),
                                Integer.parseInt(tempPcls[0]),
                                Integer.parseInt(tempPcls[1])));
                    } else {
                        formattedValues.add(this.getFormattedTaxNumbers(
                                Integer.parseInt(sec[0]),
                                Integer.parseInt(sec[1]),
                                Integer.parseInt(userInput[1]),
                                Integer.parseInt(pcl)));
                    }
                }
            } else {
                formattedValues.add(this.getFormattedTaxNumbers(
                        Integer.parseInt(sec[0]),
                        Integer.parseInt(sec[1]),
                        Integer.parseInt(userInput[1])));
            }

            return formattedValues;
        }
        return null;
    }

    private TaxRollParcel getFormattedTaxNumbers(int section1, int section2, int block, int parcel1, int parcel2) throws TaxRollFormattingException {
        String [] fmt = getDecimalParcelFormat();
        return new TaxRollParcel(String.format(fmt[0], section1, section2), String.format(fmt[1], block), String.format(fmt[2], parcel1, parcel2), "Not Found! Double Check Rolls", "X", "X", "X", "X", "X");
    }

    private TaxRollParcel getFormattedTaxNumbers(int section1, int section2, int block, int parcel) throws TaxRollFormattingException {
        String [] fmt = getWholeParcelFormat();
        return new TaxRollParcel(String.format(fmt[0], section1, section2), String.format(fmt[1], block), String.format(fmt[2], parcel), "Not Found! Double Check Rolls", "X", "X", "X", "X", "X");
    }

    private TaxRollParcel getFormattedTaxNumbers(int section1, int section2, int block) throws TaxRollFormattingException {
        String [] fmt = getWholeParcelFormat();
        return new TaxRollParcel(String.format(fmt[0], section1, section2), String.format(fmt[1], block), null, "Not Found! Double Check Rolls", "X", "X", "X", "X", "X");
    }

    private String[] getWholeParcelFormat() throws TaxRollFormattingException {

        return switch (this.townCityVillage) {
            case CityTownVillageVals.ANNSVILLE -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.AUGUSTA -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.AVA -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.BOONVILLE -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.BRIDGEWATER -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.CAMDEN -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.DEERFIELD -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.FLORENCE -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.FLOYD -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.FORESTPORT -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.KIRKLAND -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.LEE -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.MARCY -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.MARSHALL -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.NEW_HARTFORD -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.PARIS -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.REMSEN -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.SANGERFIELD -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.STEUBEN -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.TRENTON -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.VERNON -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.VERONA -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.VIENNA -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.WESTERN -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.WESTMORELAND -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.WHITESTOWN -> CityTownVillageVals.DEFAULT_FMT;
            case CityTownVillageVals.ROME -> CityTownVillageVals.ROME_FMT;
            default -> throw new TaxRollFormattingException("Error: Town/City Not Found! Double Check Your Program Arguments!\n");
        };
    }
    private String[] getDecimalParcelFormat() throws TaxRollFormattingException {

        return switch (this.townCityVillage) {
            case CityTownVillageVals.ANNSVILLE, CityTownVillageVals.AUGUSTA, CityTownVillageVals.AVA, CityTownVillageVals.BOONVILLE,
                 CityTownVillageVals.BRIDGEWATER, CityTownVillageVals.CAMDEN, CityTownVillageVals.DEERFIELD, CityTownVillageVals.FLOYD,
                 CityTownVillageVals.FLORENCE, CityTownVillageVals.FORESTPORT, CityTownVillageVals.KIRKLAND, CityTownVillageVals.LEE,
                 CityTownVillageVals.MARCY, CityTownVillageVals.MARSHALL, CityTownVillageVals.NEW_HARTFORD, CityTownVillageVals.PARIS,
                 CityTownVillageVals.REMSEN, CityTownVillageVals.SANGERFIELD, CityTownVillageVals.STEUBEN, CityTownVillageVals.TRENTON,
                 CityTownVillageVals.VERNON, CityTownVillageVals.VERONA, CityTownVillageVals.VIENNA, CityTownVillageVals.WESTERN,
                 CityTownVillageVals.WESTMORELAND, CityTownVillageVals.WHITESTOWN -> CityTownVillageVals.DEFAULT_DML_FMT;
            case CityTownVillageVals.ROME -> CityTownVillageVals.ROME_FMT;
            default -> throw new TaxRollFormattingException("Error: Town/City Not Found! Double Check Your Program Arguments!\n");
        };
    }

    public void setTownCity(String townCity) {
        this.townCityVillage = townCity;
    }
}
