package neat;

import calculations.Calculator;
import genome.Genome;

public class Client {
    private double score;
    private Genome genome;
    private Species species;

    private Calculator calculator;

    public void generateCalculator() {
        this.calculator = new Calculator(this.genome);
    }

    public double distance(Client c2) {
        return this.getGenome().distance(c2.getGenome());
    }

    public void mutate() {
        getGenome().mutate();
    }

    public double[] calculate (double... arr) {
        if (this.calculator == null) {
            this.generateCalculator();
        }

        return calculator.calculate(arr);
    }

    public Calculator getCalculator() {
        return calculator;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Genome getGenome() {
        return genome;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
    }
}
