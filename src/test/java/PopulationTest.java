import edu.neu.psa.GA.Database;
import edu.neu.psa.GA.GeneticAlgorithm;
import edu.neu.psa.GA.Genotype;
import edu.neu.psa.GA.Population;
import junit.framework.Assert;
import org.junit.Test;

public class PopulationTest {

    @Test
    public void testCrossOver() {
        Database database = initializeDatabaseTeam6();
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.01, 0.95, 0);
        Population population = ga.initPopulation(database);
        Population crossOverPopulation = ga.crossoverPopulation(population);
        population.sortBasedOnFitness();
        crossOverPopulation.sortBasedOnFitness();


    }

    @Test
    public void testMutation() {
        Database database = initializeDatabaseTeam6();
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.01, 0.95, 0);
        Population population = ga.initPopulation(database);
        population = ga.crossoverPopulation(population);
        population.sortBasedOnFitness();
        ga.mutatePopulation(population, database);


    }

    public static Database initializeDatabaseTeam6() {
        Database database = new Database();
        database.addTeam(1, "Manchester United");
        database.addTeam(2, "Manchester City");
        database.addTeam(3, "Chelsea");
        database.addTeam(4, "Liverpool");
        database.addTeam(5, "Tottenham");
        database.addTeam(6, "Arsenal");
        return database;

    }
}
