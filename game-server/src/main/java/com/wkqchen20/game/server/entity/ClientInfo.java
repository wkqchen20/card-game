package com.wkqchen20.game.server.entity;

import com.wkqchen20.game.core.GameType;
import org.squirrelframework.foundation.fsm.StateMachine;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
public class ClientInfo {

    private StateMachine commonFsm;
    private GameType gameType;

    public StateMachine getCommonFsm() {
        return commonFsm;
    }

    public void setCommonFsm(StateMachine sm) {
        this.commonFsm = sm;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

}
