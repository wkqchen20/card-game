package com.wkqchen20.game.common.command;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/13
 */
public interface ClientCommand {

    String CONNECTED = "connected";

    String SET_PLAYER_NICKNAME = "setNickname";
    String MY_PLAYER_INFO = "myInfo";

    String OPTION_GAMES = "optionGames";

    String OPTION_ROOMS = "optionRooms";

    String NEW_ROOM = "newRoom";

    String JOINED_ROOM = "joinedRoom";

    String ILLEGAL = "illegal";

    String SHOW_MESSAGE = "showMessage";


    String LANDLORDS_SHOW_CARDS = "landlords.showCards";
    String LANDLORDS_SHOW_SELL_CARDS = "landlords.showSellCards";
    String LANDLORDS_SHOW_PASS = "landlords.showPass";
    String LANDLORDS_OPTION_IS_LANDLORDS = "landlords.isLandlords";
    String LANDLORDS_OPTION_ELECT = "landlords.optionElect";
    String LANDLORDS_STATUS_ELECT = "landlords.elect";
    String LANDLORDS_OPTION_PLAY = "landlords.optionPlay";
    String LANDLORDS_STATUS_PLAY = "landlords.play";
    String LANDLORDS_GAME_OVER = "landlords.gameOver";

}
