package com.wkqchen20.game.common.entity;

import java.util.Objects;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/15
 */
public class DisplayPlayer {

    private int id;
    private String nickname;

    public DisplayPlayer() {
    }

    public DisplayPlayer(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String display() {
        return String.format("%s(%s)", nickname, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayPlayer that = (DisplayPlayer) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
