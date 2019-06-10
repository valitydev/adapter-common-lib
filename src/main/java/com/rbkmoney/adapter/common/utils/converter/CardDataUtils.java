package com.rbkmoney.adapter.common.utils.converter;

import com.rbkmoney.damsel.cds.ExpDate;
import com.rbkmoney.damsel.cds.SessionData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardDataUtils {

    public static String getYearFromExpDate(Short year) {
        return String.format("%1$02d", year % 100);
    }

    public static String getYearFromExpDate(ExpDate expDate) {
        return getYearFromExpDate(expDate.getYear());
    }

    public static String getFullDateFromExpDate(ExpDate expDate) {
        int correctYear = expDate.getYear() / 100 == 0 ? expDate.getYear() + 2000 : expDate.getYear();
        return String.format("%1$04d%2$02d%3$02d", correctYear, expDate.getMonth(), getDayOfMonth(expDate));
    }

    public static Integer getDayOfMonth(ExpDate expDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(expDate.getYear(), expDate.getMonth(), -1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String getMonthFromExpDate(Byte month) {
        return String.format("%1$02d", month);
    }

    public static String getMonthFromExpDate(ExpDate expDate) {
        return String.format("%02d", expDate.getMonth());
    }

    public static String getMonthFromByte(byte month) {
        return String.format("%02d", month);
    }

    public static String getCvv2(SessionData sessionData) {
        if (sessionData == null
                || sessionData.getAuthData() == null
                || !sessionData.getAuthData().isSetCardSecurityCode()) {
            return null;
        }
        return sessionData.getAuthData().getCardSecurityCode().getValue();
    }

}
