//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package agents;

import core.Agent;
import java.util.ArrayList;
import java.util.Random;

public class HayashiAgent implements Agent {
    private String name;
    private int nextAction;
    private int currentRound = 0;
    private static final double THRESHOLD = 0.60;
    private double[][] probability;

    public HayashiAgent(String name) {
        this.name = name;
        this.probability = new double[3][3];
    }

    public void before(int palAction) {
        this.decideNextAction(palAction);
    }

    public void after(ArrayList<Integer> actionEpisodeA, ArrayList<Integer> actionEpisodeB) {
        double[] probabilityA = this.computeProbability(actionEpisodeA);
        double[] probabilityB = this.computeProbability(actionEpisodeB);

        for(int i = 0; i < probabilityA.length; ++i) {
            for(int j = 0; j < probabilityB.length; ++j) {
                this.probability[i][j] = probabilityA[i] * probabilityB[j];
            }
        }

        ++this.currentRound;
    }

    public String getName() {
        return this.name;
    }

    public int getAction() {
        return this.nextAction;
    }

    private double[] computeProbability(ArrayList<Integer> actionEpisode) {
        double[] probability = new double[3];
        int[] count = new int[3];

        for(int a : actionEpisode){
            switch(a) {
                case 0:
                    count[0]++;
                    break;
                case 1:
                    count[1]++;
                    break;
                case 2:
                    count[2]++;
                    break;
            }
        }

        probability[0] = (double)(count[0] / actionEpisode.size());
        probability[1] = (double)(count[1] / actionEpisode.size());
        probability[2] = (double)(count[2] / actionEpisode.size());
        return probability;
    }

    private void setDifferentAction(int palAction) {
        Random random = new Random();
        int rnd = random.nextInt(2);
        switch(palAction) {
            case 0:
                if (rnd == 0) {
                    this.nextAction = 1;
                } else {
                    this.nextAction = 2;
                }
                break;
            case 1:
                if (rnd == 0) {
                    this.nextAction = 0;
                } else {
                    this.nextAction = 2;
                }
                break;
            case 2:
                if (rnd == 0) {
                    this.nextAction = 0;
                } else {
                    this.nextAction = 1;
                }
        }

    }

    private void decideNextAction(int palAction) {
        if (this.currentRound < 10) {
            this.setDifferentAction(palAction);
        } else {
            double totalProbability = 0.0D;

            for(int i = 0; i < 3; ++i) {
                for(int j = 0; j < 3; ++j) {
                    if (this.RSP(palAction, i) || this.RSP(palAction, j)) {
                        totalProbability += this.probability[i][j];
                    }
                }
            }

            if (totalProbability > 0.6D) {
                this.nextAction = palAction;
            } else {
                this.setDifferentAction(palAction);
            }

        }
    }

    public boolean RSP(int a, int b) {
        return a == 0 && b == 1 || a == 1 && b == 2 || a == 2 && b == 0;
    }
}
