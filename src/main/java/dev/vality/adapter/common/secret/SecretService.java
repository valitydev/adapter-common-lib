package dev.vality.adapter.common.secret;

import dev.vality.adapter.common.exception.NotFoundException;

import java.util.Map;

public interface SecretService {

    /**
     * Ключ в options платежа/выплаты
     * В options.get("OPTIONS_ID") должен лежать уникальный идентификатор,
     * заведенный в vault для этого терминала
     * Например, maguapay-terminal-15564367 или tinkoff-merchant-882347345
     */
    String OPTIONS_ID = "OPTIONS_ID";

    /** Возвращает все секреты заданного optionsId (по смыслу терминала)
     * @param optionsId - Значение из options.get("OPTIONS_ID")
     * @return - kv всех секретов этого терминала, например {'TERMINAL_ID':'user11', 'PASSWORD':'Parolec1'}
     * @throws NotFoundException если ключ не найден
     */
    Map<String, SecretValue> getSecrets(String optionsId) throws NotFoundException;

    /**
     * Возвращает конкретный секрет из kv
     * @param secretRef Идентификатор секрета, например, SecretRef{'tinkoff-merchant-882347345', 'PASSWORD'}
     * @return Возвращает секрет
     * @throws NotFoundException если ключ не найден
     */
    SecretValue getSecret(SecretRef secretRef) throws NotFoundException;

    /**
     * Возвращает hex-encoded hmac-подпись data
     * @param data Данные для подписи, например, invoiceId=123&amount=222
     * @param secretRef Идентификатор секрета, например, SecretRef{'tinkoff-merchant-882347345', 'PASSWORD'}
     * @param hmacAlgorithm Алгоритм подписи, например, HmacSHA256
     * @return Возвращает подпись
     * @throws NotFoundException если ключ не найден
     */
    String hmac(String data, SecretRef secretRef, HmacAlgorithms hmacAlgorithm) throws NotFoundException;

    /**
     * Возвращает hex-encoded хэш от data + secret
     * @param data Данные для подписи, например, invoiceId=123&amount=222
     * @param secretRef Идентификатор секрета, например, SecretRef{'tinkoff-merchant-882347345', 'PASSWORD'}
     * @param digestAlgorithm Алгоритм хэширования, например, MD5
     * @return Возвращает подпись
     * @throws NotFoundException если ключ не найден
     */
    String digest(String data, SecretRef secretRef, DigestAlgorithms digestAlgorithm) throws NotFoundException;

}
