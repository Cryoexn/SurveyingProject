import CustomExceptions.TaxRollFileException;

import java.io.*;
import java.util.ArrayList;

public class TaxRollParser {
    
    private String cityTownVillageFile;
    private String rollsTxtPath;

    public TaxRollParser(String rollsTxtPath) {
        this.rollsTxtPath = rollsTxtPath;
        this.cityTownVillageFile = null;
    }

    public TaxRollParser(String rollsTxtPath, String cityTown) {
        this.rollsTxtPath = rollsTxtPath;
        switch (cityTown) {
            case CityTownVillageVals.ANNSVILLE:
                this.cityTownVillageFile = CityTownVillageVals.ANNSVILLE_ROLL;
                break;
            case CityTownVillageVals.AUGUSTA:
                this.cityTownVillageFile = CityTownVillageVals.AUGUSTA_ROLL;
                break;
            case CityTownVillageVals.AVA:
                this.cityTownVillageFile = CityTownVillageVals.AVA_ROLL;
                break;
            case CityTownVillageVals.BOONVILLE:
                this.cityTownVillageFile = CityTownVillageVals.BOONVILLE_ROLL;
                break;
            case CityTownVillageVals.BRIDGEWATER:
                this.cityTownVillageFile = CityTownVillageVals.BRIDGEWATER_ROLL;
                break;
            case CityTownVillageVals.CAMDEN:
                this.cityTownVillageFile = CityTownVillageVals.CAMDEN_ROLL;
                break;
            case CityTownVillageVals.DEERFIELD:
                this.cityTownVillageFile = CityTownVillageVals.DEERFIELD_ROLL;
                break;
            case CityTownVillageVals.FLORENCE:
                this.cityTownVillageFile = CityTownVillageVals.FLORENCE_ROLL;
                break;
            case CityTownVillageVals.FLOYD:
                this.cityTownVillageFile = CityTownVillageVals.FLOYD_ROLL;
                break;
            case CityTownVillageVals.FORESTPORT:
                this.cityTownVillageFile = CityTownVillageVals.FORESTPORT_ROLL;
                break;
            case CityTownVillageVals.KIRKLAND:
                this.cityTownVillageFile = CityTownVillageVals.KIRKLAND_ROLL;
                break;
            case CityTownVillageVals.LEE:
                this.cityTownVillageFile = CityTownVillageVals.LEE_ROLL;
                break;
            case CityTownVillageVals.MARCY:
                this.cityTownVillageFile = CityTownVillageVals.MARCY_ROLL;
                break;
            case CityTownVillageVals.MARSHALL:
                this.cityTownVillageFile = CityTownVillageVals.MARSHALL_ROLL;
                break;
            case CityTownVillageVals.NEW_HARTFORD:
                this.cityTownVillageFile = CityTownVillageVals.NEW_HARTFORD_ROLL;
                break;
            case CityTownVillageVals.PARIS:
                this.cityTownVillageFile = CityTownVillageVals.PARIS_ROLL;
                break;
            case CityTownVillageVals.REMSEN:
                this.cityTownVillageFile = CityTownVillageVals.REMSEN_ROLL;
                break;
            case CityTownVillageVals.SANGERFIELD:
                this.cityTownVillageFile = CityTownVillageVals.SANGERFIELD_ROLL;
                break;
            case CityTownVillageVals.STEUBEN:
                this.cityTownVillageFile = CityTownVillageVals.STEUBEN_ROLL;
                break;
            case CityTownVillageVals.TRENTON:
                this.cityTownVillageFile = CityTownVillageVals.TRENTON_ROLL;
                break;
            case CityTownVillageVals.VERNON:
                this.cityTownVillageFile = CityTownVillageVals.VERNON_ROLL;
                break;
            case CityTownVillageVals.VERONA:
                this.cityTownVillageFile = CityTownVillageVals.VERONA_ROLL;
                break;
            case CityTownVillageVals.VIENNA:
                this.cityTownVillageFile = CityTownVillageVals.VIENNA_ROLL;
                break;
            case CityTownVillageVals.WESTERN:
                this.cityTownVillageFile = CityTownVillageVals.WESTERN_ROLL;
                break;
            case CityTownVillageVals.WESTMORELAND:
                this.cityTownVillageFile = CityTownVillageVals.WESTMORELAND_ROLL;
                break;
            case CityTownVillageVals.WHITESTOWN:
                this.cityTownVillageFile = CityTownVillageVals.WHITESTOWN_ROLL;
                break;
            case CityTownVillageVals.ROME:
                this.cityTownVillageFile = CityTownVillageVals.ROME_ROLL;
                break;
            default:
                this.cityTownVillageFile = null;
                break;
        }
    }

    public ArrayList<TaxRollParcel> searchTaxRollsForValues(ArrayList<TaxRollParcel> parcelsToSearch) {

        // Change to this.file when all tax rolls are converted.
        ArrayList<TaxRollParcel> fileParcels = getTaxRollParcelsFromFile(new File(this.rollsTxtPath+this.cityTownVillageFile));

        if(fileParcels != null) {
            System.out.println("Searching " + this.cityTownVillageFile + "...");

            for (TaxRollParcel fileParcel : fileParcels) {
                for (int j = 0; j < parcelsToSearch.size(); j++) {
                    if (fileParcel.getSecBlkPcl().equals(parcelsToSearch.get(j).getSecBlkPcl())) {
                        parcelsToSearch.set(j, fileParcel);
                    }
                }
            }

            int found = 0;

            for(TaxRollParcel parcel : parcelsToSearch)
                if(!parcel.getName().equals("Not Found! Double Check Rolls")) { found++; }

            System.out.printf("Found - %d/%d Parcels.\n\n", found, parcelsToSearch.size());
        } else {
            return null;
        }

        return parcelsToSearch;
    }

