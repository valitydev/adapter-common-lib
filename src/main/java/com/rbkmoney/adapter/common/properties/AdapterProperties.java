package com.rbkmoney.adapter.common.properties;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public abstract class AdapterProperties {

    @NotEmpty
    private String url;

    @NotEmpty
    private String callbackUrl;

    @NotEmpty
    private String pathCallbackUrl;

    @NotEmpty
    private String pathRecurrentCallbackUrl;

}
