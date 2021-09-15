import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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

public class Payment extends WebDriverSettings {

    //ТЕСТ НА СТАТУСЫ ОПЛАТЫ ПО ИСХОДНЫМ КАРТАМ
    @Test
    public void paymentCheck() throws Exception {


        for (String[] data : cardInfo) {
            //ДАННЫЕ ПРОВЕРЯЕМОЙ КАРТЫ
            String card_number = data[0];
            String secure = data[1];
            String status = data[2];

        /*String card_number = "4000000000000085";
        String secure="3-D secure version 2, сhallenge flow, full authentication\t";
        String status="Payment has CONFIRMED status";
*/
            String cardHolder = "Test Test"; //ИМЯ ДЕРЖАТЕЛЯ КАРТЫ

            //ПОЛУЧЕНИЕ ЭЛЕМЕНТОВ СТРАНИЦЫ ВВОДА ДАННЫХ
            driver.get(paymentPage);
            WebElement inputCardNumber = driver.findElement(By.id("input-card-number"));
            WebElement inputCardHolder = driver.findElement(By.id("input-card-holder"));
            Select cardExpiresMonth = new Select(driver.findElement(By.id("card-expires-month")));
            Select cardExpiresYear = new Select(driver.findElement(By.id("card-expires-year")));
            WebElement inputCardCvc = driver.findElement(By.id("input-card-cvc"));
            WebElement actionSubmit = driver.findElement(By.id("action-submit"));
            WebElement actionCancel = driver.findElement(By.id("action-cancel"));

            //ПОДСТАНОВКА ЗНАЧЕНИЙ В ПОЛЯ СТРАНИЦЫ
            inputCardNumber.sendKeys(card_number);
            inputCardHolder.sendKeys(cardHolder);
            cardExpiresMonth.selectByIndex(7);
            cardExpiresYear.selectByValue("2037");
            inputCardCvc.sendKeys("777");

            //ОТПРАВКА ФОРМЫ С ДАННЫМИ
            actionSubmit.click();

            //ПРОВЕРКА НА 3D-SECURE
            if (secure.contains("full authentication")) {
                if (secure.contains("version 1")) //ЕСЛИ ИСПОЛЬЗУЕТСЯ 1 ВЕРСИЯ
                    driver.findElement(By.id("success")).click();
                else if (secure.contains("version 2")) { //ЕСЛИ ИСПОЛЬЗУЕТСЯ 2 ВЕРСИЯ
                    TimeUnit.SECONDS.sleep(10); //ЗАГЛУШКА, ЧТОБЫ ДОБРАТЬСЯ ДО ФРЕЙМА С КНОПКАМИ
                    driver.switchTo().frame(0);
                    driver.findElement(By.id("successButton")).click();
                    driver.switchTo().parentFrame();
                }
            }

            //ПРОВЕРКА НА ОЖИДАЕМЫЙ РЕЗУЛЬТАТ ТРАНЗАКЦИИ
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

            //ПРОВЕРКА ДАННЫХ ЗАКАЗА С ИСХОДНЫМИ ДАННЫМИ
            assertEquals(orderNumber, driver.findElement(By.xpath("//*[@id=\"payment-item-ordernumber\"]/div[2]")).getText());
            assertEquals(totalAmount, driver.findElement(By.xpath("//*[@id=\"payment-item-total-amount\"]")).getText());
            assertEquals("..." + card_number.substring(card_number.length() - 4, card_number.length()), driver.findElement(By.xpath("//*[@id=\"payment-item-cardnumber\"]/div[2]")).getText());
            assertEquals(cardHolder.toUpperCase(), driver.findElement(By.xpath(" //*[@id=\"payment-item-cardholder\"]/div[2]")).getText());
            //    assertEquals(currency, driver.findElement(By.xpath("//*[@id=\"payment-item-total\"]/div[2]")).getText().replace("&nbsp;"," "));
        }

    }
    //ТЕСТ НА ПОЯВЛЕНИЕ ПОДСКАЗКИ ПРИ НАВЕДЕНИИ КУРСОРА НА ЗНАК ?
    @Test
    public void testScreenShot() throws IOException {
        driver.get(paymentPage);

        WebElement Cvc = driver.findElement(By.id("cvc-hint-toggle"));

        Actions action = new Actions(driver);
        action.moveToElement(Cvc).build().perform();

        Screenshot screenshot = new AShot().takeScreenshot(driver);
        ImageIO.write(screenshot.getImage(), "png", new File("src/test/screenshots/cvv_marksc.png"));
    }

