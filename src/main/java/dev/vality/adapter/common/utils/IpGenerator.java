package dev.vality.adapter.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IpGenerator {

    public static final String DEFAULT_IP_ADDRESS = "127.0.0.1";

    public static String checkAndGenerate(String ip) {
        return checkAndGenerate(ip, DEFAULT_IP_ADDRESS);
    }

    public static String checkAndGenerate(String ip, String defaultIpAddress) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            if (StringUtils.isEmpty(ip) || address instanceof Inet6Address) {
                return defaultIpAddress;
            }
        } catch (UnknownHostException e) {
            log.error("Error when convert ipAddress: {}", ip, e);
            return defaultIpAddress;
        }
        return ip;
    }

}
