package ru.cwark.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class TovarsTests extends TestBase {
    private final String baseUrl = "http://localhost/litecart/";
    private final String adminPage = baseUrl + "admin/";

    @Test
    public void testTovarItems() {
        driver.get(baseUrl);
        wait.until(titleContains("Cwark Store"));
        wait.until(visibilityOfElementLocated(By.id("box-campaigns")));

        WebElement regularPrice = driver.findElement(By.xpath("//div[@id='box-campaigns']//li[1]//s[@class='regular-price']"));
        WebElement compPrice = driver.findElement(By.xpath("//div[@id='box-campaigns']//li[1]//strong[@class='campaign-price']"));

        String tovarNameOnMainPage = driver.findElement(By.xpath("//div[@id='box-campaigns']//li[1]//div[@class='name']")).getText();
        String regularPriceOnMainPage = regularPrice.getText();
        String compPriceOnMainPage = compPrice.getText();

        //проверяем цвет на главной странице
        checkPrice(regularPrice, compPrice);

        //переходим на страницу товара
        driver.findElement(By.xpath("//div[@id='box-campaigns']//li[1]//a[@class='link']")).click();
        wait.until(visibilityOfElementLocated(By.xpath("//h1[@class='title']")));

        //а) на главной странице и на странице товара совпадает текст названия товара
        String tovarNameOnPage = driver.findElement(By.xpath("//h1[@class='title']")).getText();
        Assert.assertEquals(tovarNameOnPage, tovarNameOnMainPage);

        //б) на главной странице и на странице товара совпадают цены (обычная и акционная)
        regularPrice = driver.findElement(By.xpath("//div[@class='information']//s[@class='regular-price']"));
        compPrice = driver.findElement(By.xpath("//div[@class='information']//strong[@class='campaign-price']"));

        Assert.assertEquals(regularPriceOnMainPage, regularPrice.getText());
        Assert.assertEquals(compPriceOnMainPage, compPrice.getText());

        //проверяем цвет на странице товара
        checkPrice(regularPrice, compPrice);

    }

    private void checkPrice(WebElement regularPrice, WebElement compPrice) {
        //акционная цена крупнее, чем обычная
        int regularFontSize = Integer.parseInt(regularPrice.getCssValue("font-size").substring(0, 2));
        int compFontSize = Integer.parseInt(compPrice.getCssValue("font-size").substring(0, 2));

        Assert.assertTrue(regularFontSize < compFontSize);

        //акционная жирная
        int fontWeight = Integer.parseInt(compPrice.getCssValue("font-weight"));
        Assert.assertTrue(fontWeight >= 700);

        //акционная красная
        String[] arrayColor = getArrayColor(compPrice);

        Assert.assertTrue(Integer.parseInt(arrayColor[1]) == 0);
        Assert.assertTrue(Integer.parseInt(arrayColor[2]) == 0);

        //обычная цена зачёркнутая
        String textDecor = regularPrice.getCssValue("text-decoration-line");
        Assert.assertTrue("line-through".equals(textDecor));

        //обычная цена серая
        arrayColor = getArrayColor(regularPrice);
        Assert.assertTrue(Integer.parseInt(arrayColor[0]) == Integer.parseInt(arrayColor[1]));
        Assert.assertTrue(Integer.parseInt(arrayColor[0]) == Integer.parseInt(arrayColor[2]));
    }

    private String[] getArrayColor(WebElement element){
        String rgba = element.getCssValue("color");
        return rgba.substring(rgba.indexOf("(") + 1, rgba.indexOf(")")).split(", ");
    }
}
