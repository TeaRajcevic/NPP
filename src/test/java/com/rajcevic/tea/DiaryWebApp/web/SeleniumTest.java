package com.rajcevic.tea.DiaryWebApp.web;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.Select;


public class SeleniumTest {

    String driverPath = "D:\\faks\\Gecko\\GeckoDriver.exe";
    public WebDriver driver;

    @Before
    public void startBrowser() {
        System.setProperty("webdriver.gecko.driver", driverPath);
        FirefoxOptions capabilities = new FirefoxOptions();
        capabilities.setCapability("marionette", true);
        driver = new FirefoxDriver(capabilities);

    }

    @Test
    public void tryingToRegister() {
        driver.get("http://localhost:2222/login");

        WebElement register = driver.findElement(By.name("register"));
        register.click();

        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("NewUser");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("testpass");
        Select dropAccount = new Select(driver.findElement(By.name("account")));
        dropAccount.selectByVisibleText("Free");
        WebElement submitButton = driver.findElement(By.name("register"));
        submitButton.click();

        String currentUrl = driver.getCurrentUrl();

        Assert.assertEquals(currentUrl, "http://localhost:2222/login");
    }

    @Test
    public void tryingToLoginWithBadCredentials() {
        driver.get("http://localhost:2222/login");

        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("Tea");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("test");
        WebElement submitButton = driver.findElement(By.name("submit"));
        submitButton.click();

        String currentUrl = driver.getCurrentUrl();

        Assert.assertEquals(currentUrl, "http://localhost:2222/login?error");
    }

    @Test
    public void tryingToLoginWithGoodCredentials() {
        driver.get("http://localhost:2222/login");

        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("admin");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("adminpass");
        WebElement submitButton = driver.findElement(By.name("submit"));
        submitButton.click();

        String currentUrl = driver.getCurrentUrl();

        Assert.assertEquals(currentUrl, "http://localhost:2222/image/new");
    }

    @Test
    public void tryingToLogout() {
        driver.get("http://localhost:2222/login");

        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("admin");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("adminpass");
        WebElement submitButton = driver.findElement(By.name("submit"));
        submitButton.click();

        String currentUrl = driver.getCurrentUrl();

        if (currentUrl.equals("http://localhost:2222/image/new")) {
            WebElement submitButton2 = driver.findElement(By.name("Logout"));
            submitButton2.click();
            currentUrl = driver.getCurrentUrl();
            Assert.assertEquals(currentUrl, "http://localhost:2222/login?logout");
        }
    }

    @Test
    public void tryingToUploadImage() {
        driver.get("http://localhost:2222/login");

        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("admin");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("adminpass");
        WebElement submitButton = driver.findElement(By.name("submit"));
        submitButton.click();

        WebElement uploadElement = driver.findElement(By.id("imageData"));
        uploadElement.sendKeys("C:\\Users\\Tea\\Desktop\\Dvorci\\castle.png");

        WebElement title = driver.findElement(By.name("title"));
        title.sendKeys("Title1");
        WebElement description = driver.findElement(By.name("description"));
        description.sendKeys("description1");
        WebElement tags = driver.findElement(By.name("tags"));
        tags.sendKeys("tags");
        WebElement sizex = driver.findElement(By.name("sizex"));
        sizex.sendKeys("720");
        WebElement sizey = driver.findElement(By.name("sizey"));
        sizey.sendKeys("560");

        Select dropFormat = new Select(driver.findElement(By.name("format")));
        dropFormat.selectByVisibleText("JPG");

        Select dropFilter = new Select(driver.findElement(By.name("filter")));
        dropFilter.selectByVisibleText("Blur");

        WebElement submitImage = driver.findElement(By.name("submitImage"));
        submitImage.click();
        String currentUrl = driver.getCurrentUrl();

        Assert.assertEquals(currentUrl, "http://localhost:2222/image/new");
}

    @Test
    public void tryingToEditImage() {
        driver.get("http://localhost:2222/login");

        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("admin");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("adminpass");
        WebElement submitButton = driver.findElement(By.name("submit"));
        submitButton.click();

        WebElement uploadElement = driver.findElement(By.id("imageData"));
        uploadElement.sendKeys("C:\\Users\\Tea\\Desktop\\Dvorci\\castle.png");

        WebElement title = driver.findElement(By.name("title"));
        title.sendKeys("Title1");
        WebElement description = driver.findElement(By.name("description"));
        description.sendKeys("description1");
        WebElement tags = driver.findElement(By.name("tags"));
        tags.sendKeys("tags");
        WebElement sizex = driver.findElement(By.name("sizex"));
        sizex.sendKeys("720");
        WebElement sizey = driver.findElement(By.name("sizey"));
        sizey.sendKeys("560");
        Select dropFormat = new Select(driver.findElement(By.name("format")));
        dropFormat.selectByVisibleText("JPG");
        Select dropFilter = new Select(driver.findElement(By.name("filter")));
        dropFilter.selectByVisibleText("Blur");
        WebElement submitImage = driver.findElement(By.name("submitImage"));
        submitImage.click();

        WebElement editImage = driver.findElement(By.name("edit"));
        editImage.click();
        WebElement edit = driver.findElement(By.name("edit"));
        edit.click();
        WebElement title2 = driver.findElement(By.id("title"));
        title2.sendKeys("Title2");
        WebElement description2 = driver.findElement(By.id("description"));
        description2.sendKeys("description2");
        WebElement tags2 = driver.findElement(By.id("tags"));
        tags2.sendKeys("tags2");
        WebElement submitImage2 = driver.findElement(By.name("submitImage"));
        submitImage2.click();

        String currentUrl = driver.getCurrentUrl();

        Assert.assertEquals(currentUrl, "http://localhost:2222/gallery");

    }

    @Test
    public void tryingToChangePackageByUser() {
        driver.get("http://localhost:2222/login");

        WebElement username = driver.findElement(By.name("username"));
        username.sendKeys("user");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys("userpass");
        WebElement submitButton = driver.findElement(By.name("submit"));
        submitButton.click();

        WebElement profile = driver.findElement(By.name("profile"));
        profile.click();
        Select dropAccount = new Select(driver.findElement(By.name("account")));
        dropAccount.selectByVisibleText("Pro");
        WebElement submit = driver.findElement(By.name("submit"));
        submit.click();

        String currentUrl = driver.getCurrentUrl();

        Assert.assertEquals(currentUrl, "http://localhost:2222/profile/user");
    }


    @After
    public void endTest() {
        driver.quit();
    }

}