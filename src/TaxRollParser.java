import java.io.*;
import java.util.ArrayList;

public class TaxRollParser {
    private final ArrayList<TaxRollParcel> fileParcels;

    public TaxRollParser(String rollsTxtPath) {
        this.fileParcels = getTaxRollParcelsFromFile(new File(rollsTxtPath));
    }

    public ArrayList<TaxRollParcel> searchTaxRollsForValues(ArrayList<TaxRollParcel> parcelsToSearch) {

        if(fileParcels != null) {
            System.out.println("Searching Parcels...");

            for(int j = 0; j < parcelsToSearch.size(); j++) {
                for (TaxRollParcel fileParcel : fileParcels) {
                    if (parcelsToSearch.get(j).getSecBlkPcl().equals(fileParcel.getSecBlkPcl())) {
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
                if(secBlkPcl.length == 3) {
                    String[] sec = secBlkPcl[0].contains(".") ? secBlkPcl[0].split("\\.") : new String[]{secBlkPcl[0], ".0"};
                    String[] pcl = secBlkPcl[2].contains(".") ? secBlkPcl[2].split("\\.") : new String[]{secBlkPcl[0], ".0"};

                    parcels.add(new TaxRollParcel(secBlkPcl[0], secBlkPcl[1], secBlkPcl[2], comps[1], comps[2], comps[3], comps[4], comps[5], comps[6]));
                } else if(secBlkPcl.length == 2){
                    parcels.add(new TaxRollParcel(secBlkPcl[0], secBlkPcl[1], null, comps[1], comps[2], comps[3], comps[4], comps[5], comps[6]));
                } else if(secBlkPcl.length == 4){
                    parcels.add(new TaxRollParcel(secBlkPcl[0], secBlkPcl[1], secBlkPcl[2]+"-"+secBlkPcl[3], comps[1], comps[2], comps[3], comps[4], comps[5], comps[6]));
                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return parcels;
    }
}
