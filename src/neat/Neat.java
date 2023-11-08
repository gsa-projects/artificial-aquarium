package neat;

import data_structures.RandomHashSet;
import genome.EdgeGene;
import genome.Genome;
import genome.NodeGene;

import java.util.HashMap;

public class Neat {
    public static final int MAX_NODES = (int) Math.pow(2, 20);
    private final double C1 = 1, C2 = 1, C3 = 1;
    private double WEIGHT_SHIFT_STRENGTH = 0.3;
    private double WEIGHT_RANDOM_STRENGTH = 1;
    private double PROBABILITY_MUTATE_LINK = 0.4;
    private double PROBABILITY_MUTATE_NODE = 0.4;
    private double PROBABILITY_MUTATE_WEIGHT_SHIFT = 0.4;
    private double PROBABILITY_MUTATE_WEIGHT_RANDOM = 0.4;
    private double PROBABILITY_MUTATE_TOGGLE = 0.4;

    private final HashMap<EdgeGene, EdgeGene> allEdges = new HashMap<>();
    private final RandomHashSet<NodeGene> allNodes = new RandomHashSet<>();

    private int inputSize;
    private int outputSize;
    private int maxClients;

    public Neat(int inputSize, int outputSize, int clients) {
        this.reset(inputSize, outputSize, clients);
    }

    public Genome emptyGenome() {
        Genome g = new Genome(this);

        for (int i = 0; i < inputSize + outputSize; i++) {
            g.getNodes().add(getNode(i + 1));
        }

        return g;
    }

    public void reset(int inputSize, int outputSize, int clients) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.maxClients = clients;

        allEdges.clear();
        allNodes.clear();

        for (int i = 0; i < inputSize; i++) {
            NodeGene n = getNode();
            n.setX(0.1);
            n.setY((double) (i + 1) / (inputSize + 1));
            allNodes.add(n);
        }

        for (int i = 0; i < outputSize; i++) {
            NodeGene n = getNode();
            n.setX(0.9);
            n.setY((double) (i + 1) / (outputSize + 1));
            allNodes.add(n);
        }

        // edges
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                EdgeGene e = getEdge(allNodes.get(i), allNodes.get(inputSize + j));
                e.setWeight(Math.random() * 2 - 1);
                allEdges.put(e, e);
            }
        }
    }

    // copy edge
    public static EdgeGene getEdge(EdgeGene edge) {
        EdgeGene e = new EdgeGene(edge.getFrom(), edge.getTo());
        e.setWeight(edge.getWeight());
        e.setEnabled(edge.isEnabled());
        return e;
    }

    public EdgeGene getEdge(NodeGene from, NodeGene to) {
        EdgeGene e = new EdgeGene(from, to);

        if (allEdges.containsKey(e)) {
            e.setInnovation(allEdges.get(e).getInnovation());
        } else {
            e.setInnovation(allEdges.size() + 1);
            allEdges.put(e, e);
        }

        return e;
    }

    public NodeGene getNode() {
        NodeGene n = new NodeGene(allNodes.size() + 1);
        allNodes.add(n);
        return n;
    }

    public NodeGene getNode(int id) {
        if (1 <= id && id <= allNodes.size()) {
            return allNodes.get(id - 1);
        }
        return getNode();
    }

    public double getC1() {
        return C1;
    }

    public double getC2() {
        return C2;
    }

    public double getC3() {
        return C3;
    }

    public double getWeightShiftStrength() {
        return WEIGHT_SHIFT_STRENGTH;
    }

    public double getWeightRandomStrength() {
        return WEIGHT_RANDOM_STRENGTH;
    }

    public double getPROBABILITY_MUTATE_LINK() {
        return PROBABILITY_MUTATE_LINK;
    }

    public double getPROBABILITY_MUTATE_NODE() {
        return PROBABILITY_MUTATE_NODE;
    }

    public double getPROBABILITY_MUTATE_TOGGLE() {
        return PROBABILITY_MUTATE_TOGGLE;
    }

    public double getPROBABILITY_MUTATE_WEIGHT_RANDOM() {
        return PROBABILITY_MUTATE_WEIGHT_RANDOM;
    }

    public double getPROBABILITY_MUTATE_WEIGHT_SHIFT() {
        return PROBABILITY_MUTATE_WEIGHT_SHIFT;
    }

    public int getInputSize() {
        return inputSize;
    }

    public int getOutputSize() {
        return outputSize;
    }

    public static void main(String[] args) {
        Neat neat = new Neat(3, 3, 100);

        Genome g = neat.emptyGenome();
        System.out.println(g.getNodes().size());
    }
}
