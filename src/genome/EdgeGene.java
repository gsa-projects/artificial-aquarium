package genome;

import neat.Neat;

public class EdgeGene extends Gene {
    private NodeGene from;
    private NodeGene to;

    private double weight;
    private boolean enabled = true;

    public EdgeGene(NodeGene from, NodeGene to) {
        this.from = from;
        this.to = to;
    }

    public NodeGene getFrom() {
        return from;
    }

    public void setFrom(NodeGene from) {
        this.from = from;
    }

    public NodeGene getTo() {
        return to;
    }

    public void setTo(NodeGene to) {
        this.to = to;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EdgeGene edgeGene)) return false;
        return edgeGene.from.equals(from) && edgeGene.to.equals(to);
    }

    @Override
    public int hashCode() {
        return from.getInnovation() * Neat.MAX_NODES + to.getInnovation();
    }
}
