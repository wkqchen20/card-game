package com.wkqchen20.game.core;

import com.wkqchen20.game.core.util.IdGenerator;

/**
 * @Author: wkqchen20
 * @Date: 2020/9/10
 */
public class RobotPlayer extends Player {

    private static final String NAME_PREFIX = "Robot_";
    private final int aiLevel;

    private RobotPlayer(int id, int aiLevel) {
        super(id, NAME_PREFIX + id);
        this.aiLevel = aiLevel;
    }
    public static RobotPlayer easyRobot() { 
        return new RobotPlayer(IdGenerator.playerIdGenerate(), 1);
    }

    public static RobotPlayer mediumRobot() {
        return new RobotPlayer(IdGenerator.playerIdGenerate(), 2);
    }

    public static RobotPlayer difficultyRobot() {
        return new RobotPlayer(IdGenerator.playerIdGenerate(), 3);
    }
    
    public boolean isEasy() {
        return aiLevel == 1;
    }

    public boolean isMedium() {
        return aiLevel == 2;
    }

    public boolean isDifficulty() {
        return aiLevel == 3;
    }
}
