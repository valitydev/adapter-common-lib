package dev.vality.adapter.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AmountConverter {

    public static String getFormattedAmount(long amount) {
        return new BigDecimal(amount).movePointLeft(2).toString();
    }

}
