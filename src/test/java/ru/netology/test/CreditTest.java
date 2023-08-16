package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.DataHelperDB;
import ru.netology.page.OrderPage;

public class CreditTest {
    private OrderPage orderPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        String appUrl = System.getProperty("appUrl", "http://localhost:8080/");
        orderPage = new OrderPage(appUrl);
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void clearDataBaseTable() {
        DataHelperDB.clearTables();
    }

    @Test
    @DisplayName("Кредит на тур по валидной карте со статусом APPROVED")
    void shouldCreditValidCardApproved() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageSuccessfully();

        Assertions.assertEquals("APPROVED", DataHelperDB.findCreditStatus());
    }

    @Test
    @DisplayName("Кредит на тур по валидной карте со статусом DECLINED")
    void shouldCreditValidDeclined() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageError();

        Assertions.assertEquals("DECLINED", DataHelperDB.findCreditStatus());
    }

    @Test
    @DisplayName("Кредит на тур по несуществующей карте")
    void shouldCreditInvalidCard() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberNotExist());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageError();

        Assertions.assertEquals(0, DataHelperDB.getOrderTransactionCount());
    }

    @Test
    @DisplayName("Кредит на тур по карте с невалидными данными")
    void shouldCreditNotFilledCard() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberShort());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();

        Assertions.assertEquals(0, DataHelperDB.getOrderTransactionCount());
    }

    @Test
    @DisplayName("Кредит на тур по карте с истекшим сроком дейсвтия (месяц)")
    void shouldCreditExpiredCard() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(-1));
        orderPage.setYear(DataHelper.generateYear(0));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfInvalidDate();
    }

    @Test
    @DisplayName("Кредит на тур по карте с невалидным значением месяца")
    void shouldCreditInvalidMonth() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.getInvalidMonth());
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfInvalidDate();
    }

    @Test
    @DisplayName("Кредит на тур по карте с незаполненным месяцем")
    void shouldCreditEmptyMonth() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.getEmptyValue());
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит на тур по карте c истекшим сроком действия (год)")
    void shouldCreditExpiredYear() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(-1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfCardExpired();
    }

    @Test
    @DisplayName("Кредит на тур по карте с годом +5 лет")
    void shouldCreditYearPlusFive() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(5));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfInvalidDate();
    }

    @Test
    @DisplayName("Кредит на тур с незаполненным полем год")
    void shouldCreditEmptyYear() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.getEmptyValue());
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит на тур с вводом числовых значений в поле Владелец")
    void shouldCreditNumberName() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getNumberOwner());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfRequiredForm();
    }

    @Test
    @DisplayName("Кредит на тур с вводом специальных символов в поле Владелец")
    void shouldCreditSymbolsName() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getSpecialCharactersOwner());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfRequiredForm();
    }

    @Test
    @DisplayName("Кредит на тур с незаполненным полем Владелец")
    void shouldCreditEmptyName() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getEmptyValue());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfRequiredForm();
    }

    @Test
    @DisplayName("Кредит на тур с невалидным CVC (1 цифра)")
    void shouldCreditCVC1() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(1));
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит на тур с невалидным CVC (2 цифры)")
    void shouldCreditCVC2() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(2));
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит на тур с незаполненным полем CVC")
    void shouldCreditEmptyCVC() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getEmptyValue());
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит на тур с незаполненными полями")
    void shouldCreditEmptyFields() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getEmptyValue());
        orderPage.setMonth(DataHelper.getEmptyValue());
        orderPage.setYear(DataHelper.getEmptyValue());
        orderPage.setOwnerName(DataHelper.getEmptyValue());
        orderPage.setCVC(DataHelper.getEmptyValue());
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();
    }
}
