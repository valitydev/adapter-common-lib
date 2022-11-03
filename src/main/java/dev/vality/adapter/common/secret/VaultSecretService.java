package dev.vality.adapter.common.secret;

import dev.vality.adapter.common.exception.HexDecodeException;
import dev.vality.adapter.common.exception.SecretNotFoundException;
import dev.vality.adapter.common.exception.SecretPathNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.vault.core.VaultTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VaultSecretService implements SecretService {

    public static final String VAULT_PATH = "VAULT_PATH";

    private final VaultTemplate vaultTemplate;
    private static final String SECRET_PATH = "/secret/data/";

    @Override
    public Map<String, SecretValue> getSecrets(String path) throws SecretPathNotFoundException {
        var map = vaultTemplate.read(SECRET_PATH + path);
        if (map == null || map.getData() == null) {
            throw new SecretPathNotFoundException(path);
        }
        return map.getData().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new SecretValue(e.getValue().toString())));
    }

    @Override
    public SecretValue getSecret(SecretRef secretRef) throws SecretNotFoundException {
        String secret = getSecretString(secretRef);
        return new SecretValue(secret);
    }

    @Override
    public String hmac(String data, SecretRef secretRef, HmacAlgorithms hmacAlgorithm)
            throws SecretNotFoundException, HexDecodeException {
        String hexSecret = getSecretString(secretRef);
        try {
            byte[] key = Hex.decodeHex(hexSecret);
            return new HmacUtils(hmacAlgorithm.getName(), key).hmacHex(data);
        } catch (DecoderException e) {
            throw new HexDecodeException(secretRef.toString());
        }
    }

    @Override
    public String digest(String data, SecretRef secretRef, DigestAlgorithms digestAlgorithm) throws SecretNotFoundException {
        String secret = getSecretString(secretRef);
        return switch (digestAlgorithm) {
            case MD5 -> DigestUtils.md5Hex(data + secret);
            case SHA256 -> DigestUtils.sha256Hex(data + secret);
        };
    }

    private String getSecretString(SecretRef secretRef) throws SecretNotFoundException {
        var map = vaultTemplate.read(SECRET_PATH + secretRef.getPath());
        if (map == null || map.getData() == null || map.getData().get(secretRef.getKey()) == null) {
            throw new SecretNotFoundException(secretRef.toString());
        }
        return map.getData().get(secretRef.getKey()).toString();
    }
}
