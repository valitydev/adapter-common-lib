package dev.vality.adapter.common.secret;

import dev.vality.adapter.common.exception.HexDecodeException;
import dev.vality.adapter.common.exception.SecretNotFoundException;
import dev.vality.adapter.common.exception.SecretPathNotFoundException;

import java.util.Map;

public interface SecretService {

    /**
     * Возвращает все секреты по заданному пути (по смыслу терминала)
     *
     * @param serviceName - имя сервиса, которому принадлежат секреты. Хранится в настройках сервиса.
     * @param path        - путь, по которому в vault хранятся секреты одного терминала. Хранится в options платежа.
     * @return - kv всех секретов этого терминала, например {'TERMINAL_ID':'user11', 'PASSWORD':'Parolec1'}
     * @throws SecretPathNotFoundException если путь не найден
     */
    Map<String, SecretValue> getSecrets(String serviceName, String path) throws SecretPathNotFoundException;

    /**
     * Возвращает конкретный секрет из kv
     *
     * @param serviceName - имя сервиса, которому принадлежат секреты. Хранится в настройках сервиса.
     * @param secretRef   Идентификатор секрета, например, SecretRef{'882347345', 'PASSWORD'}
     * @return Возвращает секрет
     * @throws SecretNotFoundException если секрет не найден
     */
    SecretValue getSecret(String serviceName, SecretRef secretRef) throws SecretNotFoundException;

    /**
     * Возвращает hex-encoded hmac-подпись data. Секрет должен быть hex-encoded.
     *
     * @param serviceName   - имя сервиса, которому принадлежат секреты. Хранится в настройках сервиса.
     * @param data          Данные для подписи, например, invoiceId=123&amount=222
     * @param secretRef     Идентификатор секрета, например, SecretRef{'882347345', 'PASSWORD'}
     * @param hmacAlgorithm Алгоритм подписи, например, HmacSHA256
     * @return Возвращает подпись
     * @throws SecretNotFoundException если секрет не найден
     * @throws HexDecodeException      если секрет не в hex-формате
     */
    String hmac(String serviceName, String data, SecretRef secretRef, HmacAlgorithms hmacAlgorithm)
            throws SecretNotFoundException, HexDecodeException;

    /**
     * Возвращает hex-encoded хэш от data + secret
     *
     * @param serviceName     - имя сервиса, которому принадлежат секреты. Хранится в настройках сервиса.
     * @param data            Данные для подписи, например, invoiceId=123&amount=222
     * @param secretRef       Идентификатор секрета, например, SecretRef{'882347345', 'PASSWORD'}
     * @param digestAlgorithm Алгоритм хэширования, например, MD5
     * @return Возвращает подпись
     * @throws SecretNotFoundException если секрет не найден
     */
    String digest(String serviceName, String data, SecretRef secretRef, DigestAlgorithms digestAlgorithm)
            throws SecretNotFoundException;

}
