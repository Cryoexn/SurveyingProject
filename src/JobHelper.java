
public class JobHelper {

    public static void main(String [] args) {

        // Work Computer path to jobs
        // String jobsPath = "Z:\"
        // String taxRollsPath = "";
        // String templatePath = "";

        // Desktop Computer path to data.
        String jobsPath = "C:/Users/David/Documents/Java-Projects/SurveyingProject/data/";
        String taxRollsPath = "C:/Users/David/Documents/Java-Projects/SurveyingProject/data/taxrolls-txt/";
        String templatePath = "C:/Users/David/Documents/Java-Projects/SurveyingProject/data/";

        // Laptop path to data.
//        String jobsPath = "/home/cryoexn/IdeaProjects/DeedResearch/data/";
//        String taxRollsPath = "/home/cryoexn/IdeaProjects/DeedResearch/data/taxrolls-txt/";
//        String templatePath = "/home/cryoexn/IdeaProjects/DeedResearch/data/";

        DeedResearchGUI researcher = new DeedResearchGUI(new TaxRollParser(taxRollsPath), new TaxRollFormatting(), jobsPath, templatePath);
    }
}