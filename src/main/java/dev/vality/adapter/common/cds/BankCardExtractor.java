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
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BankCardExtractor {

    private static final Name FAKER_NAME = new Faker(Locale.ENGLISH).name();
    private static final String NAME_REGEXP = "[^a-zA-Z +]";

    public static CardDataProxyModel initCardDataProxyModel(BankCard bankCard, CardData cardData) {
        return initCardDataProxyModel(bankCard, cardData, null);
    }

    public static CardDataProxyModel initCardDataProxyModel(BankCard bankCard,
                                                            CardData cardData,
                                                            List<String> cardHoldersNames) {
        String cardHolder = extractCardHolder(bankCard, cardData, cardHoldersNames);
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

    private static String extractCardHolder(BankCard bankCard, CardData cardData, List<String> cardHoldersNames) {
        if (bankCard.isSetCardholderName()) {
            return bankCard.getCardholderName();
        } else if (cardData.isSetCardholderName()) {
            return cardData.getCardholderName();
        } else if (!CollectionUtils.isEmpty(cardHoldersNames)) {
            return getCardHolderFromList(cardHoldersNames, bankCard.getToken());
        } else {
            return (FAKER_NAME.firstName() + StringUtils.SPACE + FAKER_NAME.lastName())
                    .replaceAll(NAME_REGEXP, StringUtils.EMPTY)
                    .toUpperCase();
        }
    }

    private static String getCardHolderFromList(List<String> cardHoldersNames, String cardToken) {
        return cardHoldersNames.get(Math.abs(cardToken.hashCode() % cardHoldersNames.size()));
    }

    public static CardDataProxyModel initCardDataProxyModelWithOptionalExpDate(BankCard bankCard, CardData cardData) {
        String cardHolder = extractCardHolder(bankCard, cardData, null);
        return CardDataProxyModel.builder()
                .cardholderName(cardHolder)
                .pan(cardData.getPan())
                .expMonth(getExpMonth(bankCard, cardData))
                .expYear(getExpYear(bankCard, cardData))
                .build();
    }

    private static byte getExpMonth(BankCard bankCard, CardData cardData) {
        if (bankCard.isSetExpDate()) {
            return bankCard.getExpDate().getMonth();
        }
        return cardData.isSetExpDate() ? cardData.getExpDate().getMonth() : 0;
    }

    private static short getExpYear(BankCard bankCard, CardData cardData) {
        if (bankCard.isSetExpDate()) {
            return bankCard.getExpDate().getYear();
        }
        return cardData.isSetExpDate() ? cardData.getExpDate().getYear() : 0;
    }
}
