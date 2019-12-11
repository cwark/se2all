package ru.cwark.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class MenuTests extends TestBase {
    private final String baseUrl = "http://localhost/litecart/";
    private final String adminPage = baseUrl + "admin/";

    @Test
    public void testLeftMenuItems() {
        get(adminPage);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        click(By.name("login"));
        wait.until(titleIs("Cwark Store"));

        List<WebElement> lis = driver.findElements(By.xpath("//li[@id='app-']/a/span[@class='name']"));
        List<String> liNames = new ArrayList<String>();
        for (WebElement li : lis) {
            liNames.add(li.getText().trim());
        }

        for (String liName : liNames) {
            WebElement li = driver.findElement(By.xpath("//span[text()='" + liName + "']/.."));
            li.click();
            wait.until(visibilityOfElementLocated(By.cssSelector("h1")));
            List<WebElement> uls = driver.findElements(By.xpath("//ul[@class='docs']/li"));
            if (uls.size() > 0) {
                List<String> ulNames = new ArrayList<String>();
                for (WebElement ul : uls) {
                    ulNames.add(ul.getAttribute("id").trim());
                }
                for (String ulName : ulNames) {
                    WebElement ul = driver.findElement(By.xpath("//li[@id='" + ulName + "']/a"));
                    ul.click();
                    wait.until(visibilityOfElementLocated(By.cssSelector("h1")));
                }
            }
        }

    }
}
