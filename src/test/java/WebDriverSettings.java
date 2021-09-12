import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class WebDriverSettings {

    public ChromeDriver driver, driver1;
    public String [][] cardInfo;
    public String OrderNumber, Value;
    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "src\\test\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
       driver.get("https://sandbox.cardpay.com/MI/cardpayment2.html?orderXml=PE9SREVSIFdBTExFVF9JRD0nODI5OScgT1JERVJfTlVNQkVSPSc0NTgyMTEnIEFNT1VOVD0nMjkxLjg2JyBDVVJSRU5DWT0nRVVSJyAgRU1BSUw9J2N1c3RvbWVyQGV4YW1wbGUuY29tJz4KPEFERFJFU1MgQ09VTlRSWT0nVVNBJyBTVEFURT0nTlknIFpJUD0nMTAwMDEnIENJVFk9J05ZJyBTVFJFRVQ9JzY3NyBTVFJFRVQnIFBIT05FPSc4NzY5OTA5MCcgVFlQRT0nQklMTElORycvPgo8L09SREVSPg==&sha512=998150a2b27484b776a1628bfe7505a9cb430f276dfa35b14315c1c8f03381a90490f6608f0dcff789273e05926cd782e1bb941418a9673f43c47595aa7b8b0d");
        OrderNumber = driver.findElement(By.id("order-number")).getText();
        System.out.println(OrderNumber);

    }
    @Before
    public void getCardsInfo() {
        driver1 = new ChromeDriver();
        driver1.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver1.get("https://integration.unlimint.com/v1/api/#environments");

        WebElement baseTableTitle = driver1.findElement(By.xpath("/html/body/div[2]/div[2]/table[1]"));
        WebElement baseTableBody = driver1.findElement(By.xpath("/html/body/div[2]/div[2]/table[1]/tbody"));
        List<WebElement> tableRows = baseTableBody.findElements(By.tagName("tr"));
        List<WebElement> tableColumns = baseTableTitle.findElements(By.tagName("th"));

        cardInfo = new String[tableRows.size()][tableColumns.size()];
        for (int i = 0; i < cardInfo.length; i++)
            for (int j = 0; j < cardInfo[0].length; j++)
                cardInfo[i][j] = driver1.findElement(By.xpath("/html/body/div[2]/div[2]/table[1]/tbody/tr[" + (i+1)+"]/td[" + (j+1) + "]")).getText();

        driver1.quit();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }
}
