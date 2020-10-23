package com.wkqchen20.game.common.util;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
public class Reflections {

    private static org.reflections.Reflections reflections = new org.reflections.Reflections("com.wkqchen20.game");

    private Reflections() {}

    public static org.reflections.Reflections getReflections() {
        return reflections;
    }
}
