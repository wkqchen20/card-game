package com.wkqchen20.game.landlords.core;

import com.wkqchen20.game.core.Status;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/9
 */
public enum LStatus implements Status {


    NOT_START,

    /**
     * 初始状态
     */
    INIT,

    /**
     * 选地主
     */
    ELECTION,

    PLAYING,

    OVER;



}
