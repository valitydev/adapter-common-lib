package dev.vality.adapter.common.cds;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import dev.vality.adapter.common.cds.model.CardDataProxyModel;
import dev.vality.adapter.common.exception.CdsStorageExpDateException;
import dev.vality.cds.storage.CardData;
import dev.vality.damsel.domain.BankCard;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BankCardExtractor {

    private static final Name FAKER_NAME = new Faker(Locale.ENGLISH).name();
    private static final String NAME_REGEXP = "[^a-zA-Z +]";

    public static CardDataProxyModel initCardDataProxyModel(BankCard bankCard, CardData cardData) {
        String cardHolder;
        if (bankCard.isSetCardholderName()) {
            cardHolder = bankCard.getCardholderName();
        } else if (cardData.isSetCardholderName()) {
            cardHolder = cardData.getCardholderName();
        } else {
            cardHolder = (FAKER_NAME.firstName() + StringUtils.SPACE + FAKER_NAME.lastName())
                    .replaceAll(NAME_REGEXP, StringUtils.EMPTY)
                    .toUpperCase();
        }

        if (!bankCard.isSetExpDate() && !cardData.isSetExpDate()) {
            throw new CdsStorageExpDateException("Expiration date not found");
        }

        return CardDataProxyModel.builder()
                .cardholderName(cardHolder)
                .pan(cardData.getPan())
                .expMonth(bankCard.isSetExpDate() ? bankCard.getExpDate().getMonth() : cardData.getExpDate().getMonth())
                .expYear(bankCard.isSetExpDate() ? bankCard.getExpDate().getYear() : cardData.getExpDate().getYear())
                .build();
    }
}
