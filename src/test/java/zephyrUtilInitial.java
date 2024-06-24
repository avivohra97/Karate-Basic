import net.lingala.zip4j.ZipFile;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;



public class zephyrUtilInitial {
    public static String reportPath;
    public static void main(String[] args) throws IOException {
        System.out.println("dir ::"+System.getProperty("user.dir"));
        reportPath = "./target/karate-reports";
        uploadResult();
    }
    public static void uploadResult() throws IOException{
        System.out.println("zephyr Validation starts");
        System.out.println("report path :: "+reportPath);

//        createZip();
        zipDirectory("./target");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String duration = sdf.format((new Date()));

        Properties props = new Properties();
        props.load(new FileInputStream("config.properties"));
        String accessToken = props.getProperty("zephyr_access_token");
        String projectKey = props.getProperty("zephyr_project_key");
        String testCycleId = props.getProperty("zephyr_testcycle_id");
        String baseUrl = props.getProperty("zephyr_base_url");
        String testCycleName = props.getProperty("zephyr_testcycle_name");
        String testCycleDesc = props.getProperty("zephyr_testcycle_desc");

        String apiEndpoint = "automation/execution/cucumber/" + projectKey + "?autoCreateTestCases=true";
        String testCycleDetails = "{\"folderId\": \""+testCycleId + "\", \"name\":\""+testCycleName + "-"+duration+ "\",\"description\":" +
                "\""+testCycleDesc+"\",\"status\":\"Approved\"}";

        System.out.println("duration ::::: "+ duration);
        System.out.println(testCycleDetails);
        System.out.println(apiEndpoint);

//        OkHttpClient client = new OkHttpClient();


    }

    public static void createZip() throws IOException {
        File reportFile = new File(reportPath);

        // Check if the report file exists
        if (!reportFile.exists()) {
            throw new FileNotFoundException("Report file not found: " + reportPath);
        }

        // Check if the report file is not empty
        if (reportFile.length() == 0) {
            System.out.println("Report file is empty: " + reportPath);
            // You can decide to handle this case (e.g., not create an empty ZIP)
        }

//        ZipFile zip = new ZipFile(zipPath);  // Create a new ZIP file
//        zip.addEntry(new ZipEntry(reportFile.getName()));  // Add entry with report file name
//        FileInputStream inputStream = new FileInputStream(reportFile);
//        IOUtils.copy(inputStream, zip.getOutputStream(zip.getEntry(reportFile.getName())));
//        inputStream.close();
        ZipFile zip = new ZipFile(reportPath+".zip");
        zip.addFile(new File(reportPath));
        zip.close();
    }
    public static void zipDirectory( String zipPath) throws IOException {
        File sourceDirFile = new File(reportPath);
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream("./target"));
        addDirectory(sourceDirFile, zipOut);
        zipOut.close();
    }

    private static void addDirectory(File directory, ZipOutputStream zipOut) throws IOException {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                addDirectory(file, zipOut);
            } else {
                FileInputStream in = new FileInputStream(file);
                ZipEntry entry = new ZipEntry(file.getPath().substring(directory.getPath().length() + 1));
                zipOut.putNextEntry(entry);
                IOUtils.copy(in, zipOut);
                in.close();
            }
        }
    }

}
