package com.wkqchen20.game.server.flow;

import org.squirrelframework.foundation.fsm.Condition;

public class ErrorCondition implements Condition<ConditionContext> {

    @Override
    public boolean isSatisfied(ConditionContext context) {
        return context.hasErrors();
    }

    @Override
    public String name() {
        return "ErrorCondition";
    }
}