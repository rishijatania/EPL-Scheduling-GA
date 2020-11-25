package edu.neu.psa.Actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import edu.neu.psa.GA.*;

import java.util.List;

public class MasterActor extends AbstractActor {

    private Population currentPopulation;

    private final int populationSize;
    private final GeneticAlgorithm geneticAlgorithm;
    private int currentGeneration = 1;

    private final double mutationRate;
    private final double crossoverRate;
    private final int elitismCount;
    private Database database;

    public MasterActor(int populationSize, double mutationRate, double crossoverRate, int elitismCount) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.geneticAlgorithm = new GeneticAlgorithm(this.populationSize, mutationRate, crossoverRate, elitismCount);
    }

    static public Props props() {
        return Props.create(MasterActor.class, () -> new MasterActor(100, 0.01, 0.95, 0));
    }

    /*
     * Messages that can be received by the MasterActor
     *
     * */
    static public class Init {

        public Init() {
        }
    }

    static public class Result {

        public final Genotype genotype;

        public Result(Genotype genotype) {
            this.genotype = genotype;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Init.class, init -> {
            executeInitLogic();

        }).match(Result.class, result -> {
            evaluateResult(result.genotype);
        }).build();
    }


    public void evaluateResult(Genotype genotype) {
        this.currentPopulation.addGenotype(genotype);
        if (this.currentPopulation.populationSize() == this.populationSize) {
            //recalculateFitness();
            this.currentPopulation = this.geneticAlgorithm.mutatePopulation(this.currentPopulation, this.database);
            recalculateFitness(this.currentPopulation);
            if (this.geneticAlgorithm.isTerminationConditionMet(this.currentPopulation) == false) {
                System.out.println("Generation: " + this.currentGeneration + " fittest " + this.currentPopulation.getFittest().getFitness());
                // Population previousGeneration = new Population(this.currentPopulation);
                Population previousGeneration = new Population(this.currentPopulation);
                this.currentPopulation.getGenotypes().clear();
                for (int i = 0; i < populationSize; i++) {
                    WorkerActor.RegenerateGenotype regenerateGenotype = new WorkerActor.RegenerateGenotype(i, crossoverRate, previousGeneration, mutationRate, elitismCount, database);
                    ActorRef workerActor = getContext().actorOf(WorkerActor.props(), "Generation-" + this.currentGeneration + "-Child-" + i);
                    workerActor.tell(regenerateGenotype, getSelf());

                }
                this.currentGeneration++;


            } else {
                printSolution();
                context().stop(getSelf());
                context().system().terminate();

            }


        }
    }


    public void recalculateFitness(Population population) {
        population.calculateFitness();
        population.sortBasedOnFitness();

    }


    public void executeInitLogic() {
        //Create Initial Population
        this.database = initializeDatabase();
        Population population = this.geneticAlgorithm.initPopulation(database);
        this.currentPopulation = population;
        for (Genotype genotype : population.getGenotypes()) {
            WorkerActor.CalculateFitness calculateFitness = new WorkerActor.CalculateFitness(genotype, database.getTeams());
            ActorRef workerActor = getContext()
                    .actorOf
                            (WorkerActor.props(), "Generation-1" + "-Child-" + population.getGenotypes().indexOf(genotype));
            workerActor.tell(calculateFitness, getSelf());
        }
        this.currentGeneration++;
        this.currentPopulation.getGenotypes().clear();

    }


    public void printSolution() {
        System.out.println("Found solution in " + this.currentGeneration + " generations");
        System.out.println("Best solution fitness: " + this.currentPopulation.getFittest().getFitness());
        System.out.println("Best solution Genotype: " + this.currentPopulation.getFittest().toString());
        System.out.println();
        System.out.println("###############################");
        System.out.println("English Premier League Schedule");
        System.out.println("###############################");
        System.out.println();
        Genotype fittest = this.currentPopulation.getFittest();
        fittest.createPhenoType(database.getTeams());
        //database.createSeasonSchedule(fittest);
        List<MatchSchedule> seasonSchedule = fittest.getPhenotype().getMatchSchedules();
        for (MatchSchedule matchSchedule : seasonSchedule) {
            int matchDayNumber = seasonSchedule.indexOf(matchSchedule) + 1;
            System.out.println("MatchDay: " + matchDayNumber);
            for (Match match : matchSchedule.getMatches()) {
                Integer[] match1 = match.getMatch();
                int matchNumber = matchSchedule.getMatches().indexOf(match) + 1;
                Team teamA = database.getTeamBasedOnId(match1[0]);
                Team teamB = database.getTeamBasedOnId(match1[1]);
                System.out.println("Match " + matchNumber + ": " + teamA.getTeamName() + " (H)" + " Vs " + teamB.getTeamName() + " (A)");

            }
            System.out.println();


        }

    }

    public static Database initializeDatabase() {
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
