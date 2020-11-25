package edu.neu.psa.GA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Population {

    private List<Genotype> population;

    private double populationFitness = -1;


    public Population() {
        this.population = new ArrayList<>();
    }

    public Population(int populationSize, Database database) {
        this.population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Genotype genotype = new Genotype(database);
            this.population.add(genotype);
        }

    }

    public Population(Population cloneable) {
        this.population = new ArrayList<>();
        for (Genotype genotype : cloneable.getGenotypes()) {
            this.population.add(genotype);

        }
        this.sortBasedOnFitness();
        this.populationFitness = cloneable.populationFitness;

    }

//    public Population(int populationSize) {
//        this.population = new ArrayList<>();
//        for (Genotype genotype : cloneable.getGenotypes()) {
//            this.population.add(genotype);
//
//        }
//        this.sortBasedOnFitness();
//        this.populationFitness = cloneable.populationFitness;
//
//    }


    public void sortBasedOnFitness() {
        this.population = this.population.stream().sorted((x, y) -> {
            double xFitness = x.getFitness();
            double yFitness = y.getFitness();
            if (xFitness > yFitness) {
                return -1;
            } else if (xFitness < yFitness) {
                return 1;
            } else {
                return 0;
            }
            // return 0;
        }).collect(Collectors.toList());


    }


    public Genotype getFittest() {
        return this.population.get(0);

    }

    public Genotype getFittest(int index) {
        return this.population.get(index);

    }

    public void addGenotype(Genotype genotype) {
        this.population.add(genotype);
    }


    public double getPopulationFitness() {
        return populationFitness;
    }

    public void setPopulationFitness(double populationFitness) {
        this.populationFitness = populationFitness;
    }

    public List<Genotype> getGenotypes() {
        return population;
    }

    public void calculateFitness() {
        double populationFitness = this.population.stream()
                .mapToDouble(x -> x.getFitness())
                .sum();
        this.setPopulationFitness(populationFitness);

    }

    public void shuffle() {
        Random random = new Random();
        for (int i = 0; i < population.size(); i++) {
            int index = random.nextInt(i + 1);
            Genotype a = population.get(index);
            population.set(index, population.get(i));
            population.set(i, a);
        }
    }


    public int populationSize() {
        return this.population.size();
    }

    public void setIndividual(int populationIndex, Genotype parent1) {
        this.population.add(populationIndex, parent1);

    }
}
