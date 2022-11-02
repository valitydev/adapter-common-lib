package dev.vality.adapter.common.secret;

import lombok.Data;
import lombok.ToString;

@Data
public class SecretValue {
    @ToString.Exclude
    private String value;
}
