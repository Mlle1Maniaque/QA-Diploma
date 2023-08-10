package ru.netology.page;


import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class OrderPage {
    private SelenideElement buttonBuy = $$(".button__text").find(exactText("Купить"));
    private SelenideElement buttonBuyOnCredit = $$(".button__text").find(exactText("Купить в кредит"));
    private SelenideElement cardNumberForm = $$(".input__inner").findBy(text("Номер карты"))
            .$(".input__control");
    private SelenideElement monthForm = $$(".input__inner").findBy(text("Месяц"))
            .$(".input__control");
    private SelenideElement yearForm = $$(".input__inner").findBy(text("Год"))
            .$(".input__control");
    private SelenideElement cardOwnerForm = $$(".input__inner").findBy(text("Владелец"))
            .$(".input__control");
    private SelenideElement cvcForm = $$(".input__inner").findBy(text("CVC/CVV"))
            .$(".input__control");

    private SelenideElement cardPay = $$(".heading").find(exactText("Оплата по карте"));
    private SelenideElement creditByCard = $$(".heading")
            .find(exactText("Кредит по данным карты"));
    private SelenideElement messageSuccessfully = $$(".notification__title").find(exactText("Успешно"));
    private SelenideElement messageError = $$(".notification__title").find(exactText("Ошибка"));
    private SelenideElement continueButton = $$(".button__content").find(text("Продолжить"));
    private SelenideElement cardExpired = $$("span.input__sub")
            .find(exactText("Истёк срок действия карты"));
    private SelenideElement invalidCardExpirationDate = $$("span.input__sub")
            .find(exactText("Неверно указан срок действия карты"));
    private SelenideElement incorrectFormat = $$("span.input__sub")
            .find(exactText("Неверный формат"));
    private SelenideElement requiredForm = $$(".input__inner span.input__sub")
            .find(exactText("Поле обязательно для заполнения"));

    public void buyByCard() {
        open("http://localhost:8080/");
        buttonBuy.click();
        cardPay.shouldBe(visible);
    }

    public void buyByCreditCard() {
        open("http://localhost:8080/");
        buttonBuyOnCredit.click();
        creditByCard.shouldBe(visible);
    }

    public void setCardNumber(String number) {
        cardNumberForm.setValue(number);
    }

    public void setMonth(String month) {
        monthForm.setValue(month);
    }

    public void setYear(String year) {
        yearForm.setValue(year);
    }

    public void setOwnerName(String ownerName) {
        cardOwnerForm.setValue(ownerName);
    }

    public void setCVC(String cvc) {
        cvcForm.setValue(cvc);
    }

    public void clickContinueButton() {
        continueButton.click();
    }

    public void messageSuccessfully() {
        messageSuccessfully.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void messageError() {
        messageError.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void messageOfIncorrectFormat() {
        incorrectFormat.shouldBe(visible);
    }

    public void messageOfRequiredForm() {
        requiredForm.shouldBe(visible);
    }

    public void messageOfInvalidDate() {
        invalidCardExpirationDate.shouldBe(visible);
    }

    public void messageOfCardExpired() {
        cardExpired.shouldBe(visible);
    }
}
