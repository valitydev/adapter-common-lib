package dev.vality.adapter.common.utils.damsel.converter;

import dev.vality.damsel.domain.BankCardExpDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BankCardExpDateConverter {

    /**
     * Return date format should be YYММ
     */
    public String getYearMonth(BankCardExpDate bankCardExpDate) {
        return String.format("%1$02d%2$02d", bankCardExpDate.getYear() % 100, bankCardExpDate.getMonth());
    }

    /**
     * Return last 2 digits year (e.g. YY)
     */
    public static String getYear(BankCardExpDate bankCardExpDate) {
        return String.format("%1$02d", bankCardExpDate.getYear() % 100);
    }

    /**
     * Return full date YYYYMMDD
     */
    public static String getFullDate(BankCardExpDate bankCardExpDate) {
        return String.format(
                "%1$04d%2$02d%3$02d",
                bankCardExpDate.getYear(),
                bankCardExpDate.getMonth(),
                getDayOfMonth(bankCardExpDate)
        );
    }

    /**
     * Return full date MM
     */
    public static String getMonth(BankCardExpDate bankCardExpDate) {
        return String.format("%1$02d", bankCardExpDate.getMonth());
    }

    /**
     * Return max days of month (DD)
     */
    public static Integer getDayOfMonth(BankCardExpDate bankCardExpDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(bankCardExpDate.getYear(), bankCardExpDate.getMonth(), -1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
