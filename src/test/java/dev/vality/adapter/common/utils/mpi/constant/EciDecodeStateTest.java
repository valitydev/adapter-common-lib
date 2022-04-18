package dev.vality.adapter.common.utils.mpi.constant;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class EciDecodeStateTest {

    @Test
    public void isEciDecodeEnabled() {
        Map<String, String> options = new HashMap<>();
        assertFalse("Default disabled", EciDecodeState.isEnabled(options));

        options.put(EciDecodeState.ECI_DECODE, EciDecodeState.ENABLED);
        assertTrue("Excepted enabled", EciDecodeState.isEnabled(options));

        options.put(EciDecodeState.ECI_DECODE, EciDecodeState.DISABLED);
        assertFalse("Excepted disabled", EciDecodeState.isEnabled(options));
    }
}