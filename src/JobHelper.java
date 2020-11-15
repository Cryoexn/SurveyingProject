
public class JobHelper {

    public static void main(String [] args) {

        // work computer path to jobs. - "Z:\";
        // String jobsPath = "Z:\"
        // String taxRollsPath = "";
        // String templatePath = "";

        String jobsPath = "/home/cryoexn/IdeaProjects/DeedResearch/data/";
        String taxRollsPath = "/home/cryoexn/IdeaProjects/DeedResearch/data/taxrolls-txt/";
        String templatePath = "/home/cryoexn/IdeaProjects/DeedResearch/data/";

        DeedResearchGUI researcher = new DeedResearchGUI(new TaxRollParser(taxRollsPath), new TaxRollFormatting(), jobsPath, templatePath);
    }
}