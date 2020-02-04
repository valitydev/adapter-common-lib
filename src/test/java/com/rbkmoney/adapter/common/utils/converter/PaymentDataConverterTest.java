package com.rbkmoney.adapter.common.utils.converter;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PaymentDataConverterTest {

    @Test
    public void prepareOrder() {
        String prepareOrder = PaymentDataConverter.prepareOrder("1CXKmuvVqEq6", "937");
        Assert.assertTrue(Long.parseLong(prepareOrder) > 0);
    }

}