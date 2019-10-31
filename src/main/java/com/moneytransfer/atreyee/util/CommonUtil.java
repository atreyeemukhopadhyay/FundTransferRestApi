package com.moneytransfer.atreyee.util;

import javax.ws.rs.core.UriBuilder;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

public class CommonUtil {

    public static URI getURI() {
        return UriBuilder.fromUri("http://" + getHostName() + "/").port(Integer.parseInt(ConfigProperties.getProps().getProperty("server.port"))).build();
    }

    public static String getHostName() {
        String hostName = "localhost";
        try {
            hostName = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostName;
    }
}
