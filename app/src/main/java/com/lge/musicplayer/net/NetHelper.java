package com.lge.musicplayer.net;

import java.util.HashMap;
import java.util.Map;

public class NetHelper {
    public static Map<String, String> getMusicApiCommonParams(String method) {
        Map params = new HashMap();
        params.put("format", "json");
        params.put("calback", "");
        params.put("from", "webapp_music");
        params.put("method", method);
        return params;
    }
}
