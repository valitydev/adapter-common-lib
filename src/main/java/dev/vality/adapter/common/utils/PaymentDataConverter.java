package dev.vality.adapter.common.utils;

import dev.vality.damsel.proxy_provider.PaymentInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaymentDataConverter {

    private static final String DEFAULT_ID = "1";

    public static String makeHex16length() {
        return String.format("%016X", new Random().nextLong()).toUpperCase();
    }

    public static BigDecimal prepareFormattedAmount(long amount) {
        return new BigDecimal(amount).movePointLeft(2);
    }

    public static String prepareOrder(String invoiceId) {
        return prepareOrder(invoiceId, DEFAULT_ID);
    }

    public static String prepareOrder(PaymentInfo paymentInfo) {
        return prepareOrder(paymentInfo.getInvoice().getId(), paymentInfo.getPayment().getId());
    }

    public static String prepareOrder(String invoiceId, String paymentId) {
        return Long.toString(Math.abs(new Base62().decodeBase62(invoiceId) ^ Integer.parseInt(paymentId)));
    }

    public static String extractEmail(PaymentInfo paymentInfo) {
        if (paymentInfo == null
                || paymentInfo.getPayment() == null
                || paymentInfo.getPayment().getContactInfo() == null
                || paymentInfo.getPayment().getContactInfo().getEmail() == null) {
            return "";
        } else {
            return paymentInfo.getPayment().getContactInfo().getEmail();
        }
    }

}
