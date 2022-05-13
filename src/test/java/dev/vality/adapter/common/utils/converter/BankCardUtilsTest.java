package dev.vality.adapter.common.utils.converter;

import dev.vality.adapter.common.cds.model.CardDataProxyModel;
import dev.vality.adapter.common.utils.BankCardUtils;
import dev.vality.cds.storage.CardData;
import dev.vality.cds.storage.ExpDate;
import dev.vality.damsel.domain.BankCard;
import dev.vality.damsel.domain.BankCardExpDate;
import org.junit.jupiter.api.Test;

import static dev.vality.adapter.common.utils.BankCardUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankCardUtilsTest {

    private static final short TEST_YEAR = 2020;
    private static final byte TEST_MONTH = 3;
    private static final byte TEST_NEW_MONTH = 6;
    private static final ExpDate TEST_EXP_DATE = new ExpDate(TEST_MONTH, TEST_YEAR);
    private static final CardData TEST_CARD_DATA =
            new CardData("pan").setExpDate(new ExpDate(TEST_NEW_MONTH, TEST_YEAR));
    private static final BankCardExpDate TEST_BANK_CARD_EXP_DATE = new BankCardExpDate(TEST_MONTH, TEST_YEAR);
    private static final BankCard TEST_BANK_CARD = new BankCard().setExpDate(TEST_BANK_CARD_EXP_DATE);
    private static final CardDataProxyModel TEST_CARD_DATA_PROXY_MODEL = CardDataProxyModel.builder()
            .expMonth(TEST_MONTH)
            .expYear(TEST_YEAR)
            .build();

    @Test
    public void bankCardDateTest() {
        // exp date: year 2020, month 3

        assertEquals("20200331", getFullCardExpDate(TEST_BANK_CARD_EXP_DATE));
        assertEquals("20200331", getFullCardExpDate(TEST_CARD_DATA_PROXY_MODEL));

        assertEquals(Integer.valueOf(31), getDayOfMonth(TEST_BANK_CARD_EXP_DATE));
        assertEquals(Integer.valueOf(31), getDayOfMonth(TEST_CARD_DATA_PROXY_MODEL));
        assertEquals(Integer.valueOf(30), getDayOfMonth(TEST_CARD_DATA));

        assertEquals("20", BankCardUtils.getYearFromExpDate(TEST_BANK_CARD_EXP_DATE));
        assertEquals("20", BankCardUtils.getYearFromExpDate(TEST_CARD_DATA_PROXY_MODEL));

        assertEquals("20", getBankCardFormattedYear(TEST_YEAR));
        assertEquals("03", getBankCardFormattedMonth(TEST_MONTH));

        assertEquals("03", getMonthFromExpDate(TEST_BANK_CARD_EXP_DATE));
        assertEquals("03", getMonthFromExpDate(TEST_CARD_DATA_PROXY_MODEL));

        assertEquals("2003", expDateToString(new CardData(), TEST_BANK_CARD));
        assertEquals("2006", expDateToString(TEST_CARD_DATA, TEST_BANK_CARD));

        assertEquals("2003", expDateToString(TEST_CARD_DATA_PROXY_MODEL));
        assertEquals("2003", expDateToString(TEST_EXP_DATE));
        assertEquals("2003", expDateToString(TEST_BANK_CARD_EXP_DATE));

        assertEquals("0620", expDateToString(TEST_YEAR, TEST_NEW_MONTH, MMYY_EXP_DATE_FORMAT));
    }

}
