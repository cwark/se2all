package ru.cwark.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class BuyTests extends TestBase {

    /*
1) открыть главную страницу
2) открыть первый товар из списка
2) добавить его в корзину (при этом может случайно добавиться товар, который там уже есть, ничего страшного)
3) подождать, пока счётчик товаров в корзине обновится
4) вернуться на главную страницу, повторить предыдущие шаги ещё два раза, чтобы в общей сложности в корзине было 3 единицы товара
5) открыть корзину (в правом верхнем углу кликнуть по ссылке Checkout)
6) удалить все товары из корзины один за другим, после каждого удаления подождать, пока внизу обновится таблица

Можно оформить сценарий либо как тест, либо как отдельный исполняемый файл.
     */

    @Test
    public void testBuy() throws InterruptedException {
        get(baseUrl);

        for (int step = 0; step < 3; step++) {

            Thread.sleep(500);
            click(By.xpath("//div[@id='box-most-popular']//li[1]/a[@class='link']"));

            wait.until(visibilityOfElementLocated(By.xpath("//button[@name='add_cart_product']")));
            wait.until(elementToBeClickable(By.xpath("//button[@name='add_cart_product']")));

            Thread.sleep(500);

            List<WebElement> list = driver.findElements(By.xpath("//form[@name='buy_now_form']//span[@class='required']"));
            if (list.size() > 0) {
                Select s = new Select(driver.findElement(By.xpath("//select[@name='options[Size]']")));
                s.selectByIndex(2);
            }

            String q = driver.findElement(By.xpath("//div[@id='cart']//span[@class='quantity']")).getText();
            click(By.xpath("//button[@name='add_cart_product']"));
            Thread.sleep(500);
            wait.until(textToBe(By.xpath("//div[@id='cart']//span[@class='quantity']"), Integer.toString(Integer.parseInt(q) + 1)));

            WebElement element = driver.findElement(By.xpath("//button[@name='add_cart_product']"));
            WebElement element1 = driver.findElement(By.xpath("//div[@id='cart']/a[@class='link']"));
            click(By.xpath("//div[@id='logotype-wrapper']/a"));
            wait.until(stalenessOf(element));
            wait.until(stalenessOf(element1));
            wait.until(visibilityOfElementLocated(By.xpath("//ul[@id='slider']//a")));
            Thread.sleep(500);
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='cart']/a[@class='link']")));
        click(By.xpath("//div[@id='cart']/a[@class='link']"));

        wait.until(presenceOfElementLocated(By.xpath("//div[@id='checkout-customer-wrapper']")));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@id='order_confirmation-wrapper']//tr")));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@id='box-checkout-cart']//li[@class='shortcut']")));

        int iQTable = driver.findElements(By.xpath("//div[@id='order_confirmation-wrapper']//tr")).size();
        int iQItems = driver.findElements(By.xpath("//div[@id='box-checkout-cart']//li[@class='shortcut']")).size();

        Thread.sleep(500);

        driver.findElements(By.xpath("//div[@id='box-checkout-cart']//li[@class='shortcut']")).get(0).click();

        for (int step = 0; step < 3; step++) {
            click(By.xpath("//div[@id='box-checkout-cart']//li[1]//button[@name='remove_cart_item']"));

            Thread.sleep(500);

            iQTable--;
            iQItems--;

            if (iQItems > 1) {
                wait.until(numberOfElementsToBe(By.xpath("//div[@id='order_confirmation-wrapper']//tr"), iQTable));
                wait.until(numberOfElementsToBe(By.xpath("//div[@id='box-checkout-cart']//li[@class='shortcut']"), iQItems));
            }else if (iQItems == 1){
                wait.until(numberOfElementsToBe(By.xpath("//div[@id='order_confirmation-wrapper']//tr"), iQTable));
                wait.until(invisibilityOfElementLocated(By.xpath("//div[@id='box-checkout-cart']//li[@class='shortcut']")));
            } else {
                wait.until(textToBe(By.xpath("//div[@id='checkout-cart-wrapper']//p"), "There are no items in your cart."));
            }
        }

    }
}
