package com.wkqchen20.game.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.wkqchen20.game.common.Card;
import com.wkqchen20.game.core.util.IdGenerator;

import java.util.List;

/**
 * @Author: wkqchen20
 * @Date: 2020/9/10
 */
public abstract class AbstractListenableGame<S extends Status, C extends Card> implements Game<S, C> {

    private List<GameListener> listeners = Lists.newArrayList();
    private int id;
    private volatile S status;

    public AbstractListenableGame(S status) {
        this.status = status;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public void start() {
        this.id = IdGenerator.gameIdGenerate();
        doStart();
    }

    protected abstract void doStart();

    @Override
    public S status() {
        return status;
    }

    protected void changeStatus(S s) {
        if (status == s) {
            return;
        }
        status = s;
        afterStatusChanged();
    }

    @Override
    public void addListener(GameListener listener) {
        Preconditions.checkNotNull(listener, "listener should not be null");
        listeners.add(listener);
    }

    private void afterStatusChanged() {
        listeners.forEach(listener -> listener.onStatusChanged(this));
    }

}
