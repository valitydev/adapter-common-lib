package com.rbkmoney.adapter.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ThreeDsFields {

    MD("MD"),
    PA_REQ("PaReq"),
    PA_RES("PaRes"),
    TERM_URL("TermUrl");

    private final String value;
}
