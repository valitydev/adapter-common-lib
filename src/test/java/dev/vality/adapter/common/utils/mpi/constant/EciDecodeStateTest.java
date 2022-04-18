package dev.vality.adapter.common.utils.mpi.constant;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EciDecodeStateTest {

    @Test
    public void isEciDecodeEnabled() {
        Map<String, String> options = new HashMap<>();
        assertFalse(EciDecodeState.isEnabled(options));

        options.put(EciDecodeState.ECI_DECODE, EciDecodeState.ENABLED);
        assertTrue(EciDecodeState.isEnabled(options));

        options.put(EciDecodeState.ECI_DECODE, EciDecodeState.DISABLED);
        assertFalse(EciDecodeState.isEnabled(options));
    }
}