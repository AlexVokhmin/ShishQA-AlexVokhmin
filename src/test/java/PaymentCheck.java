import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class PaymentCheck extends WebDriverSettings{

    @Test
    public void testUntitledTestCase() throws Exception {

        driver.findElement(By.id("input-card-number")).click();
        driver.findElement(By.id("input-card-number")).sendKeys(cardInfo[0][0]);
        driver.findElement(By.id("input-card-holder")).sendKeys("TEST TEST");
        driver.findElement(By.id("card-expires-month")).click();
        new Select(driver.findElement(By.id("card-expires-month"))).selectByVisibleText("12");
        driver.findElement(By.id("payment-data-card-fields")).click();
        driver.findElement(By.id("card-expires-year")).click();
        new Select(driver.findElement(By.id("card-expires-year"))).selectByVisibleText("2035");
        driver.findElement(By.id("input-card-cvc")).click();
        driver.findElement(By.id("input-card-cvc")).sendKeys("777");
        driver.findElement(By.id("action-submit")).click();
        driver.findElement(By.id("success")).click();
        driver.findElement(By.id("payment-status")).click();
        assertEquals("Success", driver.findElement(By.xpath("//div[@id='payment-status-title']/span")).getText());
    }
    @Test
    public void testUntitledTestCasTwo() throws Exception {

        driver.findElement(By.id("input-card-number")).click();
        driver.findElement(By.id("input-card-number")).sendKeys(cardInfo[0][0]);
        driver.findElement(By.id("input-card-holder")).sendKeys("TEST TEST");
        driver.findElement(By.id("card-expires-month")).click();
        new Select(driver.findElement(By.id("card-expires-month"))).selectByVisibleText("12");
        driver.findElement(By.id("payment-data-card-fields")).click();
        driver.findElement(By.id("card-expires-year")).click();
        new Select(driver.findElement(By.id("card-expires-year"))).selectByVisibleText("2035");
        driver.findElement(By.id("input-card-cvc")).click();
        driver.findElement(By.id("input-card-cvc")).sendKeys("777");
        driver.findElement(By.id("action-submit")).click();
        driver.findElement(By.id("success")).click();
        driver.findElement(By.id("payment-status")).click();
        assertEquals("Success", driver.findElement(By.xpath("//div[@id='payment-status-title']/span")).getText());
    }

}
