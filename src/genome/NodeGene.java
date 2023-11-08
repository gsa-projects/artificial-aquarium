package genome;

public class NodeGene extends Gene {
    private double x, y;

    public NodeGene(int innovation) {
        super(innovation);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y){
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeGene nodeGene)) return false;
        if (!super.equals(o)) return false;

        if (Double.compare(nodeGene.x, x) != 0) return false;
        return Double.compare(nodeGene.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return innovation;
    }
}
