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

        String tName = driver.findElement(By.xpath("//div[@id='box-campaigns']//li[1]//div[@class='name']")).getText();
        String tMan = driver.findElement(By.xpath("//div[@id='box-campaigns']//li[1]//div[@class='manufacturer']")).getText();
        WebElement rPrice = driver.findElement(By.xpath("//div[@id='box-campaigns']//li[1]//s[@class='regular-price']"));
        WebElement cPrice = driver.findElement(By.xpath("//div[@id='box-campaigns']//li[1]//strong[@class='campaign-price']"));
        String srPrice = rPrice.getText();
        String scPrice = cPrice.getText();

        System.out.println("text-decoration-line: " + rPrice.getCssValue("text-decoration-line"));
        System.out.println("font-weight: " + rPrice.getCssValue("font-weight"));
        System.out.println("font-size: " + rPrice.getCssValue("font-size"));
        System.out.println("color: " + rPrice.getCssValue("color"));
        System.out.println("========================");
        System.out.println("text-decoration-line: " + cPrice.getCssValue("text-decoration-line"));
        System.out.println("font-weight: " + cPrice.getCssValue("font-weight"));
        System.out.println("font-size: "+ cPrice.getCssValue("font-size"));
        System.out.println("color: " + cPrice.getCssValue("color"));

        //проверки на главной странице
        /*
        а) на главной странице и на странице товара совпадает текст названия товара
        б) на главной странице и на странице товара совпадают цены (обычная и акционная)
        в) обычная цена зачёркнутая и серая (можно считать, что "серый" цвет это такой, у которого в RGBa представлении одинаковые значения для каналов R, G и B)
        г) акционная жирная и красная (можно считать, что "красный" цвет это такой, у которого в RGBa представлении каналы G и B имеют нулевые значения)
        (цвета надо проверить на каждой странице независимо, при этом цвета на разных страницах могут не совпадать)
        д) акционная цена крупнее, чем обычная (это тоже надо проверить на каждой странице независимо)
        */
        //д) акционная цена крупнее, чем обычная (это тоже надо проверить на каждой странице независимо)
        int rFS = Integer.parseInt(rPrice.getCssValue("font-size").substring(0,2));
        int cFS = Integer.parseInt(cPrice.getCssValue("font-size").substring(0,2));
        Assert.assertTrue(rFS < cFS);

        // г) акционная жирная и красная (можно считать, что "красный" цвет это такой, у которого в RGBa представлении каналы G и B имеют нулевые значения)
        //        (цвета надо проверить на каждой странице независимо, при этом цвета на разных страницах могут не совпадать)
            //акционная жирная
        int cBold = Integer.parseInt(cPrice.getCssValue("font-weight"));
        Assert.assertEquals(cBold, 700);

        String cPriceColor = cPrice.getCssValue("color");
        String[] tC = cPriceColor.substring(5, cPriceColor.length()-1).split(", ");

        Assert.assertTrue(Integer.parseInt(tC[1]) == 0);
        Assert.assertTrue(Integer.parseInt(tC[2]) == 0);

        //в) обычная цена зачёркнутая и серая (можно считать, что "серый" цвет это такой, у которого в RGBa представлении одинаковые значения для каналов R, G и B)
        String lineDecor = rPrice.getCssValue("text-decoration-line");
        Assert.assertTrue("line-through".equals(lineDecor));

        String rPriceColor = rPrice.getCssValue("color");
        tC = rPriceColor.substring(5, rPriceColor.length()-1).split(", ");

        Assert.assertTrue(Integer.parseInt(tC[0]) == Integer.parseInt(tC[1]));
        Assert.assertTrue(Integer.parseInt(tC[0]) == Integer.parseInt(tC[2]));

        //а) на главной странице и на странице товара совпадает текст названия товара
        driver.findElement(By.xpath("//div[@id='box-campaigns']//li[1]//a[@class='link']")).click();
        wait.until(visibilityOfElementLocated(By.xpath("//h1[@class='title']")));

        String ntName = driver.findElement(By.xpath("//h1[@class='title']")).getText();
        Assert.assertEquals(ntName, tName);

        //б) на главной странице и на странице товара совпадают цены (обычная и акционная)
        WebElement nrPrice = driver.findElement(By.xpath("//div[@class='information']//s[@class='regular-price']"));
        WebElement ncPrice = driver.findElement(By.xpath("//div[@class='information']//strong[@class='campaign-price']"));

        Assert.assertEquals(srPrice, nrPrice.getText());
        Assert.assertEquals(scPrice, ncPrice.getText());

        //д) акционная цена крупнее, чем обычная (это тоже надо проверить на каждой странице независимо)
        rFS = Integer.parseInt(nrPrice.getCssValue("font-size").substring(0,2));
        cFS = Integer.parseInt(ncPrice.getCssValue("font-size").substring(0,2));
        Assert.assertTrue(rFS < cFS);

        // г) акционная жирная и красная (можно считать, что "красный" цвет это такой, у которого в RGBa представлении каналы G и B имеют нулевые значения)
        //        (цвета надо проверить на каждой странице независимо, при этом цвета на разных страницах могут не совпадать)
        //акционная жирная
        cBold = Integer.parseInt(ncPrice.getCssValue("font-weight"));

        Assert.assertEquals(cBold, 700);

        String ncPriceColor = ncPrice.getCssValue("color");
        tC = ncPriceColor.substring(5, ncPriceColor.length()-1).split(", ");

        Assert.assertTrue(Integer.parseInt(tC[1]) == 0);
        Assert.assertTrue(Integer.parseInt(tC[2]) == 0);

        //в) обычная цена зачёркнутая и серая (можно считать, что "серый" цвет это такой, у которого в RGBa представлении одинаковые значения для каналов R, G и B)
        lineDecor = nrPrice.getCssValue("text-decoration-line");
        Assert.assertTrue("line-through".equals(lineDecor));

        String nrPriceColor = nrPrice.getCssValue("color");
        tC = nrPriceColor.substring(5, nrPriceColor.length()-1).split(", ");

        Assert.assertTrue(Integer.parseInt(tC[0]) == Integer.parseInt(tC[1]));
        Assert.assertTrue(Integer.parseInt(tC[0]) == Integer.parseInt(tC[2]));

        //проверки на странице товара
    }
}
