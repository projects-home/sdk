package com.x.sdk.util;

/**
 * Created by mayt on 2018/1/24.
 */
public abstract class Assert {

    protected Assert() {
    }

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        } else if (obj instanceof String && StringUtil.isBlank(obj.toString())) {
            throw new IllegalArgumentException(message);
        }
    }

}
