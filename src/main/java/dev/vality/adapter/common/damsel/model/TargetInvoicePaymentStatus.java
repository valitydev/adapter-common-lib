package dev.vality.adapter.common.damsel.model;

public enum TargetInvoicePaymentStatus {

    PROCESSED,
    CAPTURED,
    CANCELLED,
    REFUNDED;

    public static TargetInvoicePaymentStatus valueFromThrift(
            dev.vality.damsel.domain.TargetInvoicePaymentStatus targetInvoicePaymentStatus) {
        if (targetInvoicePaymentStatus.isSetProcessed()) {
            return PROCESSED;
        } else if (targetInvoicePaymentStatus.isSetCaptured()) {
            return CAPTURED;
        } else if (targetInvoicePaymentStatus.isSetCancelled()) {
            return CANCELLED;
        } else if (targetInvoicePaymentStatus.isSetRefunded()) {
            return REFUNDED;
        }
        return null;
    }
}
