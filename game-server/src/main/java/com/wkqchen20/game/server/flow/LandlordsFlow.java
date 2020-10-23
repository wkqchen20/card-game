package com.wkqchen20.game.server.flow;

import com.alibaba.fastjson.JSON;
import com.wkqchen20.game.common.TransferRequestResponseData.TransferRequestResponseDataProto;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.ServerCommand;
import com.wkqchen20.game.common.entity.DisplayPlayer;
import com.wkqchen20.game.core.Player;
import com.wkqchen20.game.core.PlayerWithCards;
import com.wkqchen20.game.landlords.common.LCard;
import com.wkqchen20.game.landlords.common.LCommandType;
import com.wkqchen20.game.landlords.common.entity.PlayerWithPass;
import com.wkqchen20.game.landlords.common.entity.PlayerWithSellCards;
import com.wkqchen20.game.landlords.core.LGame;
import com.wkqchen20.game.landlords.core.LPlayerBehavior;
import com.wkqchen20.game.landlords.core.LStatus;
import com.wkqchen20.game.server.Context;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.squirrelframework.foundation.fsm.StateMachine;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.UntypedStateMachine;
import org.squirrelframework.foundation.fsm.UntypedStateMachineBuilder;
import org.squirrelframework.foundation.fsm.annotation.*;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

import java.util.List;
import java.util.Optional;


/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
@States({
        @State(name = ClientCommand.LANDLORDS_SHOW_CARDS, entryCallMethod = "elect"),
})
@Transitions({
        @Transit(from = LandlordsFlow.INIT_STATUS, to = ClientCommand.LANDLORDS_SHOW_CARDS, on = ServerCommand.GAME_STATUS_INIT, callMethod = "showPlayersCards"),
        @Transit(from = ClientCommand.LANDLORDS_SHOW_CARDS, to = ClientCommand.LANDLORDS_STATUS_ELECT, on = LCommandType.NOT_ELECTION, callMethod = "ifElect"),
        @Transit(from = ClientCommand.LANDLORDS_SHOW_CARDS, to = ClientCommand.LANDLORDS_STATUS_ELECT, on = LCommandType.ELECTION, callMethod = "ifElect"),
        @Transit(from = ClientCommand.LANDLORDS_STATUS_ELECT, to = ClientCommand.LANDLORDS_STATUS_ELECT, on = LCommandType.NOT_ELECTION, callMethod = "ifElect"),
        @Transit(from = ClientCommand.LANDLORDS_STATUS_ELECT, to = ClientCommand.LANDLORDS_STATUS_ELECT, on = LCommandType.ELECTION, callMethod = "ifElect"),
        @Transit(from = ClientCommand.LANDLORDS_SHOW_CARDS, to = ClientCommand.LANDLORDS_STATUS_PLAY, on = ServerCommand.GAME_STATUS_PLAYING, callMethod = "optionAndShowLandlordsCards"),
        @Transit(from = ClientCommand.LANDLORDS_STATUS_ELECT, to = ClientCommand.LANDLORDS_STATUS_PLAY, on = ServerCommand.GAME_STATUS_PLAYING, callMethod = "optionAndShowLandlordsCards"),
        @Transit(from = ClientCommand.LANDLORDS_STATUS_PLAY, to = ClientCommand.LANDLORDS_STATUS_PLAY, on = LCommandType.SELL, callMethod = "sellCardsSucceedAndShowPlayersSellCards", when = SucceedCondition.class),
        @Transit(from = ClientCommand.LANDLORDS_STATUS_PLAY, to = ClientCommand.LANDLORDS_STATUS_PLAY, on = LCommandType.SELL, callMethod = "sellCardsFailedAndShowPlayPlayerCards", when = ErrorCondition.class),
        @Transit(from = ClientCommand.LANDLORDS_STATUS_PLAY, to = ClientCommand.LANDLORDS_STATUS_PLAY, on = LCommandType.PASS, callMethod = "passSucceed", when = SucceedCondition.class),
        @Transit(from = ClientCommand.LANDLORDS_STATUS_PLAY, to = ClientCommand.LANDLORDS_STATUS_PLAY, on = LCommandType.PASS, callMethod = "passFailed", when = ErrorCondition.class),
        @Transit(from = ClientCommand.LANDLORDS_STATUS_PLAY, to = ClientCommand.LANDLORDS_GAME_OVER, on = ServerCommand.GAME_STATUS_GAMEOVER, callMethod = "gameOver"),
})
@StateMachineParameters(stateType = String.class, eventType = String.class, contextType = LandlordsGameContext.class)
public class LandlordsFlow extends AbstractUntypedStateMachine {
    private static final Logger logger = LoggerFactory.getLogger(LandlordsFlow.class);
    static final String INIT_STATUS = "init";

