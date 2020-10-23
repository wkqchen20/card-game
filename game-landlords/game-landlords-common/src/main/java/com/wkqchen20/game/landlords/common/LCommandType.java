package com.wkqchen20.game.landlords.common;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/9
 */
public interface LCommandType {

    String PASS = "landlords.pass";

    String SELL = "landlords.sell";

    String NOT_ELECTION = "landlords.notElection";

    String ELECTION = "landlords.election";

    static boolean isLandlordsType(String command) {
        return command.startsWith("landlords.");
    }

}