    private ArrayList<TaxRollParcel> getTaxRollParcelsFromFile(File taxRoll) {

        ArrayList<TaxRollParcel> parcels = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(taxRoll));
            parcels = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String [] comps = line.split("\\|");
                String [] secBlkPcl = comps[0].split("-");
                if(secBlkPcl.length > 2) {
                    String[] sec = secBlkPcl[0].contains(".") ? secBlkPcl[0].split("\\.") : new String[]{secBlkPcl[0], ".0"};
                    String[] pcl = secBlkPcl[2].contains(".") ? secBlkPcl[2].split("\\.") : new String[]{secBlkPcl[0], ".0"};
                    System.out.println(line);
                    parcels.add(new TaxRollParcel(secBlkPcl[0], secBlkPcl[1], secBlkPcl[2], comps[1], comps[2], comps[3], comps[4], comps[5], comps[6], comps[7]));
                } else {
                    parcels.add(new TaxRollParcel(secBlkPcl[0], secBlkPcl[1], null, comps[1], comps[2], comps[3], comps[4], comps[5], comps[6], comps[7]));
                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return parcels;
    }

    public void setCityTownVillage(String cityTown) {
        if(cityTown != null) {
            switch (cityTown) {
                case CityTownVillageVals.ANNSVILLE:
                    this.cityTownVillageFile = CityTownVillageVals.ANNSVILLE_ROLL;
                    break;
                case CityTownVillageVals.AUGUSTA:
                    this.cityTownVillageFile = CityTownVillageVals.AUGUSTA_ROLL;
                    break;
                case CityTownVillageVals.AVA:
                    this.cityTownVillageFile = CityTownVillageVals.AVA_ROLL;
                    break;
                case CityTownVillageVals.BOONVILLE:
                    this.cityTownVillageFile = CityTownVillageVals.BOONVILLE_ROLL;
                    break;
                case CityTownVillageVals.BRIDGEWATER:
                    this.cityTownVillageFile = CityTownVillageVals.BRIDGEWATER_ROLL;
                    break;
                case CityTownVillageVals.CAMDEN:
                    this.cityTownVillageFile = CityTownVillageVals.CAMDEN_ROLL;
                    break;
                case CityTownVillageVals.DEERFIELD:
                    this.cityTownVillageFile = CityTownVillageVals.DEERFIELD_ROLL;
                    break;
                case CityTownVillageVals.FLORENCE:
                    this.cityTownVillageFile = CityTownVillageVals.FLORENCE_ROLL;
                    break;
                case CityTownVillageVals.FLOYD:
                    this.cityTownVillageFile = CityTownVillageVals.FLOYD_ROLL;
                    break;
                case CityTownVillageVals.FORESTPORT:
                    this.cityTownVillageFile = CityTownVillageVals.FORESTPORT_ROLL;
                    break;
                case CityTownVillageVals.KIRKLAND:
                    this.cityTownVillageFile = CityTownVillageVals.KIRKLAND_ROLL;
                    break;
                case CityTownVillageVals.LEE:
                    this.cityTownVillageFile = CityTownVillageVals.LEE_ROLL;
                    break;
                case CityTownVillageVals.MARCY:
                    this.cityTownVillageFile = CityTownVillageVals.MARCY_ROLL;
                    break;
                case CityTownVillageVals.MARSHALL:
                    this.cityTownVillageFile = CityTownVillageVals.MARSHALL_ROLL;
                    break;
                case CityTownVillageVals.NEW_HARTFORD:
                    this.cityTownVillageFile = CityTownVillageVals.NEW_HARTFORD_ROLL;
                    break;
                case CityTownVillageVals.PARIS:
                    this.cityTownVillageFile = CityTownVillageVals.PARIS_ROLL;
                    break;
                case CityTownVillageVals.REMSEN:
                    this.cityTownVillageFile = CityTownVillageVals.REMSEN_ROLL;
                    break;
                case CityTownVillageVals.SANGERFIELD:
                    this.cityTownVillageFile = CityTownVillageVals.SANGERFIELD_ROLL;
                    break;
                case CityTownVillageVals.STEUBEN:
                    this.cityTownVillageFile = CityTownVillageVals.STEUBEN_ROLL;
                    break;
                case CityTownVillageVals.TRENTON:
                    this.cityTownVillageFile = CityTownVillageVals.TRENTON_ROLL;
                    break;
                case CityTownVillageVals.VERNON:
                    this.cityTownVillageFile = CityTownVillageVals.VERNON_ROLL;
                    break;
                case CityTownVillageVals.VERONA:
                    this.cityTownVillageFile = CityTownVillageVals.VERONA_ROLL;
                    break;
                case CityTownVillageVals.VIENNA:
                    this.cityTownVillageFile = CityTownVillageVals.VIENNA_ROLL;
                    break;
                case CityTownVillageVals.WESTERN:
                    this.cityTownVillageFile = CityTownVillageVals.WESTERN_ROLL;
                    break;
                case CityTownVillageVals.WESTMORELAND:
                    this.cityTownVillageFile = CityTownVillageVals.WESTMORELAND_ROLL;
                    break;
                case CityTownVillageVals.WHITESTOWN:
                    this.cityTownVillageFile = CityTownVillageVals.WHITESTOWN_ROLL;
                    break;
                case CityTownVillageVals.ROME:
                    this.cityTownVillageFile = CityTownVillageVals.ROME_ROLL;
                    break;
                default:
                    this.cityTownVillageFile = null;
                    break;
            }
        }
    }

    public String getTaxRollFile() {
        return this.cityTownVillageFile;
    }
}
