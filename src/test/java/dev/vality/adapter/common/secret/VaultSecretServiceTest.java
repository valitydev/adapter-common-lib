package dev.vality.adapter.common.secret;

import dev.vality.adapter.common.exception.HexDecodeException;
import dev.vality.adapter.common.exception.SecretNotFoundException;
import dev.vality.adapter.common.exception.SecretPathNotFoundException;
import dev.vality.adapter.common.utils.HmacEncryption;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.vault.VaultContainer;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class VaultSecretServiceTest {

    public static final String SIMPLE_KEY = "simpleKey";
    public static final String SIMPLE_SECRET = "sbdhfvh2y32bub";
    public static final String HMAC_KEY = "hmacKey";
    public static final String HMAC_SECRET = "6d6b6c6172657772";
    private static VaultSecretService vaultService;

    private static final String TEST_PATH = "test-terminal-123";

    @BeforeAll
    public static void setUp() {
        VaultContainer<?> container = new VaultContainer<>(DockerImageName.parse("vault:1.1.3"))
                .withVaultToken("my-root-token");
        container.start();
        VaultEndpoint vaultEndpoint = VaultEndpoint.create("localhost", container.getFirstMappedPort());
        vaultEndpoint.setScheme("http");
        VaultTemplate vaultTemplate = new VaultTemplate(vaultEndpoint, new TokenAuthentication("my-root-token"));
        vaultTemplate.opsForVersionedKeyValue("secret").put(TEST_PATH,
                Map.of(SIMPLE_KEY, SIMPLE_SECRET,
                HMAC_KEY, HMAC_SECRET
        ));
        vaultService = new VaultSecretService(vaultTemplate);

    }

    @Test
    public void testGetSecrets() {
        assertNotNull(vaultService.getSecrets(TEST_PATH));
        assertThrows(SecretPathNotFoundException.class, () -> vaultService.getSecrets("kekek"));
    }

    @Test
    public void testGetSecret() {
        assertEquals(SIMPLE_SECRET, vaultService.getSecret(new SecretRef(TEST_PATH, SIMPLE_KEY)).getValue());
        assertThrows(SecretNotFoundException.class, () -> vaultService.getSecret(new SecretRef(TEST_PATH, "der")));
    }

    @Test
    public void testHmac() {
        String expected = HmacEncryption.calculateHMacSha256("some_data", "6d6b6c6172657772");
        SecretRef hmacRef = new SecretRef(TEST_PATH, HMAC_KEY);
        String actual = vaultService.hmac("some_dat", hmacRef, HmacAlgorithms.HMAC_SHA_256);
        assertEquals(expected, actual);
        assertThrows(HexDecodeException.class,
                () -> vaultService.hmac("some_dat", new SecretRef(TEST_PATH,SIMPLE_KEY), HmacAlgorithms.HMAC_MD5));
    }

    @Test
    public void digest() {
        String expected = DigestUtils.md5Hex("some_da" + SIMPLE_SECRET);
        String actual = vaultService.digest("some_da", new SecretRef(TEST_PATH, SIMPLE_KEY), DigestAlgorithms.MD5);
        assertEquals(expected, actual);
    }
}