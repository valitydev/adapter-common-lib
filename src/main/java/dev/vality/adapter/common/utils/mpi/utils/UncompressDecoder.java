package dev.vality.adapter.common.utils.mpi.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.zip.Inflater;

@Slf4j
public class UncompressDecoder {

    private static final int START_OFFSET_IN_DATA = 0;

    public static String decodePaRes(String str) {
        return UncompressDecoder.decode(str);
    }

    public static String decode(String str) {
        byte[] decode = Base64.getDecoder().decode(str.getBytes());
        return new String(gzuncompress(decode));
    }

    private static byte[] gzuncompress(byte[] data) {
        byte[] unCompressed = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        Inflater deCompressor = new Inflater();
        try {
            deCompressor.setInput(data);
            final byte[] buf = new byte[1024];
            while (!deCompressor.finished()) {
                int count = deCompressor.inflate(buf);
                bos.write(buf, START_OFFSET_IN_DATA, count);
            }
            unCompressed = bos.toByteArray();
            bos.close();
        } catch (Exception e) {
            log.error("UnCompress Exception", e);
        } finally {
            deCompressor.end();
        }

        return unCompressed;
    }

}
