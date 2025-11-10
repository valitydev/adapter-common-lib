package dev.vality.adapter.common.secret;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @deprecated Use {@code vault-client}. This API will be removed in the next major release.
 */
@Deprecated(forRemoval = true)
@Getter
@RequiredArgsConstructor
public enum DigestAlgorithms {
    MD5("MD5"),
    SHA256("SHA-256");

    private final String name;
}
