package com.rbkmoney.adapter.common.utils.converter;

import com.rbkmoney.damsel.domain.BankCardExpDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BankCardUtils {

    public static String getYearFromBankCardExpDate(Short year) {
        return String.format("%1$02d", year % 100);
    }

    public static String getYearFromBankCardExpDate(BankCardExpDate expDate) {
        return getYearFromBankCardExpDate(expDate.getYear());
    }

    public static String getFullDateFromBankCardExpDate(BankCardExpDate bankCardExpDate) {
        int correctYear = bankCardExpDate.getYear() / 100 == 0 ? bankCardExpDate.getYear() + 2000 : bankCardExpDate.getYear();
        return String.format("%1$04d%2$02d%3$02d", correctYear, bankCardExpDate.getMonth(), getDayOfMonth(bankCardExpDate));
    }

    public static Integer getDayOfMonth(BankCardExpDate expDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(expDate.getYear(), expDate.getMonth(), -1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String getMonthFromExpDate(Byte month) {
        return String.format("%1$02d", month);
    }

    public static String getMonthFromBankCardExpDate(BankCardExpDate expDate) {
        return String.format("%02d", expDate.getMonth());
    }

    public static String getMonthFromByte(byte month) {
        return String.format("%02d", month);
    }

}
