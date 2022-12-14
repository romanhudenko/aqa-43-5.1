package ru.netology.delivery.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    public void criticalPath() {
        var daysToAddForFirstMeeting = 4;
        String planningDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        String secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        DataGenerator.UserInfo userInfo = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=\"city\"] input").sendKeys(userInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=\"date\"] input").sendKeys(planningDate);
        $("[data-test-id=\"name\"] input").sendKeys(userInfo.getName());
        $("[data-test-id=\"phone\"] span span input").sendKeys(userInfo.getPhone());
        $("[data-test-id=\"agreement\"]").click();
        $x("//*[contains(text(),'Запланировать')]").click();
        $(".notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(visible);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=\"date\"] input").sendKeys(secondMeetingDate);
        $x("//*[contains(text(),'Запланировать')]").click();
        $x("//*[contains(text(),'Перепланировать')]").click();
        $(".notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Should raise error about wrong city")
    public void wrongCity() {
        DataGenerator.UserInfo userInfo = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=\"city\"] input").sendKeys(DataGenerator.generateNonRussianCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        String planningDate = DataGenerator.generateDate(4);
        $("[data-test-id=\"date\"] input").sendKeys(planningDate);
        $("[data-test-id=\"name\"] input").sendKeys(userInfo.getName());
        $("[data-test-id=\"phone\"] span span input").sendKeys(userInfo.getPhone());
        $("[data-test-id=\"agreement\"]").click();
        $x("//*[contains(text(),'Запланировать')]").click();
        $x("//*[contains(text(),'Доставка в выбранный город недоступна')]").shouldBe(visible);
    }

    @Test
    @DisplayName("Should raise error about wrong delivery date")
    public void tooEarly() {
        DataGenerator.UserInfo userInfo = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=\"city\"] input").sendKeys(userInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        String planningDate = DataGenerator.generateDate(2);
        $("[data-test-id=\"date\"] input").sendKeys(planningDate);
        $("[data-test-id=\"name\"] input").sendKeys(userInfo.getName());
        $("[data-test-id=\"phone\"] span span input").sendKeys(userInfo.getPhone());
        $("[data-test-id=\"agreement\"]").click();
        $x("//*[contains(text(),'Запланировать')]").click();
        $x("//*[contains(text(),'Заказ на выбранную дату невозможен')]").shouldBe(visible);
    }

    @Test
    @DisplayName("Should forbid to plan meeting with incorrect name")
    public void wrongName() {
        DataGenerator.UserInfo userInfo = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=\"city\"] input").sendKeys(userInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        String planningDate = DataGenerator.generateDate(4);
        $("[data-test-id=\"date\"] input").sendKeys(planningDate);
        $("[data-test-id=\"name\"] input").sendKeys(DataGenerator.generateNonRussianName());
        $("[data-test-id=\"phone\"] span span input").sendKeys(userInfo.getPhone());
        $("[data-test-id=\"agreement\"]").click();
        $x("//*[contains(text(),'Запланировать')]").click();
        $x("//*[contains(text(),'Имя и Фамилия указаные неверно')]").shouldBe(visible);
    }

    @Test
    public void noAgreement() {
        String fullCLass = "checkbox checkbox_size_m checkbox_theme_alfa-on-white input_invalid";
        DataGenerator.UserInfo userInfo = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=\"city\"] input").sendKeys(userInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        String planningDate = DataGenerator.generateDate(4);
        $("[data-test-id=\"date\"] input").sendKeys(planningDate);
        $("[data-test-id=\"name\"] input").sendKeys(userInfo.getName());
        $("[data-test-id=\"phone\"] span span input").sendKeys(userInfo.getPhone());
        $x("//*[contains(text(),'Запланировать')]").click();
        $("[data-test-id=\"agreement\"]").shouldHave(
                attribute("class", fullCLass)
        );
    }
}
