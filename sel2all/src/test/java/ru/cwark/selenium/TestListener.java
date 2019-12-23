package ru.cwark.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class TestListener extends AbstractWebDriverEventListener {
    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        System.out.println(by);
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        System.out.println(by +  " found");
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        System.out.println(throwable);
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        System.out.println(element.getTagName() +  " to Click");
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        //System.out.println(element.getTagName() +  " clicked");
    }
}
