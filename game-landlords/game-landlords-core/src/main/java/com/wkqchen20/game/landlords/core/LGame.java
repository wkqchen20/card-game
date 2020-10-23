package com.wkqchen20.game.landlords.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.wkqchen20.game.core.*;
import com.wkqchen20.game.landlords.common.LCard;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static com.wkqchen20.game.landlords.common.LCard.Value.VALUE_BIG_KING;
import static com.wkqchen20.game.landlords.common.LCard.Value.VALUE_SMALL_KING;
import static java.util.stream.Collectors.toList;

/**
 * @Author: wkqchen20
 * @Date: 2020/9/10
 */
public class LGame extends AbstractListenableGame<LStatus, LCard> {

    private static final List<LCard> ALL_CARDS = Lists.newArrayListWithCapacity(54);

    private GameType gameType;
    private LRoom room;
    private List<PlayerWithCards<LCard>> playerWithCards;
    /**
     * 该出牌或者发出指令的玩家
     */
    private Player currentToPlayPlayer;
    /**
     * 地主
     */
    private Player landlordPlayer;
    private List<LCard> threeLandlordCards;
    private List<LPlayerBehavior> playerBehaviorList;

    public LGame(LRoom room) {
        this(room, new CommonLGameType());
    }

    public LGame(LRoom room, GameType gameType) {
        super(LStatus.NOT_START);
        this.room = room;
        this.gameType = gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public GameType gameType() {
        return gameType;
    }

    @Override
    public boolean isPlaying() {
        LStatus status = status();
        return status == LStatus.INIT || status == LStatus.ELECTION || status == LStatus.PLAYING;
    }

    @Override
    protected void doStart() {
        List<Player> players = room.getPlayers();
        if (players.size() != gameType.allowPlayerCount()) {
            throw new IllegalStateException("人数不对，不能开始游戏");
        }
        if (status() != LStatus.NOT_START) {
            throw new IllegalStateException("游戏状态有误");
        }
        changeStatus(LStatus.INIT);
        // 首先出牌的玩家
        int index = ThreadLocalRandom.current().nextInt(0, players.size());
        currentToPlayPlayer = players.get(index);
        // 出牌信息
        playerBehaviorList = Lists.newArrayList();
        // 发牌
        List<LCard> lCards = Lists.newArrayList(ALL_CARDS);
        GameType.DistributeAndRemainCardsTuple tuple = gameType.distributeCards(lCards);
        List<List<LCard>> distributeCardsList = tuple.getDistributeCardsList();
        playerWithCards = IntStream.range(0, players.size()).mapToObj(idx -> {
            List<LCard> tmpCards = distributeCardsList.get(idx);
            return new PlayerWithCards<>(players.get(idx), tmpCards);
        }).collect(toList());
        threeLandlordCards = tuple.getRemainCards();
        //
        changeStatus(LStatus.ELECTION);
    }

    @Override
    public Player currentPlayerToPlay() {
        return currentToPlayPlayer;
    }

    @Override
    public boolean isPlayerTurn(Player player) {
        return currentToPlayPlayer.equals(player);
    }

    @Override
    public Player turnToNext() {
        checkOngoing();
        List<Player> players = playerWithCards.stream().map(PlayerWithCards::getPlayer).collect(toList());
        int index = players.indexOf(currentToPlayPlayer);
        Player nextPlayer;
        if (index == players.size() - 1) {
            nextPlayer = players.get(0);
        } else {
            nextPlayer = players.get(index + 1);
        }
        currentToPlayPlayer = nextPlayer;
        return nextPlayer;
    }

    @Override
    public List<PlayerWithCards<LCard>> playerWithCards() {
        return ImmutableList.copyOf(playerWithCards);
    }

    public void setLandlordPlayer(Player player) {
        if (status() != LStatus.ELECTION) {
            throw new IllegalStateException("游戏状态有误");
        }
        landlordPlayer = player;
        PlayerWithCards playerWithCards = this.playerWithCards.stream().filter(pc -> pc.getPlayer().equals(player)).findFirst().orElseThrow(() -> new IllegalArgumentException("unknown player:" + player.getId()));
        playerWithCards.getCards().addAll(threeLandlordCards);
        currentToPlayPlayer = player;
        Collections.sort(playerWithCards.getCards());
        changeStatus(LStatus.PLAYING);
    }

    @Override
    public List<LPlayerBehavior> playerBehaviors() {
        checkOngoing();
        return ImmutableList.copyOf(playerBehaviorList);
    }

    @Override
    public void addBehavior(PlayerBehavior playerBehavior) {
        LPlayerBehavior lPlayerBehavior = (LPlayerBehavior) playerBehavior;
        checkOngoing();
        playerBehaviorList.add(lPlayerBehavior);
        if (lPlayerBehavior.getStatus() == BehaviorStatus.SELL) {
            PlayerWithCards sellCardPlayer = playerWithCards.stream()
                                                            .filter(pc -> pc.getPlayer().equals(playerBehavior.getPlayer()))
                                                            .findFirst()
                                                            .orElseThrow(() -> new IllegalArgumentException("unknown player:" + playerBehavior.getPlayer().getId()));
            sellCardPlayer.getCards().removeAll(lPlayerBehavior.getData().getCards());
            if (sellCardPlayer.getCards().isEmpty()) {
                changeStatus(LStatus.OVER);
            }
        }
    }

    private void checkOngoing() {
        LStatus status = status();
        if (status == LStatus.ELECTION || status == LStatus.PLAYING) {
            return;
        }
        throw new IllegalStateException("游戏状态有误");
    }

    public Player getLandlordPlayer() {
        return landlordPlayer;
    }

    static {
        ALL_CARDS.add(LCard.smallKing());
        ALL_CARDS.add(LCard.bigKing());
        for (LCard.Color color : LCard.Color.values()) {
            if (color == LCard.Color.NONE_0 || color == LCard.Color.NONE_1) {
                continue;
            }
            for (LCard.Value value : LCard.Value.values()) {
                if (value == VALUE_SMALL_KING || value == VALUE_BIG_KING) {
                    continue;
                }
                ALL_CARDS.add(LCard.of(value, color));
            }
        }
    }
}
