package ru.cwark.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class UserTests extends TestBase {

    @Test
    public void testUserCreation() throws InterruptedException {
        get("http://localhost/litecart/");

        click(By.xpath("//form[@name='login_form']//a"));

        wait.until(titleContains("Create Account"));
        wait.until(visibilityOfElementLocated(By.id("create-account")));
        wait.until(visibilityOfElementLocated(By.xpath("//input[@name='tax_id']")));
        wait.until(visibilityOfElementLocated(By.xpath("//input[@name='company']")));
        wait.until(visibilityOfElementLocated(By.xpath("//input[@name='firstname']")));

        //Thread.sleep(1000);

        type(By.xpath("//input[@name='tax_id']"), getRandomNumberString());
        type(By.xpath("//input[@name='company']"));
        type(By.xpath("//input[@name='firstname']"));
        type(By.xpath("//input[@name='lastname']"));
        type(By.xpath("//input[@name='address1']"));
        type(By.xpath("//input[@name='address2']"));
        type(By.xpath("//input[@name='postcode']"), getRandomPostcode());
        type(By.xpath("//input[@name='city']"));

        Select s = new Select(driver.findElement(By.xpath("//select[@name='country_code']")));
        s.selectByValue("US");

        Thread.sleep(500);

        String email = getRandomEMail();
        type(By.xpath("//input[@name='email']"), email);
        type(By.xpath("//input[@name='phone']"), getRandomNumberString(10));

        click(By.xpath("//input[@name='newsletter']"));

        String psw = getRandomShortString();
        type(By.xpath("//input[@name='password']"), psw);
        type(By.xpath("//input[@name='confirmed_password']"), psw);

        System.out.println(email + ":" + psw);

        click(By.xpath("//button[@name='create_account']"));
        wait.until(visibilityOfElementLocated(By.xpath("//div[@id='notices-wrapper']//div[@class='notice errors']")));

        s = new Select(driver.findElement(By.xpath("//select[@name='zone_code']")));
        s.selectByValue("CA");

        Thread.sleep(500);

        type(By.xpath("//input[@name='password']"), psw);
        type(By.xpath("//input[@name='confirmed_password']"), psw);

        click(By.xpath("//button[@name='create_account']"));

        wait.until(visibilityOfElementLocated(By.xpath("//div[@id='notices-wrapper']//div[@class='notice success']")));
        wait.until(visibilityOfElementLocated(By.xpath("//div[@id='box-account']//a[.='Logout']")));
        wait.until(invisibilityOfElementLocated(By.xpath("//div[@id='notices-wrapper']//div[@class='notice success']")));

        click(By.xpath("//div[@id='box-account']//a[.='Logout']"));

        wait.until(visibilityOfElementLocated(By.xpath("//div[@id='notices-wrapper']//div[@class='notice success']")));
        wait.until(visibilityOfElementLocated(By.xpath("//input[@name='email']")));
        wait.until(visibilityOfElementLocated(By.xpath("//input[@name='password']")));
        wait.until(visibilityOfElementLocated(By.xpath("//button[@name='login']")));

        type(By.xpath("//input[@name='email']"), email);
        type(By.xpath("//input[@name='password']"), psw);

        click(By.xpath("//button[@name='login']"));
        wait.until(visibilityOfElementLocated(By.xpath("//div[@id='notices-wrapper']//div[@class='notice success']")));
        wait.until(visibilityOfElementLocated(By.xpath("//div[@id='box-account']//a[.='Logout']")));

        click(By.xpath("//div[@id='box-account']//a[.='Logout']"));
        wait.until(visibilityOfElementLocated(By.xpath("//div[@id='notices-wrapper']//div[@class='notice success']")));
        wait.until(visibilityOfElementLocated(By.xpath("//input[@name='email']")));
        wait.until(visibilityOfElementLocated(By.xpath("//input[@name='password']")));
        wait.until(visibilityOfElementLocated(By.xpath("//button[@name='login']")));
    }

}
