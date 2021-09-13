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
    public String orderNumber, totalAmount, currency;

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "src\\test\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
о        orderNumber = driver.findElement(By.id("order-number")).getText();
        totalAmount = driver.findElement(By.id("total-amount")).getText();
        currency = driver.findElement(By.id("currency")).getText();
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
