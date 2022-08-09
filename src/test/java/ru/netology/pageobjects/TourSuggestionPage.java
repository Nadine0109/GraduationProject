package ru.netology.pageobjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;


public class TourSuggestionPage {

    private SelenideElement header = $("[class = 'heading heading_size_l heading_theme_alfa-on-white']");
    private SelenideElement tourConditions =
            $("[class='grid-row grid-row_gutter-mobile-s_16 grid-row_gutter-desktop-m_24 grid-row_justify_between grid-row_theme_alfa-on-white']");
    private SelenideElement payButton = $(byText("Купить"));
    private SelenideElement creditButton = $(byText("Купить в кредит"));

    public TourSuggestionPage() {
        header.shouldBe(visible).shouldHave(Condition.exactText("Путешествие дня"));
        tourConditions.shouldBe(visible);
    }

    public CardPurchasePage ordinaryPayment() {
        payButton.click();
        return new CardPurchasePage();
    }

    public CreditPurchasePage creditPayment() {
        creditButton.click();
        return new CreditPurchasePage();
    }

}
