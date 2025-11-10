package dev.vality.adapter.common.secret;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * Объект с секретами
 * secretes - карта с ключами и соответсвующими им занчениями секретов.
 * version - версия хранилища секретов
 * Например,
 * VersionedSecret{
 * {'secret-password','PASSWORD'},
 * 42
 * }
 * @deprecated Use {@code vault-client}. This API will be removed in the next major release.
 */

@Deprecated(forRemoval = true)
@Data
@ToString
@AllArgsConstructor
public class VersionedSecret {

    private Map<String, SecretValue> secretes;
    private Integer version;
}
