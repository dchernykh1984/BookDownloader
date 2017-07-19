import com.codeborne.selenide.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

/**
 * Created by Denis on 7/19/2017.
 */
public class Downloader {
    static String PATH_COUNTRY_NAME = "//div[@id='navigation']//li[contains(@class,'branch sub-collections')]";
    static String TEMPLATE_COUNTRY_LINK = "//li[@id='%s']/a";
    static String TEMPLATE_PATH_CITY_LINK = "//li[@id='%s']/ul/li[@class='branch ']/a";

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

        open("https://digitalcollections.nypl.org/collections/the-vinkhuijzen-collection-of-military-uniforms#/?tab=navigation&roots=3:708df000-c532-012f-0fc1-58d385a7bc34");
        for(WebElement country: $$(By.xpath(PATH_COUNTRY_NAME))) {
            WebElement countryLink = $(By.xpath(String.format(TEMPLATE_COUNTRY_LINK, country.getAttribute("id"))));
            countryLink.click();
            File countryDir = new File(currentDir.getAbsolutePath() + "\\" + countryLink.getText());
            createNewDir(countryDir);
            for(WebElement city:$$(By.xpath(String.format(TEMPLATE_PATH_CITY_LINK, country.getAttribute("id"))))) {
                city.click();
                File cityDir = new File(countryDir.getAbsolutePath() + "\\" + city.getText());
                createNewDir(cityDir);
            }


        }
    }
}










