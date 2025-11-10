package dev.vality.adapter.common.secret;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @deprecated Use {@code vault-client}. This API will be removed in the next major release.
 */
@Deprecated(forRemoval = true)
@Data
@AllArgsConstructor
public class SecretValue {
    @ToString.Exclude
    private String value;
}
