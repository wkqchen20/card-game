package com.wkqchen20.game.core;

import com.wkqchen20.game.common.constant.Errors;


/**
 * @Author: wkqchen20
 * @Date: 2020/9/11
 */
public class ExecuteResult {

    private final boolean succeed;
    private Errors errors;
    private Object data;

    private ExecuteResult(boolean succeed) {
        this.succeed = succeed;
    }

    public static ExecuteResult success() {
        return new ExecuteResult(true);
    }

    public static ExecuteResult success(Object data) {
        ExecuteResult result = new ExecuteResult(true);
        result.data = data;
        return result;
    }

    public static ExecuteResult fail(Errors errors) {
        ExecuteResult result = new ExecuteResult(false);
        result.errors = errors;
        return result;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public Errors getErrors() {
        return errors;
    }

    public Object getData() {
        return data;
    }
}
