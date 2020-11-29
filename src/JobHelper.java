import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class JobHelper {

    public static void main(String [] args) throws IOException {

        FileReader fr = new FileReader("jobhelper.properties");
        Properties props = new Properties();

        // Load directory properties
        props.load(fr);

        // Get the properties
        String jobsPath = props.getProperty("jobs");
        String taxRollsPath = props.getProperty("taxrolls");
        String templatePath = props.getProperty("templates");

        DeedResearchGUI researcher = new DeedResearchGUI(new TaxRollParser(taxRollsPath), jobsPath, templatePath);
    }
}