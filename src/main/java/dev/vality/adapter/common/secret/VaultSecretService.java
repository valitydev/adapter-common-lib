package dev.vality.adapter.common.secret;

import dev.vality.adapter.common.exception.HexDecodeException;
import dev.vality.adapter.common.exception.SecretNotFoundException;
import dev.vality.adapter.common.exception.SecretPathNotFoundException;
import dev.vality.adapter.common.exception.SecretsNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.Versioned;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VaultSecretService implements SecretService {

    private final VaultTemplate vaultTemplate;

    @Override
    public Map<String, SecretValue> getSecrets(String serviceName, String path) throws SecretPathNotFoundException {
        var map = vaultTemplate.opsForVersionedKeyValue(serviceName).get(path);
        if (map == null || map.getData() == null) {
            throw new SecretPathNotFoundException(path);
        }
        return map.getData().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new SecretValue(e.getValue().toString())));
    }

    @Override
    public VersionedSecret getVersionSecrets(String serviceName, String path) throws SecretPathNotFoundException {
        var map = vaultTemplate.opsForVersionedKeyValue(serviceName).get(path);
        if (map == null || !map.hasData() || CollectionUtils.isEmpty(map.getData()) || isEmptyValues(map)) {
            throw new SecretsNotFoundException("Secrets doesn't exist or empty for path %s".formatted(path));
        }

        Map<String, SecretValue> secretes = map.getData().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new SecretValue(e.getValue().toString())));
        return new VersionedSecret(secretes, map.getVersion().getVersion());
    }

    private boolean isEmptyValues(Versioned<Map<String, Object>> map) {
        return map.getData().values().stream()
                .noneMatch(o -> StringUtils.hasText(o.toString()));
    }

    @Override
    public SecretValue getSecret(String serviceName, SecretRef secretRef) throws SecretNotFoundException {
        String secret = getSecretString(serviceName, secretRef);
        return new SecretValue(secret);
    }

    @Override
    public String hmac(String serviceName, String data, SecretRef secretRef, HmacAlgorithms hmacAlgorithm)
            throws SecretNotFoundException, HexDecodeException {
        String hexSecret = getSecretString(serviceName, secretRef);
        return new HmacSigner().sign(data, hexSecret, secretRef, hmacAlgorithm);
    }

    @Override
    public String digest(String serviceName, String data, SecretRef secretRef, DigestAlgorithms algorithm)
            throws SecretNotFoundException {
        String secret = getSecretString(serviceName, secretRef);
        return new DigestSigner().sign(data, secret, algorithm);
    }

    @Override
    public void writeSecret(String serviceName, SecretObj secretObj) {
        vaultTemplate.opsForVersionedKeyValue(serviceName).put(secretObj.getPath(), secretObj.getValues());
    }

    private String getSecretString(String serviceName, SecretRef secretRef) throws SecretNotFoundException {
        var map = vaultTemplate.opsForVersionedKeyValue(serviceName).get(secretRef.getPath());
        if (map == null || map.getData() == null || map.getData().get(secretRef.getKey()) == null) {
            throw new SecretNotFoundException(secretRef.toString());
        }
        return map.getData().get(secretRef.getKey()).toString();
    }
}
