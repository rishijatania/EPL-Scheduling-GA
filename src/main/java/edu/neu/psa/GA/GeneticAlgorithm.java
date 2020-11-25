package edu.neu.psa.GA;

import java.util.List;

public class GeneticAlgorithm {

    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount;


    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
    }


    public Population initPopulation(Database database) {
        Population population = new Population(this.populationSize, database);
        return population;
    }

    public double calculateFitness(Genotype genotype, Database database) {
        // Create new database object to use -- cloned from an existing database
        Database threadDatabase = new Database(database);
        threadDatabase.createSeasonSchedule(genotype);
        int clashes = threadDatabase.calculateClashes();
        double fitness = 1 / (double) (clashes + 1);
        genotype.setFitness(fitness);
        return fitness;

    }

    public void evalPopulation(Population population, Database database) {
        //double populationFitness = 0;
        double populationFitness = population.getGenotypes().stream()
                .mapToDouble(x -> calculateFitness(x, database))
                .sum();
        population.setPopulationFitness(populationFitness);
        //sort the population based on fitness
        population.sortBasedOnFitness();


    }

    public boolean isTerminationConditionMet(Population population) {
        boolean isTerminationConditionMet = population.getGenotypes().stream()
                .anyMatch(ind -> ind.getFitness() == 1);
        return isTerminationConditionMet;
    }

    public Genotype selectParent(Population population) {
        List<Genotype> genotypes = population.getGenotypes();
        double populationFitness = population.getPopulationFitness();
        double rouletteWheelPosition = Math.random() * populationFitness;
        double spinWheel = 0;
        for (Genotype genotype : genotypes) {
            spinWheel += genotype.getFitness();
            if (spinWheel >= rouletteWheelPosition) {
                return genotype;
            }
        }
        // Get genotypes
        return genotypes.get(population.populationSize() - 1);
    }
//    public Genotype selectParent(Population population) {
//        // Create tournament
//        Population tournament = new Population();
//        int tournamentSize = 5;
//        // Add random individuals to the tournament
//        population.shuffle();
//        for (int i = 0; i < tournamentSize; i++) {
//            Genotype tournamentIndividual = population.getFittest(i);
//            tournament.setIndividual(i, tournamentIndividual);
//        }
//
//        tournament.sortBasedOnFitness();
//        // Return the best
//        return tournament.getFittest(0);
//    }


    public Population crossoverPopulation(Population population) {
        // Create new population
        Population newPopulation = new Population();
        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.populationSize(); populationIndex++) {
            Genotype parent1 = population.getFittest();
            // Apply crossover to this individual?
            if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
                // Initialize offspring
                Genotype offspring = new Genotype(parent1.getChromosomeLength());
                // Find second parent
                Genotype parent2 = selectParent(population);
                // Loop over genome
                for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
                    // Use half of parent1's genes and half of parent2's genes
                    if (0.5 > Math.random()) {
                        offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                    } else {
                        offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                    }
                }
                // Add offspring to new population
                newPopulation.setIndividual(populationIndex, offspring);
            } else {
                // Add individual to new population without applying crossover
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }
        return newPopulation;
    }


    public Population mutatePopulation(Population population, Database database) {
        population.sortBasedOnFitness();
        // Initialize new population
        Population newPopulation = new Population();
        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.populationSize(); populationIndex++) {
            Genotype genotype = population.getFittest(populationIndex);
            // Loop over genotype’s genes
            // Create random genotype to swap genes with
            Genotype randomGenotype = new Genotype(database);
            for (int geneIndex = 0; geneIndex < genotype.getChromosomeLength(); geneIndex++) {
                // Skip mutation if this is an elite genotype
                if (populationIndex > this.elitismCount) {
                    // Does this gene need mutation?
                    if (this.mutationRate > Math.random()) {
                        // Swap for new gene
                        genotype.setGene(geneIndex, randomGenotype.getGene(geneIndex));
                    }
                }
            }
            // Add genotype to population
            newPopulation.setIndividual(populationIndex, genotype);
        }
        // Return mutated population
        return newPopulation;
    }
}
