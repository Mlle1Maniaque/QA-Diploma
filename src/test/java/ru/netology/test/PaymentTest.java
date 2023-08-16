package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.DataHelperDB;
import ru.netology.page.OrderPage;

public class PaymentTest {
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
    @DisplayName("Оплата тура с валидной картой с статусом APPROVED")
    void shouldPayValidCardApproved() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageSuccessfully();

        Assertions.assertEquals("APPROVED", DataHelperDB.findPayStatus());
    }

    @Test
    @DisplayName("Оплата тура с валидной картой с статусом DECLINED")
    void shouldPayValidCardDeclined() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberDeclined());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageError();

        Assertions.assertEquals("DECLINED", DataHelperDB.findPayStatus());
    }

    @Test
    @DisplayName("Оплата тура по карте с невалидными данными")
    void shouldPayNotFilledCard() {
        orderPage.buyByCard();
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
    @DisplayName("Оплата тура по несуществующей карте")
    void shouldPayInvalidCard() {
        orderPage.buyByCard();
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
    @DisplayName("Оплата тура по карте с истекшим сроком действия (месяц)")
    void shouldPayExpiredCard() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(-1));
        orderPage.setYear(DataHelper.generateYear(0));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfInvalidDate();
    }

    @Test
    @DisplayName("Оплата тура по карте с невалидным значением месяца")
    void shouldPayInvalidMonth() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.getInvalidMonth());
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfInvalidDate();
    }

    @Test
    @DisplayName("Оплата тура по карте с незаполненным месяцем")
    void shouldPayEmptyMonth() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.getEmptyValue());
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();
    }

    @Test
    @DisplayName("Оплата тура по карте с истекшим сроком действия (год)")
    void shouldPayExpiredYear() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(-1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfCardExpired();
    }

    @Test
    @DisplayName("Оплата тура по карте с годом +5 лет")
    void shouldPayYearPlusFive() {
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
    @DisplayName("Оплата тура с незаполненным полем год")
    void shouldPayEmptyYear() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.getEmptyValue());
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();
    }

    @Test
    @DisplayName("Оплата тура с вводом числовых значений в поле Владелец")
    void shouldPayNumberName() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getNumberOwner());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfRequiredForm();
    }

    @Test
    @DisplayName("Оплата тура с вводом специальных символов в поле Владелец")
    void shouldPaySymbolsName() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getSpecialCharactersOwner());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfRequiredForm();
    }

    @Test
    @DisplayName("Оплата тура с незаполненным полем Владелец")
    void shouldPayEmptyName() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getEmptyValue());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfRequiredForm();
    }

    @Test
    @DisplayName("Оплата тура с невалидным CVC (1 цифра)")
    void shouldPayCVC1() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(1));
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();
    }

    @Test
    @DisplayName("Оплата тура с невалидным CVC (2 цифры)")
    void shouldPayCVC2() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(2));
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();
    }

    @Test
    @DisplayName("Оплата тура с незаполненным полем CVC")
    void shouldPayEmptyCVC() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getEmptyValue());
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();
    }

    @Test
    @DisplayName("Оплата тура с незаполненными полями")
    void shouldPayEmptyFields() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getEmptyValue());
        orderPage.setMonth(DataHelper.getEmptyValue());
        orderPage.setYear(DataHelper.getEmptyValue());
        orderPage.setOwnerName(DataHelper.getEmptyValue());
        orderPage.setCVC(DataHelper.getEmptyValue());
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();
    }
}