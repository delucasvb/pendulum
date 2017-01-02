package com.realbadapps.pendulum.server.game;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DealOrNoDeal extends Game {
    private static final Double[] POINTS = {0.01, 0.05, 0.1, 0.25, 0.5, 1d, 2d, 5d, 10d, 25d, 50d,
            100d, 200d, 500d, 1000d, 2000d, 5000d, 10000d, 25000d, 50000d, 100000d, 200000d,
            400000d, 600000d, 800000d, 1000000d};
    public static final String TYPE = "deal_or_no_deal";

    private int rounds;

    @JsonIgnore
    private int currentRound;

    @JsonIgnore
    private Distribution[] distributions;

    public DealOrNoDeal() {
        super(TYPE);
    }

    @Override
    public void convertFramesToTime() {
        // Not supported
    }

    private static Distribution[] prepare(int rounds) {
        Distribution[] distributions = new Distribution[rounds];
        for (int i = 0; i < distributions.length; i++)
            distributions[i] = new Distribution();
        return distributions;
    }

    /*
        Getters & setters
     */

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;

        this.distributions = prepare(rounds);
        this.currentRound = 0;
    }

    public Distribution[] getDistributions() {
        return distributions;
    }

    public void setDistributions(Distribution[] distributions) {
        this.distributions = distributions;
    }

    public Distribution getDistribution() {
        Distribution distribution = distributions[currentRound];
        currentRound = (currentRound + 1) % distributions.length;
        return distribution;
    }

    private static class Distribution {
        private Double[] values;

        private Distribution() {
            List<Double> valueList = Arrays.asList(POINTS);
            Collections.shuffle(valueList);
            values = valueList.toArray(new Double[valueList.size()]);
        }

        public Double[] getValues() {
            return values;
        }

        public void setValues(Double[] values) {
            this.values = values;
        }
    }
}