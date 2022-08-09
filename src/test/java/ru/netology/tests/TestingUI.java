package ru.netology.tests;


import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.pageobjects.TourSuggestionPage;
import ru.netology.utils.*;


import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestingUI {
    TourSuggestionPage tourSuggestionPage;


    @BeforeAll
    static void allureSetup() {
        SelenideLogger.addListener("allure", new AllureSelenide().
                screenshots(true).savePageSource(false));
    }

    @BeforeEach
    void browserSetUp() {
        open("http://localhost:8080/");
        tourSuggestionPage = new TourSuggestionPage();
    }

    @AfterEach
    void tearDown() {
        closeWindow();
    }

    @AfterAll
    static void tearDownAllure() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Успешная отправка заполненной формы и одобрение операции по валидной действующей карте со страницы оплаты")
    void shouldBuyTourWithApprovedCardOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        cardPurchasePage.approvedPayment(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    @DisplayName("Отправка со страницы оплаты заполненной формы с заблокированной картой, отклонение операции")
    void shouldNotBuyTourWithDeclinedCardOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var declinedPayment = DataHelper.declinedPayment(DataHelper.randomPlusMonth());
        cardPurchasePage.declinedPayment(declinedPayment.getCardNumber(), declinedPayment.getMonth(),
                declinedPayment.getYear(), declinedPayment.getCardHolder(), declinedPayment.getCvv());
    }

    @Test
    @DisplayName("Успешное переключение со страницы оплаты на страницу оформления кредита")
    void shouldSwitchFromCardPurchasePageToCreditPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var creditPurchasePage = cardPurchasePage.creditPayment();

    }

    @Test
    @DisplayName("Успешное переключение со страницы оформления кредита на страницу оплаты")
    void shouldSwitchFromCreditPurchasePageToCardPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var cardPurchasePage = creditPurchasePage.ordinaryPayment();
    }

    @Test
    @DisplayName("Успешная отправка заполненной формы и одобрение операции по активной карте со сроком истечения 59 месяцев")
    void shouldBuyTourWith59MonthExpiredCardOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(59);
        cardPurchasePage.approvedPayment(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    @DisplayName("Успешная отправка заполненной формы и одобрение операции по активной карте со сроком истечения 60 месяцев")
    void shouldBuyTourWith60MonthExpiredCardOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(60);
        cardPurchasePage.approvedPayment(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    @DisplayName("Уведомление об ошибке при оформлении покупки по активной карте со сроком истечения более 5 лет")
    void shouldNotBuyTourWith61MonthExpiredCardOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(61);
        cardPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        cardPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Успешная отправка заполненной формы и одобрение операции по активной карте со сроком истечения 1 месяц")
    void shouldBuyTourWithOneMonthExpiredCardOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(1);
        cardPurchasePage.approvedPayment(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    @DisplayName("Успешная отправка заполненной формы и одобрение операции по активной карте со сроком истечения в текущем месяце")
    void shouldBuyTourWithZeroMonthExpiredCardOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(0);
        cardPurchasePage.approvedPayment(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    @DisplayName("Уведомление об ошибке при оформлении покупки по карте, истекшей в прошлом календарном месяце")
    void shouldNotBuyTourWithExpiredCardOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(-1);
        cardPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(), approvedPayment.getYear(),
                approvedPayment.getCardHolder(), approvedPayment.getCvv());
        cardPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Успешная отправка заполненной формы и одобрение операции по активной карте с заполнением поля месяца значением 01")
    void shouldBuyTourWith01MonthValueOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(24);
        cardPurchasePage.approvedPayment(approvedPayment.getCardNumber(), "01",
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    @DisplayName("Уведомление об ошибке при оформлении покупки по карте с заполнением поля месяца значением 00")
    void shouldNotBuyTourWithZeroMonthValueOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(24);
        cardPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), "00",
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        cardPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Успешная отправка формы и одобрение операции по активной карте с заполнением поля месяца значением 12")
    void shouldBuyTourWith12MonthValueOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(24);
        cardPurchasePage.approvedPayment(approvedPayment.getCardNumber(), "12",
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    @DisplayName("Уведомление об ошибке при оформлении покупки по карте с заполнением поля месяца значением 13")
    void shouldNotBuyTourWith13MonthValueOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(24);
        cardPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), "13",
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        cardPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Уведомление об ошибке при отправке пустого поля 'Номер карты' на странице оплаты")
    void shouldRequestCardNumberOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        cardPurchasePage.fillAndSendInfo("", approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        cardPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Уведомление об ошибке при отправке пустого поля 'Месяц' на странице оплаты")
    void shouldRequestMonthOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        cardPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), "",
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        cardPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Уведомление об ошибке при отправке пустого поля 'Год' на странице оплаты")
    void shouldRequestYearOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        cardPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                "", approvedPayment.getCardHolder(), approvedPayment.getCvv());
        cardPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Уведомление об ошибке при отправке пустого поля 'Владелец' на странице оплаты")
    void shouldRequestCardHolderOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        cardPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), "", approvedPayment.getCvv());
        cardPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Уведомление об ошибке при отправке пустого поля 'CVC/CVV' на странице оплаты")
    void shouldRequestCvvOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        cardPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), "");
        cardPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Запрет на ввод кириллицы в поле 'CVC/CVV' на странице оплаты")
    void shouldNotAllowCyrillicSymbolsInCvvNumberFieldOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        cardPurchasePage.getCvvNumberField().setValue(DataHelper.getRandomCyrillicSymbols());
        assertEquals("", cardPurchasePage.getCvvNumberField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод ввод латиницы в поле 'CVC/CVV' на странице оплаты")
    void shouldNotAllowLatinSymbolsInCvvNumberFieldOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        cardPurchasePage.getCvvNumberField().setValue(DataHelper.getRandomLatinSymbols());
        assertEquals("", cardPurchasePage.getCvvNumberField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод спецсимволов в поле 'CVC/CVV' на странице оплаты")
    void shouldNotAllowSpecialNumbersInCvvNumberFieldOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        cardPurchasePage.getCvvNumberField().setValue("!@#$%^&*()_+-~|");
        assertEquals("", cardPurchasePage.getCvvNumberField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод латиницы в поле 'Номер карты' на странице оплаты")
    void shouldNotAllowLatinSymbolsInCardNumberFieldOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        cardPurchasePage.getCardNumberField().setValue(DataHelper.getRandomLatinSymbols());
        assertEquals("", cardPurchasePage.getCardNumberField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод кириллицы в поле 'Номер карты' на странице оплаты")
    void shouldNotAllowCyrillicSymbolsInCardNumberFieldOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        cardPurchasePage.getCardNumberField().setValue(DataHelper.getRandomCyrillicSymbols());
        assertEquals("", cardPurchasePage.getCardNumberField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод спецсимволов в поле 'Номер карты' на странице оплаты")
    void shouldNotAllowSpecialCharactersInCardNumberFieldOnCardPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.ordinaryPayment();
        creditPurchasePage.getCardNumberField().setValue("!@#$%^&*()_+-~|");
        assertEquals("", creditPurchasePage.getCardNumberField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод латиницы в поле 'Месяц' на странице оплаты")
    void shouldNotAllowLatinSymbolsInMonthFieldOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        cardPurchasePage.getMonthField().setValue(DataHelper.getRandomLatinSymbols());
        assertEquals("", cardPurchasePage.getMonthField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод кириллицы в поле 'Месяц' на странице оплаты")
    void shouldNotAllowCyrillicSymbolsInMonthFieldOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        cardPurchasePage.getMonthField().setValue(DataHelper.getRandomCyrillicSymbols());
        assertEquals("", cardPurchasePage.getMonthField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод спецсимволов в поле 'Месяц' на странице оплаты")
    void shouldNotAllowSpecialNumbersInMonthFieldOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        cardPurchasePage.getMonthField().setValue("!@#$%^&*()_+-~|");
        assertEquals("", cardPurchasePage.getMonthField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод кириллицы в поле 'Год' на странице оплаты")
    void shouldNotAllowCyrillicSymbolsInYearFieldOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        cardPurchasePage.getYearField().setValue(DataHelper.getRandomCyrillicSymbols());
        assertEquals("", cardPurchasePage.getYearField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод латиницы в поле 'Год' на странице оплаты")
    void shouldNotAllowLatinSymbolsInYearFieldOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        cardPurchasePage.getYearField().setValue(DataHelper.getRandomLatinSymbols());
        assertEquals("", cardPurchasePage.getYearField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод спецсимволов в поле 'Год' на странице оплаты")
    void shouldNotAllowSpecialCharactersInYearFieldOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        cardPurchasePage.getYearField().setValue("!@#$%^&*()_+-~|");
        assertEquals("", cardPurchasePage.getYearField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод цифр в поле 'Владелец' на странице оплаты")
    void shouldNotAllowNumbersInCardHolderFieldOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        cardPurchasePage.getCardHolderField().setValue("0123456789");
        assertEquals("", cardPurchasePage.getCardHolderField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод спецсимволов в поле 'Владелец' на странице оплаты")
    void shouldNotAllowSpecialNumbersInCardHolderFieldOnCardPurchasePage() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        cardPurchasePage.getCardHolderField().setValue("!@#$%^&*()_+-~|");
        assertEquals("", cardPurchasePage.getCardHolderField().getValue());
    }

    @Test
    @DisplayName("Уведомление об ошибке при отправке пустой формы со страницы оплаты")
    void shouldRequireFillPaymentFormFields() {
        var cardPurchasePage = tourSuggestionPage.ordinaryPayment();
        cardPurchasePage.sendEmptyForm();
    }

    @Test
    @DisplayName("Запрет на ввод кириллицы в поле 'CVC/CVV' на странице оформления кредита")
    void shouldNotAllowCyrillicSymbolsInCvvNumberFieldOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.getCvvNumberField().setValue(DataHelper.getRandomCyrillicSymbols());
        assertEquals("", creditPurchasePage.getCvvNumberField().getValue());
    }

    @Test
    @DisplayName("Успешная отправка заполненной формы и одобрение операции по действующей карте со страницы оформления кредита")
    void shouldBuyTourWithApprovedCardOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        creditPurchasePage.approvedPayment(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }


    @Test
    @DisplayName("Отправка заполненной формы и отлонение операции по заблокированной карте со страницы оформления кредита")
    void shouldNotBuyTourWithDeclinedCardOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var declinedPayment = DataHelper.declinedPayment(DataHelper.randomPlusMonth());
        creditPurchasePage.declinedPayment(declinedPayment.getCardNumber(), declinedPayment.getMonth(),
                declinedPayment.getYear(), declinedPayment.getCardHolder(), declinedPayment.getCvv());
    }


    @Test
    @DisplayName("Успешная отправка заполненной формы и одобрение кредита по активной карте со сроком истечения 59 месяцев")
    void shouldBuyTourWith59MonthExpiredCardOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(59);
        creditPurchasePage.approvedPayment(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    @DisplayName("Успешная отправка заполненной формы и одобрение кредита по активной карте со сроком истечения 60 месяцев")
    void shouldBuyTourWith60MonthExpiredCardOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(60);
        creditPurchasePage.approvedPayment(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    @DisplayName("Уведомление об ошибке при оформлении кредита по активной карте со сроком истечения более 5 лет")
    void shouldNotBuyTourWith61MonthExpiredCardOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(61);
        creditPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(), approvedPayment.getYear(),
                approvedPayment.getCardHolder(), approvedPayment.getCvv());
        creditPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Успешная отправка заполненной формы и одобрение кредита по активной карте со сроком истечения 1 месяц")
    void shouldBuyTourWithOneMonthExpiredCardOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(1);
        creditPurchasePage.approvedPayment(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    @DisplayName("Успешная отправка заполненной формы и одобрение кредита по активной карте со сроком истечения в текущем месяце")
    void shouldBuyTourWithZeroMonthExpiredCardOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(0);
        creditPurchasePage.approvedPayment(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    @DisplayName("Уведомление об ошибке при оформлении кредита по карте, истекшей в прошлом календарном месяце")
    void shouldNotBuyTourWithExpiredCardOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(-1);
        creditPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(), approvedPayment.getYear(),
                approvedPayment.getCardHolder(), approvedPayment.getCvv());
        creditPurchasePage.inputInvalidError();
    }


    @Test
    @DisplayName("Успешная отправка заполненной формы и одобрение кредита по активной карте с заполнением поля месяца значением 01")
    void shouldBuyTourWith01MonthValueOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(24);
        creditPurchasePage.approvedPayment(approvedPayment.getCardNumber(), "01",
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    @DisplayName("Уведомление об ошибке при оформлении кредита по активной карте с заполнением поля месяца значением 00")
    void shouldNotBuyTourWithZeroMonthValueOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(24);
        creditPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), "00",
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        creditPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Успешная отправка заполненной формы и одобрение кредита по активной карте с заполнением поля месяца значением 12")
    void shouldBuyTourWith12MonthValueOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(24);
        creditPurchasePage.approvedPayment(approvedPayment.getCardNumber(), "12",
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    @DisplayName("Уведомление об ошибке при оформлении кредита по активной карте с заполнением поля месяца значением 13")
    void shouldNotBuyTourWith13MonthValueOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(24);
        creditPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), "13",
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        creditPurchasePage.inputInvalidError();
    }


    @Test
    @DisplayName("Уведомление об ошибке при отправке пустой формы со страницы оформления кредита")
    void shouldRequestFillCreditFormFields() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.sendEmptyForm();
    }


    @Test
    @DisplayName("Уведомление об ошибке при отправке пустого поля 'Номер карты' на странице оформления кредита")
    void shouldRequestCardNumberOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        creditPurchasePage.fillAndSendInfo("", approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        creditPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Уведомление об ошибке при отправке пустого поля 'Месяц' на странице оформления кредита")
    void shouldRequestMonthOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        creditPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), "",
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        creditPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Уведомление об ошибке при отправке пустого поля 'Год' на странице оформления кредита")
    void shouldRequestYearOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        creditPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                "", approvedPayment.getCardHolder(), approvedPayment.getCvv());
        creditPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Уведомление об ошибке при отправке пустого поля 'Владелец' на странице оформления кредита")
    void shouldRequestCardHolderOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        creditPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), "", approvedPayment.getCvv());
        creditPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Уведомление об ошибке при отправке пустого поля 'CVC/CVV' на странице оформления кредита")
    void shouldRequestCvvOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        creditPurchasePage.fillAndSendInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), "");
        creditPurchasePage.inputInvalidError();
    }

    @Test
    @DisplayName("Запрет на ввод спецсимволов в поле 'Номер карты' на странице оформления кредита")
    void shouldNotAllowSpecialCharactersInCardNumberFieldOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.getCardNumberField().setValue("!@#$%^&*()_+-~|");
        assertEquals("", creditPurchasePage.getCardNumberField().getValue());
    }


    @Test
    @DisplayName("Запрет на ввод латиницы в поле 'Номер карты' на странице оформления кредита")
    void shouldNotAllowLatinSymbolsInCardNumberFieldOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.getCardNumberField().setValue(DataHelper.getRandomLatinSymbols());
        assertEquals("", creditPurchasePage.getCardNumberField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод кириллицы в поле 'Номер карты' на странице оформления кредита")
    void shouldNotAllowCyrillicSymbolsInCardNumberFieldOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.getCardNumberField().setValue(DataHelper.getRandomCyrillicSymbols());
        assertEquals("", creditPurchasePage.getCardNumberField().getValue());
    }


    @Test
    @DisplayName("Запрет на ввод латиницы в поле 'Месяц' на странице оформления кредита")
    void shouldNotAllowLatinSymbolsInMonthFieldOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.getMonthField().setValue(DataHelper.getRandomLatinSymbols());
        assertEquals("", creditPurchasePage.getMonthField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод кириллицы в поле 'Месяц' на странице оформления кредита")
    void shouldNotAllowCyrillicSymbolsInMonthFieldOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.getMonthField().setValue(DataHelper.getRandomCyrillicSymbols());
        assertEquals("", creditPurchasePage.getMonthField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод спецсимволов в поле 'Месяц' на странице оформления кредита")
    void shouldNotAllowSpecialNumbersInMonthFieldOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.getMonthField().setValue("!@#$%^&*()_+-~|");
        assertEquals("", creditPurchasePage.getMonthField().getValue());
    }


    @Test
    @DisplayName("Запрет на ввод кириллицы в поле 'Год' на странице оформления кредита")
    void shouldNotAllowCyrillicSymbolsInYearFieldOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.getYearField().setValue(DataHelper.getRandomCyrillicSymbols());
        assertEquals("", creditPurchasePage.getYearField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод латиницы в поле 'Год' на странице оформления кредита")
    void shouldNotAllowLatinZOSymbolsInYearFieldOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.getYearField().setValue(DataHelper.getRandomLatinSymbols());
        assertEquals("", creditPurchasePage.getYearField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод спецсимволов в поле 'Год' на странице оформления кредита")
    void shouldNotAllowSpecialCharactersInYearFieldOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.getYearField().setValue("!@#$%^&*()_+-~|");
        assertEquals("", creditPurchasePage.getYearField().getValue());
    }


    @Test
    @DisplayName("Запрет на ввод цифр в поле 'Владелец' на странице оформления кредита")
    void shouldNotAllowNumbersInCardHolderFieldOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.getCardHolderField().setValue("0123456789");
        assertEquals("", creditPurchasePage.getCardHolderField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод спецсимволов в поле 'Владелец' на странице оформления кредита")
    void shouldNotAllowSpecialNumbersInCardHolderFieldOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.getCardHolderField().setValue("!@#$%^&*()_+-~|");
        assertEquals("", creditPurchasePage.getCardHolderField().getValue());
    }


    @Test
    @DisplayName("Запрет на ввод латиницы в поле 'CVC/CVV' на странице оформления кредита")
    void shouldNotAllowLatinSymbolsInCvvNumberFieldOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.getCvvNumberField().setValue(DataHelper.getRandomLatinSymbols());
        assertEquals("", creditPurchasePage.getCvvNumberField().getValue());
    }

    @Test
    @DisplayName("Запрет на ввод спецсимволов в поле 'CVC/CVV' на странице оформления кредита")
    void shouldNotAllowSpecialNumbersInCvvNumberFieldOnCreditPurchasePage() {
        var creditPurchasePage = tourSuggestionPage.creditPayment();
        creditPurchasePage.getCvvNumberField().setValue("!@#$%^&*()_+-~|");
        assertEquals("", creditPurchasePage.getCvvNumberField().getValue());
    }
}

