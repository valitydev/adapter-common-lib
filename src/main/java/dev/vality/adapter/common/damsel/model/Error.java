package dev.vality.adapter.common.damsel.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Error {

    public static final String DEFAULT_ERROR_CODE = "error";
    public static final String SLEEP_TIMEOUT = "sleep timeout";
    public static final String UNKNOWN = "unknown";
    public static final String THREE_DS_NOT_FINISHED = "3DS_NOT_FINISHED";

}
