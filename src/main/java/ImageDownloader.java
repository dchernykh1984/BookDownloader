import org.openqa.selenium.By;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Denis on 7/19/2017.
 */
public class ImageDownloader implements Runnable {
    static String PATH_MORE_DOWNLOAD_LINK = "//div[@class='more-downloads-link']/a";
    static String PATH_HIRES_LINK = "//a[@class='deriv-link highres']";
    String directory;

    public ImageDownloader(String dir) {
        directory = dir;
    }

    @Override
    public void run() {
        try {
            File savedPicture;
            $(By.xpath(PATH_MORE_DOWNLOAD_LINK)).click();
            savedPicture = $(By.xpath(PATH_HIRES_LINK)).download();
            File toPicture = new File(directory, savedPicture.getName() + ".tiff");
            while (toPicture.exists()) {
                int fileIndex = 1;
                if(toPicture.length() == savedPicture.length()) {
                    toPicture.delete();
                } else {
                    toPicture = new File(toPicture.getParent(), fileIndex + "_" + toPicture.getName());
                    System.out.println("File " + toPicture.getAbsolutePath() + " already exists.");
                }
            }
            Files.copy(savedPicture.toPath(), toPicture.toPath());
            savedPicture.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
