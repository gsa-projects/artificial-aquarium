package visual;

import calculations.Node;
import genome.EdgeGene;
import genome.Genome;
import genome.NodeGene;
import neat.Client;
import neat.Neat;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.nCopies;

public class Frame extends JFrame {
    private Genome genome;
    private double[] calculated;

    public Frame(Genome genome) {
        this.genome = genome;
        this.calculated = new double[genome.getNeat().getOutputSize()];
        Arrays.fill(calculated, 0);

        setTitle("Genome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            System.out.println(e);
        }

        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        MyPanel p = new MyPanel();
        p.setBackground(Color.BLACK);
        c.add(p, BorderLayout.CENTER);

        JPanel menu = new JPanel();
        menu.setLayout(new GridLayout(1, 7));

        JButton[] buttons = {
            new JButton("Random Weight"),
            new JButton("Weight Shift"),
            new JButton("Link Mutate"),
            new JButton("Node Mutate"),
            new JButton("On/Off"),
            new JButton("Mutate"),
            new JButton("Calculate")
        };

        buttons[0].addActionListener(e -> {
            genome.mutateWeightRandom();
            p.repaint();
        });

        buttons[1].addActionListener(e -> {
            genome.mutateWeightShift();
            p.repaint();
        });

        buttons[2].addActionListener(e -> {
            genome.mutateLink();
            p.repaint();
        });

        buttons[3].addActionListener(e -> {
            genome.mutateNode();
            p.repaint();
        });

        buttons[4].addActionListener(e -> {
            genome.mutateLinkToggle();
            p.repaint();
        });

        buttons[5].addActionListener(e -> {
            genome.mutate();
            p.repaint();
        });

        buttons[6].addActionListener(e -> {
            genome.generateCalculator();
            double[] list = nCopies(genome.getNeat().getInputSize(), 1.).stream().mapToDouble(Double::doubleValue).toArray();
            this.calculated = genome.calculate(list);
            p.repaint();
        });

        for (JButton button : buttons) {
            menu.add(button);
        }

        c.add(menu, BorderLayout.NORTH);

        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    class MyPanel extends JPanel {
        @Override
        public void repaint() {
            super.repaint();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            Graphics2D g2d = (Graphics2D) g;

            g2d.setStroke(new BasicStroke(2));
            for (EdgeGene edge: genome.getEdges().getData()) {
                NodeGene from = edge.getFrom();
                NodeGene to = edge.getTo();
                int width = getWidth(), height = getHeight();

                if (!edge.isEnabled()) {
                    g2d.setColor(Color.DARK_GRAY);
                } else {
                    g2d.setColor(Color.GREEN);
                }

                g2d.drawLine((int) (from.getX() * width), (int) (from.getY() * height), (int) (to.getX() * width), (int) (to.getY() * height));
                g2d.drawString(String.format("%.2f", edge.getWeight()), (int) ((from.getX() + to.getX()) / 2 * width), (int) ((from.getY() + to.getY()) / 2 * height));
            }

            g2d.setStroke(new BasicStroke(3));
            int i = 0;
            for (NodeGene node : genome.getNodes().getData()) {
                int radius = 15;
                double x = node.getX(), y = node.getY();
                int width = getWidth(), height = getHeight();

                g2d.setColor(Color.WHITE);
                g2d.fillOval((int) (x * width) - (radius + 1), (int) (y * height) - (radius + 1), (radius + 1) * 2, (radius + 1) * 2);
                g2d.setColor(Color.BLACK);
                g2d.fillOval((int) (x * width) - radius, (int) (y * height) - radius, radius * 2, radius * 2);
                // fill
                if (node.getX() == 0.9) {
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(String.format("%.2f", calculated[i++]), (int) (node.getX() * width - radius / 2. - 5), (int) (node.getY() * height + 4));
                }
            }
        }
    }

    public static void main(String[] args) {
        Neat neat = new Neat(7, 3, 100);

//        double[] in = new double[neat.getInputSize()];
//        for (int i = 0; i < in.length; i++) {
//            in[i] = Math.random();
//        }
//
//        for (Client c : neat.getClients().getData()) {
//            c.setScore(c.calculate(in)[0]);   
//        }

        new Frame(neat.emptyGenome());
    }
}
