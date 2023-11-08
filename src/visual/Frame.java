package visual;

import genome.EdgeGene;
import genome.Genome;
import genome.NodeGene;
import neat.Neat;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private Genome genome;

    public Frame(Genome genome) {
        this.genome = genome;

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
        public void paint(Graphics g) {
            super.paint(g);

            Graphics2D g2d = (Graphics2D) g;

            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.WHITE);
            for (NodeGene node : genome.getNodes().getData()) {
                int radius = 15;
                double x = node.getX(), y = node.getY();
                int width = getWidth(), height = getHeight();

                g2d.drawOval((int) (x * width) - radius, (int) (y * height) - radius, radius * 2, radius * 2);
            }

            g2d.setStroke(new BasicStroke(2));
            for (EdgeGene edge: genome.getEdges().getData()) {
                NodeGene from = edge.getFrom();
                NodeGene to = edge.getTo();
                int width = getWidth(), height = getHeight();

                if (!edge.isEnabled()) {
                    g2d.setColor(Color.RED);
                } else {
                    g2d.setColor(Color.GREEN);
                }

                g2d.drawLine((int) (from.getX() * width), (int) (from.getY() * height), (int) (to.getX() * width), (int) (to.getY() * height));
                g2d.drawString(String.format("%.2f", edge.getWeight()), (int) ((from.getX() + to.getX()) / 2 * width), (int) ((from.getY() + to.getY()) / 2 * height));
            }
        }
    }

    public static void main(String[] args) {
        Neat neat = new Neat(4, 5, 100);
        new Frame(neat.emptyGenome());
    }
}
