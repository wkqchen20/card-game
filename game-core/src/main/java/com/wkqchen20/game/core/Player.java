package com.wkqchen20.game.core;

import com.wkqchen20.game.common.constant.Errors;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.Optional;

/**
 * @Author: wkqchen20
 * @Date: 2020/9/10
 */
public abstract class Player {

    private final int id;
    private String nickname;
    private Room bindRoom;

    public Player(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void createRoom(Room room) {
        this.bindRoom = room;
    }

    public Either<Errors, Void> joinRoom(Room room) {
        // game is playing?
        this.bindRoom = room;
        return bindRoom.addPlayer(this);
    }

    public void leaveRoom() {
        if (bindRoom == null) {
            return;
        }
        bindRoom.removePlayer(this);
        this.bindRoom = null;
    }

    public Optional<Room> getJoinedRoom() {
        return Optional.ofNullable(bindRoom);
    }

    public ExecuteResult play(PlayCommand command, String data) {
        Optional<Room> joinedRoom = getJoinedRoom();
        if (!joinedRoom.isPresent()) {
            return ExecuteResult.fail(Errors.COMMAND_FLOW_ERROR);
        }
        Game game = joinedRoom.get().getGame();
        if (!game.isPlaying()) {
            throw new IllegalStateException("game is playing in room:" + bindRoom.getId());
        }
        return command.play(this, game, data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Player)) return false;
        Player player = (Player) o;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
