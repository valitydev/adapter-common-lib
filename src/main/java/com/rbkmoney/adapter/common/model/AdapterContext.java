package com.rbkmoney.adapter.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.rbkmoney.adapter.common.enums.Step;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdapterContext {
    private String md;
    private String paReq;
    private String paRes;
    private String acsUrl;
    private String termUrl;
    private String terminationUri;
    private String cReq;
    private String threeDsMethodData;
    private String threeDsSessionData;
    private String trxId;
    private Step step;
    private Map<String, String> options;

    @JsonUnwrapped
    private PollingInfo pollingInfo;
}