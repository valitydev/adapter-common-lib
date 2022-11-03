package dev.vality.adapter.common.secret;

import lombok.Data;
import lombok.ToString;

/**
 * Идентификатор секрета в vault
 * path - путь, по которому в vault хранятся секреты одного терминала. Хранится в options платежа.
 * key - ключ секрета в vault по указанному пути
 * Например, SecretRef{'tinkoff-merchant-882347345', 'PASSWORD'}
 */

@Data
@ToString
public class SecretRef {
    private String path;
    private String key;
}
