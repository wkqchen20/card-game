package com.wkqchen20.game.server.flow;

import com.wkqchen20.game.common.constant.Errors;
import com.wkqchen20.game.core.Player;
import com.wkqchen20.game.landlords.core.LGame;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/15
 */
public class LandlordsGameContext extends ConditionContext {

    private LGame game;
    private Player player;

    protected LandlordsGameContext(Errors responseErr, LGame game, Player player) {
        super(responseErr);
        this.game = game;
        this.player = player;
    }

    public static LandlordsGameContext of(LGame game) {
        return new LandlordsGameContext(null, game, null);
    }


    public static LandlordsGameContext of(LGame game, Player player) {
        return new LandlordsGameContext(null, game, player);
    }

    public static LandlordsGameContext failed(Errors responseErr, LGame game, Player player) {
        return new LandlordsGameContext(responseErr, game, player);
    }

    public LGame getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }
}
