package dev.vality.adapter.common.utils.mpi.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EciDecodeState {

    public static final String ECI_DECODE = "eci_decode_state";
    public static final String ENABLED = "true";
    public static final String DISABLED = "false";

    public static boolean isEnabled(Map<String, String> options) {
        String eciDecode = options.getOrDefault(EciDecodeState.ECI_DECODE, EciDecodeState.DISABLED);
        return EciDecodeState.ENABLED.equalsIgnoreCase(eciDecode);
    }

}
