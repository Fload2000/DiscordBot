package de.fload.util;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TIME {
    @NotNull
    public static String getTime() {
        Date now = new Date();
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
        return sdf.format(now);
    }

    @NotNull
    public static String getTimeSeconds() {
        Date now = new Date();
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
        return sdf.format(now);
    }

    @NotNull
    public static String getDate() {
        Date now = new Date();
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(now);
    }

    @NotNull
    public static String getTimestamp() {
        return "[" + getDate() + "][" + getTimeSeconds() + "]";
    }

    @NotNull
    public static String getFullTimestamp() {
        return getDate() + " " + getTimeSeconds();
    }
}
