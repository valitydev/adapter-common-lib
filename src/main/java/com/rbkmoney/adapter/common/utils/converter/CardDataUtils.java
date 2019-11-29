package com.rbkmoney.adapter.common.utils.converter;

import com.rbkmoney.damsel.cds.SessionData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardDataUtils {

    public static String extractCvv2(SessionData sessionData) {
        if (sessionData == null
                || sessionData.getAuthData() == null
                || !sessionData.getAuthData().isSetCardSecurityCode()) {
            return null;
        }
        return sessionData.getAuthData().getCardSecurityCode().getValue();
    }

}
