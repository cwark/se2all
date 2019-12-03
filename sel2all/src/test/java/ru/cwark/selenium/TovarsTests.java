package ru.cwark.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class TovarsTests extends TestBase {
    private final String baseUrl = "http://localhost/litecart/";
    private final String adminPage = baseUrl + "admin/";

    @Test
    public void testStickerItems() {
        driver.get(baseUrl);
        wait.until(titleContains("Cwark Store"));
        wait.until(visibilityOfElementLocated(By.id("box-campaigns")));

        String tName = driver.findElement(By.xpath("//div[@id='box-campaigns']//div[@class='name']")).getText();
        String tMan = driver.findElement(By.xpath("//div[@id='box-campaigns']//div[@class='manufacturer']")).getText();
        WebElement rPrice = driver.findElement(By.xpath("//div[@id='box-campaigns']//s[@class='regular-price']"));
        WebElement cPrice = driver.findElement(By.xpath("//div[@id='box-campaigns']//strong[@class='campaign-price']"));
        String srPrice = rPrice.getText();
        String scPrice = cPrice.getText();

        String rPriceColor = rPrice.getCssValue("color");
        String cPriceColor = cPrice.getCssValue("color");

        System.out.println("r: " + rPriceColor + " c: " + cPriceColor);

        System.out.println(rPrice.getCssValue("font-weight"));
        System.out.println(rPrice.getCssValue("font-size"));
        System.out.println(rPrice.getCssValue("color"));

        System.out.println(cPrice.getCssValue("font-weight"));
        System.out.println(cPrice.getCssValue("font-size"));
        System.out.println(cPrice.getCssValue("color"));
    }
}
