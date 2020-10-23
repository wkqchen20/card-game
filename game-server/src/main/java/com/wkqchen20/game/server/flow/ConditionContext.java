package com.wkqchen20.game.server.flow;

import com.wkqchen20.game.common.constant.Errors;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/22
 */
public abstract class ConditionContext {
    private final Errors responseErr;

    protected ConditionContext(Errors responseErr) {
        this.responseErr = responseErr;
    }

    public boolean hasErrors() {
        return responseErr != null;
    }
}
