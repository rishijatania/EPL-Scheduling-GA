package edu.neu.psa.GA;

import java.util.ArrayList;
import java.util.List;

public class MatchSchedule {
    private List<Match> matches;


    public MatchSchedule() {
        this.matches = new ArrayList<>();
    }

    public void add(Match match) {
        this.matches.add(match);
    }


    public List<Match> getMatches() {
        return matches;
    }
}
