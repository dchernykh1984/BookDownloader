import com.codeborne.selenide.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Denis on 7/19/2017.
 */
public class Downloader {
    static String PATH_COUNTRY_NAME = "//div[@id='navigation']//li[contains(@class,'branch sub-collections')]";
    static String TEMPLATE_COUNTRY_LINK = "//li[@id='%s']/a";
    static String TEMPLATE_PATH_CITY_LINK = "//li[@id='%s']/ul/li[contains(@class,'branch')]/a";
    static String PATH_IMAGE_LINK = "//ul[@id='results-list']/li/div[@class='item']/a";
    static String PATH_LINK_FORWARD = "//div[@class='pagination noscript']/div[@class='controls']/a[@class='next_page']";
    static String PATH_MORE_DOWNLOAD_LINK = "//div[@class='more-downloads-link']/a";
    static String PATH_HIRES_LINK = "//a[@class='deriv-link highres']";

    static void createNewDir(File currentDir) {
        if(!currentDir.exists()) {
            currentDir.mkdir();
        } else {
            if(!currentDir.isDirectory()) {
                throw new RuntimeException("File with name exists");
            }
        }
    }

    static void toTopOfMainPage() {
        System.out.println("To top of page");
        $(By.id("results-head")).scrollTo();
    }

    public static void setSaveDirectory(String directory) {
    }

    public static void downloadAllPhotos(String directory) throws IOException {
        for(WebElement element:$$(By.xpath(PATH_IMAGE_LINK))) {
            element.click();
            setSaveDirectory(directory);
            File savedPicture;
            $(By.xpath(PATH_MORE_DOWNLOAD_LINK)).click();
            savedPicture = $(By.xpath(PATH_HIRES_LINK)).download();
            File toPicture = new File(directory,savedPicture.getName() + ".tiff");
            if(toPicture.exists()) {
                toPicture.delete();
            }
            Files.copy(savedPicture.toPath(), toPicture.toPath());
            savedPicture.delete();
            back();
        }

    }

    public static void main(String [] args) throws IOException, InterruptedException {
        File currentDir = new File("SavedFiles");
        Configuration.browser = "chrome";
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        open("https://digitalcollections.nypl.org/collections/the-vinkhuijzen-collection-of-military-uniforms#/?tab=navigation&roots=3:708df000-c532-012f-0fc1-58d385a7bc34");
        for(WebElement country: $$(By.xpath(PATH_COUNTRY_NAME))) {
            WebElement countryLink = $(By.xpath(String.format(TEMPLATE_COUNTRY_LINK, country.getAttribute("id"))));
            toTopOfMainPage();
            countryLink.click();
            System.out.println("Click county:" + countryLink.getText());
            Thread.sleep(2000);
            File countryDir = new File(currentDir.getAbsolutePath(),countryLink.getText().replace(" ", ""));
            createNewDir(countryDir);
            for(WebElement city:$$(By.xpath(String.format(TEMPLATE_PATH_CITY_LINK, country.getAttribute("id"))))) {
                System.out.println("\tClick city:" + city.getText());
                toTopOfMainPage();
                city.click();
                Thread.sleep(2000);
                File cityDir = new File(countryDir.getAbsolutePath(),city.getText().replace(" ", ""));
                createNewDir(cityDir);
                downloadAllPhotos(cityDir.getAbsolutePath());
                while($(By.xpath(PATH_LINK_FORWARD)).isDisplayed() && $(By.xpath(PATH_LINK_FORWARD)).isEnabled()) {
                    $(By.xpath(PATH_LINK_FORWARD)).click();
                    downloadAllPhotos(cityDir.getAbsolutePath());
                }
            }


        }
    }
}










