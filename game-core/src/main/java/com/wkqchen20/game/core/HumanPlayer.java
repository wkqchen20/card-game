package com.wkqchen20.game.core;

import com.wkqchen20.game.core.util.IdGenerator;

/**
 * @Author: wkqchen20
 * @Date: 2020/9/10
 */
public class HumanPlayer extends Player {

    public HumanPlayer(String nickname) {
        super(IdGenerator.playerIdGenerate(), nickname);
    }

}
