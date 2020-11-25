import edu.neu.psa.GA.Database;
import edu.neu.psa.GA.Genotype;
import edu.neu.psa.GA.Match;
import edu.neu.psa.GA.Phenotype;
import junit.framework.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class GenotypeTest {


    @Test
    public void testClashesSixTeam() {
        Database database = initializeDatabaseTeam6();
        int[] chromosomes = new int[]{5, 1, 6, 4, 3, 2, 3, 5, 6, 6, 2, 4, 6, 1, 4, 2, 5, 3, 2, 3, 6, 5, 1, 4, 1, 3, 4, 6, 2, 5, 1, 5, 2, 6, 3, 4, 5, 2, 4, 1, 3, 6, 6, 3, 5, 4, 1, 2, 3, 1, 4, 5, 6, 2, 2, 1, 5, 6, 4, 3};
        Genotype genotype = new Genotype(chromosomes);
        genotype.createPhenoType(database.getTeams());
        genotype.calculateFitness();
        Assert.assertEquals(0.5, genotype.getFitness());


    }

    @Test
    public void testClashesEightTeam() {
        Database database = initializeDatabaseTeam6();
        int[] chromosomes = new int[]{3, 6, 2, 5, 1, 4, 5, 6, 3, 2, 4, 1, 2, 1, 4, 5, 6, 3, 5, 3, 6, 1, 2, 4, 5, 4, 1, 3, 2, 6, 5, 1, 4, 6, 2, 3, 6, 5, 4, 2, 3, 1, 1, 5, 3, 4, 6, 2, 5, 2, 4, 3, 1, 6, 3, 5, 1, 2, 6, 4};
        Genotype genotype = new Genotype(chromosomes);
        genotype.createPhenoType(database.getTeams());
        genotype.calculateFitness();
        Assert.assertEquals(1.0, genotype.getFitness());


    }

    @Test
    public void testPhenoTypeMatchSchedule() {
        Database database = initializeDatabaseTeam6();
        int[] chromosomes = new int[]{3, 6, 2, 5, 1, 4, 5, 6, 3, 2, 4, 1, 2, 1, 4, 5, 6, 3, 5, 3, 6, 1, 2, 4, 5, 4, 1, 3, 2, 6, 5, 1, 4, 6, 2, 3, 6, 5, 4, 2, 3, 1, 1, 5, 3, 4, 6, 2, 5, 2, 4, 3, 1, 6, 3, 5, 1, 2, 6, 4};
        Genotype genotype = new Genotype(chromosomes);
        genotype.createPhenoType(database.getTeams());
        Phenotype phenotype = genotype.getPhenotype();
        phenotype.getMatchSchedules().size();
        Assert.assertEquals(10, phenotype.getMatchSchedules().size());


    }

    @Test
    public void testPhenoTypeNumberofMatches() {
        Database database = initializeDatabaseTeam6();
        int[] chromosomes = new int[]{3, 6, 2, 5, 1, 4, 5, 6, 3, 2, 4, 1, 2, 1, 4, 5, 6, 3, 5, 3, 6, 1, 2, 4, 5, 4, 1, 3, 2, 6, 5, 1, 4, 6, 2, 3, 6, 5, 4, 2, 3, 1, 1, 5, 3, 4, 6, 2, 5, 2, 4, 3, 1, 6, 3, 5, 1, 2, 6, 4};
        Genotype genotype = new Genotype(chromosomes);
        genotype.createPhenoType(database.getTeams());
        Phenotype phenotype = genotype.getPhenotype();
        List<Match> noOfMatches = phenotype.getMatchSchedules().
                stream()
                .flatMap(m -> m.getMatches().stream()).collect(Collectors.toList());
        Assert.assertEquals(30, noOfMatches.size());


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


    public static Database initializeDatabaseTeam8() {
        Database database = new Database();
        database.addTeam(1, "Manchester United");
        database.addTeam(2, "Manchester City");
        database.addTeam(3, "Chelsea");
        database.addTeam(4, "Liverpool");
        database.addTeam(5, "Tottenham");
        database.addTeam(6, "Arsenal");
        database.addTeam(7, "Everton");
        database.addTeam(8, "Wolves");
        return database;

    }

}
