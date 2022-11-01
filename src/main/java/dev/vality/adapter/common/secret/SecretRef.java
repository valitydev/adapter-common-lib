package dev.vality.adapter.common.secret;

import lombok.Data;

import java.util.Map;

@Data
public class SecretRef {
    private String optionsId;
    private String key;

    public SecretRef(Map<String, String> options, String key) {
        this.optionsId = options.get(SecretService.OPTIONS_ID);
        this.key = key;
    }
}
