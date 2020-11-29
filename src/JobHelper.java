import java.io.IOException;

public class JobHelper {

    public static void main(String [] args) throws IOException {

        //FileReader fr = new FileReader("jobhelper.properties");
        //Properties props = new Properties();

        // Load directory properties
        //props.load(fr);

        // Get the properties
        //String jobsPath = props.getProperty("jobs");
        //String taxRollsPath = props.getProperty("taxrolls");
        //String templatePath = props.getProperty("templates");
        String jobsPath = "";
        String taxRollsPath = "";
        String templatePath = "";

        DeedResearchGUI researcher = new DeedResearchGUI(new TaxRollParser(taxRollsPath), new TaxRollFormatting(taxRollsPath), jobsPath, templatePath);
    }
}