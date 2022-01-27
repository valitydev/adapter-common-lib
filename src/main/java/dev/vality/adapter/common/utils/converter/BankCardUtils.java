package dev.vality.adapter.common.utils.converter;

import dev.vality.cds.storage.CardData;
import dev.vality.cds.storage.ExpDate;
import dev.vality.damsel.domain.BankCard;
import dev.vality.damsel.domain.BankCardExpDate;
import dev.vality.java.cds.utils.model.CardDataProxyModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BankCardUtils {

    private static final String DEFAULT_NUMBER_FORMAT = "%1$02d";

    public static final String DEFAULT_EXP_DATE_FORMAT = "%1$02d%2$02d";
    public static final String YYMM_EXP_DATE_FORMAT = DEFAULT_EXP_DATE_FORMAT;
    public static final String MMYY_EXP_DATE_FORMAT = "%2$02d%1$02d";

    public static String getFullCardExpDate(CardDataProxyModel expDate) {
        return getFullCardExpDate(expDate.getExpYear(), expDate.getExpMonth());
    }

    public static String getFullCardExpDate(BankCardExpDate expDate) {
        return getFullCardExpDate(expDate.getYear(), expDate.getMonth());
    }

    public static String getFullCardExpDate(short year, byte month) {
        int correctYear = year < 100 ? year + 2000 : year;
        return String.format("%1$04d%2$02d%3$02d", correctYear, month, getDayOfMonth(year, month));
    }

    public static Integer getDayOfMonth(CardDataProxyModel cardData) {
        return getDayOfMonth(cardData.getExpYear(), cardData.getExpMonth());
    }

    public static Integer getDayOfMonth(CardData cardData) {
        ExpDate expDate = cardData.getExpDate();
        return getDayOfMonth(expDate.getYear(), expDate.getMonth());
    }

    public static Integer getDayOfMonth(BankCardExpDate expDate) {
        return getDayOfMonth(expDate.getYear(), expDate.getMonth());
    }

    public static Integer getDayOfMonth(short year, byte month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, -1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String getYearFromExpDate(CardDataProxyModel expDate) {
        return getBankCardFormattedYear(expDate.getExpYear());
    }

    public static String getYearFromExpDate(BankCardExpDate expDate) {
        return getBankCardFormattedYear(expDate.getYear());
    }

    public static String getMonthFromExpDate(CardDataProxyModel expDate) {
        return getBankCardFormattedMonth(expDate.getExpMonth());
    }

    public static String getMonthFromExpDate(BankCardExpDate expDate) {
        return getBankCardFormattedMonth(expDate.getMonth());
    }

    public static String getBankCardFormattedYear(Short year) {
        return String.format(DEFAULT_NUMBER_FORMAT, year % 100);
    }

    public static String getBankCardFormattedMonth(Byte month) {
        return String.format(DEFAULT_NUMBER_FORMAT, month);
    }

    public static String expDateToString(CardData cardData, BankCard bankCard) {
        if (cardData.isSetExpDate()) {
            return expDateToString(cardData.getExpDate());
        }
        return expDateToString(bankCard.getExpDate());
    }

    public static String expDateToString(CardDataProxyModel model) {
        return expDateToString(model.getExpYear(), model.getExpMonth(), DEFAULT_EXP_DATE_FORMAT);
    }

    public static String expDateToString(CardDataProxyModel model, String expDateFormat) {
        return expDateToString(model.getExpYear(), model.getExpMonth(), expDateFormat);
    }

    public static String expDateToString(ExpDate expDate) {
        return expDateToString(expDate.getYear(), expDate.getMonth(), DEFAULT_EXP_DATE_FORMAT);
    }

    public static String expDateToString(ExpDate expDate, String expDateFormat) {
        return expDateToString(expDate.getYear(), expDate.getMonth(), expDateFormat);
    }

    public static String expDateToString(BankCardExpDate expDate) {
        return expDateToString(expDate.getYear(), expDate.getMonth(), DEFAULT_EXP_DATE_FORMAT);
    }

    public static String expDateToString(BankCardExpDate expDate, String expDateFormat) {
        return expDateToString(expDate.getYear(), expDate.getMonth(), expDateFormat);
    }

    public static String expDateToString(short year, byte month, String expDateFormat) {
        return String.format(expDateFormat, year % 100, month);
    }
}
