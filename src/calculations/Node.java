package calculations;

import java.util.ArrayList;

public class Node implements Comparable<Node> {
    private double x;
    private double output;
    private ArrayList<Edge> edges = new ArrayList<>();

    @Override
    public int compareTo(Node o) {
        if (this.x > o.x) return -1;
        else if (this.x < o.x) return 1;
        return 0;
    }

    public boolean forward() {
        double s = 0;

        for (Edge e : edges) {
            if (e.isEnabled()) {
                s += e.getWeight() * e.getFrom().getOutput();
            }
        }

        output = activateFunc(s);

        return true;
    }

    private double activateFunc(double x) {
        return 1d / (1 + Math.exp(-x));
    }

    public Node(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }
}
