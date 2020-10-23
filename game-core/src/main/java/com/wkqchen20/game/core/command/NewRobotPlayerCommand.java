package com.wkqchen20.game.core.command;

import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.constant.Errors;
import com.wkqchen20.game.core.*;

import java.util.Objects;

import static com.wkqchen20.game.common.command.ServerCommand.*;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/9
 */
@CommandTag(value = {NEW_EASY_ROBOT_PLAYER, NEW_MEDIUM_ROBOT_PLAYER, NEW_DIFFICULTY_ROBOT_PLAYER})
public class NewRobotPlayerCommand implements Command {

    @Override
    public ExecuteResult execute(Player player, String data) {
        String value = data;
        if (Objects.equals(value, NEW_EASY_ROBOT_PLAYER)) {
            return ExecuteResult.success(RobotPlayer.easyRobot());
        }
        if (Objects.equals(value, NEW_MEDIUM_ROBOT_PLAYER)) {
            return ExecuteResult.success(RobotPlayer.mediumRobot());
        }
        if (Objects.equals(value, NEW_DIFFICULTY_ROBOT_PLAYER)) {
            return ExecuteResult.success(RobotPlayer.difficultyRobot());
        }
        return ExecuteResult.fail(Errors.COMMAND_NOT_FIND);
    }
}
