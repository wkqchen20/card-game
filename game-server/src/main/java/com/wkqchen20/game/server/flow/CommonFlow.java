package com.wkqchen20.game.server.flow;

import com.alibaba.fastjson.JSON;
import com.wkqchen20.game.common.TransferRequestResponseData.TransferRequestResponseDataProto;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.ServerCommand;
import com.wkqchen20.game.common.entity.DisplayPlayer;
import com.wkqchen20.game.common.entity.DisplayRoom;
import com.wkqchen20.game.common.entity.RoomDetail;
import com.wkqchen20.game.core.Game;
import com.wkqchen20.game.core.GameType;
import com.wkqchen20.game.core.Player;
import com.wkqchen20.game.core.Room;
import com.wkqchen20.game.server.Context;
import com.wkqchen20.game.server.entity.ClientInfo;
import com.wkqchen20.game.server.listener.GameListener;
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

import static java.util.stream.Collectors.toList;

/**
 * server记录了client的状态，client仅承担命令执行的角色
 * <pre>
 *
 *   SET_PLAYER_NICKNAME --> OPTION_GAMES <--> OPTION_ROOMS <--> NEW_ROOM
 *                                                  ⥮
 *                                              JOINED_ROOM
 * </pre>
 *
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
@States({
        @State(name = ClientCommand.SET_PLAYER_NICKNAME, exitCallMethod = "pushPlayerInfo"),
        @State(name = ClientCommand.NEW_ROOM, entryCallMethod = "roomInfo"),
        @State(name = ClientCommand.JOINED_ROOM, entryCallMethod = "roomInfo"),
})
@Transitions({
        @Transit(from = ClientCommand.SET_PLAYER_NICKNAME, to = ClientCommand.OPTION_GAMES, on = ServerCommand.NEW_PLAYER, callMethod = "optionGames"),
        @Transit(from = ClientCommand.OPTION_GAMES, to = ClientCommand.OPTION_ROOMS, on = ServerCommand.CHOOSE_GAME, callMethod = "optionRooms", when = SucceedCondition.class),
        @Transit(from = ClientCommand.OPTION_GAMES, to = ClientCommand.OPTION_GAMES, on = ServerCommand.CHOOSE_GAME, callMethod = "optionGames", when = ErrorCondition.class),
        @Transit(from = ClientCommand.OPTION_ROOMS, to = ClientCommand.NEW_ROOM, on = ServerCommand.CREATE_ROOM),
        @Transit(from = ClientCommand.OPTION_ROOMS, to = ClientCommand.JOINED_ROOM, on = ServerCommand.CHOOSE_ROOM, when = SucceedCondition.class),
        @Transit(from = ClientCommand.OPTION_ROOMS, to = ClientCommand.OPTION_ROOMS, on = ServerCommand.CHOOSE_ROOM, callMethod = "optionRooms", when = ErrorCondition.class),
        @Transit(from = ClientCommand.NEW_ROOM, to = ClientCommand.OPTION_ROOMS, on = ServerCommand.LEAVE_ROOM, callMethod = "optionRooms"),
        @Transit(from = ClientCommand.JOINED_ROOM, to = ClientCommand.OPTION_ROOMS, on = ServerCommand.LEAVE_ROOM, callMethod = "optionRooms"),
        @Transit(from = ClientCommand.NEW_ROOM, to = ClientCommand.OPTION_ROOMS, on = ServerCommand.BACK, callMethod = "optionRooms"),
        @Transit(from = ClientCommand.JOINED_ROOM, to = ClientCommand.OPTION_ROOMS, on = ServerCommand.BACK, callMethod = "optionRooms"),
        @Transit(from = ClientCommand.OPTION_ROOMS, to = ClientCommand.OPTION_GAMES, on = ServerCommand.BACK, callMethod = "optionGames"),
})
@StateMachineParameters(stateType = String.class, eventType = String.class, contextType = FlowContext.class)
public class CommonFlow extends AbstractUntypedStateMachine {
    private static final Logger logger = LoggerFactory.getLogger(CommonFlow.class);

    public static StateMachine commonFlow(String state) {
        UntypedStateMachineBuilder builder = StateMachineBuilderFactory.create(CommonFlow.class);
        UntypedStateMachine fsm = builder.newStateMachine(state);
        fsm.start();
        return fsm;
    }

    protected void optionGames(String from, String to, String event, FlowContext context) {
        logger.info("trigger push to client the games, from:{}, to:{}, event:{}, context:{}", from, to, event, context);
        List<String> gameTypes = GameType.GameTypes.gameTypes().stream().map(GameType::name).collect(toList());
        Channel channel = context.getChannel();
        channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.OPTION_GAMES).setData(JSON.toJSONString(gameTypes)).build());
    }

    protected void optionGames(String from, String to, String event, ConditionContext conditionContext) {
        FlowContext context = (FlowContext) conditionContext;
        optionGames(from, to, event, context);
    }

    protected void optionRooms(String from, String to, String event, ConditionContext conditionContext) {
        FlowContext context = (FlowContext) conditionContext;
        optionRooms(from, to, event, context);
    }

    protected void optionRooms(String from, String to, String event, FlowContext context) {
        logger.info("trigger push to client the rooms, from:{}, to:{}, event:{}, context:{}", from, to, event, context);
        ClientInfo clientInfo = Context.clientInfo(context.getPlayer());
        Channel channel = context.getChannel();
        GameType gameType = clientInfo.getGameType();
        List<com.wkqchen20.game.core.Room> rooms = Context.rooms(gameType);
        List<DisplayRoom> roomDataList = rooms.stream().filter(r -> !r.isEmpty()).map(r -> {
            DisplayRoom ret = new DisplayRoom();
            ret.setId(r.getId());
            ret.setName(r.getName());
            ret.setAllowPlayerCount(gameType.allowPlayerCount());
            ret.setCurrentPlayerCount(r.getPlayers().size());
            return ret;
        }).collect(toList());
        channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.OPTION_ROOMS).setData(JSON.toJSONString(roomDataList)).build());
    }

    protected void pushPlayerInfo(String from, String to, String event, FlowContext context) {
        Player player = context.getPlayer();
        context.getChannel().writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.MY_PLAYER_INFO).setData(JSON.toJSONString(new DisplayPlayer(player.getId(), player.getNickname()))).build());
    }

    protected void roomInfo(String from, String to, String event, FlowContext context) {
        Player player = context.getPlayer();
        Optional<Room> joinedRoom = player.getJoinedRoom();
        Room room = joinedRoom.get();
        List<Player> players = room.getPlayers();
        GameType gameType = Context.clientInfo(context.getPlayer()).getGameType();
        RoomDetail detail = new RoomDetail();
        detail.setId(room.getId());
        detail.setName(room.getName());
        detail.setAllowPlayerCount(gameType.allowPlayerCount());
        detail.setCurrentPlayerCount(room.getPlayers().size());
        detail.setPlayers(room.getPlayers().stream().map(p -> new DisplayPlayer(p.getId(), p.getNickname())).collect(toList()));
        String responseData = JSON.toJSONString(detail);
        for (Player roomPlayer : players) {
            Channel channel = Context.getChannel(roomPlayer);
            channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ClientCommand.JOINED_ROOM).setData(responseData).build());
        }
        // start game
        if (room.getPlayers().size() == gameType.allowPlayerCount()) {
            Game game = room.getGame();
            game.addListener(GameListener.LISTENER);
            room.startGame();
        }
    }
}
