package com.rbkmoney.adapter.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.adapter.common.enums.Step;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AdapterContext extends PollableContext {

    private String md;
    private String paReq;
    private String paRes;
    private String acsUrl;
    private String termUrl;

    private String trxId;

    private Step step;

}