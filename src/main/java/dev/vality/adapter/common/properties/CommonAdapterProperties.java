package dev.vality.adapter.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Validated
public class CommonAdapterProperties {

    @NotEmpty
    private String url;

    private String callbackUrl;

    private String pathCallbackUrl;

    private String pathRecurrentCallbackUrl;

    private String tagPrefix;

}
