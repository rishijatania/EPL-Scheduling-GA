# INFO6205_524 - English Premier League Match Scheduling Using Genetic Algorithm 

## What is Genetic Algorithm?
The term “Genetic Algorithm” refers to a specific algorithm implemented in a specific way to solve specific sorts of problems. [Daniel Shiffman, Nature of Code – Chapter 9]
This algorithm is inspired by process of “natural selection” which basically belongs to the larger class of evolutionary algorithms (EA).
These algorithms are used to generate high quality solutions to optimize and search problems by relying on bio-inspired operations such as “crossover”, “mutation”, “selection” etc.
John Holland introduced Genetic Algorithm (GA) in 1960 based on the concept of Darwin’s theory of evolution

## Why to use Genetic Algorithm?
Because they are simple to program!
Proven to find the global optimum if given enough time.
Can be applied to diverse problem domains, etc. 
Can be used when we know what’s a good solution but we can’t figure out the road to that solution. It provides a good way to search and traverse the space of possible solutions in a smart way.
Along with easy implementation, it is easy to trace the performance.

## Problem Statement
Find an optimal match schedule for a set of teams over a period of time that satisfy a the below mentioned set of constraints
* No team should play on in the same consecutive week
* Each team should have played exactly 2 matches with every other teams
* Each team should have played one match on their home ground and one on away ground with each opponent

## Running the application locally

In-order to run the application. 
Download the source code from https://github.com/anubhav1011/INFO6205_524
Have Maven and Java pre-installed

Run the below command to install all the dependencies.
```shell
mvn clean install
```
One approach is to Execute the `main` method in the `edu.neu.psa.GA.EplGAParallel` class from your IDE.

Another approach is to run the jar file under the target folder.
```shell
java -jar
```

## References
Daniel Shiffman, “Nature of Code”, Chapter 09
Wikipedia
Vijini Mallawaarachchi’s article on “Introduction To Genetic Algorithms” featured at www.towardsdatascience.com
Paper on “A NEW APPROACH FOR DATA ENCRYPTION USING GENETIC ALGORITHMS” featured at www.researchgate.net
YouTube - A video explaining the workflow of a scheduling algorithm.
https://www.youtube.com/watch?v=cn1JyZvV5YA&list=PLSM8fkP9ppPruxyt1r1nWJxaxWUX3yabg&index=3