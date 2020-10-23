package com.wkqchen20.game.common.constant;

/**
 * @Author: wkqchen20
 * @Date: 2020/9/14
 */
public enum Errors {

    COMMAND_FLOW_ERROR("流程错误，还未加入房间"),

    COMMAND_NOT_FIND("命令未找到"),

    ROOM_PLAYER_FULL("满员了"),

    ROOM_NOT_FOUND("房间未找到"),

    GAME_TYPE_UNKNOWN("未知的游戏类型"),

    GAME_CAN_NOT_PASS_WHEN_STATUS_ERROR("游戏状态有误"),

    GAME_CAN_NOT_PASS_WHEN_NOT_SELL_CARDS("需出牌"),

    GAME_CAN_NOT_SELL_AGAIN("出牌有误"),

    GAME_CARD_CAN_NOT_COMPARE("出牌有误"),

    GAME_CARD_ILLEGAL("出牌有误"),

    GAME_NOT_YOUR_TURN("不是你"),



    ;

    public final String error;

    Errors(String error) {
        this.error = error;
    }
}
