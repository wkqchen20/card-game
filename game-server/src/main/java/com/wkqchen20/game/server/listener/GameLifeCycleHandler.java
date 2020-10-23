package com.wkqchen20.game.server.listener;

import com.wkqchen20.game.common.Card;
import com.wkqchen20.game.core.Game;
import com.wkqchen20.game.core.Status;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/15
 */
public interface GameLifeCycleHandler<S extends Status, C extends Card, G extends Game<S, C>> {

    boolean isSupport(G game);

    void doStatusChanged(G game);
}
