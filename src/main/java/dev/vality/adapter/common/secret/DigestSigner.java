package dev.vality.adapter.common.secret;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @deprecated Use {@code vault-client}. This API will be removed in the next major release.
 */
@Deprecated(forRemoval = true)
class DigestSigner {
    public String sign(String data, String secret, DigestAlgorithms algorithm) {
        return switch (algorithm) {
            case MD5 -> DigestUtils.md5Hex(data + secret);
            case SHA256 -> DigestUtils.sha256Hex(data + secret);
        };
    }
}
