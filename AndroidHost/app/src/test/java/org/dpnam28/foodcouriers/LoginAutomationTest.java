package org.dpnam28.foodcouriers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class LoginAutomationTest {
    private AndroidDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setUdid("127.0.0.1:5555")
                .setAppPackage("org.dpnam28.foodcouriers")
                .setAppActivity(".SplashScreenActivity")
                .setAutomationName("UiAutomator2")
                .setNoReset(true);

        // Trỏ tới Appium Server đang chạy
        URL appiumServerUrl = new URL("http://127.0.0.1:4723");

        // Khởi tạo Driver
        driver = new AndroidDriver(appiumServerUrl, options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    private void fillLoginForm(String email, String pass) {
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("org.dpnam28.foodcouriers:id/edtEmailLogin")));
        emailField.clear();
        emailField.sendKeys(email);
        WebElement passField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("org.dpnam28.foodcouriers:id/edtPasswordLogin")));
        passField.clear();
        passField.sendKeys(pass);
        WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("org.dpnam28.foodcouriers:id/btnLogin")));
        loginBtn.click();
    }

    private void assertToast(String expectedToastText) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.widget.Toast[@text='" + expectedToastText + "']")));
        } catch (TimeoutException e) {
            org.junit.Assert.fail("Không tìm thấy Toast: " + expectedToastText);
        }
    }

    @Test
    public void testEmptyData() {
        fillLoginForm("      ", "        ");
        assertToast("Vui lòng nhập đầy đủ thông tin");
    }

    @Test
    public void testInvalidEmail() {
        fillLoginForm("invalid-email", "123456");
        assertToast("Email không hợp lệ");
    }

    @Test
    public void testUnexistedEmail() {
        fillLoginForm("aaa@aaa.aaa", "123456");
        assertToast("Người dùng không tồn tại");
    }

    @Test
    public void testShortPassword() {
        fillLoginForm("valid@gmail.com", "123");
        assertToast("Mật khẩu phải có ít nhất 6 ký tự");
    }

    @Test
    public void testLoginSuccess() {
        fillLoginForm("test@gmail.com", "123456");
        assertToast("Đăng nhập thành công");

        try {
            WebElement info = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.ImageView[@content-desc=\"Thông tin cá nhân\"]")));
            info.click();

            WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("org.dpnam28.foodcouriers:id/btnLogout")));
            logout.click();
        } catch (NoSuchElementException e) {
            org.junit.Assert.fail("Error: Cannot find logout button");
        } finally {
            try {
                WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.id("org.dpnam28.foodcouriers:id/edtEmailLogin")));
                WebElement passInput = driver.findElement(By.id("org.dpnam28.foodcouriers:id/edtPasswordLogin"));
                emailInput.clear();
                passInput.clear();
            } catch (Exception ignored) {
            }
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}