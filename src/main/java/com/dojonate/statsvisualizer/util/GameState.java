package com.dojonate.statsvisualizer.util;

import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.model.RunnerAdvance;

import java.util.List;

public class GameState {
    private Player batter;
    private Player firstBase;
    private Player secondBase;
    private Player thirdBase;

    public GameState() {
        this.batter = null;
        this.firstBase = null;
        this.secondBase = null;
        this.thirdBase = null;
    }

    // Getters and setters for each base

    public Player getBatter() {
        return batter;
    }

    public void setBatter(Player batter) {
        this.batter = batter;
    }

    public Player getFirstBase() {
        return firstBase;
    }

    public void setFirstBase(Player firstBase) {
        this.firstBase = firstBase;
    }

    public Player getSecondBase() {
        return secondBase;
    }

    public void setSecondBase(Player secondBase) {
        this.secondBase = secondBase;
    }

    public Player getThirdBase() {
        return thirdBase;
    }

    public void setThirdBase(Player thirdBase) {
        this.thirdBase = thirdBase;
    }

//    // You may also want a method to “advance” the runners when a play is processed.
//    // For example:
//    public void processPlay(Player batter, List<RunnerAdvance> advances) {
//        for (RunnerAdvance advance : advances) {
//            // Logic to move runners based on the advance details
//        }
//    }
}
