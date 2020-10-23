package com.wkqchen20.game.server.listener;

import com.wkqchen20.game.landlords.common.LCard;
import com.wkqchen20.game.landlords.core.LGame;
import com.wkqchen20.game.landlords.core.LStatus;
import com.wkqchen20.game.server.Context;
import com.wkqchen20.game.server.flow.LandlordsGameContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.squirrelframework.foundation.fsm.StateMachine;

import static com.wkqchen20.game.common.command.ServerCommand.*;
import static com.wkqchen20.game.landlords.core.LStatus.*;
import static io.vavr.API.*;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/15
 */
public class LGameLiftCycleHandler implements GameLifeCycleHandler<LStatus, LCard, LGame> {

    private static final Logger logger = LoggerFactory.getLogger(LGameLiftCycleHandler.class);

    @Override
    public boolean isSupport(LGame game) {
        return game instanceof LGame;
    }

    @Override
    public void doStatusChanged(LGame game) {
        LStatus status = game.status();
        Match(status).of(
                Case($(ELECTION), () -> {
                    logger.info("landlords game:{} status is election", game.id());
                    StateMachine fsm = Context.newFlow(game);
                    fsm.fire(GAME_STATUS_INIT, LandlordsGameContext.of(game));
                    return null;
                }),
                Case($(PLAYING), () -> {
                    logger.info("landlords game:{} status is playing", game.id());
                    StateMachine fsm = Context.fromGame(game);
                    fsm.fire(GAME_STATUS_PLAYING, LandlordsGameContext.of(game));
                    return null;
                }),
                Case($(OVER), () -> {
                    logger.info("landlords game:{} status is over", game.id());
                    StateMachine fsm = Context.fromGame(game);
                    fsm.fire(GAME_STATUS_GAMEOVER, LandlordsGameContext.of(game));
                    return null;
                }),
                Case($(), () -> null)
        );
    }
}
