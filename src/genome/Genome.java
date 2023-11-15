package genome;

import calculations.Calculator;
import data_structures.RandomHashSet;
import neat.Neat;

public class Genome {
    private final RandomHashSet<EdgeGene> edges = new RandomHashSet<>();
    private final RandomHashSet<NodeGene> nodes = new RandomHashSet<>();
    private final Neat neat;
    private Calculator calculator;

    public Genome(Neat neat) {
        this.neat = neat;
    }

    public void generateCalculator() {
        this.calculator = new Calculator(this);
    }

    public double[] calculate (double... arr) {
        if (this.calculator != null) {
            return calculator.calculate(arr);
        }

        return null;
    }

    public double distance(Genome g2) {
        Genome g1 = this;

        int highestInnovation1 = 0;
        if (g1.getEdges().size() != 0)
            highestInnovation1 = g1.getEdges().get(g1.getEdges().size() - 1).getInnovation();

        int highestInnovation2 = 0;
        if (g2.getEdges().size() != 0)
            highestInnovation2 = g2.getEdges().get(g2.getEdges().size() - 1).getInnovation();

        // should be g1.innovation > g2.innovation
        if (highestInnovation1 < highestInnovation2) {
            Genome temp = g1;
            g1 = g2;
            g2 = temp;
        }

        int index1 = 0, index2 = 0;

        int D = 0;      // disjoint
        int E = 0;      // excess
        double W = 0;   // weight diff
        int similar = 0;

        while (index1 < g1.getEdges().size() && index2 < g2.getEdges().size()) {
            EdgeGene e1 = g1.getEdges().get(index1);
            EdgeGene e2 = g2.getEdges().get(index2);

            if (e1.getInnovation() == e2.getInnovation()) {
                similar++;
                W += Math.abs(e1.getWeight() - e2.getWeight());
                index1++;
                index2++;
            } else if (e1.getInnovation() > e2.getInnovation()) {
                D++;
                index2++;
            } else {
                D++;
                index1++;
            }
        }

        W /= similar;
        E = g1.getEdges().size() - index1;

        double c1 = neat.getC1(), c2 = neat.getC2(), c3 = neat.getC3();
        double N = Math.max(g1.getEdges().size(), g2.getEdges().size());
        if (N < 20) N = 1;

        return (c1 * E / N) + (c2 * D / N) + (c3 * W);
    }

    public static Genome crossOver(Genome g1, Genome g2) {
        Neat neat = g1.getNeat();
        Genome genome = neat.emptyGenome();

        int index1 = 0, index2 = 0;

        while (index1 < g1.getEdges().size() && index2 < g2.getEdges().size()) {
            EdgeGene e1 = g1.getEdges().get(index1);
            EdgeGene e2 = g2.getEdges().get(index2);

            if (e1.getInnovation() == e2.getInnovation()) {
                if (Math.random() > 0.5) {
                    genome.getEdges().add(Neat.getEdge(e1));
                } else {
                    genome.getEdges().add(Neat.getEdge(e2));
                }
                index1++;
                index2++;
            } else if (e1.getInnovation() > e2.getInnovation()) {
                genome.getEdges().add(Neat.getEdge(e2));
                index2++;
            } else {
                genome.getEdges().add(Neat.getEdge(e1));
                index1++;
            }
        }

        while (index1 < g1.getEdges().size()) {
            EdgeGene e1 = g1.getEdges().get(index1);
            genome.getEdges().add(Neat.getEdge(e1));
            index1++;
        }

        for (EdgeGene e : genome.getEdges().getData()) {
            genome.getNodes().add(e.getFrom());
            genome.getNodes().add(e.getTo());
        }

        return genome;
    }

    public void mutate() {
        if (Math.random() < neat.getPROBABILITY_MUTATE_LINK()) {
            mutateLink();
        }

        if (Math.random() < neat.getPROBABILITY_MUTATE_NODE()) {
            mutateNode();
        }

        if (Math.random() < neat.getPROBABILITY_MUTATE_WEIGHT_SHIFT()) {
            mutateWeightShift();
        }

        if (Math.random() < neat.getPROBABILITY_MUTATE_WEIGHT_RANDOM()) {
            mutateWeightRandom();
        }

        if (Math.random() < neat.getPROBABILITY_MUTATE_TOGGLE()) {
            mutateLinkToggle();
        }
    }

    public void mutateLink() {
        for (int i = 0; i < 100; i++) {
            NodeGene a = nodes.randomElement();
            NodeGene b = nodes.randomElement();

            if (a.getX() == b.getX())
                continue;

            EdgeGene e;
            if (a.getX() < b.getX()) {
                e = new EdgeGene(a, b);
            } else {
                e = new EdgeGene(b, a);
            }

            if (edges.contains(e))
                continue;

            e = neat.getEdge(e.getFrom(), e.getTo());
            e.setWeight(e.getWeight() + (Math.random() * 2 - 1) * neat.getWeightShiftStrength());

            edges.addSorted(e);
        }
    }

    public void mutateNode() {
        EdgeGene e = edges.randomElement();

        if (e == null) return;

        NodeGene from = e.getFrom();
        NodeGene to = e.getTo();
        NodeGene mid = neat.getNode();
        mid.setX((from.getX() + to.getX()) / 2);
        mid.setY((from.getY() + to.getY()) / 2 + Math.random() * 0.1 - 0.05);

        EdgeGene e1 = neat.getEdge(from, mid);
        EdgeGene e2 = neat.getEdge(mid, to);

        e1.setWeight(1);
        e2.setWeight(e.getWeight());
        e2.setEnabled(e.isEnabled());

        edges.remove(e);
        edges.add(e1);
        edges.add(e2);
        nodes.add(mid);
    }

    public void mutateWeightShift() {
        EdgeGene e = edges.randomElement();

        if (e != null) {
            e.setWeight(e.getWeight() + (Math.random() * 2 - 1) * neat.getWeightShiftStrength());
        }
    }

    public void mutateWeightRandom() {
        EdgeGene e = edges.randomElement();

        if (e != null) {
            e.setWeight((Math.random() * 2 - 1) * neat.getWeightRandomStrength());
        }
    }

    public void mutateLinkToggle() {
        EdgeGene e = edges.randomElement();

        if (e != null) {
            e.setEnabled(!e.isEnabled());
        }
    }

    public RandomHashSet<EdgeGene> getEdges() {
        return edges;
    }

    public RandomHashSet<NodeGene> getNodes() {
        return nodes;
    }

    public Neat getNeat() {
        return neat;
    }
}
