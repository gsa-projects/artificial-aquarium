package neat;

import calculations.Calculator;
import data_structures.RandomHashSet;
import genome.Genome;

import java.util.Comparator;

public class Species {
    private final RandomHashSet<Client> clients = new RandomHashSet<>();
    private Client representative;
    private double score;

    public Species(Client representative) {
        this.representative = representative;
        this.representative.setSpecies(this);
        this.clients.add(representative);
    }

    public boolean put(Client client) {
        if (client.distance(representative) < representative.getGenome().getNeat().getCP()) {
            client.setSpecies(this);
            clients.add(client);
            return true;
        }

        return false;
    }

    public void forcePut(Client client) {
        client.setSpecies(this);
        clients.add(client);
    }

    public void goExtinct() {
        for (Client client : this.clients.getData()) {
            client.setSpecies(null);
        }
    }

    public void evaluateScore() {
        double v = 0;
        for (Client client : this.clients.getData()) {
            v += client.getScore();
        }

        this.score = v / this.clients.size();
    }

    public void reset() {
        representative = clients.randomElement();
        for (Client client : clients.getData()) {
            client.setSpecies(null);
        }
        clients.clear();

        clients.add(representative);
        representative.setSpecies(this);
        score = 0;
    }

    public void kill(double percentage) {
        clients.getData().sort(Comparator.comparingDouble(Client::getScore));

        double amount = percentage * this.clients.size();

        for (int i = 0; i < amount; i++) {
            clients.get(0).setSpecies(null);
            clients.remove(0);
        }
    }

    public Genome breed() {
        Client c1 = clients.randomElement();
        Client c2 = clients.randomElement();

        if (c1.getScore() > c2.getScore()) {
            return Genome.crossOver(c1.getGenome(), c2.getGenome());
        } else {
            return Genome.crossOver(c2.getGenome(), c1.getGenome());
        }
    }

    public int size() {
        return clients.size();
    }

    public double getScore() {
        return score;
    }

    public Client getRepresentative() {
        return representative;
    }

    public RandomHashSet<Client> getClients() {
        return clients;
    }
}
