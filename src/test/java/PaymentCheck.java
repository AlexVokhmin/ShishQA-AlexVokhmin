import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import javax.swing.*;

public class PaymentCheck extends WebDriverSettings{

    @Test
    public void testUntitledTestCase() throws Exception {

        WebElement inputCardNumber = driver.findElement(By.id("input-card-number"));

        WebElement inputCardHolder = driver.findElement(By.id("input-card-holder"));
        Select cardExpiresMonth = new Select(driver.findElement(By.id("card-expires-month")));
        Select cardExpiresYear = new Select(driver.findElement(By.id("card-expires-year")));
        WebElement inputCardCvc = driver.findElement(By.id("input-card-cvc"));
        WebElement actionSubmit =  driver.findElement(By.id("action-submit"));
        WebElement actionCancel = driver.findElement(By.id("action-cancel"));


        inputCardNumber.sendKeys("4000000000000002");
        inputCardHolder.sendKeys("Jhon Down");
        cardExpiresMonth.selectByIndex(7);
        cardExpiresYear.selectByIndex(2037);
        inputCardCvc.sendKeys("777");
        driver.findElement(By.id("success")).click();

        assertEquals("Confirmed", driver.findElement(By.xpath("//*[@id=\"payment-item-status\"]/div[2]")).getText());
        assertEquals(orderNumber, driver.findElement(By.xpath("//*[@id=\"payment-item-ordernumber\"]/div[2]")).getText());
        assertEquals(totalAmount, driver.findElement(By.xpath("//*[@id=\"payment-item-total-amount\"]")).getText());
        assertEquals(totalAmount, driver.findElement(By.xpath("//*[@id=\"payment-item-total-amount\"]")).getText());
        assertEquals(currency, driver.findElement(By.xpath("//*[@id=\"payment-item-total\"]/div[2]/text()")).getText());

    }

    @Test
    public void testScreenShot() throws IOException {
        driver.get("https://sandbox.cardpay.com/MI/cardpayment2.html?orderXml=PE9SREVSIFdBTExFVF9JRD0nODI5OScgT1JERVJfTlVNQkVSPSc0NTgyMTEnIEFNT1VOVD0nMjkxLjg2JyBDVVJSRU5DWT0nRVVSJyAgRU1BSUw9J2N1c3RvbWVyQGV4YW1wbGUuY29tJz4KPEFERFJFU1MgQ09VTlRSWT0nVVNBJyBTVEFURT0nTlknIFpJUD0nMTAwMDEnIENJVFk9J05ZJyBTVFJFRVQ9JzY3NyBTVFJFRVQnIFBIT05FPSc4NzY5OTA5MCcgVFlQRT0nQklMTElORycvPgo8L09SREVSPg==&sha512=998150a2b27484b776a1628bfe7505a9cb430f276dfa35b14315c1c8f03381a90490f6608f0dcff789273e05926cd782e1bb941418a9673f43c47595aa7b8b0d");

        WebElement Cvc = driver.findElement(By.id("cvc-hint-toggle"));

        Actions action = new Actions(driver);
        action.moveToElement(Cvc).build().perform();

        Screenshot screenshot = new AShot().takeScreenshot(driver);
        ImageIO.write(screenshot.getImage(), "png", new File("src\\test\\sc.png"));
    }
}
