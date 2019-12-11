package ru.cwark.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.UUID;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class TestBase {
    public WebDriver driver;
    public WebDriverWait wait;

    public boolean areElementsPresent(By locator) {
        return driver.findElements(locator).size() > 0;
    }

    @Before
    public void start() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);

        //driver = new FirefoxDriver();
        //driver = new InternetExplorerDriver();


        wait = new WebDriverWait(driver, 30);
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }

    protected void get(String url){
        driver.get(url);
    }

    protected void click(By locator) {
        driver.findElement(locator).click();
    }

    protected String getRandomShortString(){
        String s = UUID.randomUUID().toString();
        return s.substring(0, 8);
    }

    protected String getRandomEMail(){
        return getRandomShortString() + "@" + getRandomShortString() + "." + getRandomShortString().substring(0, 2);
    }

    protected String getRandomPostcode(){
        return getRandomNumberString(5) + "-" + getRandomNumberString(4);
    }

    protected String getRandomNumberString(int length){
        String s1 = Long.toString(Math.round(Math.random() * Math.pow(10, length)));
        String s2 = Long.toString(Math.round(Math.random() * Math.pow(10, length)));
        return (s1+s2).substring(0, length);
    }

    protected String getRandomNumberString(){
        return getRandomNumberString(6);
    }

    protected void type(By locator) {
        type(locator, getRandomShortString());
    }

    protected void type(By locator, String text) {
        wait.until(visibilityOfElementLocated(locator));

        click(locator);

        if (text != null) {
            String exValue = driver.findElement(locator).getAttribute("value");
            if (!exValue.equals(text)) {
                driver.findElement(locator).clear();
                driver.findElement(locator).sendKeys(text);
            }
        }
    }
}
