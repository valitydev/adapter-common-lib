package com.rbkmoney.adapter.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ThreeDsFields {

    MD("MD"),
    PA_REQ("PaReq"),
    PA_RES("PaRes"),
    TERM_URL("TermUrl"),
    THREE_DS_METHOD_DATA("ThreeDsMethodData"),
    C_REQ("creq"),
    THREE_DS_SESSION_DATA("threeDSSessionData");

    private final String value;
}
