import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import javax.swing.*;

public class PaymentCheck extends WebDriverSettings {

    @Test
    public void testCardPayment() throws Exception {

        for (String[] data : cardInfo) {

            String card_number = data[0];
            String secure = data[1];
            String status = data[2];

            String cardHolder = "Test Test";

            driver.get(paymentPage);
            WebElement inputCardNumber = driver.findElement(By.id("input-card-number"));
            WebElement inputCardHolder = driver.findElement(By.id("input-card-holder"));
            Select cardExpiresMonth = new Select(driver.findElement(By.id("card-expires-month")));
            Select cardExpiresYear = new Select(driver.findElement(By.id("card-expires-year")));
            WebElement inputCardCvc = driver.findElement(By.id("input-card-cvc"));

            WebElement actionSubmit = driver.findElement(By.id("action-submit"));
            WebElement actionCancel = driver.findElement(By.id("action-cancel"));

            inputCardNumber.sendKeys(card_number);
            //inputCardNumber.sendKeys("4000000000000085");
            inputCardHolder.sendKeys(cardHolder);
            cardExpiresMonth.selectByIndex(7);
            cardExpiresYear.selectByValue("2037");
            inputCardCvc.sendKeys("777");

            actionSubmit.click();

            if (secure.contains("full authentication")) {
                if (secure.contains("version 1"))
                    driver.findElement(By.id("success")).click();
                else if (secure.contains("version 2")) {
                    TimeUnit.SECONDS.sleep(5);
                    driver.switchTo().frame(0);
                    driver.findElement(By.id("successButton")).click();
                    driver.switchTo().parentFrame();
                }
            }

            if (status.contains("CONFIRMED")) {
                assertEquals("Success", driver.findElement(By.xpath("//*[@id=\"payment-status-title\"]/span")).getText());
                assertEquals("Confirmed", driver.findElement(By.xpath("//*[@id=\"payment-item-status\"]/div[2]")).getText());

            } else if (status.contains("DECLINED")) {
                assertEquals("Decline", driver.findElement(By.xpath("//*[@id=\"payment-status-title\"]/span")).getText());
                assertEquals("Declined by issuing bank", driver.findElement(By.xpath("//*[@id=\"payment-item-status\"]/div[2]")).getText());
            } else if (status.contains("AUTHORIZED")) {
                assertEquals("Info", driver.findElement(By.xpath("//*[@id=\"payment-status-title\"]/span")).getText());
                assertEquals("CONFIRMED", driver.findElement(By.xpath("//*[@id=\"payment-item-status\"]/div[2]")).getText());
            }

            assertEquals(orderNumber, driver.findElement(By.xpath("//*[@id=\"payment-item-ordernumber\"]/div[2]")).getText());
            assertEquals(totalAmount, driver.findElement(By.xpath("//*[@id=\"payment-item-total-amount\"]")).getText());
            assertEquals("..." + card_number.substring(card_number.length()-4,card_number.length()), driver.findElement(By.xpath("//*[@id=\"payment-item-cardnumber\"]/div[2]")).getText());
            assertEquals(cardHolder.toUpperCase(), driver.findElement(By.xpath(" //*[@id=\"payment-item-cardholder\"]/div[2]")).getText());

            //assertEquals(currency, driver.findElement(By.xpath("//*[@id=\"payment-item-total\"]/div[2]")).getText());
            // driver.close();


        }
    }

    @Test
    public void testScreenShot() throws IOException {
        driver.get(paymentPage);

        WebElement Cvc = driver.findElement(By.id("cvc-hint-toggle"));

        Actions action = new Actions(driver);
        action.moveToElement(Cvc).build().perform();

        Screenshot screenshot = new AShot().takeScreenshot(driver);
        ImageIO.write(screenshot.getImage(), "png", new File("src\\test\\sc.png"));
    }
}
