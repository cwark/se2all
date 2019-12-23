package ru.cwark.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class ProductTests extends TestBase {

    @Test
    public void testLogProductPage() throws InterruptedException {
        get(catalogPage);
        login();

        // открываем все категории
        By b = By.xpath("//table[@class='dataTable']//i[@class='fa fa-folder']/../a");
        WebElement element = driver.findElement(b);
        click(b);
        wait.until(ExpectedConditions.visibilityOfElementLocated(b));

        while (isElementPresent(b)) {
            WebElement tmpElement = driver.findElement(b);
            click(b);
        }

        // получаем список всех товаров
        List<String> listNames = new ArrayList<String>();
        List<WebElement> list = driver.findElements(By.xpath("//table[@class='dataTable']//td[3]/a"));
        for (WebElement item : list) {
            listNames.add(item.getText());
        }

        // открываем товар
        for (String name : listNames) {
            click(By.xpath("//table[@class='dataTable']//td[3]/a[.='" + name + "']"));
            wait.until(presenceOfElementLocated(By.xpath("//td[@id='content']//div[@class='tabs']")));
            Thread.sleep(500);
            List<LogEntry> lLogEntry = driver.manage().logs().get("browser").getAll();
            if (lLogEntry.size() > 0) {
                for (LogEntry logEntry : lLogEntry) {
                    System.out.println(logEntry);
                }
            }

            click(By.xpath("//button[@name='cancel']"));
            wait.until(invisibilityOfElementLocated(By.xpath("//button[@name='cancel']")));
            Thread.sleep(500);
        }
    }

    private boolean isElementPresent(By xpath) {
        return (driver.findElements(xpath).size() > 0);
    }

    @Test
    public void testAddProduct() throws InterruptedException {
        get(catalogPage);
        login();

        wait.until(visibilityOfElementLocated(By.xpath("//a[@class='button'][contains(text(),'Add New Product')]")));

        click(By.xpath("//a[@class='button'][contains(text(),'Add New Product')]"));
        wait.until(visibilityOfElementLocated(By.xpath("//h1")));
        wait.until(visibilityOfElementLocated(By.xpath("//div[@id='tab-general']")));

        Thread.sleep(500);
        click(By.xpath("//div[@id='tab-general']//input[@name='status'][@value='1']"));
        Thread.sleep(500);

        String productName = getRandomShortString();

        type(By.xpath("//div[@id='tab-general']//input[@name='name[en]']"), productName);
        type(By.xpath("//div[@id='tab-general']//input[@name='code']"), getRandomNumberString());
        type(By.xpath("//div[@id='tab-general']//input[@name='quantity']"), getRandomNumberString(2));

        String gValue = "1-" + Long.toString(random(3, true));
        click(By.xpath("//div[@id='tab-general']//input[@name='product_groups[]'][@value='" + gValue + "']"));

        String fName = System.getProperty("user.dir") + "\\src\\resources\\133989045_0.jpg";
        attachFile(By.xpath("//div[@id='tab-general']//input[@name='new_images[]']"), fName);

        click(By.xpath("//div[@class='tabs']//a[@href='#tab-information']"));
        wait.until(presenceOfElementLocated(By.xpath("//select[@name='manufacturer_id']")));

        Select s = new Select(driver.findElement(By.xpath("//select[@name='manufacturer_id']")));
        s.selectByValue("4");

        Thread.sleep(500);

        type(By.xpath("//div[@class='trumbowyg-editor']"));

        click(By.xpath("//div[@class='tabs']//a[@href='#tab-prices']"));
        wait.until(visibilityOfElementLocated(By.xpath("//select[@name='tax_class_id']")));

        type(By.xpath("//div[@id='tab-prices']//input[@name='purchase_price']"), getRandomNumberString(2));

        s = new Select(driver.findElement(By.xpath("//select[@name='purchase_price_currency_code']")));
        s.selectByValue("EUR");

        Thread.sleep(500);

        click(By.xpath("//button[@name='save']"));
        wait.until(visibilityOfElementLocated(By.xpath("//a[@class='button'][contains(text(),'Add New Product')]")));

        Thread.sleep(500);

        type(By.xpath("//form[@name='search_form']/input[@name='query']"), productName + Keys.ENTER);
        wait.until(visibilityOfElementLocated(By.xpath("//form[@name='catalog_form']//a[contains(text(), '" + productName + "')]")));
    }

    @Test
    public void testProductItems() {
        get(baseUrl);
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
        click(By.xpath("//div[@id='box-campaigns']//li[1]//a[@class='link']"));
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

    private String[] getArrayColor(WebElement element) {
        String rgba = element.getCssValue("color");
        return rgba.substring(rgba.indexOf("(") + 1, rgba.indexOf(")")).split(", ");
    }


}
