package dev.vality.adapter.common.cds;

import dev.vality.adapter.common.cds.model.CardDataProxyModel;
import dev.vality.cds.storage.CardData;
import dev.vality.damsel.domain.BankCard;
import dev.vality.damsel.domain.BankCardExpDate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankCardExtractorTest {

    public static final String TEST_2 = "test_2";
    public static final String TEST_1 = "test_1";

    @Test
    void initCardDataProxyModel() {
        CardDataProxyModel cardDataProxyModel =
                BankCardExtractor.initCardDataProxyModel(
                        new BankCard()
                                .setToken("test")
                                .setExpDate(new BankCardExpDate()),
                        new CardData(),
                        List.of(TEST_1, TEST_2));

        assertEquals(TEST_1, cardDataProxyModel.getCardholderName());
    }

    @Test
    void initCardDataProxyModelWithOptionalExpDate() {
        String pan = "pan";
        CardDataProxyModel cardDataProxyModel =
                BankCardExtractor.initCardDataProxyModelWithOptionalExpDate(
                        new BankCard()
                                .setToken("test")
                                .setCardholderName(TEST_1),
                        new CardData()
                                .setPan(pan));

        assertEquals(TEST_1, cardDataProxyModel.getCardholderName());
        assertEquals(pan, cardDataProxyModel.getPan());
        assertEquals(0, cardDataProxyModel.getExpMonth());
        assertEquals(0, cardDataProxyModel.getExpYear());
    }
}