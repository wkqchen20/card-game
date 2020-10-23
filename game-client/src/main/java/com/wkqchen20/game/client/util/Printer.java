package com.wkqchen20.game.client.util;

import com.wkqchen20.game.client.Context;
import com.wkqchen20.game.common.entity.DisplayPlayer;
import com.wkqchen20.game.landlords.common.LCard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/13
 */
public class Printer {

    private Printer() {}

    /**
     * 等待输入的输出，不换行
     *
     * @param msg
     */
    public static void waitForInput(String msg) {
        DisplayPlayer player = Context.getPlayer();
        if (player != null) {
            System.out.print(String.format("@%s %s > %s", player.display(), time(), msg));
        } else {
            System.out.print(String.format("%s > %s", time(), msg));
        }
    }

    /**
     * @param msg
     * @xxx 10-14 10:48:50 > msg
     */
    public static void notice(String msg) {
        DisplayPlayer player = Context.getPlayer();
        if (player != null) {
            System.out.println(String.format("@%s %s > %s", player.display(), time(), msg));
        } else {
            System.out.println(String.format("%s > %s", time(), msg));
        }
    }

    public static void landlordsSellCards(List<LCard> cards) {
        innerLandlordsCards(cards, false);
    }

    public static void landlordsCards(List<LCard> cards) {
        innerLandlordsCards(cards, true);
    }

    private static void innerLandlordsCards(List<LCard> cards, boolean withIndex) {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        StringBuilder sb4 = new StringBuilder();
//┌────┐────┐────┐────┐────┐────┐────┐────┐────┐────┐────┐────┐────┐────┐────┐
//│︎♥   │︎♥   │︎♥   │︎♥   │︎♥   │︎♥   │︎♥   │︎♥   │︎♥   │︎♥   │︎♥   │︎♥   │︎♥   │S   │B   │
//│3   │4   │5   │6   │7   │8   │9   │10  │J   │Q   │K   │A   │2   │W   │W   │
//└─1──┘─2──┘─3──┘─4──┘─5──┘─6──┘─7──┘─8──┘─9──┘─10─┘─11─┘─12─┘─13─┘─14─┘─15─┘

        for (int i = 0; i < cards.size(); i++) {
            if (i == 0) {
                sb1.append("┌");
                sb2.append("│");
                sb3.append("│");
                sb4.append("└");
            }
            sb1.append("────┐");
            LCard card = cards.get(i);
            sb2.append(card.getColor().display).append("   │");
            sb3.append(card.getValue().display);
            if (card.getValue() == LCard.Value.VALUE_10) {
                sb3.append("  │");
            } else {
                sb3.append("   │");
            }
            sb4.append("─");
            if (withIndex) {
                int displayIndex = i + 1;
                sb4.append(displayIndex);
                if (displayIndex < 10) {
                    sb4.append("─");
                }
            } else {
                sb4.append("──");
            }
            sb4.append("─┘");
        }
        sb1.append(System.lineSeparator()).append(sb2).append(System.lineSeparator()).append(sb3).append(System.lineSeparator()).append(sb4).append(System.lineSeparator()).toString();
        System.out.println(sb1.toString());
    }

    private static String time() {
        return new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date());
    }

}
