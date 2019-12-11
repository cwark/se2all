package ru.cwark.selenium;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class MyFirstTest extends TestBase{

    @Test
    public void myFirstTest() {
        get("http://www.google.com/");
        driver.findElement(By.name("q")).sendKeys("webdriver");
        click(By.name("btnK"));
        wait.until(titleIs("webdriver - Поиск в Google"));
    }

}
