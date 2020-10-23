package com.wkqchen20.game.common.command;

/**
 * @Author: wkqchen20
 * @Date: 2020/9/12
 */
public interface ServerCommand {

    String NEW_PLAYER = "newPlayer";

    String NEW_EASY_ROBOT_PLAYER = "new1AiLevelRobotPlayer";
    String NEW_MEDIUM_ROBOT_PLAYER = "new2AiLevelRobotPlayer";
    String NEW_DIFFICULTY_ROBOT_PLAYER = "new3AiLevelRobotPlayer";

    String CHOOSE_GAME = "chooseGame";
    String CHOOSE_ROOM = "chooseRoom";
    String CREATE_ROOM = "createRoom";
    String LEAVE_ROOM = "leaveRoom";
    String BACK = "back";

    String NONE_BUSINESS = "NONE";

    String HEART_BEAT = "heartbeat";

    String GAME_STATUS_INIT = "gameInit";
    String GAME_STATUS_PLAYING = "gamePlay";
    String GAME_STATUS_GAMEOVER = "gameOver";

    static boolean isCommonType(String type) {
        return !type.contains(".");
    }
}
