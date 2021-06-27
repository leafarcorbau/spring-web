package com.dh.sp.web.mvc.util;

import java.util.UUID;

import static java.lang.String.format;

public class TestUtil {

    public static String genField(final String field, final UUID seed){
        return format("%s-%s",field, seed.toString().substring(seed.toString().length() - 4));
    }

    public static String getBaseUrl(int port){
        return format("http://localhost:%d", port);
    }
}