    public static StateMachine landlordsFlow() {
        UntypedStateMachineBuilder builder = StateMachineBuilderFactory.create(LandlordsFlow.class);
        UntypedStateMachine fsm = builder.newStateMachine(INIT_STATUS);
        fsm.start();
        return fsm;
    }

    protected void showPlayersCards(String from, String to, String event, LandlordsGameContext context) {
        logger.info("trigger push to all players the games, from:{}, to:{}, event:{}, context:{}", from, to, event, context);
        LGame game = context.getGame();
        List<PlayerWithCards<LCard>> playerWithCards = game.playerWithCards();
        for (PlayerWithCards<LCard> pc : playerWithCards) {
            Player player = pc.getPlayer();
            Channel channel = Context.getChannel(player);
            channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_SHOW_CARDS).setData(JSON.toJSONString(pc.getCards())).build());
        }
    }

    protected void ifElect(String from, String to, String event, LandlordsGameContext context) {
        logger.info("trigger ifElect, from:{}, to:{}, event:{}, context:{}", from, to, event, context);
        LGame game = context.getGame();
        if (game.status() == LStatus.ELECTION) {
            elect(from, to, event, context);
            return;
        }
    }

    protected void optionAndShowLandlordsCards(String from, String to, String event, LandlordsGameContext context) {
        logger.info("trigger push to landlords player cards, from:{}, to:{}, event:{}, context:{}", from, to, event, context);
        LGame game = context.getGame();
        Player landlordsPlayer = game.getLandlordPlayer();
        for (PlayerWithCards<LCard> pc : game.playerWithCards()) {
            Player player = pc.getPlayer();
            Channel channel = Context.getChannel(player);
            channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_OPTION_IS_LANDLORDS).setData(JSON.toJSONString(new DisplayPlayer(landlordsPlayer.getId(), landlordsPlayer.getNickname()))).build());
            if (player.equals(landlordsPlayer)) {
                channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_SHOW_CARDS).setData(JSON.toJSONString(pc.getCards())).build());
                channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_OPTION_PLAY).build());
            }
        }
    }

    protected void sellCardsSucceedAndShowPlayersSellCards(String from, String to, String event, LandlordsGameContext context) {
        logger.info("trigger sellCardsSucceedAndShowPlayersSellCards, from:{}, to:{}, event:{}, context:{}", from, to, event, context);
        LGame game = context.getGame();
        if (game.status() == LStatus.OVER) {
            return;
        }
        Player player = context.getPlayer();
        LPlayerBehavior playerBehavior = game.playerBehaviors().get(game.playerBehaviors().size() - 1);
        List<LCard> sellCards = playerBehavior.getData().getCards();
        PlayerWithSellCards psc = new PlayerWithSellCards();
        psc.setCards(sellCards);
        psc.setPlayer(new DisplayPlayer(player.getId(), player.getNickname()));
        String sellCardData = JSON.toJSONString(psc);
        for (PlayerWithCards<LCard> pc : game.playerWithCards()) {
            Player tmpPlayer = pc.getPlayer();
            Channel channel = Context.getChannel(tmpPlayer);
            if (tmpPlayer.equals(player)) {
                channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_SHOW_CARDS).setData(JSON.toJSONString(pc.getCards())).build());
            } else {
                channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_SHOW_SELL_CARDS).setData(sellCardData).build());
                if (tmpPlayer.equals(game.currentPlayerToPlay())) {
                    channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_OPTION_PLAY).build());
                }
            }
        }
    }

    protected void sellCardsFailedAndShowPlayPlayerCards(String from, String to, String event, LandlordsGameContext context) {
        logger.info("trigger sellCardsFailedAndShowPlayPlayerCards, from:{}, to:{}, event:{}, context:{}", from, to, event, context);
        Player player = context.getPlayer();
        Channel channel = Context.getChannel(player);
        Optional<List<LCard>> optional = context.getGame().playerWithCards().stream().filter(pc -> pc.getPlayer().equals(player)).map(PlayerWithCards::getCards).findFirst();
        channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_SHOW_CARDS).setData(JSON.toJSONString(optional.get())).build());
        channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_OPTION_PLAY).build());
    }

    protected void passSucceed(String from, String to, String event, LandlordsGameContext context) {
        logger.info("trigger passSucceed, from:{}, to:{}, event:{}, context:{}", from, to, event, context);
        Player player = context.getPlayer();
        LGame game = context.getGame();
        DisplayPlayer dp = new DisplayPlayer(player.getId(), player.getNickname());
        PlayerWithPass pp = new PlayerWithPass();
        pp.setPlayer(dp);
        for (PlayerWithCards<LCard> pc : game.playerWithCards()) {
            Player tmpPlayer = pc.getPlayer();
            if (tmpPlayer.equals(player)) {
                continue;
            }
            Channel channel = Context.getChannel(tmpPlayer);
            channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_SHOW_PASS).setData(JSON.toJSONString(pp)).build());
            if (tmpPlayer.equals(game.currentPlayerToPlay())) {
                channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_OPTION_PLAY).build());
            }
        }
    }

    protected void passFailed(String from, String to, String event, LandlordsGameContext context) {
        logger.info("trigger sellCardsFailedAndShowPlayPlayerCards, from:{}, to:{}, event:{}, context:{}", from, to, event, context);
        Player player = context.getPlayer();
        Channel channel = Context.getChannel(player);
        Optional<List<LCard>> optional = context.getGame().playerWithCards().stream().filter(pc -> pc.getPlayer().equals(player)).map(PlayerWithCards::getCards).findFirst();
        channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_SHOW_CARDS).setData(JSON.toJSONString(optional.get())).build());
        channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_OPTION_PLAY).build());
    }

    protected void gameOver(String from, String to, String event, LandlordsGameContext context) {
        logger.info("trigger push to player game over, from:{}, to:{}, event:{}, context:{}", from, to, event, context);
        LGame game = context.getGame();
        List<PlayerWithCards<LCard>> playerWithCards = game.playerWithCards();
        Player player = game.currentPlayerToPlay();
        Player landlordPlayer = game.getLandlordPlayer();
        boolean isLandlordsWin = player.equals(landlordPlayer);
        // 农民
        playerWithCards.stream().filter(pc -> !pc.getPlayer().equals(landlordPlayer)).forEach(pc -> {
            Channel channel = Context.getChannel(pc.getPlayer());
            if (isLandlordsWin) {
                channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_GAME_OVER).setData("lose").build());
            } else {
                channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_GAME_OVER).setData("win").build());
            }
        });
        // 地主
        Channel channel = Context.getChannel(landlordPlayer);
        if (isLandlordsWin) {
            channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_GAME_OVER).setData("win").build());
        } else {
            channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_GAME_OVER).setData("lose").build());
        }
    }

    protected void elect(String from, String to, String event, LandlordsGameContext context) {
        LGame game = context.getGame();
        Player player = game.currentPlayerToPlay();
        Channel channel = Context.getChannel(player);
        channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.LANDLORDS_OPTION_ELECT).build());
    }


}
