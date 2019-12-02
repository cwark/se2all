package ru.cwark.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;

public class StikersTests extends TestBase {
    private final String baseUrl = "http://localhost/litecart/";
    private final String adminPage = baseUrl + "admin/";

    @Test
    public void testStickerItems() {
        driver.get(baseUrl);
        wait.until(titleContains("Cwark Store"));

        List<WebElement> lis = driver.findElements(By.xpath("//div[@class='content']//li[contains(@class,'product')]"));

        Assert.assertTrue(lis.size() > 0);
        for(WebElement li:lis){
            List<WebElement> webElementList = li.findElements(By.xpath(".//div[contains(@class,'sticker')]"));
            Assert.assertTrue(webElementList.size() == 1);
        }

    }
}
