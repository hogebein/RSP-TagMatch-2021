package agents;

import core.Agent;

import java.util.Random;

public class TemAgent implements Agent {
    private String name;
    public static final double EPSILON = 0.30;       // ε-greedy法のε
    public static final double ALPHA = 0.50;         // 学習率α
    public static final double GAMMA = 0.90;         // 割引率γ
    public static final int WIN_REWARD = 1;       // ゴール時の報酬
    public static final int LOSE_PENALTY = -1;    // 1ステップ経過のペナルティ
    public static int enemy_hand;
    private static int last_hand;
    public static double q[][] = {{2.611796695626926,2.583483233219517,3.2619736714093874,} ,
            {2.641246281284708,2.9701459203517384,2.0376437533837874,} ,
            {3.457878256190312,2.0126526350509564,2.903273339691354,} ,
            {2.2977585853179536,2.069232509359991,2.460234676635113,} ,
            {3.1650196331806617,2.0577060812329497,3.1326901926753994,} ,
            {2.6285838318622092,2.542574227348031,3.570532947402728,} };



    public TemAgent(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAction() {
        return eGreedy();
    }

    public static int eGreedy() {

        int selectedA = 0;

        Random rand = new Random();
        int randNum = rand.nextInt(100+1);

        if (randNum <= EPSILON * 100.0) {
            // εの確率　Q値が最大となるようなaを選択
            for (int a = 0; a < 3; a++) {
                if (q[last_hand][selectedA] < q[last_hand][a]) {
                    selectedA = a;
                }
            }
        } else {
            // (1-ε)の確率　ランダムにaを選択
            selectedA = rand.nextInt(3);
        }

        return selectedA;

    }

    public static int Action(int my_hand, int hand1, int hand2) {
        if (hand1 == hand2){
            enemy_hand = hand1;
        } else if (hand1 == 0 && hand2 == 1 || hand1 == 1 && hand2 == 0){
            enemy_hand = 3;
        } else if (hand1 == 0 && hand2 == 2 || hand1 == 2 && hand2 == 0){
            enemy_hand = 4;
        } else {
            enemy_hand = 5;
        }

        int r = 0;
        if((my_hand==0 && enemy_hand==1) || (my_hand==1 && enemy_hand==2) || (my_hand==2 && enemy_hand==0) || (my_hand==0 && enemy_hand==3) || (my_hand==2 && enemy_hand==4) || (my_hand==1 && enemy_hand==5)) {
            r += WIN_REWARD;
        }else if( (my_hand==0 && enemy_hand==2) || (my_hand==1 && enemy_hand==0) || (my_hand==2 && enemy_hand==1) || (my_hand==1 && enemy_hand==3) || (my_hand==0 && enemy_hand==4) || (my_hand==2 && enemy_hand==5)) {
            r += LOSE_PENALTY;
        }else {
            r += 0;
        }

        return r;
    }

    // Q値の更新
    public static void updateQ(int r, int a) {

        // 状態s'で行った時にQ値が最大となるような行動
        int maxA = 0;
        for (int i = 0; i < 3; i++) {
            if (q[enemy_hand][maxA] < q[enemy_hand][i]) {
                maxA = i;
            }
        }

        // Q値の更新
        q[last_hand][a] = (1.0 - ALPHA) * q[last_hand][a] + ALPHA * (r + GAMMA * q[enemy_hand][maxA]);

    }

    // 状態の更新
    public static void updateS() {
        last_hand = enemy_hand;
    }
}