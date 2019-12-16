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

/*
1) зайти в админку
2) открыть пункт меню Countries (или страницу http://localhost/litecart/admin/?app=countries&doc=countries)
3) открыть на редактирование какую-нибудь страну или начать создание новой
4) возле некоторых полей есть ссылки с иконкой в виде квадратика со стрелкой -- они ведут на внешние страницы и открываются в новом окне, именно это и нужно проверить.

Конечно, можно просто убедиться в том, что у ссылки есть атрибут target="_blank". Но в этом упражнении требуется именно кликнуть по ссылке, чтобы она открылась в новом окне, потом переключиться в новое окно, закрыть его, вернуться обратно, и повторить эти действия для всех таких ссылок.

Не забудьте, что новое окно открывается не мгновенно, поэтому требуется ожидание открытия окна.
 */
    @Test
    public void testCountryNewWindow() throws InterruptedException {
        get(countryPage);
        login();

        List<WebElement> list = driver.findElements(By.xpath("//form[@name='countries_form']//tr"));
        WebElement tr = list.get((int)(random(list.size(), true)));
        click(tr.findElement(By.xpath("//td[5]/a")));

        wait.until(visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'Edit Country')]")));
        wait.until(titleContains("Edit Country"));

        Thread.sleep(10000);
    }

    @Test
    public void testCountryAndZones() throws InterruptedException {
        get(adminPage);
        login();
        wait.until(elementToBeClickable(By.xpath("//span[text()='Countries']/..")));

        Thread.sleep(3000);

        click(By.xpath("//span[text()='Countries']/.."));
        wait.until(visibilityOfElementLocated(By.xpath("//td[@id='content']/h1")));
        wait.until(textToBePresentInElementLocated(By.xpath("//td[@id='content']/h1"), "Countries"));
        wait.until(visibilityOfElementLocated(By.cssSelector("li#app-.selected")));

        List<WebElement> lis = driver.findElements(By.xpath("//table[@class='dataTable']//tr/td[5]"));

        for (int idx = 0; idx < lis.size() - 1; idx++) {
            String t1 = lis.get(idx).getText().trim().toLowerCase();
            String t2 = lis.get(idx + 1).getText().trim().toLowerCase();

            Assert.assertTrue(t1.compareToIgnoreCase(t2) <= 0);
        }

        lis = driver.findElements(By.xpath("//table[@class='dataTable']//tr/td[6][.!='0']/../td[5]"));
        if (lis.size() > 0) {
            List<String> cNames = new ArrayList<>();
            for (WebElement element : lis) {
                cNames.add(element.getText().trim());
            }

            for (String name : cNames) {
                click(By.xpath("//td[.='" + name + "']/a"));
                lis = driver.findElements(By.xpath("//table[@id='table-zones']//td/input[contains(@name, '[name]')][@type='hidden']/.."));
                for (int idx = 0; idx < lis.size() - 1; idx++) {
                    String t1 = lis.get(idx).getText().trim();
                    String t2 = lis.get(idx + 1).getText().trim();

                    Assert.assertTrue(t1.compareToIgnoreCase(t2) <= 0);
                }

                Thread.sleep(3000);

                click(By.xpath("//button[@name='cancel']"));
                wait.until(visibilityOfElementLocated(By.xpath("//td[@id='content']/h1")));
                wait.until(textToBePresentInElementLocated(By.xpath("//td[@id='content']/h1"), "Countries"));
            }
        }
    }

    @Test
    public void testGeoZones() throws InterruptedException {
        get(adminPage);
        login();
        wait.until(elementToBeClickable(By.xpath("//span[text()='Geo Zones']/..")));

        Thread.sleep(3000);

        click(By.xpath("//span[text()='Geo Zones']/.."));

        List<WebElement> lis = driver.findElements(By.xpath("//table[@class='dataTable']//tr/td[3]/a"));
        List<String> gNames = new ArrayList<>();
        for (WebElement gName : lis) {
            gNames.add(gName.getText());
        }
        for (String gName : gNames) {
            click(By.xpath("//table[@class='dataTable']//tr/td[3]/a[.='" + gName + "']"));

            wait.until(visibilityOfElementLocated(By.xpath("//td[@id='content']/h1")));
            wait.until(textToBePresentInElementLocated(By.xpath("//td[@id='content']/h1"), "Edit Geo Zone"));

            lis = driver.findElements(By.xpath("//table[@id='table-zones']//td[3]//option[@selected]"));

            for (int idx = 0; idx < lis.size() - 1; idx++) {
                String t1 = lis.get(idx).getText().trim().toLowerCase();
                String t2 = lis.get(idx + 1).getText().trim().toLowerCase();

                Assert.assertTrue(t1.compareToIgnoreCase(t2) <= 0);
            }

            Thread.sleep(3000);

            click(By.xpath("//button[@name='cancel']"));
            wait.until(visibilityOfElementLocated(By.xpath("//td[@id='content']/h1")));
            wait.until(textToBePresentInElementLocated(By.xpath("//td[@id='content']/h1"), "Geo Zones"));
        }

    }
}
