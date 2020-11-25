package edu.neu.psa.GA;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Genotype {

    private int[] chromosome;
    private double fitness = -1;
    private Phenotype phenotype;


    public Genotype(int[] chromosome) {
        this.chromosome = chromosome;
    }


    public Genotype(Database database) {
        int numberOfTeams = database.getNumberOfTeams();
        int matchesPlayedByEachTeam = (numberOfTeams - 1) * 2;

        /* Ex: numberOfTeams = 4

         * Each team plays each other twice (Home and Away)
         * Every team plays 6 matches.
         * Chromosome Length is the number of matches played by each team
         * In this case - 4 * 6 = 24
         *
         * */
        int chromosomeLength = matchesPlayedByEachTeam * numberOfTeams;
        this.chromosome = new int[chromosomeLength];
        int chromosomeIndex = 0;
        for (int i = 0; i < chromosomeLength / 2; i++) {
            int team1 = database.getRandomTeam().getTeamId();
            this.chromosome[chromosomeIndex] = team1;
            chromosomeIndex++;
            int team2 = database.getRandomTeam().getTeamId();
            this.chromosome[chromosomeIndex] = team2;
            chromosomeIndex++;

        }
    }

    public Genotype(int chromosomeLength) {
        // Create random individual
        int[] individual;
        individual = new int[chromosomeLength];
        /**
         * This comment and the for loop doesn't make sense for this chapter.
         * But I'm leaving it in here because you were instructed to copy this
         * class from Chapter 4 -- and NOT having this comment here might be
         * more confusing than keeping it in.
         *
         * Comment from Chapter 4:
         *
         * "In this case, we can no longer simply pick 0s and 1s -- we need to
         * use every city index available. We also don't need to randomize or
         * shuffle this chromosome, as crossover and mutation will ultimately
         * take care of that for us."
         */
        for (int gene = 0; gene < chromosomeLength; gene++) {
            individual[gene] = gene;
        }
        this.chromosome = individual;
    }


    public int getChromosomeLength() {
        return chromosome.length;
    }

    public int[] getChromosome() {
        return chromosome;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int getGene(int geneIndex) {
        return chromosome[geneIndex];
    }

    public void setGene(int geneIndex, int gene) {
        this.chromosome[geneIndex] = gene;
    }

    public String toString() {
        String output = "";
        for (int gene = 0; gene < this.chromosome.length; gene++) {
            output += this.chromosome[gene] + ",";
        }
        return output;
    }

    public void createPhenoType(HashMap<Integer, Team> teams) {
        int numberOfTeams = teams.size();
        int matchDays = (numberOfTeams - 1) * 2;
        int[] chromosome = this.getChromosome();
        int chromsoPos = 0;
        Phenotype phenotype = new Phenotype();
        List<MatchSchedule> matchSchedules = phenotype.getMatchSchedules();
        for (int i = 0; i < matchDays; i++) {
            MatchSchedule matchSchedule = new MatchSchedule();
            for (int j = 0; j < numberOfTeams / 2; j++) {
                int teamA = chromsoPos++;
                int teamB = chromsoPos++;
                Match match = new Match(chromosome[teamA], chromosome[teamB]);
                matchSchedule.add(match);

            }
            matchSchedules.add(matchSchedule);

        }
        this.phenotype = phenotype;
        this.calculateFitness();
    }
    /*
    * Calculate fitness by inversing the clashes + 1
    *
    * */
    public void calculateFitness() {
        int clashes = calculateClashes();
        double fitness = 1 / (double) (clashes + 1);
        this.setFitness(fitness);
    }


    /*
    * Calculate Clashes:
    * 1) Calculate number of times same match being played
    * 2) Calculate the number of time a team is playing multiple matches on the same day.
    * 3) Sum 1) and 2) and return back.
    *
    * */

    public int calculateClashes() {
        int clashes;
        int numberOfTimeSameMatchBeingPlayed;
        int teamsPlayingMultipleMatchesSameDay = 0;
        int teamsPlayingAgainstEachOther = 0;
        List<MatchSchedule> seasonSchedule = this.phenotype.getMatchSchedules();


        //Get all matches for a particular schedule
        List<Match> allMatches = seasonSchedule.stream()
                .flatMap(x -> x.getMatches().stream())
                .collect(Collectors.toList());
        // Calculate number of times same match being played
        Map<Match, Long> matchesMap = allMatches.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        numberOfTimeSameMatchBeingPlayed = matchesMap.values().stream()
                .filter(x -> x.intValue() > 1)
                .mapToInt(x -> x.intValue())
                .sum();



        if (numberOfTimeSameMatchBeingPlayed != 0) {
            numberOfTimeSameMatchBeingPlayed = numberOfTimeSameMatchBeingPlayed / 2;
        }


        //Calculate the number of time one team is playing matches on the same day.
        for (MatchSchedule matchSchedule : seasonSchedule) {
            Integer[] matchDayChromosome = matchSchedule.getMatches().stream()
                    .flatMap(x -> Stream.of(x.getMatch()))
                    .toArray(Integer[]::new);
            Map<Integer, Long> teamPlayingMultipleTimesSameDayMap = Arrays.asList(matchDayChromosome).stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            int sum = teamPlayingMultipleTimesSameDayMap.values().stream()
                    .filter(x -> x.intValue() > 1)
                    .mapToInt(x -> x.intValue())
                    .sum();
            if (sum != 0) {
                sum = sum / 2;
            }
            teamsPlayingMultipleMatchesSameDay = teamsPlayingMultipleMatchesSameDay + sum;
        }
        clashes = numberOfTimeSameMatchBeingPlayed + teamsPlayingMultipleMatchesSameDay + teamsPlayingAgainstEachOther;
        return clashes;


    }

    public Phenotype getPhenotype() {
        return phenotype;
    }
}
