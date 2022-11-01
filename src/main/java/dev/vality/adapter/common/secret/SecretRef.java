package dev.vality.adapter.common.secret;

import lombok.Data;

import java.util.Map;

/**
 * Идентификатор секрета в vault
 * optionsId - это уникальный идентификатор опций/терминаола, или path в терминах vault
 * key - ключ секрета в vault по указанному пути
 * Например, SecretRef{'tinkoff-merchant-882347345', 'PASSWORD'}
 */

@Data
public class SecretRef {
    private String optionsId;
    private String key;

    /**
     * Создает инстанс по опциям платежа/выплаты и ключ секрета в vault
     * @param options Опции платежа
     * @param key Ключ
     */
    public SecretRef(Map<String, String> options, String key) {
        this.optionsId = options.get(SecretService.OPTIONS_ID);
        this.key = key;
    }
}
