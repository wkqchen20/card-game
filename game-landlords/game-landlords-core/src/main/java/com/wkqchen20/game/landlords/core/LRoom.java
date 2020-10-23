package com.wkqchen20.game.landlords.core;

import com.wkqchen20.game.core.GameType;
import com.wkqchen20.game.core.HumanPlayer;
import com.wkqchen20.game.core.Room;
import com.wkqchen20.game.core.util.IdGenerator;

/**
 *
 * @Author: wkqchen20
 * @Date: 2020/9/10
 */
public class LRoom extends Room {

    private LGame game;

    private LRoom(int id, String name, HumanPlayer createdPlayer) {
        super(id, name, createdPlayer);
    }

    public static LRoom create(String name, HumanPlayer createPlayer, GameType gameType) {
        LRoom room = new LRoom(IdGenerator.roomIdGenerate(), name, createPlayer);
        room.game = new LGame(room, gameType);
        return room;
    }

    @Override
    public LGame getGame() {
        return game;
    }

}
