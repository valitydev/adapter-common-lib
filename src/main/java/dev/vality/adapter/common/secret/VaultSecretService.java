package dev.vality.adapter.common.secret;

import dev.vality.adapter.common.exception.HexDecodeException;
import dev.vality.adapter.common.exception.SecretNotFoundException;
import dev.vality.adapter.common.exception.SecretPathNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.vault.core.VaultTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VaultSecretService implements SecretService {

    public static final String VAULT_PATH = "VAULT_PATH";
    private static final String SECRET_PATH = "secret";

    private final VaultTemplate vaultTemplate;

    @Override
    public Map<String, SecretValue> getSecrets(String path) throws SecretPathNotFoundException {
        var map = vaultTemplate.opsForVersionedKeyValue(SECRET_PATH).get(path);
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
        return new HmacSigner().sign(data, hexSecret, secretRef, hmacAlgorithm);
    }

    @Override
    public String digest(String data, SecretRef secretRef, DigestAlgorithms algorithm) throws SecretNotFoundException {
        String secret = getSecretString(secretRef);
        return new DigestSigner().sign(data, secret, algorithm);
    }

    private String getSecretString(SecretRef secretRef) throws SecretNotFoundException {
        var map = vaultTemplate.opsForVersionedKeyValue(SECRET_PATH).get(secretRef.getPath());
        if (map == null || map.getData() == null || map.getData().get(secretRef.getKey()) == null) {
            throw new SecretNotFoundException(secretRef.toString());
        }
        return map.getData().get(secretRef.getKey()).toString();
    }
}
