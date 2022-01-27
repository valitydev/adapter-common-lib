package dev.vality.adapter.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dev.vality.adapter.common.enums.Step;
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
    @JsonProperty("cReq")
    private String creq;
    private String threeDsMethodData;
    private String threeDsSessionData;
    private String trxId;
    private Step step;
    private Map<String, String> options;

    @JsonUnwrapped
    private PollingInfo pollingInfo;
}