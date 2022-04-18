package dev.vality.adapter.common.utils.mpi.utils;

import dev.vality.adapter.common.utils.mpi.exception.ThreeDSecureException;
import dev.vality.adapter.common.utils.mpi.model.Message;
import dev.vality.adapter.common.utils.mpi.model.PaRes;
import dev.vality.adapter.common.utils.mpi.model.TX;
import dev.vality.adapter.common.utils.mpi.model.ThreeDSecure;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.StringReader;
import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreeDSecureUtils {

    private static JAXBContext jaxbContext;

    static {
        try {
            jaxbContext = JAXBContext.newInstance(ThreeDSecure.class);
        } catch (JAXBException ex) {
            log.error("Failed to create jaxb context", ex);
            throw new RuntimeException("Failed to create jaxb context", ex);
        }
    }

    public static ThreeDSecure extractThreeDSecure(String str) {
        try {
            Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(str);
            return (ThreeDSecure) jaxbMarshaller.unmarshal(reader);
        } catch (JAXBException ex) {
            log.error("Can't extract ThreeDSecure", ex);
            throw new ThreeDSecureException(ex);
        }
    }

    public static String extractEciFromPaRes(ThreeDSecure threeDSecure) {
        return Optional.ofNullable(threeDSecure)
                .map(ThreeDSecure::getMessage)
                .map(Message::getPaRes)
                .map(PaRes::getTx)
                .map(TX::getEci).orElse("");
    }

}
