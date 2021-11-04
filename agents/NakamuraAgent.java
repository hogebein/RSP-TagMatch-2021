package agents;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import core.Agent;

public class NakamuraAgent implements Agent {

    private String name;

    static final double EPSILON = 0.1;
    static final double ALPHA = 0.3;
    static final double GAMMA = 0.8;

    // Q値
    private double[][] q;

    // 敵が出す手の組数
    static final int N_ACTIONS = 9;
    // 過去何手まで考慮するか
    static final int HISTORY_LENGTH = 3;
    // 過去の手を格納(n進数 n:N_ACTIONS 最下位桁が最新の手)
    private int actionHistory;

    // 過去の記録を残す割合
    static final double REMAIN_RATE = 0.9;
    // 過去の手ごとにその次の手をカウント
    private double[][] statistics;
    // 予測する手の数(高い順)
    static final int N_PREDICTS = 1;
    // 予測した相手の手
    private int predict;


    public NakamuraAgent(String name) {
        this.name = name;

        this.init();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAction() {
        return this.decideAction(this.predictOpponentAction());
    }


    public void init() {
        // Q値の初期化
        this.q = new double[(int)Math.pow(N_ACTIONS, N_PREDICTS)][3];
        for(int i = 0; i < this.q.length; i++) {
            for(int j = 0; j < this.q[0].length; j++) {
                this.q[i][j] = 0;
            }
        }

        // 過去の手・予測を初期化
        this.actionHistory = 0;
        this.statistics = new double[(int)Math.pow(N_ACTIONS, HISTORY_LENGTH)][N_ACTIONS];
        for(int i = 0; i < this.statistics.length; i++) {
            for(int j = 0; j < this.statistics[0].length; j++) {
                this.statistics[i][j] = 1.0 / N_ACTIONS;
            }
        }
        this.predict = 0;

    }

    // 事後処理
    public void after(HashMap<String, Integer> ourActions, HashMap<String, Integer> opponentActions, HashMap<String, Integer> wins) {
        int myAction = ourActions.get(this.name);

        int nextState = 0;
        int i = 0;
        for(HashMap.Entry<String, Integer> s : opponentActions.entrySet()) {
            nextState += Math.pow(3, i) * s.getValue();
            i++;
        }
        
        int reward = 0;
        if(wins.get(this.name) == 1) {
            reward = 1;
        } else if(wins.containsValue(1)) {
            reward = -10;
        }
        
        this.updateStatistics(nextState);
        this.actionHistory = (this.actionHistory * N_ACTIONS) % (int)Math.pow(N_ACTIONS, HISTORY_LENGTH);
        this.actionHistory += nextState;
        
        boolean isUpdate = false;
        int tmp = this.predict;
        for(int j = 0; j < N_PREDICTS; j++) {
            isUpdate |= tmp % N_ACTIONS == nextState;
            tmp /= N_ACTIONS;
        }

        int nextPredict = this.predictOpponentAction();
        if(isUpdate) this.updateQ(this.predict, myAction, nextPredict, reward);
        this.predict = nextPredict;
    }

    // Q値を更新
    private void updateQ(int state, int action, int nextState, int reward) {
        this.q[state][action] += 
            ALPHA * (
                reward
                + GAMMA * this.q[nextState][this.getMaxQValue(nextState)]
                - this.q[state][action]
            );
    }

    // 次の手を決定
    private int decideAction(int state) {
        Random random = new Random();

        if(random.nextDouble() >= EPSILON) {
            return this.getMaxQValue(state);
        } else {
            return random.nextInt(this.q[state].length);
        }
    }

    // 最大のQ値を求める
    private int getMaxQValue(int state) {
        int maxIndex = 0;

        for(int i = 0; i < this.q[state].length; i++) {
            if(this.q[state][maxIndex] < this.q[state][i]) {
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    // 統計を更新
    private void updateStatistics(int nextState) {
        for(int i = 0; i < this.statistics[this.actionHistory].length; i++) {
            this.statistics[this.actionHistory][i] *= REMAIN_RATE;
        }
        this.statistics[this.actionHistory][nextState] += 1.0 - REMAIN_RATE;
    }

    // 相手の手を予測
    private int predictOpponentAction() {
        double[] tmp = this.statistics[this.actionHistory].clone();
        Arrays.sort(tmp);

        int[] maxIndexes = new int[N_PREDICTS];
        for(int i = 0; i < maxIndexes.length; i++) {
            maxIndexes[i] = -1;
        }
        for(int i = 0; i < maxIndexes.length; i++) {
            int j = 0;
            while(this.statistics[this.actionHistory][j] != tmp[tmp.length - i - 1] || this.contains(maxIndexes, j)) j++;
            maxIndexes[i] = j;
        }

        int predicts = 0;
        for(int i = 0; i < maxIndexes.length; i++) {
            predicts += Math.pow(this.statistics[this.actionHistory].length, i) * maxIndexes[i];
        }
        return predicts;
    }

    private boolean contains(int[] array, int value) {
        boolean result = false;
        for(int i = 0; i < array.length; i++) {
            result |= array[i] == value;
        }
        return result;
    }

}
