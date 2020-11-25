package edu.neu.psa.Actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import edu.neu.psa.GA.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorkerActor extends AbstractActor {

    static public Props props() {
        return Props.create(WorkerActor.class, () -> new WorkerActor());
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder().match(CalculateFitness.class, request -> {
            Genotype genotype = request.genotype;
            HashMap<Integer, Team> teams = request.teams;
            genotype.createPhenoType(teams);
            sender().tell(new MasterActor.Result(genotype), getSelf());
            context().stop(getSelf());


        }).match(RegenerateGenotype.class, request -> {
            Population previousGeneration = request.previousGeneration;
            double crossoverRate = request.crossoverRate;
            double mutationRate = request.mutationRate;
            double elitismCount = request.elitismCount;
            int fitnessIndex = request.fitnessIndex;
            Database database = request.database;
            Genotype genotype = crossoverPopulation(previousGeneration, crossoverRate, elitismCount, fitnessIndex);
           // mutateGenotype(genotype, database, fitnessIndex, elitismCount, mutationRate);
            genotype.createPhenoType(database.getTeams());
            sender().tell(new MasterActor.Result(genotype), getSelf());
            context().stop(getSelf());


        }).build();
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


    public Genotype crossoverPopulation(Population previousPopulation, double crossoverRate, double elitismCount, int fitnessIndex) {
        Genotype parent1 = previousPopulation.getFittest();
        //Genotype parent1 = selectParent(previousPopulation);
        if (crossoverRate > Math.random() && fitnessIndex >= elitismCount) {
            Genotype offspring = new Genotype(parent1.getChromosomeLength());
            Genotype parent2 = selectParent(previousPopulation);
            for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
                // Use half of parent1's genes and half of parent2's genes
                if (0.5 > Math.random()) {
                    offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                } else {
                    offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                }
            }
            return offspring;


        } else {
            return parent1;
        }

    }

    public void mutateGenotype(Genotype genotype, Database database, int fitnessIndex, double elitismCount, double mutationRate) {
        //Population newPopulation = new Population();
        Genotype randomGenotype = new Genotype(database);
        for (int geneIndex = 0; geneIndex < genotype.getChromosomeLength(); geneIndex++) {
            // Skip mutation if this is an elite genotype
            // Does this gene need mutation?
            if (mutationRate > Math.random()) {
                // Swap for new gene
                genotype.setGene(geneIndex, randomGenotype.getGene(geneIndex));
            }
        }

    }


    static public class CalculateFitness {
        public final Genotype genotype;
        private final HashMap<Integer, Team> teams;

        public CalculateFitness(Genotype genotype, HashMap<Integer, Team> teams) {
            this.genotype = genotype;
            this.teams = teams;
        }
    }

    static public class RegenerateGenotype {

        private final int fitnessIndex;
        private final double crossoverRate;
        private final Population previousGeneration;
        private final double mutationRate;
        private final double elitismCount;
        private final Database database;

        public RegenerateGenotype(int fitnessIndex, double crossoverRate, Population previousGeneration, double mutationRate, double elitismCount, Database database) {
            this.fitnessIndex = fitnessIndex;
            this.crossoverRate = crossoverRate;
            this.previousGeneration = previousGeneration;
            this.mutationRate = mutationRate;
            this.elitismCount = elitismCount;
            this.database = database;
        }
    }
}
