package com.wkqchen20.game.common.command;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommandTag {

    /**
     * 命令
     * @return
     */
    String[] value();
}
