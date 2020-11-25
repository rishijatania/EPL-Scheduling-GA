package edu.neu.psa.GA;

import java.util.ArrayList;
import java.util.List;

public class Phenotype {

    private List<MatchSchedule> matchSchedules;
    private double fitnessScore;

    public Phenotype() {
        this.matchSchedules = new ArrayList<>();
    }

    public List<MatchSchedule> getMatchSchedules() {
        return matchSchedules;
    }

    public void setMatchSchedules(List<MatchSchedule> matchSchedules) {
        this.matchSchedules = matchSchedules;
    }

    public double getFitnessScore() {
        return fitnessScore;
    }

    public void setFitnessScore(double fitnessScore) {
        this.fitnessScore = fitnessScore;
    }
}
