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

            String[] sec = userInput[0].split("\\.");
            String[] parcels = userInput[2].split(",");

            ArrayList<TaxRollParcel> formattedValues = new ArrayList<>();

            // Split parcels into multiples.

            for(String pcl : parcels) {
                if (pcl.contains(".")) {
                    String [] tempPcls = pcl.split("\\.");
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

            return formattedValues;
        }
        return null;
    }

    private TaxRollParcel getFormattedTaxNumbers(int section1, int section2, int block, int parcel1, int parcel2) throws TaxRollFormattingException {
        String [] fmt = getDecimalParcelFormat();
        return new TaxRollParcel(String.format(fmt[0], section1, section2), String.format(fmt[1], block), String.format(fmt[2], parcel1, parcel2), "Not Found! Double Check Rolls", "X", "X", "X", "X", "X", "X");
    }

    private TaxRollParcel getFormattedTaxNumbers(int section1, int section2, int block, int parcel) throws TaxRollFormattingException {
        String [] fmt = getWholeParcelFormat();
        return new TaxRollParcel(String.format(fmt[0], section1, section2), String.format(fmt[1], block), String.format(fmt[2], parcel), "Not Found! Double Check Rolls", "X", "X", "X", "X", "X", "X");
    }

    private String[] getWholeParcelFormat() throws TaxRollFormattingException {
        String [] fmt;

        switch (this.townCityVillage) {
            case CityTownVillageVals.ANNSVILLE:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.AUGUSTA:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.AVA:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.BOONVILLE:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.BRIDGEWATER:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.CAMDEN:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.DEERFIELD:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.FLORENCE:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.FLOYD:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.FORESTPORT:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.KIRKLAND:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.LEE:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.MARCY:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.MARSHALL:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.NEW_HARTFORD:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.PARIS:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.REMSEN:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.SANGERFIELD:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.STEUBEN:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.TRENTON:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.VERNON:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.VERONA:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.VIENNA:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.WESTERN:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.WESTMORELAND:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.WHITESTOWN:
                fmt = CityTownVillageVals.DEFAULT_FMT;
                break;
            case CityTownVillageVals.ROME:
                fmt = CityTownVillageVals.ROME_FMT;
                break;
            default:
                throw new TaxRollFormattingException("Error: Town/City Not Found! Double Check Your Program Arguments!\n");
        }
        return fmt;
    }
    private String[] getDecimalParcelFormat() throws TaxRollFormattingException {
        String [] fmt;

        switch (this.townCityVillage) {
            case CityTownVillageVals.ANNSVILLE:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.AUGUSTA:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.AVA:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.BOONVILLE:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.BRIDGEWATER:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.CAMDEN:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.DEERFIELD:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.FLORENCE:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.FLOYD:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.FORESTPORT:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.KIRKLAND:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.LEE:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.MARCY:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.MARSHALL:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.NEW_HARTFORD:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.PARIS:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.REMSEN:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.SANGERFIELD:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.STEUBEN:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.TRENTON:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.VERNON:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.VERONA:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.VIENNA:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.WESTERN:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.WESTMORELAND:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.WHITESTOWN:
                fmt = CityTownVillageVals.DEFAULT_DML_FMT;
                break;
            case CityTownVillageVals.ROME:
                fmt = CityTownVillageVals.ROME_FMT;
                break;
            default:
                throw new TaxRollFormattingException("Error: Town/City Not Found! Double Check Your Program Arguments!\n");
        }
        return fmt;
    }

    public void setTownCity(String townCity) {
        this.townCityVillage = townCity;
    }
}
