package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.pageobjects.*;
import ru.netology.utils.*;


import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestingDB {

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
    @DisplayName("Не сохранять номер карты в БД при заказе со страницы оплаты")
    void shouldNotSaveCreditIdOnPaymentPageTest() throws InterruptedException {
        var paymentPage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        paymentPage.fillAndSendInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        paymentPage.anyNotification();
        assertEquals("null", DBUtils.getCreditId());
    }

    @Test
    @DisplayName("Не сохранять номер карты в БД при заказе со страницы оформления кредита")
    void shouldNotSaveCreditIdOnCreditPageTest() {
        var creditPage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        creditPage.fillAndSendInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        creditPage.anyNotification();
        assertEquals("null", DBUtils.getCreditId());
    }

    @Test
    @DisplayName("Сохранять платеж с действующей карты в БД как одобренный при заказе со страницы оплаты")
    void shouldApprovePaymentsWithApprovedCardOnPaymentPageTest() {
        var paymentPage = tourSuggestionPage.ordinaryPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        paymentPage.fillAndSendInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        paymentPage.anyNotification();
        assertEquals("APPROVED", DBUtils.getPaymentStatus());
    }

    @Test
    @DisplayName("Сохранять платеж с недействительной карты в БД как отклоненный при заказе со страницы оплаты")
    void shouldDeclinePaymentsWithDeclinedCardOnPaymentPageTest() {
        var paymentPage = tourSuggestionPage.ordinaryPayment();
        var declinedPayment = DataHelper.declinedPayment(DataHelper.randomPlusMonth());
        paymentPage.fillAndSendInfo(declinedPayment.getCardNumber(), declinedPayment.getMonth(),
                declinedPayment.getYear(), declinedPayment.getCardHolder(), declinedPayment.getCvv());
        paymentPage.anyNotification();
        assertEquals("DECLINED", DBUtils.getPaymentStatus());
    }

    @Test
    @DisplayName("Сохранять платеж с действующей карты в БД как одобренный при заказе со страницы оформления кредита")
    void shouldApprovePaymentsWithApprovedCardOnCreditPageTest() {
        var creditPage = tourSuggestionPage.creditPayment();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        creditPage.fillAndSendInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        creditPage.anyNotification();
        assertEquals("APPROVED", DBUtils.getCreditStatus());
    }

    @Test
    @DisplayName("Сохранять платеж с недействительной карты в БД как отклоненный при заказе со страницы оформления кредита")
    void shouldDeclinePaymentsWithDeclinedCardOnCreditPageTest() {
        var creditPage = tourSuggestionPage.creditPayment();
        var declinedPayment = DataHelper.declinedPayment(DataHelper.randomPlusMonth());
        creditPage.fillAndSendInfo(declinedPayment.getCardNumber(), declinedPayment.getMonth(),
                declinedPayment.getYear(), declinedPayment.getCardHolder(), declinedPayment.getCvv());
        creditPage.anyNotification();
        assertEquals("DECLINED", DBUtils.getCreditStatus());
    }
}
