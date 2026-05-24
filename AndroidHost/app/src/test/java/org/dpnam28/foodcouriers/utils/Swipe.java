package org.dpnam28.foodcouriers.utils;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;

import io.appium.java_client.android.AndroidDriver;

public class Swipe {
    private final AndroidDriver driver;
    public Swipe(AndroidDriver driver){
        this.driver = driver;
    }

    public void fastSwipeDown() {
        // Lấy kích thước màn hình hiện tại
        Dimension size = driver.manage().window().getSize();

        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        // 1. Đưa ngón tay vào điểm bắt đầu (Phía dưới màn hình)
        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));

        // 2. Chạm ngón tay xuống màn hình
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        // 3. Kéo ngón tay lên điểm kết thúc.
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(150), PointerInput.Origin.viewport(), startX, endY));

        // 4. Nhấc ngón tay lên
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Thực thi thao tác
        driver.perform(Collections.singletonList(swipe));
    }

    public void fastSwipeUp() {
        // Lấy kích thước màn hình hiện tại
        Dimension size = driver.manage().window().getSize();

        int startX = size.width / 2;
        int startY = (int) (size.height * 0.5);
        int endY = (int) (size.height * 0.8);

        // Khởi tạo "Ngón tay"
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        // 1. Đưa ngón tay vào điểm bắt đầu (Phía trên màn hình)
        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));

        // 2. Chạm ngón tay xuống màn hình
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        // 3. Kéo ngón tay tuột xuống điểm kết thúc.
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(150), PointerInput.Origin.viewport(), startX, endY));

        // 4. Nhấc ngón tay lên khỏi màn hình
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Yêu cầu Appium thực thi thao tác
        driver.perform(Collections.singletonList(swipe));
    }
}
