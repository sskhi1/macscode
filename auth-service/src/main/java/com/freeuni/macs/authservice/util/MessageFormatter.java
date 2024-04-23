package com.freeuni.macs.authservice.util;

import java.util.Arrays;

public class MessageFormatter {

    public static String format(String message, Object... args) {
        final StringBuilder stringBuilder = new StringBuilder(message);

        Arrays.stream(args).forEach(arg -> {
            int start = stringBuilder.indexOf("{}");
            stringBuilder.replace(start, start + 2, arg.toString());
        });

        return stringBuilder.toString();
    }
}
