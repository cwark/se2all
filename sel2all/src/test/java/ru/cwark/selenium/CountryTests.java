package ru.cwark.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class CountryTests extends TestBase {
    private final String baseUrl = "http://localhost/litecart/";
    private final String adminPage = baseUrl + "admin/";

    @Test
    public void testCountryAndZones() {
        driver.get(adminPage);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        wait.until(titleIs("Cwark Store"));
        //wait.until(elementSelectionStateToBe(By.xpath("//span[text()='Countries']/.."), true));
        wait.until(elementToBeClickable(By.xpath("//span[text()='Countries']/..")));

        driver.findElement(By.xpath("//span[text()='Countries']/..")).click();
        wait.until(visibilityOfElementLocated(By.xpath("//td[@id='content']/h1")));
        wait.until(textToBePresentInElementLocated(By.xpath("//td[@id='content']/h1"),"Countries"));
        wait.until(visibilityOfElementLocated(By.cssSelector("li#app-.selected")));

        List<WebElement> lis = driver.findElements(By.xpath("//table[@class='dataTable']//tr/td[5]"));

        for (int idx = 0; idx < lis.size() - 1; idx++) {
            String t1 = lis.get(idx).getText().trim().toLowerCase();
            String t2 = lis.get(idx + 1).getText().trim().toLowerCase();

            Assert.assertTrue(t1.compareToIgnoreCase(t2) <= 0);
        }

        lis = driver.findElements(By.xpath("//table[@class='dataTable']//tr/td[6][.!='0']/../td[5]"));
        System.out.println(lis.size());
        if (lis.size() > 0) {
            List<String> cNames = new ArrayList<>();
            for (WebElement element : lis) {
                cNames.add(element.getText().trim());
            }

            for (String name : cNames) {
                driver.findElement(By.xpath("//td[.='" + name + "']/a")).click();
                lis = driver.findElements(By.xpath("//table[@id='table-zones']//td/input[contains(@name, '[name]')]/.."));
                for (int idx = 0; idx < lis.size() - 1; idx++) {
                    String t1 = lis.get(idx).getText().trim();
                    String t2 = lis.get(idx + 1).getText().trim();

                    if (!t1.isEmpty() && !t2.isEmpty()) {
                        System.out.println("t1: >" + t1 + "< t2: >" + t2 + "<");
                        Assert.assertTrue(t1.compareToIgnoreCase(t2) <= 0);
                    }
                }
                
                driver.findElement(By.xpath("//button[@name='cancel']")).click();
                wait.until(visibilityOfElementLocated(By.xpath("//td[@id='content']/h1")));
                wait.until(textToBePresentInElementLocated(By.xpath("//td[@id='content']/h1"),"Countries"));
            }
        }


    }
}
