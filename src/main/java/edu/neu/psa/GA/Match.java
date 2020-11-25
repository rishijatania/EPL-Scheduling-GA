package edu.neu.psa.GA;

import java.util.Arrays;

public class Match {

    private Integer[] match;

    public Match(int homeTeam, int awayTeam) {
        this.match = new Integer[2];
        this.match[0] = homeTeam;
        this.match[1] = awayTeam;

    }


    public Integer[] getMatch() {
        return match;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match1 = (Match) o;
        return Arrays.equals(getMatch(), match1.getMatch());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getMatch());
    }
}
