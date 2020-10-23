package com.wkqchen20.game.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
public final class Inputs {

    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private Inputs() {}

    public static String input() {
        try {
            return reader.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
