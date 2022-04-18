package dev.vality.adapter.common.utils.converter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentDataConverterTest {

    @Test
    public void prepareOrder() {
        String prepareOrder = PaymentDataConverter.prepareOrder("1CXKmuvVqEq6", "937");
        assertTrue(Long.parseLong(prepareOrder) > 0);
    }

}