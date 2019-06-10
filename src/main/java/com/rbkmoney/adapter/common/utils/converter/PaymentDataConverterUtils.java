package com.rbkmoney.adapter.common.utils.converter;

import com.rbkmoney.adapter.common.enums.TargetStatus;
import com.rbkmoney.damsel.domain.TargetInvoicePaymentStatus;
import com.rbkmoney.damsel.proxy_provider.PaymentContext;
import com.rbkmoney.damsel.proxy_provider.PaymentInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaymentDataConverterUtils {

    private static final String DEFAULT_ID = "1";

    public static String makeHex16length() {
        return String.format("%016X", new Random().nextLong()).toUpperCase();
    }

    public static BigDecimal getFormattedAmount(long amount) {
        return new BigDecimal(amount).movePointLeft(2);
    }

    public static String prepareOrder(String invoiceId) {
        return prepareOrder(invoiceId, DEFAULT_ID);
    }

    public static String prepareOrder(PaymentInfo paymentInfo) {
        return prepareOrder(paymentInfo.getInvoice().getId(), paymentInfo.getPayment().getId());
    }

    public static String prepareOrder(String invoiceId, String paymentId) {
        return Long.toString(new Base62().decodeBase62(invoiceId) ^ Integer.parseInt(paymentId));
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

    public static TargetStatus getTargetStatus(PaymentContext paymentContext) {
        if (paymentContext == null) {
            throw new IllegalArgumentException("PaymentContext cannot be empty");
        } else if (paymentContext.getSession() == null) {
            throw new IllegalArgumentException("Payment context session cannot be empty");
        } else {
            return getTargetStatus(paymentContext.getSession().getTarget());
        }
    }

    public static TargetStatus getTargetStatus(TargetInvoicePaymentStatus targetInvoicePaymentStatus) {
        if (targetInvoicePaymentStatus == null) {
            throw new IllegalArgumentException("Argument targetInvoicePaymentStatus cannot be empty");
        }
        TargetStatus targetStatus;
        if (targetInvoicePaymentStatus.isSetProcessed()) {
            targetStatus = TargetStatus.PROCESSED;
        } else if (targetInvoicePaymentStatus.isSetCancelled()) {
            targetStatus = TargetStatus.CANCELLED;
        } else if (targetInvoicePaymentStatus.isSetCaptured()) {
            targetStatus = TargetStatus.CAPTURED;
        } else if (targetInvoicePaymentStatus.isSetRefunded()) {
            targetStatus = TargetStatus.REFUNDED;
        } else {
            throw new IllegalStateException("Unknown target status: " + targetInvoicePaymentStatus);
        }
        return targetStatus;
    }

}
