package org.dpnam28.foodcouriers;

import com.android.volley.Response;

import org.dpnam28.foodcouriers.ui.login.LoginContract;
import org.dpnam28.foodcouriers.ui.login.LoginPresenter;
import org.dpnam28.foodcouriers.utils.ApiClient;
import org.dpnam28.foodcouriers.utils.Swipe;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import android.content.Context;

import org.mockito.MockedStatic;

import java.time.Duration;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class RegisterValidationTest {
    private AndroidDriver driver;
    private WebDriverWait wait;
    private Swipe swipe;

    UiAutomator2Options options = new UiAutomator2Options()
            .setPlatformName("Android")
            .setUdid("127.0.0.1:5555")
            .setAppPackage("org.dpnam28.foodcouriers")
            .setAppActivity(".SplashScreenActivity")
            .setAutomationName("UiAutomator2")
            .setNoReset(true);

    @Before
    public void setUpRegisterPage() throws MalformedURLException {

        URL appiumServerUrl = new URL("http://127.0.0.1:4723");

        // Khởi tạo Driver
        driver = new AndroidDriver(appiumServerUrl, options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        swipe = new Swipe(driver);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement tabContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("org.dpnam28.foodcouriers:id/tabContainer")));
        try {
            WebElement registerTab = driver.findElement(By.xpath("//android.widget.LinearLayout[@content-desc='Đăng ký']"));
            registerTab.click();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            WebElement tabLayoutInternal = tabContainer.findElement(By.className("android.widget.LinearLayout"));
            tabLayoutInternal.findElements(By.className("android.widget.LinearLayout")).get(0).click();
        }
    }

    private void fillRegisterForm(String name, String email, String pass, String phone, String address) {
        swipe.fastSwipeUp();
        swipe.fastSwipeUp();
        swipe.fastSwipeUp();

        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("org.dpnam28.foodcouriers:id/edtFullNameRegister")));
        nameField.clear();
        nameField.sendKeys(name);

        WebElement emailField = driver.findElement(By.id("org.dpnam28.foodcouriers:id/edtEmailRegister"));
        emailField.clear();
        emailField.sendKeys(email);

        WebElement passField = driver.findElement(By.id("org.dpnam28.foodcouriers:id/edtPasswordRegister"));
        passField.clear();
        passField.sendKeys(pass);

        WebElement phoneField = driver.findElement(By.id("org.dpnam28.foodcouriers:id/edtPhoneRegister"));
        phoneField.clear();
        phoneField.sendKeys(phone);

        swipe.fastSwipeDown();
        swipe.fastSwipeDown();
        swipe.fastSwipeDown();

        WebElement addressField = driver.findElement(By.id("org.dpnam28.foodcouriers:id/edtAddressRegister"));
        addressField.clear();
        addressField.sendKeys(address);
    }

    private void submitAndAssertToast(String expectedToastText) {
        driver.findElement(By.id("org.dpnam28.foodcouriers:id/btnSignUp")).click();
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.widget.Toast[@text='" + expectedToastText + "']")));
        } catch (TimeoutException e) {
            org.junit.Assert.fail("Không tìm thấy Toast: " + expectedToastText);
        }
    }

    @Test
    public void testInvalidEmail() {
        fillRegisterForm("Nguyen Van A", "invalid-email", "123456", "0123456789", "123 Street");
        submitAndAssertToast("Email không hợp lệ");
    }

    @Test
    public void testShortPassword() {
        fillRegisterForm("Nguyen Van A", "valid@gmail.com", "12345", "0123456789", "123 Street");
        submitAndAssertToast("Mật khẩu phải có ít nhất 6 ký tự");
    }

    @Test
    public void testShortFullName() {
        fillRegisterForm("A", "valid@gmail.com", "123456", "0123456789", "123 Street");
        submitAndAssertToast("Họ tên phải có từ 2 đến 50 ký tự");
    }

    @Test
    public void testInvalidPhone() {
        fillRegisterForm("Nguyen Van A", "valid@gmail.com", "123456", "123", "123 Street");
        submitAndAssertToast("Số điện thoại không hợp lệ");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRegisterSuccess() {
        Context mockContext = Mockito.mock(Context.class);
        Context mockAppContext = Mockito.mock(Context.class);
        Mockito.when(mockContext.getApplicationContext()).thenReturn(mockAppContext);

        LoginContract.View mockView = Mockito.mock(LoginContract.View.class);

        try (MockedStatic<ApiClient> mockedApiClientClass = Mockito.mockStatic(ApiClient.class)) {
            ApiClient mockApiClient = Mockito.mock(ApiClient.class);
            mockedApiClientClass.when(() -> ApiClient.getInstance(ArgumentMatchers.any(Context.class)))
                    .thenReturn(mockApiClient);

            LoginPresenter presenter = new LoginPresenter(mockContext, mockView);
            LoginContract.UserForm form = new LoginContract.UserForm(
                    "Test Mock", "mock@gmail.com", "123456", "0123456789",
                    "123 Mock Street", "Khách hàng", 1L
            );

            presenter.register(form);

            ArgumentCaptor<Response.Listener<JSONObject>> successCaptor =
                    ArgumentCaptor.forClass(Response.Listener.class);

            Mockito.verify(mockApiClient).postJson(
                    ArgumentMatchers.eq("users"),
                    ArgumentMatchers.any(JSONObject.class),
                    successCaptor.capture(),
                    ArgumentMatchers.any()
            );

            Mockito.verify(mockView).showLoading(true);

            JSONObject mockServerResponse = new JSONObject();
            successCaptor.getValue().onResponse(mockServerResponse);

            Mockito.verify(mockView).showLoading(false);
            Mockito.verify(mockView).onRegisterSuccess();
        }
    }
}