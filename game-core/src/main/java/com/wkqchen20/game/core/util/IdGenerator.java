package com.wkqchen20.game.core.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: wkqchen20
 * @Date: 2020/9/10
 */
public class IdGenerator {

    private static final AtomicInteger ROOM_ID = new AtomicInteger(0);
    private static final AtomicInteger PLAYER_ID = new AtomicInteger(0);
    private static final AtomicInteger GAME_ID = new AtomicInteger(0);

    private IdGenerator() {}

    public static Integer roomIdGenerate() {
        return ROOM_ID.incrementAndGet();
    }

    public static Integer playerIdGenerate() {
        return PLAYER_ID.incrementAndGet();
    }

    public static Integer gameIdGenerate() {
        return GAME_ID.incrementAndGet();
    }
}
