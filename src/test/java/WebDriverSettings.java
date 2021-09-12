import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class WebDriverSettings {

    public ChromeDriver driver, driver1;
    public String [][] cardInfo = new String[10][2];

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "src\\test\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    @Before
    public void getCardsInfo() {
        driver1 = new ChromeDriver();
        driver1.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver1.get("https://integration.unlimint.com/v1/api/#environments");
/*
        ArrayList<String> CardNumber = new ArrayList<String>();

        CardNumber.add(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Transaction result'])[1]/following::td[1]")).getText());
        CardNumber.add(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Payment has CONFIRMED status'])[1]/following::td[1]")).getText());
        CardNumber.add(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Payment has DECLINED status'])[1]/following::td[1]")).getText());
        CardNumber.add(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Payment has AUTHORIZED status'])[1]/following::td[1]")).getText());
        CardNumber.add(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Payment has CONFIRMED status'])[2]/following::td[1]")).getText());
        CardNumber.add(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Payment has CONFIRMED status'])[3]/following::td[1]")).getText());
        CardNumber.add(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Payment has CONFIRMED status'])[4]/following::td[1]")).getText());
        CardNumber.add(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Payment has CONFIRMED status'])[5]/following::td[1]")).getText());
        CardNumber.add(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Payment has CONFIRMED status'])[6]/following::td[1]")).getText());
        CardNumber.add(driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Payment has DECLINED status'])[2]/following::td[1]")).getText());
*/

        //НОМЕР КАРТЫ
        cardInfo[0][0] = driver1.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Transaction result'])[1]/following::td[1]")).getText();
        //СТАТУС ОПЕРАЦИИ
        cardInfo[0][1] = driver1.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Transaction result'])[1]/following::td[3]")).getText();
        driver1.quit();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }
}