    @Test
    public void cardNumberCheck() {

        WebElement inputCardNumber = driver.findElement(By.id("input-card-number"));
        WebElement actionSubmit = driver.findElement(By.id("action-submit"));
        WebElement labelCN;
        String labelCN_xpath = "//*[@id=\"card-number-field\"]/div/label";

        String validData = cardInfo[0][0];

        String[] non_validData = {
                "test!@##$%^&*()_",
                "testtesttesttest",
                "400000000000000_2",
        };

        String[] non_validData2 = {
                cardInfo[0][0].substring(0, cardInfo[0][0].length()-1)+1,
                cardInfo[0][0].substring(0, cardInfo[0][0].length()-1),
                cardInfo[0][0]+1
        };


        for (String s: non_validData) {
            inputCardNumber.clear();
            inputCardNumber.sendKeys(s);
            assertNotEquals(s, inputCardNumber.getText());
        }

        for (String s: non_validData2) {
            inputCardNumber.clear();
            inputCardNumber.sendKeys(s);
            actionSubmit.click();
            labelCN = driver.findElement(By.xpath(labelCN_xpath));
            assertEquals("Card number is not valid", labelCN.getText());
        }

        inputCardNumber.clear();
        inputCardNumber.sendKeys(validData);
        labelCN = driver.findElement(By.xpath(labelCN_xpath));
        assertEquals("", labelCN.getText());

    }

