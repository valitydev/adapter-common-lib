package dev.vality.adapter.common.utils.damsel.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpiState {

    public static final String MPI_STATE = "mpi_state";
    public static final String ENABLE = "true";
    public static final String DISABLE = "false";

    public static boolean isMpiEnabled(Map<String, String> options) {
        String mpiState = options.getOrDefault(MpiState.MPI_STATE, MpiState.ENABLE);
        return MpiState.ENABLE.equalsIgnoreCase(mpiState);
    }
}
