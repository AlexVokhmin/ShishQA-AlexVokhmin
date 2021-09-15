import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class WebDriverSettings {

    public static ChromeDriver driver;


    WebElement date_lebel; //Надпись ошибки у даты

    public static String [][] cardInfo;
    public static String orderNumber, totalAmount, currency;
    public static String cardInfoPage = "https://integration.unlimint.com/v1/api/#environments";
    public static String paymentPage = "https://sandbox.cardpay.com/MI/cardpayment2.html?orderXml=PE9SREVSIFdBTExFVF9JRD0nODI5OScgT1JERVJfTlVNQkVSPSc0NTgyMTEnIEFNT1VOVD0nMjkxLjg2JyBDVVJSRU5DWT0nRVVSJyAgRU1BSUw9J2N1c3RvbWVyQGV4YW1wbGUuY29tJz4KPEFERFJFU1MgQ09VTlRSWT0nVVNBJyBTVEFURT0nTlknIFpJUD0nMTAwMDEnIENJVFk9J05ZJyBTVFJFRVQ9JzY3NyBTVFJFRVQnIFBIT05FPSc4NzY5OTA5MCcgVFlQRT0nQklMTElORycvPgo8L09SREVSPg==&sha512=998150a2b27484b776a1628bfe7505a9cb430f276dfa35b14315c1c8f03381a90490f6608f0dcff789273e05926cd782e1bb941418a9673f43c47595aa7b8b0d";
    public static String[][] cards = {
            {"22", "mir"},
            {"4", "visa"},
            {"51", "mastercard"}, {"52", "mastercard"}, {"53", "mastercard"}, {"54", "mastercard"}, {"55", "mastercard"},
            {"50", "maestro"}, {"56", "maestro"}, {"57", "maestro"}, {"58", "maestro"}, {"63", "maestro"}, {"67", "maestro"},
            {"30", "diners_club"}, {"36", "diners_club"}, {"38", "diners_club"},
            {"31", "jcb"}, {"3528", "jcb"},
            {"34", "american_express"}, {"37", "american_express"},
            {"60", "discover"},
            {"62", "china_union_pay"},
    };
    String emptyCard = "0000 0000 0000 0000";

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "src\\test\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10000,
                TimeUnit.MILLISECONDS);
        driver.get(paymentPage);
        orderNumber = driver.findElement(By.id("order-number")).getText();
        totalAmount = driver.findElement(By.id("total-amount")).getText();
        currency = driver.findElement(By.id("currency")).getText();
    }

    @BeforeClass
    public static void getCardsInfo() {
        System.setProperty("webdriver.chrome.driver", "src\\test\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(cardInfoPage);

        WebElement baseTableTitle = driver.findElement(By.xpath("/html/body/div[2]/div[2]/table[1]"));
        WebElement baseTableBody = driver.findElement(By.xpath("/html/body/div[2]/div[2]/table[1]/tbody"));
        List<WebElement> tableRows = baseTableBody.findElements(By.tagName("tr"));
        List<WebElement> tableColumns = baseTableTitle.findElements(By.tagName("th"));

        cardInfo = new String[tableRows.size()][tableColumns.size()];
        for (int i = 0; i < cardInfo.length; i++)
            for (int j = 0; j < cardInfo[0].length; j++) {
                cardInfo[i][j] = driver.findElement(By.xpath("/html/body/div[2]/div[2]/table[1]/tbody/tr[" + (i + 1) + "]/td[" + (j + 1) + "]")).getText();
            }
        driver.close();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }
}
