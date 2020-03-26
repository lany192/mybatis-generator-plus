package com.github.lany192.generator.utils;

public class Log {

    public static void i(String msg) {
        System.out.println(msg);
    }

    public static void i(String TAG, String msg) {
        System.out.println(TAG + ":" + msg);
    }

    public static void i(String TAG, int value) {
        System.out.println(TAG + ":" + value);
    }
}
