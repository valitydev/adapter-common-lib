package com.rbkmoney.adapter.common.logback.mdc;

import com.rbkmoney.damsel.domain.TransactionInfo;
import com.rbkmoney.damsel.proxy_provider.PaymentContext;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import java.util.Map;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MdcContext {

    public static void mdcPutContext(RecurrentTokenContext context, String[] fieldsToPutInMdc) {
        TransactionInfo transactionInfo = context.getTokenInfo().getTrx();
        mdcPutContextTransactionInfo(transactionInfo, fieldsToPutInMdc);
    }

    public static void mdcPutContext(PaymentContext context, String[] fieldsToPutInMdc) {
        TransactionInfo transactionInfo = context.getPaymentInfo().getPayment().getTrx();
        mdcPutContextTransactionInfo(transactionInfo, fieldsToPutInMdc);
    }

    public static void mdcPutContextTransactionInfo(TransactionInfo transactionInfo, String[] fieldsToPutInMdc) {
        if (transactionInfo != null) {
            Map<String, String> trxextra = transactionInfo.getExtra();
            for (String field : fieldsToPutInMdc) {
                MDC.put(field, trxextra.get(field));
            }
        }
    }

    public static void mdcPutContext(PaymentContext context) {
        TransactionInfo transactionInfo = context.getPaymentInfo().getPayment().getTrx();
        mdcPutContextTransactionInfo(transactionInfo);
    }

    public static void mdcPutContext(RecurrentTokenContext context) {
        TransactionInfo transactionInfo = context.getTokenInfo().getTrx();
        mdcPutContextTransactionInfo(transactionInfo);
    }

    public static void mdcPutContextTransactionInfo(TransactionInfo transactionInfo) {
        if (transactionInfo != null) {
            Map<String, String> trxextra = transactionInfo.getExtra();
            String maskPattern = "\\b\\d{6}([\\d\\s]{2,9})\\d{4}\\b";
            Pattern pattern = Pattern.compile(maskPattern);
            for (Map.Entry<String, String> extra : trxextra.entrySet()) {
                if (pattern.matcher(extra.getValue()).find()) {
                    MDC.put(extra.getKey(), extra.getValue());
                }
            }
        }
    }

    public static void mdcRemoveContext(String[] fieldsToPutInMdc) {
        for (String field : fieldsToPutInMdc) {
            MDC.remove(field);
        }
    }
}
