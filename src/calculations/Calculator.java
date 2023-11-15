package calculations;

import data_structures.RandomHashSet;
import genome.EdgeGene;
import genome.Genome;
import genome.NodeGene;

import java.util.ArrayList;
import java.util.HashMap;

public class Calculator {
    private ArrayList<Node> inputNodes = new ArrayList<>();
    private ArrayList<Node> hiddenNodes = new ArrayList<>();
    private ArrayList<Node> outputNodes = new ArrayList<>();

    public Calculator(Genome g) {
        RandomHashSet<NodeGene> nodes = g.getNodes();
        RandomHashSet<EdgeGene> edges = g.getEdges();

        HashMap<Integer, Node> nodeHashMap = new HashMap<>();

        for (NodeGene n : nodes.getData()) {
            Node node = new Node(n.getX());
            nodeHashMap.put(n.getInnovation(), node);

            if (n.getX() <= 0.1) {
                inputNodes.add(node);
            } else if (n.getX() >= 0.9) {
                outputNodes.add(node);
            } else {
                hiddenNodes.add(node);
            }
        }

        hiddenNodes.sort(Node::compareTo);

        for (EdgeGene e : edges.getData()) {
            NodeGene from = e.getFrom();
            NodeGene to = e.getTo();

            Node fromNode = nodeHashMap.get(from.getInnovation());
            Node toNode = nodeHashMap.get(to.getInnovation());

            Edge edge = new Edge(fromNode, toNode);

            edge.setWeight(e.getWeight());
            edge.setEnabled(e.isEnabled());
            toNode.getEdges().add(edge);
        }
    }

    public double[] calculate(double... inputs) {
        if (inputs.length != inputNodes.size()) {
            throw new IllegalArgumentException("Input size does not match");
        }

        for (int i = 0; i < inputNodes.size(); i++) {
            inputNodes.get(i).setOutput(inputs[i]);
        }

        for (Node n : hiddenNodes) {
            n.forward();
        }

        double[] outputs = new double[outputNodes.size()];
        for (int i = 0; i < outputNodes.size(); i++) {
            outputNodes.get(i).forward();
            outputs[i] = outputNodes.get(i).getOutput();
        }

        return outputs;
    }
}
