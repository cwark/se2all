package ru.cwark.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class LitecartTests extends TestBase {

    private final String baseUrl = "http://localhost/litecart/";
    private final String adminPage = baseUrl + "admin/";


    @Test
    public void openAdminPageTest() {
        driver.get(adminPage);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        wait.until(titleIs("Cwark Store"));

    }

    @Test
    public void testLeftMenuItems() {
        driver.get(adminPage);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        wait.until(titleIs("Cwark Store"));

        List<WebElement> lis = driver.findElements(By.xpath("//li[@id='app-']/a/span[@class='name']"));
        out.println(lis.size());
        List<String> liNames = new ArrayList<String>();
        for (WebElement li : lis) {
            out.println(li.getText().trim());
            liNames.add(li.getText().trim());
        }

        for (String liName : liNames) {
            WebElement li = driver.findElement(By.xpath("//span[text()='" + liName + "']/.."));
            li.click();
            wait.until(visibilityOfElementLocated(By.cssSelector("h1")));
            List<WebElement> uls = driver.findElements(By.xpath("//ul[@class='docs']//span[@class='name']"));
            out.println(liName + ":" + uls.size());
            if (uls.size() > 0) {
                List<String> ulNames = new ArrayList<String>();
                for (WebElement ul : uls) {
                    out.println(ul.getText().trim());
                    ulNames.add(ul.getText().trim());
                }
                for (String ulName : ulNames) {
                    WebElement ul = driver.findElement(By.xpath("//ul[@class='docs']//span[text()='" + ulName + "']/.."));
                    ul.click();
                    wait.until(visibilityOfElementLocated(By.cssSelector("h1")));
                }
            }
        }

    }
}
