package dev.vality.adapter.common.mapper;

import dev.vality.woody.api.flow.error.WRuntimeException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ErrorMappingTest {

    @Test
    public void errorMappingTest() {
        var error = new Error();
        error.setMapping("authorization_failed:unknown");
        error.setCodeRegex("00001");
        error.setDescriptionRegex("Invalid Merchant ID");
        var error1 = new Error();
        error1.setMapping("authorization_failed:provider_malfunction");
        error1.setCodeRegex("00002");
        error1.setDescriptionRegex("Invalid Merchant Name");
        ErrorMapping errorMapping = new ErrorMapping("'%s' - '%s'", List.of(error));
        assertThrows(WRuntimeException.class, () -> errorMapping.mapFailure("unknown"));
        assertEquals(
                "Failure(code:authorization_failed, reason:'00001' - 'null', sub:SubFailure(code:unknown))",
                errorMapping.mapFailure("00001").toString());
    }
}
