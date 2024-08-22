package org.lb.utils;

import java.util.ArrayList;
import java.util.List;

public class BackendServers {
    private static final List<String> servers = new ArrayList<>();
    private static final List<String> ports = new ArrayList<>();
    private static  int count = 0;
    static {
        servers.add("localhost");
        servers.add("localhost");
        ports.add("8050");
        ports.add("8090");
    }

    public static String[] getHostAndPort(){
        String host = servers.get(count%servers.size());
        String port = ports.get(count%servers.size());
        count++;
        return new String[]{host, port};
    }
}
