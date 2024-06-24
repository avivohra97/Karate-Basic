import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZephyrUtils {

    public static void uploadResult() throws IOException {
        String karateReportsPath = "./target/cucumber"; // Replace with your actual path
        String zipFilePath = karateReportsPath + ".zip";
        Properties props = new Properties();
        props.load(new FileInputStream("config.properties"));
        String token = props.getProperty("zephyr_access_token");
        String projectKey = props.getProperty("zephyr_project_key");

        zipDirectory(karateReportsPath, zipFilePath);
        String curlCommand = String.format("curl -H \"Authorization: Bearer %s\" -F \"file=@%s;type=application/x-zip-compressed\" https://api.zephyrscale.smartbear.com/v2/automations/executions/cucumber?projectKey=%s&autoCreateTestCases=false", token, zipFilePath, projectKey);
        System.out.println("Curl command to upload reports:");
        System.out.println(curlCommand);

        // Uncomment the following line to execute the curl command (replace Runtime.getRuntime() with your process execution method if needed)
         Runtime.getRuntime().exec(curlCommand);
        System.out.println("Upload complete");
    }

    private static void zipDirectory(String sourceDir, String zipPath) throws IOException {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipPath))) {
            File sourceDirFile = new File(sourceDir);
            addDirectory(sourceDirFile, zipOut);
        }
    }

    private static void addDirectory(File directory, ZipOutputStream zipOut) throws IOException {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                addDirectory(file, zipOut);
            } else {
                String relativePath = file.getPath().substring(directory.getPath().length() + 1);
                zipOut.putNextEntry(new ZipEntry(relativePath));
                try (FileInputStream in = new FileInputStream(file)) {
                    IOUtils.copy(in, zipOut);
                }
            }
        }
    }
}
