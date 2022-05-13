package dev.vality.adapter.common.cds;

import dev.vality.adapter.common.exception.CdsIDStorageException;
import dev.vality.damsel.identity_document_storage.IdentityDocument;
import dev.vality.damsel.identity_document_storage.IdentityDocumentStorageSrv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

@Slf4j
@RequiredArgsConstructor
public class CdsClientIDStorage {

    private final IdentityDocumentStorageSrv.Iface cdsIDStorageApi;

    public IdentityDocument get(String token) {
        log.info("Get Identity Document by token: {}", token);
        try {
            return cdsIDStorageApi.get(token);
        } catch (TException ex) {
            throw new CdsIDStorageException(
                    String.format("Failed to get identity document from cds storage with token: %s", token), ex);
        }
    }

}
