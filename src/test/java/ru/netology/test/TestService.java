package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.DataHelperDB;
import ru.netology.page.OrderPage;

public class TestService {
    OrderPage orderPage = new OrderPage();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
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
    @DisplayName("Оплата тура по карте с незаполненным месяцем")
    void shouldPayEmptyMonth() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.getEmptyMonth());
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит на тур по карте с незаполненным месяцем")
    void shouldCreditEmptyMonth() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.getEmptyMonth());
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
    @DisplayName("Оплата тура с незаполненным полем год")
    void shouldPayEmptyYear() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.getEmptyYear());
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getCVC(3));
        orderPage.clickContinueButton();
        orderPage.messageOfIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит на тур с незаполненным полем год")
    void shouldCreditEmptyYear() {
        orderPage.buyByCreditCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.getEmptyYear());
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
    @DisplayName("Оплата тура с незаполненным полем Владелец")
    void shouldPayEmptyName() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getEmptyOwner());
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
        orderPage.setOwnerName(DataHelper.getEmptyOwner());
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
    @DisplayName("Оплата тура с незаполненным полем CVC")
    void shouldPayEmptyCVC() {
        orderPage.buyByCard();
        orderPage.setCardNumber(DataHelper.getCardNumberApproved());
        orderPage.setMonth(DataHelper.generateMonth(1));
        orderPage.setYear(DataHelper.generateYear(1));
        orderPage.setOwnerName(DataHelper.getOwnersName());
        orderPage.setCVC(DataHelper.getEmptyCVC());
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
        orderPage.setCVC(DataHelper.getEmptyCVC());
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