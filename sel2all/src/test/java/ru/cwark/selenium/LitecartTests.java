package ru.cwark.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class LitecartTests extends TestBase {

    @Test
    public void openAdminPageTest() {
        get(adminPage);
        login();
    }

    @Test
    public void testLeftMenuItems() {
        get(adminPage);
        login();

        List<WebElement> lis = driver.findElements(By.xpath("//li[@id='app-']/a/span[@class='name']"));
        List<String> liNames = new ArrayList<String>();
        for (WebElement li : lis) {
            liNames.add(li.getText().trim());
        }

        for (String liName : liNames) {
            WebElement li = driver.findElement(By.xpath("//span[text()='" + liName + "']/.."));
            li.click();
            wait.until(visibilityOfElementLocated(By.cssSelector("h1")));
            List<WebElement> uls = driver.findElements(By.xpath("//ul[@class='docs']//span[@class='name']"));
            if (uls.size() > 0) {
                List<String> ulNames = new ArrayList<String>();
                for (WebElement ul : uls) {
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