    //СКРИНШОТ ПОЛЯ ВВОДА НОМЕРА КАРТЫ ДЛЯ ПРОВЕРКИ ПОЯВЛЕНИЯ ЛОГОТИПА ПЛАТЕЖНОЙ СИСТЕМЫ
    @Test
    public void cardTypeCheck() throws Exception {

        driver.get(paymentPage);

        WebElement card_number = driver.findElement(By.id("input-card-number"));
        Screenshot screenshot;

        for (String[] card : cards) {
            card_number.sendKeys(card[0] + emptyCard.substring(card[0].length()));
            screenshot = new AShot().takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File(
                    "src\\test\\screenshots\\cardtypes\\" + card[1] + "_" + card[0] + ".png"));
            card_number.clear();
        }
    }

    //ПРОВЕРКА ПОЛЯ ВЛАДЕЛЬЦА КАРТЫ
    @Test
    public void cardHolderCheck() {

        driver.get(paymentPage);

        WebElement inputCardHolder = driver.findElement(By.id("input-card-holder"));
        WebElement actionSubmit = driver.findElement(By.id("action-submit"));
        String labelCH_xpath = "//*[@id=\"card-holder-field\"]/div/label";
        WebElement labelCH;
        //VALID DATA
        String[] validData = {
                "TEST TEST",
                "test",
                "TEST test",
                "test-test",
                "test'test TEST-test",
        };
        String[] non_validData = {
                "123456",
                "test!@##$%^&*()_",
                "<test>",
                ".test",
                "'test",
                "-test"
        };
        String[] dataInBounds = {
                "TEST", //МИН 4 СИМВ
                "TESTTESTTESTTESTTESTTESTTEST",
                "TESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTE" // МАКС 50 СИМВ
        };
        String[] dataOutOfBounds = {
                "TES",
                "TESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTESTTES" //51 СИМВ
        };

        for (String s : non_validData) {
            inputCardHolder.clear();
            inputCardHolder.sendKeys(s);
            actionSubmit.click();
            labelCH = driver.findElement(By.xpath(labelCH_xpath));
            assertEquals("Cardholder name is not valid", labelCH.getText());
        }

        for (String s : validData) {
            inputCardHolder.clear();
            inputCardHolder.sendKeys(s);
            labelCH = driver.findElement(By.xpath(labelCH_xpath));
            assertEquals("", labelCH.getText());
        }
        for (String s : dataInBounds) {
            inputCardHolder.clear();
            inputCardHolder.sendKeys(s);
            labelCH = driver.findElement(By.xpath(labelCH_xpath));
            assertEquals("", labelCH.getText());
        }
        for (String s : dataOutOfBounds) {
            inputCardHolder.clear();
            inputCardHolder.sendKeys(s);
            labelCH = driver.findElement(By.xpath(labelCH_xpath));
            assertEquals("Allowed from 4 to 50 characters", labelCH.getText());
        }

    }

    //ПРОВЕРКА СЕЛЕКТОРОВ ДАТЫ
    @Test
    public void expDate() {

        //ПОЛУЧЕНИЕ СЕГОДНЯШНЕЙ ДАТЫ
        java.util.Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();

        driver.get(paymentPage);

        Select cardExpiresMonth = new Select(driver.findElement(By.id("card-expires-month")));
        Select cardExpiresYear = new Select(driver.findElement(By.id("card-expires-year")));
        WebElement actionSubmit = driver.findElement(By.id("action-submit"));

        int m_selector_size = cardExpiresMonth.getOptions().size();
        int y_selector_size = cardExpiresYear.getOptions().size();

        String date_label_xpath = "//*[@id=\"card-expires-field\"]/div/label";

        //ЕСЛИ МЕСЯЦ ИЛИ ГОД НЕ ВЫБРАНЫ
        actionSubmit.click();
        date_lebel = driver.findElement(By.xpath(date_label_xpath));
        assertEquals("Expiration Date is required", date_lebel.getText());

        //ЕСЛИ ДАТА КАРТЫ ПРОШЛА
        if (month != 1) {
            cardExpiresYear.selectByValue(Integer.toString(year));
            cardExpiresMonth.selectByValue(Integer.toString(month - 1));
            actionSubmit.click();
            assertEquals("Invalid date", date_lebel.getText());
        }

        //УСТАНОВЛЕНА ТЕКУЩАЯ ДАТА
        cardExpiresYear.selectByValue(Integer.toString(year));
        cardExpiresMonth.selectByValue(Integer.toString(month));
        assertEquals("", date_lebel.getText());

        //УСТАНОВЛЕНА БУДУЩАЯ ДАТА
        cardExpiresYear.selectByValue(Integer.toString(year + 1));
        cardExpiresMonth.selectByValue(Integer.toString(month));
        assertEquals("", date_lebel.getText());

        //ИЗБЫТОЧНОСТЬ БЫТИЯ
        /*
        for (int m = 1; m < m_selector_size; m++) {
            cardExpiresMonth.selectByIndex(m);
            for (int y = 1; y < y_selector_size; y++) {
                cardExpiresYear.selectByIndex(y);
                //ЕСЛИ ДАТА КАРТЫ ПРОШЛА
                if ((Integer.parseInt(cardExpiresYear.getFirstSelectedOption().getText()) <= year) &&
                        (Integer.parseInt(cardExpiresMonth.getFirstSelectedOption().getText()) < month)) {
                    actionSubmit.click();
                    date_lebel = driver.findElement(By.xpath(date_label_xpath));
                    assertEquals("Invalid date", date_lebel.getText());
                } else {
                    //ПРОВЕРКА НА ОТСУТСТВИЕ ЛЕЙБЛА С ОШИБКОЙ ДАТЫ
                    assertEquals("", date_lebel.getText());
                }
            }
        }

         */
    }

    //ПРОВЕРКА ПОЛЯ CVV
    @Test
    public void cvvCheck() {
        WebElement inputCardCvc = driver.findElement(By.id("input-card-cvc"));
        WebElement actionSubmit = driver.findElement(By.id("action-submit"));
        String label_cvv_xpath = "//*[@id=\"card-cvc-field\"]/div/label";
        WebElement label_cvv;

        String validData = "061";

        String[] non_validData = {
                "asd",
                "ASD",
                "!#2",
                "1_2",
                "<2>"
        };
        String dataInBounds = "123";

        String dataOutOfBounds1 =  "12", dataOutOfBounds2 = "1234";

        for (String s : non_validData) {
            inputCardCvc.clear();
            inputCardCvc.sendKeys(s);
            assertNotEquals(s, inputCardCvc.getAttribute("value"));
        }
        //Ввод валидных данных
        inputCardCvc.clear();
        inputCardCvc.sendKeys(validData);
        String sdqqds = inputCardCvc.getAttribute("value");
        int dwqdqw = inputCardCvc.getAttribute("value").length();
        assertEquals(validData, inputCardCvc.getAttribute("value"));

        // Ввод данных на границе длины
        inputCardCvc.clear();
        inputCardCvc.sendKeys(dataInBounds);
        assertEquals(dataInBounds, inputCardCvc.getAttribute("value"));

        // Ввод данных до границы
        inputCardCvc.clear();
        inputCardCvc.sendKeys(dataOutOfBounds1);
        actionSubmit.click();
        label_cvv = driver.findElement(By.xpath(label_cvv_xpath));
        assertEquals("CVV2/CVC2/CAV2 is not valid", label_cvv.getText());

        //Ввод данных за границами
        inputCardCvc.clear();
        inputCardCvc.sendKeys(dataOutOfBounds2);
        label_cvv = driver.findElement(By.xpath(label_cvv_xpath));
        actionSubmit.click();
        assertNotEquals(dataOutOfBounds2.length(), inputCardCvc.getText().length());
    }
}