import com.codeborne.selenide.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

/**
 * Created by Denis on 7/19/2017.
 */
public class Downloader {
    static String PATH_COUNTRY_NAME = "//div[@id='navigation']//li[contains(@class,'branch sub-collections')]";
    static String PATH_COUNTRY_LINK = PATH_COUNTRY_NAME + "/a";
    static String PATH_CITY_LINK = PATH_COUNTRY_NAME + "/ul/li[contains(@class,'branch')]/a";

    static void createNewDir(File currentDir) {
        if(!currentDir.exists()) {
            currentDir.mkdir();
        } else {
            if(!currentDir.isDirectory()) {
                throw new RuntimeException("File with name exists");
            }
        }
    }

    public static void main(String [] args) {
        File currentDir = new File("SavedFiles");
        Configuration.browser = "chrome";
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        open("https://digitalcollections.nypl.org/collections/the-vinkhuijzen-collection-of-military-uniforms#/?tab=navigation&roots=5246dff0-c52f-012f-a661-58d385a7bc34");
        for(WebElement country: $$(By.xpath(PATH_COUNTRY_LINK))) {
            country.click();
            File countryDir = new File(currentDir.getAbsolutePath() + "//" + country.getText());
            createNewDir(countryDir);
            for(WebElement city:$$(By.xpath(PATH_CITY_LINK))) {
                city.click();
                File cityDir = new File(countryDir.getAbsolutePath() + "//" + city.getText());
                createNewDir(cityDir);
            }


        }
    }
}










