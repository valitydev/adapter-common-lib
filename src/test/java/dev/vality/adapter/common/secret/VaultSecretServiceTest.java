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
import org.testcontainers.vault.VaultLogLevel;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class VaultSecretServiceTest {

    public static final String SIMPLE_KEY = "simpleKey";
    public static final String SIMPLE_SECRET = "sbdhfvh2y32bub";
    public static final String HMAC_KEY = "hmacKey";
    public static final String HMAC_SECRET = "6d6b6c6172657772";
    public static final String SERVICE_NAME = "adapter-vtb";
    public static final String TOKEN = "token";
    public static final String TOKEN_VALUE = "token-value";
    public static final String TOKEN_EXP_DATE = "token_exp_date";
    public static final String TOKEN_EXP_DATE_VALUE = "2023-04-20T12:26:17.191286";
    private static VaultSecretService vaultService;

    private static final String TEST_PATH = "test-terminal-123";

    @BeforeAll
    public static void setUp() throws IOException, InterruptedException {
        VaultContainer<?> container = new VaultContainer<>(DockerImageName.parse("vault:1.1.3"))
                .withLogLevel(VaultLogLevel.Trace)
                .withVaultToken("my-root-token");
        container.start();
        container.execInContainer("vault", "secrets", "enable", "-version=2", "-path=" + SERVICE_NAME, " kv");
        VaultEndpoint vaultEndpoint = VaultEndpoint.create("localhost", container.getFirstMappedPort());
        vaultEndpoint.setScheme("http");
        VaultTemplate vaultTemplate = new VaultTemplate(vaultEndpoint, new TokenAuthentication("my-root-token"));
        vaultTemplate.opsForVersionedKeyValue(SERVICE_NAME).put(TEST_PATH,
                Map.of(SIMPLE_KEY, SIMPLE_SECRET,
                        HMAC_KEY, HMAC_SECRET
                ));
        vaultService = new VaultSecretService(vaultTemplate);
    }

    @Test
    void testGetSecrets() {
        assertNotNull(vaultService.getSecrets(SERVICE_NAME, TEST_PATH));
        assertThrows(SecretPathNotFoundException.class, () -> vaultService.getSecrets(SERVICE_NAME, "kekek"));
    }

    @Test
    void testGetSecret() {
        assertEquals(SIMPLE_SECRET,
                vaultService.getSecret(SERVICE_NAME, new SecretRef(TEST_PATH, SIMPLE_KEY)).getValue());
        assertThrows(SecretNotFoundException.class,
                () -> vaultService.getSecret(SERVICE_NAME, new SecretRef(TEST_PATH, "der")));
    }

    @Test
    void testHmac() {
        String expected = HmacEncryption.calculateHMacSha256("some_dat", "6d6b6c6172657772");
        SecretRef hmacRef = new SecretRef(TEST_PATH, HMAC_KEY);
        String actual = vaultService.hmac(SERVICE_NAME, "some_dat", hmacRef, HmacAlgorithms.HMAC_SHA_256);
        assertEquals(expected, actual);
        assertThrows(HexDecodeException.class,
                () -> vaultService.hmac(SERVICE_NAME, "some_dat", new SecretRef(TEST_PATH, SIMPLE_KEY),
                        HmacAlgorithms.HMAC_MD5));
    }

    @Test
    void digest() {
        String expected = DigestUtils.md5Hex("some_da" + SIMPLE_SECRET);
        String actual = vaultService.digest(SERVICE_NAME, "some_da", new SecretRef(TEST_PATH, SIMPLE_KEY),
                DigestAlgorithms.MD5);
        assertEquals(expected, actual);
    }

    @Test
    void writeSingleSecret() {
        SecretObj secretObj = new SecretObj(TEST_PATH, Map.of(TOKEN, TOKEN_VALUE));
        vaultService.writeSecret(SERVICE_NAME, secretObj);

        SecretValue secret = vaultService.getSecret(SERVICE_NAME, new SecretRef(TEST_PATH, TOKEN));

        assertNotNull(secret);
        assertEquals(TOKEN_VALUE, secret.getValue());

    }

    @Test
    void writeMultipleSecret() {
        SecretObj secretObj = new SecretObj(
                TEST_PATH,
                Map.of(
                        TOKEN, TOKEN_VALUE,
                        TOKEN_EXP_DATE, TOKEN_EXP_DATE_VALUE
                )
        );
        vaultService.writeSecret(SERVICE_NAME, secretObj);

        Map<String, SecretValue> secret = vaultService.getSecrets(SERVICE_NAME, TEST_PATH);

        assertNotNull(secret);
        assertEquals(TOKEN_VALUE, secret.get(TOKEN).getValue());
        assertEquals(TOKEN_EXP_DATE_VALUE, secret.get(TOKEN_EXP_DATE).getValue());

    }
}