package com.softevol.appsystemimpl.model;

/**
 * Created with IntelliJ IDEA.
 * User: android
 * Date: 03.04.13
 * Time: 12:16
 * To change this template use File | Settings | File Templates.
 */
public class ScoresModel {

    private int scoreId;

    private String scoreName;

    private int scoreDifficulty;

    private int scoreMoves;

    private String scoreTime;

    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    public String getScoreName() {
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }

    public int getScoreDifficulty() {
        return scoreDifficulty;
    }

    public void setScoreDifficulty(int scoreDifficulty) {
        this.scoreDifficulty = scoreDifficulty;
    }

    public int getScoreMoves() {
        return scoreMoves;
    }

    public void setScoreMoves(int scoreMoves) {
        this.scoreMoves = scoreMoves;
    }

    public String getScoreTime() {
        return scoreTime;
    }

    public void setScoreTime(String scoreTime) {
        this.scoreTime = scoreTime;
    }
}
