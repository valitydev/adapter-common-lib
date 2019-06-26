package com.rbkmoney.adapter.common.logback.mdc;

import com.rbkmoney.adapter.common.utils.converter.PaymentDataConverter;
import com.rbkmoney.damsel.domain.TransactionInfo;
import com.rbkmoney.damsel.proxy_provider.PaymentContext;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenContext;
import org.slf4j.MDC;

import java.util.Map;

public class MdcContext {

    private static final String AMOUNT_KEY = "amount";
    public static void mdcPutContext(RecurrentTokenContext context, String[] fieldsToPutInMdc) {
        TransactionInfo transactionInfo = context.getTokenInfo().getTrx();
        String amount = PaymentDataConverter.getFormattedAmount(context.getTokenInfo().getPaymentTool().getMinimalPaymentCost().getAmount()).toString();
        MDC.put(AMOUNT_KEY, amount);
        mdcPutContextTransactionInfo(transactionInfo, fieldsToPutInMdc);
    }

    public static void mdcPutContext(PaymentContext context, String[] fieldsToPutInMdc) {
        TransactionInfo transactionInfo = context.getPaymentInfo().getPayment().getTrx();
        String amount = PaymentDataConverter.getFormattedAmount(context.getPaymentInfo().getPayment().getCost().getAmount()).toString();
        MDC.put(AMOUNT_KEY, amount);
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

    public static void mdcRemoveContext(String[] fieldsToPutInMdc) {
        MDC.remove(AMOUNT_KEY);
        for (String field : fieldsToPutInMdc) {
            MDC.remove(field);
        }
    }
}
